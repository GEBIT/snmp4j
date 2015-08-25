/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - UsmKeyChange.java  
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
import org.snmp4j.smi.OctetString;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.security.AuthenticationProtocol;
import org.snmp4j.PDU;
import org.snmp4j.security.PrivacyProtocol;

public class UsmKeyChange extends KeyChange {

  public static final int AUTH_KEY_CHANGE = 0;
  public static final int PRIV_KEY_CHANGE = 1;

  private int type;

  public UsmKeyChange(int columnID,
                      MOAccess access, int type) {
    super(columnID, access, new OctetString(), true);
    this.type = type;
  }

  public void prepare(SubRequest subRequest, MOTableRow row,
                      MOTableRow changeSet, int column) {
    super.prepare(subRequest, row, changeSet, column);
    if (!subRequest.hasError()) {
      UsmMIB.UsmTableRow usmRow = (UsmMIB.UsmTableRow)row;
      int digestLength = getDigestLength(usmRow, changeSet);
      if (digestLength <= 0) {
        subRequest.completed();
        return;
      }
      OctetString v =
          (OctetString) subRequest.getVariableBinding().getVariable();
      if (v.length() != digestLength) {
        subRequest.getStatus().setErrorStatus(PDU.wrongValue);
      }
    }
  }

  protected int getDigestLength(UsmMIB.UsmTableRow row, MOTableRow changeSet) {
    switch (type) {
      case AUTH_KEY_CHANGE: {
        AuthenticationProtocol a = row.getAuthProtocol(changeSet);
        if (a != null) {
          return a.getDigestLength() * 2;
        }
        break;
      }
      case PRIV_KEY_CHANGE: {
        PrivacyProtocol p = row.getPrivProtocol(changeSet);
        if (p != null) {
          return p.getMaxKeyLength() * 2;
        }
        break;
      }
    }
    return -1;
  }

  public void commit(SubRequest subRequest, MOTableRow row,
                     MOTableRow changeSet, int column) {
    int digestLength = getDigestLength((UsmMIB.UsmTableRow)row, changeSet);
    if (digestLength > 0) {
      super.commit(subRequest, row, changeSet, column);
    }
    else {
      subRequest.completed();
    }
  }

}
