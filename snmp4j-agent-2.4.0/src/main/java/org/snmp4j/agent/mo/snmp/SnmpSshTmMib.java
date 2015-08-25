/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpSshTmMib.java  
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
 
//--AgentGen BEGIN=_BEGIN
//--AgentGen END

package org.snmp4j.agent.mo.snmp;

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.mo.snmp.tc.*;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class SnmpSshTmMib 
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup 
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(SnmpSshTmMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private MOFactory moFactory = 
    DefaultMOFactory.getInstance();

  // Constants 

  /**
   * OID of this MIB module for usage which can be 
   * used for its identification.
   */
  public static final OID oidSnmpSshTmMib =
    new OID(new int[] { 1,3,6,1,2,1,189 });

  public static final OID oidSnmpSshtmSessionOpens = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,1,0 });
  public static final OID oidSnmpSshtmSessionCloses = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,2,0 });
  public static final OID oidSnmpSshtmSessionOpenErrors = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,3,0 });
  public static final OID oidSnmpSshtmSessionUserAuthFailures = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,4,0 });
  public static final OID oidSnmpSshtmSessionNoChannels = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,5,0 });
  public static final OID oidSnmpSshtmSessionNoSubsystems = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,6,0 });
  public static final OID oidSnmpSshtmSessionNoSessions = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,7,0 });
  public static final OID oidSnmpSshtmSessionInvalidCaches = 
    new OID(new int[] { 1,3,6,1,2,1,189,1,1,8,0 });


  // Enumerations




  // TextualConventions

  // Scalars
  private MOScalar snmpSshtmSessionOpens;
  private MOScalar snmpSshtmSessionCloses;
  private MOScalar snmpSshtmSessionOpenErrors;
  private MOScalar snmpSshtmSessionUserAuthFailures;
  private MOScalar snmpSshtmSessionNoChannels;
  private MOScalar snmpSshtmSessionNoSubsystems;
  private MOScalar snmpSshtmSessionNoSessions;
  private MOScalar snmpSshtmSessionInvalidCaches;

  // Tables


//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  /**
   * Constructs a SnmpSshTmMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling 
   * {@link #createMO(MOFactory moFactory)}. 
   */
  protected SnmpSshTmMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  /**
   * Constructs a SnmpSshTmMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied 
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public SnmpSshTmMib(MOFactory moFactory) {
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
//--AgentGen END

  /**
   * Create the ManagedObjects defined for this MIB module
   * using the specified {@link MOFactory}.
   * @param moFactory
   *    the <code>MOFactory</code> instance to use for object 
   *    creation.
   */
  protected void createMO(MOFactory moFactory) {
    addTCsToFactory(moFactory);
    snmpSshtmSessionOpens = 
      moFactory.createScalar(oidSnmpSshtmSessionOpens,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionCloses = 
      moFactory.createScalar(oidSnmpSshtmSessionCloses,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionOpenErrors = 
      moFactory.createScalar(oidSnmpSshtmSessionOpenErrors,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionUserAuthFailures = 
      moFactory.createScalar(oidSnmpSshtmSessionUserAuthFailures,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionNoChannels = 
      moFactory.createScalar(oidSnmpSshtmSessionNoChannels,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionNoSubsystems = 
      moFactory.createScalar(oidSnmpSshtmSessionNoSubsystems,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionNoSessions = 
      moFactory.createScalar(oidSnmpSshtmSessionNoSessions,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpSshtmSessionInvalidCaches = 
      moFactory.createScalar(oidSnmpSshtmSessionInvalidCaches,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
  }

  public MOScalar getSnmpSshtmSessionOpens() {
    return snmpSshtmSessionOpens;
  }
  public MOScalar getSnmpSshtmSessionCloses() {
    return snmpSshtmSessionCloses;
  }
  public MOScalar getSnmpSshtmSessionOpenErrors() {
    return snmpSshtmSessionOpenErrors;
  }
  public MOScalar getSnmpSshtmSessionUserAuthFailures() {
    return snmpSshtmSessionUserAuthFailures;
  }
  public MOScalar getSnmpSshtmSessionNoChannels() {
    return snmpSshtmSessionNoChannels;
  }
  public MOScalar getSnmpSshtmSessionNoSubsystems() {
    return snmpSshtmSessionNoSubsystems;
  }
  public MOScalar getSnmpSshtmSessionNoSessions() {
    return snmpSshtmSessionNoSessions;
  }
  public MOScalar getSnmpSshtmSessionInvalidCaches() {
    return snmpSshtmSessionInvalidCaches;
  }




  public void registerMOs(MOServer server, OctetString context) 
    throws DuplicateRegistrationException 
  {
    // Scalar Objects
    server.register(this.snmpSshtmSessionOpens, context);
    server.register(this.snmpSshtmSessionCloses, context);
    server.register(this.snmpSshtmSessionOpenErrors, context);
    server.register(this.snmpSshtmSessionUserAuthFailures, context);
    server.register(this.snmpSshtmSessionNoChannels, context);
    server.register(this.snmpSshtmSessionNoSubsystems, context);
    server.register(this.snmpSshtmSessionNoSessions, context);
    server.register(this.snmpSshtmSessionInvalidCaches, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpSshtmSessionOpens, context);
    server.unregister(this.snmpSshtmSessionCloses, context);
    server.unregister(this.snmpSshtmSessionOpenErrors, context);
    server.unregister(this.snmpSshtmSessionUserAuthFailures, context);
    server.unregister(this.snmpSshtmSessionNoChannels, context);
    server.unregister(this.snmpSshtmSessionNoSubsystems, context);
    server.unregister(this.snmpSshtmSessionNoSessions, context);
    server.unregister(this.snmpSshtmSessionInvalidCaches, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars

  // Value Validators


  // Rows and Factories


//--AgentGen BEGIN=_METHODS
//--AgentGen END

  // Textual Definitions of MIB module SnmpSshTmMib
  protected void addTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  public void addImportedTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


