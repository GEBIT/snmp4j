/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - VariableProvider.java  
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

import org.snmp4j.smi.Variable;

/**
 * The <code>VariableProvider</code> provides {@link Variable} instances.
 * It is implementation dependent whether the variables are identified by
 * a dotted OID string or a textual name.
 *
 * @author Frank Fock
 * @version 1.2
 */
public interface VariableProvider {

  /**
   * Gets the variable with the specified name, which might be either a
   * textual name or an instance OID.
   *
   * @param name
   *    the name or OID of the variable to return.
   * @return
   *    a Variable instance or <code>null</code> if such a variable with the
   *    specified name or OID does not exists.
   */
  Variable getVariable(String name);

}
