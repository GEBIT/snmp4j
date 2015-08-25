/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DeniableEventObject.java  
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

package org.snmp4j.agent.mo;

import java.util.EventObject;

/**
 * The <code>DeniableEventObject</code> describes an event that can be canceled
 * through reporting a SNMP error status to the event source.
 *
 * @author Frank Fock
 * @version 1.1
 * @since 1.1
 */
public class DeniableEventObject extends EventObject {

  private static final long serialVersionUID = 8808826350825049569L;

  private int denyReason = 0;
  private boolean deniable = false;

  /**
   * Creates an deniable event instance.
   * @param source
   *   the event source.
   * @param deniable
   *   if <code>true</code> the event can be canceled by setting its deny
   *   reason to a SNMPv2/v3 error status, <code>false</code> if the event
   *   cannot be canceled, because, for example, it is fired on behalf of
   *   the commit phase of a 2PC transaction.
   */
  public DeniableEventObject(Object source, boolean deniable) {
    super(source);
    this.deniable = deniable;
  }

  /**
   * Sets the reason why this event needs to be canceled. A reason other than
   * zero will cancel the change if it has not been performed yet.
   * @param denyReason
   *    a SNMPv2/v3 error status.
   */
  public void setDenyReason(int denyReason) {
    this.denyReason = denyReason;
  }

  /**
   * Returns the reason (i.e., SNMPv2/v3 error status) that indicates the error
   * condition that caused this event to be canceled.
   * @return
   *    a SNMP2v/v3 error status.
   */
  public int getDenyReason() {
    return denyReason;
  }

  /**
   * Checks whether this event is fired in the preparation phase or the commit
   * phase of the 2PC.
   * @return
   *    <code>true</code> if the event can be canceled and thus the event has
   *    been fired on behalf of the preparation phase and <code>false</code>
   *    if it has been fired on behalf of the commit phase.
   */
  public boolean isDeniable() {
    return deniable;
  }

}
