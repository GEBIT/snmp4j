/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - NotificationTask.java  
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

import org.snmp4j.event.*;
import org.snmp4j.smi.*;
import org.snmp4j.util.WorkerTask;

/**
 * The <code>NotificationTask</code> is a <code>Runnable</code> that sends
 * a notifcation or a series of notifications/traps/informs - depending
 * on the configuration associated with the supplied
 * <code>NotificationOriginator</code>.
 *
 * @author Frank Fock
 * @version 1.9
 */
public class NotificationTask implements WorkerTask {

  private NotificationOriginator notificationOriginator;
  private OctetString context;
  private OID notificationID;
  private TimeTicks sysUpTime;
  private VariableBinding[] vbs;
  private ResponseEvent[] responses;

  public NotificationTask(NotificationOriginator notificationOriginator,
                          OctetString context,
                          OID notificationID,
                          TimeTicks sysUptime,
                          VariableBinding[] vbs) {
    this.notificationOriginator = notificationOriginator;
    this.context = context;
    this.notificationID = notificationID;
    this.sysUpTime = sysUptime;
    this.vbs = vbs;
  }

  /**
   * Send the notification a notify this object afterwards.
   */
  public synchronized void run() {
    if (sysUpTime != null) {
      this.responses = (ResponseEvent[])
          notificationOriginator.notify(context, notificationID,
                                        sysUpTime, vbs);
    }
    else {
      this.responses = (ResponseEvent[])
          notificationOriginator.notify(context, notificationID, vbs);
    }
    notify();
  }

  public OctetString getContext() {
    return context;
  }

  public OID getNotificationID() {
    return notificationID;
  }

  public NotificationOriginator getNotificationOriginator() {
    return notificationOriginator;
  }

  /**
   * Returns an array of ResponseEvent instances. Since the
   * <code>NotificationOriginator</code> determines on behalf of the
   * SNMP-NOTIFICTON-MIB contents whether a notification is sent as
   * trap/notification or as inform request, the returned array contains
   * an element for each addressed target, but only a response PDU for
   * inform targets.
   * @return
   *    an array of ResponseEvent instances (informs) or <code>null</code>
   *    values (for traps/notifications).
   */
  public ResponseEvent[] getResponses() {
    return responses;
  }

  public TimeTicks getSysUpTime() {
    return sysUpTime;
  }

  public VariableBinding[] getVariableBindings() {
    return vbs;
  }

  /**
   * This method does nothing by default, because this task cannot be terminated
   * gracefully while being executed.
   */
  public void terminate() {
  }

  public void join() throws InterruptedException {
  }

  public void interrupt() {
  }

}
