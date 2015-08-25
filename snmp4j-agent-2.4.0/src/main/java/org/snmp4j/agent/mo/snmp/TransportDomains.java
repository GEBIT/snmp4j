/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TransportDomains.java  
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

/**
 * The <code>TransportDomains</code> class defines constant OIDs for the
 * various transport types defined by the INET-ADDRESS-MIB.
 *
 * @author Frank Fock
 * @version 2.0
 */
public final class TransportDomains {

  public static final OID snmpUDPDomain =
      new OID(new int[] { 1,3,6,1,6,1,1 });
  public static final OID snmpTLSTCPDomain =
      new OID(new int[] { 1,3,6,1,6,1,8 });
  public static final OID snmpTLSUDPDomain =
      new OID(new int[] { 1,3,6,1,6,1,9 });

  public static final OID transportDomainUdpIpv4 =
      new OID(new int[] { 1,3,6,1,2,1,100,1,1 });
  public static final OID transportDomainUdpIpv6 =
      new OID(new int[] { 1,3,6,1,2,1,100,1,2 });
  public static final OID transportDomainUdpIpv4z =
      new OID(new int[] { 1,3,6,1,2,1,100,1,3 });
  public static final OID transportDomainUdpIpv6z =
      new OID(new int[] { 1,3,6,1,2,1,100,1,4 });
  public static final OID transportDomainTcpIpv4 =
      new OID(new int[] { 1,3,6,1,2,1,100,1,5 });
  public static final OID transportDomainTcpIpv6 =
      new OID(new int[] { 1,3,6,1,2,1,100,1,6 });
  public static final OID transportDomainTcpIpv4z =
      new OID(new int[] { 1,3,6,1,2,1,100,1,7 });
  public static final OID transportDomainTcpIpv6z =
      new OID(new int[] { 1,3,6,1,2,1,100,1,8 });
  public static final OID transportDomainUdpDns =
      new OID(new int[] { 1,3,6,1,2,1,100,1,14 });
  public static final OID transportDomainTcpDns =
      new OID(new int[] { 1,3,6,1,2,1,100,1,15 });

}
