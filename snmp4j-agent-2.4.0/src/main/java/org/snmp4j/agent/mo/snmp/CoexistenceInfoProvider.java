/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - CoexistenceInfoProvider.java  
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


package org.snmp4j.agent.mo.snmp;

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Address;

/**
 * A <code>CoexistenceInfoProvider</code> provides access to coexistence
 * information.
 *
 * @author Frank Fock
 * @version 1.2.1
 */
public interface CoexistenceInfoProvider {

  /**
   * Gets the information needed for community based security protocols to
   * coexistence with the SNMPv3 security model.
   *
   * @param community
   *    a community OctetString that identifies the coexistence information
   *    required.
   * @return
   *    the CoexistenceInfo instances with security name,
   *    context engine ID, context, and optionally a transport tag
   */
  CoexistenceInfo[] getCoexistenceInfo(OctetString community);

  /**
   * Returns the community associated with the supplied security name.
   * @param securityName
   *    the security name.
   * @param contextEngineID
   *    the context engine ID of the remote target (when proxying) otherwise
   *    the local engine ID.
   * @param contextName
   *    the context name (default is an empty string).
   * @return OctetString
   *    the associated community string or <code>null</code> if such an
   *    association does not exists.
   */
  OctetString getCommunity(OctetString securityName,
                           OctetString contextEngineID,
                           OctetString contextName);

  /**
   * Checks whether the supplied address passes the source address filtering
   * provided for community based security models.
   *
   * @param address
   *    the source Address of a SNMP message.
   * @param coexistenceInfo
   *    a set of coexistence information (returned by
   *    {@link #getCoexistenceInfo}) that provides the transport tag
   *    used to identify allowed source addresses. On return, the maximum
   *    message size attribute of the coexistence info set will be set
   *    according to the values defined for the matched source address in
   *    the snmpTargetAddrExtTable.
   * @return
   *    <code>true</code> if the address passes the filter, <code>false</code>
   *    otherwise.
   */
  boolean passesFilter(Address address, CoexistenceInfo coexistenceInfo);

}
