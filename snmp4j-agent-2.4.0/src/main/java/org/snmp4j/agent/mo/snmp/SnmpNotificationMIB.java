/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpNotificationMIB.java  
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

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class SnmpNotificationMIB implements MOGroup {

  private static final LogAdapter logger =
      LogFactory.getLogger(SnmpNotificationMIB.class);

  // Constants

  public static final OID oidSnmpNotifyEntry =
    new OID(new int[] { 1,3,6,1,6,3,13,1,1,1 });

  // Column sub-identifer defintions for snmpNotifyEntry:
  public static final int colSnmpNotifyTag = 2;
  public static final int colSnmpNotifyType = 3;
  public static final int colSnmpNotifyStorageType = 4;
  public static final int colSnmpNotifyRowStatus = 5;

  // Column index defintions for snmpNotifyEntry:
  public static final int idxSnmpNotifyTag = 0;
  public static final int idxSnmpNotifyType = 1;
  public static final int idxSnmpNotifyStorageType = 2;
  public static final int idxSnmpNotifyRowStatus = 3;

  private static MOTableSubIndex[] snmpNotifyEntryIndexes =
    new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpNotifyEntryIndex =
      new MOTableIndex(snmpNotifyEntryIndexes, true);


  private DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>> snmpNotifyEntry;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> snmpNotifyEntryModel;
  public static final OID oidSnmpNotifyFilterEntry =
    new OID(new int[] { 1,3,6,1,6,3,13,1,3,1 });

  // Column sub-identifer defintions for snmpNotifyFilterEntry:
  public static final int colSnmpNotifyFilterMask = 2;
  public static final int colSnmpNotifyFilterType = 3;
  public static final int colSnmpNotifyFilterStorageType = 4;
  public static final int colSnmpNotifyFilterRowStatus = 5;

  // Column index defintions for snmpNotifyFilterEntry:
  public static final int idxSnmpNotifyFilterSubtree = 1;
  public static final int idxSnmpNotifyFilterMask = 0;
  public static final int idxSnmpNotifyFilterType = 1;
  public static final int idxSnmpNotifyFilterStorageType = 2;
  public static final int idxSnmpNotifyFilterRowStatus = 3;
  private static MOTableSubIndex[] snmpNotifyFilterEntryIndexes =
    new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32),
        new MOTableSubIndex(SMIConstants.SYNTAX_OBJECT_IDENTIFIER, 0, 128)  };

  private static MOTableIndex snmpNotifyFilterEntryIndex =
      new MOTableIndex(snmpNotifyFilterEntryIndexes, true);

  private DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
      snmpNotifyFilterEntry;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> snmpNotifyFilterEntryModel;
  public static final OID oidSnmpNotifyFilterProfileEntry =
    new OID(new int[] { 1,3,6,1,6,3,13,1,2,1 });

  // Column sub-identifer defintions for snmpNotifyFilterProfileEntry:
  public static final int colSnmpNotifyFilterProfileName = 1;
  public static final int colSnmpNotifyFilterProfileStorType = 2;
  public static final int colSnmpNotifyFilterProfileRowStatus = 3;

  // Column index defintions for snmpNotifyFilterProfileEntry:
  public static final int idxSnmpNotifyFilterProfileName = 0;
  public static final int idxSnmpNotifyFilterProfileStorType = 1;
  public static final int idxSnmpNotifyFilterProfileRowStatus = 2;
  private static MOTableSubIndex[] snmpNotifyFilterProfileEntryIndexes =
    new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpNotifyFilterProfileEntryIndex =
      new MOTableIndex(snmpNotifyFilterProfileEntryIndexes, true);


  private DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
      snmpNotifyFilterProfileEntry;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> snmpNotifyFilterProfileEntryModel;

  public SnmpNotificationMIB() {
    createSnmpNotifyEntry();
    createSnmpNotifyFilterEntry();
    createSnmpNotifyFilterProfileEntry();
  }

  @SuppressWarnings("unchecked")
  private void createSnmpNotifyEntry() {
    MOColumn[] snmpNotifyEntryColumns = new MOColumn[4];
    snmpNotifyEntryColumns[idxSnmpNotifyTag] =
      new SnmpTagValue(colSnmpNotifyTag,
                       MOAccessImpl.ACCESS_READ_CREATE,
                       new OctetString(new byte[] {  }),
                       true);
    snmpNotifyEntryColumns[idxSnmpNotifyType] =
      new Enumerated<Integer32>(colSnmpNotifyType,
                     SMIConstants.SYNTAX_INTEGER32,
                     MOAccessImpl.ACCESS_READ_CREATE,
                     new Integer32(1),
                     true,
                     new int[] {  1, 2 });
    snmpNotifyEntryColumns[idxSnmpNotifyStorageType] =
      new StorageType(colSnmpNotifyStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(3),
                      true);
    snmpNotifyEntryColumns[idxSnmpNotifyRowStatus] =
      new RowStatus(colSnmpNotifyRowStatus);

    snmpNotifyEntry =
      new DefaultMOTable(oidSnmpNotifyEntry,
                         snmpNotifyEntryIndex,
                         snmpNotifyEntryColumns);
    snmpNotifyEntryModel = new DefaultMOMutableTableModel();
    snmpNotifyEntryModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    snmpNotifyEntry.setModel(snmpNotifyEntryModel);
  }

  @SuppressWarnings("unchecked")
  private void createSnmpNotifyFilterEntry() {
    MOColumn[] snmpNotifyFilterEntryColumns = new MOColumn[4];
    snmpNotifyFilterEntryColumns[idxSnmpNotifyFilterMask] =
      new MOMutableColumn(colSnmpNotifyFilterMask,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString(new byte[] {  }),
                          true);
    ((MOMutableColumn)snmpNotifyFilterEntryColumns[idxSnmpNotifyFilterMask]).
      addMOValueValidationListener(new SnmpNotifyFilterMaskValidator());
    snmpNotifyFilterEntryColumns[idxSnmpNotifyFilterType] =
      new Enumerated<Integer32>(colSnmpNotifyFilterType,
                      SMIConstants.SYNTAX_INTEGER32,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(1),
                      true,
                      new int[] {  1, 2 });
    snmpNotifyFilterEntryColumns[idxSnmpNotifyFilterStorageType] =
      new StorageType(colSnmpNotifyFilterStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(3),
                      true);
    snmpNotifyFilterEntryColumns[idxSnmpNotifyFilterRowStatus] =
      new RowStatus(colSnmpNotifyFilterRowStatus);

    snmpNotifyFilterEntry =
      new DefaultMOTable(oidSnmpNotifyFilterEntry,
                         snmpNotifyFilterEntryIndex,
                         snmpNotifyFilterEntryColumns);
    snmpNotifyFilterEntryModel = new DefaultMOMutableTableModel();
    snmpNotifyFilterEntryModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    snmpNotifyFilterEntry.setModel(snmpNotifyFilterEntryModel);
  }

  @SuppressWarnings("unchecked")
  private void createSnmpNotifyFilterProfileEntry() {
    MOColumn[] snmpNotifyFilterProfileEntryColumns = new MOColumn[3];
    snmpNotifyFilterProfileEntryColumns[idxSnmpNotifyFilterProfileName] =
      new SnmpAdminString(colSnmpNotifyFilterProfileName,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString(),
                          true, 1, 32);
    snmpNotifyFilterProfileEntryColumns[idxSnmpNotifyFilterProfileStorType] =
      new StorageType(colSnmpNotifyFilterProfileStorType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(3),
                      true);
    snmpNotifyFilterProfileEntryColumns[idxSnmpNotifyFilterProfileRowStatus] =
      new RowStatus(colSnmpNotifyFilterProfileRowStatus);

    snmpNotifyFilterProfileEntry =
      new DefaultMOTable(oidSnmpNotifyFilterProfileEntry,
                         snmpNotifyFilterProfileEntryIndex,
                         snmpNotifyFilterProfileEntryColumns);
    snmpNotifyFilterProfileEntryModel = new DefaultMOMutableTableModel();
    snmpNotifyFilterProfileEntryModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    snmpNotifyFilterProfileEntry.setModel(snmpNotifyFilterProfileEntryModel);
  }


  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.snmpNotifyEntry, context);
    server.register(this.snmpNotifyFilterEntry, context);
    server.register(this.snmpNotifyFilterProfileEntry, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpNotifyEntry, context);
    server.unregister(this.snmpNotifyFilterEntry, context);
    server.unregister(this.snmpNotifyFilterProfileEntry, context);
  }

  public boolean addNotifyEntry(OctetString name, OctetString tag, int type,
                                int storageType) {
    Variable[] vbs = new Variable[snmpNotifyEntry.getColumnCount()];
    int n=0;
    vbs[n++] = tag;
    vbs[n++] = new Integer32(type);
    vbs[n++] = new Integer32(storageType);
    vbs[n  ] = new Integer32(RowStatus.active);
    OID index = name.toSubIndex(true);
    DefaultMOMutableRow2PC row = snmpNotifyEntry.createRow(index, vbs);
    snmpNotifyEntry.addRow(row);
    return true;
  }

  public boolean removeNotifyEntry(OctetString name) {
    return snmpNotifyEntry.removeRow(name.toSubIndex(true)) != null;
  }

  public DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
    getNotifyTable() {
    return snmpNotifyEntry;
  }

  public DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
    getNotifyFilterTable() {
    return snmpNotifyFilterEntry;
  }

  public DefaultMOTable<DefaultMOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
    getNotifyFilterProfileTable() {
    return snmpNotifyFilterProfileEntry;
  }

  public boolean hasFilter(final OctetString filterName) {
    MOTableRowFilter<DefaultMOMutableRow2PC> selectFilter = new MOTableRowFilter<DefaultMOMutableRow2PC>() {
      public boolean passesFilter(DefaultMOMutableRow2PC row) {
        Integer32 rs = (Integer32) row.getValue(idxSnmpNotifyFilterProfileRowStatus);
        if (rs == null) {
          return false;
        }
        return ((rs.getValue() == RowStatus.active) &&
                filterName.equals(row.getValue(idxSnmpNotifyFilterProfileName)));
      }
    };
    synchronized (snmpNotifyFilterProfileEntryModel) {
      Iterator it = snmpNotifyFilterProfileEntryModel.iterator(selectFilter);
      return it.hasNext();
    }
  }

  public boolean passesFilter(final OctetString filterName, OID notificationID,
                              VariableBinding[] vbs) {
    MOTableRowFilter<DefaultMOMutableRow2PC> selectFilter = new MOTableRowFilter<DefaultMOMutableRow2PC>() {
      public boolean passesFilter(DefaultMOMutableRow2PC row) {
        Integer32 rs = (Integer32) row.getValue(idxSnmpNotifyFilterProfileRowStatus);
        if (rs == null) {
          return false;
        }
        return ((rs.getValue() == RowStatus.active) &&
                filterName.equals(row.getValue(idxSnmpNotifyFilterProfileName)));
      }
    };
    List<DefaultMOMutableRow2PC> profiles =
        snmpNotifyFilterProfileEntryModel.getRows(null, null, selectFilter);
    return passesFilter(notificationID, vbs, profiles);
  }

  public boolean passesFilter(OID paramsIndex, OID notificationID,
                              VariableBinding[] vbs) {
    MOTableRowFilter<DefaultMOMutableRow2PC> activeFilter =
        new RowStatus.ActiveRowsFilter<DefaultMOMutableRow2PC>(idxSnmpNotifyFilterProfileRowStatus);
    List<DefaultMOMutableRow2PC> profiles =
        snmpNotifyFilterProfileEntryModel.getRows(paramsIndex,
            paramsIndex.successor(),
            activeFilter);
    return passesFilter(notificationID, vbs, profiles);
  }

  private boolean passesFilter(OID notificationID, VariableBinding[] vbs,
                               List<DefaultMOMutableRow2PC> profiles) {
    if (profiles.size() == 0) {
      // no profile -> passes filter
      return true;
    }
    OctetString profileName = (OctetString)
        (profiles.get(0)).getValue(idxSnmpNotifyFilterProfileName);
    OID profileNameOID = profileName.toSubIndex(false);
    OID profileNameOIDNext = profileNameOID.nextPeer();
    MOTableRowFilter<DefaultMOMutableRow2PC> activeFilter =
        new RowStatus.ActiveRowsFilter<DefaultMOMutableRow2PC>(idxSnmpNotifyFilterRowStatus);
    List<DefaultMOMutableRow2PC> filters =
        snmpNotifyFilterEntryModel.getRows(profileNameOID,
            profileNameOIDNext,
            activeFilter);
    if (filters.size() == 0) {
      // no filters -> no trap is sent
      return false;
    }
    TreeMap<OID, Integer32> oidMatches = new TreeMap<OID, Integer32>();
    List<TreeMap<OID, Integer32>> vbMatches = new ArrayList<TreeMap<OID,Integer32>>(vbs.length);
    for (MOTableRow row : filters) {
      Variable[] indexValues =
          snmpNotifyFilterEntryIndex.getIndexValues(row.getIndex());
      OID subtree = (OID) indexValues[idxSnmpNotifyFilterSubtree];
      OctetString mask = (OctetString) row.getValue(idxSnmpNotifyFilterMask);
      Integer32 type = (Integer32) row.getValue(idxSnmpNotifyFilterType);
      OID subtreeWithLength = new OID();
      subtreeWithLength.append(subtree.size());
      subtreeWithLength.append(subtree);
      if (isInSubtree(notificationID, subtree, mask)) {
        oidMatches.put(subtreeWithLength, type);
      }
      // filter VBs
      for (int i = 0; i < vbs.length; i++) {
        if (isInSubtree(vbs[i].getOid(), subtree, mask)) {
          if ((vbMatches.size() > i) && (vbMatches.get(i) == null)) {
            vbMatches.set(i, new TreeMap<OID, Integer32>());
          }
          else if (vbMatches.size() <= i) {
            for (int j=0; j<=i-vbMatches.size(); j++) {
              vbMatches.add(new TreeMap<OID, Integer32>());
            }
          }
          else {
            vbMatches.get(i).put(subtreeWithLength, type);
          }
        }
      }
    }
    if (oidMatches.size() == 0) {
      // no matches against notification ID
      if (logger.isInfoEnabled()) {
        logger.info("Filter " + profileName +
                    " has no matches for notification ID " +
                    notificationID);
      }
      return false;
    }
    else if ((oidMatches.get(oidMatches.lastKey())).getValue() ==
             SnmpNotifyFilterTypeEnum.excluded) {
      if (logger.isInfoEnabled()) {
        logger.info("Notification ID " + notificationID +
                    " is excluded from filter " + profileName);
      }
      return false;
    }
    for (int i=0; i<vbMatches.size(); i++) {
      TreeMap<OID, Integer32> vbMatch = vbMatches.get(i);
      if ((vbMatch!= null) && (vbMatch.size() > 0)) {
        if ((vbMatches.get(i).get(vbMatch.lastKey())).getValue() ==
            SnmpNotifyFilterTypeEnum.excluded) {
          if (logger.isInfoEnabled()) {
            logger.info("Variable binding " + vbs[i] +
                        " is not in filter " + profileName);
          }
          return false;
        }
      }
    }
    return true;
  }

  private static boolean isInSubtree(OID oid, OID subtree, OctetString mask) {
    OID maskedSubtree = subtree.mask(mask);
    OID maskedOID = oid.mask(mask);
    if (maskedOID.equals(maskedSubtree)) {
      return true;
    }
    return maskedOID.startsWith(maskedSubtree);
  }

  // Enumerations

  public static final class SnmpNotifyTypeEnum {
    public static final int trap = 1;
    public static final int inform = 2;
  }
  public static final class SnmpNotifyFilterTypeEnum {
    public static final int included = 1;
    public static final int excluded = 2;
  }

  // Scalars

  // Value Validators

  /**
   * The <code>SnmpNotifyFilterMaskValidator</code> implements the value validation
   * for <code>SnmpNotifyFilterMask</code>.
   */
  static class SnmpNotifyFilterMaskValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 16)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpNotifyFilterMask::validate
     //--AgentGen END
    }
  }

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


