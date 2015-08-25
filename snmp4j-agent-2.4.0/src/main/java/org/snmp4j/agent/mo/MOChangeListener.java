/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOChangeListener.java  
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

import java.util.*;

/**
 * A <code>MOChangeListener</code> is informed about changes and change attempts
 * of managed objects. By changing the deny reason member of the supplied
 * event object changes can also be canceled.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOChangeListener extends EventListener {

  /**
   * A ManagedObject change is being prepared. To cancel preparation set the
   * deny reason to a SNMPv2/v3 error status.
   * @param changeEvent
   *    the change event object.
   */
  void beforePrepareMOChange(MOChangeEvent changeEvent);

  /**
   * A change has been prepared. Setting the deny reason of the supplied event
   * object will be ignored.
   * @param changeEvent
   *    the change event object.
   */
  void afterPrepareMOChange(MOChangeEvent changeEvent);

  /**
   * A ManagedObject change is being committed. To cancel the commit phase set
   * the deny reason to a SNMPv2/v3 error status.
   * <p>
   * NOTE: Canceling the commit phase must be avoided. Setting a deny reason
   * has only an effect if {@link MOChangeEvent#isDeniable()} returns
   * <code>true</code>. Otherwise, you will need to throw an exception.
   *
   * @param changeEvent
   *    the change event object.
   */
  void beforeMOChange(MOChangeEvent changeEvent);

  /**
   * A change has been committed. Setting the deny reason of the supplied event
   * object will be ignored.
   * @param changeEvent
   *    the change event object.
   */
  void afterMOChange(MOChangeEvent changeEvent);

}
