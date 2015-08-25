/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOValueValidationEvent.java  
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

import org.snmp4j.smi.*;

/**
 * The <code>MOValueValidationEvent</code> class represents a value validation
 * request. The request's result is carries in its status member.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOValueValidationEvent extends EventObject {

  private Variable newValue;
  private Variable oldValue;
  private int validationStatus = 0;

  /**
   * Creates a new value validation request.
   * @param source
   *    the event source (request creator).
   * @param oldValue
   *    the old value.
   * @param newValue
   *    the new value to validate.
   */
  public MOValueValidationEvent(Object source,
                                Variable oldValue, Variable newValue) {
    super(source);
    this.newValue = newValue;
    this.oldValue = oldValue;
  }

  /**
   * Gets the validation status.
   * @return
   *    an SNMP error status or zero if validation was successful.
   */
  public int getValidationStatus() {
    return validationStatus;
  }

  public Variable getNewValue() {
    return newValue;
  }

  public Variable getOldValue() {
    return oldValue;
  }

  /**
   * Sets the validation status. The default status is zero.
   * @param validationStatus
   *    a SNMP error status.
   */
  public void setValidationStatus(int validationStatus) {
    this.validationStatus = validationStatus;
  }

}
