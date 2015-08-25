/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ProxyForwarder.java  
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

/**
 * The <code>ProxyForwarder</code> class represents a proxy forwarder instance
 * as defined by RFC 3413.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface ProxyForwarder {

  int PROXY_TYPE_ALL = 0;
  int PROXY_TYPE_READ = 1;
  int PROXY_TYPE_WRITE = 2;
  int PROXY_TYPE_NOTIFY = 3;
  int PROXY_TYPE_INFORM = 4;

  /**
   * Forwards a <code>Request</code> if it matches the criteria defined
   * by the SNMP-PROXY-MIB associated with this proxy forwarder.
   * @param request
   *    the proxy forward request. If the request has been forwarded
   *    successfully to a single target, then the <code>responsePDU</code>
   *    will be set to the response PDU received from the target entity.
   * @return
   *    <code>true</code> if the request has been forwarded, <code>false</code>
   *    otherwise.
   */
  boolean forward(ProxyForwardRequest request);

}
