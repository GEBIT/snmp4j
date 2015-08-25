/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - RowPointer.java  
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
import org.snmp4j.agent.request.*;
import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;

public class RowPointer extends MOMutableColumn<OID> {

  private MOTable targetTable;

  public RowPointer(int columnID, MOAccess access,
                    OID defaultValue, boolean mutableInService,
                    MOTable targetTable) {
    super(columnID, SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
          access, defaultValue, mutableInService);
    this.targetTable = targetTable;
  }

  public RowPointer(int columnID, MOAccess access,
                    OID defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
          access, defaultValue, mutableInService);
  }

  protected void illegalRowPointer(SubRequest subRequest) {
    subRequest.getStatus().
        setErrorStatus(SnmpConstants.SNMP_ERROR_INCONSISTENT_NAME);
  }

  public void prepare(SubRequest subRequest, MOTableRow row,
                      MOTableRow preparedChanges, int column) {
    super.prepare(subRequest, row, null, column);
    if (!subRequest.hasError()) {
      OID rowPointer = (OID) subRequest.getVariableBinding().getVariable();
      MOTableCellInfo cell = targetTable.getCellInfo(rowPointer);
      if ((cell == null) ||
          (cell.getIndex() == null) || (cell.getColumn() < 0)) {
        illegalRowPointer(subRequest);
      }
    }
  }

  public MOTable getTargetTable() {
    return targetTable;
  }

  public void setTargetTable(MOTable targetTable) {
    this.targetTable = targetTable;
  }

}
