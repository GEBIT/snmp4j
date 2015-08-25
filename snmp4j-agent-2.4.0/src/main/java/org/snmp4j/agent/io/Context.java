/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Context.java  
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
import org.snmp4j.smi.OctetString;

public class Context implements Serializable {

  static final long serialVersionUID = -6616464416265180630L;

  private OctetString context;

  public Context(OctetString context) {
    this.context = context;
  }

  public OctetString getContext() {
    return context;
  }

  public boolean equals(Object obj) {
    if (obj instanceof Context) {
      return context.equals(((Context)obj).context);
    }
    return false;
  }

  public int hashCode() {
    return context.hashCode();
  }

}
