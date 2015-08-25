/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TruthValueTC.java  
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


package org.snmp4j.agent.mo.snmp.tc;

import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.mo.snmp.smi.EnumerationConstraint;
import org.snmp4j.agent.mo.snmp.smi.ValueConstraintValidator;
import org.snmp4j.mp.SnmpConstants;

public class TruthValueTC implements TextualConvention<Integer32> {

  public static final int TRUE = 1;
  public static final int FALSE = 2;

  private EnumerationConstraint constraint =
      new EnumerationConstraint(new int[] { TRUE, FALSE });

  public TruthValueTC() {
  }

  public MOColumn<Integer32> createColumn(int columnID, int syntax, MOAccess access, Integer32 defaultValue,
                                          boolean mutableInService) {
    MOColumn<Integer32> c;
    if (access.isAccessibleForWrite()) {
      c = new MOMutableColumn<Integer32>(columnID, SMIConstants.SYNTAX_INTEGER32,
                              access, defaultValue);
      ((MOMutableColumn)c).addMOValueValidationListener(
          new ValueConstraintValidator(constraint));
    }
    else {
      c = new MOColumn<Integer32>(columnID, SMIConstants.SYNTAX_INTEGER32, access);
    }
    return c;
  }

  public MOScalar<Integer32> createScalar(OID oid, MOAccess access, Integer32 value) {
    MOScalar<Integer32> scalar =
        new MOScalar<Integer32>(oid, access, (value == null) ? createInitialValue() : value);
    if (constraint.validate(scalar.getValue()) !=
        SnmpConstants.SNMP_ERROR_SUCCESS) {
      throw new IllegalArgumentException("Illegal TruthValue "+value);
    }
    scalar.
        addMOValueValidationListener(new ValueConstraintValidator(constraint));
    return scalar;
  }

  public String getModuleName() {
    return "SNMPv2-TC";
  }

  public String getName() {
    return "TruthValue";
  }

  public static Integer32 getValue(boolean b) {
    return (b) ? new Integer32(TRUE) : new Integer32(FALSE);
  }

  public Integer32 createInitialValue() {
    return new Integer32(FALSE);
  }

  /**
   * Gets the boolean value of a TruthValue variable.
   * @param truthValue
   *    a TruthValue.
   * @return
   *    <code>true</code> if <code>truthValue</code> equals
   *    {@link #TRUE}.
   * @since 2.0
   */
  public static boolean getBooleanValue(Integer32 truthValue) {
     return (TRUE == truthValue.getValue());
  }
}
