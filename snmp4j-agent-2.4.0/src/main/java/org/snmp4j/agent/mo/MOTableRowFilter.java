/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableRowFilter.java  
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

/**
 * The <code>MOTableRowFilter</code> interface can be used to filter rows. The
 * <code>RowStatus.ActiveRowsFilter</code> class,
 * for example, can be used to filter only active rows.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOTableRowFilter<R extends MOTableRow> {

  /**
   * Checks whether the supplied row passes the filter criteria implemented by
   * this row filter.
   * @param row
   *    a <code>MOTableRow</code> instance.
   * @return
   *    <code>true</code> if the row passes the filter or <code>false</code>
   *    if not.
   */
  boolean passesFilter(R row);

}
