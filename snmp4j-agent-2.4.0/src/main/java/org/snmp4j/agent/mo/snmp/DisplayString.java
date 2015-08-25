/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DisplayString.java  
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


package org.snmp4j.agent.mo.snmp;

import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.agent.mo.MOValueValidationEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.mo.snmp.smi.Constraints;
import org.snmp4j.agent.mo.snmp.smi.ConstraintsImpl;
import org.snmp4j.agent.mo.snmp.smi.Constraint;
import org.snmp4j.agent.mo.snmp.smi.ValueConstraintValidator;
import org.snmp4j.PDU;
import org.snmp4j.agent.mo.snmp.smi.ValueConstraint;

/**
 * The <code>DisplayString</code> class implements the DisplayString textual
 * convention as defined by the SNMPv2-TC MIB specification for columnar
 * objects.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class DisplayString<V extends OctetString> extends MOMutableColumn<V> {

  public static final int MIB_SIZE = 0;
  public static final int MAX_SIZE = 255;

  private Constraints sizeConstraints = new ConstraintsImpl();

  public DisplayString(int columnID,
                       MOAccess access,
                       V defaultValue,
                       boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue, mutableInService);
  }

  public DisplayString(int columnID,
                       MOAccess access,
                       V defaultValue) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue);
  }

  public DisplayString(int columnID,
                       MOAccess access,
                       V defaultValue,
                       boolean mutableInService, int minSize, int maxSize) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue, mutableInService);
    sizeConstraints.add(new Constraint(minSize, maxSize));
  }

  public synchronized int validate(V newValue, V oldValue) {
    int status = super.validate(newValue, oldValue);
    if (status == SnmpConstants.SNMP_ERROR_SUCCESS) {
      status = validateDisplayString(newValue, sizeConstraints);
    }
    return status;
  }

  /**
   * Validates a variable as a DisplayString OCTET STRING. If the variable
   * is not an OctetString instance, wrongType is returned as error status.
   * Otherwise wrongValue is returned if the string contains non-printable
   * characters other than 'return' and 'new-line'.
   *
   * @param displayString
   *    a variable to validate.
   * @param sizeConstraints
   *    a constraint for the size (length) of the string.
   * @return
   *    a SNMP error status if the variable is not a valid DisplayString or zero
   *    if it is.
   */
  public static int validateDisplayString(Variable displayString,
                                          ValueConstraint sizeConstraints) {
    if (displayString instanceof OctetString) {
      OctetString os = (OctetString)displayString;
      int status = sizeConstraints.validate(displayString);
      if (status != PDU.noError) {
        return status;
      }
      for (int i=0; i<os.length(); i++) {
        if (os.get(i) < 0) {
          return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
        }
        if (os.get(i) == '\r') {
          if (i+1 == os.length()) {
            return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
          }
          else if ((os.get(i+1) != 0) && (os.get(i+1) != '\n')) {
            return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
          }
        }
      }
      return SnmpConstants.SNMP_ERROR_SUCCESS;
    }
    else {
      return SnmpConstants.SNMP_ERROR_WRONG_TYPE;
    }
  }

  /**
   * The <code>DisplayStringValidation</code> can be used to validate the
   * contents of <code>OctetString</code> variables that follow the
   * DisplayString TC rules.
   *
   * @author Frank Fock
   * @version 1.0
   */
  public static class DisplayStringValidation extends ValueConstraintValidator
  {
    public DisplayStringValidation(Constraints valueConstraint) {
      super(valueConstraint);
    }

    public DisplayStringValidation(int minSize, int maxSize) {
      super(new ConstraintsImpl());
      ((ConstraintsImpl)
       getValueConstraint()).add(new Constraint(minSize, maxSize));
    }

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      int status =
          DisplayString.validateDisplayString(newValue, getValueConstraint());
      validationEvent.setValidationStatus(status);
    }
  }

}
