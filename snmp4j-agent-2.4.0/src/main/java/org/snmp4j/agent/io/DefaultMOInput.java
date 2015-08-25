/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOInput.java  
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

import org.snmp4j.log.*;
import org.snmp4j.smi.*;

public class DefaultMOInput implements MOInput {

  private static final LogAdapter logger =
      LogFactory.getLogger(DefaultMOInput.class);

  private int importMode;
  private ObjectInputStream ois;

  public DefaultMOInput(ObjectInputStream ois) {
    this.ois = ois;
  }

  public int getImportMode() {
    return importMode;
  }

  public Context readContext() throws IOException {
    try {
      return (Context) ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to load Context: " + ex.getMessage());
      return null;
    }
  }

  public IndexedVariables readIndexedVariables() throws IOException {
    try {
      return (IndexedVariables) ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to load IndexedVariables: " + ex.getMessage());
      return null;
    }
  }

  public MOInfo readManagedObject() throws IOException {
    try {
      return (MOInfo) ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to load MOInfo: " + ex.getMessage());
      return null;
    }
  }

  public Sequence readSequence() throws IOException {
    try {
      return (Sequence) ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to load Sequence: " + ex.getMessage());
      return null;
    }
  }

  public Variable readVariable() throws IOException {
    try {
      return (Variable) ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to load Variable: " + ex.getMessage());
      return null;
    }
  }

  public void skipContext(Context context) throws IOException {
    Object next = null;
    try {
      next = ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to skip Context: " + ex.getMessage());
      next = null;
    }
    while ((!(next instanceof Context)) || (!((Context)next).equals(context))) {
      try {
        next = ois.readObject();
      }
      catch (ClassNotFoundException ex1) {
        logger.error("Failed to skip Context: " + ex1.getMessage());
      }
    }
  }

  public void skipManagedObject(MOInfo moInfo) throws IOException {
    Object next = null;
    try {
      next = ois.readObject();
    }
    catch (ClassNotFoundException ex) {
      logger.error("Failed to skip Context: " + ex.getMessage());
      next = null;
    }
    while ((!(next instanceof MOInfo)) || (!((MOInfo)next).equals(moInfo))) {
      try {
        next = ois.readObject();
      }
      catch (ClassNotFoundException ex1) {
        logger.error("Failed to skip Context: " + ex1.getMessage());
      }
    }
  }

  public void setOverwriteMode(int importMode) {
    this.importMode = importMode;
  }

  public void close() throws IOException {
    ois.close();
  }

}
