/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableIndexValidator.java  
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
 * A <code>MOTableIndexValidator</code> instance is able to validate an index
 * value for compatibility with an index definition.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOTableIndexValidator {

  /**
   * Checks whether an index OID is a valid index for this index definition
   * or not.
   * @param index
   *    an OID (possibly zero length).
   * @return
   *    <code>true</code> if the index is valid or <code>false</code> otherwise.
   */
  boolean isValidIndex(OID index);

}
