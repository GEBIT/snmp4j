/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpTsmMib.java  
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

import org.snmp4j.event.CounterEvent;
import org.snmp4j.event.CounterListener;
import org.snmp4j.mp.DefaultCounterListener;
import org.snmp4j.security.TSM;
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

public class SnmpTsmMib 
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup 
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(SnmpTsmMib.class);

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
  public static final OID oidSnmpTsmMib =
    new OID(new int[] { 1,3,6,1,2,1,190 });

  public static final OID oidSnmpTsmInvalidCaches = 
    new OID(new int[] { 1,3,6,1,2,1,190,1,1,1,0 });
  public static final OID oidSnmpTsmInadequateSecurityLevels = 
    new OID(new int[] { 1,3,6,1,2,1,190,1,1,2,0 });
  public static final OID oidSnmpTsmUnknownPrefixes = 
    new OID(new int[] { 1,3,6,1,2,1,190,1,1,3,0 });
  public static final OID oidSnmpTsmInvalidPrefixes = 
    new OID(new int[] { 1,3,6,1,2,1,190,1,1,4,0 });
  public static final OID oidSnmpTsmConfigurationUsePrefix = 
    new OID(new int[] { 1,3,6,1,2,1,190,1,2,1,0 });


  // Enumerations




  // TextualConventions
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_TRUTHVALUE = "TruthValue";

  // Scalars
  private MOScalar<Counter32> snmpTsmInvalidCaches;
  private MOScalar<Counter32> snmpTsmInadequateSecurityLevels;
  private MOScalar<Counter32> snmpTsmUnknownPrefixes;
  private MOScalar<Counter32> snmpTsmInvalidPrefixes;
  private MOScalar<Integer32> snmpTsmConfigurationUsePrefix;

  // Tables


//--AgentGen BEGIN=_MEMBERS
  private TSM tsm;
  private DefaultCounterListener counterListener = new DefaultCounterListener();
//--AgentGen END

  /**
   * Constructs a SnmpTsmMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling 
   * {@link #createMO(MOFactory moFactory)}. 
   */
  protected SnmpTsmMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
    counterListener.setCountRegisteredOnly(true);
//--AgentGen END
  }

  /**
   * Constructs a SnmpTsmMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied 
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public SnmpTsmMib(MOFactory moFactory) {
    this();
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
    counterListener.add(snmpTsmInvalidCaches.getOid(), snmpTsmInvalidCaches.getValue());
    counterListener.add(snmpTsmInadequateSecurityLevels.getOid(), snmpTsmInadequateSecurityLevels.getValue());
    counterListener.add(snmpTsmUnknownPrefixes.getOid(), snmpTsmUnknownPrefixes.getValue());
    counterListener.add(snmpTsmInvalidPrefixes.getOid(), snmpTsmInvalidPrefixes.getValue());
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
  @SuppressWarnings("unchecked")
  protected void createMO(MOFactory moFactory) {
    addTCsToFactory(moFactory);
    snmpTsmInvalidCaches = 
      moFactory.createScalar(oidSnmpTsmInvalidCaches,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTsmInadequateSecurityLevels = 
      moFactory.createScalar(oidSnmpTsmInadequateSecurityLevels,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTsmUnknownPrefixes = 
      moFactory.createScalar(oidSnmpTsmUnknownPrefixes,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTsmInvalidPrefixes = 
      moFactory.createScalar(oidSnmpTsmInvalidPrefixes,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTsmConfigurationUsePrefix = 
      new SnmpTsmConfigurationUsePrefix(oidSnmpTsmConfigurationUsePrefix, 
                                        moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
    snmpTsmConfigurationUsePrefix.addMOValueValidationListener(new SnmpTsmConfigurationUsePrefixValidator());
  }

  public MOScalar<Counter32> getSnmpTsmInvalidCaches() {
    return snmpTsmInvalidCaches;
  }
  public MOScalar<Counter32> getSnmpTsmInadequateSecurityLevels() {
    return snmpTsmInadequateSecurityLevels;
  }
  public MOScalar<Counter32> getSnmpTsmUnknownPrefixes() {
    return snmpTsmUnknownPrefixes;
  }
  public MOScalar<Counter32> getSnmpTsmInvalidPrefixes() {
    return snmpTsmInvalidPrefixes;
  }
  public MOScalar<Integer32> getSnmpTsmConfigurationUsePrefix() {
    return snmpTsmConfigurationUsePrefix;
  }




  public void registerMOs(MOServer server, OctetString context) 
    throws DuplicateRegistrationException 
  {
    // Scalar Objects
    server.register(this.snmpTsmInvalidCaches, context);
    server.register(this.snmpTsmInadequateSecurityLevels, context);
    server.register(this.snmpTsmUnknownPrefixes, context);
    server.register(this.snmpTsmInvalidPrefixes, context);
    server.register(this.snmpTsmConfigurationUsePrefix, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpTsmInvalidCaches, context);
    server.unregister(this.snmpTsmInadequateSecurityLevels, context);
    server.unregister(this.snmpTsmUnknownPrefixes, context);
    server.unregister(this.snmpTsmInvalidPrefixes, context);
    server.unregister(this.snmpTsmConfigurationUsePrefix, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars
  public class SnmpTsmConfigurationUsePrefix extends MOScalar<Integer32> {
    SnmpTsmConfigurationUsePrefix(OID oid, MOAccess access) {
      super(oid, access, new Integer32());
//--AgentGen BEGIN=snmpTsmConfigurationUsePrefix
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
     //--AgentGen BEGIN=snmpTsmConfigurationUsePrefix::isValueOK
     //--AgentGen END
      return valueOK; 
    }

    public Integer32 getValue() {
     //--AgentGen BEGIN=snmpTsmConfigurationUsePrefix::getValue
     //--AgentGen END
      return super.getValue();    
    }

    public int setValue(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTsmConfigurationUsePrefix::setValue
      TSM tsmCopy = tsm;
      if (tsmCopy != null) {
        tsmCopy.setUsePrefix(TruthValueTC.getBooleanValue(newValue));
      }
     //--AgentGen END
      return super.setValue(newValue);    
    }

     //--AgentGen BEGIN=snmpTsmConfigurationUsePrefix::_METHODS
     //--AgentGen END

  }


  // Value Validators
  /**
   * The <code>SnmpTsmConfigurationUsePrefixValidator</code> implements the value
   * validation for <code>SnmpTsmConfigurationUsePrefix</code>.
   */
  static class SnmpTsmConfigurationUsePrefixValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTsmConfigurationUsePrefix::validate
     //--AgentGen END
    }
  }


  // Rows and Factories


//--AgentGen BEGIN=_METHODS

  public TSM getTsm() {
    return tsm;
  }

  public void setTsm(TSM tsm) {
    if (this.tsm != null) {
      this.tsm.getCounterSupport().removeCounterListener(counterListener);
    }
    this.tsm = tsm;
    this.tsm.getCounterSupport().addCounterListener(counterListener);
    tsm.setUsePrefix(TruthValueTC.getBooleanValue(snmpTsmConfigurationUsePrefix.getValue()));
  }

//--AgentGen END

  // Textual Definitions of MIB module SnmpTsmMib
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


