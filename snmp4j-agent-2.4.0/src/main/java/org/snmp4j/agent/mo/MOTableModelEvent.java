/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableModelEvent.java  
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

import java.util.EventObject;

/**
 * The <code>MOTableModelEvent</code> event object describes events that change
 * a table model. Such events include adding, removing, and changing of rows
 * as well as clearing a whole model.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOTableModelEvent extends EventObject {

  private static final long serialVersionUID = -2260159403182301993L;

  public static final int ROW_CHANGED = 0;
  public static final int ROW_ADDED = 1;
  public static final int ROW_REMOVED = 2;
  public static final int TABLE_CLEAR = 3;

  private int type;
  private MOTableRow affectedRow;
  private int columnIndex = -1;

  /**
   * Creates a model event associated with a single row and column.
   * @param source
   *    the event source.
   * @param type
   *    the event type as defined by the ROW_* constants of this object.
   * @param affectedRow
   *    the row that is associated with this event.
   * @param columnIndex
   *    the column index associated with this event.
   */
  public MOTableModelEvent(Object source, int type, MOTableRow affectedRow,
                           int columnIndex) {
    super(source);
    this.type = type;
    this.affectedRow = affectedRow;
    this.columnIndex = columnIndex;
  }

  /**
   * Creates a model event associated with a single row.
   * @param source
   *    the event source.
   * @param type
   *    the event type as defined by the ROW_* constants of this object.
   * @param affectedRow
   *    the row that is associated with this event.
   */
  public MOTableModelEvent(Object source, int type, MOTableRow affectedRow) {
    this(source, type, affectedRow, -1);
  }

  /**
   * Creates the model wide event.
   * @param source
   *    the event source.
   * @param type
   *    the event type as defined by the constants of this object.
   */
  public MOTableModelEvent(Object source, int type) {
    this(source, type, null);
  }

  /**
   * Returns the type of event.
   * @return
   *    one of the event type constants defined by this object.
   */
  public int getType() {
    return type;
  }

  /**
   * Gets the affected row (if a single row is affected by the event).
   * @return
   *    the <code>MOTableRow</code> instance associated with this event, or
   *    <code>null</code> if the whole model is affected.
   */
  public MOTableRow getAffectedRow() {
    return affectedRow;
  }

  /**
   * Returns the column index associated with this event.
   * @return
   *    a column index >= 0 if a column is associated with this event or -1
   *    if not.
   */
  public int getColumnIndex() {
    return columnIndex;
  }

  public String toString() {
    return MOTableModelEvent.class.getName()+"[type="+type+
        ",affectedRow="+affectedRow+",columnIndex="+columnIndex+"]";
  }
}
