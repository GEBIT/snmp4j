/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - EnumeratedScalar.java  
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

import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.Integer32;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.OID;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.mo.snmp.smi.EnumerationConstraint;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * The <code>EnumeratedScalar</code> class represents enumerated SMI INTEGER
 * (={@link Integer32}) or an OCTET STRING with enumerated named bits for
 * scalar objects. The latter represents the SMI construct BITS.
 *
 * @author Frank Fock
 * @version 2.0.5
 */
public class EnumeratedScalar<V extends Variable> extends MOScalar<V> {

  private EnumerationConstraint constraint;

  /**
   * Creates an enumerated Integer32 or BITS (OctetString) scalar with
   * specifying a set of possible values. To constraint the possible values
   * assignable to this object, you will have to set the corresponding
   * {@link EnumerationConstraint} with {@link #setConstraint} or use an
   * appropriate value validation listener.
   * @param oid
   *    the instance oid (with ".0" suffix) of the scalar.
   * @param access
   *    the maximum access for this column.
   * @param value
   *    the initial value.
   */
  public EnumeratedScalar(OID oid,
                          MOAccess access,
                          V value) {
    super(oid, access, value);
  }

  /**
   * Creates an enumerated scalar with specifying a set of possible
   * values.
   * @param oid
   *    the instance oid (with ".0" suffix) of the scalar.
   * @param access
   *    the maximum access for this column.
   * @param value
   *    the initial value.
   * @param allowedValues
   *    an array of possible values for this object.
   */
  public EnumeratedScalar(OID oid,
                          MOAccess access,
                          V value,
                          int[] allowedValues) {
    super(oid, access, value);
    this.constraint = new EnumerationConstraint(allowedValues);
  }

  public int isValueOK(SubRequest request) {
    int result = super.isValueOK(request);
    if ((constraint != null) && (result == SnmpConstants.SNMP_ERROR_SUCCESS)) {
      return constraint.validate(request.getVariableBinding().getVariable());
    }
    return result;
  }

  protected void setConstraint(EnumerationConstraint constraint) {
    this.constraint = constraint;
  }
}
