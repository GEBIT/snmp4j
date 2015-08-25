/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - UpdatableMOSupport.java  
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

import java.lang.ref.WeakReference;

/**
 * The <code>UpdatableMOSupport</code> class provides support for update
 * {@link UpdatableManagedObject}s.
 *
 * @author Frank Fock
 * @version 1.2
 * @since 1.2
 */
public class UpdatableMOSupport {

  private Date lastUpdate;
  private WeakReference<Object> lastUpdateSource;

  /**
   * Creates a <code>UpdatableMOSupport</code> with undefined
   * (<code>null</code>) last update time and source.
   */
  public UpdatableMOSupport() {
  }


  /**
   * Gets the date and time of the last update.
   *
   * @return the Date when the last
   * {@link UpdatableManagedObject#update(MOQuery updateScope)} has been called.
   */
  public Date getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Sets the last update date and time.
   * @param lastUpdate
   *    the date and time of the last successful update or <code>null</code>
   *    the reset/update a managed object on the next access.
   */
  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * Set the last update to the current time.
   */
  public void setLastUpdateNow() {
    this.lastUpdate = new Date();
  }

  /**
   * Gets the object that triggered the last update of this managed object.
   *
   * @return an object or <code>null</code> if the source of the last update
   *   is unknown/undefined.
   */
  public Object getLastUpdateSource() {
    return (lastUpdateSource == null) ? null : lastUpdateSource.get();
  }

  /**
   * Sets the source object of the last update. The object is not directly
   * referenced by this <code>UpdatableMOSupport</code>. Instead, a
   * {@link WeakReference} is used, so that the object source can be garbage
   * collected if it is not referenced elsewhere.
   *
   * @param source
   *    an Object that identifies an update source.
   */
  public void setLastUpdateSource(Object source) {
    this.lastUpdateSource = new WeakReference<Object>(source);
  }
}
