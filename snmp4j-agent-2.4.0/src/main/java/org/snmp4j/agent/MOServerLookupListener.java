/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOServerLookupListener.java  
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

import java.util.*;

/**
 * An object that is interested in callback notifications of lookup events
 * on a <code>MOServer</code> instance has to implement the
 * <code>MOServerLookupListener</code> interface.
 *
 * @author Frank Fock
 * @version 1.1
 */
public interface MOServerLookupListener extends EventListener {

  /**
   * A {@link MOServer} instance has looked up a managed object for which the
   * listener has been registered.
   * @param event
   *    a <code>MOServerLookupEvent</code> describing the lookup query and the
   *    managed object that has been looked up.
   */
  void lookupEvent(MOServerLookupEvent event);

  /**
   * A {@link MOServer} instance is about to check if the managed object for
   * which the listener had been registered matches a query. A managed object
   * with dynamic content like a non-static table might use this event to
   * update its content.
   *
   * @param event
   *    a <code>MOServerLookupEvent</code> describing the lookup query and the
   *    managed object that is to be queried.
   */
  void queryEvent(MOServerLookupEvent event);

}
