/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableRowEvent.java  
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

import org.snmp4j.mp.*;

/**
 * The <code>MOTableRowEvent</code> class describes the changes
 * @param <R>
 *   The {@link MOTable} row type of this event.
 * @author Frank Fock
 * @version 2.2
 */
public class MOTableRowEvent<R extends MOTableRow> extends DeniableEventObject {

  private static final long serialVersionUID = 5846054060591503486L;

  /**
   * A single column of a row is changed.
   */
  public static final int CHANGE = 0;
  /**
   * A row is created.
   */
  public static final int CREATE = 1;
  /**
   * A row is added.
   */
  public static final int ADD    = 2;
  /**
   * A row is deleted.
   */
  public static final int DELETE = 3;

  /**
   * This event type indicates that a complete row has been updated.
   */
  public static final int UPDATED = 4;


  private MOTable<R,? extends MOColumn,? extends MOTableModel<R>> table;
  private R row;
  private MOTableRow preparedChanges;
  private int type;
  private int vetoColumn = -1;

  /**
   * Creates a table row event based on table, row and type that cannot be
   * canceled by the event listener.
   * @param source
   *    the event source.
   * @param table
   *    the table.
   * @param row
   *    the row associated with this event.
   * @param type
   *    the event type.
   */
  public MOTableRowEvent(Object source,
                         MOTable<R,? extends MOColumn,? extends MOTableModel<R>> table, R row,
                         int type) {
    super(source, false);
    this.table = table;
    this.row = row;
    this.type = type;
  }

  /**
   * Creates a table row event based on table, row and type that may be
   * canceled by the event listener depending on the specified flag.
   * @param source
   *    the event source.
   * @param table
   *    the table.
   * @param row
   *    the row associated with this event.
   * @param type
   *    the event type.
   * @param deniable
   *    indicates whether the event can be canceled through setting its
   *    denyReason member to a SNMP error status.
   * @since 1.1
   */
  public MOTableRowEvent(Object source,
                         MOTable<R,? extends MOColumn,? extends MOTableModel<R>> table, R row,
                         int type, boolean deniable) {
    super(source, deniable);
    this.table = table;
    this.row = row;
    this.type = type;
  }

  /**
   * Creates a table row event based on table, row, prepared changes, and type
   * that cannot be canceled by the event listener.
   *
   * @param source
   *    the event source.
   * @param table
   *    the table.
   * @param row
   *    the row associated with this event.
   * @param preparedChanges
   *    a row instance containing the prepared changes for <code>row</code>.
   * @param type
   *    the event type.
   */
  public MOTableRowEvent(Object source,
                         MOTable<R,? extends MOColumn,? extends MOTableModel<R>> table,
                         R row,
                         MOTableRow preparedChanges,
                         int type) {
    super(source, false);
    this.table = table;
    this.row = row;
    this.preparedChanges = preparedChanges;
    this.type = type;
  }

  /**
   * Creates a table row event based on table, row, prepared changes, and type.
   * @param source
   *    the event source.
   * @param table
   *    the table.
   * @param row
   *    the row associated with this event.
   * @param preparedChanges
   *    a row instance containing the prepared changes for <code>row</code>.
   * @param type
   *    the event type.
   * @param deniable
   *    indicates whether the event can be canceled through setting its
   *    denyReason member to a SNMP error status.
   * @since 1.1
   */
  public MOTableRowEvent(Object source,
                         MOTable<R,? extends MOColumn,? extends MOTableModel<R>> table,
                         R row,
                         MOTableRow preparedChanges,
                         int type,
                         boolean deniable) {
    super(source, deniable);
    this.table = table;
    this.row = row;
    this.preparedChanges = preparedChanges;
    this.type = type;
  }

  public R getRow() {
    return row;
  }

  public MOTable<R,? extends MOColumn,? extends MOTableModel<R>> getTable() {
    return table;
  }

  public int getType() {
    return type;
  }

  /**
   * Returns the veto status that revokes the row operation or zero if the
   * row operation is accepted by all listeners.
   * @return
   *    a SNMP or sub-agent protocol error status or zero if the row operation
   *    is accepted.
   */
  public int getVetoStatus() {
    return getDenyReason();
  }

  public MOTableRow getPreparedChanges() {
    return preparedChanges;
  }

  public int getVetoColumn() {
    return vetoColumn;
  }

  /**
   * Sets the veto status that revokes the row operation.
   * @param denyReason
   *    a SNMP error status or a sub-agent protocol specific error status.
   *    In any case zero represents no error.
   */
  public void setVetoStatus(int denyReason) {
    setDenyReason(denyReason);
  }

  /**
   * Sets the column index on whose behalf the veto is issued.
   * @param vetoColumn
   *    a column index.
   */
  public void setVetoColumn(int vetoColumn) {
    this.vetoColumn = vetoColumn;
  }

  public String toString() {
    return getClass().getName()+"[source="+source+",table="+table+",row="+row+
        ",type="+type+",deniable="+isDeniable()+"]";
  }
}
