/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOFilter.java  
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

import org.snmp4j.agent.ManagedObject;

/**
 * The <code>MOFilter</code> interface can be used to filter
 * {@link ManagedObject}s.
 *
 * @author Frank Fock
 * @version 1.12
 * @since 1.12
 */
public interface MOFilter {

  /**
   * Checks whether the given {@link ManagedObject} passes this filter.
   * @param mo
   *    a ManagedObject instance.
   * @return
   *    <code>true</code> if it passes the filter, <code>false</code> otherwise.
   */
  boolean passesFilter(ManagedObject mo);

}
