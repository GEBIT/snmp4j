/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - AgentppSimulationMib.java  
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


//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class AgentppSimulationMib
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  private static final LogAdapter LOGGER =
      LogFactory.getLogger(AgentppSimulationMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private static MOFactory moFactory = DefaultMOFactory.getInstance();

  // Constants
  public static final OID oidAgentppSimMode =
    new OID(new int[] { 1,3,6,1,4,1,4976,2,1,1,0 });
  public static final OID oidAgentppSimDeleteRow =
    new OID(new int[] { 1,3,6,1,4,1,4976,2,1,2,0 });
  public static final OID oidAgentppSimDeleteTableContents =
    new OID(new int[] { 1,3,6,1,4,1,4976,2,1,3,0 });

  // Enumerations
  public static final class AgentppSimModeEnum {
    public static final int oper = 1;
    public static final int config = 2;
  }


  // TextualConventions

  // Scalars
  private MOScalar agentppSimMode;
  private MOScalar agentppSimDeleteRow;
  private MOScalar agentppSimDeleteTableContents;

  // Tables


//--AgentGen BEGIN=_MEMBERS
  private MOServer moServer;
  private OctetString myContext;
//--AgentGen END

  public AgentppSimulationMib() {
    agentppSimMode =
      new AgentppSimMode(oidAgentppSimMode, MOAccessImpl.ACCESS_READ_WRITE);
    agentppSimDeleteRow =
      new AgentppSimDeleteRow(oidAgentppSimDeleteRow,
                              MOAccessImpl.ACCESS_READ_WRITE);
    agentppSimDeleteTableContents =
      new AgentppSimDeleteTableContents(oidAgentppSimDeleteTableContents,
                                        MOAccessImpl.ACCESS_READ_WRITE);
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
//--AgentGen END




  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.agentppSimMode, context);
    server.register(this.agentppSimDeleteRow, context);
    server.register(this.agentppSimDeleteTableContents, context);
//--AgentGen BEGIN=_registerMOs
    moServer = server;
    myContext = context;
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.agentppSimMode, context);
    server.unregister(this.agentppSimDeleteRow, context);
    server.unregister(this.agentppSimDeleteTableContents, context);
//--AgentGen BEGIN=_unregisterMOs
    moServer = null;
    myContext = null;
//--AgentGen END
  }

  // Notifications

  // Scalars
  public class AgentppSimMode extends EnumeratedScalar<Integer32> {
    AgentppSimMode(OID oid, MOAccess access) {
      super(oid, access, new Integer32(),
            new int[] { AgentppSimModeEnum.oper,
                        AgentppSimModeEnum.config });
//--AgentGen BEGIN=agentppSimMode
      setValue(new Integer32(AgentppSimModeEnum.oper));
      setVolatile(true);
//--AgentGen END
    }

    public int setValue(Integer32 newValue) {
     //--AgentGen BEGIN=agentppSimMode::setValue
     SimMOFactory.setSimulationModeEnabled(newValue.getValue() ==
                                           AgentppSimModeEnum.config);
     //--AgentGen END
      return super.setValue(newValue);
    }

     //--AgentGen BEGIN=agentppSimMode::_METHODS
     //--AgentGen END

  }

  public class AgentppSimDeleteRow extends MOScalar<OID> {
    AgentppSimDeleteRow(OID oid, MOAccess access) {
      super(oid, access, new OID());
//--AgentGen BEGIN=agentppSimDeleteRow
      super.setValue(SnmpConstants.zeroDotZero);
      setVolatile(true);
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
     //--AgentGen BEGIN=agentppSimDeleteRow::isValueOK
     if (AgentppSimulationMib.this.moServer == null) {
       return SnmpConstants.SNMP_ERROR_RESOURCE_UNAVAILABLE;
     }
     OID oid = (OID)newValue;
     ManagedObject mo = getManagedObject(oid);
     if (!(mo instanceof MOTable)) {
       return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
     }
     MOTable table = (MOTable)mo;
     OID index = new OID(oid.getValue(), table.getOID().size(),
                         oid.size()-table.getOID().size());
     MOTableRow row = table.getModel().getRow(index);
     if (row == null) {
       return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
     }
     //--AgentGen END
      return valueOK;
    }

    public int setValue(OID newValue) {
     //--AgentGen BEGIN=agentppSimDeleteRow::setValue
     if (AgentppSimulationMib.this.moServer == null) {
       return SnmpConstants.SNMP_ERROR_COMMIT_FAILED;
     }
     ManagedObject mo = getManagedObject(newValue);
     if (!(mo instanceof MOTable)) {
       return SnmpConstants.SNMP_ERROR_COMMIT_FAILED;
     }
     MOTable table = (MOTable)mo;
     OID index = new OID(newValue.getValue(), table.getOID().size(),
                         newValue.size()-table.getOID().size());
     MOTableRow row = table.removeRow(index);
     if (row == null) {
       return SnmpConstants.SNMP_ERROR_COMMIT_FAILED;
     }
     //--AgentGen END
      return super.setValue(newValue);
    }

     //--AgentGen BEGIN=agentppSimDeleteRow::_METHODS
     //--AgentGen END

  }

  public class AgentppSimDeleteTableContents extends MOScalar<OID> {
    AgentppSimDeleteTableContents(OID oid, MOAccess access) {
      super(oid, access, new OID());
//--AgentGen BEGIN=agentppSimDeleteTableContents
      setVolatile(true);
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
     //--AgentGen BEGIN=agentppSimDeleteTableContents::isValueOK
     if (AgentppSimulationMib.this.moServer == null) {
       return SnmpConstants.SNMP_ERROR_RESOURCE_UNAVAILABLE;
     }
     OID oid = (OID)newValue;
     oid.append(0);
     ManagedObject mo = getManagedObject(oid);
     if ((!(mo instanceof MOTable)) ||
         (!(((MOTable)mo).getModel() instanceof MOMutableTableModel))) {
       return SnmpConstants.SNMP_ERROR_WRONG_VALUE;
     }
     //--AgentGen END
      return valueOK;
    }

    public int setValue(OID newValue) {
     //--AgentGen BEGIN=agentppSimDeleteTableContents::setValue
     if (AgentppSimulationMib.this.moServer == null) {
       return SnmpConstants.SNMP_ERROR_COMMIT_FAILED;
     }
     OID oid = (OID)newValue;
     oid.append(0);
     ManagedObject mo = getManagedObject(oid);
     if ((!(mo instanceof MOTable)) ||
         (!(((MOTable)mo).getModel() instanceof MOMutableTableModel))) {
       return SnmpConstants.SNMP_ERROR_COMMIT_FAILED;
     }
     MOTable table = (MOTable)mo;
     ((MOMutableTableModel)table.getModel()).clear();
     //--AgentGen END
      return super.setValue(newValue);
    }

     //--AgentGen BEGIN=agentppSimDeleteTableContents::_METHODS
     //--AgentGen END

  }


  // Value Validators

  // Rows and Factories


//--AgentGen BEGIN=_METHODS
  private synchronized ManagedObject getManagedObject(OID oid) {
    MOQuery query = new DefaultMOQuery(new DefaultMOContextScope(myContext,
        oid, true, oid, true), true);
    return this.moServer.lookup(query);
  }
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


