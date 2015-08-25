/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOInfo.java  
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


package org.snmp4j.agent.io;

import java.io.*;

import org.snmp4j.smi.*;

public class MOInfo implements Serializable {

  static final long serialVersionUID = 590760177476248255L;

  private OID oid;
  private String version;

  public MOInfo(OID oid) {
    this.oid = oid;
  }

  public MOInfo(OID oid, String version) {
    this.oid = oid;
    this.version = version;
  }

  public OID getOID() {
    return oid;
  }

  public String getVersion() {
    return version;
  }

  public boolean equals(Object obj) {
    if (obj instanceof MOInfo) {
      MOInfo o = (MOInfo)obj;
      return o.oid.equals(oid) && ((version == null) || (o.version == null) ||
                                   version.equals(o.version));
    }
    return false;
  }

  public int hashCode() {
    return oid.hashCode();
  }

  public String toString() {
    return "MOInfo[oid="+oid+",version="+version+"]";
  }
}
