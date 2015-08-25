/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ManagedObjectValueAccess.java  
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

package org.snmp4j.agent;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * The <code>ManagedObjectValueAccess</code> interface provides direct
 * access to instance values of a {@link ManagedObject}.
 *
 * @author Frank Fock
 * @version 1.4
 * @since 1.4
 */
public interface ManagedObjectValueAccess extends ManagedObject {

  /**
   * Returns the variable (a copy thereof) with the specified instance OID
   * managed by this {@link ManagedObject}.
   * @param instanceOID
   *    the instance OID of the value. Thus, for scalar values with .0 suffix
   *    and for tabular objects with table index suffix.
   * @return
   *    a copy of the requested <code>Variable</code> or <code>null</code> if
   *    such a variable does not exist.
   */
  Variable getValue(OID instanceOID);

  /**
   * Sets the value of a particular MIB object instance managed by
   * this {@link ManagedObject}. This is a low level operation, thus
   * no change events will be fired.
   * @param newValueAndInstanceOID
   *    a <code>VariableBinding</code> identifying the object instance to modify
   *    by its OID and the new value by its variable part.
   * @return
   *    <code>true</code> if the object instance exists and has been modified
   *    successfully, <code>false</code> otherwise.
   */
  boolean setValue(VariableBinding newValueAndInstanceOID);
}
