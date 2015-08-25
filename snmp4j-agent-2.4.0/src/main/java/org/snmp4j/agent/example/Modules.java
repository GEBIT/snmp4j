/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Modules.java  
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

package org.snmp4j.agent.example;
//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.agent.mo.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.smi.OctetString;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class Modules implements MOGroup {

  private static final LogAdapter LOGGER =
      LogFactory.getLogger(Modules.class);

  private Snmp4jDemoMib snmp4jDemoMib;

  private MOFactory factory;

//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  public Modules() {
   snmp4jDemoMib = new Snmp4jDemoMib();
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  public Modules(MOFactory factory) {
   snmp4jDemoMib = new Snmp4jDemoMib(factory);
//--AgentGen BEGIN=_CONSTRUCTOR
//--AgentGen END
  }

  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
	  snmp4jDemoMib.registerMOs(server, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
	  snmp4jDemoMib.unregisterMOs(server, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  public Snmp4jDemoMib getSnmp4jDemoMib() {
    return snmp4jDemoMib;
  }


//--AgentGen BEGIN=_METHODS
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END

}
