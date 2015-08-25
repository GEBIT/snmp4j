/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpTargetMIB.java  
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
import org.snmp4j.agent.request.*;
import org.snmp4j.event.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class SnmpTargetMIB implements MOGroup, CounterListener,
    MOTableRowListener {

  private static final LogAdapter logger =
      LogFactory.getLogger(SnmpTargetMIB.class);

  // Constants
  private static final OID oidSnmpTargetSpinLock =
      new OID(new int[] {1, 3, 6, 1, 6, 3, 12, 1, 1, 0});

  private MOScalar snmpTargetSpinLock;

  private static final OID oidSnmpUnknownContexts =
      new OID(new int[] {1, 3, 6, 1, 6, 3, 12, 1, 5, 0});

  private MOScalar snmpUnknownContexts;

  private static final OID oidSnmpUnavailableContexts =
      new OID(new int[] {1, 3, 6, 1, 6, 3, 12, 1, 4, 0});

  private MOScalar snmpUnavailableContexts;

  private static final OID oidSnmpTargetParamsEntry =
      new OID(new int[] {1, 3, 6, 1, 6, 3, 12, 1, 3, 1});

  // Column sub-identifer defintions for snmpTargetParamsEntry:
  private static final int colSnmpTargetParamsMPModel = 2;
  private static final int colSnmpTargetParamsSecurityModel = 3;
  private static final int colSnmpTargetParamsSecurityName = 4;
  private static final int colSnmpTargetParamsSecurityLevel = 5;
  private static final int colSnmpTargetParamsStorageType = 6;
  private static final int colSnmpTargetParamsRowStatus = 7;

  // Column index defintions for snmpTargetParamsEntry:
  static final int idxSnmpTargetParamsMPModel = 0;
  static final int idxSnmpTargetParamsSecurityModel = 1;
  static final int idxSnmpTargetParamsSecurityName = 2;
  static final int idxSnmpTargetParamsSecurityLevel = 3;
  static final int idxSnmpTargetParamsStorageType = 4;
  static final int idxSnmpTargetParamsRowStatus = 5;
  private static MOTableSubIndex[] snmpTargetParamsEntryIndexes =
      new MOTableSubIndex[] {
      new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpTargetParamsEntryIndex =
      new MOTableIndex(snmpTargetParamsEntryIndexes, true);

  private DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
      snmpTargetParamsEntry;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> snmpTargetParamsEntryModel;
  private static final OID oidSnmpTargetAddrEntry =
      new OID(new int[] {1, 3, 6, 1, 6, 3, 12, 1, 2, 1});

  // Column sub-identifier definitions for snmpTargetAddrEntry:
  private static final int colSnmpTargetAddrTDomain = 2;
  private static final int colSnmpTargetAddrTAddress = 3;
  private static final int colSnmpTargetAddrTimeout = 4;
  private static final int colSnmpTargetAddrRetryCount = 5;
  private static final int colSnmpTargetAddrTagList = 6;
  private static final int colSnmpTargetAddrParams = 7;
  private static final int colSnmpTargetAddrStorageType = 8;
  private static final int colSnmpTargetAddrRowStatus = 9;

  // Column index definitions for snmpTargetAddrEntry:
  static final int idxSnmpTargetAddrTDomain = 0;
  static final int idxSnmpTargetAddrTAddress = 1;
  static final int idxSnmpTargetAddrTimeout = 2;
  static final int idxSnmpTargetAddrRetryCount = 3;
  static final int idxSnmpTargetAddrTagList = 4;
  static final int idxSnmpTargetAddrParams = 5;
  static final int idxSnmpTargetAddrStorageType = 6;
  static final int idxSnmpTargetAddrRowStatus = 7;
  private static MOTableSubIndex[] snmpTargetAddrEntryIndexes =
      new MOTableSubIndex[] {
      new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpTargetAddrEntryIndex =
      new MOTableIndex(snmpTargetAddrEntryIndexes, true);

  private static final OID[] DEFAULT_TDOMAINS = {
     TransportDomains.snmpUDPDomain,
     TransportDomains.transportDomainTcpIpv4,
     TransportDomains.transportDomainTcpIpv6,
     TransportDomains.transportDomainUdpIpv4,
     TransportDomains.transportDomainUdpIpv6,
     TransportDomains.transportDomainUdpDns,
     TransportDomains.transportDomainTcpDns
  };

  private DefaultMOTable<SnmpTargetAddrEntryRow,MOColumn,DefaultMOMutableTableModel<SnmpTargetAddrEntryRow>>
      snmpTargetAddrEntry;
  private DefaultMOMutableTableModel<SnmpTargetAddrEntryRow> snmpTargetAddrEntryModel;

  private Map<OctetString, Set<SnmpTargetAddrEntryRow>> snmpTargetAddrTagIndex =
      new Hashtable<OctetString, Set<SnmpTargetAddrEntryRow>>();

  // maps TDomain OIDs to TDomainAddressFactory instances
  private Hashtable<OID, TDomainAddressFactory> supportedAddressClasses = new Hashtable<OID, TDomainAddressFactory>();
  private MessageDispatcher messageDispatcher;
  private CoexistenceInfoProvider coexistenceProvider;
  private SnmpTlsTmMib tlsTmMib;

  public SnmpTargetMIB(MessageDispatcher messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
    snmpTargetSpinLock = new TestAndIncr(oidSnmpTargetSpinLock);
    snmpUnknownContexts =
        new MOScalar<Counter32>(oidSnmpUnknownContexts,
                     MOAccessImpl.ACCESS_READ_ONLY, new Counter32());
    snmpUnavailableContexts =
        new MOScalar<Counter32>(oidSnmpUnavailableContexts,
                     MOAccessImpl.ACCESS_READ_ONLY, new Counter32());
    createSnmpTargetParamsEntry();
    createSnmpTargetAddrEntry();
  }

  public Collection<SnmpTargetAddrEntryRow> getTargetAddrRowsForTag(OctetString tag) {
    Collection<SnmpTargetAddrEntryRow> l = snmpTargetAddrTagIndex.get(tag);
    if (l == null) {
      return Collections.emptySet();
    }
    else {
      synchronized (l) {
        l = new ArrayList<SnmpTargetAddrEntryRow>(l);
      }
    }
    return l;
  }

  public SnmpTlsTmMib getTlsTmMib() {
    return tlsTmMib;
  }

  public void setTlsTmMib(SnmpTlsTmMib tlsTmMib) {
    this.tlsTmMib = tlsTmMib;
  }

  /**
   * Returns the local SNMPv3 engine ID.
   * @return
   *    the SNMP3v local engine ID, if the {@link MPv3} is available or
   *    <code>null</code> otherwise.
   * @since 1.2
   */
  public byte[] getLocalEngineID() {
    MPv3 mpv3 = (MPv3)
        messageDispatcher.getMessageProcessingModel(MessageProcessingModel.MPv3);
    if (mpv3 == null) {
      return null;
    }
    return mpv3.getLocalEngineID();
  }

  public Address getTargetAddress(OctetString name) {
    OID index = name.toSubIndex(true);
    SnmpTargetAddrEntryRow trow = this.snmpTargetAddrEntryModel.getRow(index);
    if (trow != null) {
      return trow.getAddress();
    }
    return null;
  }

  public Target getTarget(OctetString name,
                          OctetString contextEngineID,
                          OctetString contextName) {
    OID index = name.toSubIndex(true);
    SnmpTargetAddrEntryRow trow =
        (SnmpTargetAddrEntryRow) this.snmpTargetAddrEntryModel.getRow(index);
    if (trow != null) {
      return trow.getTarget(contextEngineID, contextName);
    }
    return null;
  }

  public void addDefaultTDomains() {
    TDomainAddressFactoryImpl factory = new TDomainAddressFactoryImpl();
    for (OID tDomain : DEFAULT_TDOMAINS) {
      supportedAddressClasses.put(tDomain, factory);
    }
  }

  public void addSupportedTDomain(OID transportDomain,
                                  TDomainAddressFactory factory) {
    supportedAddressClasses.put(transportDomain, factory);
  }

  public boolean addTargetAddress(OctetString name,
                                  OID transportDomain, OctetString address,
                                  int timeout, int retries,
                                  OctetString tagList,
                                  OctetString params,
                                  int storageType) {
    Variable[] vbs = new Variable[snmpTargetAddrEntry.getColumnCount()];
    int n=0;
    vbs[n++] = transportDomain;
    vbs[n++] = address;
    vbs[n++] = new Integer32(timeout);
    vbs[n++] = new Integer32(retries);
    vbs[n++] = tagList;
    vbs[n++] = params;
    vbs[n++] = new Integer32(storageType);
    vbs[n  ] = new Integer32(RowStatus.active);
    OID index = name.toSubIndex(true);
    SnmpTargetAddrEntryRow row = snmpTargetAddrEntry.createRow(index, vbs);
    snmpTargetAddrEntry.addRow(row);
    return true;
  }

  public MOTableRow removeTargetAddress(OctetString name) {
    OID index = name.toSubIndex(true);
    MOTableRow removedRow = snmpTargetAddrEntry.removeRow(index);
    if (removedRow != null) {
      removeRowFromTargetAddrTagIndex(removedRow);
    }
    return removedRow;
  }

  protected void removeRowFromTargetAddrTagIndex(MOTableRow removedRow) {
    OctetString tagList =
        (OctetString) removedRow.getValue(idxSnmpTargetAddrTagList);
    Set<OctetString> tags = SnmpTagList.getTags(tagList);
    if ((tags != null) && (this.snmpTargetAddrTagIndex != null)) {
      for (OctetString item : tags) {
        Collection<SnmpTargetAddrEntryRow> indexRows = this.snmpTargetAddrTagIndex.get(item);
        if (indexRows != null) {
          synchronized (indexRows) {
            indexRows.remove(removedRow);
            if (indexRows.isEmpty()) {
              this.snmpTargetAddrTagIndex.remove(item);
            }
          }
        }
      }
    }
  }

  public boolean addTargetParams(OctetString name, int mpModel, int secModel,
                                 OctetString secName, int secLevel,
                                 int storageType) {
    Variable[] vbs = new Variable[snmpTargetParamsEntry.getColumnCount()];
    int n=0;
    vbs[n++] = new Integer32(mpModel);
    vbs[n++] = new Integer32(secModel);
    vbs[n++] = secName;
    vbs[n++] = new Integer32(secLevel);
    vbs[n++] = new Integer32(storageType);
    vbs[n  ] = new Integer32(RowStatus.active);
    OID index = name.toSubIndex(true);
    DefaultMOMutableRow2PC row = snmpTargetParamsEntry.createRow(index, vbs);
    snmpTargetParamsEntry.addRow(row);
    return true;
  }

  public MOTableRow removeTargetParams(OctetString name) {
    OID index = name.toSubIndex(true);
    return snmpTargetParamsEntry.removeRow(index);
  }

  private void createSnmpTargetParamsEntry() {
    MOColumn[] snmpTargetParamsEntryColumns = new MOColumn[6];
    snmpTargetParamsEntryColumns[idxSnmpTargetParamsMPModel] =
        new MOMutableColumn(colSnmpTargetParamsMPModel,
                            SMIConstants.SYNTAX_INTEGER,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            null,
                            false);
    ((MOMutableColumn) snmpTargetParamsEntryColumns[idxSnmpTargetParamsMPModel]).
        addMOValueValidationListener(new SnmpTargetParamsMPModelValidator());
    snmpTargetParamsEntryColumns[idxSnmpTargetParamsSecurityModel] =
        new MOMutableColumn(colSnmpTargetParamsSecurityModel,
                            SMIConstants.SYNTAX_INTEGER,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            null,
                            false);
    ((MOMutableColumn) snmpTargetParamsEntryColumns[
     idxSnmpTargetParamsSecurityModel]).
        addMOValueValidationListener(new SnmpTargetParamsSecurityModelValidator());
    snmpTargetParamsEntryColumns[idxSnmpTargetParamsSecurityName] =
        new SnmpAdminString(colSnmpTargetParamsSecurityName,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            null,
                            false);
    snmpTargetParamsEntryColumns[idxSnmpTargetParamsSecurityLevel] =
        new Enumerated<Integer32>(colSnmpTargetParamsSecurityLevel,
                        SMIConstants.SYNTAX_INTEGER32,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        null,
                        false,
                        new int[] {
                        SnmpTargetParamsSecurityLevelEnum.noAuthNoPriv,
                        SnmpTargetParamsSecurityLevelEnum.authPriv,
                        SnmpTargetParamsSecurityLevelEnum.authNoPriv});
    snmpTargetParamsEntryColumns[idxSnmpTargetParamsStorageType] =
        new StorageType(colSnmpTargetParamsStorageType,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(3),
                        true);
    snmpTargetParamsEntryColumns[idxSnmpTargetParamsRowStatus] =
        new RowStatus(colSnmpTargetParamsRowStatus,
                      MOAccessImpl.ACCESS_READ_CREATE);
    ((RowStatus)snmpTargetParamsEntryColumns[idxSnmpTargetParamsRowStatus]).
        addRowStatusListener(new SnmpTargetParamsEntryRowStatusListener());

    snmpTargetParamsEntry =
        new DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>(oidSnmpTargetParamsEntry,
                           snmpTargetParamsEntryIndex,
                           snmpTargetParamsEntryColumns);
    snmpTargetParamsEntryModel = new DefaultMOMutableTableModel<DefaultMOMutableRow2PC>();
    snmpTargetParamsEntryModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    snmpTargetParamsEntry.setModel(snmpTargetParamsEntryModel);
  }

  private void createSnmpTargetAddrEntry() {
    MOColumn[] snmpTargetAddrEntryColumns = new MOColumn[8];
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrTDomain] =
        new MOMutableColumn(colSnmpTargetAddrTDomain,
                            SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            null,
                            false);
    ((MOMutableColumn) snmpTargetAddrEntryColumns[idxSnmpTargetAddrTDomain]).
        addMOValueValidationListener(new SnmpTargetAddrTDomainValidator());
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrTAddress] =
        new MOMutableColumn(colSnmpTargetAddrTAddress,
                            SMIConstants.SYNTAX_OCTET_STRING,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            null,
                            false);
    ((MOMutableColumn) snmpTargetAddrEntryColumns[idxSnmpTargetAddrTAddress]).
        addMOValueValidationListener(new SnmpTargetAddrTAddressValidator());
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrTimeout] =
        new MOMutableColumn(colSnmpTargetAddrTimeout,
                            SMIConstants.SYNTAX_INTEGER,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new Integer32(1500),
                            true);
    ((MOMutableColumn) snmpTargetAddrEntryColumns[idxSnmpTargetAddrTimeout]).
        addMOValueValidationListener(new SnmpTargetAddrTimeoutValidator());
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrRetryCount] =
        new MOMutableColumn(colSnmpTargetAddrRetryCount,
                            SMIConstants.SYNTAX_INTEGER32,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new Integer32(3),
                            true);
    ((MOMutableColumn) snmpTargetAddrEntryColumns[idxSnmpTargetAddrRetryCount]).
        addMOValueValidationListener(new SnmpTargetAddrRetryCountValidator());
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrTagList] =
        new SnmpTagList(colSnmpTargetAddrTagList,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new OctetString(new byte[] {}),
                        true);
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrParams] =
        new MOMutableColumn(colSnmpTargetAddrParams,
                            SMIConstants.SYNTAX_OCTET_STRING,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new OctetString(),
                            true);
    ((MOMutableColumn) snmpTargetAddrEntryColumns[idxSnmpTargetAddrParams]).
        addMOValueValidationListener(new SnmpTargetAddrParamsValidator());
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrStorageType] =
        new StorageType(colSnmpTargetAddrStorageType,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(3),
                        true);
    snmpTargetAddrEntryColumns[idxSnmpTargetAddrRowStatus] =
        new RowStatus(colSnmpTargetAddrRowStatus);
    snmpTargetAddrEntry =
        new DefaultMOTable<SnmpTargetAddrEntryRow,MOColumn,DefaultMOMutableTableModel<SnmpTargetAddrEntryRow>>
            (oidSnmpTargetAddrEntry, snmpTargetAddrEntryIndex, snmpTargetAddrEntryColumns);
    snmpTargetAddrEntryModel = new DefaultMOMutableTableModel<SnmpTargetAddrEntryRow>();
    snmpTargetAddrEntryModel.setRowFactory(new SnmpTargetAddrEntryFactory());
    snmpTargetAddrEntry.setModel(snmpTargetAddrEntryModel);
  }

  public void registerMOs(MOServer server, OctetString context) throws
      DuplicateRegistrationException {
    // Scalar Objects
    server.register(this.snmpTargetSpinLock, context);
    server.register(this.snmpUnknownContexts, context);
    server.register(this.snmpUnavailableContexts, context);
    server.register(this.snmpTargetParamsEntry, context);
    server.register(this.snmpTargetAddrEntry, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpTargetSpinLock, context);
    server.unregister(this.snmpUnknownContexts, context);
    server.unregister(this.snmpUnavailableContexts, context);
    server.unregister(this.snmpTargetParamsEntry, context);
    server.unregister(this.snmpTargetAddrEntry, context);
  }

  class SnmpTargetAddrEntryFactory implements MOTableRowFactory<SnmpTargetAddrEntryRow> {

    public SnmpTargetAddrEntryRow createRow(OID index, Variable[] values) throws
        UnsupportedOperationException {
      return new SnmpTargetAddrEntryRow(index, values);
    }

    public void freeRow(SnmpTargetAddrEntryRow row) {
    }

  }


  class SnmpTargetParamsEntryRowStatusListener implements RowStatusListener {

    public void rowStatusChanged(RowStatusEvent event) {
      if (event.getNewStatus() == RowStatus.destroy) {
        OID index = event.getRow().getIndex();
        OctetString paramsIndex =
            (OctetString) snmpTargetParamsEntryIndex.getIndexValues(index)[0];
        synchronized (snmpTargetAddrEntryModel) {
          for (Iterator<SnmpTargetAddrEntryRow> it = snmpTargetAddrEntryModel.iterator(); it.hasNext(); ) {
            MOTableRow r = it.next();
            Integer32 rowStatus =
                (Integer32) r.getValue(idxSnmpTargetAddrRowStatus);
            if ((rowStatus == null) ||
                (rowStatus.getValue() != RowStatus.active)) {
              continue;
            }
            if (paramsIndex.equals(r.getValue(idxSnmpTargetAddrParams))) {
              event.setDenyReason(SnmpConstants.SNMP_ERROR_INCONSISTENT_VALUE);
              return;
            }
          }
        }
      }
    }

  }

  public class SnmpTargetAddrEntryRow extends DefaultMOMutableRow2PC {
    public SnmpTargetAddrEntryRow(OID index, Variable[] values) {
      super(index, values);
      updateUserObject(this);
    }

    /**
     * Update the user object of the request with the tag list from the
     * supplied change set.
     * @param changeSet
     *    the changeSet with the tags.
     */
    private void updateUserObject(MOTableRow changeSet) {
      Variable tagList = changeSet.getValue(idxSnmpTargetAddrTagList);
      if (tagList != null) {
        @SuppressWarnings("unchecked")
        Set<OctetString> obsolete = (Set<OctetString>) getUserObject();
        Set<OctetString> tags = SnmpTagList.getTags((OctetString)tagList);
        if (obsolete != null) {
          obsolete.removeAll(tags);
        }
        setUserObject(tags);
        updateIndex(obsolete, tags);
      }
    }

    public void commitRow(SubRequest subRequest, MOTableRow changeSet) {
      super.commitRow(subRequest, changeSet);
      updateUserObject(changeSet);
    }

    private void updateIndex(Set<OctetString> remove, Set<OctetString> tags) {
      if (remove != null) {
        for (OctetString next : remove) {
          Collection<SnmpTargetAddrEntryRow> list = snmpTargetAddrTagIndex.get(next);
          if (list != null) {
            synchronized (list) {
              if (!list.remove(this)) {
                logger.error("Inconsistent tag value '" + next +
                    "' for rows: " + list);
              }
            }
          } else {
            logger.error("Tag value '" + next + "' not found in tag index");
          }
        }
      }
      for (OctetString next : tags) {
        Set<SnmpTargetAddrEntryRow> list = snmpTargetAddrTagIndex.get(next);
        if (list == null) {
          list = new HashSet<SnmpTargetAddrEntryRow>();
        }
        synchronized (list) {
          if (!list.add(this)) {
            // make sure this version of the row is part of the index
            list.remove(this);
            list.add(this);
          }
        }
        snmpTargetAddrTagIndex.put(next, list);
      }
    }

    public void prepareRow(SubRequest subRequest, MOTableRow changeSet) {
      OID tdomain =
          (OID) getResultingValue(idxSnmpTargetAddrTDomain, changeSet);
      OctetString taddress =
          (OctetString) getResultingValue(idxSnmpTargetAddrTAddress,
                                          changeSet);
      if (tdomain != null) {
        TDomainAddressFactory factory =
            supportedAddressClasses.get(tdomain);
        if ((factory == null) || (!factory.isValidAddress(tdomain, taddress))) {
          subRequest.getStatus().
              setErrorStatus(SnmpConstants.SNMP_ERROR_INCONSISTENT_VALUE);
        }
      }
      else if (taddress != null) {
        subRequest.getStatus().
            setErrorStatus(SnmpConstants.SNMP_ERROR_INCONSISTENT_VALUE);
      }
    }

    public Address getAddress() {
      OID tdomain = (OID) getValue(idxSnmpTargetAddrTDomain);
      TDomainAddressFactory factory = supportedAddressClasses.get(tdomain);
      if (factory != null) {
        OctetString addr =  (OctetString) getValue(idxSnmpTargetAddrTAddress);
        return factory.createAddress(tdomain, addr);
      }
      return null;
    }

    public OctetString getTAddress(Address address) {
      OID tdomain = (OID) getValue(idxSnmpTargetAddrTDomain);
      TDomainAddressFactory factory = supportedAddressClasses.get(tdomain);
      OID[] domains = factory.getTransportDomain(address);
      for (OID domain : domains) {
        if (domain.equals(tdomain)) {
          return factory.getAddress(address);
        }
      }
      return null;
    }

    public Target getTarget(OctetString contextEngineID,
                            OctetString contextName) {
      Address addr = getAddress();
      OctetString addrParams =
          (OctetString) getValue(idxSnmpTargetAddrParams);
      OID paramsIndex = addrParams.toSubIndex(true);
      MOTableRow paramsRow = snmpTargetParamsEntryModel.getRow(paramsIndex);
      if (paramsRow == null) {
        return null;
      }
      Target t = null;
      if (paramsRow.getValue(idxSnmpTargetParamsMPModel).toInt() ==
          MPv3.ID) {
        SnmpTlsTmMib myTlsTmMib = tlsTmMib;
        if (myTlsTmMib != null) {

          SnmpTlsTmMib.SnmpTlstmAddrEntryRow tlstmAddrEntryRow =
              (SnmpTlsTmMib.SnmpTlstmAddrEntryRow)
                  myTlsTmMib.getSnmpTlstmAddrEntry().getModel().getRow(index);
          if (tlstmAddrEntryRow != null) {
            t = new CertifiedTarget(addr,
                tlstmAddrEntryRow.getSnmpTlstmAddrServerIdentity(),
                tlstmAddrEntryRow.getSnmpTlstmAddrServerFingerprint(),
                null
                );

          }
        }
        if (t == null) {
          t = new UserTarget(addr,
                             (OctetString)
                             paramsRow.getValue(idxSnmpTargetParamsSecurityName),
                             contextEngineID.getValue(),
                             paramsRow.getValue(idxSnmpTargetParamsSecurityLevel).
                             toInt());
        }
      }
      else {
        OctetString community =
            (OctetString) paramsRow.getValue(idxSnmpTargetParamsSecurityName);
        if (coexistenceProvider != null) {
          community = coexistenceProvider.getCommunity(community,
              contextEngineID, contextName);
          if (community == null) {
            return null;
          }
        }
        t = new CommunityTarget(addr, community);
      }
      t.setRetries(getValue(idxSnmpTargetAddrRetryCount).toInt());
      t.setTimeout(getValue(idxSnmpTargetAddrTimeout).toInt()*10);
      t.setVersion(paramsRow.getValue(idxSnmpTargetParamsMPModel).toInt());
      return t;
    }
  }

  // Value Validators

  /**
   * The <code>SnmpTargetParamsMPModelValidator</code> implements the value
   * validation for <code>SnmpTargetParamsMPModel</code>.
   */
  class SnmpTargetParamsMPModelValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      int v = ((Integer32) newValue).getValue();
      if (messageDispatcher.getMessageProcessingModel(v) == null) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_VALUE);
        return;
      }
      //--AgentGen BEGIN=snmpTargetParamsMPModel::validate
      //--AgentGen END
    }
  }

  /**
   * The <code>SnmpTargetParamsSecurityModelValidator</code> implements the value validation
   * for <code>SnmpTargetParamsSecurityModel</code>.
   */
  static class SnmpTargetParamsSecurityModelValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      //--AgentGen BEGIN=snmpTargetParamsSecurityModel::validate
      Variable newValue = validationEvent.getNewValue();
      switch (((Integer32)newValue).getValue()) {
        case SecurityModel.SECURITY_MODEL_USM: {
          if (SecurityModels.getInstance().
              getSecurityModel((Integer32) newValue) == null) {
            validationEvent.setValidationStatus(SnmpConstants.
                                                SNMP_ERROR_WRONG_VALUE);
            return;
          }
          break;
        }
        default:
          break;
      }
      //--AgentGen END
    }
  }

  /**
   * The <code>SnmpTargetAddrTDomainValidator</code> implements the value validation
   * for <code>SnmpTargetAddrTDomain</code>.
   */
  class SnmpTargetAddrTDomainValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      //--AgentGen BEGIN=snmpTargetAddrTDomain::validate
      if (newValue instanceof OID) {
        OID tdomain = (OID)newValue;
        if (!supportedAddressClasses.containsKey(tdomain)) {
          validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_BAD_VALUE);
        }
      }
      else {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_TYPE);
      }
      //--AgentGen END
    }
  }

  /**
   * The <code>SnmpTargetAddrTAddressValidator</code> implements the value
   * validation for <code>SnmpTargetAddrTAddress</code>.
   */
  static class SnmpTargetAddrTAddressValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString) newValue;
      if (!(((os.length() >= 1) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_LENGTH);
        return;
      }
      //--AgentGen BEGIN=snmpTargetAddrTAddress::validate
      //--AgentGen END
    }
  }

  /**
   * The <code>SnmpTargetAddrTimeoutValidator</code> implements the value
   * validation for <code>SnmpTargetAddrTimeout</code>.
   */
  static class SnmpTargetAddrTimeoutValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      long v = ((Integer32) newValue).getValue();
      if (!(((v >= 0L) /*&& (v <= 2147483647L)*/))) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_VALUE);
        return;
      }
      //--AgentGen BEGIN=snmpTargetAddrTimeout::validate
      //--AgentGen END
    }
  }

  /**
   * The <code>SnmpTargetAddrRetryCountValidator</code> implements the value validation
   * for <code>SnmpTargetAddrRetryCount</code>.
   */
  static class SnmpTargetAddrRetryCountValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      long v = ((Integer32) newValue).getValue();
      if (!(((v >= 0L) && (v <= 255L)))) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_VALUE);
        return;
      }
      //--AgentGen BEGIN=snmpTargetAddrRetryCount::validate
      //--AgentGen END
    }
  }

  /**
   * The <code>SnmpTargetAddrParamsValidator</code> implements the value validation
   * for <code>SnmpTargetAddrParams</code>.
   */
  class SnmpTargetAddrParamsValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString) newValue;
      if (!(((os.length() >= 1) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_LENGTH);
        return;
      }
      //--AgentGen BEGIN=snmpTargetAddrParams::validate
      OID paramsIndexOID = os.toSubIndex(true);
      MOTableRow paramsRow =
          snmpTargetParamsEntryModel.getRow(paramsIndexOID);
      if (paramsRow == null) {
        validationEvent.
            setValidationStatus(SnmpConstants.SNMP_ERROR_INCONSISTENT_VALUE);
      }
      //--AgentGen END
    }
  }

  public void incrementCounter(CounterEvent event) {
    if (event.getOid().equals(snmpUnavailableContexts.getOid())) {
      ((Counter32) snmpUnavailableContexts.getValue()).increment();
      event.setCurrentValue(snmpUnavailableContexts.getValue());
    }
    else if (event.getOid().equals(snmpUnknownContexts.getOid())) {
      ((Counter32) snmpUnknownContexts.getValue()).increment();
      event.setCurrentValue(snmpUnknownContexts.getValue());
    }
  }

 // Enumerations

  public static final class SnmpTargetParamsSecurityLevelEnum {
    public static final int noAuthNoPriv = 1;
    public static final int authNoPriv = 2;
    public static final int authPriv = 3;
  }

//--AgentGen BEGIN=_CLASSES

  public void rowChanged(MOTableRowEvent event) {
    if ((event.getType() == MOTableRowEvent.DELETE) &&
        (event.getRow() instanceof SnmpTargetAddrEntryRow)) {
      Variable[] vbs = new Variable[event.getRow().size()];
      vbs[idxSnmpTargetAddrTagList] = new OctetString();
      MOTableRow dummyRow =
          new DefaultMOTableRow(event.getRow().getIndex(), vbs);
      ((SnmpTargetAddrEntryRow)event.getRow()).updateUserObject(dummyRow);
    }
  }

  /**
   * Returns the SNMP Target Parameters row for the specified name.
   * @param paramsName
   *    the name of the parameters set to return.
   * @return
   *    if the row containing the target parameters if such an entry
   *    exists or <code>null</code> if no such entry exists
   */
  public MOTableRow getTargetParamsRow(OctetString paramsName) {
    if (paramsName == null) {
      return null;
    }
    OID paramsIndex = paramsName.toSubIndex(true);
    return snmpTargetParamsEntryModel.getRow(paramsIndex);
  }

  /**
   * Returns the SNMP Target Parameters row for the specified name.
   * @param paramsName
   *    the name of the parameters set to return.
   * @param activeOnly
   *    if <code>true</code> only an active row will be returned.
   * @return
   *    if the row containing the target parameters if such an entry
   *    exists or <code>null</code> if no such entry exists
   */
  public MOTableRow getTargetParamsRow(OctetString paramsName,
                                       boolean activeOnly) {
    MOTableRow row = getTargetParamsRow(paramsName);
    if (activeOnly && (row != null)) {
      if (((Integer32)row.getValue(idxSnmpTargetParamsRowStatus)).getValue()
          != RowStatus.active) {
        return null;
      }
    }
    return row;
  }

  public DefaultMOTable<SnmpTargetAddrEntryRow,? extends MOColumn,? extends MOTableModel<SnmpTargetAddrEntryRow>>
    getSnmpTargetAddrEntry() {
    return snmpTargetAddrEntry;
  }

  public DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
    getSnmpTargetParamsEntry() {
    return snmpTargetParamsEntry;
  }

  public CoexistenceInfoProvider getCoexistenceProvider() {
    return coexistenceProvider;
  }

  public void setCoexistenceProvider(CoexistenceInfoProvider
                                     coexistenceProvider) {
    this.coexistenceProvider = coexistenceProvider;
  }
  //--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}
