/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - UpdateStrategy.java  
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
 * An <code>UpdateStrategy</code> defines how {@link UpdatableManagedObject}s
 * are updated on behalf of server queries to a {@link MOServer}.
 *
 * @author Frank Fock
 * @version 1.2
 */
public interface UpdateStrategy {

  /**
   * Checks whether the supplied {@link UpdatableManagedObject} needs to be
   * updated or not to serve the supplied query.
   *
   * @param server
   *    the MOServer trying to access the <code>mo</code> (can be
   *    <code>null</code> if no such server is known).
   * @param mo
   *    the accessed updatable managed object.
   * @param query
   *    the query accessing the <code>mo</code>.
   * @return
   *    <code>true</code> if the server (or any other interested instance)
   *   should call {@link UpdatableManagedObject#update(MOQuery updateScope)}
   *   to update <code>mo</code>'s content.
   */
  boolean isUpdateNeeded(MOServer server,
                         UpdatableManagedObject mo, MOQuery query);

}
