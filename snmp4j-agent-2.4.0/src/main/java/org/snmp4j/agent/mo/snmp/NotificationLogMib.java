/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - NotificationLogMib.java  
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

package org.snmp4j.agent.mo.snmp;
//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import java.util.*;

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.agent.mo.snmp.tc.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.agent.security.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;

//--AgentGen END

public class NotificationLogMib
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
,NotificationLogListener, RowStatusListener
//--AgentGen END
{

  private static final LogAdapter LOGGER =
      LogFactory.getLogger(NotificationLogMib.class);

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
  public static final OID oidNotificationLogMib =
    new OID(new int[] { 1,3,6,1,2,1,92 });

  public static final OID oidNlmConfigGlobalEntryLimit =
    new OID(new int[] { 1,3,6,1,2,1,92,1,1,1,0 });
  public static final OID oidNlmConfigGlobalAgeOut =
    new OID(new int[] { 1,3,6,1,2,1,92,1,1,2,0 });
  public static final OID oidNlmStatsGlobalNotificationsLogged =
    new OID(new int[] { 1,3,6,1,2,1,92,1,2,1,0 });
  public static final OID oidNlmStatsGlobalNotificationsBumped =
    new OID(new int[] { 1,3,6,1,2,1,92,1,2,2,0 });


  // Enumerations

  public static final class NlmConfigLogAdminStatusEnum {
    public static final int enabled = 1;
    public static final int disabled = 2;
  }
  public static final class NlmConfigLogOperStatusEnum {
    public static final int disabled = 1;
    public static final int operational = 2;
    public static final int noFilter = 3;
  }
  public static final class NlmLogVariableValueTypeEnum {
    public static final int counter32 = 1;
    public static final int unsigned32 = 2;
    public static final int timeTicks = 3;
    public static final int integer32 = 4;
    public static final int ipAddress = 5;
    public static final int octetString = 6;
    public static final int objectId = 7;
    public static final int counter64 = 8;
    public static final int opaque = 9;
  }



  // TextualConventions
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_MODULE_SNMP_FRAMEWORK_MIB = "SNMP-FRAMEWORK-MIB";
  private static final String TC_DATEANDTIME = "DateAndTime";
  private static final String TC_SNMPADMINSTRING = "SnmpAdminString";
  private static final String TC_TADDRESS = "TAddress";
  private static final String TC_ROWSTATUS = "RowStatus";
  private static final String TC_TDOMAIN = "TDomain";
  private static final String TC_TIMESTAMP = "TimeStamp";
  private static final String TC_STORAGETYPE = "StorageType";
  private static final String TC_SNMPENGINEID = "SnmpEngineID";

  // Scalars
  private MOScalar<UnsignedInteger32> nlmConfigGlobalEntryLimit;
  private MOScalar<UnsignedInteger32> nlmConfigGlobalAgeOut;
  private MOScalar<Counter32> nlmStatsGlobalNotificationsLogged;
  private MOScalar<Counter32> nlmStatsGlobalNotificationsBumped;

  // Tables
  public static final OID oidNlmConfigLogEntry =
    new OID(new int[] { 1,3,6,1,2,1,92,1,1,3,1 });

  // Index OID definitions
  public static final OID oidNlmLogName =
    new OID(new int[] { 1,3,6,1,2,1,92,1,1,3,1,1 });

  // Column TC definitions for nlmConfigLogEntry:
  public static final String tcModuleSnmpFrameworkMib = "SNMP-FRAMEWORK-MIB";
  public static final String tcDefSnmpAdminString = "SnmpAdminString";
  public static final String tcModuleSNMPv2Tc = "SNMPv2-TC";
  public static final String tcDefStorageType = "StorageType";
  public static final String tcDefRowStatus = "RowStatus";

  // Column sub-identifer definitions for nlmConfigLogEntry:
  public static final int colNlmConfigLogFilterName = 2;
  public static final int colNlmConfigLogEntryLimit = 3;
  public static final int colNlmConfigLogAdminStatus = 4;
  public static final int colNlmConfigLogOperStatus = 5;
  public static final int colNlmConfigLogStorageType = 6;
  public static final int colNlmConfigLogEntryStatus = 7;

  // Column index definitions for nlmConfigLogEntry:
  public static final int idxNlmConfigLogFilterName = 0;
  public static final int idxNlmConfigLogEntryLimit = 1;
  public static final int idxNlmConfigLogAdminStatus = 2;
  public static final int idxNlmConfigLogOperStatus = 3;
  public static final int idxNlmConfigLogStorageType = 4;
  public static final int idxNlmConfigLogEntryStatus = 5;

  private MOTableSubIndex[] nlmConfigLogEntryIndexes;
  private MOTableIndex nlmConfigLogEntryIndex;

  private MOTable<NlmConfigLogEntryRow,MOColumn,MOTableModel<NlmConfigLogEntryRow>>
      nlmConfigLogEntry;
  private MOTableModel<NlmConfigLogEntryRow> nlmConfigLogEntryModel;
  public static final OID oidNlmStatsLogEntry =
    new OID(new int[] { 1,3,6,1,2,1,92,1,2,3,1 });

  // Index OID definitions
  //public static final OID oidNlmLogName =
  //  new OID(new int[] { 1,3,6,1,2,1,92,1,1,3,1,1 });

  // Column TC definitions for nlmStatsLogEntry:

  // Column sub-identifier definitions for nlmStatsLogEntry:
  public static final int colNlmStatsLogNotificationsLogged = 1;
  public static final int colNlmStatsLogNotificationsBumped = 2;

  // Column index definitions for nlmStatsLogEntry:
  public static final int idxNlmStatsLogNotificationsLogged = 0;
  public static final int idxNlmStatsLogNotificationsBumped = 1;

  private MOTableSubIndex[] nlmStatsLogEntryIndexes;
  private MOTableIndex nlmStatsLogEntryIndex;

  private MOTable<NlmStatsLogEntryRow,MOColumn,MOTableModel<NlmStatsLogEntryRow>>
      nlmStatsLogEntry;
  private MOTableModel<NlmStatsLogEntryRow> nlmStatsLogEntryModel;
  public static final OID oidNlmLogEntry =
    new OID(new int[] { 1,3,6,1,2,1,92,1,3,1,1 });

  // Index OID definitions
  //public static final OID oidNlmLogName =
  //  new OID(new int[] { 1,3,6,1,2,1,92,1,1,3,1,1 });
  public static final OID oidNlmLogIndex =
    new OID(new int[] { 1,3,6,1,2,1,92,1,3,1,1,1 });

  // Column TC definitions for nlmLogEntry:
  public static final String tcDefTimeStamp = "TimeStamp";
  public static final String tcDefDateAndTime = "DateAndTime";
  public static final String tcDefSnmpEngineID = "SnmpEngineID";
  public static final String tcDefTAddress = "TAddress";
  public static final String tcDefTDomain = "TDomain";

  // Column sub-identifer definitions for nlmLogEntry:
  public static final int colNlmLogTime = 2;
  public static final int colNlmLogDateAndTime = 3;
  public static final int colNlmLogEngineID = 4;
  public static final int colNlmLogEngineTAddress = 5;
  public static final int colNlmLogEngineTDomain = 6;
  public static final int colNlmLogContextEngineID = 7;
  public static final int colNlmLogContextName = 8;
  public static final int colNlmLogNotificationID = 9;

  // Column index definitions for nlmLogEntry:
  public static final int idxNlmLogTime = 0;
  public static final int idxNlmLogDateAndTime = 1;
  public static final int idxNlmLogEngineID = 2;
  public static final int idxNlmLogEngineTAddress = 3;
  public static final int idxNlmLogEngineTDomain = 4;
  public static final int idxNlmLogContextEngineID = 5;
  public static final int idxNlmLogContextName = 6;
  public static final int idxNlmLogNotificationID = 7;

  private MOTableSubIndex[] nlmLogEntryIndexes;
  private MOTableIndex nlmLogEntryIndex;

  private MOTable<NlmLogEntryRow,MOColumn,MOTableModel<NlmLogEntryRow>>
      nlmLogEntry;
  private MOTableModel<NlmLogEntryRow> nlmLogEntryModel;
  public static final OID oidNlmLogVariableEntry =
    new OID(new int[] { 1,3,6,1,2,1,92,1,3,2,1 });

  // Index OID definitions
  //public static final OID oidNlmLogName =
  //  new OID(new int[] { 1,3,6,1,2,1,92,1,1,3,1,1 });
  //public static final OID oidNlmLogIndex =
  //  new OID(new int[] { 1,3,6,1,2,1,92,1,3,1,1,1 });
  public static final OID oidNlmLogVariableIndex =
    new OID(new int[] { 1,3,6,1,2,1,92,1,3,2,1,1 });

  // Column TC definitions for nlmLogVariableEntry:

  // Column sub-identifer definitions for nlmLogVariableEntry:
  public static final int colNlmLogVariableID = 2;
  public static final int colNlmLogVariableValueType = 3;
  public static final int colNlmLogVariableCounter32Val = 4;
  public static final int colNlmLogVariableUnsigned32Val = 5;
  public static final int colNlmLogVariableTimeTicksVal = 6;
  public static final int colNlmLogVariableInteger32Val = 7;
  public static final int colNlmLogVariableOctetStringVal = 8;
  public static final int colNlmLogVariableIpAddressVal = 9;
  public static final int colNlmLogVariableOidVal = 10;
  public static final int colNlmLogVariableCounter64Val = 11;
  public static final int colNlmLogVariableOpaqueVal = 12;

  // Column index definitions for nlmLogVariableEntry:
  public static final int idxNlmLogVariableID = 0;
  public static final int idxNlmLogVariableValueType = 1;
  public static final int idxNlmLogVariableCounter32Val = 2;
  public static final int idxNlmLogVariableUnsigned32Val = 3;
  public static final int idxNlmLogVariableTimeTicksVal = 4;
  public static final int idxNlmLogVariableInteger32Val = 5;
  public static final int idxNlmLogVariableOctetStringVal = 6;
  public static final int idxNlmLogVariableIpAddressVal = 7;
  public static final int idxNlmLogVariableOidVal = 8;
  public static final int idxNlmLogVariableCounter64Val = 9;
  public static final int idxNlmLogVariableOpaqueVal = 10;

  private MOTableSubIndex[] nlmLogVariableEntryIndexes;
  private MOTableIndex nlmLogVariableEntryIndex;

  private MOTable<NlmLogVariableEntryRow,MOColumn,MOTableModel<NlmLogVariableEntryRow>>
      nlmLogVariableEntry;
  private MOTableModel<NlmLogVariableEntryRow> nlmLogVariableEntryModel;


//--AgentGen BEGIN=_MEMBERS
  public static final OID oidSnmp4jNotificationLogMode =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,3,0 });

  // Enumerations
  public static final class Snmp4jNotificationLogModeEnum {
    public static final int fired = 1;
    public static final int sent = 2;
  }

  private static final int idxNlmConfigLogViewName = 6;
  private static final int VAR_SYNTAX_LIST[] = {
    SMIConstants.SYNTAX_COUNTER32,
    SMIConstants.SYNTAX_UNSIGNED_INTEGER32,
    SMIConstants.SYNTAX_TIMETICKS,
    SMIConstants.SYNTAX_INTEGER32,
    SMIConstants.SYNTAX_OCTET_STRING,
    SMIConstants.SYNTAX_IPADDRESS,
    SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
    SMIConstants.SYNTAX_COUNTER64,
    SMIConstants.SYNTAX_OPAQUE
  };

  private VACM vacm;
  private SnmpNotificationMIB snmpNotificationMIB;
  private long nextLogIndex = 0;
  private TDomainAddressFactory addressFactory = new TDomainAddressFactoryImpl();
  private MOTableRelation<NlmConfigLogEntryRow,NlmStatsLogEntryRow> nlmStatsLogEntryAugmentation;
  private List<OID> nlmLogEntries = new ArrayList<OID>();

  private MOScalar<Integer32> snmp4jNotificationLogMode;

//--AgentGen END

  /**
   * Constructs a NotificationLogMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling
   * {@link #createMO(MOFactory moFactory)}.
   */
  protected NotificationLogMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  /**
   * Constructs a NotificationLogMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public NotificationLogMib(MOFactory moFactory) {
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
    ((DefaultMOTable)nlmLogEntry).setVolatile(true);
    ((DefaultMOTable)nlmLogVariableEntry).setVolatile(true);
    ((DefaultMOTable)nlmStatsLogEntry).setVolatile(true);
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
  public NotificationLogMib(MOFactory moFactory, VACM vacm,
                            SnmpNotificationMIB snmpNotificationMIB) {
    this(moFactory);
    this.vacm = vacm;
    this.snmpNotificationMIB = snmpNotificationMIB;
    this.nlmStatsLogEntryAugmentation =
        new MOTableRelation<NlmConfigLogEntryRow,NlmStatsLogEntryRow>(nlmConfigLogEntry, nlmStatsLogEntry);
    this.nlmStatsLogEntryAugmentation.createRelationShip();
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
    nlmConfigGlobalEntryLimit =
      new NlmConfigGlobalEntryLimit(oidNlmConfigGlobalEntryLimit,
                                    moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
    nlmConfigGlobalAgeOut =
      new NlmConfigGlobalAgeOut(oidNlmConfigGlobalAgeOut,
                                moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
    nlmStatsGlobalNotificationsLogged =
      moFactory.createScalar(oidNlmStatsGlobalNotificationsLogged,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             new Counter32());
    nlmStatsGlobalNotificationsBumped =
      moFactory.createScalar(oidNlmStatsGlobalNotificationsBumped,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             new Counter32());
    snmp4jNotificationLogMode =
      moFactory.createScalar(oidSnmp4jNotificationLogMode,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),
                             new Integer32(Snmp4jNotificationLogModeEnum.fired));
    snmp4jNotificationLogMode.addMOValueValidationListener(
      new ValueConstraintValidator(
        new EnumerationConstraint(new int[] {
                                  Snmp4jNotificationLogModeEnum.fired,
                                  Snmp4jNotificationLogModeEnum.sent })));
    createNlmConfigLogEntry(moFactory);
    createNlmStatsLogEntry(moFactory);
    createNlmLogEntry(moFactory);
    createNlmLogVariableEntry(moFactory);
  }

  public MOScalar<UnsignedInteger32> getNlmConfigGlobalEntryLimit() {
    return nlmConfigGlobalEntryLimit;
  }
  public MOScalar<UnsignedInteger32> getNlmConfigGlobalAgeOut() {
    return nlmConfigGlobalAgeOut;
  }
  public MOScalar<Counter32> getNlmStatsGlobalNotificationsLogged() {
    return nlmStatsGlobalNotificationsLogged;
  }
  public MOScalar<Counter32> getNlmStatsGlobalNotificationsBumped() {
    return nlmStatsGlobalNotificationsBumped;
  }


  public MOTable<NlmConfigLogEntryRow,MOColumn,MOTableModel<NlmConfigLogEntryRow>> getNlmConfigLogEntry() {
    return nlmConfigLogEntry;
  }


  private void createNlmConfigLogEntry(MOFactory moFactory) {
    // Index definition
    nlmConfigLogEntryIndexes =
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidNlmLogName,
                               SMIConstants.SYNTAX_OCTET_STRING, 0, 32)
    };

    nlmConfigLogEntryIndex =
      moFactory.createIndex(nlmConfigLogEntryIndexes,
                            false);

    // Columns
    MOColumn[] nlmConfigLogEntryColumns = new MOColumn[6];
    nlmConfigLogEntryColumns[idxNlmConfigLogFilterName] =
      new MOMutableColumn(colNlmConfigLogFilterName,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OctetString(new byte[] {  }));
    ValueConstraint nlmConfigLogFilterNameVC = new ConstraintsImpl();
    ((ConstraintsImpl)nlmConfigLogFilterNameVC).add(new Constraint(0L, 32L));
    ((MOMutableColumn)nlmConfigLogEntryColumns[idxNlmConfigLogFilterName]).
      addMOValueValidationListener(new ValueConstraintValidator(nlmConfigLogFilterNameVC));
    ((MOMutableColumn)nlmConfigLogEntryColumns[idxNlmConfigLogFilterName]).
      addMOValueValidationListener(new NlmConfigLogFilterNameValidator());
    nlmConfigLogEntryColumns[idxNlmConfigLogEntryLimit] =
      new MOMutableColumn(colNlmConfigLogEntryLimit,
                          SMIConstants.SYNTAX_GAUGE32,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new UnsignedInteger32(0));
    nlmConfigLogEntryColumns[idxNlmConfigLogAdminStatus] =
      new Enumerated<Integer32>(colNlmConfigLogAdminStatus,
                     SMIConstants.SYNTAX_INTEGER32,
                     moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                     new Integer32(1));
    ValueConstraint nlmConfigLogAdminStatusVC = new EnumerationConstraint(
      new int[] { NlmConfigLogAdminStatusEnum.enabled,
                  NlmConfigLogAdminStatusEnum.disabled });
    ((MOMutableColumn)nlmConfigLogEntryColumns[idxNlmConfigLogAdminStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(nlmConfigLogAdminStatusVC));
    nlmConfigLogEntryColumns[idxNlmConfigLogOperStatus] =
      moFactory.createColumn(colNlmConfigLogOperStatus,
                             SMIConstants.SYNTAX_INTEGER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmConfigLogEntryColumns[idxNlmConfigLogStorageType] =
      new StorageType(colNlmConfigLogStorageType,
                      moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                      (Integer32)null);
    ValueConstraint nlmConfigLogStorageTypeVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5 });
    ((MOMutableColumn)nlmConfigLogEntryColumns[idxNlmConfigLogStorageType]).
      addMOValueValidationListener(new ValueConstraintValidator(nlmConfigLogStorageTypeVC));
    nlmConfigLogEntryColumns[idxNlmConfigLogEntryStatus] =
      new RowStatus(colNlmConfigLogEntryStatus);
    ValueConstraint nlmConfigLogEntryStatusVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5,
                  6 });
    ((MOMutableColumn)nlmConfigLogEntryColumns[idxNlmConfigLogEntryStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(nlmConfigLogEntryStatusVC));
    // Table model
    nlmConfigLogEntryModel =
      moFactory.createTableModel(oidNlmConfigLogEntry,
                                 nlmConfigLogEntryIndex,
                                 nlmConfigLogEntryColumns);
    ((MOMutableTableModel<NlmConfigLogEntryRow>)nlmConfigLogEntryModel).setRowFactory(
      new NlmConfigLogEntryRowFactory());
    nlmConfigLogEntry =
      moFactory.createTable(oidNlmConfigLogEntry,
                            nlmConfigLogEntryIndex,
                            nlmConfigLogEntryColumns,
                            nlmConfigLogEntryModel);
  }

  public MOTable getNlmStatsLogEntry() {
    return nlmStatsLogEntry;
  }


  private void createNlmStatsLogEntry(MOFactory moFactory) {
    // Index definition
    nlmStatsLogEntryIndexes =
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidNlmLogName,
                               SMIConstants.SYNTAX_OCTET_STRING, 0, 32)
    };

    nlmStatsLogEntryIndex =
      moFactory.createIndex(nlmStatsLogEntryIndexes,
                            false);

    // Columns
    MOColumn[] nlmStatsLogEntryColumns = new MOColumn[2];
    nlmStatsLogEntryColumns[idxNlmStatsLogNotificationsLogged] =
      moFactory.createColumn(colNlmStatsLogNotificationsLogged,
                             SMIConstants.SYNTAX_COUNTER32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmStatsLogEntryColumns[idxNlmStatsLogNotificationsBumped] =
      moFactory.createColumn(colNlmStatsLogNotificationsBumped,
                             SMIConstants.SYNTAX_COUNTER32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    // Table model
    nlmStatsLogEntryModel =
      moFactory.createTableModel(oidNlmStatsLogEntry,
                                 nlmStatsLogEntryIndex,
                                 nlmStatsLogEntryColumns);
    ((MOMutableTableModel<NlmStatsLogEntryRow>)nlmStatsLogEntryModel).setRowFactory(
      new NlmStatsLogEntryRowFactory());
    nlmStatsLogEntry =
      moFactory.createTable(oidNlmStatsLogEntry,
                            nlmStatsLogEntryIndex,
                            nlmStatsLogEntryColumns,
                            nlmStatsLogEntryModel);
  }

  public MOTable getNlmLogEntry() {
    return nlmLogEntry;
  }


  private void createNlmLogEntry(MOFactory moFactory) {
    // Index definition
    nlmLogEntryIndexes =
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidNlmLogName,
                               SMIConstants.SYNTAX_OCTET_STRING, 0, 32)
,
      moFactory.createSubIndex(oidNlmLogIndex,
                               SMIConstants.SYNTAX_INTEGER, 1, 1)    };

    nlmLogEntryIndex =
      moFactory.createIndex(nlmLogEntryIndexes,
                            false);

    // Columns
    MOColumn[] nlmLogEntryColumns = new MOColumn[8];
    nlmLogEntryColumns[idxNlmLogTime] =
      moFactory.createColumn(colNlmLogTime,
                             SMIConstants.SYNTAX_TIMETICKS,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSNMPv2Tc,
                             tcDefTimeStamp);
    nlmLogEntryColumns[idxNlmLogDateAndTime] =
      moFactory.createColumn(colNlmLogDateAndTime,
                             SMIConstants.SYNTAX_OCTET_STRING,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSNMPv2Tc,
                             tcDefDateAndTime);
    nlmLogEntryColumns[idxNlmLogEngineID] =
      moFactory.createColumn(colNlmLogEngineID,
                             SMIConstants.SYNTAX_OCTET_STRING,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSnmpFrameworkMib,
                             tcDefSnmpEngineID);
    nlmLogEntryColumns[idxNlmLogEngineTAddress] =
      moFactory.createColumn(colNlmLogEngineTAddress,
                             SMIConstants.SYNTAX_OCTET_STRING,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSNMPv2Tc,
                             tcDefTAddress);
    nlmLogEntryColumns[idxNlmLogEngineTDomain] =
      moFactory.createColumn(colNlmLogEngineTDomain,
                             SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSNMPv2Tc,
                             tcDefTDomain);
    nlmLogEntryColumns[idxNlmLogContextEngineID] =
      moFactory.createColumn(colNlmLogContextEngineID,
                             SMIConstants.SYNTAX_OCTET_STRING,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSnmpFrameworkMib,
                             tcDefSnmpEngineID);
    nlmLogEntryColumns[idxNlmLogContextName] =
      moFactory.createColumn(colNlmLogContextName,
                             SMIConstants.SYNTAX_OCTET_STRING,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY),
                             tcModuleSnmpFrameworkMib,
                             tcDefSnmpAdminString);
    nlmLogEntryColumns[idxNlmLogNotificationID] =
      moFactory.createColumn(colNlmLogNotificationID,
                             SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    // Table model
    nlmLogEntryModel =
      moFactory.createTableModel(oidNlmLogEntry,
                                 nlmLogEntryIndex,
                                 nlmLogEntryColumns);
    ((MOMutableTableModel<NlmLogEntryRow>)nlmLogEntryModel).setRowFactory(
      new NlmLogEntryRowFactory());
    nlmLogEntry =
      moFactory.createTable(oidNlmLogEntry,
                            nlmLogEntryIndex,
                            nlmLogEntryColumns,
                            nlmLogEntryModel);
  }

  public MOTable getNlmLogVariableEntry() {
    return nlmLogVariableEntry;
  }


  private void createNlmLogVariableEntry(MOFactory moFactory) {
    // Index definition
    nlmLogVariableEntryIndexes =
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidNlmLogName,
                               SMIConstants.SYNTAX_OCTET_STRING, 0, 32)
,
      moFactory.createSubIndex(oidNlmLogIndex,
                               SMIConstants.SYNTAX_INTEGER, 1, 1),
      moFactory.createSubIndex(oidNlmLogVariableIndex,
                               SMIConstants.SYNTAX_INTEGER, 1, 1)    };

    nlmLogVariableEntryIndex =
      moFactory.createIndex(nlmLogVariableEntryIndexes,
                            false);

    // Columns
    MOColumn[] nlmLogVariableEntryColumns = new MOColumn[11];
    nlmLogVariableEntryColumns[idxNlmLogVariableID] =
      moFactory.createColumn(colNlmLogVariableID,
                             SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableValueType] =
      moFactory.createColumn(colNlmLogVariableValueType,
                             SMIConstants.SYNTAX_INTEGER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableCounter32Val] =
      moFactory.createColumn(colNlmLogVariableCounter32Val,
                             SMIConstants.SYNTAX_COUNTER32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableUnsigned32Val] =
      moFactory.createColumn(colNlmLogVariableUnsigned32Val,
                             SMIConstants.SYNTAX_GAUGE32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableTimeTicksVal] =
      moFactory.createColumn(colNlmLogVariableTimeTicksVal,
                             SMIConstants.SYNTAX_TIMETICKS,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableInteger32Val] =
      moFactory.createColumn(colNlmLogVariableInteger32Val,
                             SMIConstants.SYNTAX_INTEGER32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableOctetStringVal] =
      moFactory.createColumn(colNlmLogVariableOctetStringVal,
                             SMIConstants.SYNTAX_OCTET_STRING,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableIpAddressVal] =
      moFactory.createColumn(colNlmLogVariableIpAddressVal,
                             SMIConstants.SYNTAX_IPADDRESS,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableOidVal] =
      moFactory.createColumn(colNlmLogVariableOidVal,
                             SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableCounter64Val] =
      moFactory.createColumn(colNlmLogVariableCounter64Val,
                             SMIConstants.SYNTAX_COUNTER64,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    nlmLogVariableEntryColumns[idxNlmLogVariableOpaqueVal] =
      moFactory.createColumn(colNlmLogVariableOpaqueVal,
                             SMIConstants.SYNTAX_OPAQUE,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    // Table model
    nlmLogVariableEntryModel =
      moFactory.createTableModel(oidNlmLogVariableEntry,
                                 nlmLogVariableEntryIndex,
                                 nlmLogVariableEntryColumns);
    ((MOMutableTableModel<NlmLogVariableEntryRow>)nlmLogVariableEntryModel).setRowFactory(
      new NlmLogVariableEntryRowFactory());
    nlmLogVariableEntry =
      moFactory.createTable(oidNlmLogVariableEntry,
                            nlmLogVariableEntryIndex,
                            nlmLogVariableEntryColumns,
                            nlmLogVariableEntryModel);
  }


  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.nlmConfigGlobalEntryLimit, context);
    server.register(this.nlmConfigGlobalAgeOut, context);
    server.register(this.nlmStatsGlobalNotificationsLogged, context);
    server.register(this.nlmStatsGlobalNotificationsBumped, context);
    server.register(this.nlmConfigLogEntry, context);
    server.register(this.nlmStatsLogEntry, context);
    server.register(this.nlmLogEntry, context);
    server.register(this.nlmLogVariableEntry, context);
//--AgentGen BEGIN=_registerMOs
    server.register(this.snmp4jNotificationLogMode, context);
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.nlmConfigGlobalEntryLimit, context);
    server.unregister(this.nlmConfigGlobalAgeOut, context);
    server.unregister(this.nlmStatsGlobalNotificationsLogged, context);
    server.unregister(this.nlmStatsGlobalNotificationsBumped, context);
    server.unregister(this.nlmConfigLogEntry, context);
    server.unregister(this.nlmStatsLogEntry, context);
    server.unregister(this.nlmLogEntry, context);
    server.unregister(this.nlmLogVariableEntry, context);
//--AgentGen BEGIN=_unregisterMOs
    server.unregister(this.snmp4jNotificationLogMode, context);
//--AgentGen END
  }

  // Notifications

  // Scalars
  public class NlmConfigGlobalEntryLimit extends MOScalar<UnsignedInteger32> {
    NlmConfigGlobalEntryLimit(OID oid, MOAccess access) {
      super(oid, access, new UnsignedInteger32());
//--AgentGen BEGIN=nlmConfigGlobalEntryLimit
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
     //--AgentGen BEGIN=nlmConfigGlobalEntryLimit::isValueOK
     //--AgentGen END
      return valueOK;
    }

    public UnsignedInteger32 getValue() {
     //--AgentGen BEGIN=nlmConfigGlobalEntryLimit::getValue
     //--AgentGen END
      return super.getValue();
    }

    public int setValue(UnsignedInteger32 newValue) {
     //--AgentGen BEGIN=nlmConfigGlobalEntryLimit::setValue
     //--AgentGen END
      return super.setValue(newValue);
    }

     //--AgentGen BEGIN=nlmConfigGlobalEntryLimit::_METHODS
    public void commit(SubRequest sreq) {
      super.commit(sreq);
      if (sreq.getStatus().getErrorStatus() == PDU.noError) {
        OID index = sreq.getVariableBinding().getOid();
        index = new OID(index.getValue(),
                        oidNlmConfigLogEntry.size() + 1, index.size() -
                        oidNlmConfigLogEntry.size() + 1);
        OctetString profileName = new OctetString();
        profileName.fromSubIndex(index, false);
        checkLimits(profileName);
      }
    }
     //--AgentGen END

  }

  public class NlmConfigGlobalAgeOut extends MOScalar<UnsignedInteger32> {
    NlmConfigGlobalAgeOut(OID oid, MOAccess access) {
      super(oid, access, new UnsignedInteger32());
//--AgentGen BEGIN=nlmConfigGlobalAgeOut
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
     //--AgentGen BEGIN=nlmConfigGlobalAgeOut::isValueOK
     //--AgentGen END
      return valueOK;
    }

    public UnsignedInteger32 getValue() {
     //--AgentGen BEGIN=nlmConfigGlobalAgeOut::getValue
     //--AgentGen END
      return super.getValue();
    }

    public int setValue(UnsignedInteger32 newValue) {
     //--AgentGen BEGIN=nlmConfigGlobalAgeOut::setValue
     //--AgentGen END
      return super.setValue(newValue);
    }

     //--AgentGen BEGIN=nlmConfigGlobalAgeOut::_METHODS
     //--AgentGen END

  }


  // Value Validators

  /**
   * The <code>NlmConfigLogFilterNameValidator</code> implements the value
   * validation for <code>NlmConfigLogFilterName</code>.
   */
  static class NlmConfigLogFilterNameValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=nlmConfigLogFilterName::validate
     //--AgentGen END
    }
  }

  // Rows and Factories

  public class NlmConfigLogEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=nlmConfigLogEntry::RowMembers
     //--AgentGen END

    public NlmConfigLogEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=nlmConfigLogEntry::RowConstructor
     //--AgentGen END
    }

    public OctetString getNlmConfigLogFilterName() {
     //--AgentGen BEGIN=nlmConfigLogEntry::getNlmConfigLogFilterName
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmConfigLogFilterName);
    }

    public void setNlmConfigLogFilterName(OctetString newValue) {
     //--AgentGen BEGIN=nlmConfigLogEntry::setNlmConfigLogFilterName
     //--AgentGen END
      super.setValue(idxNlmConfigLogFilterName, newValue);
    }

    public UnsignedInteger32 getNlmConfigLogEntryLimit() {
     //--AgentGen BEGIN=nlmConfigLogEntry::getNlmConfigLogEntryLimit
     //--AgentGen END
      return (UnsignedInteger32) super.getValue(idxNlmConfigLogEntryLimit);
    }

    public void setNlmConfigLogEntryLimit(UnsignedInteger32 newValue) {
     //--AgentGen BEGIN=nlmConfigLogEntry::setNlmConfigLogEntryLimit
     //--AgentGen END
      super.setValue(idxNlmConfigLogEntryLimit, newValue);
    }

    public Integer32 getNlmConfigLogAdminStatus() {
     //--AgentGen BEGIN=nlmConfigLogEntry::getNlmConfigLogAdminStatus
     //--AgentGen END
      return (Integer32) super.getValue(idxNlmConfigLogAdminStatus);
    }

    public void setNlmConfigLogAdminStatus(Integer32 newValue) {
     //--AgentGen BEGIN=nlmConfigLogEntry::setNlmConfigLogAdminStatus
     //--AgentGen END
      super.setValue(idxNlmConfigLogAdminStatus, newValue);
    }

    public Integer32 getNlmConfigLogOperStatus() {
     //--AgentGen BEGIN=nlmConfigLogEntry::getNlmConfigLogOperStatus
     if (NlmConfigLogAdminStatusEnum.enabled ==
         getNlmConfigLogAdminStatus().getValue()) {
       OctetString logFilter = getNlmConfigLogFilterName();
       if ((logFilter != null) && (logFilter.length() > 0) &&
           (snmpNotificationMIB.hasFilter(getNlmConfigLogFilterName()))) {
         values[idxNlmConfigLogOperStatus] =
             new Integer32(NlmConfigLogOperStatusEnum.operational);
       }
       else {
         values[idxNlmConfigLogOperStatus] =
             new Integer32(NlmConfigLogOperStatusEnum.noFilter);
       }
     }
     else {
       values[idxNlmConfigLogOperStatus] =
           new Integer32(NlmConfigLogOperStatusEnum.disabled);
     }
     //--AgentGen END
      return (Integer32) super.getValue(idxNlmConfigLogOperStatus);
    }

    public void setNlmConfigLogOperStatus(Integer32 newValue) {
     //--AgentGen BEGIN=nlmConfigLogEntry::setNlmConfigLogOperStatus
     //--AgentGen END
      super.setValue(idxNlmConfigLogOperStatus, newValue);
    }

    public Integer32 getNlmConfigLogStorageType() {
     //--AgentGen BEGIN=nlmConfigLogEntry::getNlmConfigLogStorageType
     //--AgentGen END
      return (Integer32) super.getValue(idxNlmConfigLogStorageType);
    }

    public void setNlmConfigLogStorageType(Integer32 newValue) {
     //--AgentGen BEGIN=nlmConfigLogEntry::setNlmConfigLogStorageType
     //--AgentGen END
      super.setValue(idxNlmConfigLogStorageType, newValue);
    }

    public Integer32 getNlmConfigLogEntryStatus() {
     //--AgentGen BEGIN=nlmConfigLogEntry::getNlmConfigLogEntryStatus
     //--AgentGen END
      return (Integer32) super.getValue(idxNlmConfigLogEntryStatus);
    }

    public void setNlmConfigLogEntryStatus(Integer32 newValue) {
     //--AgentGen BEGIN=nlmConfigLogEntry::setNlmConfigLogEntryStatus
     //--AgentGen END
      super.setValue(idxNlmConfigLogEntryStatus, newValue);
    }

    public Variable getValue(int column) {
     //--AgentGen BEGIN=nlmConfigLogEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxNlmConfigLogFilterName:
        	return getNlmConfigLogFilterName();
        case idxNlmConfigLogEntryLimit:
        	return getNlmConfigLogEntryLimit();
        case idxNlmConfigLogAdminStatus:
        	return getNlmConfigLogAdminStatus();
        case idxNlmConfigLogOperStatus:
        	return getNlmConfigLogOperStatus();
        case idxNlmConfigLogStorageType:
        	return getNlmConfigLogStorageType();
        case idxNlmConfigLogEntryStatus:
        	return getNlmConfigLogEntryStatus();
        default:
          return super.getValue(column);
      }
    }

    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=nlmConfigLogEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxNlmConfigLogFilterName:
        	setNlmConfigLogFilterName((OctetString)value);
        	break;
        case idxNlmConfigLogEntryLimit:
        	setNlmConfigLogEntryLimit((UnsignedInteger32)value);
        	break;
        case idxNlmConfigLogAdminStatus:
        	setNlmConfigLogAdminStatus((Integer32)value);
        	break;
        case idxNlmConfigLogOperStatus:
        	setNlmConfigLogOperStatus((Integer32)value);
        	break;
        case idxNlmConfigLogStorageType:
        	setNlmConfigLogStorageType((Integer32)value);
        	break;
        case idxNlmConfigLogEntryStatus:
        	setNlmConfigLogEntryStatus((Integer32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=nlmConfigLogEntry::Row
     public void setViewName(OctetString viewName) {
       values[idxNlmConfigLogViewName] = viewName;
     }

     public OctetString getViewName() {
       return (OctetString) values[idxNlmConfigLogViewName];
     }
     //--AgentGen END
  }

  class NlmConfigLogEntryRowFactory
        implements MOTableRowFactory<NlmConfigLogEntryRow>
  {
    public synchronized NlmConfigLogEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      NlmConfigLogEntryRow row =
        new NlmConfigLogEntryRow(index, values);
     //--AgentGen BEGIN=nlmConfigLogEntry::createRow
      if (values.length < nlmConfigLogEntry.getColumnCount()+1) {
        Variable[] extended = new Variable[nlmConfigLogEntry.getColumnCount()+1];
        System.arraycopy(values, 0, extended, 0, values.length);
        extended[values.length] = new OctetString();
        row = new NlmConfigLogEntryRow(index, extended);
      }
     //--AgentGen END
      return row;
    }

    public synchronized void freeRow(NlmConfigLogEntryRow row) {
     //--AgentGen BEGIN=nlmConfigLogEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=nlmConfigLogEntry::RowFactory
     //--AgentGen END
  }

  public class NlmStatsLogEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=nlmStatsLogEntry::RowMembers
     //--AgentGen END

    public NlmStatsLogEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=nlmStatsLogEntry::RowConstructor
     //--AgentGen END
    }

    public Counter32 getNlmStatsLogNotificationsLogged() {
     //--AgentGen BEGIN=nlmStatsLogEntry::getNlmStatsLogNotificationsLogged
     //--AgentGen END
      return (Counter32) super.getValue(idxNlmStatsLogNotificationsLogged);
    }

    public void setNlmStatsLogNotificationsLogged(Counter32 newValue) {
     //--AgentGen BEGIN=nlmStatsLogEntry::setNlmStatsLogNotificationsLogged
     //--AgentGen END
      super.setValue(idxNlmStatsLogNotificationsLogged, newValue);
    }

    public Counter32 getNlmStatsLogNotificationsBumped() {
     //--AgentGen BEGIN=nlmStatsLogEntry::getNlmStatsLogNotificationsBumped
     //--AgentGen END
      return (Counter32) super.getValue(idxNlmStatsLogNotificationsBumped);
    }

    public void setNlmStatsLogNotificationsBumped(Counter32 newValue) {
     //--AgentGen BEGIN=nlmStatsLogEntry::setNlmStatsLogNotificationsBumped
     //--AgentGen END
      super.setValue(idxNlmStatsLogNotificationsBumped, newValue);
    }

    public Variable getValue(int column) {
     //--AgentGen BEGIN=nlmStatsLogEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxNlmStatsLogNotificationsLogged:
        	return getNlmStatsLogNotificationsLogged();
        case idxNlmStatsLogNotificationsBumped:
        	return getNlmStatsLogNotificationsBumped();
        default:
          return super.getValue(column);
      }
    }

    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=nlmStatsLogEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxNlmStatsLogNotificationsLogged:
        	setNlmStatsLogNotificationsLogged((Counter32)value);
        	break;
        case idxNlmStatsLogNotificationsBumped:
        	setNlmStatsLogNotificationsBumped((Counter32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=nlmStatsLogEntry::Row
     //--AgentGen END
  }

  class NlmStatsLogEntryRowFactory
        implements MOTableRowFactory<NlmStatsLogEntryRow>
  {
    public synchronized NlmStatsLogEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      NlmStatsLogEntryRow row =
        new NlmStatsLogEntryRow(index, values);
     //--AgentGen BEGIN=nlmStatsLogEntry::createRow
      values[idxNlmStatsLogNotificationsLogged] = new Counter32(0);
      values[idxNlmStatsLogNotificationsBumped] = new Counter32(0);
     //--AgentGen END
      return row;
    }

    public synchronized void freeRow(NlmStatsLogEntryRow row) {
     //--AgentGen BEGIN=nlmStatsLogEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=nlmStatsLogEntry::RowFactory
     //--AgentGen END
  }

  public class NlmLogEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=nlmLogEntry::RowMembers
     //--AgentGen END

    public NlmLogEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=nlmLogEntry::RowConstructor
     //--AgentGen END
    }

    public TimeTicks getNlmLogTime() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogTime
     //--AgentGen END
      return (TimeTicks) super.getValue(idxNlmLogTime);
    }

    public void setNlmLogTime(TimeTicks newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogTime
     //--AgentGen END
      super.setValue(idxNlmLogTime, newValue);
    }

    public OctetString getNlmLogDateAndTime() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogDateAndTime
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmLogDateAndTime);
    }

    public void setNlmLogDateAndTime(OctetString newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogDateAndTime
     //--AgentGen END
      super.setValue(idxNlmLogDateAndTime, newValue);
    }

    public OctetString getNlmLogEngineID() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogEngineID
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmLogEngineID);
    }

    public void setNlmLogEngineID(OctetString newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogEngineID
     //--AgentGen END
      super.setValue(idxNlmLogEngineID, newValue);
    }

    public OctetString getNlmLogEngineTAddress() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogEngineTAddress
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmLogEngineTAddress);
    }

    public void setNlmLogEngineTAddress(OctetString newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogEngineTAddress
     //--AgentGen END
      super.setValue(idxNlmLogEngineTAddress, newValue);
    }

    public OID getNlmLogEngineTDomain() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogEngineTDomain
     //--AgentGen END
      return (OID) super.getValue(idxNlmLogEngineTDomain);
    }

    public void setNlmLogEngineTDomain(OID newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogEngineTDomain
     //--AgentGen END
      super.setValue(idxNlmLogEngineTDomain, newValue);
    }

    public OctetString getNlmLogContextEngineID() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogContextEngineID
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmLogContextEngineID);
    }

    public void setNlmLogContextEngineID(OctetString newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogContextEngineID
     //--AgentGen END
      super.setValue(idxNlmLogContextEngineID, newValue);
    }

    public OctetString getNlmLogContextName() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogContextName
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmLogContextName);
    }

    public void setNlmLogContextName(OctetString newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogContextName
     //--AgentGen END
      super.setValue(idxNlmLogContextName, newValue);
    }

    public OID getNlmLogNotificationID() {
     //--AgentGen BEGIN=nlmLogEntry::getNlmLogNotificationID
     //--AgentGen END
      return (OID) super.getValue(idxNlmLogNotificationID);
    }

    public void setNlmLogNotificationID(OID newValue) {
     //--AgentGen BEGIN=nlmLogEntry::setNlmLogNotificationID
     //--AgentGen END
      super.setValue(idxNlmLogNotificationID, newValue);
    }

    public Variable getValue(int column) {
     //--AgentGen BEGIN=nlmLogEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxNlmLogTime:
        	return getNlmLogTime();
        case idxNlmLogDateAndTime:
        	return getNlmLogDateAndTime();
        case idxNlmLogEngineID:
        	return getNlmLogEngineID();
        case idxNlmLogEngineTAddress:
        	return getNlmLogEngineTAddress();
        case idxNlmLogEngineTDomain:
        	return getNlmLogEngineTDomain();
        case idxNlmLogContextEngineID:
        	return getNlmLogContextEngineID();
        case idxNlmLogContextName:
        	return getNlmLogContextName();
        case idxNlmLogNotificationID:
        	return getNlmLogNotificationID();
        default:
          return super.getValue(column);
      }
    }

    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=nlmLogEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxNlmLogTime:
        	setNlmLogTime((TimeTicks)value);
        	break;
        case idxNlmLogDateAndTime:
        	setNlmLogDateAndTime((OctetString)value);
        	break;
        case idxNlmLogEngineID:
        	setNlmLogEngineID((OctetString)value);
        	break;
        case idxNlmLogEngineTAddress:
        	setNlmLogEngineTAddress((OctetString)value);
        	break;
        case idxNlmLogEngineTDomain:
        	setNlmLogEngineTDomain((OID)value);
        	break;
        case idxNlmLogContextEngineID:
        	setNlmLogContextEngineID((OctetString)value);
        	break;
        case idxNlmLogContextName:
        	setNlmLogContextName((OctetString)value);
        	break;
        case idxNlmLogNotificationID:
        	setNlmLogNotificationID((OID)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=nlmLogEntry::Row
     //--AgentGen END
  }

  class NlmLogEntryRowFactory
        implements MOTableRowFactory<NlmLogEntryRow>
  {
    public synchronized NlmLogEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      NlmLogEntryRow row =
        new NlmLogEntryRow(index, values);
     //--AgentGen BEGIN=nlmLogEntry::createRow
     //--AgentGen END
      return row;
    }

    public synchronized void freeRow(NlmLogEntryRow row) {
     //--AgentGen BEGIN=nlmLogEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=nlmLogEntry::RowFactory
     //--AgentGen END
  }

  public class NlmLogVariableEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=nlmLogVariableEntry::RowMembers
     //--AgentGen END

    public NlmLogVariableEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=nlmLogVariableEntry::RowConstructor
     //--AgentGen END
    }

    public OID getNlmLogVariableID() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableID
     //--AgentGen END
      return (OID) super.getValue(idxNlmLogVariableID);
    }

    public void setNlmLogVariableID(OID newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableID
     //--AgentGen END
      super.setValue(idxNlmLogVariableID, newValue);
    }

    public Integer32 getNlmLogVariableValueType() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableValueType
     //--AgentGen END
      return (Integer32) super.getValue(idxNlmLogVariableValueType);
    }

    public void setNlmLogVariableValueType(Integer32 newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableValueType
     //--AgentGen END
      super.setValue(idxNlmLogVariableValueType, newValue);
    }

    public Counter32 getNlmLogVariableCounter32Val() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableCounter32Val
     //--AgentGen END
      return (Counter32) super.getValue(idxNlmLogVariableCounter32Val);
    }

    public void setNlmLogVariableCounter32Val(Counter32 newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableCounter32Val
     //--AgentGen END
      super.setValue(idxNlmLogVariableCounter32Val, newValue);
    }

    public UnsignedInteger32 getNlmLogVariableUnsigned32Val() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableUnsigned32Val
     //--AgentGen END
      return (UnsignedInteger32) super.getValue(idxNlmLogVariableUnsigned32Val);
    }

    public void setNlmLogVariableUnsigned32Val(UnsignedInteger32 newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableUnsigned32Val
     //--AgentGen END
      super.setValue(idxNlmLogVariableUnsigned32Val, newValue);
    }

    public TimeTicks getNlmLogVariableTimeTicksVal() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableTimeTicksVal
     //--AgentGen END
      return (TimeTicks) super.getValue(idxNlmLogVariableTimeTicksVal);
    }

    public void setNlmLogVariableTimeTicksVal(TimeTicks newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableTimeTicksVal
     //--AgentGen END
      super.setValue(idxNlmLogVariableTimeTicksVal, newValue);
    }

    public Integer32 getNlmLogVariableInteger32Val() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableInteger32Val
     //--AgentGen END
      return (Integer32) super.getValue(idxNlmLogVariableInteger32Val);
    }

    public void setNlmLogVariableInteger32Val(Integer32 newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableInteger32Val
     //--AgentGen END
      super.setValue(idxNlmLogVariableInteger32Val, newValue);
    }

    public OctetString getNlmLogVariableOctetStringVal() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableOctetStringVal
     //--AgentGen END
      return (OctetString) super.getValue(idxNlmLogVariableOctetStringVal);
    }

    public void setNlmLogVariableOctetStringVal(OctetString newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableOctetStringVal
     //--AgentGen END
      super.setValue(idxNlmLogVariableOctetStringVal, newValue);
    }

    public IpAddress getNlmLogVariableIpAddressVal() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableIpAddressVal
     //--AgentGen END
      return (IpAddress) super.getValue(idxNlmLogVariableIpAddressVal);
    }

    public void setNlmLogVariableIpAddressVal(IpAddress newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableIpAddressVal
     //--AgentGen END
      super.setValue(idxNlmLogVariableIpAddressVal, newValue);
    }

    public OID getNlmLogVariableOidVal() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableOidVal
     //--AgentGen END
      return (OID) super.getValue(idxNlmLogVariableOidVal);
    }

    public void setNlmLogVariableOidVal(OID newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableOidVal
     //--AgentGen END
      super.setValue(idxNlmLogVariableOidVal, newValue);
    }

    public Counter64 getNlmLogVariableCounter64Val() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableCounter64Val
     //--AgentGen END
      return (Counter64) super.getValue(idxNlmLogVariableCounter64Val);
    }

    public void setNlmLogVariableCounter64Val(Counter64 newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableCounter64Val
     //--AgentGen END
      super.setValue(idxNlmLogVariableCounter64Val, newValue);
    }

    public Opaque getNlmLogVariableOpaqueVal() {
     //--AgentGen BEGIN=nlmLogVariableEntry::getNlmLogVariableOpaqueVal
     //--AgentGen END
      return (Opaque) super.getValue(idxNlmLogVariableOpaqueVal);
    }

    public void setNlmLogVariableOpaqueVal(Opaque newValue) {
     //--AgentGen BEGIN=nlmLogVariableEntry::setNlmLogVariableOpaqueVal
     //--AgentGen END
      super.setValue(idxNlmLogVariableOpaqueVal, newValue);
    }

    public Variable getValue(int column) {
     //--AgentGen BEGIN=nlmLogVariableEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxNlmLogVariableID:
        	return getNlmLogVariableID();
        case idxNlmLogVariableValueType:
        	return getNlmLogVariableValueType();
        case idxNlmLogVariableCounter32Val:
        	return getNlmLogVariableCounter32Val();
        case idxNlmLogVariableUnsigned32Val:
        	return getNlmLogVariableUnsigned32Val();
        case idxNlmLogVariableTimeTicksVal:
        	return getNlmLogVariableTimeTicksVal();
        case idxNlmLogVariableInteger32Val:
        	return getNlmLogVariableInteger32Val();
        case idxNlmLogVariableOctetStringVal:
        	return getNlmLogVariableOctetStringVal();
        case idxNlmLogVariableIpAddressVal:
        	return getNlmLogVariableIpAddressVal();
        case idxNlmLogVariableOidVal:
        	return getNlmLogVariableOidVal();
        case idxNlmLogVariableCounter64Val:
        	return getNlmLogVariableCounter64Val();
        case idxNlmLogVariableOpaqueVal:
        	return getNlmLogVariableOpaqueVal();
        default:
          return super.getValue(column);
      }
    }

    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=nlmLogVariableEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxNlmLogVariableID:
        	setNlmLogVariableID((OID)value);
        	break;
        case idxNlmLogVariableValueType:
        	setNlmLogVariableValueType((Integer32)value);
        	break;
        case idxNlmLogVariableCounter32Val:
        	setNlmLogVariableCounter32Val((Counter32)value);
        	break;
        case idxNlmLogVariableUnsigned32Val:
        	setNlmLogVariableUnsigned32Val((UnsignedInteger32)value);
        	break;
        case idxNlmLogVariableTimeTicksVal:
        	setNlmLogVariableTimeTicksVal((TimeTicks)value);
        	break;
        case idxNlmLogVariableInteger32Val:
        	setNlmLogVariableInteger32Val((Integer32)value);
        	break;
        case idxNlmLogVariableOctetStringVal:
        	setNlmLogVariableOctetStringVal((OctetString)value);
        	break;
        case idxNlmLogVariableIpAddressVal:
        	setNlmLogVariableIpAddressVal((IpAddress)value);
        	break;
        case idxNlmLogVariableOidVal:
        	setNlmLogVariableOidVal((OID)value);
        	break;
        case idxNlmLogVariableCounter64Val:
        	setNlmLogVariableCounter64Val((Counter64)value);
        	break;
        case idxNlmLogVariableOpaqueVal:
        	setNlmLogVariableOpaqueVal((Opaque)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=nlmLogVariableEntry::Row
     //--AgentGen END
  }

  class NlmLogVariableEntryRowFactory
        implements MOTableRowFactory<NlmLogVariableEntryRow>
  {
    public synchronized NlmLogVariableEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      NlmLogVariableEntryRow row =
        new NlmLogVariableEntryRow(index, values);
     //--AgentGen BEGIN=nlmLogVariableEntry::createRow
     //--AgentGen END
      return row;
    }

    public synchronized void freeRow(NlmLogVariableEntryRow row) {
     //--AgentGen BEGIN=nlmLogVariableEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=nlmLogVariableEntry::RowFactory
     //--AgentGen END
  }


//--AgentGen BEGIN=_METHODS

  /**
   * Sets the log mode of the events. Possible values are defined by
   * #Snmp4jNotificationLogModeEnum.
   * @param loggerMode
   *    1 to set log mode to log fired notification events only,
   *    2 to set log mode to log sent notifications only.
   * @since 1.4.2
   */
  public void setLogMode(int loggerMode) {
    snmp4jNotificationLogMode.setValue(new Integer32(loggerMode));
  }

  /**
   * Returns the current log mode as defined by #Snmp4jNotificationLogModeEnum.
   * @return
   *    1 if fired notification events are logged only,
   *    2 if sent notifications are logged only.
   * @since 1.4.2
   */
  public int getLogMode() {
    return snmp4jNotificationLogMode.getValue().toInt();
  }

  public void notificationLogEvent(NotificationLogEvent notificationLogEvent) {
    if (notificationLogEvent.isSubEvent() ==
        (getLogMode() == Snmp4jNotificationLogModeEnum.sent)) {
      Integer32 enabledStatus = new Integer32(NlmConfigLogAdminStatusEnum.
                                              enabled);
      synchronized (nlmConfigLogEntryModel) {
        for (Iterator<NlmConfigLogEntryRow> it = nlmConfigLogEntryModel.iterator(); it.hasNext(); ) {
          NlmConfigLogEntryRow row = it.next();
          if (enabledStatus.equals(row.getNlmConfigLogAdminStatus())) {
            OctetString profileName = new OctetString();
            profileName.fromSubIndex(row.getIndex(), false);
            OctetString filterName = row.getNlmConfigLogFilterName();
            if (filterName != null) {
              if ((profileName.length() == 0) ||
                     (checkAccess(notificationLogEvent.getNotificationID(),
                                  notificationLogEvent.getVariables(), row))) {
                if ((snmpNotificationMIB.passesFilter(
                    filterName,
                    notificationLogEvent.getNotificationID(),
                    notificationLogEvent.getVariables()))) {
                  if (nextLogIndex > 4294967295L) {
                    nextLogIndex = 1;
                  }
                  else {
                    nextLogIndex++;
                  }
                  while ((!nlmLogEntryModel.isEmpty()) &&
                      (nlmLogEntryModel.lastIndex().last() >= nextLogIndex)) {
                    nextLogIndex++;
                  }
                  OID logIndex = profileName.toSubIndex(false);
                  logIndex.append((int) nextLogIndex);
                  OctetString addrString = new OctetString();
                  OID domainOID = new OID();
                  if (notificationLogEvent.getOriginatorTarget() != null) {
                    Address addr =
                        notificationLogEvent.getOriginatorTarget().getAddress();
                    addrString = addressFactory.getAddress(addr);
                    domainOID = addressFactory.getTransportDomain(addr)[0];
                  }
                  NlmLogEntryRow logRow =
                      nlmLogEntry.createRow(logIndex,
                          new Variable[]{
                              SNMPv2MIB.getSysUpTime(null).get(),
                              DateAndTime.makeDateAndTime(new
                                  GregorianCalendar()),
                              notificationLogEvent.
                                  getOriginatorEngineID(),
                              addrString,
                              domainOID,
                              notificationLogEvent.getContextEngineID(),
                              notificationLogEvent.getContext(),
                              notificationLogEvent.getNotificationID()
                          }
                      );
                  if (nlmLogEntry.addRow(logRow)) {
                    nlmLogEntries.add(logRow.getIndex());
                  }
                  for (int i = 0; i < notificationLogEvent.getVariables().length;
                       i++) {
                    addVariable(logIndex, i + 1,
                        notificationLogEvent.getVariables()[i]);
                  }
                  synchronized (nlmStatsLogEntryModel) {
                    NlmStatsLogEntryRow statsRow = (NlmStatsLogEntryRow)
                        nlmStatsLogEntryModel.getRow(profileName.toSubIndex(false));
                    if (statsRow != null) {
                      statsRow.getNlmStatsLogNotificationsLogged().increment();
                    }
                  }
                  ((Counter32) nlmStatsGlobalNotificationsLogged.getValue()).
                      increment();
                  checkLimits(profileName);
                }
              }
            }
          }
        }
      }
    }
  }

  private void checkLimits(OctetString profileName) {
    long globalLimit =
        ((UnsignedInteger32)nlmConfigGlobalEntryLimit.getValue()).getValue();
    if (globalLimit > 0) {
      deleteLogRows(globalLimit, null);
    }
    long ageOut =
        ((UnsignedInteger32)nlmConfigGlobalAgeOut.getValue()).getValue();
    if (ageOut > 0) {
      List<OID> victims = new ArrayList<OID>();
      synchronized (nlmLogEntryModel) {
        TimeTicks uptime = SNMPv2MIB.getSysUpTime(null).get();
        for (Iterator<NlmLogEntryRow> it = nlmLogEntryModel.iterator(); it.hasNext(); ) {
          NlmLogEntryRow row = it.next();
          if (uptime.getValue() - row.getNlmLogTime().getValue() >
              ageOut * 6000) {
            NlmStatsLogEntryRow statsRow = nlmStatsLogEntryModel.getRow(row.getIndex().trim());
            if (statsRow != null) {
              statsRow.getNlmStatsLogNotificationsBumped().increment();
              victims.add(statsRow.getIndex());
            }
          }
        }
      }
      for (OID index : victims) {
        nlmLogEntries.remove(index);
        nlmLogEntry.removeRow(index);
        ((DefaultMOMutableTableModel) nlmLogVariableEntryModel).
            removeRows(index, index.nextPeer());
        ((Counter32) nlmStatsGlobalNotificationsBumped.getValue()).increment();
      }
    }
    if (profileName != null) {
      NlmConfigLogEntryRow profile = nlmConfigLogEntry.getModel().getRow(profileName.toSubIndex(false));
      if (profile != null) {
        UnsignedInteger32 limit = profile.getNlmConfigLogEntryLimit();
        if (limit != null) {
          long l = limit.getValue();
          if (l > 0) {
            deleteLogRows(l, profileName);
          }
        }
      }
    }
  }

  private void deleteLogRows(long limit, OctetString profileName) {
    long delta = nlmLogEntry.getModel().getRowCount() - limit;
    if (delta > 0) {
      synchronized (nlmLogEntries) {
        for (int i=0; (i<delta) && (nlmLogEntries.size()>0); i++) {
          OID firstIndex = nlmLogEntries.remove(0);
          if (firstIndex != null) {
            nlmLogEntry.removeRow(firstIndex);
            ((DefaultMOMutableTableModel)
             nlmLogVariableEntryModel).removeRows(firstIndex, firstIndex.nextPeer());
            NlmStatsLogEntryRow statsRow = (NlmStatsLogEntryRow)
                nlmStatsLogEntryModel.getRow(firstIndex.trim());
            if (statsRow != null) {
              statsRow.getNlmStatsLogNotificationsBumped().increment();
            }
            ((Counter32)nlmStatsGlobalNotificationsBumped.getValue()).increment();
            if (profileName != null) {
              NlmStatsLogEntryRow profile = (NlmStatsLogEntryRow)
                  nlmStatsLogEntry.getModel().getRow(profileName.toSubIndex(false));
              if (profile != null) {
                profile.getNlmStatsLogNotificationsBumped().increment();
              }
            }
          }
        }
      }
    }
  }

  private boolean addVariable(OID logIndex, int varIndex, VariableBinding vb) {
    OID vIndex = new OID(logIndex);
    vIndex.append(varIndex);
    Variable[] variables =  new Variable[] {
        vb.getOid(),
        null, null, null, null, null, null, null, null, null, null
    };
    for (int i=0; i<VAR_SYNTAX_LIST.length; i++) {
      if (VAR_SYNTAX_LIST[i] == vb.getSyntax()) {
        switch(i+1) {
          // correct mixed up type values in NOTIFICATION-LOG-MIB:
          case NlmLogVariableValueTypeEnum.ipAddress :
            variables[1] = new Integer32(NlmLogVariableValueTypeEnum.octetString);
            break;
          // correct mixed up type values in NOTIFICATION-LOG-MIB:
          case NlmLogVariableValueTypeEnum.octetString :
            variables[1] = new Integer32(NlmLogVariableValueTypeEnum.ipAddress);
            break;
          // regular mapping:
          default:
            variables[1] = new Integer32(i+1);
        }
        variables[2+i] = vb.getVariable();
      }
    }
    NlmLogVariableEntryRow row = nlmLogVariableEntry.createRow(vIndex,variables);
    return nlmLogVariableEntry.addRow(row);
  }

  private boolean checkAccess(OID notifyID,
                              VariableBinding[] vbs, NlmConfigLogEntryRow row) {
    OctetString viewName = row.getViewName();
    // access is given to all null length view names (which indicates null named
    // profiles.
    if (viewName.length() == 0) {
      return true;
    }
    boolean accessAllowed = true;
    if (vacm.isAccessAllowed(row.getViewName(), notifyID) != VACM.VACM_OK) {
      accessAllowed = false;
    }
    for (int i=0; (accessAllowed && (i<vbs.length)); i++) {
      if (vacm.isAccessAllowed(row.getViewName(), vbs[i].getOid()) != VACM.VACM_OK) {
        accessAllowed = false;
      }
    }
    if (!accessAllowed) {
      LOGGER.info("Notification not logged because view '"+row.getViewName()+
                  "' of log entry '"+row.getIndex()+"' has not access");
    }
    return accessAllowed;
  }

  public void rowStatusChanged(RowStatusEvent event) {
    Request r = event.getRequest().getRequest();
    if (event.isRowActivated() && event.isDeniable()) {
      OctetString viewName =
          vacm.getViewName(r.getContext(), r.getSecurityName(),
                           r.getSecurityModel(), r.getSecurityLevel(),
                           VACM.VIEW_NOTIFY);
      if (viewName == null) {
        event.setDenyReason(PDU.authorizationError);
        return;
      }
      event.getRequest().setUserObject(viewName);
    }
    else if (event.isRowActivated()) {
      ((NlmConfigLogEntryRow)event.getRow()).setViewName(
          (OctetString)event.getRequest().getUserObject());
    }
  }

//--AgentGen END

  // Textual Definitions of MIB module NotificationLogMib
  protected void addTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  public void addImportedTCsToFactory(MOFactory moFactory) {
   moFactory.addTextualConvention(new SnmpAdminString());
   moFactory.addTextualConvention(new TAddress());
   moFactory.addTextualConvention(new TDomain());
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

  // Textual Convention TAddress from MIB module SNMPv2-TC

  public class TAddress implements TextualConvention {

    public TAddress() {
    }

    public String getModuleName() {
      return TC_MODULE_SNMPV2_TC;
    }

    public String getName() {
      return TC_TADDRESS;
    }

    public Variable createInitialValue() {
    	Variable v = new OctetString();
      if (v instanceof AssignableFromLong) {
      	((AssignableFromLong)v).setValue(1L);
      }
    	// further modify value to comply with TC constraints here:
     //--AgentGen BEGIN=TAddress::createInitialValue
     //--AgentGen END
	    return v;
    }

    public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
      MOScalar scalar = moFactory.createScalar(oid, access, value);
      ValueConstraint vc = new ConstraintsImpl();
      ((ConstraintsImpl)vc).add(new Constraint(1L, 255L));
      scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));
     //--AgentGen BEGIN=TAddress::createScalar
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
        ((ConstraintsImpl)vc).add(new Constraint(1L, 255L));
        mcol.addMOValueValidationListener(new ValueConstraintValidator(vc));
      }
     //--AgentGen BEGIN=TAddress::createColumn
     //--AgentGen END
      return col;
    }
  }

  // Textual Convention TDomain from MIB module SNMPv2-TC

  public class TDomain implements TextualConvention {

    public TDomain() {
    }

    public String getModuleName() {
      return TC_MODULE_SNMPV2_TC;
    }

    public String getName() {
      return TC_TDOMAIN;
    }

    public Variable createInitialValue() {
    	Variable v = new OID();
    	// further modify value to comply with TC constraints here:
     //--AgentGen BEGIN=TDomain::createInitialValue
     //--AgentGen END
	    return v;
    }

    public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
      MOScalar scalar = moFactory.createScalar(oid, access, value);
     //--AgentGen BEGIN=TDomain::createScalar
     //--AgentGen END
      return scalar;
    }

    public MOColumn createColumn(int columnID, int syntax, MOAccess access,
                                 Variable defaultValue, boolean mutableInService) {
      MOColumn col = moFactory.createColumn(columnID, syntax, access,
                                            defaultValue, mutableInService);
     //--AgentGen BEGIN=TDomain::createColumn
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
  /**
   * The <code>Snmp4jNotificationLogModeValidator</code> implements the value
   * validation for <code>Snmp4jNotificationLogMode</code>.
   */
  static class Snmp4jNotificationLogModeValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jNotificationLogMode::validate

     //--AgentGen END
    }
  }
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


