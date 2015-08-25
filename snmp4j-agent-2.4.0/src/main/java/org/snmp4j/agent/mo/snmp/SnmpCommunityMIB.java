/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpCommunityMIB.java  
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

import org.snmp4j.agent.mo.snmp.tc.SnmpEngineIDTC;
import org.snmp4j.agent.mo.util.RowIndexComparator;
import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB.SnmpTargetAddrEntryRow;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class SnmpCommunityMIB

//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
    , CoexistenceInfoProvider
//--AgentGen END
{

  // Factory
  private static MOFactory moFactory = DefaultMOFactory.getInstance();

  // Constants

  public static final OID oidSnmpCommunityEntry =
    new OID(new int[] { 1,3,6,1,6,3,18,1,1,1 });

  // Column sub-identifier definitions for snmpCommunityEntry:
  public static final int colSnmpCommunityName = 2;
  public static final int colSnmpCommunitySecurityName = 3;
  public static final int colSnmpCommunityContextEngineID = 4;
  public static final int colSnmpCommunityContextName = 5;
  public static final int colSnmpCommunityTransportTag = 6;
  public static final int colSnmpCommunityStorageType = 7;
  public static final int colSnmpCommunityStatus = 8;

  // Column index definitions for snmpCommunityEntry:
  public static final int idxSnmpCommunityName = 0;
  public static final int idxSnmpCommunitySecurityName = 1;
  public static final int idxSnmpCommunityContextEngineID = 2;
  public static final int idxSnmpCommunityContextName = 3;
  public static final int idxSnmpCommunityTransportTag = 4;
  public static final int idxSnmpCommunityStorageType = 5;
  public static final int idxSnmpCommunityStatus = 6;
  private static MOTableSubIndex[] snmpCommunityEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpCommunityEntryIndex =
      moFactory.createIndex(snmpCommunityEntryIndexes,
                            true);


  private MOTable<SnmpCommunityEntryRow,MOColumn,MOMutableTableModel<SnmpCommunityEntryRow>>
      snmpCommunityEntry;
  private MOMutableTableModel<SnmpCommunityEntryRow> snmpCommunityEntryModel;
  public static final OID oidSnmpTargetAddrExtEntry =
    new OID(new int[] { 1,3,6,1,6,3,18,1,2,1 });

  // Column sub-identifier definitions for snmpTargetAddrExtEntry:
  public static final int colSnmpTargetAddrTMask = 1;
  public static final int colSnmpTargetAddrMMS = 2;

  // Column index definitions for snmpTargetAddrExtEntry:
  public static final int idxSnmpTargetAddrTMask = 0;
  public static final int idxSnmpTargetAddrMMS = 1;
  private static MOTableSubIndex[] snmpTargetAddrExtEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpTargetAddrExtEntryIndex =
      moFactory.createIndex(snmpTargetAddrExtEntryIndexes,
                            true);


  private MOTable<SnmpTargetAddrExtEntryRow,MOColumn,MOMutableTableModel<SnmpTargetAddrExtEntryRow>>
      snmpTargetAddrExtEntry;
  private MOMutableTableModel<SnmpTargetAddrExtEntryRow> snmpTargetAddrExtEntryModel;

  private MOTableRelation<SnmpTargetAddrEntryRow,SnmpTargetAddrExtEntryRow> snmpTargetAddrEntryRelation;

//--AgentGen BEGIN=_MEMBERS
  private static final LogAdapter logger =
      LogFactory.getLogger(SnmpCommunityMIB.class);

  private Map<Variable, SortedSet<SnmpCommunityEntryRow>> coexistenceInfo;
  private Map<Variable, List<SnmpCommunityEntryRow>> communityInfo;
  private SnmpTargetMIB targetMIB;
  private boolean sourceAddressFiltering;
  private SnmpEngineIDTC snmpEngineIDTC;

  public SnmpCommunityMIB(SnmpTargetMIB targetMIB) {
    this(new OctetString(targetMIB.getLocalEngineID()));
    this.targetMIB = targetMIB;
    setBaseTableSnmpTargetAddrEntry(targetMIB.getSnmpTargetAddrEntry());
  }

//--AgentGen END

  private SnmpCommunityMIB(OctetString localEngineID) {
    this.snmpEngineIDTC = new SnmpEngineIDTC(localEngineID);
    createSnmpCommunityEntry();
    createSnmpTargetAddrExtEntry();
  }


  public MOTable<SnmpCommunityEntryRow,MOColumn,MOMutableTableModel<SnmpCommunityEntryRow>> getSnmpCommunityEntry() {
    return snmpCommunityEntry;
  }


  private void createSnmpCommunityEntry() {
    MOColumn[] snmpCommunityEntryColumns = new MOColumn[7];
    snmpCommunityEntryColumns[idxSnmpCommunityName] =
      new MOMutableColumn(colSnmpCommunityName,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    snmpCommunityEntryColumns[idxSnmpCommunitySecurityName] =
      new MOMutableColumn(colSnmpCommunitySecurityName,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpCommunityEntryColumns[idxSnmpCommunitySecurityName]).
      addMOValueValidationListener(new SnmpCommunitySecurityNameValidator());
    snmpCommunityEntryColumns[idxSnmpCommunityContextEngineID] =
        snmpEngineIDTC.createColumn(colSnmpCommunityContextEngineID,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpCommunityEntryColumns[idxSnmpCommunityContextEngineID]).
      addMOValueValidationListener(new SnmpCommunityContextEngineIDValidator());
    snmpCommunityEntryColumns[idxSnmpCommunityContextName] =
      new MOMutableColumn(colSnmpCommunityContextName,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString(new byte[] {  }),
                          true);
    ((MOMutableColumn)snmpCommunityEntryColumns[idxSnmpCommunityContextName]).
      addMOValueValidationListener(new SnmpCommunityContextNameValidator());
    snmpCommunityEntryColumns[idxSnmpCommunityTransportTag] =
      new MOMutableColumn(colSnmpCommunityTransportTag,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString(new byte[] {  }),
                          true);
    ((MOMutableColumn)snmpCommunityEntryColumns[idxSnmpCommunityTransportTag]).
      addMOValueValidationListener(new SnmpCommunityTransportTagValidator());
    snmpCommunityEntryColumns[idxSnmpCommunityStorageType] =
      new StorageType(colSnmpCommunityStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      null,
                      true);
    snmpCommunityEntryColumns[idxSnmpCommunityStatus] =
      new RowStatus(colSnmpCommunityStatus);

    snmpCommunityEntryModel = new DefaultMOMutableTableModel<SnmpCommunityEntryRow>();
    snmpCommunityEntryModel.setRowFactory(new SnmpCommunityEntryRowFactory());
    snmpCommunityEntry =
      moFactory.createTable(oidSnmpCommunityEntry,
                            snmpCommunityEntryIndex,
                            snmpCommunityEntryColumns,
                            snmpCommunityEntryModel);
  }

  public MOTable getSnmpTargetAddrExtEntry() {
    return snmpTargetAddrExtEntry;
  }

  public void setBaseTableSnmpTargetAddrEntry(MOTable<SnmpTargetAddrEntryRow,? extends MOColumn,
      ? extends MOTableModel<SnmpTargetAddrEntryRow>> baseTable) {
     snmpTargetAddrEntryRelation =
       moFactory.createTableRelation(baseTable, snmpTargetAddrExtEntry);
     snmpTargetAddrEntryRelation.createRelationShip();
  }

  private void createSnmpTargetAddrExtEntry() {
    MOColumn[] snmpTargetAddrExtEntryColumns = new MOColumn[2];
    snmpTargetAddrExtEntryColumns[idxSnmpTargetAddrTMask] =
      new MOMutableColumn(colSnmpTargetAddrTMask,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString(new byte[] {  }),
                          true);
    ((MOMutableColumn)snmpTargetAddrExtEntryColumns[idxSnmpTargetAddrTMask]).
      addMOValueValidationListener(new SnmpTargetAddrTMaskValidator());
    snmpTargetAddrExtEntryColumns[idxSnmpTargetAddrMMS] =
      new MOMutableColumn(colSnmpTargetAddrMMS,
                          SMIConstants.SYNTAX_INTEGER32,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new Integer32(484),
                          true);
    ((MOMutableColumn)snmpTargetAddrExtEntryColumns[idxSnmpTargetAddrMMS]).
      addMOValueValidationListener(new SnmpTargetAddrMMSValidator());

    snmpTargetAddrExtEntryModel = new DefaultMOMutableTableModel<SnmpTargetAddrExtEntryRow>();
    snmpTargetAddrExtEntryModel.setRowFactory(new SnmpTargetAddrExtEntryRowFactory());
    snmpTargetAddrExtEntry =
      moFactory.createTable(oidSnmpTargetAddrExtEntry,
                            snmpTargetAddrExtEntryIndex,
                            snmpTargetAddrExtEntryColumns,
                            snmpTargetAddrExtEntryModel);
  }


  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.snmpCommunityEntry, context);
    server.register(this.snmpTargetAddrExtEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpCommunityEntry, context);
    server.unregister(this.snmpTargetAddrExtEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars

  // Value Validators

  /**
   * The <code>SnmpCommunitySecurityNameValidator</code> implements the value
   * validation for <code>SnmpCommunitySecurityName</code>.
   */
  static class SnmpCommunitySecurityNameValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 1) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpCommunitySecurityName::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpCommunityContextEngineIDValidator</code> implements the value
   * validation for <code>SnmpCommunityContextEngineID</code>.
   */
  static class SnmpCommunityContextEngineIDValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 5) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpCommunityContextEngineID::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpCommunityContextNameValidator</code> implements the value
   * validation for <code>SnmpCommunityContextName</code>.
   */
  static class SnmpCommunityContextNameValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpCommunityContextName::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpCommunityTransportTagValidator</code> implements the value
   * validation for <code>SnmpCommunityTransportTag</code>.
   */
  static class SnmpCommunityTransportTagValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpCommunityTransportTag::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTargetAddrTMaskValidator</code> implements the value
   * validation for <code>SnmpTargetAddrTMask</code>.
   */
  static class SnmpTargetAddrTMaskValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpTargetAddrTMask::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpTargetAddrMMSValidator</code> implements the value
   * validation for <code>SnmpTargetAddrMMS</code>.
   */
  static class SnmpTargetAddrMMSValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      long v = ((Integer32)newValue).getValue();
      if (!(((v >= 0L) && (v <= 0L)) ||
          ((v >= 484L)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_VALUE);
        return;
      }
     //--AgentGen BEGIN=snmpTargetAddrMMS::validate
     //--AgentGen END
    }
  }



  // Rows and Factories
  class SnmpCommunityEntryRowFactory
        implements MOTableRowFactory<SnmpCommunityEntryRow>
  {
    public synchronized SnmpCommunityEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      SnmpCommunityEntryRow row = new SnmpCommunityEntryRow(index, values);
      //--AgentGen BEGIN=snmpCommunityEntry::createRow
      if (coexistenceInfo == null) {
        coexistenceInfo = Collections.synchronizedMap(new HashMap<Variable, SortedSet<SnmpCommunityEntryRow>>());
      }
      SortedSet<SnmpCommunityEntryRow> cinfos =
          coexistenceInfo.get(values[idxSnmpCommunityName]);
      if (cinfos == null) {
        cinfos = new TreeSet<SnmpCommunityEntryRow>(new RowIndexComparator());
        coexistenceInfo.put(values[idxSnmpCommunityName], cinfos);
      }
      cinfos.add(row);
      if (communityInfo == null) {
        communityInfo = Collections.synchronizedMap(new HashMap<Variable, List<SnmpCommunityEntryRow>>());
      }
      List<SnmpCommunityEntryRow> l = communityInfo.get(values[idxSnmpCommunitySecurityName]);
      if (l == null) {
        l = new LinkedList<SnmpCommunityEntryRow>();
        communityInfo.put(values[idxSnmpCommunitySecurityName], l);
      }
      l.add(row);
      //--AgentGen END
      return row;
    }

    public synchronized void freeRow(SnmpCommunityEntryRow row) {
     //--AgentGen BEGIN=snmpCommunityEntry::freeRow
     SortedSet<SnmpCommunityEntryRow> cinfos =
         coexistenceInfo.get(row.getValue(idxSnmpCommunityName));
     if (cinfos != null) {
       cinfos.remove(row);
       if (cinfos.isEmpty()) {
         coexistenceInfo.remove(row.getValue(idxSnmpCommunityName));
       }
     }
     List<SnmpCommunityEntryRow> l =
         communityInfo.get(row.getValue(idxSnmpCommunitySecurityName));
     if (l != null) {
       l.remove(row);
       if (l.size() == 0) {
         communityInfo.remove(row.getValue(idxSnmpCommunitySecurityName));
       }
     }
     //--AgentGen END
    }
  }

  public class SnmpCommunityEntryRow extends DefaultMOMutableRow2PC {
    public SnmpCommunityEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public OctetString getSnmpCommunityName() {
      return (OctetString) getValue(idxSnmpCommunityName);
    }

    public void setSnmpCommunityName(OctetString newValue) {
      setValue(idxSnmpCommunityName, newValue);
    }

    @Override
    public void setValue(int column, Variable value) {
      if (column == idxSnmpCommunityName) {
        SortedSet<SnmpCommunityEntryRow> cinfos = coexistenceInfo.get(getSnmpCommunityName());
        if (cinfos != null) {
          for (SnmpCommunityEntryRow row : cinfos) {
            if (row.getIndex().equals(getIndex())) {
              cinfos.remove(row);
              break;
            }
          }
        }
      }
      super.setValue(column, value);
      if (column == idxSnmpCommunityName) {
        SortedSet<SnmpCommunityEntryRow> cinfos = coexistenceInfo.get(value);
        if (cinfos == null) {
          cinfos = new TreeSet<SnmpCommunityEntryRow>();
          coexistenceInfo.put(values[idxSnmpCommunityName], cinfos);
        }
        cinfos.add(this);
      }
    }

    public OctetString getSnmpCommunitySecurityName() {
      return (OctetString) getValue(idxSnmpCommunitySecurityName);
    }

    public void setSnmpCommunitySecurityName(OctetString newValue) {
      setValue(idxSnmpCommunitySecurityName, newValue);
    }

    public OctetString getSnmpCommunityContextEngineID() {
      return (OctetString) getValue(idxSnmpCommunityContextEngineID);
    }

    public void setSnmpCommunityContextEngineID(OctetString newValue) {
      setValue(idxSnmpCommunityContextEngineID, newValue);
    }

    public OctetString getSnmpCommunityContextName() {
      return (OctetString) getValue(idxSnmpCommunityContextName);
    }

    public void setSnmpCommunityContextName(OctetString newValue) {
      setValue(idxSnmpCommunityContextName, newValue);
    }

    public OctetString getSnmpCommunityTransportTag() {
      return (OctetString) getValue(idxSnmpCommunityTransportTag);
    }

    public void setSnmpCommunityTransportTag(OctetString newValue) {
      setValue(idxSnmpCommunityTransportTag, newValue);
    }

    public Integer32 getSnmpCommunityStorageType() {
      return (Integer32) getValue(idxSnmpCommunityStorageType);
    }

    public void setSnmpCommunityStorageType(Integer32 newValue) {
      setValue(idxSnmpCommunityStorageType, newValue);
    }

    public Integer32 getSnmpCommunityStatus() {
      return (Integer32) getValue(idxSnmpCommunityStatus);
    }

    public void setSnmpCommunityStatus(Integer32 newValue) {
      setValue(idxSnmpCommunityStatus, newValue);
    }


     //--AgentGen BEGIN=snmpCommunityEntry::RowFactory
     //--AgentGen END
  }

  class SnmpTargetAddrExtEntryRowFactory
        implements MOTableRowFactory<SnmpTargetAddrExtEntryRow>
  {
    public SnmpTargetAddrExtEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      SnmpTargetAddrExtEntryRow row = new SnmpTargetAddrExtEntryRow(index, values);
     //--AgentGen BEGIN=snmpTargetAddrExtEntry::createRow
     //--AgentGen END
      return row;
    }

    public void freeRow(SnmpTargetAddrExtEntryRow row) {
     //--AgentGen BEGIN=snmpTargetAddrExtEntry::freeRow
     //--AgentGen END
    }
  }

  class SnmpTargetAddrExtEntryRow extends DefaultMOMutableRow2PC {
    public SnmpTargetAddrExtEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public OctetString getSnmpTargetAddrTMask() {
      return (OctetString) getValue(idxSnmpTargetAddrTMask);
    }

    public void setSnmpTargetAddrTMask(OctetString newValue) {
      setValue(idxSnmpTargetAddrTMask, newValue);
    }

    public Integer32 getSnmpTargetAddrMMS() {
      return (Integer32) getValue(idxSnmpTargetAddrMMS);
    }

    public void setSnmpTargetAddrMMS(Integer32 newValue) {
      setValue(idxSnmpTargetAddrMMS, newValue);
    }


     //--AgentGen BEGIN=snmpTargetAddrExtEntry::RowFactory
     //--AgentGen END
  }



//--AgentGen BEGIN=_METHODS

  public CoexistenceInfo[] getCoexistenceInfo(OctetString community) {
    if (logger.isDebugEnabled()) {
      logger.debug("Looking up coexistence info for '"+community+"'");
    }
    if (coexistenceInfo == null) {
      return null;
    }
    SortedSet<SnmpCommunityEntryRow> cinfos = coexistenceInfo.get(community);
    if (cinfos != null) {
      // make a copy to improve consistency on concurrent access
      SortedSet<SnmpCommunityEntryRow> cinfosCopy = new TreeSet<SnmpCommunityEntryRow>(cinfos);
      ArrayList<CoexistenceInfo> infos = new ArrayList<CoexistenceInfo>(cinfosCopy.size());
      for (SnmpCommunityEntryRow row : cinfosCopy) {
        // check if cache is up-to-date:
        SnmpCommunityEntryRow modelRow = getSnmpCommunityEntry().getModel().getRow(row.getIndex());
        if (modelRow != row) {
          cinfos.remove(row);
          if (modelRow == null) {
            continue;
          }
          cinfos.add(modelRow);
          row = modelRow;
        }
        if (row.getSnmpCommunityStatus() == null || row.getSnmpCommunityStatus().getValue() != RowStatus.active) {
          continue;
        }
        CoexistenceInfo coexistenceInfo = new CoexistenceInfo(row.getSnmpCommunitySecurityName(),
            row.getSnmpCommunityContextEngineID(),
            row.getSnmpCommunityContextName(),
            row.getSnmpCommunityTransportTag());
        infos.add(coexistenceInfo);
        if (logger.isDebugEnabled()) {
          logger.debug("Found coexistence info for '"+community+"'="+coexistenceInfo);
        }
      }
      return infos.toArray(new CoexistenceInfo[infos.size()]);
    }
    return null;
  }

  /**
   * Checks whether the supplied address passes the source address filter
   * configured for the supplied transport tag. The tag identifies a set of
   * addresses configured in the snmpTargetAddrTable which is extended by
   * the snmpTargetAddrExtTable. The transport address mask allows entries
   * in the snmpTargetAddrTable to define a set of addresses instead of
   * just a single address.
   *
   * @param address
   *    the address of the incoming packet to check.
   * @param coexistenceInfo
   *    a set of coexistence information that provides the transport tag that
   *    is used to identify allowed source addresses. On return, the maximum
   *    message size attribute of the coexistence info set will be set
   *    according to the values defined for the matched source address in
   *    the snmpTargetAddrExtTable.
   * @return boolean
   */
  public boolean passesFilter(Address address,
                              CoexistenceInfo coexistenceInfo) {
    if (!isSourceAddressFiltering()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Address "+address+
                     " passes filter, "+
                     "because source address filtering is disabled");
      }
      return true;
    }
    if ((coexistenceInfo.getTransportTag() == null) ||
        (coexistenceInfo.getTransportTag().length() == 0)) {
      if (logger.isDebugEnabled()) {
        logger.debug("Address "+address+
                     " passes filter, because transportTag is null");
      }
      return true;
    }
    Collection matches =
        targetMIB.getTargetAddrRowsForTag(coexistenceInfo.getTransportTag());
    for (Object match : matches) {
      SnmpTargetAddrEntryRow row = (SnmpTargetAddrEntryRow) match;
      SnmpTargetAddrExtEntryRow extRow =
          snmpTargetAddrExtEntryModel.getRow(row.getIndex());
      OctetString filterTAddress =
          (OctetString) row.getValue(SnmpTargetMIB.idxSnmpTargetAddrTAddress);
      if (extRow != null) {
        OctetString mask = extRow.getSnmpTargetAddrTMask();
        OctetString matchAddress = row.getTAddress(address);
        if ((mask.length() == 0) &&
            (matchAddress != null) && (matchAddress.equals(filterTAddress))) {
          coexistenceInfo.setMaxMessageSize(
              extRow.getSnmpTargetAddrMMS().getValue());
          return true;
        } else if ((matchAddress != null) &&
            (mask.length() == matchAddress.length()) &&
            (matchAddress.length() == filterTAddress.length())) {
          OctetString maskedFilterTAddress = filterTAddress.mask(mask);
          OctetString maskedMatchTAddress = matchAddress.mask(mask);
          if (maskedFilterTAddress.equals(maskedMatchTAddress)) {
            coexistenceInfo.setMaxMessageSize(
                extRow.getSnmpTargetAddrMMS().getValue());
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Enables or disables source address filtering for incoming SNMPv1 or SNMPv2c
   * packets. By default source address filtering is disabled! If enabled, only
   * messages received for community based security models are processed, that
   * match an address in the snmpTargetAddressTable with the transport tag
   * associated with the community used.
   *
   * @param sourceAddressFiltering
   *    if <code>true</code> source address filtering is enabled, otherwise
   *    it is disabled.
   */
  public void setSourceAddressFiltering(boolean sourceAddressFiltering) {
    this.sourceAddressFiltering = sourceAddressFiltering;
  }

  /**
   * Returns <code>true</code> if source address filtering is enabled. In that
   * case a call to {@link #passesFilter} will always return <code>true</code>.
   * @return
   *    <code>true</code> if source address filtering is enabled,
   *    <code>false</code> otherwise.
   */
  public boolean isSourceAddressFiltering() {
    return sourceAddressFiltering;
  }

  public OctetString getCommunity(OctetString securityName,
                                  OctetString contextEngineID,
                                  OctetString contextName) {
    if (communityInfo == null) {
      return null;
    }
    List<SnmpCommunityEntryRow> l = communityInfo.get(securityName);
    if (l != null) {
      for (SnmpCommunityEntryRow row : l) {
        if (row.getSnmpCommunityStatus().getValue() == RowStatus.active) {
          if (((contextEngineID == null) ||
              (contextEngineID.equals(row.getSnmpCommunityContextEngineID()))) &&
              ((row.getSnmpCommunityContextName().equals(contextName)))) {
            return row.getSnmpCommunityName();
          }
        }
      }
    }
    return null;
  }

  /**
   * Adds an entry to the SnmpCommunityTable and a corresponding coexistence information entry in the internal
   * cache.
   * @param indexName
   *    the ID of the entry (max. 32 bytes long).
   * @param community
   *    the community string for which a row in this table represents a configuration.
   * @param securityName
   *    A human readable string representing the corresponding value of snmpCommunityName in a Security Model
   *    independent format.
   * @param contextEngineID
   *    The contextEngineID indicating the location of the context in which management information is accessed
   *    when using the community string specified by the corresponding instance of snmpCommunityName.
   * @param contextName
   *    The context in which management information is accessed when using the community string specified by the
   *    corresponding instance of snmpCommunityName.
   * @param communityTransportTag
   *    This object specifies a set of transport endpoints from which a command responder application will accept
   *    management requests.  If a management request containing this community is received on a transport endpoint
   *    other than the transport endpoints identified by this object, the request is deemed unauthentic.
   *    The transports identified by this object are specified in the snmpTargetAddrTable.  Entries in that table
   *    whose snmpTargetAddrTagList contains this tag value are identified.
   *    If the value of this object has zero-length, transport endpoints are not checked when authenticating messages
   *    containing this community string.
   * @param storageType
   *    The storage type for this conceptual row in the snmpCommunityTable.  Conceptual rows having the value
   *    'permanent' need not allow write-access to any columnar object in the row.
   * @since 2.3.1
   */
  public void addSnmpCommunityEntry(OctetString indexName, OctetString community, OctetString securityName,
                                    OctetString contextEngineID, OctetString contextName,
                                    OctetString communityTransportTag, int storageType) {
    if (indexName.length() > 32) {
      throw new IllegalArgumentException("The length of the snmpCommunityIndex ("+indexName.length()+
          ") exceeds the maximum of 32 bytes");
    }
    snmpCommunityEntry.addRow(snmpCommunityEntry.createRow(indexName.toSubIndex(true), new Variable[] {
        community,
        securityName,
        contextEngineID,
        contextName,
        communityTransportTag,
        new Integer32(storageType),
        new Integer32(RowStatus.active)
    }));
  }

  /**
   * Removes a community entry from the SnmpCommunityTable and also removes the corresponding coexistence
   * information from the cache.
   * @param indexName
   *   the ID of the entry.
   * @return
   *    <code<true/code> if the row has been found and removed, <code>false</code> otherwise.
   * @since 2.3.1
   */
  public boolean removeSnmpCommuntiyEntry(OctetString indexName) {
    SnmpCommunityEntryRow removedRow = snmpCommunityEntry.removeRow(indexName.toSubIndex(true));
    if (removedRow != null) {
      snmpCommunityEntry.getModel().getRowFactory().freeRow(removedRow);
      return true;
    }
    return false;
  }

  //--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


