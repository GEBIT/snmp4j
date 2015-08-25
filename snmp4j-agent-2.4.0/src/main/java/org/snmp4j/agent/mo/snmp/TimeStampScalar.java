/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TimeStampScalar.java  
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

public class TimeStampScalar extends MOScalar<TimeTicks> {

  private SysUpTime sysUpTime;

  public TimeStampScalar(OID oid, MOAccess access, TimeTicks value,
                         SysUpTime sysUpTime) {
    super(oid, access, value);
    this.sysUpTime = sysUpTime;
  }

  public TimeStampScalar(OID oid, MOAccess access, SysUpTime sysUpTime) {
    this(oid, access, new TimeTicks(0), sysUpTime);
  }

  public void update() {
    setValue(sysUpTime.get());
  }

  public void setSysUpTime(SysUpTime sysUpTime) {
    if (sysUpTime == null) {
      throw new NullPointerException();
    }
    this.sysUpTime = sysUpTime;
  }

  public SysUpTime getSysUpTime() {
    return sysUpTime;
  }
}
