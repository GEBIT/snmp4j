/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableRowListener.java  
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
 * A <code>MOTableRowListener</code> can be informed about row changes.
 *
 * @author Frank Fock
 * @version 2.2
 */
public interface MOTableRowListener<R extends MOTableRow> extends EventListener {

  /**
   * A column or a complete row is changed/has been changed.
   * @param event
   *    a <code>MOTableRowEvent</code> describing the event. To veto the event
   *    the {@link MOTableRowEvent#setVetoStatus} and optionally also the
   *    {@link MOTableRowEvent#setVetoColumn} can be called.
   */
  void rowChanged(MOTableRowEvent<R> event);

}
