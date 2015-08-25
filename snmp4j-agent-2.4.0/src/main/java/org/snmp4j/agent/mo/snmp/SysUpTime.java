/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SysUpTime.java  
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

import org.snmp4j.smi.TimeTicks;

/**
 * The <code>SysUpTime</code> interface defines how the system's up-time can
 * be accessed.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface SysUpTime {

  /**
   * Returns the system's up-time in 1/100 seconds.
   * @return
   *    a TimeTicks instance.
   */
  TimeTicks get();

}
