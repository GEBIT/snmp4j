/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableRow.java  
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
import org.snmp4j.smi.Variable;

/**
 * The <code>MOTableRow</code> interface describes a conceptual table row.
 *
 * @author Frank Fock
 * @version 2.0.5
 */
public interface MOTableRow<V extends Variable> {

  /**
   * Gets the row's index OID, for example the row index of the first row
   * of an ifTable would return <code>OID("1")</code>.
   * @return
   *    the row index of this row.
   */
  OID getIndex();

  /**
   * Gets the value at the specified column index.
   * @param column
   *    the zero-based column index.
   * @return
   *    the value at the specified index.
   */
  V getValue(int column);

  /**
   * Gets the associated base row for this row (if this row is a dependent row).
   * @return
   *    the base row or <code>null</code> if this row is a base row itself.
   */
  MOTableRow getBaseRow();

  /**
   * Sets the associated base row.
   * @param baseRow
   *    the base row.
   */
  void setBaseRow(MOTableRow baseRow);

  /**
   * Returns the number of columns in this row.
   * @return
   *    the column count.
   */
  int size();

}
