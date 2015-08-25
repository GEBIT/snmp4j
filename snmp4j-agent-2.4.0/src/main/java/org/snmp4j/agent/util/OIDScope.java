/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - OIDScope.java  
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


package org.snmp4j.agent.util;

import org.snmp4j.agent.*;
import org.snmp4j.smi.*;

public class OIDScope implements MOScope {

  private OID oid;

  public OIDScope(OID oid) {
    this.oid = oid;
  }

  public OID getLowerBound() {
    return oid;
  }

  public OID getUpperBound() {
    return oid;
  }

  public boolean isCovered(MOScope other) {
    return DefaultMOScope.covers(this, other);
  }

  public boolean isLowerIncluded() {
    return true;
  }

  public boolean isUpperIncluded() {
    return true;
  }

  public boolean isOverlapping(MOScope other) {
    return DefaultMOScope.overlaps(this, other);
  }

  public boolean covers(OID oid) {
    return this.oid.equals(oid);
  }

}
