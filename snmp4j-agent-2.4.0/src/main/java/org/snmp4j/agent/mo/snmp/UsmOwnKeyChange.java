/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - UsmOwnKeyChange.java  
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
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.PDU;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.security.SecurityModel;

public class UsmOwnKeyChange extends UsmKeyChange {

  public UsmOwnKeyChange(int columnID, MOAccess access, int type) {
    super(columnID, access, type);
  }

  public void prepare(SubRequest subRequest, MOTableRow row,
                      MOTableRow changeSet, int column) {
    Variable[] indexValues =
        ((UsmMIB.UsmTableRow)row).getIndexDef().getIndexValues(row.getIndex());
    OctetString userName = (OctetString) indexValues[1];
    if ((!subRequest.getRequest().getSecurityName().equals(userName)) ||
        subRequest.getRequest().getSecurityModel() !=
        SecurityModel.SECURITY_MODEL_USM) {
      subRequest.getStatus().setErrorStatus(PDU.noAccess);
    }
    else {
      super.prepare(subRequest, row, changeSet, column);
    }
  }


}
