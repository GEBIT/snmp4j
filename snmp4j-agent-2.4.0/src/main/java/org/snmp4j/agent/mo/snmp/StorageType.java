/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - StorageType.java  
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
import org.snmp4j.smi.Integer32;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.mo.MOTableRow;

public class StorageType extends MOMutableColumn {

  public static final int other = 1;
  public static final int volatile_ = 2;
  public static final int nonVolatile = 3;
  public static final int permanent = 4;
  public static final int readOnly = 5;


  public StorageType(int columnID, MOAccess access,
                     Integer32 defaultValue, boolean mutableInService) {
    super(columnID, SMIConstants.SYNTAX_INTEGER,
          access, defaultValue, mutableInService);
  }

  public StorageType(int columnID, MOAccess access,
                     Integer32 defaultValue) {
    super(columnID, SMIConstants.SYNTAX_INTEGER,
          access, defaultValue);
  }


  public synchronized int validate(Variable newValue, Variable oldValue) {
    int v = (((Integer32)newValue).getValue());
    if ((v < 1) || (v > 5)) {
      return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
    }
    if (oldValue != null) {
      int ov = (((Integer32)oldValue).getValue());
      if ((ov < 4) && (v >= 4)) {
        return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
      }
      if (ov >= 4) {
        return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
      }
    }
    return super.validate(newValue, oldValue);
  }

  public boolean isVolatile(MOTableRow row, int column) {
    Integer32 value = (Integer32) row.getValue(column);
    if (value != null) {
      int storageType = value.getValue();
      switch (storageType) {
        case other:
        case volatile_:
        case readOnly: {
          return true;
        }
        default:
          return false;
      }
    }
    return false;
  }

}
