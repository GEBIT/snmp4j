/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableCellInfo.java  
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

/**
 * The <code>MOTableCellInfo</code> interface provides information about a
 * cell of a conceptual table.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOTableCellInfo {

  /**
   * Returns the index of the cell's row.
   * @return
   *    an OID representing the row's index.
   */
  OID getIndex();

  /**
   * Gets the column index of the cell's column.
   * @return
   *    the zero-based index of the cell's column.
   */
  int getColumn();

  /**
   * Gets the column ID of the cell's column.
   * @return
   *   the column ID which is the last sub-identifier of the cell's column
   *   specification.
   */
  int getColumnID();

  /**
   * Gets the OID that uniquely identifies the cell instance.
   * @return
   *    an OID
   */
  OID getCellOID();
}
