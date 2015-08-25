/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOInput.java  
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

/**
 * The <code>MOInput</code> models the interface for reading ManagedObject
 * data into a SNMP4J-Agent in a generic way.
 *
 * @author Frank Fock
 * @version 1.2
 */
public interface MOInput {

  /**
   * Returns the update mode, which might be one of the constants defined
   * by {@link ImportModes}.
   * @return
   *    the constant denoting the update mode that should be used by a
   *    <code>SerializableManagedObject</code> to import its content from
   *    persistent storage.
   */
  int getImportMode();

  Context readContext() throws IOException;
  void skipContext(Context context) throws IOException;

  MOInfo readManagedObject() throws IOException;

  /**
   * Skips to the end of the specified managed object's configuration.
   * @param mo
   *    a MOInfo instance.
   * @throws
   *    IOException
   */
  void skipManagedObject(MOInfo mo) throws IOException;

  Variable readVariable() throws IOException;

  Sequence readSequence() throws IOException;

  IndexedVariables readIndexedVariables() throws IOException;

  void close() throws IOException;

}
