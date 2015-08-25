/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpTlsTmMib.java  
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

import org.snmp4j.event.CounterListener;
import org.snmp4j.mp.DefaultCounterListener;
import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.mo.snmp.tc.*;
import org.snmp4j.transport.TLSTM;
import org.snmp4j.transport.tls.TlsTmSecurityCallback;

import javax.security.auth.x500.X500Principal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class SnmpTlsTmMib 
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup 
//--AgentGen BEGIN=_IMPLEMENTS
, TlsTmSecurityCallback<X509Certificate>
//--AgentGen END
{

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(SnmpTlsTmMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private static MOFactory moFactory =
    DefaultMOFactory.getInstance();

  // Constants 

  /**
   * OID of this MIB module for usage which can be 
   * used for its identification.
   */
  public static final OID oidSnmpTlsTmMib =
    new OID(new int[] { 1,3,6,1,2,1,198 });

  public static final OID oidSnmpTlstmCertSpecified =
    new OID(new int[] { 1,3,6,1,2,1,198,1,1,1 });
  public static final OID oidSnmpTlstmCertSANRFC822Name =
    new OID(new int[] { 1,3,6,1,2,1,198,1,1,2 });
  public static final OID oidSnmpTlstmCertSANDNSName =
    new OID(new int[] { 1,3,6,1,2,1,198,1,1,3 });
  public static final OID oidSnmpTlstmCertSANIpAddress =
    new OID(new int[] { 1,3,6,1,2,1,198,1,1,4 });
  public static final OID oidSnmpTlstmCertSANAny =
    new OID(new int[] { 1,3,6,1,2,1,198,1,1,5 });
  public static final OID oidSnmpTlstmCertCommonName =
    new OID(new int[] { 1,3,6,1,2,1,198,1,1,6 });
  public static final OID oidSnmpTLSTCPDomain =
    new OID(new int[] { 1,3,6,1,6,1,8 });
  public static final OID oidSnmpDTLSUDPDomain =
    new OID(new int[] { 1,3,6,1,6,1,9 });

  public static final OID oidSnmpTlstmSessionOpens = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,1,0 });
  public static final OID oidSnmpTlstmSessionClientCloses = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,2,0 });
  public static final OID oidSnmpTlstmSessionOpenErrors = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,3,0 });
  public static final OID oidSnmpTlstmSessionAccepts = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,4,0 });
  public static final OID oidSnmpTlstmSessionServerCloses = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,5,0 });
  public static final OID oidSnmpTlstmSessionNoSessions = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,6,0 });
  public static final OID oidSnmpTlstmSessionInvalidClientCertificates = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,7,0 });
  public static final OID oidSnmpTlstmSessionUnknownServerCertificate = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,8,0 });
  public static final OID oidSnmpTlstmSessionInvalidServerCertificates = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,9,0 });
  public static final OID oidSnmpTlstmSessionInvalidCaches = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,10,0 });
  public static final OID oidSnmpTlstmCertToTSNCount = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,1,0 });
  public static final OID oidSnmpTlstmCertToTSNTableLastChanged = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,2,0 });
  public static final OID oidSnmpTlstmParamsCount = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,4,0 });
  public static final OID oidSnmpTlstmParamsTableLastChanged = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,5,0 });
  public static final OID oidSnmpTlstmAddrCount = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,7,0 });
  public static final OID oidSnmpTlstmAddrTableLastChanged = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,8,0 });

  public static final OID oidSnmpTlstmServerCertificateUnknown =
    new OID(new int[] { 1,3,6,1,2,1,198,0,1 });   
  public static final OID oidTrapVarSnmpTlstmSessionUnknownServerCertificate =
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,8 });

  public static final OID oidSnmpTlstmServerInvalidCertificate =
    new OID(new int[] { 1,3,6,1,2,1,198,0,2 });   
  public static final OID oidTrapVarSnmpTlstmAddrServerFingerprint =
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,9,1,1 });
  public static final OID oidTrapVarSnmpTlstmSessionInvalidServerCertificates =
    new OID(new int[] { 1,3,6,1,2,1,198,2,1,9 });


  // Enumerations




  // TextualConventions
  private static final String TC_MODULE_SNMP_TLS_TM_MIB = "SNMP-TLS-TM-MIB";
  private static final String TC_MODULE_SNMP_FRAMEWORK_MIB = "SNMP-FRAMEWORK-MIB";
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_SNMPADMINSTRING = "SnmpAdminString";
  private static final String TC_TIMESTAMP = "TimeStamp";
  private static final String TC_AUTONOMOUSTYPE = "AutonomousType";
  private static final String TC_STORAGETYPE = "StorageType";
  private static final String TC_ROWSTATUS = "RowStatus";
  private static final String TC_SNMPTLSFINGERPRINT = "SnmpTLSFingerprint";

  // Scalars
  private MOScalar<Counter32> snmpTlstmSessionOpens;
  private MOScalar<Counter32> snmpTlstmSessionClientCloses;
  private MOScalar<Counter32> snmpTlstmSessionOpenErrors;
  private MOScalar<Counter32> snmpTlstmSessionAccepts;
  private MOScalar<Counter32> snmpTlstmSessionServerCloses;
  private MOScalar<Counter32> snmpTlstmSessionNoSessions;
  private MOScalar<Counter32> snmpTlstmSessionInvalidClientCertificates;
  private MOScalar<Counter32> snmpTlstmSessionUnknownServerCertificate;
  private MOScalar<Counter32> snmpTlstmSessionInvalidServerCertificates;
  private MOScalar<Counter32> snmpTlstmSessionInvalidCaches;
  private MOScalar<Gauge32> snmpTlstmCertToTSNCount;
  private MOScalar<TimeTicks> snmpTlstmCertToTSNTableLastChanged;
  private MOScalar<Gauge32> snmpTlstmParamsCount;
  private MOScalar<TimeTicks> snmpTlstmParamsTableLastChanged;
  private MOScalar<Gauge32> snmpTlstmAddrCount;
  private MOScalar<TimeTicks> snmpTlstmAddrTableLastChanged;

  // Tables
  public static final OID oidSnmpTlstmCertToTSNEntry = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,3,1 });

  // Index OID definitions
  public static final OID oidSnmpTlstmCertToTSNID =
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,3,1,1 });

  // Column TC definitions for snmpTlstmCertToTSNEntry:
  public static final String tcModuleSnmpTlsTmMib = "SNMP-TLS-TM-MIB";
  public static final String tcDefSnmpTLSFingerprint = "SnmpTLSFingerprint";
  public static final String tcModuleSNMPv2Tc = "SNMPv2-TC";
  public static final String tcDefAutonomousType = "AutonomousType";
  public static final String tcDefStorageType = "StorageType";
  public static final String tcDefRowStatus = "RowStatus";
    
  // Column sub-identifer definitions for snmpTlstmCertToTSNEntry:
  public static final int colSnmpTlstmCertToTSNFingerprint = 2;
  public static final int colSnmpTlstmCertToTSNMapType = 3;
  public static final int colSnmpTlstmCertToTSNData = 4;
  public static final int colSnmpTlstmCertToTSNStorageType = 5;
  public static final int colSnmpTlstmCertToTSNRowStatus = 6;

  // Column index definitions for snmpTlstmCertToTSNEntry:
  public static final int idxSnmpTlstmCertToTSNFingerprint = 0;
  public static final int idxSnmpTlstmCertToTSNMapType = 1;
  public static final int idxSnmpTlstmCertToTSNData = 2;
  public static final int idxSnmpTlstmCertToTSNStorageType = 3;
  public static final int idxSnmpTlstmCertToTSNRowStatus = 4;

  private MOTableSubIndex[] snmpTlstmCertToTSNEntryIndexes;
  private MOTableIndex snmpTlstmCertToTSNEntryIndex;
  
  private MOTable<SnmpTlstmCertToTSNEntryRow,MOColumn,DefaultMOMutableTableModel<SnmpTlstmCertToTSNEntryRow>>
      snmpTlstmCertToTSNEntry;
  private DefaultMOMutableTableModel<SnmpTlstmCertToTSNEntryRow> snmpTlstmCertToTSNEntryModel;
  public static final OID oidSnmpTlstmParamsEntry = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,6,1 });

  // Index OID definitions
  public static final OID oidSnmpTargetParamsName =
    new OID(new int[] { 1,3,6,1,6,3,12,1,3,1,1 });

  // Column TC definitions for snmpTlstmParamsEntry:
    
  // Column sub-identifer definitions for snmpTlstmParamsEntry:
  public static final int colSnmpTlstmParamsClientFingerprint = 1;
  public static final int colSnmpTlstmParamsStorageType = 2;
  public static final int colSnmpTlstmParamsRowStatus = 3;

  // Column index definitions for snmpTlstmParamsEntry:
  public static final int idxSnmpTlstmParamsClientFingerprint = 0;
  public static final int idxSnmpTlstmParamsStorageType = 1;
  public static final int idxSnmpTlstmParamsRowStatus = 2;

  private MOTableSubIndex[] snmpTlstmParamsEntryIndexes;
  private MOTableIndex snmpTlstmParamsEntryIndex;
  
  private MOTable<SnmpTlstmParamsEntryRow, MOColumn, MOTableModel<SnmpTlstmParamsEntryRow>>      snmpTlstmParamsEntry;
  private MOTableModel<SnmpTlstmParamsEntryRow> snmpTlstmParamsEntryModel;
  public static final OID oidSnmpTlstmAddrEntry = 
    new OID(new int[] { 1,3,6,1,2,1,198,2,2,1,9,1 });

  // Index OID definitions
  public static final OID oidSnmpTargetAddrName =
    new OID(new int[] { 1,3,6,1,6,3,12,1,2,1,1 });

  // Column TC definitions for snmpTlstmAddrEntry:
  public static final String tcModuleSnmpFrameworkMib = "SNMP-FRAMEWORK-MIB";
  public static final String tcDefSnmpAdminString = "SnmpAdminString";
    
  // Column sub-identifer definitions for snmpTlstmAddrEntry:
  public static final int colSnmpTlstmAddrServerFingerprint = 1;
  public static final int colSnmpTlstmAddrServerIdentity = 2;
  public static final int colSnmpTlstmAddrStorageType = 3;
  public static final int colSnmpTlstmAddrRowStatus = 4;

  // Column index definitions for snmpTlstmAddrEntry:
  public static final int idxSnmpTlstmAddrServerFingerprint = 0;
  public static final int idxSnmpTlstmAddrServerIdentity = 1;
  public static final int idxSnmpTlstmAddrStorageType = 2;
  public static final int idxSnmpTlstmAddrRowStatus = 3;

  private MOTableSubIndex[] snmpTlstmAddrEntryIndexes;
  private MOTableIndex snmpTlstmAddrEntryIndex;
  
  private MOTable<MOMutableRow2PC,MOColumn,MOTableModel<MOMutableRow2PC>>      snmpTlstmAddrEntry;
  private MOTableModel<MOMutableRow2PC> snmpTlstmAddrEntryModel;


//--AgentGen BEGIN=_MEMBERS
  private DefaultCounterListener counterListener = new DefaultCounterListener();
//--AgentGen END

  /**
   * Constructs a SnmpTlsTmMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling 
   * {@link #createMO(MOFactory moFactory)}. 
   */
  protected SnmpTlsTmMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
    counterListener.setCountRegisteredOnly(true);
//--AgentGen END
  }

  /**
   * Constructs a SnmpTlsTmMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied 
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public SnmpTlsTmMib(MOFactory moFactory) {
    this();
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
    counterListener.add(snmpTlstmSessionOpens.getOid(), snmpTlstmSessionOpens.getValue());
    counterListener.add(snmpTlstmSessionClientCloses.getOid(), snmpTlstmSessionClientCloses.getValue());
    counterListener.add(snmpTlstmSessionOpenErrors.getOid(), snmpTlstmSessionOpenErrors.getValue());
    counterListener.add(snmpTlstmSessionAccepts.getOid(), snmpTlstmSessionAccepts.getValue());
    counterListener.add(snmpTlstmSessionServerCloses.getOid(), snmpTlstmSessionServerCloses.getValue());
    counterListener.add(snmpTlstmSessionNoSessions.getOid(), snmpTlstmSessionNoSessions.getValue());
    counterListener.add(snmpTlstmSessionInvalidClientCertificates.getOid(), snmpTlstmSessionInvalidClientCertificates.getValue());
    counterListener.add(snmpTlstmSessionUnknownServerCertificate.getOid(), snmpTlstmSessionUnknownServerCertificate.getValue());
    counterListener.add(snmpTlstmSessionInvalidServerCertificates.getOid(), snmpTlstmSessionInvalidServerCertificates.getValue());
    counterListener.add(snmpTlstmSessionInvalidCaches.getOid(), snmpTlstmSessionInvalidCaches.getValue());
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
    snmpTlstmSessionOpens =
      moFactory.createScalar(oidSnmpTlstmSessionOpens,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionClientCloses = 
      moFactory.createScalar(oidSnmpTlstmSessionClientCloses,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionOpenErrors = 
      moFactory.createScalar(oidSnmpTlstmSessionOpenErrors,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionAccepts = 
      moFactory.createScalar(oidSnmpTlstmSessionAccepts,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionServerCloses = 
      moFactory.createScalar(oidSnmpTlstmSessionServerCloses,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionNoSessions = 
      moFactory.createScalar(oidSnmpTlstmSessionNoSessions,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionInvalidClientCertificates = 
      moFactory.createScalar(oidSnmpTlstmSessionInvalidClientCertificates,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionUnknownServerCertificate = 
      moFactory.createScalar(oidSnmpTlstmSessionUnknownServerCertificate,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionInvalidServerCertificates = 
      moFactory.createScalar(oidSnmpTlstmSessionInvalidServerCertificates,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmSessionInvalidCaches = 
      moFactory.createScalar(oidSnmpTlstmSessionInvalidCaches,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Counter32());
    snmpTlstmCertToTSNCount = 
      moFactory.createScalar(oidSnmpTlstmCertToTSNCount,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Gauge32());
    snmpTlstmCertToTSNTableLastChanged = 
      moFactory.createScalar(oidSnmpTlstmCertToTSNTableLastChanged,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             null,
                             TC_MODULE_SNMPV2_TC, TC_TIMESTAMP);
    snmpTlstmParamsCount = 
      moFactory.createScalar(oidSnmpTlstmParamsCount,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Gauge32());
    snmpTlstmParamsTableLastChanged = 
      moFactory.createScalar(oidSnmpTlstmParamsTableLastChanged,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             null,
                             TC_MODULE_SNMPV2_TC, TC_TIMESTAMP);
    snmpTlstmAddrCount = 
      moFactory.createScalar(oidSnmpTlstmAddrCount,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new Gauge32());
    snmpTlstmAddrTableLastChanged = 
      moFactory.createScalar(oidSnmpTlstmAddrTableLastChanged,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             null,
                             TC_MODULE_SNMPV2_TC, TC_TIMESTAMP);
    createSnmpTlstmCertToTSNEntry(moFactory);
    createSnmpTlstmParamsEntry(moFactory);
    createSnmpTlstmAddrEntry(moFactory);
  }

  public MOScalar getSnmpTlstmSessionOpens() {
    return snmpTlstmSessionOpens;
  }
  public MOScalar getSnmpTlstmSessionClientCloses() {
    return snmpTlstmSessionClientCloses;
  }
  public MOScalar getSnmpTlstmSessionOpenErrors() {
    return snmpTlstmSessionOpenErrors;
  }
  public MOScalar getSnmpTlstmSessionAccepts() {
    return snmpTlstmSessionAccepts;
  }
  public MOScalar getSnmpTlstmSessionServerCloses() {
    return snmpTlstmSessionServerCloses;
  }
  public MOScalar getSnmpTlstmSessionNoSessions() {
    return snmpTlstmSessionNoSessions;
  }
  public MOScalar getSnmpTlstmSessionInvalidClientCertificates() {
    return snmpTlstmSessionInvalidClientCertificates;
  }
  public MOScalar getSnmpTlstmSessionUnknownServerCertificate() {
    return snmpTlstmSessionUnknownServerCertificate;
  }
  public MOScalar getSnmpTlstmSessionInvalidServerCertificates() {
    return snmpTlstmSessionInvalidServerCertificates;
  }
  public MOScalar getSnmpTlstmSessionInvalidCaches() {
    return snmpTlstmSessionInvalidCaches;
  }
  public MOScalar getSnmpTlstmCertToTSNCount() {
    return snmpTlstmCertToTSNCount;
  }
  public MOScalar getSnmpTlstmCertToTSNTableLastChanged() {
    return snmpTlstmCertToTSNTableLastChanged;
  }
  public MOScalar getSnmpTlstmParamsCount() {
    return snmpTlstmParamsCount;
  }
  public MOScalar getSnmpTlstmParamsTableLastChanged() {
    return snmpTlstmParamsTableLastChanged;
  }
  public MOScalar getSnmpTlstmAddrCount() {
    return snmpTlstmAddrCount;
  }
  public MOScalar getSnmpTlstmAddrTableLastChanged() {
    return snmpTlstmAddrTableLastChanged;
  }


  public MOTable getSnmpTlstmCertToTSNEntry() {
    return snmpTlstmCertToTSNEntry;
  }


  @SuppressWarnings("unchecked")
  private void createSnmpTlstmCertToTSNEntry(MOFactory moFactory) {
    // Index definition
    snmpTlstmCertToTSNEntryIndexes = 
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidSnmpTlstmCertToTSNID, 
                               SMIConstants.SYNTAX_INTEGER, 1, 1)    };

    snmpTlstmCertToTSNEntryIndex = 
      moFactory.createIndex(snmpTlstmCertToTSNEntryIndexes,
                            false,
                            new MOTableIndexValidator() {
      public boolean isValidIndex(OID index) {
        boolean isValidIndex = true;
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::isValidIndex
     //--AgentGen END
        return isValidIndex;
      }
    });

    // Columns
    MOColumn[] snmpTlstmCertToTSNEntryColumns = new MOColumn[5];
    snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNFingerprint] = 
      new MOMutableColumn(colSnmpTlstmCertToTSNFingerprint,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          (OctetString)null);
    ValueConstraint snmpTlstmCertToTSNFingerprintVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmpTlstmCertToTSNFingerprintVC).add(new Constraint(1L, 255L));
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNFingerprint]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmCertToTSNFingerprintVC));                                  
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNFingerprint]).
      addMOValueValidationListener(new SnmpTlstmCertToTSNFingerprintValidator());
    snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNMapType] = 
      new MOMutableColumn(colSnmpTlstmCertToTSNMapType,
                          SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OID("1.3.6.1.2.1.198.1.1.1"));
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNMapType]).
      addMOValueValidationListener(new SnmpTlstmCertToTSNMapTypeValidator());
    snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNData] = 
      new MOMutableColumn(colSnmpTlstmCertToTSNData,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OctetString(new byte[] {  }));
    ValueConstraint snmpTlstmCertToTSNDataVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmpTlstmCertToTSNDataVC).add(new Constraint(0L, 1024L));
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNData]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmCertToTSNDataVC));                                  
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNData]).
      addMOValueValidationListener(new SnmpTlstmCertToTSNDataValidator());
    snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNStorageType] = 
      new StorageType(colSnmpTlstmCertToTSNStorageType,
                      moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                      new Integer32(3));
    ValueConstraint snmpTlstmCertToTSNStorageTypeVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5 });
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNStorageType]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmCertToTSNStorageTypeVC));                                  
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNStorageType]).
      addMOValueValidationListener(new SnmpTlstmCertToTSNStorageTypeValidator());
    snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNRowStatus] = 
      new RowStatus(colSnmpTlstmCertToTSNRowStatus);
    ValueConstraint snmpTlstmCertToTSNRowStatusVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5,
                  6 });
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNRowStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmCertToTSNRowStatusVC));                                  
    ((MOMutableColumn)snmpTlstmCertToTSNEntryColumns[idxSnmpTlstmCertToTSNRowStatus]).
      addMOValueValidationListener(new SnmpTlstmCertToTSNRowStatusValidator());
    // Table model
    snmpTlstmCertToTSNEntryModel =

        moFactory.createTableModel(oidSnmpTlstmCertToTSNEntry,
                                   snmpTlstmCertToTSNEntryIndex,
                                   snmpTlstmCertToTSNEntryColumns);
    snmpTlstmCertToTSNEntryModel.setRowFactory(new SnmpTlstmCertToTSNEntryRowFactory());
    snmpTlstmCertToTSNEntry = 
      moFactory.createTable(oidSnmpTlstmCertToTSNEntry,
                            snmpTlstmCertToTSNEntryIndex,
                            snmpTlstmCertToTSNEntryColumns,
                            snmpTlstmCertToTSNEntryModel);
  }

  public MOTable getSnmpTlstmParamsEntry() {
    return snmpTlstmParamsEntry;
  }


  @SuppressWarnings("unchecked")
  private void createSnmpTlstmParamsEntry(MOFactory moFactory) {
    // Index definition
    snmpTlstmParamsEntryIndexes = 
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidSnmpTargetParamsName, 
                               SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
    };

    snmpTlstmParamsEntryIndex = 
      moFactory.createIndex(snmpTlstmParamsEntryIndexes,
                            true,
                            new MOTableIndexValidator() {
      public boolean isValidIndex(OID index) {
        boolean isValidIndex = true;
     //--AgentGen BEGIN=snmpTlstmParamsEntry::isValidIndex
     //--AgentGen END
        return isValidIndex;
      }
    });

    // Columns
    MOColumn[] snmpTlstmParamsEntryColumns = new MOColumn[3];
    snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsClientFingerprint] = 
      new MOMutableColumn(colSnmpTlstmParamsClientFingerprint,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          (OctetString)null);
    ValueConstraint snmpTlstmParamsClientFingerprintVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmpTlstmParamsClientFingerprintVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsClientFingerprint]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmParamsClientFingerprintVC));                                  
    ((MOMutableColumn)snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsClientFingerprint]).
      addMOValueValidationListener(new SnmpTlstmParamsClientFingerprintValidator());
    snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsStorageType] = 
      new StorageType(colSnmpTlstmParamsStorageType,
                      moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                      new Integer32(3));
    ValueConstraint snmpTlstmParamsStorageTypeVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5 });
    ((MOMutableColumn)snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsStorageType]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmParamsStorageTypeVC));                                  
    ((MOMutableColumn)snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsStorageType]).
      addMOValueValidationListener(new SnmpTlstmParamsStorageTypeValidator());
    snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsRowStatus] = 
      new RowStatus(colSnmpTlstmParamsRowStatus);
    ValueConstraint snmpTlstmParamsRowStatusVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5,
                  6 });
    ((MOMutableColumn)snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsRowStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmParamsRowStatusVC));                                  
    ((MOMutableColumn)snmpTlstmParamsEntryColumns[idxSnmpTlstmParamsRowStatus]).
      addMOValueValidationListener(new SnmpTlstmParamsRowStatusValidator());
    // Table model
    snmpTlstmParamsEntryModel = 
      moFactory.createTableModel(oidSnmpTlstmParamsEntry,
                                 snmpTlstmParamsEntryIndex,
                                 snmpTlstmParamsEntryColumns);
    ((MOMutableTableModel)snmpTlstmParamsEntryModel).setRowFactory(
      new SnmpTlstmParamsEntryRowFactory());
    snmpTlstmParamsEntry = 
      moFactory.createTable(oidSnmpTlstmParamsEntry,
                            snmpTlstmParamsEntryIndex,
                            snmpTlstmParamsEntryColumns,
                            snmpTlstmParamsEntryModel);
  }

  public MOTable getSnmpTlstmAddrEntry() {
    return snmpTlstmAddrEntry;
  }


  @SuppressWarnings("unchecked")
  private void createSnmpTlstmAddrEntry(MOFactory moFactory) {
    // Index definition
    snmpTlstmAddrEntryIndexes = 
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidSnmpTargetAddrName, 
                               SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
    };

    snmpTlstmAddrEntryIndex = 
      moFactory.createIndex(snmpTlstmAddrEntryIndexes,
                            true,
                            new MOTableIndexValidator() {
      public boolean isValidIndex(OID index) {
        boolean isValidIndex = true;
     //--AgentGen BEGIN=snmpTlstmAddrEntry::isValidIndex
     //--AgentGen END
        return isValidIndex;
      }
    });

    // Columns
    MOColumn[] snmpTlstmAddrEntryColumns = new MOColumn[4];
    snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrServerFingerprint] = 
      new MOMutableColumn(colSnmpTlstmAddrServerFingerprint,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OctetString(new byte[] {  }));
    ValueConstraint snmpTlstmAddrServerFingerprintVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmpTlstmAddrServerFingerprintVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrServerFingerprint]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmAddrServerFingerprintVC));                                  
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrServerFingerprint]).
      addMOValueValidationListener(new SnmpTlstmAddrServerFingerprintValidator());
    snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrServerIdentity] = 
      new MOMutableColumn(colSnmpTlstmAddrServerIdentity,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                          new OctetString(new byte[] {  }));
    ValueConstraint snmpTlstmAddrServerIdentityVC = new ConstraintsImpl();
    ((ConstraintsImpl)snmpTlstmAddrServerIdentityVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrServerIdentity]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmAddrServerIdentityVC));                                  
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrServerIdentity]).
      addMOValueValidationListener(new SnmpTlstmAddrServerIdentityValidator());
    snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrStorageType] = 
      new StorageType(colSnmpTlstmAddrStorageType,
                      moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_CREATE),
                      new Integer32(3));
    ValueConstraint snmpTlstmAddrStorageTypeVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5 });
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrStorageType]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmAddrStorageTypeVC));                                  
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrStorageType]).
      addMOValueValidationListener(new SnmpTlstmAddrStorageTypeValidator());
    snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrRowStatus] = 
      new RowStatus(colSnmpTlstmAddrRowStatus);
    ValueConstraint snmpTlstmAddrRowStatusVC = new EnumerationConstraint(
      new int[] { 1,
                  2,
                  3,
                  4,
                  5,
                  6 });
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrRowStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(snmpTlstmAddrRowStatusVC));                                  
    ((MOMutableColumn)snmpTlstmAddrEntryColumns[idxSnmpTlstmAddrRowStatus]).
      addMOValueValidationListener(new SnmpTlstmAddrRowStatusValidator());
    // Table model
    snmpTlstmAddrEntryModel = 
      moFactory.createTableModel(oidSnmpTlstmAddrEntry,
                                 snmpTlstmAddrEntryIndex,
                                 snmpTlstmAddrEntryColumns);
    ((MOMutableTableModel)snmpTlstmAddrEntryModel).setRowFactory(
      new SnmpTlstmAddrEntryRowFactory());
    snmpTlstmAddrEntry = 
      moFactory.createTable(oidSnmpTlstmAddrEntry,
                            snmpTlstmAddrEntryIndex,
                            snmpTlstmAddrEntryColumns,
                            snmpTlstmAddrEntryModel);
  }



  public void registerMOs(MOServer server, OctetString context) 
    throws DuplicateRegistrationException 
  {
    // Scalar Objects
    server.register(this.snmpTlstmSessionOpens, context);
    server.register(this.snmpTlstmSessionClientCloses, context);
    server.register(this.snmpTlstmSessionOpenErrors, context);
    server.register(this.snmpTlstmSessionAccepts, context);
    server.register(this.snmpTlstmSessionServerCloses, context);
    server.register(this.snmpTlstmSessionNoSessions, context);
    server.register(this.snmpTlstmSessionInvalidClientCertificates, context);
    server.register(this.snmpTlstmSessionUnknownServerCertificate, context);
    server.register(this.snmpTlstmSessionInvalidServerCertificates, context);
    server.register(this.snmpTlstmSessionInvalidCaches, context);
    server.register(this.snmpTlstmCertToTSNCount, context);
    server.register(this.snmpTlstmCertToTSNTableLastChanged, context);
    server.register(this.snmpTlstmParamsCount, context);
    server.register(this.snmpTlstmParamsTableLastChanged, context);
    server.register(this.snmpTlstmAddrCount, context);
    server.register(this.snmpTlstmAddrTableLastChanged, context);
    server.register(this.snmpTlstmCertToTSNEntry, context);
    server.register(this.snmpTlstmParamsEntry, context);
    server.register(this.snmpTlstmAddrEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpTlstmSessionOpens, context);
    server.unregister(this.snmpTlstmSessionClientCloses, context);
    server.unregister(this.snmpTlstmSessionOpenErrors, context);
    server.unregister(this.snmpTlstmSessionAccepts, context);
    server.unregister(this.snmpTlstmSessionServerCloses, context);
    server.unregister(this.snmpTlstmSessionNoSessions, context);
    server.unregister(this.snmpTlstmSessionInvalidClientCertificates, context);
    server.unregister(this.snmpTlstmSessionUnknownServerCertificate, context);
    server.unregister(this.snmpTlstmSessionInvalidServerCertificates, context);
    server.unregister(this.snmpTlstmSessionInvalidCaches, context);
    server.unregister(this.snmpTlstmCertToTSNCount, context);
    server.unregister(this.snmpTlstmCertToTSNTableLastChanged, context);
    server.unregister(this.snmpTlstmParamsCount, context);
    server.unregister(this.snmpTlstmParamsTableLastChanged, context);
    server.unregister(this.snmpTlstmAddrCount, context);
    server.unregister(this.snmpTlstmAddrTableLastChanged, context);
    server.unregister(this.snmpTlstmCertToTSNEntry, context);
    server.unregister(this.snmpTlstmParamsEntry, context);
    server.unregister(this.snmpTlstmAddrEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications
  public void snmpTlstmServerCertificateUnknown(NotificationOriginator notificationOriginator,
                                                OctetString context, VariableBinding[] vbs) {
    if (vbs.length < 1) {
      throw new IllegalArgumentException("Too few notification objects: "+
                                         vbs.length+"<1");
    }
    if (!(vbs[0].getOid().startsWith(oidTrapVarSnmpTlstmSessionUnknownServerCertificate))) {
      throw new IllegalArgumentException("Variable 0 has wrong OID: "+vbs[0].getOid()+
                                         " does not start with "+oidTrapVarSnmpTlstmSessionUnknownServerCertificate);
    }
    notificationOriginator.notify(context, oidSnmpTlstmServerCertificateUnknown, vbs);
  }
  // Named traps: 
  // Methods to fire named traps: []
   


  public void snmpTlstmServerInvalidCertificate(NotificationOriginator notificationOriginator,
                                                OctetString context, VariableBinding[] vbs) {
    if (vbs.length < 2) {
      throw new IllegalArgumentException("Too few notification objects: "+
                                         vbs.length+"<2");
    }
    if (!(vbs[0].getOid().startsWith(oidTrapVarSnmpTlstmAddrServerFingerprint))) {
      throw new IllegalArgumentException("Variable 0 has wrong OID: "+vbs[0].getOid()+
                                         " does not start with "+oidTrapVarSnmpTlstmAddrServerFingerprint);
    }
    if (!snmpTlstmAddrEntryIndex.isValidIndex(snmpTlstmAddrEntry.getIndexPart(vbs[0].getOid()))) {
      throw new IllegalArgumentException("Illegal index for variable 0 specified: "+
                                         snmpTlstmAddrEntry.getIndexPart(vbs[0].getOid()));
    }
    if (!(vbs[1].getOid().startsWith(oidTrapVarSnmpTlstmSessionInvalidServerCertificates))) {
      throw new IllegalArgumentException("Variable 1 has wrong OID: "+vbs[1].getOid()+
                                         " does not start with "+oidTrapVarSnmpTlstmSessionInvalidServerCertificates);
    }
    notificationOriginator.notify(context, oidSnmpTlstmServerInvalidCertificate, vbs);
  }
  // Named traps:
  // Methods to fire named traps: []
   



  // Scalars

  // Value Validators

  /**
   * The <code>SnmpTlstmCertToTSNFingerprintValidator</code> implements the value
   * validation for <code>SnmpTlstmCertToTSNFingerprint</code>.
   */
  static class SnmpTlstmCertToTSNFingerprintValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 1) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpTlstmCertToTSNFingerprint::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmCertToTSNMapTypeValidator</code> implements the value
   * validation for <code>SnmpTlstmCertToTSNMapType</code>.
   */
  static class SnmpTlstmCertToTSNMapTypeValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmCertToTSNMapType::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmCertToTSNDataValidator</code> implements the value
   * validation for <code>SnmpTlstmCertToTSNData</code>.
   */
  static class SnmpTlstmCertToTSNDataValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 1024)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpTlstmCertToTSNData::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmCertToTSNStorageTypeValidator</code> implements the value
   * validation for <code>SnmpTlstmCertToTSNStorageType</code>.
   */
  static class SnmpTlstmCertToTSNStorageTypeValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmCertToTSNStorageType::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmCertToTSNRowStatusValidator</code> implements the value
   * validation for <code>SnmpTlstmCertToTSNRowStatus</code>.
   */
  static class SnmpTlstmCertToTSNRowStatusValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmCertToTSNRowStatus::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmParamsClientFingerprintValidator</code> implements the value
   * validation for <code>SnmpTlstmParamsClientFingerprint</code>.
   */
  static class SnmpTlstmParamsClientFingerprintValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpTlstmParamsClientFingerprint::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmParamsStorageTypeValidator</code> implements the value
   * validation for <code>SnmpTlstmParamsStorageType</code>.
   */
  static class SnmpTlstmParamsStorageTypeValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmParamsStorageType::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmParamsRowStatusValidator</code> implements the value
   * validation for <code>SnmpTlstmParamsRowStatus</code>.
   */
  static class SnmpTlstmParamsRowStatusValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmParamsRowStatus::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmAddrServerFingerprintValidator</code> implements the value
   * validation for <code>SnmpTlstmAddrServerFingerprint</code>.
   */
  static class SnmpTlstmAddrServerFingerprintValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpTlstmAddrServerFingerprint::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmAddrServerIdentityValidator</code> implements the value
   * validation for <code>SnmpTlstmAddrServerIdentity</code>.
   */
  static class SnmpTlstmAddrServerIdentityValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpTlstmAddrServerIdentity::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmAddrStorageTypeValidator</code> implements the value
   * validation for <code>SnmpTlstmAddrStorageType</code>.
   */
  static class SnmpTlstmAddrStorageTypeValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmAddrStorageType::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTlstmAddrRowStatusValidator</code> implements the value
   * validation for <code>SnmpTlstmAddrRowStatus</code>.
   */
  static class SnmpTlstmAddrRowStatusValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmpTlstmAddrRowStatus::validate
     //--AgentGen END
    }
  }

  // Rows and Factories

  public class SnmpTlstmCertToTSNEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::RowMembers
     //--AgentGen END

    public SnmpTlstmCertToTSNEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::RowConstructor
     //--AgentGen END
    }
    
    public OctetString getSnmpTlstmCertToTSNFingerprint() {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::getSnmpTlstmCertToTSNFingerprint
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmpTlstmCertToTSNFingerprint);
    }  
    
    public void setSnmpTlstmCertToTSNFingerprint(OctetString newValue) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::setSnmpTlstmCertToTSNFingerprint
     //--AgentGen END
      super.setValue(idxSnmpTlstmCertToTSNFingerprint, newValue);
    }
    
    public OID getSnmpTlstmCertToTSNMapType() {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::getSnmpTlstmCertToTSNMapType
     //--AgentGen END
      return (OID) super.getValue(idxSnmpTlstmCertToTSNMapType);
    }  
    
    public void setSnmpTlstmCertToTSNMapType(OID newValue) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::setSnmpTlstmCertToTSNMapType
     //--AgentGen END
      super.setValue(idxSnmpTlstmCertToTSNMapType, newValue);
    }
    
    public OctetString getSnmpTlstmCertToTSNData() {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::getSnmpTlstmCertToTSNData
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmpTlstmCertToTSNData);
    }  
    
    public void setSnmpTlstmCertToTSNData(OctetString newValue) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::setSnmpTlstmCertToTSNData
     //--AgentGen END
      super.setValue(idxSnmpTlstmCertToTSNData, newValue);
    }
    
    public Integer32 getSnmpTlstmCertToTSNStorageType() {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::getSnmpTlstmCertToTSNStorageType
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmpTlstmCertToTSNStorageType);
    }  
    
    public void setSnmpTlstmCertToTSNStorageType(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::setSnmpTlstmCertToTSNStorageType
     //--AgentGen END
      super.setValue(idxSnmpTlstmCertToTSNStorageType, newValue);
    }
    
    public Integer32 getSnmpTlstmCertToTSNRowStatus() {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::getSnmpTlstmCertToTSNRowStatus
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmpTlstmCertToTSNRowStatus);
    }  
    
    public void setSnmpTlstmCertToTSNRowStatus(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::setSnmpTlstmCertToTSNRowStatus
     //--AgentGen END
      super.setValue(idxSnmpTlstmCertToTSNRowStatus, newValue);
    }
    
    public Variable getValue(int column) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxSnmpTlstmCertToTSNFingerprint: 
        	return getSnmpTlstmCertToTSNFingerprint();
        case idxSnmpTlstmCertToTSNMapType: 
        	return getSnmpTlstmCertToTSNMapType();
        case idxSnmpTlstmCertToTSNData: 
        	return getSnmpTlstmCertToTSNData();
        case idxSnmpTlstmCertToTSNStorageType: 
        	return getSnmpTlstmCertToTSNStorageType();
        case idxSnmpTlstmCertToTSNRowStatus: 
        	return getSnmpTlstmCertToTSNRowStatus();
        default:
          return super.getValue(column);
      }
    }
    
    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxSnmpTlstmCertToTSNFingerprint: 
        	setSnmpTlstmCertToTSNFingerprint((OctetString)value);
        	break;
        case idxSnmpTlstmCertToTSNMapType: 
        	setSnmpTlstmCertToTSNMapType((OID)value);
        	break;
        case idxSnmpTlstmCertToTSNData: 
        	setSnmpTlstmCertToTSNData((OctetString)value);
        	break;
        case idxSnmpTlstmCertToTSNStorageType: 
        	setSnmpTlstmCertToTSNStorageType((Integer32)value);
        	break;
        case idxSnmpTlstmCertToTSNRowStatus: 
        	setSnmpTlstmCertToTSNRowStatus((Integer32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::Row
     //--AgentGen END
  }
  
  class SnmpTlstmCertToTSNEntryRowFactory 
        implements MOTableRowFactory<SnmpTlstmCertToTSNEntryRow>
  {
    public synchronized SnmpTlstmCertToTSNEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException 
    {
      SnmpTlstmCertToTSNEntryRow row = 
        new SnmpTlstmCertToTSNEntryRow(index, values);
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::createRow
     //--AgentGen END
      return row;
    }
    
    public synchronized void freeRow(SnmpTlstmCertToTSNEntryRow row) {
     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=snmpTlstmCertToTSNEntry::RowFactory
     //--AgentGen END
  }

  public class SnmpTlstmParamsEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=snmpTlstmParamsEntry::RowMembers
     //--AgentGen END

    public SnmpTlstmParamsEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=snmpTlstmParamsEntry::RowConstructor
     //--AgentGen END
    }
    
    public OctetString getSnmpTlstmParamsClientFingerprint() {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::getSnmpTlstmParamsClientFingerprint
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmpTlstmParamsClientFingerprint);
    }  
    
    public void setSnmpTlstmParamsClientFingerprint(OctetString newValue) {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::setSnmpTlstmParamsClientFingerprint
     //--AgentGen END
      super.setValue(idxSnmpTlstmParamsClientFingerprint, newValue);
    }
    
    public Integer32 getSnmpTlstmParamsStorageType() {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::getSnmpTlstmParamsStorageType
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmpTlstmParamsStorageType);
    }  
    
    public void setSnmpTlstmParamsStorageType(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::setSnmpTlstmParamsStorageType
     //--AgentGen END
      super.setValue(idxSnmpTlstmParamsStorageType, newValue);
    }
    
    public Integer32 getSnmpTlstmParamsRowStatus() {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::getSnmpTlstmParamsRowStatus
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmpTlstmParamsRowStatus);
    }  
    
    public void setSnmpTlstmParamsRowStatus(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::setSnmpTlstmParamsRowStatus
     //--AgentGen END
      super.setValue(idxSnmpTlstmParamsRowStatus, newValue);
    }
    
    public Variable getValue(int column) {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxSnmpTlstmParamsClientFingerprint: 
        	return getSnmpTlstmParamsClientFingerprint();
        case idxSnmpTlstmParamsStorageType: 
        	return getSnmpTlstmParamsStorageType();
        case idxSnmpTlstmParamsRowStatus: 
        	return getSnmpTlstmParamsRowStatus();
        default:
          return super.getValue(column);
      }
    }
    
    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxSnmpTlstmParamsClientFingerprint: 
        	setSnmpTlstmParamsClientFingerprint((OctetString)value);
        	break;
        case idxSnmpTlstmParamsStorageType: 
        	setSnmpTlstmParamsStorageType((Integer32)value);
        	break;
        case idxSnmpTlstmParamsRowStatus: 
        	setSnmpTlstmParamsRowStatus((Integer32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=snmpTlstmParamsEntry::Row
     //--AgentGen END
  }
  
  class SnmpTlstmParamsEntryRowFactory 
        implements MOTableRowFactory<SnmpTlstmParamsEntryRow>
  {
    public synchronized SnmpTlstmParamsEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException 
    {
      SnmpTlstmParamsEntryRow row = 
        new SnmpTlstmParamsEntryRow(index, values);
     //--AgentGen BEGIN=snmpTlstmParamsEntry::createRow
     //--AgentGen END
      return row;
    }
    
    public synchronized void freeRow(SnmpTlstmParamsEntryRow row) {
     //--AgentGen BEGIN=snmpTlstmParamsEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=snmpTlstmParamsEntry::RowFactory
     //--AgentGen END
  }

  public class SnmpTlstmAddrEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=snmpTlstmAddrEntry::RowMembers
     //--AgentGen END

    public SnmpTlstmAddrEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=snmpTlstmAddrEntry::RowConstructor
     //--AgentGen END
    }
    
    public OctetString getSnmpTlstmAddrServerFingerprint() {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::getSnmpTlstmAddrServerFingerprint
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmpTlstmAddrServerFingerprint);
    }  
    
    public void setSnmpTlstmAddrServerFingerprint(OctetString newValue) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::setSnmpTlstmAddrServerFingerprint
     //--AgentGen END
      super.setValue(idxSnmpTlstmAddrServerFingerprint, newValue);
    }
    
    public OctetString getSnmpTlstmAddrServerIdentity() {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::getSnmpTlstmAddrServerIdentity
     //--AgentGen END
      return (OctetString) super.getValue(idxSnmpTlstmAddrServerIdentity);
    }  
    
    public void setSnmpTlstmAddrServerIdentity(OctetString newValue) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::setSnmpTlstmAddrServerIdentity
     //--AgentGen END
      super.setValue(idxSnmpTlstmAddrServerIdentity, newValue);
    }
    
    public Integer32 getSnmpTlstmAddrStorageType() {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::getSnmpTlstmAddrStorageType
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmpTlstmAddrStorageType);
    }  
    
    public void setSnmpTlstmAddrStorageType(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::setSnmpTlstmAddrStorageType
     //--AgentGen END
      super.setValue(idxSnmpTlstmAddrStorageType, newValue);
    }
    
    public Integer32 getSnmpTlstmAddrRowStatus() {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::getSnmpTlstmAddrRowStatus
     //--AgentGen END
      return (Integer32) super.getValue(idxSnmpTlstmAddrRowStatus);
    }  
    
    public void setSnmpTlstmAddrRowStatus(Integer32 newValue) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::setSnmpTlstmAddrRowStatus
     //--AgentGen END
      super.setValue(idxSnmpTlstmAddrRowStatus, newValue);
    }
    
    public Variable getValue(int column) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxSnmpTlstmAddrServerFingerprint: 
        	return getSnmpTlstmAddrServerFingerprint();
        case idxSnmpTlstmAddrServerIdentity: 
        	return getSnmpTlstmAddrServerIdentity();
        case idxSnmpTlstmAddrStorageType: 
        	return getSnmpTlstmAddrStorageType();
        case idxSnmpTlstmAddrRowStatus: 
        	return getSnmpTlstmAddrRowStatus();
        default:
          return super.getValue(column);
      }
    }
    
    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxSnmpTlstmAddrServerFingerprint: 
        	setSnmpTlstmAddrServerFingerprint((OctetString)value);
        	break;
        case idxSnmpTlstmAddrServerIdentity: 
        	setSnmpTlstmAddrServerIdentity((OctetString)value);
        	break;
        case idxSnmpTlstmAddrStorageType: 
        	setSnmpTlstmAddrStorageType((Integer32)value);
        	break;
        case idxSnmpTlstmAddrRowStatus: 
        	setSnmpTlstmAddrRowStatus((Integer32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=snmpTlstmAddrEntry::Row
     //--AgentGen END
  }
  
  class SnmpTlstmAddrEntryRowFactory 
        implements MOTableRowFactory<SnmpTlstmAddrEntryRow>
  {
    public synchronized SnmpTlstmAddrEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException 
    {
      SnmpTlstmAddrEntryRow row = 
        new SnmpTlstmAddrEntryRow(index, values);
     //--AgentGen BEGIN=snmpTlstmAddrEntry::createRow
     //--AgentGen END
      return row;
    }
    
    public synchronized void freeRow(SnmpTlstmAddrEntryRow row) {
     //--AgentGen BEGIN=snmpTlstmAddrEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=snmpTlstmAddrEntry::RowFactory
     //--AgentGen END
  }


//--AgentGen BEGIN=_METHODS

  public CounterListener getCounterListener() {
    return counterListener;
  }

  @Override
  public boolean isClientCertificateAccepted(X509Certificate peerEndCertificate) {
    X509Certificate x509Certificate = peerEndCertificate;
    OctetString peerFingerprint = new OctetString(x509Certificate.getSignature());
    synchronized (snmpTlstmCertToTSNEntryModel) {
      MOTableRowFilter<SnmpTlstmCertToTSNEntryRow> activeRowsFilter =
          new RowStatus.ActiveRowsFilter<SnmpTlstmCertToTSNEntryRow>(idxSnmpTlstmCertToTSNRowStatus);
      for (Iterator<SnmpTlstmCertToTSNEntryRow> rows =
               snmpTlstmCertToTSNEntryModel.iterator(activeRowsFilter); rows.hasNext();) {
        SnmpTlstmCertToTSNEntryRow row = rows.next();
        if (peerFingerprint.equals(row.getSnmpTlstmCertToTSNFingerprint())) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public OctetString getSecurityName(X509Certificate[] peerCertificateChain) {
    if (peerCertificateChain != null) {
      synchronized (snmpTlstmCertToTSNEntryModel) {
        MOTableRowFilter<SnmpTlstmCertToTSNEntryRow> activeRowsFilter =
            new RowStatus.ActiveRowsFilter<SnmpTlstmCertToTSNEntryRow>(idxSnmpTlstmCertToTSNRowStatus);
        for (Iterator<SnmpTlstmCertToTSNEntryRow> rows =
             snmpTlstmCertToTSNEntryModel.iterator(activeRowsFilter); rows.hasNext();) {
          SnmpTlstmCertToTSNEntryRow row = rows.next();
          OctetString fingerprint = row.getSnmpTlstmCertToTSNFingerprint();
          for (X509Certificate cert : peerCertificateChain) {
            OctetString certFingerprint = null;
            certFingerprint = TLSTM.getFingerprint(cert);
            if (LOGGER.isDebugEnabled()) {
              LOGGER.debug("Comparing certificate fingerprint "+certFingerprint+
                           " with "+fingerprint);
            }
            if (certFingerprint == null) {
              LOGGER.error("Failed to determine fingerprint for certificate "+cert+
                           " and algorithm "+cert.getSigAlgName());
            }
            else if (certFingerprint.equals(fingerprint)) {
              // possible match found -> now try to map to tmSecurityName
              OID mappingType = row.getSnmpTlstmCertToTSNMapType();
              OctetString data = row.getSnmpTlstmCertToTSNData();
              OctetString tmSecurityName = null;
              try {
                tmSecurityName = mapCertToTSN(cert, mappingType, data);
              } catch (CertificateParsingException e) {
                LOGGER.warn("Failed to parse client certificate: " + e.getMessage());
              }
              if ((tmSecurityName != null) && (tmSecurityName.length() <= 32)) {
                return tmSecurityName;
              }
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public boolean isServerCertificateAccepted(X509Certificate[] peerCertificateChain) {
    // As we are the server
    return true;
  }

  @Override
  public boolean isAcceptedIssuer(X509Certificate issuerCertificate) {
    // As we are the server
    return true;
  }

  @Override
  public String getLocalCertificateAlias(Address targetAddress) {
    return null;
  }

  private OctetString mapCertToTSN(X509Certificate cert, OID mappingType, OctetString data)
      throws CertificateParsingException
  {
    if (oidSnmpTlstmCertSpecified.equals(mappingType)) {
      return data;
    }
    else if (oidSnmpTlstmCertSANRFC822Name.equals(mappingType) ||
             oidSnmpTlstmCertSANAny.equals(mappingType)) {
      Object entry = TLSTM.getSubjAltName(cert.getSubjectAlternativeNames(), 1);
      if (entry != null) {
        String[] rfc822Name = ((String)entry).split("@");
        return new OctetString(rfc822Name[0]+"@"+rfc822Name[1].toLowerCase());
      }
    }
    if (oidSnmpTlstmCertSANDNSName.equals(mappingType) ||
        oidSnmpTlstmCertSANAny.equals(mappingType)) {
      Object entry = TLSTM.getSubjAltName(cert.getSubjectAlternativeNames(), 2);
      if (entry != null) {
        String dNSName = ((String)entry).toLowerCase();
        return new OctetString(dNSName);
      }
    }
    if (oidSnmpTlstmCertSANIpAddress.equals(mappingType) ||
        oidSnmpTlstmCertSANAny.equals(mappingType)) {
      Object entry = TLSTM.getSubjAltName(cert.getSubjectAlternativeNames(), 7);
      if (entry != null) {
        String ipAddress = ((String)entry).toLowerCase();
        if (ipAddress.indexOf(':')>=0) {
          // IPv6 address
          StringBuffer buf = new StringBuffer(16);
          String[] bytes = ipAddress.split(":");
          for (String b : bytes) {
            for (int diff = 2-b.length(); diff>0; diff--) {
              buf.append('0');
            }
            buf.append(b);
          }
          return new OctetString(buf.toString());
        }
        return new OctetString(ipAddress);
      }
    }
    if (oidSnmpTlstmCertCommonName.equals(mappingType)) {
      X500Principal x500Principal = cert.getSubjectX500Principal();
      return new OctetString(x500Principal.getName());
    }
    return null;
  }

//--AgentGen END

  // Textual Definitions of MIB module SnmpTlsTmMib
  protected void addTCsToFactory(MOFactory moFactory) {
   moFactory.addTextualConvention(new SnmpTLSFingerprint()); 
  }


  public class SnmpTLSFingerprint implements TextualConvention {
  	
    public SnmpTLSFingerprint() {
    }

    public String getModuleName() {
      return TC_MODULE_SNMP_TLS_TM_MIB;
    }
  	
    public String getName() {
      return TC_SNMPTLSFINGERPRINT;
    }
    
    public Variable createInitialValue() {
    	Variable v = new OctetString();
      if (v instanceof AssignableFromLong) {
      	((AssignableFromLong)v).setValue(0L);
      }
    	// further modify value to comply with TC constraints here:
     //--AgentGen BEGIN=SnmpTLSFingerprint::createInitialValue
     //--AgentGen END
	    return v;
    }
  	
    public MOScalar createScalar(OID oid, MOAccess access, Variable value) {
      MOScalar scalar = moFactory.createScalar(oid, access, value);
      ValueConstraint vc = new ConstraintsImpl();
      ((ConstraintsImpl)vc).add(new Constraint(0L, 255L));
      scalar.addMOValueValidationListener(new ValueConstraintValidator(vc));                                  
     //--AgentGen BEGIN=SnmpTLSFingerprint::createScalar
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
     //--AgentGen BEGIN=SnmpTLSFingerprint::createColumn
     //--AgentGen END
      return col;      
    }
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  public void addImportedTCsToFactory(MOFactory moFactory) {
   moFactory.addTextualConvention(new SnmpAdminString()); 
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


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


