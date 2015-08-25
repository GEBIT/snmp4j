/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableSizeLimit.java  
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
import java.util.Properties;
import java.util.SortedMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.snmp4j.smi.OID;
import org.snmp4j.PDU;
// for JavaDoc
import org.snmp4j.agent.DefaultMOServer;

/**
 * This class implements a size limit on the number of rows in a table.
 * If the limit is reached or exceeded, no more additional rows can be
 * added by SNMP means.
 * <p>
 * Use the {@link DefaultMOServer#registerTableRowListener} method to
 * register this table row listener for all tables in your agent, in order
 * to be able to control the table sizes of all the tables in a server.
 *
 * @author Frank Fock
 * @version 1.4
 * @since 1.1.5
 */
public class MOTableSizeLimit<R extends MOTableRow> implements MOTableRowListener<R> {

  /**
   * The property prefix each size limit definition has to start with.
   * The OID subtree for which the definition is effective is then appended
   * as follows:
   * <pre>
   * snmp4j.MOTableSizeLimit.<OID>=<limit>
   * </pre>
   * where <code>limit</code> is a positive number or zero.
   */
  public static final String PROPERTY_PREFIX = "snmp4j.tableSizeLimit.";

  private int maxNumRows = 0;
  private SortedMap<OID, Integer> limits;

  public MOTableSizeLimit(int maxNumRows) {
    this.maxNumRows = maxNumRows;
  }

  public MOTableSizeLimit(Properties limits) {
    setLimits(limits);
  }

  /**
   * A column or a complete row is changed/has been changed.
   *
   * @param event a <code>MOTableRowEvent</code> describing the event. To veto
   *   the event the {@link MOTableRowEvent#setVetoStatus} and optionally also
   *   the {@link MOTableRowEvent#setVetoColumn} can be called.
   */
  public void rowChanged(MOTableRowEvent<R> event) {
    if (event.getType()== MOTableRowEvent.ADD) {
        if (!checkLimits(event)) {
          event.setVetoStatus(PDU.resourceUnavailable);
        }
    }
  }

  private boolean checkLimits(MOTableRowEvent<R> event) {
    int limit = maxNumRows;
    if ((limits != null) && (!limits.isEmpty())) {
      OID search = new OID(event.getTable().getOID());
      while (search.size() > 0) {
        Integer l = limits.get(search);
        if (l != null) {
          limit = l;
          break;
        }
        else {
          search.trim(1);
        }
      }
    }
    int currentSize = 0;
    if (limit > 0) {
      currentSize = event.getTable().getModel().getRowCount();
      if (currentSize >= limit) {
        // remove eldest
        return removeEldest(event, (currentSize - limit) + 1);
      }
    }
    return ((limit <= 0) || (currentSize < limit));
  }

  /**
   * Remove try to remove a given number of eldest rows of the table referred
   * to in the triggering event object.
   * @param triggeringEvent
   *    the MOTableRowEvent object that describes the table with exceeding
   *    row limit.
   * @param numRows
   *   the number of rows to remove (if possible).
   * @return
   *    <code>true</code> if one or more rows could be removed and
   *    <code>false</code> if the causing event should be denied/rejected.
   *    The default implementation returns <code>false</code> always.
   */
  protected boolean removeEldest(MOTableRowEvent<R> triggeringEvent, int numRows) {
    return false;
  }

  public void setMaxNumRows(int maxNumRows) {
    this.maxNumRows = maxNumRows;
  }

  public int getMaxNumRows() {
    return maxNumRows;
  }

  public SortedMap<OID, Integer> getLimits() {
    return limits;
  }

  public void setLimits(Properties limits) {
    this.limits = new TreeMap<OID, Integer>();
    for (Entry<Object, Object> entry : limits.entrySet()) {
      String key = (String) entry.getKey();
      if (key.startsWith(PROPERTY_PREFIX)) {
        this.limits.put(new OID(key.substring(PROPERTY_PREFIX.length())),
            new Integer(entry.getValue().toString()));
      }
    }
  }
}
