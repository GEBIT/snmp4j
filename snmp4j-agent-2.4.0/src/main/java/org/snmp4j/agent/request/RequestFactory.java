/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - RequestFactory.java  
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


package org.snmp4j.agent.request;

import java.util.EventObject;
import org.snmp4j.agent.mo.snmp.CoexistenceInfo;
// for JavaDoc
import org.snmp4j.CommandResponderEvent;

/**
 * The <code>RequestFactory</code> is a factory for (SNMP/AgentX) requests.
 * The parameter S is a subclass of {@link java.util.EventObject} that
 * specifies the type of the request source.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface RequestFactory<S extends EventObject,R,REQ extends Request<S,R,? extends SubRequest>> {

  /**
   * Creates a <code>Request</code> from a <code>CommandResponderEvent</code>
   * responder event.
   * @param initiatingEvent
   *    the initiating event instance, which is a
   *    {@link CommandResponderEvent} instance for SNMP4J by default.
   * @param cinfo
   *    optional coexistence information that provides context and context
   *    engine ID independently from the SNMP version of the initiatingEvent.
   * @return
   *    a Request instance.
   */
  REQ createRequest(S initiatingEvent, CoexistenceInfo cinfo);

}
