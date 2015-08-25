/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ValueConstraint.java  
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


package org.snmp4j.agent.mo.snmp.smi;

import org.snmp4j.smi.Variable;

/**
 * A <code>ValueConstraint</code> instance validates the value ranges of a
 * SNMP value to match a certain constraint.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface ValueConstraint {

  /**
   * Indicates whether a SNMP value matches this value constraint.
   * @param variable
   *    a SNMP value that has to match the type of SNMP value this
   *    <code>ValueConstraint</code> supports. Otherwise a wrongType error
   *    should be returned instead of throwing a
   *    <code>ClassCastException</code>.
   * @return
   *    a SNMP error status (e.g. wrongValue) if the value does not match
   *    the constraint, or zero if it matches.
   */
  int validate(Variable variable);

}
