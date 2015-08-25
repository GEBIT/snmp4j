/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TDomainAddressFactory.java  
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
import org.snmp4j.smi.Address;

/**
 * The <code>TDomainAddressFactory</code> defines the interface for address
 * factories that can create an address from a transport domain ID and a
 * TDomainAddress textual convention conforming <code>OctetString</code> value
 * and vice versa.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface TDomainAddressFactory {

  /**
   * Creates an <code>Address</code> from a transport domain ID and a
   * TDomainAddress textual convention conforming <code>OctetString</code>
   * value.
   * @param transportDomain
   *    a transport domain ID as defined by {@link TransportDomains}.
   * @param address
   *    a TDomainAddress TC conforming <code>OctetString</code>.
   * @return
   *    an <code>Address</code> if <code>address</code> could be mapped or
   *    <code>null</code> if not.
   */
  Address createAddress(OID transportDomain, OctetString address);

  /**
   * Checks whether a transport domain ID and a <code>OctetString</code> value
   * represent a valid and consistent address.
   * @param transportDomain
   *    a transport domain ID as defined by {@link TransportDomains}.
   * @param address
   *    an <code>OctetString</code>.
   * @return
   *    <code>true</code> if <code>transportDomain</code> and
   *    <code>address</code> are consitent and valid.
   */
  boolean isValidAddress(OID transportDomain, OctetString address);

  /**
   * Gets the transport domain(s) ID for the specified address.
   *
   * @param address
   *    an address.
   * @return
   *    the corresponding transport domain ID as defined by
   *    {@link TransportDomains} or <code>null</code> if the address cannot be
   *    mapped.
   */
  OID[] getTransportDomain(Address address);

  /**
   * Gets the TDomainAddress textual convention conforming
   * <code>OctetString</code> value for the specified address.
   * @param address
   *    an address.
   * @return
   *    a TDomainAddress <code>OctetString</code> value or <code>null</code>
   *    if the address cannot be mapped.
   */
  OctetString getAddress(Address address);

}
