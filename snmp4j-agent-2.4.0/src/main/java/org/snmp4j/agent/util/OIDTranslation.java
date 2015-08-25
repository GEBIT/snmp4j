/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - OIDTranslation.java  
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

package org.snmp4j.agent.util;

import org.snmp4j.smi.OID;

/**
 * The definition of OID translation specifications.
 * Forward translation changes an OID from local to remote representation.
 * Backward translation changes an OID from remote to local representation.
 *
 * @author Frank Fock
 * @since 2.0
 * @version 2.0
 */
public interface OIDTranslation {

  /**
   * Translates (changes) an OID from local to remote representation.
   * @param oid
   *    a local object identifier that needs to be translated to its remote
   *    representation.
   * @return
   *    the translated OID.
   */
  OID forwardTranslate(OID oid);

  /**
   * Translates (changes) an OID from remote to local representation.
   * @param oid
   *    a remote object identifier that needs to be translated to its local
   *    representation.
   * @return
   *    the translated OID.
   */
  OID backwardTranslate(OID oid);

}
