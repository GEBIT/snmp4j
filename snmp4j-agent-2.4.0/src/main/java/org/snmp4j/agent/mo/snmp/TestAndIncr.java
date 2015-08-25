/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TestAndIncr.java  
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

import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.request.RequestStatus;

public class TestAndIncr extends MOScalar<Integer32> {

  public TestAndIncr(OID oid) {
    super(oid, MOAccessImpl.ACCESS_READ_WRITE, new Integer32(0));
  }

  public void commit(SubRequest request) {
    Integer32 requestValue =
        (Integer32)request.getVariableBinding().getVariable();
    int v = requestValue.getValue();
    if (v == Integer.MAX_VALUE) {
      v = 0;
    }
    else {
      v++;
    }
    request.getVariableBinding().setVariable(new Integer32(v));
    super.commit(request);
    request.getVariableBinding().setVariable(requestValue);
  }

  public void prepare(SubRequest request) {
    super.prepare(request);
    if (!request.hasError()) {
      Integer32 value = (Integer32) request.getVariableBinding().getVariable();
      if (!getValue().equals(value)) {
        request.getStatus().
            setErrorStatus(SnmpConstants.SNMP_ERROR_INCONSISTENT_VALUE);
      }
    }
  }

  public int isValueOK(SubRequest request) {
    Integer32 value = (Integer32) request.getVariableBinding().getVariable();
    if (value.getValue() < 0) {
      return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
    }
    return SnmpConstants.SNMP_ERROR_SUCCESS;
  }

}
