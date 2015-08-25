/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MutableMOScope.java  
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

/**
 * The <code>MutableMOScope</code> interface describes a mutable scope
 * as needed for query processing.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MutableMOScope extends MOScope {

  /**
   * Sets the lower bound OID of the scope.
   * @param lowerBound
   *    an OID.
   */
  void setLowerBound(OID lowerBound);

  /**
   * Sets the flag specifying whether the lower bound OID is included in the
   * scope or not.
   * @param lowerIncluded
   *    <code>true</code> if the lower bound is included, <code>false</code>
   *    otherwise.
   */
  void setLowerIncluded(boolean lowerIncluded);

  /**
   * Sets the upper bound OID of the scope (can be <code>null</code> for an
   * unbounded scope.
   * @param upperBound
   *    an OID or <code>null</code>.
   */
  void setUpperBound(OID upperBound);

  /**
   * Sets the flag specifying whether the upper bound OID is included in the
   * scope or not. This flag has no effect if <code>upperBound</code> is
   * <code>null</code>.
   *
   * @param upperIncluded
   *    <code>true</code> if the upper bound is included, <code>false</code>
   *    otherwise.
   */
  void setUpperIncluded(boolean upperIncluded);

  /**
   * Changes the scope to no longer cover any elements in covered by the
   * specified scope.
   * @param scope
   *    a <code>MOScope</code> instance that defines the range of OIDs
   *    that should be no longer in this scope.
   */
  void substractScope(MOScope scope);

}
