/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpTagValue.java  
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

import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.OctetString;
import org.snmp4j.mp.SnmpConstants;

public class SnmpTagValue extends MOMutableColumn {

  public SnmpTagValue(int columnID, MOAccess access, OctetString defaultValue) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING, access, defaultValue);
  }

  public SnmpTagValue(int columnID, MOAccess access,
                      OctetString defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING, access, defaultValue,
          mutableInService);
  }

  public static boolean isDelimiter(byte b) {
    return ((b == 32) || (b == 9) || (b == 13) || (b == 11));
  }

  public synchronized int validate(Variable newValue, Variable oldValue) {
    int status = super.validate(newValue, oldValue);
    if (status == SnmpConstants.SNMP_ERROR_SUCCESS) {
      status = isValidTagValue(newValue);
    }
    return status;
  }

  public static int isValidTagValue(Variable newValue) {
    if (!(newValue instanceof OctetString)) {
      return SnmpConstants.SNMP_ERROR_WRONG_TYPE;
    }
    int status = SnmpConstants.SNMP_ERROR_SUCCESS;
    OctetString os = (OctetString)newValue;
    if (os.length() > 255) {
      status = SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
    }
    else {
      for (int i = 0; i < os.length(); i++) {
        if (isDelimiter(os.get(i))) {
          status = SnmpConstants.SNMP_ERROR_BAD_VALUE;
          break;
        }
      }
    }
    return status;
  }

}
