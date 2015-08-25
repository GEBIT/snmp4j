/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOGroup.java  
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

/**
 * A <code>MOGroup</code> instance is a group of <code>ManagedObject</code>s.
 * A group can include the managed objects from a complete MIB module or only
 * a few objects.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOGroup {

  /**
   * Registers the managed objects of this group with the server for the
   * supplied context.
   *
   * @param server
   *    the <code>MOServer</code> where to register the managed objects.
   * @param context
   *    the context to use (may be <code>null</code> if no specific context
   *    is selected).
   * @throws DuplicateRegistrationException
   *    if a managed object's scope (i.e. lower bound) is already registered
   *    at the server.
   */
  void registerMOs(MOServer server, OctetString context)
      throws DuplicateRegistrationException;

  /**
   * Unregisters the managed objects of this group from the supplied server and
   * from the supplied context.
   * @param server
   *    the <code>MOServer</code> where to unregister the managed objects.
   * @param context
   *    the context to use (may be <code>null</code> if no specific context
   *    is selected).
   */
  void unregisterMOs(MOServer server, OctetString context);

}
