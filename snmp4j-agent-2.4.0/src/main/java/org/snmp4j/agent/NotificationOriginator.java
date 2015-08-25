/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - NotificationOriginator.java  
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

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.smi.TimeTicks;

/**
 * The <code>NotificationOriginator</code> specifies the interface for
 * classes providing notification sending.
 *
 * <p>
 * See also RFC 3411 for a description of notification originators.
 * </p>
 * @author Frank Fock
 * @version 1.0
 */
public interface NotificationOriginator {

  /**
   * Sends notifications (traps) to all appropriate notification targets.
   * The targets to notify are determined through the SNMP-TARGET-MIB and
   * the SNMP-NOTIFICATION-MIB.
   *
   * @param context
   *    the context name of the context on whose behalf this notification has
   *    been generated.
   * @param notificationID
   *    the object ID that uniquely identifies this notification. For SNMPv1
   *    traps, the notification ID has to be build using the rules provided
   *    by RFC 2576.
   * @param vbs
   *    an array of <code>VariableBinding</code> instances representing the
   *    payload of the notification.
   * @return
   *    an array of ResponseEvent instances. Since the
   *    <code>NotificationOriginator</code> determines on behalf of the
   *    SNMP-NOTIFICTON-MIB contents whether a notification is sent as
   *    trap/notification or as inform request, the returned array contains
   *    an element for each addressed target, but only a response PDU for
   *    inform targets.
   */
  Object notify(OctetString context,
                OID notificationID, VariableBinding[] vbs);

  /**
   * Sends notifications (traps) to all appropriate notification targets.
   * The targets to notify are determined through the SNMP-TARGET-MIB and
   * the SNMP-NOTIFICATION-MIB.
   *
   * @param context
   *    the context name of the context on whose behalf this notification has
   *    been generated.
   * @param notificationID
   *    the object ID that uniquely identifies this notification. For SNMPv1
   *    traps, the notification ID has to be build using the rules provided
   *    by RFC 2576.
   * @param sysUpTime
   *    the value of the sysUpTime for the context <code>context</code>. This
   *    value will be included in the generated notification as
   *    <code>sysUpTime.0</code>.
   * @param vbs
   *    an array of <code>VariableBinding</code> instances representing the
   *    payload of the notification.
   * @return
   *    an array of ResponseEvent instances. Since the
   *    <code>NotificationOriginator</code> determines on behalf of the
   *    SNMP-NOTIFICTON-MIB contents whether a notification is sent as
   *    trap/notification or as inform request, the returned array contains
   *    an element for each addressed target, but only a response PDU for
   *    inform targets.
   */
  Object notify(OctetString context,
                OID notificationID,
                TimeTicks sysUpTime,
                VariableBinding[] vbs);

}
