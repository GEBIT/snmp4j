/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOColumn.java  
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

import org.snmp4j.agent.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.mp.SnmpConstants;

/**
 * The <code>MOColumn</code> class represents columnar SMI objects. It
 * represents all instances of a table's column not only a single instance
 * (cell).
 * <p>
 * Objects represented by <code>MOColumn</code> cannot be modified via SNMP,
 * thus <code>MOColumn</code> supports read-only maximum access only.
 *
 * @see MOMutableColumn
 * @author Frank Fock
 * @version 2.4
 */
public class MOColumn<V extends Variable> implements Comparable {

  private int columnID;
  private int syntax;
  private MOAccess access;
  private MOTable table;

  /**
   * Creates a read-only column object with the given column and syntax.
   *
   * @param columnID
   *    the column ID which is ID the last sub-identifier of the corresponding
   *    OBJECT-TYPE definition.
   * @param syntax
   *    the syntax of the objects in this column. See {@link SMIConstants} for
   *    possible values.
   */
  public MOColumn(int columnID, int syntax) {
    this.columnID = columnID;
    this.syntax = syntax;
    this.access = MOAccessImpl.ACCESS_READ_ONLY;
  }

  /**
   * Creates a column object with the given column, syntax, and maximum access.
   * Since <code>MOColumn</code> only supports read-only columns the only
   * reasonable values for <code>access</code> are 'not-accessible' and
   * 'read-only'. Generally this constructor should not be called directly.
   *
   * @param columnID
   *    the column ID which is ID the last sub-indentifer of the corresponding
   *    OBJECT-TYPE definition.
   * @param syntax
   *    the syntax of the objects in this column. See {@link SMIConstants} for
   *    possible values.
   * @param access
   *    the maximum access of the column.
   */
  public MOColumn(int columnID, int syntax, MOAccess access) {
    this.columnID = columnID;
    this.syntax = syntax;
    if (access == null) {
      throw new NullPointerException("Access must be specified");
    }
    this.access = access;
  }

  public void setColumnID(int columnID) {
    this.columnID = columnID;
  }

  public void setSyntax(int syntax) {
    this.syntax = syntax;
  }

  public void setAccess(MOAccess access) {
    this.access = access;
  }

  /**
   * Sets the table instance this columnar object is contained in. This method
   * should be called by {@link MOTable} instance to register the table with
   * the column.
   * @param table
   *    the <code>MOTable</code> instance where this column is contained in.
   */
  public <R extends MOTableRow> void setTable(MOTable<R, ? extends MOColumn, ? extends MOTableModel<R>> table) {
    this.table = table;
  }

  public int getColumnID() {
    return columnID;
  }

  public int getSyntax() {
    return syntax;
  }

  public MOAccess getAccess() {
    return access;
  }

  public MOTable getTable() {
    return table;
  }

  @SuppressWarnings("unchecked")
  public V getValue(MOTableRow row, int column) {
    return (V) row.getValue(column);
  }

  /**
   * Tests if the supplied row is volatile or persistent. If volatile then
   * the row will not be saved when the table is saved to persistent storage.
   *
   * @param row
   *    a row of the table where this column is part of.
   * @param column
   *    the column index of this column in <code>row</code>.
   * @return
   *    <code>true</code> if <code>row</code> should not be
   */
  public boolean isVolatile(MOTableRow<V> row, int column) {
    return false;
  }

  /**
   * Return the restore value for this column and the given row.
   * @param rowValues
   *    a row of the table where this column is part of.
   * @param column
   *    the column index of this column in <code>row</code>.
   * @return
   *    the restored value. By default this is <code>rowValues[column]</code>.
   * @since 2.4
   */
  public Variable getRestoreValue(Variable[] rowValues, int column) {
    return rowValues[column];
  }

  /**
   * Return the content of this column's value of the given row for persistent storage.
   * @param row
   *    a row of the table where this column is part of.
   * @param column
   *    the column index of this column in <code>row</code>.
   * @return
   *    the value to be stored persistently for this <code>row</code> and <code>column</code>.
   * @since 2.4
   */
  public Variable getStoreValue(MOTableRow row, int column) {
    return row.getValue(column);
  }

  /**
   * Compares this managed object column by its ID with another column.
   * @param column
   *    another <code>MOColumn</code>.
   * @return int
   *    a negative integer, zero, or a positive integer as this column ID
   *    is less than, equal to, or greater than the specified object's column
   *    ID.
   */
  public int compareTo(Object column) {
    return columnID - ((MOColumn)column).getColumnID();
  }

  public String toString() {
    return this.getClass().getName()+"[columnID="+getColumnID()+",syntax="+
        getSyntax()+"]";
  }

  @SuppressWarnings("unchecked")
  public void get(SubRequest subRequest, MOTableRow row, int column) {
    if (getAccess().isAccessibleForRead()) {
      V value = getValue(row, column);
      if (value != null) {
        subRequest.getVariableBinding().setVariable((V) value.clone());
      }
      else {
        subRequest.getVariableBinding().setVariable(Null.noSuchInstance);
      }
      subRequest.completed();
    }
    else {
      subRequest.getStatus().setErrorStatus(SnmpConstants.SNMP_ERROR_NO_ACCESS);
    }
  }

}
