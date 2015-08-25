/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ValueConstraintValidator.java  
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

package org.snmp4j.agent.mo.snmp.smi;

import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.*;
// For JavaDOC:
import org.snmp4j.mp.SnmpConstants;

/**
 * A <code>ValueConstraintValidator</code> adapts a {@link ValueConstraint}
 * as {@link MOValueValidationListener}.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class ValueConstraintValidator implements MOValueValidationListener {

  private ValueConstraint valueConstraint;

  public ValueConstraintValidator(ValueConstraint valueConstraint) {
    this.valueConstraint = valueConstraint;
  }

  /**
   * Validates a value by returning a SNMP error code if validation fails
   * or 0 ({@link SnmpConstants#SNMP_ERROR_SUCCESS}) if the validation was
   * successful. The validation is returned by calling the
   * {@link MOValueValidationEvent#setValidationStatus} method. If an error
   * occured the returned status value should be one of the following values:
   *    <ul>
   *    <li>{@link SnmpConstants#SNMP_ERROR_WRONG_LENGTH}</li>
   *    <li>{@link SnmpConstants#SNMP_ERROR_WRONG_VALUE}</li>
   *    <li>{@link SnmpConstants#SNMP_ERROR_WRONG_TYPE}</li>
   *    <li>{@link SnmpConstants#SNMP_ERROR_WRONG_ENCODING}</li>
   *    <li>{@link SnmpConstants#SNMP_ERROR_BAD_VALUE}</li>
   *    </ul>
   *
   * @param validationEvent
   *    the <code>MOValueValidationEvent</code> containing the value to
   *    validate.
   */
  public void validate(MOValueValidationEvent validationEvent) {
    Variable v = validationEvent.getNewValue();
    validationEvent.setValidationStatus(valueConstraint.validate(v));
  }

  public ValueConstraint getValueConstraint() {
    return valueConstraint;
  }
}
