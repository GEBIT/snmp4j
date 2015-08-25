/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SimMOFactory.java  
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

package org.snmp4j.agent.mo.ext;

import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.MOAccess;
import java.util.Map;
import java.util.HashMap;

/**
 * A <code>SimMOFactory</code> object can be used to create non-default MOAccess
 * instances which support an agent simulation mode which allows the
 * modification of MIB objects at runtime via SNMP that are not writable in
 * normal operation but writable in a special config mode (see
 * AGENTPP-SIMULATION-MIB).
 * <p>
 * By creating the ManagedObject instances for a MIB module based on this
 * factory the injected special MOAccess instances support changing the
 * operation mode for these instances from normal operation to simulation
 * mode. In simulation mode, an agent can be filled with data via SNMP
 * regardless whether the objects are defined as writable or not. Such a
 * simulation agent can be used for testing/developing management applications
 * when real agents are not (physically) available.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class SimMOFactory extends DefaultMOFactory {

  private Map<Number, MOAccess> accessModes = new HashMap<Number, MOAccess>();

  private static boolean simulationModeEnabled;
  private static SimMOFactory instance;

  protected SimMOFactory() {
  }

  public synchronized static MOFactory getInstance() {
    if (instance == null) {
      instance = new SimMOFactory();
    }
    return instance;
  }

  public static void setSimulationModeEnabled(boolean simulationMode) {
    simulationModeEnabled = simulationMode;
  }

  public static boolean isSimulationModeEnabled() {
    return simulationModeEnabled;
  }

  public synchronized MOAccess createAccess(int moAccess) {
    MOAccess accessObj = accessModes.get(new Integer(moAccess));
    if (accessObj == null) {
      accessObj = new SimMOAccess(moAccess);
      accessModes.put(new Integer(moAccess), accessObj);
    }
    return accessObj;
  }

  public class SimMOAccess extends MOAccessImpl {

    public SimMOAccess(int moAccess) {
      super(moAccess);
    }

    public boolean isAccessibleForCreate() {
      if (isSimulationModeEnabled()) {
        return true;
      }
      return super.isAccessibleForCreate();
    }

    public boolean isAccessibleForWrite() {
      if (isSimulationModeEnabled()) {
        return true;
      }
      return super.isAccessibleForWrite();
    }
  }
}
