/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOTable.java  
  _## 
  _##  Copyright (C) 2005-2014  Frank Fock (SNMP4J.org)
  _##  
  _##  Licensed under the Apache License, Version 2.0 (the "License");
  _##  you may not use this file except in compliance with the License.
  _##  You may obtain a copy of the License at
  _##  
  _##      http://www.apache.org/licenses/LICENSE-2.0
  _##  
  _##  Unless required by applicable law or agreed to in writing, software
  _##  distributed under the License is distributed on an "AS IS" BASIS,
  _##  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  _##  See the License for the specific language governing permissions and
  _##  limitations under the License.
  _##  
  _##########################################################################*/


package org.snmp4j.agent.mo;

import java.io.*;
import java.util.*;

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.io.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.agent.util.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;

/**
 * The <code>DefaultMOTable</code> class is the default implementation of the
 * {@link MOTable} class. For most use cases, it is not necessary to customize
 * this class through deriving your own sub-class. Instead, using a different
 * {@link MOTableModel} as table data provider is sufficient.
 * <p>
 * The default table model can be used to hold the data of a SNMP conceptual
 * table as real tabular data. If you want to implement a virtual table, you
 * will have to directly implement the interfaces {@link MOTableModel} or
 * {@link MOMutableTableModel} to access the data based on the actual view.
 *
 * @author Frank Fock
 * @version 2.2
 */
public class DefaultMOTable<R extends MOTableRow, C extends MOColumn, M extends MOTableModel<R>>
    implements MOTable<R,C,M>, MOScope, SerializableManagedObject {

  private static LogAdapter logger =
      LogFactory.getLogger(DefaultMOTable.class);

  private OID oid;
  private MOTableIndex indexDef;
  private C[] columns;
  protected M model;

  private boolean isVolatile;

  protected WeakHashMap<Request, Map<OID, R>> newRows;
  protected WeakHashMap<Request, Map<OID, ChangeSet>> pendingChanges;

  protected transient Vector<MOChangeListener> moChangeListeners;
  protected transient Vector<MOTableRowListener<R>> moTableRowListeners;

  private transient WeakHashMap<Request, RowCacheEntry> walkCache;

  private static Comparator columnComparator = new Comparator() {

    public int compare(Object o1, Object o2) {
      int id1 = (o1 instanceof MOColumn) ?
          ((MOColumn)o1).getColumnID() : (Integer) o1;
      int id2 = (o2 instanceof MOColumn) ?
          ((MOColumn)o2).getColumnID() : (Integer) o2;
      return id1 - id2;
    }
  };

  @SuppressWarnings("unchecked")
  public DefaultMOTable(OID oid, MOTableIndex indexDef, C[] columns) {
    this(oid, indexDef, columns, (M)new DefaultMOMutableTableModel<R>());
  }

  /**
   * Creates a new SNMP table with the specified "Entry" OID, INDEX, columns,
   * and {@link MOTableModel}.
   *
   * @param oid
   *    the OID of the SNMP table's Entry object. If the table is ifTable
   *    (1.3.6.1.2.1.2.2) then the OID to provide is  1.3.6.1.2.1.2.2.1
   *    which is the ifEntry OID. By SMI rule, you can simply append ".1" to
   *    the table OID.
   * @param indexDef
   *    the index definition of the table based on the INDEX clause of the
   *    table MIB definition.
   * @param columns
   *    the column definitions which may also include non-accessible columns.
   * @param model
   *    the table model holding the table data.
   */
  public DefaultMOTable(OID oid, MOTableIndex indexDef,
                        C[] columns, M model) {
    this.oid = oid;
    this.indexDef = indexDef;
    this.columns = columns;
    this.model = model;
    registerColumns();
  }

  private void registerColumns() {
    for (MOColumn<?> column : columns) {
      column.setTable(this);
    }
  }

  public MOTableCellInfo getCellInfo(OID oid) {
    return new CellInfo(this, oid);
  }

  @SuppressWarnings("unchecked")
  public int getColumnIndex(int id) {
    return Arrays.binarySearch(columns, id, columnComparator);
  }

  public C getColumn(int index) {
    return columns[index];
  }

  public int getColumnCount() {
    return columns.length;
  }

  /**
   * Creates a new row for this table with the supplied index and initial
   * values. If the underlying table model is not a {@link MOMutableTableModel}
   * instance or if one of the {@link MOTableRowListener} deny the row creation
   * attempt then <code>null</code> will be returned.
   * @param index
   *    the index OID of the new row.
   * @param initialValues
   *    the initial values that should be assigned to the new row. If the array
   *    contains less values than this table has columns, default values will
   *    be created for the missing columns.
   * @return
   *    the created <code>MOTableRow</code> instance or <code>null</code> if
   *    the row cannot be created.
   */
  @SuppressWarnings("unchecked")
  public R createRow(OID index, Variable[] initialValues) {
    if (model instanceof MOMutableTableModel) {
      Variable[] values = initialValues;
      if (values.length < getColumnCount()) {
        values = getDefaultValues();
        System.arraycopy(initialValues, 0, values, 0, initialValues.length);
      }
      R row = ((MOMutableTableModel<R>)model).createRow(index, values);
      MOTableRowEvent<R> rowEvent =
          new MOTableRowEvent<R>(this, this, row, MOTableRowEvent.CREATE, true);
      fireRowChanged(rowEvent);
      if (rowEvent.getVetoStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
        return row;
      }
    }
    return null;
  }

  @Override
  public R addNewRow(OID index, Variable[] initialValues) {
    R newRow = createRow(index, initialValues);
    if (newRow != null) {
      addRow(newRow);
    }
    return newRow;
  }

  public R createRow(OID index) {
    return createRow(index, getDefaultValues());
  }

  /**
   * Adds the supplied row to the underlying table model and fires the
   * appropriate {@link MOTableRowEvent}. Since this method is typically
   * called during the commit phase of a SET request that creates a table,
   * it should be avoided to return an error here. Instead, error checking
   * should be placed in the {@link #prepare} method.
   * @param row
   *    the <code>MOTableRow</code> to add.
   * @return
   *    <code>true</code> if the row has been added or <code>false</code>
   *    if it could not be added.
   */
  @SuppressWarnings("unchecked")
  public boolean addRow(R row) {
    if (model instanceof MOMutableTableModel) {
      MOTableRowEvent<R> rowEvent =
          new MOTableRowEvent<R>(this, this, row, MOTableRowEvent.ADD, true);
      fireRowChanged(rowEvent);
      if (rowEvent.getVetoStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
        ((MOMutableTableModel<R>)model).addRow(row);
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public R removeRow(OID index) {
    if (model instanceof MOMutableTableModel) {
      R row = model.getRow(index);
      if (row == null) {
        return null;
      }
      MOTableRowEvent<R> rowEvent =
          new MOTableRowEvent<R>(this, this, row, MOTableRowEvent.DELETE, true);
      fireRowChanged(rowEvent);
      if (rowEvent.getVetoStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
        return ((MOMutableTableModel<R>)model).removeRow(index);
      }
    }
    return null;
  }

  /**
   * Removes all rows from this table. Before a row is removed the
   * corresponding DELETE event is fired and listeners may veto these
   * events for all rows. Only if there is no veto, a row will be deleted.
   * The number of deleted rows is then returned.
   * @return
   *    the number of removed rows or -1 if the table model does not support
   *    row removal.
   */
  public int removeAll() {
    int count = 0;
    if (model instanceof MOMutableTableModel) {
      while (model.getRowCount() > 0) {
        R row = model.firstRow();
        if (row != null) {
          MOTableRowEvent<R> rowEvent =
              new MOTableRowEvent<R>(this, this, row, MOTableRowEvent.DELETE, true);
          fireRowChanged(rowEvent);
          if (rowEvent.getVetoStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
            ((MOMutableTableModel) model).removeRow(row.getIndex());
          }
          count++;
        }
      }
    }
    else {
      count = -1;
    }
    return count;
  }

  public void commit(SubRequest request) {
    OID cellOID = request.getVariableBinding().getOid();
    MOTableCellInfo cell = getCellInfo(cellOID);
    MOMutableColumn col = (MOMutableColumn) getColumn(cell.getColumn());
    if (logger.isDebugEnabled()) {
      logger.debug("Committing sub-request ("+
                   request.getVariableBinding()+") for column: "+col);
    }
    // Make sure changes are atomic -> sync whole table model
    synchronized (model) {
      R row;
      if (hasNewRows(request.getRequest())) {
        row = getNewRows(request.getRequest()).get(cell.getIndex());
        // check if row has been added already
        if (!model.containsRow(row.getIndex())) {
          if (!addRow(row)) {
            request.setErrorStatus(PDU.resourceUnavailable);
            return;
          }
        }
      }
      else {
        row = model.getRow(cell.getIndex());
      }
      Variable oldValue = null;
      if (moChangeListeners != null) {
        oldValue = row.getValue(cell.getColumn());
        MOChangeEvent changeEvent =
            new MOChangeEvent(this, new CellProxy(cell),
                              cell.getCellOID(),
                              oldValue,
                              request.getVariableBinding().getVariable(),
                              false);
        fireBeforeMOChange(changeEvent);
      }
      ChangeSet changeSet = getPendingChangeSet(request, cell.getIndex());
      // commit
      col.commit(request, row, changeSet, cell.getColumn());
      if (moChangeListeners != null) {
        MOChangeEvent changeEvent =
            new MOChangeEvent(this, new CellProxy(cell),
                              cell.getCellOID(),
                              oldValue,
                              request.getVariableBinding().getVariable(),
                              false);
        fireAfterMOChange(changeEvent);
      }
      if (isChangeSetComplete(request, cell.getIndex(), cell.getColumn())) {
        if (row instanceof MOMutableRow2PC) {
          ((MOMutableRow2PC) row).commitRow(request, changeSet);
        }
        if (moTableRowListeners != null) {
          MOTableRowEvent<R> rowEvent =
              new MOTableRowEvent<R>(this, this, row, MOTableRowEvent.UPDATED);
          fireRowChanged(rowEvent);
        }
      }
    }
  }

  public final OID getIndexPart(OID anyOID) {
    int offset = oid.size()+1;
    if ((anyOID.size() <= offset) || (!anyOID.startsWith(oid))) {
      return null;
    }
    return new OID(anyOID.getValue(), offset, anyOID.size() - offset);
  }

  public OID getCellOID(OID index, int col) {
    OID retval = new OID(oid);
    retval.append(columns[col].getColumnID());
    retval.append(index);
    return retval;
  }

  private MOTableCellInfo getNextCell(int col,
                                      OID indexLowerBound,
                                      boolean isLowerBoundIncluded) {
    for (int i=col; i<columns.length; i++) {
      Iterator<R> it = model.tailIterator(indexLowerBound);
      if (!it.hasNext()) {
        if (indexLowerBound == null) {
          return null;
        }
        indexLowerBound = null;
        isLowerBoundIncluded = true;
      }
      else {
        if ((indexLowerBound != null) && (!isLowerBoundIncluded)) {
          MOTableRow row = it.next();
          if (row.getIndex().compareTo(indexLowerBound) > 0) {
            // the specified index does not exists so we can use this next one:
            return new CellInfo(this, row.getIndex(), i, columns[i].getColumnID(),
                                row);
          }
        }
        indexLowerBound = null;
        isLowerBoundIncluded = true;
        if (it.hasNext()) {
          MOTableRow row = it.next();
          if (row == null) {
            continue;
          }
          return new CellInfo(this, row.getIndex(), i, columns[i].getColumnID(), row);
        }
      }
    }
    return null;
  }

  public OID find(MOScope range) {
    MOTableCellInfo cellInfo = findCell(range, null);
    if (cellInfo != null) {
      return cellInfo.getCellOID();
    }
    return null;
  }

  protected MOTableCellInfo findCell(MOScope range, SubRequest request) {
    synchronized (model) {
      update(range, request);
      // determine column
      if (model.isEmpty()) {
        return null;
      }
      MOTableCellInfo cellInfo = getCellInfo(range.getLowerBound());
      int col = cellInfo.getColumn();
      boolean exactMatch = true;
      if (col < 0) {
        col = (-col) - 1;
        exactMatch = false;
      }
      if (col >= columns.length) {
        return null;
      }
      boolean lowerIncluded = (!exactMatch) || range.isLowerIncluded();
      RowCacheEntry rowEntry = null;
      if (request != null) {
        rowEntry = getWalkCacheEntry(request, cellInfo, lowerIncluded);
      }
      MOTableCellInfo next;
      if (rowEntry != null) {
        next = new CellInfo(this, rowEntry.row.getIndex(),
                            col, cellInfo.getColumnID(), rowEntry.row);
      }
      else {
        next = getNextCell(col, cellInfo.getIndex(), lowerIncluded);
        if ((request != null) && (next != null) && (next.getColumn() == col)) {
          addWalkCacheEntry(request, cellInfo.getIndex(), lowerIncluded,
                            ((CellInfo)next).row);
        }
      }
      if (next != null) {
        OID cellOID = next.getCellOID();
        if (range.isCovered(new OIDScope(cellOID))) {
          return next;
        }
      }
    }
    return null;
  }

  private void addWalkCacheEntry(SubRequest request,
                                 OID lowerBound,
                                 boolean lowerIncluded,
                                 MOTableRow row) {
    if (walkCache == null) {
      walkCache = new WeakHashMap<Request, RowCacheEntry>(4);
    }
    walkCache.put(request.getRequest(),
                  new RowCacheEntry(row, lowerBound, lowerIncluded));
  }

  private RowCacheEntry getWalkCacheEntry(SubRequest request,
                                          MOTableCellInfo cellInfo,
                                          boolean lowerIncluded) {
    if (walkCache != null) {
      RowCacheEntry entry = walkCache.get(request.getRequest());
      if (entry == null) {
        return null;
      }
      if (((entry.searchLowerBound == null) && (cellInfo.getIndex() == null)) ||
          ((entry.searchLowerBound != null) &&
           (entry.searchLowerBound.equals(cellInfo.getIndex())) &&
           (lowerIncluded == entry.searchLowerBoundIncluded))) {
        return entry;
      }
    }
    return null;
  }

  public MOScope getScope() {
    return this;
  }

  public Variable getValue(OID cellOID) {
    MOTableCellInfo cell = getCellInfo(cellOID);
    if ((cell.getIndex() != null) &&
        (cell.getColumn() >= 0) && (cell.getColumn() < columns.length)) {
      return getValue(cell.getIndex(), cell.getColumn());
    }
    return null;
  }

  public Variable getValue(OID index, int col) {
    MOTableRow row = model.getRow(index);
    return getValue(row, col);
  }

  @SuppressWarnings("unchecked")
  protected Variable getValue(MOTableRow row, int col) {
    if ((row != null) && (col >= 0) && (col < row.size())) {
      return columns[col].getValue(row, col);
    }
    return null;
  }

  /**
   * Update the content of this table that is covered by the supplied
   * scope.
   * <p>
   * This method is part of the {@link UpdatableManagedObject} interface.
   * Although the {@link DefaultMOTable} does not implement that interface,
   * subclasses of this class may do so easily by overriding this hook-up
   * method.
   *
   * @param updateScope
   *    the scope to update. If <code>null</code> the whole managed object is
   *    updated.
   * @since 1.2
   * @see #update(MOScope range, SubRequest request)
   */
  public void update(MOScope updateScope) {
    // nothing to do by default -> overwrite to update an updatable table.
  }

  /**
   * Update this table for the supplied search range and sub-request if it has
   * not yet been updated for that request.
   * <p>
   * By default, the
   * {@link #update(MOScope updateScope)} is being called on behalf of this
   * method call (which itself does not nothing by default). You may choose
   * either to implement the {@link UpdatableManagedObject} interface and
   * implement its interface in a subclass. Then it is recommended to overwrite
   * this method by an empty method. Otherwise, do not implement the
   * {@link UpdatableManagedObject} interface.
   * </p>
   *
   * @param range
   *    the search range.
   * @param request
   *    the sub-request triggered the update or <code>null</code> if that
   *    request cannot be determined.
   */
  protected void update(MOScope range, SubRequest request) {
    Object updateMarker = null;
    if ((request != null) && (request.getRequest() != null)) {
        updateMarker = request.getRequest().getProcessingUserObject(getOID());
    }
    if (updateMarker == null)  {
      if ((request != null) && (request.getRequest() != null)) {
        request.getRequest().setProcessingUserObject(getOID(), new Object());
      }
      update(range);
    }
  }

  public void get(SubRequest request) {
    OID cellOID = request.getVariableBinding().getOid();
    MOTableCellInfo cell = getCellInfo(cellOID);
    if ((cell.getIndex() != null) &&
        (cell.getColumn() >= 0) && (cell.getColumn() < columns.length)) {
      // update the table part affected by this query
      update(request.getScope(), request);

      MOColumn col = getColumn(cell.getColumn());
      MOTableRow row = model.getRow(cell.getIndex());
      if (row == null) {
        request.getVariableBinding().setVariable(Null.noSuchInstance);
      }
      else if (col != null) {
        col.get(request, row, cell.getColumn());
      }
      else {
        request.getStatus().setErrorStatus(PDU.noAccess);
      }
    }
    else {
      if (cell.getColumn() >= 0) {
        request.getVariableBinding().setVariable(Null.noSuchInstance);
      }
      else {
        request.getVariableBinding().setVariable(Null.noSuchObject);
      }
    }
    request.completed();
  }

  public boolean next(SubRequest request) {
    DefaultMOScope scope = new DefaultMOScope(request.getScope());
    MOTableCellInfo nextCell;
    while ((nextCell = findCell(scope, request)) != null) {
      if (columns[nextCell.getColumn()].getAccess().isAccessibleForRead()) {
        Variable value;
        // Use row instance from cell info as shortcut if available
        if ((nextCell instanceof CellInfo) &&
            (((CellInfo)nextCell).getRow() != null)) {
          value = getValue(((CellInfo)nextCell).getRow(), nextCell.getColumn());
        }
        else {
          value = getValue(nextCell.getIndex(), nextCell.getColumn());
        }
        if (value == null) {
          scope.setLowerBound(nextCell.getCellOID());
          scope.setLowerIncluded(false);
        }
        else {
          request.getVariableBinding().setOid(nextCell.getCellOID());
          request.getVariableBinding().setVariable(value);
          request.completed();
          return true;
        }
      }
      else {
        if (nextCell.getColumn()+1 < getColumnCount()) {
          OID nextColOID = new OID(getOID());
          nextColOID.append(columns[nextCell.getColumn()+1].getColumnID());
          scope.setLowerBound(nextColOID);
          scope.setLowerIncluded(false);
        }
        else {
          return false;
        }
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public void prepare(SubRequest request) {
    OID cellOID = request.getVariableBinding().getOid();
    MOTableCellInfo cell = getCellInfo(cellOID);
    if (cell.getIndex() == null) {
      request.getStatus().setErrorStatus(PDU.inconsistentName);
      return;
    }
    if ((cell.getColumn() >= 0) && (cell.getColumn() < columns.length)) {
      MOColumn col = getColumn(cell.getColumn());
      if (logger.isDebugEnabled()) {
        logger.debug("Preparing sub-request ("+request.getVariableBinding()+")"+
                     " for column: "+col);
      }
      if ((col instanceof MOMutableColumn) &&
          (col.getAccess().isAccessibleForWrite())) {
        MOMutableColumn mcol = (MOMutableColumn)col;
        // check index
        if (getIndexDef().isValidIndex(cell.getIndex())) {
          R row = model.getRow(cell.getIndex());
          boolean newRow = false;
          if (row == null) {
            // look for already prepared row
            row = getNewRows(request.getRequest()).get(cell.getIndex());
            newRow = true;
          }
          if (row != null) {
            prepare(request, cell, mcol, row, newRow);
            request.completed();
          }
          else if (model instanceof MOMutableTableModel) {
            if (logger.isDebugEnabled()) {
              logger.debug("Trying to create new row '"+cell.getIndex()+"'");
            }
            MOMutableTableModel<R> mmodel = (MOMutableTableModel<R>)model;
            // create new row
            try {
              row = createRow(request, cell, mmodel);
              if (row == null) {
                request.getStatus().setErrorStatus(PDU.noCreation);
              }
              else {
                prepare(request, cell, mcol, row, true);
                request.completed();
              }
            }
            catch (UnsupportedOperationException ex) {
              request.getStatus().setErrorStatus(PDU.noCreation);
            }
          }
          else {
            request.getStatus().setErrorStatus(PDU.noCreation);
          }
        }
        else {
          // invalid index
          if (logger.isDebugEnabled()) {
            logger.debug("Invalid index '"+cell.getIndex()+
                         "' for row creation in table "+getID());
          }
          request.getStatus().setErrorStatus(PDU.noCreation);
        }
      }
      else {
        // read-only column
        request.getStatus().setErrorStatus(PDU.notWritable);
      }
    }
    else {
      request.getStatus().setErrorStatus(PDU.noCreation);
    }
  }

  private R createRow(SubRequest request, MOTableCellInfo cell,
                      MOMutableTableModel<R> mmodel)
      throws UnsupportedOperationException
  {
    MOColumn col = getColumn(cell.getColumn());
    if (!col.getAccess().isAccessibleForCreate()) {
      // creation not allowed
      return null;
    }
    Variable[] initialValues = new Variable[getColumnCount()];
    getChangesFromRequest(cell.getIndex(), null, request,
                          initialValues, true, true);
    R row = mmodel.createRow(cell.getIndex(), initialValues);
    getNewRows(request.getRequest()).put(row.getIndex(), row);
    return row;
  }

  private void prepare(SubRequest request, MOTableCellInfo cell,
                       MOMutableColumn mcol, R row, boolean creation) {
    if (moChangeListeners != null) {
      MOChangeEvent changeEvent =
          new MOChangeEvent(this, new CellProxy(cell),
                            cell.getCellOID(),
                            (creation) ? null : row.getValue(cell.getColumn()),
                            request.getVariableBinding().getVariable(),
                            true);
      fireBeforePrepareMOChange(changeEvent);
      if (changeEvent.getDenyReason() != PDU.noError) {
        request.getStatus().setErrorStatus(changeEvent.getDenyReason());
      }
    }
    ChangeSet changeSet = getPendingChangeSet(request, cell.getIndex());
    if (changeSet == null) {
      changeSet = addPendingChanges(request, row, creation);
    }
    if ((moTableRowListeners != null) && (!request.hasError())) {
      if (isChangeSetComplete(request, row.getIndex(), cell.getColumn())) {
        if (logger.isDebugEnabled()) {
          logger.debug("Change set complete column="+cell.getColumn()+",rowIndex="+row.getIndex()+",request="+request);
        }
        MOTableRowEvent<R> rowEvent =
            new MOTableRowEvent<R>(this, this, row, changeSet, (creation) ?
                                   MOTableRowEvent.CREATE :MOTableRowEvent.CHANGE,
                                   true);
        fireRowChanged(rowEvent);
        if (rowEvent.getVetoStatus() != PDU.noError) {
          if (rowEvent.getVetoColumn() >= 0) {
            int colID = columns[rowEvent.getVetoColumn()].getColumnID();
            OID prefix = new OID(getOID());
            prefix.append(colID);
            SubRequest r = request.getRequest().find(prefix);
            if (r != null) {
              r.getStatus().setErrorStatus(rowEvent.getVetoStatus());
            }
            else {
              request.getRequest().setErrorStatus(rowEvent.getVetoStatus());
            }
          }
          else {
            request.getRequest().setErrorStatus(rowEvent.getVetoStatus());
          }
        }
      }
      else if (logger.isDebugEnabled()) {
        logger.debug("Change set not yet complete on column="+cell.getColumn()+",rowIndex="+row.getIndex()+
            ",request="+request);
      }
    }
    if (request.getStatus().getErrorStatus() == PDU.noError) {
      mcol.prepare(request, row, changeSet, cell.getColumn());
      MOChangeEvent changeEvent =
          new MOChangeEvent(this, new CellProxy(cell),
                            cell.getCellOID(),
                            row.getValue(cell.getColumn()),
                            request.getVariableBinding().getVariable(),
                            true);
      fireAfterPrepareMOChange(changeEvent);
      if (changeEvent.getDenyReason() != PDU.noError) {
        request.getStatus().setErrorStatus(changeEvent.getDenyReason());
      }
      else {
        if ((row instanceof MOMutableRow2PC) &&
            (isChangeSetComplete(request, row.getIndex(), cell.getColumn()))) {
          ((MOMutableRow2PC)row).prepareRow(request, changeSet);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  protected int getChangesFromRequest(OID index,
                                      MOTableRow row,
                                      SubRequest request,
                                      Variable[] values,
                                      boolean setDefaultValues,
                                      boolean newRow) {
    int lastChangedColumn = -1;
    // assign default values
    if (setDefaultValues) {
      for (int i = 0; (i < values.length) && (i < getColumnCount()); i++) {
        if (columns[i] instanceof MOMutableColumn) {
          values[i] = ((MOMutableColumn) columns[i]).getDefaultValue();
        }
      }
    }
    Request req = request.getRequest();
    for (Iterator<SubRequest> it = req.iterator(); it.hasNext();) {
      SubRequest sreq = it.next();
      OID id = sreq.getVariableBinding().getOid();
      MOTableCellInfo cellInfo = getCellInfo(id);
      if (index.equals(cellInfo.getIndex())) {
        int col = cellInfo.getColumn();
        if ((col >= 0) && (col < values.length)) {
          Variable v = sreq.getVariableBinding().getVariable();
          // check that value is really changed
          if ((v != null) &&
              ((row == null) || (newRow) ||
               (row.size() <= col) ||
               (!v.equals(row.getValue(col))))) {
            values[col] = v;
            lastChangedColumn = col;
          }
        }
      }
    }
    return lastChangedColumn;
  }

  protected boolean hasNewRows(Request key) {
    return ((newRows != null) && (newRows.get(key) != null));
  }

  protected Map<OID, R> getNewRows(Request key) {
    if (newRows == null) {
      newRows = new WeakHashMap<Request, Map<OID, R>>(4);
    }
    Map<OID, R> rowMap = newRows.get(key);
    if (rowMap == null) {
      rowMap = new HashMap<OID, R>(5);
      newRows.put(key, rowMap);
    }
    return rowMap;
  }

  protected synchronized boolean isChangeSetComplete(SubRequest subRequest,
                                                     OID index,
                                                     int column) {
    ChangeSet changeSet = getPendingChangeSet(subRequest, index);
    return (changeSet == null) || (changeSet.getLastChangedColumn() == column);
  }

/*
  protected synchronized ChangeSet addPendingChange(SubRequest subRequest,
                                                    OID index, int column,
                                                    Variable value) {
    if (pendingChanges == null) {
      pendingChanges = new WeakHashMap(4);
    }
    Map rowMap = (Map) pendingChanges.get(subRequest.getRequest());
    if (rowMap == null) {
      rowMap = new HashMap(5);
      pendingChanges.put(subRequest.getRequest(), rowMap);
    }
    ChangeSet changeSet = (ChangeSet) rowMap.get(index);
    if (changeSet == null) {
      changeSet = new ChangeSet(index, new Variable[getColumnCount()]);
    }
    changeSet.setValue(column, value);
    return changeSet;
  }
*/

  protected synchronized ChangeSet addPendingChanges(SubRequest subRequest,
                                                     MOTableRow row,
                                                     boolean newRow) {
    if (pendingChanges == null) {
      pendingChanges = new WeakHashMap<Request, Map<OID, ChangeSet>>(4);
    }
    Map<OID, ChangeSet> rowMap = pendingChanges.get(subRequest.getRequest());
    if (rowMap == null) {
      rowMap = new HashMap<OID, ChangeSet>(5);
      pendingChanges.put(subRequest.getRequest(), rowMap);
    }
    Variable[] values = new Variable[getColumnCount()];
    int lastChangedColumn =
        getChangesFromRequest(row.getIndex(), row, subRequest,
                              values, newRow, newRow);
    ChangeSet changeSet = new ChangeSet(row.getIndex(), values);
    changeSet.lastChangedColumn = lastChangedColumn;
    rowMap.put(row.getIndex(), changeSet);
    return changeSet;
  }


  protected ChangeSet getPendingChangeSet(SubRequest subRequest,
                                          OID index) {
    if (pendingChanges != null) {
      Map<OID, ChangeSet> rowMap = pendingChanges.get(subRequest.getRequest());
      if (rowMap != null) {
        return rowMap.get(index);
      }
    }
    return null;
  }


  public void cleanup(SubRequest request) {
    OID cellOID = request.getVariableBinding().getOid();
    MOTableCellInfo cell = getCellInfo(cellOID);
    if ((cell.getIndex() == null) || (cell.getColumn() < 0)) {
      return;
    }
    MOColumn col = getColumn(cell.getColumn());
    if (logger.isDebugEnabled()) {
      logger.debug("Cleaning-up sub-request ("+
                   request.getVariableBinding()+") for column: "+col);
    }
    MOMutableTableRow row = (MOMutableTableRow) model.getRow(cell.getIndex());
    if ((row != null) && (col instanceof MOMutableColumn)) {
      ((MOMutableColumn) col).cleanup(request, row, cell.getColumn());
    }
    if ((row instanceof MOMutableRow2PC) &&
        isChangeSetComplete(request, row.getIndex(), cell.getColumn())) {
      ((MOMutableRow2PC)row).cleanupRow(request,
                                        getPendingChangeSet(request,
          row.getIndex()));
    }
    request.completed();
  }

  public void undo(SubRequest request) {
    OID cellOID = request.getVariableBinding().getOid();
    MOTableCellInfo cell = getCellInfo(cellOID);
    MOMutableColumn col = (MOMutableColumn) getColumn(cell.getColumn());
    if (logger.isDebugEnabled()) {
      logger.debug("Undoing sub-request ("+
                   request.getVariableBinding()+") for column: "+col);
    }
    if (hasNewRows(request.getRequest())) {
      ((MOMutableTableModel)model).removeRow(cell.getIndex());
    }
    else {
      MOMutableTableRow row = (MOMutableTableRow) model.getRow(cell.getIndex());
      if (row != null) {
        col.undo(request, row, cell.getColumn());
      }
      if ((row instanceof MOMutableRow2PC) &&
          isChangeSetComplete(request, row.getIndex(), cell.getColumn())) {
        ((MOMutableRow2PC)row).undoRow(request, getPendingChangeSet(request, row.getIndex()));
      }
    }
  }

  public OID getOID() {
    return oid;
  }

  public void setModel(M model) {
    this.model = model;
  }

  public void setVolatile(boolean isVolatile) {
    this.isVolatile = isVolatile;
  }

  public M getModel() {
    return model;
  }

  public C[] getColumns() {
    return columns;
  }

  public MOTableIndex getIndexDef() {
    return indexDef;
  }

  public boolean isVolatile() {
    return isVolatile;
  }

  public OID getLowerBound() {
    return oid;
  }

  public OID getUpperBound() {
    OID upperBound = new OID(oid);
    int lastID = oid.size()-1;
    /**
     * This is not quite correct because we would have to search up the tree
     * if the last sub ID is 0xFFFFFFFF, but since a table OID must end on 1
     * by SMI rules we should be on the safe side here.
     */
    upperBound.set(lastID, oid.get(lastID)+1);
    return upperBound;
  }

  public boolean isLowerIncluded() {
    return false;
  }

  public boolean isUpperIncluded() {
    return false;
  }

  public boolean isCovered(MOScope other) {
    return DefaultMOScope.covers(this, other);
  }

  public boolean isOverlapping(MOScope other) {
    return DefaultMOScope.overlaps(this, other);
  }

  public synchronized void addMOChangeListener(MOChangeListener l) {
    if (moChangeListeners == null) {
      moChangeListeners = new Vector<MOChangeListener>(2);
    }
    moChangeListeners.add(l);
  }

  public synchronized void removeMOChangeListener(MOChangeListener l) {
    if (moChangeListeners != null) {
      moChangeListeners.remove(l);
    }
  }

  protected void fireBeforePrepareMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.elementAt(i)).beforePrepareMOChange(changeEvent);
      }
    }
  }

  protected void fireAfterPrepareMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.elementAt(i)).afterPrepareMOChange(changeEvent);
      }
    }
  }

  protected void fireBeforeMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.elementAt(i)).beforeMOChange(changeEvent);
      }
    }
  }

  protected void fireAfterMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.elementAt(i)).afterMOChange(changeEvent);
      }
    }
  }

  public synchronized void addMOTableRowListener(MOTableRowListener<R> l) {
    if (moTableRowListeners == null) {
      moTableRowListeners = new Vector<MOTableRowListener<R>>(2);
    }
    moTableRowListeners.add(l);
  }

  public synchronized void removeMOTableRowListener(MOTableRowListener<R> l) {
    if (moTableRowListeners != null) {
      moTableRowListeners.remove(l);
    }
  }

  protected void fireRowChanged(MOTableRowEvent<R> event) {
    if (moTableRowListeners != null) {
      Vector<MOTableRowListener<R>> listeners = moTableRowListeners;
      for (MOTableRowListener<R> listener : listeners) {
        listener.rowChanged(event);
      }
    }
  }

  public static class ChangeSet implements MOTableRow {

    private OID index;
    private Variable[] values;
    private int lastChangedColumn = -1;

    public ChangeSet(OID index, Variable[] values) {
      this.index = index;
      this.values = values;
    }

    public OID getIndex() {
      return index;
    }

    public int getLastChangedColumn() {
      return lastChangedColumn;
    }

    public void setValue(int column, Variable value) {
      values[column] = value;
      this.lastChangedColumn = column;
    }

    public Variable getValue(int column) {
      return values[column];
    }

    public MOTableRow getBaseRow() {
      return null;
    }

    public int size() {
      return values.length;
    }

    public void setBaseRow(MOTableRow baseRow) {
      throw new UnsupportedOperationException();
    }
  }

  class CellProxy implements ManagedObject {

    private MOTableCellInfo cellInfo;
    private MOScope scope;

    public CellProxy(MOTableCellInfo cellInfo) {
      this.cellInfo = cellInfo;
      this.scope = new OIDScope(cellInfo.getCellOID());
    }

    public MOScope getScope() {
      return scope;
    }

    public OID find(MOScope range) {
      if (range.isCovered(scope)) {
        return cellInfo.getCellOID();
      }
      return null;
    }

    public void get(SubRequest request) {
      DefaultMOTable.this.get(request);
    }

    public boolean next(SubRequest request) {
      return DefaultMOTable.this.next(request);
    }

    public void prepare(SubRequest request) {
      DefaultMOTable.this.prepare(request);
    }

    public void commit(SubRequest request) {
      DefaultMOTable.this.commit(request);
    }

    public void undo(SubRequest request) {
      DefaultMOTable.this.undo(request);
    }

    public void cleanup(SubRequest request) {
      DefaultMOTable.this.cleanup(request);
    }

    public MOTable getTable() {
      return DefaultMOTable.this;
    }
  }

  static class CellInfo implements MOTableCellInfo {

    private OID index;
    private int id = 0;
    private int col = -1;
    private MOTableRow row;
    private DefaultMOTable table;

    public CellInfo(DefaultMOTable table, OID oid) {
      this.table = table;
      this.index = table.getIndexPart(oid);
      if ((oid.size() > table.oid.size()) &&
          (oid.startsWith(table.oid))) {
        id = oid.get(table.oid.size());
      }
    }

    public CellInfo(DefaultMOTable table, OID index, int column, int columnID) {
      this.table = table;
      this.index = index;
      this.col = column;
      this.id = columnID;
    }

    public CellInfo(DefaultMOTable table, OID index, int column, int columnID, MOTableRow row) {
      this(table, index, column, columnID);
      this.row = row;
    }

    public OID getIndex() {
      return index;
    }

    public int getColumn() {
      if (col < 0) {
        col = table.getColumnIndex(id);
      }
      return col;
    }

    public int getColumnID() {
      return id;
    }

    public OID getCellOID() {
      return table.getCellOID(index, col);
    }

    public MOTableRow getRow() {
      return row;
    }

    @Override
    public String toString() {
      return "CellInfo{" +
          "index=" + index +
          ", id=" + id +
          ", col=" + col +
          ", row=" + row +
          ", table=" + table +
          '}';
    }
  }

  public OID getID() {
    return getLowerBound();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void load(MOInput input) throws IOException {
    if (input.getImportMode() == ImportModes.REPLACE_CREATE) {
      int count = removeAll();
      if (logger.isDebugEnabled()) {
        logger.debug("Removed "+count+" rows from "+getID()+
                     " because importing with a REPLACE import mode");
      }
    }
    Sequence seq = input.readSequence();
    for (int i=0; i<seq.getSize(); i++) {
      IndexedVariables rowValues = input.readIndexedVariables();
      Variable[] rawRowValues = rowValues.getValues();
      // map raw values from storage to restored values
      for (int c=0; ((c < rawRowValues.length) && (c < getColumnCount())); c++) {
        rawRowValues[c] = getColumn(c).getRestoreValue(rawRowValues, c);
      }
      boolean rowExists = model.containsRow(rowValues.getIndex());
      if ((input.getImportMode() == ImportModes.CREATE) && (rowExists)) {
        logger.debug("Row '" + rowValues.getIndex() +
                     "' not imported because it already exists in table " +
                     getID() + " and import mode is CREATE");
        continue;
      }
      if (rowExists) {
        removeRow(rowValues.getIndex());
      }
      /**@todo Do real update here instead of delete/create */
      if ((rowExists) ||
          ((input.getImportMode() == ImportModes.CREATE) ||
           (input.getImportMode() == ImportModes.REPLACE_CREATE) ||
           (input.getImportMode() == ImportModes.UPDATE_CREATE))) {
        R row = null;
        try {
          row = createRow(rowValues.getIndex(), rowValues.getValues());
        }
        catch (UnsupportedOperationException uoex) {
          logger.debug("Could not create row by row factory: " +
                       uoex.getMessage());
          // ignore
        }
        if (row == null) {
          row = (R) new DefaultMOTableRow(rowValues.getIndex(), rowValues.getValues());
          fireRowChanged(new MOTableRowEvent<R>(this, this, row, MOTableRowEvent.CREATE));
        }
        if (logger.isDebugEnabled()) {
          logger.debug("Loaded row "+row+" into table "+getOID());
        }
        addRow(row);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void save(MOOutput output) throws IOException {
    List<IndexedVariables> rowsToSave = new LinkedList<IndexedVariables>();
    synchronized (model) {
      for (Iterator<R> it = model.iterator(); it.hasNext(); ) {
        R row = it.next();
        boolean volatileRow = false;
        for (int i = 0; i < columns.length; i++) {
          if (columns[i].isVolatile(row, i)) {
            volatileRow = true;
            break;
          }
        }
        if (!volatileRow) {
          Variable[] values = getPersistentValues(row);
          IndexedVariables rowValues =
              new IndexedVariables(row.getIndex(), values);
          rowsToSave.add(rowValues);
        }
      }
    }
    Sequence group = new Sequence(rowsToSave.size());
    output.writeSequence(group);
    for (IndexedVariables rowValues : rowsToSave) {
      output.writeIndexedVariables(rowValues);
    }
  }

  /**
   * Gets the values of a row that need to be made persistent on behalf of
   * a {@link #save(MOOutput output)} call.
   *
   * @param row
   *    a MOTableRow that is being saved into a MOOutput stream.
   * @return
   *    an array of <code>Variable</code> instances that need to be saved.
   *    Typically, these are all columns of the row - including hidden
   *    extension columns/values.
   * @since 1.2
   */
  protected Variable[] getPersistentValues(MOTableRow row) {
    Variable[] values = new Variable[row.size()];
    for (int i=0; ((i < values.length) && (i < getColumnCount())); i++) {
      MOColumn column = getColumn(i);
      values[i] = column.getStoreValue(row, i);
    }
    return values;
  }

  public Variable[] getDefaultValues() {
    Variable[] values = new Variable[getColumnCount()];
    for (int i = 0; (i < values.length); i++) {
      if (columns[i] instanceof MOMutableColumn) {
        values[i] = ((MOMutableColumn) columns[i]).getDefaultValue();
      }
    }
    return values;
  }

  public String toString() {
    return "DefaultMOTable[id="+getID()+",index="+getIndexDef()+",columns="+
        Arrays.asList(getColumns())+"]";
  }

  public boolean covers(OID oid) {
    return isCovered(new DefaultMOScope(oid, true, oid, true));
  }

  public boolean setValue(VariableBinding newValueAndInstancceOID) {
    MOTableCellInfo cell = getCellInfo(newValueAndInstancceOID.getOid());
    if (cell != null) {
      MOColumn col = getColumn(cell.getColumn());
      if (logger.isDebugEnabled()) {
        logger.debug("Setting value " +
                     newValueAndInstancceOID + " for column: " + col);
      }
      // Make sure changes are atomic -> sync whole table model
      MOTableRow row;
      synchronized (model) {
        row = model.getRow(cell.getIndex());
        if (row instanceof MOMutableTableRow) {
          ((MOMutableTableRow)row).setValue(cell.getColumn(),
                                            newValueAndInstancceOID.getVariable());
          return true;
        }
      }
    }
    return false;
  }

  private static class RowCacheEntry {
    private MOTableRow row;
    private OID searchLowerBound;
    private boolean searchLowerBoundIncluded;

    RowCacheEntry(MOTableRow row,
                  OID searchLowerBound, boolean searchLowerBoundIncluded) {
      this.row = row;
      this.searchLowerBound = searchLowerBound;
      this.searchLowerBoundIncluded = searchLowerBoundIncluded;
    }
  }
}
