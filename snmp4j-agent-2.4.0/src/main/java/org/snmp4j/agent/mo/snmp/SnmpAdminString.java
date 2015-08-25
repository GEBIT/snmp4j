/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpAdminString.java  
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

import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;

public class SnmpAdminString extends MOMutableColumn {

  private int minLength = 0;
  private int maxLength = 255;

  public SnmpAdminString(int columnID,
                         MOAccess access,
                         Variable defaultValue,
                         boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue, mutableInService);
  }

  public SnmpAdminString(int columnID,
                         MOAccess access,
                         Variable defaultValue,
                         boolean mutableInService,
                         int minLength,
                         int maxLength) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue, mutableInService);
    setMinLength(minLength);
    setMaxLength(maxLength);
  }

  public int getMaxLength() {
    return maxLength;
  }

  public int getMinLength() {
    return minLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public void setMinLength(int minLength) {
    this.minLength = minLength;
  }

  public synchronized int validate(Variable newValue, Variable oldValue) {
    OctetString os = (OctetString) newValue;
    if ((os.length() < minLength) || (os.length() > maxLength)) {
      return SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
    }
    return super.validate(newValue, oldValue);
  }

}
