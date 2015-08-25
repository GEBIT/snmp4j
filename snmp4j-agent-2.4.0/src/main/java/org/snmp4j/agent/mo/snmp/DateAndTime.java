/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DateAndTime.java  
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
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimeZone;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.smi.OID;
// JavaDoc DateAndTimeTC
import org.snmp4j.agent.mo.snmp.tc.DateAndTimeTC;

/**
 * The <code>DateAndTime</code> implements the DateAndTime textual convention
 * (TC) from the SNMPv2-TC MIB specificion for columnar objects.
 * <p/>
 * <code>DateAndTime</code> subclasses <code>MOMutableColumn</code> and can thus
 * directly be used with tables. To use this TC implementation as
 * <code>MOScalar</code> create the corresponding instance using
 * {@link #createMOScalar} or even better use the {@link DateAndTimeTC} textual
 * convention in conjunction with a <code>MOFactory</code>.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class DateAndTime<T extends OctetString> extends MOMutableColumn<T> {

  public DateAndTime(int columnID, MOAccess access,
                     OctetString defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue, mutableInService);
  }

  public DateAndTime(int columnID, MOAccess access,
                     OctetString defaultValue) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue);
  }

  /**
   * Tests a variable for DateAndTime conformance.
   * @param dateAndTime
   *    a Variable.
   * @return
   *    {@link SnmpConstants#SNMP_ERROR_SUCCESS} if <code>dateAndTime</code>
   *    is valid or an appropriate SNMP error code if not.
   */
  public static int validateDateAndTime(Variable dateAndTime) {
    if (dateAndTime instanceof OctetString) {
      OctetString os = (OctetString)dateAndTime;
      if ((os.length() != 8) && (os.length() != 11)) {
        return SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
      }
      int month = (os.get(2) & 0xFF );
      int date = (os.get(3) & 0xFF );
      int hour = (os.get(4) & 0xFF );
      int minute = (os.get(5) & 0xFF );
      int second = (os.get(6) & 0xFF );
      int deci = (os.get(7) & 0xFF );
      if ((month < 1) || (month > 12) ||
          (date < 1) || (date > 31) || (hour > 23) || (second > 59) ||
          (minute > 59) || (deci > 9)) {
        return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
      }
      if (os.length() == 11) {
        if ((os.get(8) != '+') && (os.get(8) != '-')) {
          return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
        }
      }
      return SnmpConstants.SNMP_ERROR_SUCCESS;
    }
    return SnmpConstants.SNMP_ERROR_WRONG_TYPE;
  }

  /**
   * Creates a DatenAndTime <code>OctetString</code> value from a
   * <code>GregorianCalendar</code>.
   * @param dateAndTime
   *    a <code>GregorianCalendar</code> instance.
   * @return
   *    the corresponding DateAndTime <code>OctetString</code>.
   */
  public static OctetString makeDateAndTime(GregorianCalendar dateAndTime) {
    return makeDateAndTime(dateAndTime, new OctetString());
  }


  /**
   * Creates a DateAndTime <code>OctetString</code> value from a
   * <code>GregorianCalendar</code>.
   * @param dateAndTime
   *    a <code>GregorianCalendar</code> instance.
   * @param os
   *    the OctetString instance where to store the value.
   * @return
   *    the os instance with the set calendar value.
   * @since 2.0.5
   */
  public static <T extends OctetString> T makeDateAndTime(GregorianCalendar dateAndTime, T os) {
    os.append((byte)(dateAndTime.get(Calendar.YEAR)/256));
    os.append((byte)(dateAndTime.get(Calendar.YEAR)%256));
    os.append((byte)(dateAndTime.get(Calendar.MONTH)+1));
    os.append((byte)(dateAndTime.get(Calendar.DAY_OF_MONTH)));
    os.append((byte)(dateAndTime.get(Calendar.HOUR_OF_DAY)));
    os.append((byte)(dateAndTime.get(Calendar.MINUTE)));
    os.append((byte)(dateAndTime.get(Calendar.SECOND)));
    os.append((byte)(dateAndTime.get(Calendar.MILLISECOND)/100));
    if (dateAndTime.getTimeZone() != null) {
      TimeZone tz = dateAndTime.getTimeZone();
      os.append((tz.getRawOffset()>=0) ? "+":"-");
      os.append((byte)(Math.abs(tz.getOffset(dateAndTime.getTimeInMillis())/3600000)));
      os.append((byte)(Math.abs(tz.getOffset(dateAndTime.getTimeInMillis())%3600000)/60000));
    }
    return os;
  }

  /**
   * Creates a <code>GregorianCalendar</code> from a properly formatted
   * DateAndTime <code>OctetString</code>.
   * @param dateAndTimeValue
   *    an OctetString conforming to the DateAndTime TC.
   * @return
   *    the corresponding <code>GregorianCalendar</code> instance.
   */
  public static GregorianCalendar makeCalendar(OctetString dateAndTimeValue) {
    int year = (dateAndTimeValue.get(0) & 0xFF ) * 256 +
        (dateAndTimeValue.get(1) & 0xFF );
    int month = (dateAndTimeValue.get(2) & 0xFF );
    int date = (dateAndTimeValue.get(3) & 0xFF );
    int hour = (dateAndTimeValue.get(4) & 0xFF );
    int minute = (dateAndTimeValue.get(5) & 0xFF );
    int second = (dateAndTimeValue.get(6) & 0xFF );
    int deci = (dateAndTimeValue.get(7) & 0xFF );
    GregorianCalendar gc =
        new GregorianCalendar(year, month-1, date, hour, minute, second);
    gc.set(Calendar.MILLISECOND, deci*100);
    if (dateAndTimeValue.length() == 11) {
      String timezone = "GMT" + dateAndTimeValue.get(8) +
          dateAndTimeValue.get(9) + ":" + dateAndTimeValue.get(10);
      GregorianCalendar tgc =
          new GregorianCalendar(TimeZone.getTimeZone(timezone));
      tgc.setTimeInMillis(gc.getTimeInMillis());
      return tgc;
    }
    return gc;
  }

  public synchronized int validate(Variable newValue, Variable oldValue) {
    return validateDateAndTime(newValue);
  }

  /**
   * Create a <code>MOScalar</code> DateAndTime instance.
   * @param oid
   *    the OID of the scalar (including the .0 suffix).
   * @param access
   *    the <code>MOAccess</code> instance defining the maximum access rights.
   * @param value
   *    the initial value.
   * @param localtime
   *    if <code>true</code> the returned DateAndTime instance will always
   *    return the local time (does only makes sense for a read-only instance).
   *    Otherwise the value last set will be returned on GET like requests.
   * @return
   *    the <code>MOScalar</code> instance.
   */
  public static MOScalar<OctetString> createMOScalar(final OID oid,
                                                     final MOAccess access,
                                                     final OctetString value,
                                                     final boolean localtime) {
    return new MOScalar<OctetString>(oid, access, value) {
      public int isValueOK(SubRequest sreq) {
        return validateDateAndTime(sreq.getVariableBinding().getVariable());
      }

      public OctetString getValue() {
        OctetString value = super.getValue();
        if (localtime) {
          value = makeDateAndTime(new GregorianCalendar());
        }
        return value;
      }
    };
  }
}
