/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - EnumerationConstraint.java  
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

import org.snmp4j.smi.*;
import java.util.Arrays;
import org.snmp4j.PDU;

/**
 * The <code>EnumerationConstraint</code> class checks an <code>Integer32</code>
 * value to match a set of (enumerated) values or a <code>OctetString</code>
 * value to match a set of bit values for the BITS SMI type.
 *
 * @author Frank Fock
 * @version 1.2
 */
public class EnumerationConstraint implements ValueConstraint {

  private int[] allowedValues;

  /**
   * Creates an <code>EnumerationConstraint</code> based on the specified array
   * of integer values.
   * @param allowedValues
   *    an array of allowed values.
   */
  public EnumerationConstraint(int[] allowedValues) {
    this.allowedValues = new int[allowedValues.length];
    System.arraycopy(allowedValues, 0,
                     this.allowedValues, 0, allowedValues.length);
    Arrays.sort(this.allowedValues);
  }

  public int validate(Variable variable) {
    if (variable instanceof Integer32) {
      if (Arrays.binarySearch(allowedValues,
                              ((Integer32)variable).getValue()) < 0) {
        return PDU.wrongValue;
      }
      return PDU.noError;
    }
    else if (variable instanceof OctetString) {
      OctetString s = (OctetString)variable;
      int maxLength = 0;
      if (allowedValues.length > 0) {
        maxLength = allowedValues[allowedValues.length-1] / 8 + 1;
      }
      if (s.length() > maxLength) {
        return PDU.wrongLength;
      }
      for (int i=0; i<s.length(); i++) {
        byte b = s.get(i);
        for (int j=0; j<8; j++) {
          if ((b & (1 << (7-j))) > 0) {
            if (Arrays.binarySearch(allowedValues, i*8+j) < 0) {
              return PDU.wrongValue;
            }
          }
        }
      }
      return PDU.noError;
    }
    return PDU.wrongType;
  }
}
