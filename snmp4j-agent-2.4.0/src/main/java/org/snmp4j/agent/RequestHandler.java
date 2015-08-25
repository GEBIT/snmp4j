/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - RequestHandler.java  
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

import org.snmp4j.agent.request.Request;
// For JavaDoc
import org.snmp4j.PDU;
import org.snmp4j.agent.request.SubRequest;

/**
 * A request handler is able to process a request on behalf of the managed
 * object repository represented by a {@link MOServer}.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface RequestHandler<R extends Request> {

  /**
   * Checks whether the supplied PDU type is supported by this request handler.
   *
   * @param pduType
   *    a PDU type as defined by {@link PDU}.
   * @return
   *    <code>true</code> if the PDU type is supported.
   */
  boolean isSupported(int pduType);

  /**
   * Processes a request on behalf of the supplied {@link MOServer}.
   * @param request
   *    a <code>Request</code> instance.
   * @param server
   *    a <code>MOServer</code> containing the managed objects accessible
   *    by the request.
   */
  void processPdu(R request, MOServer server);

}
