/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Constraints.java  
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

/**
 * The <code>Constraints</code> interface describes a collection of SNMP value
 * range constraints.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface Constraints extends ValueConstraint {

  /**
   * Adds a range constraint to the constraints collection.
   * @param constraint
   *    a SNMP integer/long value range constraint.
   */
  void add(Constraint constraint);

  /**
   * Removes a constraint.
   * @param constraint
   *    a SNMP integer/long value range constraint.
   */
  void remove(Constraint constraint);

  /**
   * Gets an array with the constraints in this collection ordered by insertion
   * time.
   * @return
   *    an array of <code>Constraint</code> instances.
   */
  Constraint[] getConstraints();

  /**
   * Interpretes the value range constraints contained in this collection as
   * size restrictions for OCTET STRING values and checks whether the given
   * size matches these criteria.
   * @param size
   *    a long value representing an OCTET STRING size.
   * @return
   *    <code>true</code> if the size is valid.
   */
  boolean isValidSize(long size);
}
