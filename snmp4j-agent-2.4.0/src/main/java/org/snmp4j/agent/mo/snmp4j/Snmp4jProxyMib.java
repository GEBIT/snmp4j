/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Snmp4jProxyMib.java  
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

package org.snmp4j.agent.mo.snmp4j;

//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.PDU;
import org.snmp4j.Session;
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

public class Snmp4jProxyMib 
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup 
//--AgentGen BEGIN=_IMPLEMENTS
  , RowStatusListener, MOTableRowListener<Snmp4jProxyMib.Snmp4jProxyEntryRow>
//--AgentGen END
{

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(Snmp4jProxyMib.class);

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
  public static final OID oidSnmp4jProxyMib =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,3 });

  // Identities
  // Scalars
  // Tables

  // Notifications

  // Enumerations

  public static final class Snmp4jProxyTypeEnum {
    public static final int invalid = 0;
    public static final int readAndWrite = 1;
    public static final int readOnly = 2;
    public static final int noProxy = 3;
  }



  // TextualConventions
  private static final String TC_MODULE_SNMP_FRAMEWORK_MIB = "SNMP-FRAMEWORK-MIB";
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_SNMPADMINSTRING = "SnmpAdminString";
  private static final String TC_SNMPENGINEID = "SnmpEngineID";
  private static final String TC_STORAGETYPE = "StorageType";
  private static final String TC_ROWSTATUS = "RowStatus";

  // Scalars

  // Tables
  public static final OID oidSnmp4jProxyEntry = 
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,3,1,1,1 });

  // Index OID definitions
  public static final OID oidSnmp4jProxyName =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,3,1,1,1,1 });

  // Column TC definitions for snmp4jProxyEntry:
  public static final String tcModuleSnmpFrameworkMib = "SNMP-FRAMEWORK-MIB";
  public static final String tcDefSnmpEngineID = "SnmpEngineID";
  public static final String tcDefSnmpAdminString = "SnmpAdminString";
  public static final String tcModuleSNMPv2Tc = "SNMPv2-TC";
  public static final String tcDefStorageType = "StorageType";
  public static final String tcDefRowStatus = "RowStatus";
    
  // Column sub-identifer definitions for snmp4jProxyEntry:
  public static final int colSnmp4jProxyContextEngineID = 2;
  public static final int colSnmp4jProxyContextName = 3;
  public static final int colSnmp4jProxySubtree = 4;
  public static final int colSnmp4jProxyType = 5;
  public static final int colSnmp4jProxyTarget = 6;
  public static final int colSnmp4jProxyStorageType = 7;
  public static final int colSnmp4jProxyRowStatus = 8;
  public static final int colSnmp4jProxyTargetSubtree = 9;

  // Column index definitions for snmp4jProxyEntry:
  public static final int idxSnmp4jProxyContextEngineID = 0;
  public static final int idxSnmp4jProxyContextName = 1;
  public static final int idxSnmp4jProxySubtree = 2;
  public static final int idxSnmp4jProxyType = 3;
  public static final int idxSnmp4jProxyTarget = 4;
  public static final int idxSnmp4jProxyStorageType = 5;
  public static final int idxSnmp4jProxyRowStatus = 6;
  public static final int idxSnmp4jProxyTargetSubtree = 7;

  private MOTableSubIndex[] snmp4jProxyEntryIndexes;
  private MOTableIndex snmp4jProxyEntryIndex;
  
  private MOTable<Snmp4jProxyMib.Snmp4jProxyEntryRow,MOColumn,MOTableModel<Snmp4jProxyMib.Snmp4jProxyEntryRow>>
      snmp4jProxyEntry;
  private MOTableModel<Snmp4jProxyMib.Snmp4jProxyEntryRow> snmp4jProxyEntryModel;


//--AgentGen BEGIN=_MEMBERS
  private Session session;
  private MOServer server;
  private SnmpTargetMIB targetMIB;
//--AgentGen END

  /**
   * Constructs a Snmp4jProxyMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling 
   * {@link #createMO(MOFactory moFactory)}. 
   */
  protected Snmp4jProxyMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  /**
   * Constructs a Snmp4jProxyMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied 
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  protected Snmp4jProxyMib(MOFactory moFactory) {
  	this();
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
    snmp4jProxyEntry.addMOTableRowListener(this);
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
  /**
   * Constructs a Snmp4jProxyMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   * @param session
   *    the SNMP session to be used for forwarding requests.
   * @param server
   *    the {@link MOServer} used to register new subtree proxies.
   * @param targetMIB
   *    the {@link SnmpTargetMIB} instance for proxy target
   *    configuration.
   */
  public Snmp4jProxyMib(MOFactory moFactory, Session session,
                        MOServer server, SnmpTargetMIB targetMIB) {
    this(moFactory);
    this.session = session;
    this.server = server;
    this.targetMIB = targetMIB;
  }
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
    createSnmp4jProxyEntry(moFactory);
  }



  public MOTable getSnmp4jProxyEntry() {
    return snmp4jProxyEntry;
  }


  private void createSnmp4jProxyEntry(MOFactory moFactory) {
    // Index definition
    snmp4jProxyEntryIndexes = 
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidSnmp4jProxyName, 
                               SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
    };

    snmp4jProxyEntryIndex = 
      moFactory.createIndex(snmp4jProxyEntryIndexes,
                            true);

    // Columns
    MOColumn[] snmp4jProxyEntryColumns = new MOColumn[8];
    snmp4jProxyEntryColumns[idxSnmp4jProxyContextEngineID] = 
      new MOMutableColumn(colSnmp4jProxyContextEngineID,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          (OctetString)null);
    ValueConstraint snmp4jProxyContextEngineIDVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmp4jProxyContextEngineIDVC).add(new Constraint(5L, 32L));
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyContextEngineID]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jProxyContextEngineIDVC));                                  
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyContextEngineID]).
      addMOValueValidationListener(new Snmp4jProxyContextEngineIDValidator());
    snmp4jProxyEntryColumns[idxSnmp4jProxyContextName] = 
      new MOMutableColumn(colSnmp4jProxyContextName,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OctetString(new byte[] {  }));
    ValueConstraint snmp4jProxyContextNameVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmp4jProxyContextNameVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyContextName]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jProxyContextNameVC));                                  
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyContextName]).
      addMOValueValidationListener(new Snmp4jProxyContextNameValidator());
    snmp4jProxyEntryColumns[idxSnmp4jProxySubtree] = 
      new MOMutableColumn(colSnmp4jProxySubtree,
                          SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          (OID)null);
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxySubtree]).
      addMOValueValidationListener(new Snmp4jProxySubtreeValidator());
    snmp4jProxyEntryColumns[idxSnmp4jProxyType] = 
      new Enumerated<Integer32>(colSnmp4jProxyType,
                     SMIConstants.SYNTAX_INTEGER32,
                     moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                     new Integer32(1));
    ValueConstraint snmp4jProxyTypeVC = new EnumerationConstraint(
      new int[] { Snmp4jProxyTypeEnum.readAndWrite,
                  Snmp4jProxyTypeEnum.readOnly,
                  Snmp4jProxyTypeEnum.noProxy });
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyType]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jProxyTypeVC));                                  
    snmp4jProxyEntryColumns[idxSnmp4jProxyTarget] =
      new MOMutableColumn(colSnmp4jProxyTarget,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          (OctetString)null);
    ValueConstraint snmp4jProxyTargetVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmp4jProxyTargetVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyTarget]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jProxyTargetVC));                                  
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyTarget]).
      addMOValueValidationListener(new Snmp4jProxyTargetValidator());
    snmp4jProxyEntryColumns[idxSnmp4jProxyStorageType] = 
      new StorageType(colSnmp4jProxyStorageType,
                      moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                      (Integer32)null);
    ValueConstraint snmp4jProxyStorageTypeVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5 });
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyStorageType]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jProxyStorageTypeVC));                                  
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyStorageType]).
      addMOValueValidationListener(new Snmp4jProxyStorageTypeValidator());
    snmp4jProxyEntryColumns[idxSnmp4jProxyRowStatus] = 
      new RowStatus(colSnmp4jProxyRowStatus);
    ValueConstraint snmp4jProxyRowStatusVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5,
                  6 });
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyRowStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jProxyRowStatusVC));                                  
    ((MOMutableColumn)snmp4jProxyEntryColumns[idxSnmp4jProxyRowStatus]).
      addMOValueValidationListener(new Snmp4jProxyRowStatusValidator());
    snmp4jProxyEntryColumns[idxSnmp4jProxyTargetSubtree] = 
      new MOMutableColumn(colSnmp4jProxyTargetSubtree,
                          SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OID("0.0"));
    // Table model
    snmp4jProxyEntryModel = 
      moFactory.createTableModel(oidSnmp4jProxyEntry,
                                 snmp4jProxyEntryIndex,
                                 snmp4jProxyEntryColumns);
    ((MOMutableTableModel<Snmp4jProxyEntryRow>)snmp4jProxyEntryModel).setRowFactory(
      new Snmp4jProxyEntryRowFactory());
    snmp4jProxyEntry = 
      moFactory.createTable(oidSnmp4jProxyEntry,
                            snmp4jProxyEntryIndex,
                            snmp4jProxyEntryColumns,
                            snmp4jProxyEntryModel);
  }



  public void registerMOs(MOServer server, OctetString context) 
    throws DuplicateRegistrationException 
  {
    // Scalar Objects
    server.register(this.snmp4jProxyEntry, context);
//--AgentGen BEGIN=_registerMOs
    ((RowStatus)snmp4jProxyEntry.getColumn(idxSnmp4jProxyRowStatus)).addRowStatusListener(this);
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmp4jProxyEntry, context);
//--AgentGen BEGIN=_unregisterMOs
    ((RowStatus)snmp4jProxyEntry.getColumn(idxSnmp4jProxyRowStatus)).removeRowStatusListener(this);
//--AgentGen END
  }

  // Notifications

  // Scalars

  // Value Validators

  /**
   * The <code>Snmp4jProxyContextEngineIDValidator</code> implements the value
   * validation for <code>Snmp4jProxyContextEngineID</code>.
   */
  static class Snmp4jProxyContextEngineIDValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 5) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jProxyContextEngineID::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jProxyContextNameValidator</code> implements the value
   * validation for <code>Snmp4jProxyContextName</code>.
   */
  static class Snmp4jProxyContextNameValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jProxyContextName::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jProxySubtreeValidator</code> implements the value
   * validation for <code>Snmp4jProxySubtree</code>.
   */
  static class Snmp4jProxySubtreeValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jProxySubtree::validate

     //--AgentGen END
    }
  }

  /**
   * The <code>Snmp4jProxyTargetValidator</code> implements the value
   * validation for <code>Snmp4jProxyTarget</code>.
   */
  static class Snmp4jProxyTargetValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jProxyTarget::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jProxyStorageTypeValidator</code> implements the value
   * validation for <code>Snmp4jProxyStorageType</code>.
   */
  static class Snmp4jProxyStorageTypeValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jProxyStorageType::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jProxyRowStatusValidator</code> implements the value
   * validation for <code>Snmp4jProxyRowStatus</code>.
   */
  static class Snmp4jProxyRowStatusValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jProxyRowStatus::validate
     //--AgentGen END
    }
  }

  // Rows and Factories

  public class Snmp4jProxyEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=snmp4jProxyEntry::RowMembers
     //--AgentGen END

    public Snmp4jProxyEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=snmp4jProxyEntry::RowConstructor
     //--AgentGen END
    }
    
    public OctetString getSnmp4jProxyContextEngineID() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyContextEngineID
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmp4jProxyContextEngineID);
    }  
    
    public void setSnmp4jProxyContextEngineID(OctetString newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyContextEngineID
     //--AgentGen END
      super.setValue(idxSnmp4jProxyContextEngineID, newValue);
    }
    
    public OctetString getSnmp4jProxyContextName() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyContextName
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmp4jProxyContextName);
    }  
    
    public void setSnmp4jProxyContextName(OctetString newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyContextName
     //--AgentGen END
      super.setValue(idxSnmp4jProxyContextName, newValue);
    }
    
    public OID getSnmp4jProxySubtree() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxySubtree
     //--AgentGen END
      return (OID) super.getValue(idxSnmp4jProxySubtree);
    }  
    
    public void setSnmp4jProxySubtree(OID newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxySubtree
     //--AgentGen END
      super.setValue(idxSnmp4jProxySubtree, newValue);
    }
    
    public Integer32 getSnmp4jProxyType() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyType
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jProxyType);
    }  
    
    public void setSnmp4jProxyType(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyType
     //--AgentGen END
      super.setValue(idxSnmp4jProxyType, newValue);
    }
    
    public OctetString getSnmp4jProxyTarget() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyTarget
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmp4jProxyTarget);
    }  
    
    public void setSnmp4jProxyTarget(OctetString newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyTarget
     //--AgentGen END
      super.setValue(idxSnmp4jProxyTarget, newValue);
    }
    
    public Integer32 getSnmp4jProxyStorageType() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyStorageType
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jProxyStorageType);
    }  
    
    public void setSnmp4jProxyStorageType(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyStorageType
     //--AgentGen END
      super.setValue(idxSnmp4jProxyStorageType, newValue);
    }
    
    public Integer32 getSnmp4jProxyRowStatus() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyRowStatus
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmp4jProxyRowStatus);
    }  
    
    public void setSnmp4jProxyRowStatus(Integer32 newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyRowStatus
     //--AgentGen END
      super.setValue(idxSnmp4jProxyRowStatus, newValue);
    }
    
    public OID getSnmp4jProxyTargetSubtree() {
     //--AgentGen BEGIN=snmp4jProxyEntry::getSnmp4jProxyTargetSubtree
     //--AgentGen END
      return (OID) super.getValue(idxSnmp4jProxyTargetSubtree);
    }  
    
    public void setSnmp4jProxyTargetSubtree(OID newValue) {
     //--AgentGen BEGIN=snmp4jProxyEntry::setSnmp4jProxyTargetSubtree
     //--AgentGen END
      super.setValue(idxSnmp4jProxyTargetSubtree, newValue);
    }
    
    public Variable getValue(int column) {
     //--AgentGen BEGIN=snmp4jProxyEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxSnmp4jProxyContextEngineID: 
        	return getSnmp4jProxyContextEngineID();
        case idxSnmp4jProxyContextName: 
        	return getSnmp4jProxyContextName();
        case idxSnmp4jProxySubtree: 
        	return getSnmp4jProxySubtree();
        case idxSnmp4jProxyType: 
        	return getSnmp4jProxyType();
        case idxSnmp4jProxyTarget: 
        	return getSnmp4jProxyTarget();
        case idxSnmp4jProxyStorageType: 
        	return getSnmp4jProxyStorageType();
        case idxSnmp4jProxyRowStatus: 
        	return getSnmp4jProxyRowStatus();
        case idxSnmp4jProxyTargetSubtree: 
        	return getSnmp4jProxyTargetSubtree();
        default:
          return super.getValue(column);
      }
    }
    
    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=snmp4jProxyEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxSnmp4jProxyContextEngineID: 
        	setSnmp4jProxyContextEngineID((OctetString)value);
        	break;
        case idxSnmp4jProxyContextName: 
        	setSnmp4jProxyContextName((OctetString) value);
        	break;
        case idxSnmp4jProxySubtree: 
        	setSnmp4jProxySubtree((OID) value);
        	break;
        case idxSnmp4jProxyType: 
        	setSnmp4jProxyType((Integer32) value);
        	break;
        case idxSnmp4jProxyTarget: 
        	setSnmp4jProxyTarget((OctetString) value);
        	break;
        case idxSnmp4jProxyStorageType: 
        	setSnmp4jProxyStorageType((Integer32) value);
        	break;
        case idxSnmp4jProxyRowStatus: 
        	setSnmp4jProxyRowStatus((Integer32) value);
        	break;
        case idxSnmp4jProxyTargetSubtree: 
        	setSnmp4jProxyTargetSubtree((OID) value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=snmp4jProxyEntry::Row
     //--AgentGen END
  }
  
  class Snmp4jProxyEntryRowFactory 
        implements MOTableRowFactory<Snmp4jProxyEntryRow>
  {
    public synchronized Snmp4jProxyEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException 
    {
      Snmp4jProxyEntryRow row = 
        new Snmp4jProxyEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jProxyEntry::createRow
     //--AgentGen END
      return row;
    }
    
    public synchronized void freeRow(Snmp4jProxyEntryRow row) {
     //--AgentGen BEGIN=snmp4jProxyEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=snmp4jProxyEntry::RowFactory
     //--AgentGen END
  }


  //--AgentGen BEGIN=_METHODS
  public void rowStatusChanged(RowStatusEvent event) {
    Snmp4jProxyEntryRow row =
        (Snmp4jProxyEntryRow) event.getRow();
    OctetString proxyName = new OctetString();
    proxyName.fromSubIndex(row.getIndex(), true);
    if (event.isRowActivated()) {
      // check column interdependent consistency
      int reason = registerProxy(row);
      if (reason != PDU.noError) {
        event.setDenyReason(reason);
      }
    }
    else if (event.isRowDeactivated()) {
      unregisterProxy(row);
    }
  }

  private void unregisterProxy(Snmp4jProxyEntryRow row) {
    MOSubtreeProxy proxy = (MOSubtreeProxy) row.getUserObject();
    if (proxy != null) {
      server.unregister(proxy, row.getSnmp4jProxyContextName());
    }
  }

  private int registerProxy(Snmp4jProxyEntryRow row) {
    OctetString proxyName = new OctetString();
    proxyName.fromSubIndex(row.getIndex(), true);
    MOSubtreeProxy proxy =
        new MOSubtreeProxy(session, targetMIB, proxyName,
                           row.getSnmp4jProxySubtree(),
                           row.getSnmp4jProxyContextEngineID(),
                           row.getSnmp4jProxyContextName(),
                           row.getSnmp4jProxyTarget());
    OID targetOID = row.getSnmp4jProxyTargetSubtree();
    if ((targetOID != null) && (targetOID.size() > 0) &&
        !SnmpConstants.zeroDotZero.equals(targetOID)) {
      proxy.setTargetSubtree(targetOID);
    }
    try {
      server.register(proxy, row.getSnmp4jProxyContextName());
    }
    catch (DuplicateRegistrationException drex) {
      row.setSnmp4jProxyType(new Integer32(Snmp4jProxyTypeEnum.invalid));
      return PDU.inconsistentValue;
    }
    row.setUserObject(proxy);
    return PDU.noError;
  }

  public synchronized void rowChanged(MOTableRowEvent event) {
    Snmp4jProxyEntryRow row = (Snmp4jProxyEntryRow) event.getRow();
    switch (event.getType()) {
      case MOTableRowEvent.CREATE: {
        if (row.getSnmp4jProxyRowStatus().getValue() == RowStatus.active) {
          unregisterProxy(row);
          registerProxy(row);
        }
        break;
      }
      case MOTableRowEvent.DELETE: {
        unregisterProxy(row);
        break;
      }
    }
  }
  //--AgentGen END

  // Textual Definitions of MIB module Snmp4jProxyMib
  protected void addTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  public void addImportedTCsToFactory(MOFactory moFactory) {
   moFactory.addTextualConvention(new SnmpAdminString()); 
   moFactory.addTextualConvention(new SnmpEngineID()); 
  }

  // Textual Convention SnmpAdminString from MIB module SNMP-FRAMEWORK-MIB

  public class SnmpAdminString implements TextualConvention {
  	
    public SnmpAdminString() {
    }

    public String getModuleName() {
      return TC_MODULE_SNMP_FRAMEWORK_MIB;
    }
  	
    public String getName() {
      return TC_SNMPADMINSTRING;
    }
    
    public Variable createInitialValue() {
    	Variable v = new OctetString();
      if (v instanceof AssignableFromLong) {
      	((AssignableFromLong)v).setValue(0L);
      }
    	// further modify value to comply with TC constraints here:
     //--AgentGen BEGIN=SnmpAdminString::createInitialValue
     //--AgentGen END
	    return v;
    }
  	
    public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
      MOScalar scalar = moFactory.createScalar(oid, access, value);
      ValueConstraint vc = new ConstraintsImpl();
      ((ConstraintsImpl)vc).add(new Constraint(0L, 255L));
      scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));                                  
     //--AgentGen BEGIN=SnmpAdminString::createScalar
     //--AgentGen END
      return scalar;
    }
  	
    public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                 Variable defaultValue, boolean mutableInService) {
      MOColumn col = moFactory.createColumn(columnID, syntax, access, 
                                            defaultValue, mutableInService);
      if (col instanceof MOMutableColumn) {
        MOMutableColumn mcol = (MOMutableColumn)col;
        ValueConstraint vc = new ConstraintsImpl();
        ((ConstraintsImpl)vc).add(new Constraint(0L, 255L));
        mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));                                  
      }
     //--AgentGen BEGIN=SnmpAdminString::createColumn
     //--AgentGen END
      return col;      
    }
  }

  // Textual Convention SnmpEngineID from MIB module SNMP-FRAMEWORK-MIB

  public class SnmpEngineID implements TextualConvention {
  	
    public SnmpEngineID() {
    }

    public String getModuleName() {
      return TC_MODULE_SNMP_FRAMEWORK_MIB;
    }
  	
    public String getName() {
      return TC_SNMPENGINEID;
    }
    
    public Variable createInitialValue() {
    	Variable v = new OctetString();
      if (v instanceof AssignableFromLong) {
      	((AssignableFromLong)v).setValue(5L);
      }
    	// further modify value to comply with TC constraints here:
     //--AgentGen BEGIN=SnmpEngineID::createInitialValue
     //--AgentGen END
	    return v;
    }
  	
    public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
      MOScalar scalar = moFactory.createScalar(oid, access, value);
      ValueConstraint vc = new ConstraintsImpl();
      ((ConstraintsImpl)vc).add(new Constraint(5L, 32L));
      scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));                                  
     //--AgentGen BEGIN=SnmpEngineID::createScalar
     //--AgentGen END
      return scalar;
    }
  	
    public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                 Variable defaultValue, boolean mutableInService) {
      MOColumn col = moFactory.createColumn(columnID, syntax, access, 
                                            defaultValue, mutableInService);
      if (col instanceof MOMutableColumn) {
        MOMutableColumn mcol = (MOMutableColumn)col;
        ValueConstraint vc = new ConstraintsImpl();
        ((ConstraintsImpl)vc).add(new Constraint(5L, 32L));
        mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));                                  
      }
     //--AgentGen BEGIN=SnmpEngineID::createColumn
     //--AgentGen END
      return col;      
    }
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


