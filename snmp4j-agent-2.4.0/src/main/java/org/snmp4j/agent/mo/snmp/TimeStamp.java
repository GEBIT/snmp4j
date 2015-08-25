/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TimeStamp.java  
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

import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.agent.mo.MOMutableTableRow;

public class TimeStamp extends MOColumn {

  private SysUpTime sysUpTime;

  public TimeStamp(int columnID, MOAccess access) {
    this(columnID, access, null);
  }

  public TimeStamp(int columnID, MOAccess access, SysUpTime sysUpTime) {
    super(columnID, SMIConstants.SYNTAX_TIMETICKS, access);
    this.sysUpTime = sysUpTime;
  }

  public void update(MOMutableTableRow row, int columnIndex) {
    if (sysUpTime != null) {
      row.setValue(columnIndex, sysUpTime.get());
    }
    else {
      throw new UnsupportedOperationException("TimeStamp's sysUpTime is null");
    }
  }

  public void setSysUpTime(SysUpTime sysUpTime) {
    this.sysUpTime = sysUpTime;
  }

  public SysUpTime getSysUpTime() {
    return sysUpTime;
  }
}
