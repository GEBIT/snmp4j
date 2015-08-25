/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableRowFactory.java  
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
import org.snmp4j.smi.OID;

/**
 * A <code>MOTableRowFactory</code> is used to create and delete rows for a
 * table model.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOTableRowFactory<R extends MOTableRow> {

  /**
   * Creates a new <code>MOTableRow</code> row instance and returns it.
   * @param index
   *    the index OID for the new row.
   * @param values
   *    the values to be contained in the new row.
   * @return
   *    the created <code>MOTableRow</code>.
   * @throws java.lang.UnsupportedOperationException
   *    if the specified row cannot be created.
   */
  R createRow(OID index, Variable[] values)
      throws UnsupportedOperationException;

  /**
   * Frees resources associated with the supplied row which is to be deleted.
   *
   * @param row
   *    a MOTableRow that has been created using this factory and is now to
   *    be deleted (removed from the associated table).
   */
  void freeRow(R row);
}
