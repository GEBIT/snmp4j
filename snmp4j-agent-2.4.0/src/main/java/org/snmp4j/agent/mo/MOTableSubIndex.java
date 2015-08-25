/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableSubIndex.java  
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
// JavaDoc
import org.snmp4j.smi.SMIConstants;


/**
 * The <code>MOTableSubIndex</code> class represents a sub-index definition.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOTableSubIndex {

  private int smiSyntax;
  private int minLength = 1;
  private int maxLength = 1;
  private OID oid;

  /**
   * Creates a sub-index definition based on a SMI syntax.
   * @param smiSyntax
   *    a SMI syntax ID as defined by {@link SMIConstants}.
   */
  public MOTableSubIndex(int smiSyntax) {
    this.smiSyntax = smiSyntax;
  }

  /**
   * Creates a sub-index definition based on a SMI syntax and the OID of the
   * sub-index OBJECT-TYPE definition.
   * @param oid
   *    the OID of the sub-index definition's OBJECT-TYPE.
   * @param smiSyntax
   *    a SMI syntax ID as defined by {@link SMIConstants}.
   */
  public MOTableSubIndex(OID oid, int smiSyntax) {
    this(smiSyntax);
    this.oid = oid;
  }

  /**
   * Creates a sub-index definition based on a SMI syntax, minimum, and maximum
   * sub-index length.
   * @param smiSyntax
   *    a SMI syntax ID as defined by {@link SMIConstants}.
   * @param minLength
   *    the minimum length of the sub-index (must not be greater than
   *    <code>maxLength</code>).
   * @param maxLength
   *    the maximum length of the sub-index (must not be less than
   *    <code>minLength</code>).
   */
  public MOTableSubIndex(int smiSyntax, int minLength, int maxLength) {
    this(smiSyntax);
    if (minLength > maxLength) {
      throw new IllegalArgumentException();
    }
    this.minLength = minLength;
    this.maxLength = maxLength;
  }

  /**
   * Creates a sub-index definition based on a SMI syntax, minimum, and maximum
   * sub-index length as well as the OID of the sub-index OBJECT-TYPE.
   * @param oid
   *    the OID of the sub-index definition's OBJECT-TYPE.
   * @param smiSyntax
   *    a SMI syntax ID as defined by {@link SMIConstants}.
   * @param minLength
   *    the minimum length of the sub-index (must not be greater than
   *    <code>maxLength</code>).
   * @param maxLength
   *    the maximum length of the sub-index (must not be less than
   *    <code>minLength</code>).
   */
  public MOTableSubIndex(OID oid, int smiSyntax, int minLength, int maxLength) {
    this(smiSyntax, minLength, maxLength);
    this.oid = oid;
  }

  /**
   * Gets the SMI syntax of the sub-index.
   * @return
   *    a SMI syntax as defined by {@link SMIConstants}.
   */
  public int getSmiSyntax() {
    return smiSyntax;
  }

  /**
   * Returns the minimum sub-index length.
   * @return
   *    the minimum length.
   */
  public int getMinLength() {
    return minLength;
  }

  /**
   * Returns the maximum sub-index length.
   * @return
   *    the maximum length.
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * Returns the optional OID of the sub-index object definition.
   * @return
   *    the OID of the sub-index object or <code>null</code> if that has not
   *    been specified on sub-index creation.
   */
  public OID getOid() {
    return oid;
  }
}
