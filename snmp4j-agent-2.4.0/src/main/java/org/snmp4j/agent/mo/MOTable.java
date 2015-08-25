/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTable.java  
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

import org.snmp4j.smi.OID;
import org.snmp4j.agent.MOScope;
import org.snmp4j.smi.Variable;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.ManagedObjectValueAccess;

/**
 * The <code>MOTable</code> interface describes SNMP conceptual tables.
 * In general, a conceptual table can be implemented in two different ways:
 * <ul>
 * <li>For large tables, a virtual table model is best suited where rows are
 * created on behalf of a request only. The instrumentation directly propagates
 * data to the managed objects without holding the data in a table model.</li>
 * <li>For small or medium size tables, holding the data in (non virtual) table
 * model provides data caching and decoupling of the instrumentation.</li>
 * </ul>
 * @author Frank Fock
 * @version 1.4
 */
public interface MOTable<R extends MOTableRow, C extends MOColumn, M extends MOTableModel<R>>
    extends ManagedObject, ManagedObjectValueAccess {

  /**
   * Finds the object identifier of the first object instance in the specified
   * range.
   * @param range
   *    a <code>MOScope</code> specifying the search range.
   * @return
   *    the OID of the lexicographic first instance in the search range or
   *    <code>null</code> if no such instance exists.
   */
  OID find(MOScope range);

  /**
   * Returns the zero based column index for the specified column
   * sub-identifier.
   * @param id
   *    a column sub-identifier (normally one based) as defined in the MIB
   *    specification.
   * @return
   *    a value greater or equal to zero denoting the column index
   *    of the column associated with <code>id</code>. The column index
   *    points into the column array returned by {@link #getColumns}.
   *    A value less than zero indicates that such a column does not exists
   *    currently but could be inserted at the <code>(-n)-1</code> position
   *    if <code>n</code> is the returned value.
   */
  int getColumnIndex(int id);

  /**
   * Gets the column definitions for this table.
   * @return
   *    an array with the column definitions of this table.
   */
  C[] getColumns();

  /**
   * Gets the column definition for the specified column.
   * @param index
   *    the (zero-based) column index.
   * @return
   *    a <code>MOColumn</code> instance describing the attributes of requested
   *    column.
   */
  C getColumn(int index);

  /**
   * Returns a <code>MOTableCellInfo</code> instance for the supplied
   * cell OID. The returned object contains the index, column index, and
   * column ID of the specified cell, if available.
   * @param oid
   *    cell instance OID.
   * @return
   *    a <code>MOTableCellInfo</code> instance with the index, column index
   *    and column ID of the specified cell if available.
   */
  MOTableCellInfo getCellInfo(OID oid);

  /**
   * Returns the number of columns in this table.
   * @return
   *    the column count.
   */
  int getColumnCount();

  /**
   * Gets the index definition of this table.
   *
   * @return
   *    a MOTableIndex instance containing the sub-index definitions for this
   *    table.
   */
  MOTableIndex getIndexDef();

  /**
   * Returns the index part of a column instance identifier of this table.
   * @param instanceIdentifier
   *    the OID of a column instance. The returned result is undefined, when
   *    this OID is not a column instance OID.
   * @return
   *    an OID representing the index OID of the row identified by the
   *    <code>instanceIdentifier</code> column instance OID.
   */
  OID getIndexPart(OID instanceIdentifier);

  /**
   * Gets the table model of this table.
   * @return
   *    a MOTableModel instance.
   */
  M getModel();

  /**
   * Returns the OID of the table entry.
   * @return
   *    a table entry OID (including the .1 suffix).
   */
  OID getOID();

  /**
   * Returns an array of variables where each variable corresponds to the
   * column with the same index. If a column has a default value, the returned
   * variable is not <code>null</code> and contains that default value.
   *
   * @return
   *    the default variables for a newly created row as an array of
   *     <code>Variable</code> instances.
   */
  Variable[] getDefaultValues();

  /**
   * Gets the value of the cell instance with the specified instance OID.
   * @param cellOID
   *    the instance OID of the requested cell.
   * @return
   *    the value of the cell or <code>null</code> if such a cell does not
   *    exist.
   */
  Variable getValue(OID cellOID);

  /**
   * Gets the value of the cell instance in the specified column and row.
   * @param index
   *    the row index of the cell.
   * @param col
   *    the column index of the cell.
   * @return
   *    the value of the cell or <code>null</code> if such a cell does not
   *    exist.
   */
  Variable getValue(OID index, int col);

  /**
   * Adds a <code>MOChangeListener</code> that needs to be informed about
   * state changes of this table.
   * @param l
   *    a <code>MOChangeListener</code> instance.
   */
  void addMOChangeListener(MOChangeListener l);

  /**
   * Removes a <code>MOChangeListener</code>
   * @param l
   *    a <code>MOChangeListener</code> instance.
   */
  void removeMOChangeListener(MOChangeListener l);

  /**
   * Adds a <code>MOTableRowListener</code> listener that needs to be informed
   * about row changes (creation, addition, removal).
   * @param l
   *    a <code>MOTableRowListener</code> instance.
   */
  void addMOTableRowListener(MOTableRowListener<R> l);

  /**
   * Removes <code>MOTableRowListener</code> instance.
   * @param l
   *    a <code>MOTableRowListener</code> instance.
   */
  void removeMOTableRowListener(MOTableRowListener<R> l);

  /**
   * Creates a new row for this table with the supplied index and initial
   * values. If one of the {@link MOTableRowListener} deny the row creation
   * attempt then <code>null</code> will be returned.
   * @param index
   *    the index OID of the new row.
   * @param initialValues
   *    the initial values that should be assigned to the new row.
   * @return
   *    the created <code>MOTableRow</code> instance or <code>null</code> if
   *    the row cannot be created.
   */
  R createRow(OID index, Variable[] initialValues);

  /**
   * Creates a new row for this table with the supplied index and initial
   * values and then immediately calls {@link #addRow(MOTableRow)}.
   * If one of the {@link MOTableRowListener} deny the row creation
   * attempt then <code>null</code> will be returned and {@link #addRow(MOTableRow)}
   * will not be called.
   * <p>
   *   This method is the same as calling:
   * </p>
   * <pre>
   * R newRow = createRow(index, initialValues);
   *   if (newRow != null) {
   *     addRow(newRow);
   *   }
   *   return newRow;
   * </pre>
   * @param index
   *    the index OID of the new row.
   * @param initialValues
   *    the initial values that should be assigned to the new row.
   * @return
   *    the created <code>MOTableRow</code> instance or <code>null</code> if
   *    the row cannot be created.
   * @since 2.2
   */
  R addNewRow(OID index, Variable[] initialValues);

  /**
   * Creates a new row for this table with the supplied index and
   * default values. If one of the {@link MOTableRowListener}
   * deny the row creation attempt then <code>null</code> will be returned.
   * @param index
   *    the index OID of the new row.
   * @return
   *    the created <code>MOTableRow</code> instance or <code>null</code> if
   *    the row cannot be created.
   */
  R createRow(OID index);

  /**
   * Adds the supplied row to the underlying table model and fires the
   * appropriate {@link MOTableRowEvent}. Since this method is typically
   * called during the commit phase of a SET request that creates a table,
   * it should be avoided to return an error here. Instead error checking
   * should be placed in the
   * @param row
   *    the <code>MOTableRow</code> to add.
   * @return
   *    <code>true</code> if the row has been added or <code>false</code>
   *    if it could not be added.
   */
  boolean addRow(R row);

  /**
   * Removes the row with the specified index and returns it if the operation
   * was successful.
   * @param index
   *    the index OID of the row to remove.
   * @return
   *    the removed row or <code>null</code> if the row cannot be found or
   *    cannot be removed.
   */
  R removeRow(OID index);
}
