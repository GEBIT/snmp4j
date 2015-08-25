/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOMutableRow2PC.java  
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

import org.snmp4j.agent.request.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.mo.DefaultMOTable.ChangeSet;

public class DefaultMOMutableRow2PC extends DefaultMOTableRow
    implements MOMutableRow2PC {

  private MOTableRow baseRow;
  private Object userObject;

  public DefaultMOMutableRow2PC(OID index, Variable[] values) {
    super(index, values);
  }

  public void cleanup(SubRequest subRequest, int column) {
    subRequest.completed();
  }

  public void commit(SubRequest subRequest, MOTableRow changeSet, int column) {
    subRequest.setUndoValue(values[column]);
    setValue(column,
             (Variable) subRequest.getVariableBinding().getVariable().clone());
    subRequest.completed();
  }

  public MOTableRow getBaseRow() {
    return baseRow;
  }

  public OID getIndex() {
    return index;
  }

  public Object getUserObject() {
    return userObject;
  }

  public Variable getValue(int column) {
    return values[column];
  }

  public void setBaseRow(MOTableRow baseRow) {
    this.baseRow = baseRow;
  }

  public void setUserObject(Object userObject) {
    this.userObject = userObject;
  }

  public void setValue(int column, Variable value) {
    values[column] = value;
  }

  public int size() {
    return values.length;
  }

  public void undo(SubRequest subRequest, int column) {
    if (subRequest.getUndoValue() instanceof Variable) {
      setValue(column, (Variable) subRequest.getUndoValue());
    }
    subRequest.completed();
  }

  /**
   * Returns the value of the specified column that would result if the
   * specified changes would have been applied to the row.
   * @param column
   *    the column to return
   * @param changes
   *    a <code>MOTableRow</code> instance representing the changes to apply
   *    to this row. Values that are not changed must be returned as
   *    <code>null</code> values.
   * @return
   *    the resulting <code>Variable</code>.
   */
  public Variable getResultingValue(int column, MOTableRow changes) {
    Variable retval = changes.getValue(column);
    if (retval == null) {
      retval = getValue(column);
    }
    return retval;
  }

  public void commitRow(SubRequest subRequest, MOTableRow changeSet) {
    // overwrite this to perform actions when a row has been committed.
  }

  public void prepare(SubRequest subRequest, MOTableRow changeSet, int column) {
  }

  public void cleanupRow(SubRequest request, ChangeSet changeSet) {
  }

  public void undoRow(SubRequest request, ChangeSet changeSet) {
  }

  public void prepareRow(SubRequest subRequest, MOTableRow changeSet) {
  }

  public String toString() {
    return "DefaultMOMutableRow2PC["+toStringMembers();
  }

}
