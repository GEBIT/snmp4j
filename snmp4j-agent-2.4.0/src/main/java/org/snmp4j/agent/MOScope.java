/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOScope.java  
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
 * A managed object scope defines a continuous region within the global OID
 * space.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOScope {

  /**
   * Gets the lower bound OID of the scope. Whether the lower bound is included
   * or excluded from the scope's region is determined by
   * {@link #isLowerIncluded()}.
   *
   * @return
   *    an OID.
   */
  OID getLowerBound();

  /**
   * Gets the upper bound OID of the scope. Whether the upper bound is included
   * or excluded from the scope's region is determined by
   * {@link #isUpperIncluded()}.

   * @return OID
   */
  OID getUpperBound();

  /**
   * Indicates whether the lower bound OID is included in the scope or not.
   * @return
   *    <code>true</code> if the lower bound is included.
   */
  boolean isLowerIncluded();

  /**
   * Indicates whether the upper bound OID is included in the scope or not.
   * @return
   *    <code>true</code> if the upper bound is included.
   */
  boolean isUpperIncluded();

  /**
   * Checks whether the supplied scope is covered by this scope.
   * @param other
   *    the <code>MOScope</code> to check
   * @return
   *    <code>true</code> if the lower bound of <code>other</code> is greater
   *    or equal than the lower bound of this scope and if the upper bound of
   *    <code>other</code> is lower or equal than the upper bound of this scope.
   */
  boolean isCovered(MOScope other);

  /**
   * Checks whether the supplied scope overlap with this one, thus sharing at
   * least one OID with the supplied one.
   * @param other
   *   a <code>MOScope</code>.
   * @return
   *   <code>true</code> if there exists at least one OID that is included in
   *   both scopes.
   */
  boolean isOverlapping(MOScope other);

  /**
   * Checks if this scope covers the supplied OID.
   * @param oid
   *    an OID.
   * @return
   *    <code>true</code> if <code>oid</code> is greater or equal the scope's
   *    lower bound and if it is less or equal its upper bound.
   */
  boolean covers(OID oid);

}
