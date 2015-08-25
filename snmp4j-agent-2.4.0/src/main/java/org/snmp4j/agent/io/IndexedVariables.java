/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - IndexedVariables.java  
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

import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import java.io.Serializable;
import java.util.Arrays;

public class IndexedVariables implements Serializable {

  static final long serialVersionUID = -6299480147865204027L;

  private OID index;
  private Variable[] values;

  public IndexedVariables(OID index, Variable[] values) {
    this.index = index;
    this.values = values;
  }

  public Variable[] getValues() {
    return values;
  }

  public OID getIndex() {
    return index;
  }

  public String toString() {
    return "IndexVariables[index="+index+",values="+Arrays.asList(values)+"]";
  }
}
