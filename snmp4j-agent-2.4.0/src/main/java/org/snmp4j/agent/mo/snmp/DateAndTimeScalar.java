/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DateAndTimeScalar.java  
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
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.Variable;
import org.snmp4j.agent.request.SubRequest;
import java.util.GregorianCalendar;
// JavaDoc
import org.snmp4j.agent.mo.snmp.tc.DateAndTimeTC;

/**
 * The <code>DateAndTimeScalar</code> implements the DateAndTime textual
 * convention (TC) from the SNMPv2-TC MIB specificion for scalar objects.
 * <p/>
 * <code>DateAndTimeScalar</code> subclasses <code>MOScalar</code> and
 * can thus directly be used.
 * <p>
 * It is recommended to use this TC implementation not directly, instead use
 * the {@link DateAndTimeTC} textual convention in conjunction with a
 * <code>MOFactory</code>.
 *
 * @author Frank Fock
 * @version 2.0.5
 */
public class DateAndTimeScalar<T extends OctetString> extends MOScalar<T> {

  private boolean localtime;

  public DateAndTimeScalar(final OID oid,
                           final MOAccess access,
                           final T value) {
    this(oid, access, value, false);
  }


  public DateAndTimeScalar(final OID oid,
                           final MOAccess access,
                           final T value,
                           final boolean localtime) {
    super(oid, access, value);
    this.localtime = localtime;
  }

  public int isValueOK(SubRequest sreq) {
    return DateAndTime.validateDateAndTime(sreq.getVariableBinding().getVariable());
  }

  public T getValue() {
    T value = super.getValue();
    if (localtime) {
      DateAndTime.makeDateAndTime(new GregorianCalendar(), value);
    }
    return value;
  }

  /**
   * Sets the date and time value (incl. time zone) from a gregorian calendar
   * value.
   * @param calendar
   *    a <code>GregorianCalendar</code> instance.
   */
  public void setCalendar(GregorianCalendar calendar) {
    setValue(DateAndTime.makeDateAndTime(calendar, getValue()));
  }

  /**
   * Gets a gregorian calendar instance with the date and time of this scalar.
   * @return
   *    a <code>GregorianCalendar</code> instance.
   */
  public GregorianCalendar getCalendar() {
    return DateAndTime.makeCalendar(getValue());
  }

}
