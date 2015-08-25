/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpTagList.java  
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
import org.snmp4j.mp.SnmpConstants;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public class SnmpTagList extends MOMutableColumn {

  private static final OctetString TAG_DELIMITER = new OctetString(" \t\r\n");

  public SnmpTagList(int columnID, MOAccess access,
                     Variable defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING,
          access, defaultValue, mutableInService);
  }

  public synchronized int validate(Variable newValue, Variable oldValue) {
    int status = super.validate(newValue, oldValue);
    if (status == SnmpConstants.SNMP_ERROR_SUCCESS) {
      return isValidTagList(newValue);
    }
    return status;
  }

  public static Set<OctetString> getTags(OctetString tagList) {
    Collection<OctetString> tags = OctetString.split(tagList, TAG_DELIMITER);
    HashSet<OctetString> list = new HashSet<OctetString>(tags);
    return list;
  }

  public static int isValidTagList(Variable newValue) {
    if (!(newValue instanceof OctetString)) {
      return SnmpConstants.SNMP_ERROR_WRONG_TYPE;
    }
    OctetString os = (OctetString)newValue;
    if (os.length() > 255) {
      return SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
    }
    else if (os.length() > 0) {
      if (SnmpTagValue.isDelimiter(os.get(0)) ||
          SnmpTagValue.isDelimiter(os.get(os.length()-1))) {
        return SnmpConstants.SNMP_ERROR_BAD_VALUE;
      }
      boolean lastWasDelimiter = false;
      for (int i = 0; i < os.length()-1; i++) {
        boolean isDelimiter = SnmpTagValue.isDelimiter(os.get(i));
        if (lastWasDelimiter && isDelimiter) {
          return SnmpConstants.SNMP_ERROR_BAD_VALUE;
        }
        lastWasDelimiter = isDelimiter;
      }
    }
    return SnmpConstants.SNMP_ERROR_SUCCESS;
  }
}
