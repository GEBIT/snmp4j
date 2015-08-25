/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - LogMOTableSizeLimit.java  
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

package org.snmp4j.agent.mo.util;

import org.snmp4j.agent.mo.*;

import java.util.Iterator;
import java.util.Properties;

/**
 * The <code>LogMOTableSizeLimit</code> implements a {@link MOTableSizeLimit}
 * to limit the number of entries in a table by removing the eldest rows
 * existing in the table when the limit is exceeded.
 *
 * @author Frank Fock
 * @version 1.1.5
 * @since 1.1.5
 */
public class LogMOTableSizeLimit<R extends MOTableRow> extends MOTableSizeLimit<R> {

  public LogMOTableSizeLimit(int maxNumRows) {
    super(maxNumRows);
  }

  public LogMOTableSizeLimit(Properties limits) {
    super(limits);
  }

  /**
   * Removes the given number of rows starting at the lowest index value.
   *
   * @param triggeringEvent the MOTableRowEvent object that describes the
   *   table with exceeding row limit.
   * @param numRows
   *   the number of rows to remove (if possible).
   * @return <code>true</code> if one or more rows could be removed and
   *   <code>false</code> if the causing event should be denied/rejected. The
   *   default implementation returns <code>false</code> always.
   */
  @Override
  protected boolean removeEldest(MOTableRowEvent<R> triggeringEvent, int numRows) {
    MOTable<R, ? extends MOColumn,? extends MOTableModel<R>> table = triggeringEvent.getTable();
    boolean removed = false;
    synchronized (table.getModel()) {
      Iterator<R> it = table.getModel().iterator();
      for (int i = 0; (i < numRows) && it.hasNext(); i++) {
        it.next();
        it.remove();
        removed = true;
      }
    }
    return removed;
  }
}
