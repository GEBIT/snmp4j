/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - NotificationLogEvent.java  
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

import java.util.EventObject;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.Target;

/**
 * A <code>NotificationLogEvent</code> object describes the event when a
 * notification has been received from a SNMP entity (locally or remotely).
 *
 * @author Frank Fock
 * @version 1.4
 * @since 1.4
 */
public class NotificationLogEvent extends EventObject {

  private OctetString originatorEngineID;
  private Target originatorTarget;
  private OctetString context;
  private OctetString contextEngineID;
  private OID notificationID;
  private TimeTicks sysUpTime;
  private VariableBinding[] variables;
  private long eventID;
  private boolean subEvent;


  public NotificationLogEvent(Object source, OctetString originatorEngineID,
                              Target originatorTarget,
                              OctetString contextEngineID,
                              OctetString context,
                              OID notificationID, TimeTicks sysUpTime,
                              VariableBinding[] variables,
                              long notificationEventID,
                              boolean subEvent) {
    super(source);
    this.originatorEngineID = originatorEngineID;
    this.originatorTarget = originatorTarget;
    this.contextEngineID = contextEngineID;
    this.context = context;
    this.notificationID = notificationID;
    this.sysUpTime = sysUpTime;
    this.variables = variables;
    this.eventID = notificationEventID;
    this.subEvent = subEvent;
  }

  public VariableBinding[] getVariables() {
    return variables;
  }

  public TimeTicks getSysUpTime() {
    return sysUpTime;
  }

  public Target getOriginatorTarget() {
    return originatorTarget;
  }

  public OctetString getOriginatorEngineID() {
    return originatorEngineID;
  }

  public OID getNotificationID() {
    return notificationID;
  }

  public OctetString getContext() {
    return context;
  }

  public OctetString getContextEngineID() {
    return contextEngineID;
  }

  public long getEventID() {
    return eventID;
  }

  public boolean isSubEvent() {
    return subEvent;
  }

}
