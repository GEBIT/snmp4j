/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOMutableTableRow.java  
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

import org.snmp4j.smi.Variable;

/**
 * The <code>MOMutableTableRow</code> extends <code>MOTableRow</code> by
 * means to change a cells value and to support modifications (i.e. row creation
 * and deletion) across related tables.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOMutableTableRow extends MOTableRow {

  /**
   * Sets the value of a column of this row.
   * @param column
   *    the (zero-based) column index.
   * @param value
   *    the new value for the specified column. Implementations of this method
   *    may not check the value's type to match the columns type for performance
   *    reasons. Thus, the caller have to make sure that the type's match to
   *    avoid runtime exceptions later.
   */
  void setValue(int column, Variable value);

  void setBaseRow(MOTableRow baseRow);

}
