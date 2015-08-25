/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - UpdatableManagedObject.java  
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

import java.util.Date;

/**
 * The <code>UpdatableManagedObject</code> interface defines the basic
 * operations for all SNMP(4J) manageable objects that need to be updated
 * to reflect the up-to-date state of the managed object.
 *
 * @author Frank Fock
 * @since 1.2
 * @version 1.2
 */
public interface UpdatableManagedObject extends ManagedObject {

  /**
   * Gets the date and time of the last update. If that time cannot be
   * determined <code>null</code> is returned.
   *
   * @return
   *    the Date when the last {@link #update(MOQuery updateScope)} has
   *    been called.
   */
  Date getLastUpdate();

  /**
   * Gets the object that triggered the last update of this managed object.
   * The returned object reference may be used to check if an update has
   * already been performed for the specified source, which is typically a
   * SNMP request.
   *
   * @return
   *    an object or <code>null</code> if the source of the last update is
   *    unknown/undefined.
   */
  Object getLastUpdateSource();

  /**
   * Update the content of the managed object that is covered by the supplied
   * scope.
   *
   * @param updateScope
   *    the query that triggered the update and thus defining the update scope.
   *    If <code>null</code> the whole managed object has to be updated.
   */
  void update(MOQuery updateScope);

}
