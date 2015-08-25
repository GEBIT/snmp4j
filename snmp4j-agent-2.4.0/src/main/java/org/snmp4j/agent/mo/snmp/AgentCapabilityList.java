/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - AgentCapabilityList.java  
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

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.agent.mo.MOTableRow;

/**
 * The agent capabilities list exports information about the agent's
 * capabilities.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface AgentCapabilityList {

  /**
   * Add a capabilities description to the exported list.
   * @param sysORID
   *    the OID of an AGENT-CAPABILITIES statement.
   * @param sysORDescr
   *    A textual description of the capabilities identified
   *    by the corresponding instance of sysORID.
   * @return
   *    the index OID of the entry that uniquely identifies it.
   */
  OID addSysOREntry(OID sysORID, OctetString sysORDescr);

  /**
   * Remove a capabilities description from the exported list.
   * @param index
   *    the index OID previously returned by {@link #addSysOREntry}.
   * @return MOTableRow
   *    the removed sysOREntry row or <code>null</code> if such a row
   *    could not be found.
   */
  MOTableRow removeSysOREntry(OID index);
}
