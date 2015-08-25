/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOAccess.java  
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
 * The <code>MOAccess</code> interface defines means to determine the maximum
 * access supported by a managed object.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOAccess {

  /**
   * Check whether the managed object can be read (i.e. accessed by GET,
   * GETNEXT, or GETBULK requests).
   * @return
   *    <code>true</code> if the managed object instance(s) can be read.
   */
  boolean isAccessibleForRead();

  /**
   * Check whether the managed object can be written (i.e. accessed by SET
   * requests).
   * @return
   *    <code>true</code> if the managed object instance(s) can be written.
   */
  boolean isAccessibleForWrite();

  /**
   * Check whether the managed object can be send in a notification.
   * @return
   *    <code>true</code> if the managed object instance(s) can be send in a
   *    notification.
   */
  boolean isAccessibleForNotify();

  /**
   * Check whether the managed object can be created (through a SET request on
   * a non existant instance of a columnar object).
   * @return
   *    <code>true</code> if the managed object instance(s) can be created.
   */
  boolean isAccessibleForCreate();
}
