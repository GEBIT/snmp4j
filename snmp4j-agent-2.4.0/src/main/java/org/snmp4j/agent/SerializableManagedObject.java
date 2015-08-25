/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SerializableManagedObject.java  
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

package org.snmp4j.agent;

import org.snmp4j.agent.io.MOInput;
import org.snmp4j.agent.io.MOOutput;
import java.io.IOException;
import org.snmp4j.smi.OID;

/**
 * The <code>SerializableManagedObject</code> interface is implemented by
 * <code>ManagedObject</code>s whose (data) content can be serialized
 * using {@link MOInput} and {@link MOOutput}.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface SerializableManagedObject extends RegisteredManagedObject {

  /**
   * Loads the content of the managed object from the specified input (stream).
   * @param input
   *    a <code>MOInput</code> instance.
   * @throws IOException
   *    if an MOInput operation fails.
   */
  void load(MOInput input) throws IOException;

  /**
   * Saves the (non-volatile) content of this managed object to the specified
   * output (stream).
   * @param output
   *    a <code>MOOutput</code> instance.
   * @throws IOException
   *    if an MOOutput operation fails.
   */
  void save(MOOutput output) throws IOException;

  /**
   * Tests if this instance of a SerializableManagedObject should be
   * serialized or deserialized through persistent storage
   * load or save operation.
   * @return
   *    <code>true</code> if {@link #load} and {@link #save} should not be
   *    called through a persistent storage operation and <code>false</code>
   *    if these method should be called.
   */
  boolean isVolatile();

}
