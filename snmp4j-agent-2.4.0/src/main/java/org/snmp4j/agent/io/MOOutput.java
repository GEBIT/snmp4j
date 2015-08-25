/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOOutput.java  
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

import org.snmp4j.smi.Variable;
import java.io.IOException;

public interface MOOutput {

  void writeContextBegin(Context context) throws IOException;
  void writeContextEnd(Context context) throws IOException;

  void writeManagedObjectBegin(MOInfo mo) throws IOException;
  void writeManagedObjectEnd(MOInfo mo) throws IOException;

  void writeSequence(Sequence sequence) throws IOException;

  void writeVariable(Variable variable) throws IOException;

  void writeIndexedVariables(IndexedVariables indexedVariables)
      throws IOException;

  void close() throws IOException;
}
