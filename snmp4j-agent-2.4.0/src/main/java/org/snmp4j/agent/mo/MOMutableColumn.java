/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOMutableColumn.java  
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

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;

/**
 * The <code>MOMutableColumn</code> class represents mutable columnar SMI
 * objects. It represents all instances of a table's column not only a
 * single instance (cell).
 * <p>
 * Objects represented by <code>MOMutableColumn</code> can be modified via SNMP,
 * thus <code>MOColumn</code> supports read-only, read-write, and read-create
 * maximum access.
 *
 * @see MOColumn
 * @author Frank Fock
 * @version 1.0
 */
public class MOMutableColumn<V extends Variable> extends MOColumn<V> {

  private Vector<MOValueValidationListener> validators;
  private Variable defaultValue;
  private boolean mutableInService = true;
  private boolean mandatory = true;

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
  public MOMutableColumn(int columnID, int syntax) {
    super(columnID, syntax);
  }

  /**
   * Creates a column object with the given column, syntax, and maximum access.
   * Since <code>MOColumn</code> only supports read-only columns the only
   * reasonable values for <code>access</code> are 'not-accessible' and
   * 'read-only'. Generally this constructor should not be called directly.
   *
   * @param columnID
   *    the column ID which is ID the last sub-identifier of the corresponding
   *    OBJECT-TYPE definition.
   * @param syntax
   *    the syntax of the objects in this column. See {@link SMIConstants} for
   *    possible values.
   * @param access
   *    the maximum access of the column.
   */
  public MOMutableColumn(int columnID, int syntax, MOAccess access) {
    super(columnID, syntax, access);
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
   * @param defaultValue
   *    the default value for new rows.
   */
  public MOMutableColumn(int columnID, int syntax, MOAccess access,
                         Variable defaultValue) {
    super(columnID, syntax, access);
    this.defaultValue = defaultValue;
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
   * @param defaultValue
   *    the default value for new rows.
   * @param mutableInService
   *    if <code>true</code> this column accepts value changes through SNMP
   *    (via {@link #prepare(org.snmp4j.agent.request.SubRequest, MOTableRow, MOTableRow, int)} and
   *    {@link #commit(org.snmp4j.agent.request.SubRequest, MOTableRow, MOTableRow, int)} while the
   *    rows {@link org.snmp4j.agent.mo.snmp.RowStatus} object is in state
   *    {@link org.snmp4j.agent.mo.snmp.RowStatus#active}. Otherwise, such an operation will result
   *    in a {@link PDU#inconsistentValue} error.
   */
  public MOMutableColumn(int columnID, int syntax, MOAccess access,
                         Variable defaultValue, boolean mutableInService) {
    super(columnID, syntax, access);
    this.defaultValue = defaultValue;
    this.mutableInService = mutableInService;
  }

  public synchronized void
      addMOValueValidationListener(MOValueValidationListener validator) {
    if (validators == null) {
      validators = new Vector<MOValueValidationListener>(2);
    }
    validators.add(validator);
  }

  public synchronized void
      removeMOValueValidationListener(MOValueValidationListener validator) {
    if (validators != null) {
      validators.remove(validator);
    }
  }

  public synchronized int validate(Variable newValue, Variable oldValue) {
    int status = SnmpConstants.SNMP_ERROR_SUCCESS;
    if (validators != null) {
      for (MOValueValidationListener v : validators) {
        MOValueValidationEvent event =
            new MOValueValidationEvent(this, oldValue, newValue);
        v.validate(event);
        if (event.getValidationStatus() != SnmpConstants.SNMP_ERROR_SUCCESS) {
          status = event.getValidationStatus();
          break;
        }
      }
    }
    return status;
  }

  protected boolean validateSetRequest(SubRequest subRequest,
                                       MOTableRow row, int column) {
    Variable value = subRequest.getVariableBinding().getVariable();
    if (value.getSyntax() != getSyntax()) {
      subRequest.getStatus().setErrorStatus(PDU.wrongType);
    }
    int status =
        validate(value, (row.size() > column) ? row.getValue(column) : null);
    if (status != SnmpConstants.SNMP_ERROR_SUCCESS) {
      subRequest.getStatus().setErrorStatus(status);
      return false;
    }
    return true;
  }

  public void prepare(SubRequest subRequest, MOTableRow row,
                      MOTableRow changeSet, int column) {
    if (row instanceof MOMutableRow2PC) {
      if (validateSetRequest(subRequest, row, column)) {
        ((MOMutableRow2PC) row).prepare(subRequest, changeSet, column);
      }
    }
    else if (row instanceof MOMutableTableRow) {
      if (validateSetRequest(subRequest, row, column)) {
        subRequest.completed();
      }
    }
    else {
      // not writable
      subRequest.getStatus().setErrorStatus(PDU.notWritable);
    }
  }

  public void commit(SubRequest subRequest, MOTableRow row,
                     MOTableRow changeSet, int column) {
    if (row instanceof MOMutableRow2PC) {
      ((MOMutableRow2PC)row).commit(subRequest, changeSet, column);
    }
    else if (row instanceof MOMutableTableRow) {
      if (subRequest.getUndoValue() == null) {
        subRequest.setUndoValue(row.getValue(column));
      }
      ((MOMutableTableRow)row).setValue(column,
          (Variable)subRequest.getVariableBinding().getVariable().clone());
      subRequest.completed();
    }
    else {
      // should never be reached!
      subRequest.getStatus().setErrorStatus(PDU.commitFailed);
    }
  }

  public void undo(SubRequest subRequest, MOTableRow row, int column) {
    if (row instanceof MOMutableRow2PC) {
      ((MOMutableRow2PC)row).undo(subRequest, column);
    }
    if ((row instanceof MOMutableTableRow) &&
        (subRequest.getUndoValue() instanceof Variable)) {
      ((MOMutableTableRow)row).setValue(column, (Variable)
                                        subRequest.getUndoValue());
      subRequest.completed();
    }
    else {
      // should never be reached!
      subRequest.getStatus().setErrorStatus(PDU.undoFailed);
    }
  }

  public void cleanup(SubRequest subRequest, MOTableRow row, int column) {
    if (row instanceof MOMutableRow2PC) {
      ((MOMutableRow2PC)row).cleanup(subRequest, column);
    }
    subRequest.completed();
  }

  public void setDefaultValue(Variable defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setMutableInService(boolean mutableInService) {

    this.mutableInService = mutableInService;
  }

  public Variable getDefaultValue() {
    return defaultValue;
  }

  public boolean isMutableInService() {
    return mutableInService;
  }

  /**
   * Returns <code>true</code> if this column must be specified in a SET
   * request which creates a row.
   * @return
   *    <code>true</code> if this row has a maximum access of READ-CREATE and
   *    has a <code>null</code> default value, <code>false</code> otherwise.
   */
  public boolean isMandatory() {
    return (mandatory) &&
        (defaultValue == null) && (getAccess().isAccessibleForCreate());
  }

  /**
   * Sets a flag that determines if this column must be specified in a SET
   * request which creates a row. The default is <code>true</code>.
   * @param mandatory
   *    if <code>true</code> and a row has a maximum access of READ-CREATE and
   *    has a <code>null</code> default value, then it must be provided in
   *    order to activate the row.
   * @since 1.3.2
   */
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public String toString() {
    return this.getClass().getName()+"[columnID="+getColumnID()+",syntax="+
        getSyntax()+",default="+getDefaultValue()+",mutableInService="+
        mutableInService+",mandatory="+mandatory+"]";
  }
}
