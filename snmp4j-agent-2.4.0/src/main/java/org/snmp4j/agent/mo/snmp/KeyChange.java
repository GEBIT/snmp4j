/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - KeyChange.java  
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
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.mp.SnmpConstants;

public class KeyChange extends MOMutableColumn {

  public KeyChange(int columnID, MOAccess access,
                   OctetString defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OCTET_STRING, access,
          defaultValue, mutableInService);
  }

  public static OctetString changeKey(AuthenticationProtocol authProtocol,
                                      OctetString oldKey,
                                      OctetString keyChange,
                                      int keyLength) {
    byte[] random = new byte[keyLength];
    byte[] delta = new byte[keyChange.length()-keyLength];
    System.arraycopy(keyChange.getValue(), 0, random, 0, keyLength);
    System.arraycopy(keyChange.getValue(), keyLength, delta, 0,
                     keyChange.length()-keyLength);
    byte[] newKey = authProtocol.changeDelta(oldKey.getValue(), delta, random);
    return new OctetString(newKey, random.length, newKey.length - random.length);
  }

  public void get(SubRequest subRequest, MOTableRow row, int column) {
    if (getAccess().isAccessibleForRead()) {
      subRequest.getVariableBinding().setVariable(new OctetString());
    }
    else {
      subRequest.getStatus().setErrorStatus(SnmpConstants.SNMP_ERROR_NO_ACCESS);
    }
    subRequest.completed();
  }

}
