/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOChangeEvent.java  
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

import org.snmp4j.agent.*;
import org.snmp4j.smi.*;

/**
 * The <code>MOChangeEvent</code> describes the change of a single value of
 * a <code>ManagedObject</code>.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOChangeEvent extends DeniableEventObject {

  private static final long serialVersionUID = 2377168127200875177L;

  private ManagedObject changedObject;
  private OID oid;
  private Variable oldValue;
  private Variable newValue;

  /**
   * Creates a deniable <code>MOChangeEvent</code> object based on the changed
   * managed object, the instance OID of the changed value, with old and new
   * value.
   * @param source
   *    the event source.
   * @param changedObject
   *    the <code>ManagedObject</code> whose value is changed.
   * @param oid
   *    the instance OID of the changed instance.
   * @param oldValue
   *    the old value.
   * @param newValue
   *    the new value.
   */
  public MOChangeEvent(Object source, ManagedObject changedObject,
                       OID oid, Variable oldValue, Variable newValue) {
    this(source, changedObject, oid, oldValue, newValue, true);
  }

  /**
   * Creates a <code>MOChangeEvent</code> object based on the changed managed
   * object, the instance OID of the changed value, with old and new value.
   * @param source
   *    the event source.
   * @param changedObject
   *    the <code>ManagedObject</code> whose value is changed.
   * @param oid
   *    the instance OID of the changed instance.
   * @param oldValue
   *    the old value.
   * @param newValue
   *    the new value.
   * @param deniable
   *    indicates whether the event can be canceled through setting its
   *    denyReason member to a SNMP error status.
   * @since 1.1
   */
  public MOChangeEvent(Object source, ManagedObject changedObject,
                       OID oid, Variable oldValue, Variable newValue,
                       boolean deniable) {
    super(source, deniable);
    this.changedObject = changedObject;
    this.oid = oid;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public ManagedObject getChangedObject() {
    return changedObject;
  }

  public OID getOID() {
    return oid;
  }

  public Variable getOldValue() {
    return oldValue;
  }

  public Variable getNewValue() {
    return newValue;
  }
}
