/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOMutableTableModel.java  
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

import java.util.*;

import org.snmp4j.smi.*;

public class DefaultMOMutableTableModel<R extends MOTableRow> extends DefaultMOTableModel<R>
    implements MOMutableTableModel<R>
{

  protected MOTableRowFactory<R> rowFactory;
  private transient Vector<MOTableModelListener> moTableModelListeners;

  public MOTableRowFactory<R> getRowFactory() {
    return rowFactory;
  }

  /**
   * Returns a lexicographic ordered list of the rows in the specified index
   * range.
   * @param lowerBound
   *    the lower bound index (inclusive) for the rows in the returned list.
   * @param upperBoundEx
   *    the upper bound index (exclusive) for the rows in the returned list.
   * @return
   *    the possibly empty lexicographically ordered <code>List</code>
   *    of rows of this table model in the specified index range. Modifications
   *    to the list will not affect the underlying table model, although
   *    modifications to the row elements will.
   */
  public synchronized List<MOTableRow> getRows(OID lowerBound, OID upperBoundEx) {
    return new ArrayList<MOTableRow>(getView(lowerBound, upperBoundEx).values());
  }

  /**
   * Returns a lexicographic ordered list of the rows in the specified index
   * range that match the supplied filter.
   * @param lowerBound
   *    the lower bound index (inclusive) for the rows in the returned list.
   * @param upperBoundEx
   *    the upper bound index (exclusive) for the rows in the returned list.
   * @param filter
   *    the filter to exclude rows in the range from the returned
   * @return
   *    the possibly empty lexicographically ordered <code>List</code>
   *    of rows of this table model in the specified index range. Modifications
   *    to the list will not affect the underlying table model, although
   *    modifications to the row elements will.
   */
  public synchronized List<R> getRows(OID lowerBound, OID upperBoundEx,
                                      MOTableRowFilter<R> filter) {
    LinkedList<R> result = new LinkedList<R>();
    SortedMap<OID, R> view = getView(lowerBound, upperBoundEx);
    for (R row : view.values()) {
      if (filter.passesFilter(row)) {
        result.add(row);
      }
    }
    return result;
  }

  private SortedMap<OID,R> getView(OID lowerBound, OID upperBoundEx) {
    SortedMap<OID,R> view;
    if ((lowerBound == null) && (upperBoundEx == null)) {
      view = rows;
    }
    else if (lowerBound == null) {
      view = rows.headMap(upperBoundEx);
    }
    else if (upperBoundEx == null) {
      view = rows.tailMap(lowerBound);
    }
    else {
      view = rows.subMap(lowerBound, upperBoundEx);
    }
    return view;
  }

  public synchronized R removeRow(OID index) {
    R row = rows.remove(index);
    if ((row != null) && (moTableModelListeners != null)) {
      MOTableModelEvent event =
         new MOTableModelEvent(this, MOTableModelEvent.ROW_REMOVED, row);
      fireTableModelChanged(event);
    }
    return row;
  }

  public synchronized void removeRows(OID lowerBoundIncl,
                                      OID upperBoundExcl) {
    Map<OID, R> m = (lowerBoundIncl == null) ? rows : rows.tailMap(lowerBoundIncl);
    for (Iterator<Map.Entry<OID, R>> it = m.entrySet().iterator(); it.hasNext(); ) {
      Map.Entry<OID, R> item = it.next();
      if ((upperBoundExcl == null) ||
          (upperBoundExcl.compareTo(item.getKey()) > 0)) {
        if (moTableModelListeners != null) {
          MOTableModelEvent event =
             new MOTableModelEvent(this, MOTableModelEvent.ROW_REMOVED,
                 item.getValue());
          fireTableModelChanged(event);
        }
        it.remove();
      }
      else {
        break;
      }
    }
  }

  public synchronized void clear() {
    fireTableModelChanged(new MOTableModelEvent(this,
                                                MOTableModelEvent.TABLE_CLEAR));
    rows.clear();
  }

  /**
   * Remove all rows that do not match the given filter criteria
   * from the model.
   * @param filter
   *    the <code>MOTableRowFilter</code> that filters out the rows to
   *    delete.
   */
  public synchronized void clear(MOTableRowFilter<R> filter) {
    for (Iterator<R> it = rows.values().iterator(); it.hasNext();) {
      R row = it.next();
      if (!filter.passesFilter(row)) {
        if (moTableModelListeners != null) {
          MOTableModelEvent event =
             new MOTableModelEvent(this, MOTableModelEvent.ROW_REMOVED, row);
          fireTableModelChanged(event);
        }
        it.remove();
      }
    }
  }

  /**
   * Returns an iterator over all rows in this table that pass the
   * given filter. If the table might be modified while the iterator
   * is used, it is recommended to synchronize on this model while
   * iterating.
   * @param filter
   *    a MOTableRowFilter instance that defines the rows to return.
   * @return
   *    an Iterator.
   */
  public synchronized Iterator<R> iterator(MOTableRowFilter<R> filter) {
    return new FilteredRowIterator(filter);
  }

  /**
   * Create a new row and return it. The new row will not be added to the
   * table. To add it to the model use the {@link #addRow} method.
   * If this mutable table does not support row creation, it should
   * throw an {@link UnsupportedOperationException}.
   * @param index
   *    the index OID for the new row.
   * @param values
   *    the values to be contained in the new row.
   * @return
   *    the created <code>MOTableRow</code>.
   * @throws java.lang.UnsupportedOperationException
   *    if the specified row cannot be created.
   */
  public R createRow(OID index, Variable[] values)
      throws UnsupportedOperationException
  {
    if (rowFactory == null) {
      throw new UnsupportedOperationException("No row factory");
    }
    return rowFactory.createRow(index, values);
  }

  @Override
  public <F extends MOTableRowFactory<R>> void setRowFactory(F rowFactory) {
    this.rowFactory = rowFactory;
  }

  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  public void freeRow(R row) {
    if (rowFactory != null) {
      rowFactory.freeRow(row);
    }
  }

  public synchronized void addMOTableModelListener(MOTableModelListener l) {
    if (moTableModelListeners == null) {
      moTableModelListeners = new Vector<MOTableModelListener>(2);
    }
    moTableModelListeners.add(l);
  }

  public synchronized void removeMOTableModelListener(MOTableModelListener l) {
    if (moTableModelListeners != null) {
      moTableModelListeners.remove(l);
    }
  }

  protected void fireTableModelChanged(MOTableModelEvent event) {
    final Vector<MOTableModelListener> listeners = moTableModelListeners;
    if (listeners != null) {
      synchronized (listeners) {
        for (MOTableModelListener listener : listeners) {
          listener.tableModelChanged(event);
        }
      }
    }
  }

  public class FilteredRowIterator implements Iterator<R> {

    private Iterator<R> iterator;
    private MOTableRowFilter<R> filter;
    private R next;

    FilteredRowIterator(MOTableRowFilter<R> filter) {
      this.filter = filter;
      this.iterator = iterator();
    }

    public void remove() {
      iterator.remove();
    }

    public boolean hasNext() {
      if (next != null) {
        return true;
      }
      findNext();
      return (next != null);
    }

    private void findNext() {
      while (iterator.hasNext()) {
        next = iterator.next();
        if (filter.passesFilter(next)) {
          break;
        }
        else {
          next = null;
        }
      }
    }

    public R next() {
      if (next == null) {
        findNext();
      }
      if (next != null) {
        R retval = next;
        next = null;
        return retval;
      }
      throw new NoSuchElementException();
    }

  }

  public R addRow(R row) {
    R newRow = super.addRow(row);
    if (moTableModelListeners != null) {
      MOTableModelEvent event =
         new MOTableModelEvent(this, MOTableModelEvent.ROW_ADDED, row);
      fireTableModelChanged(event);
    }
    return newRow;
  }

}
