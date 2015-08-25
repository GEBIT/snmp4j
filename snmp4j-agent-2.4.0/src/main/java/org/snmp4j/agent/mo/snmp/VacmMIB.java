/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - VacmMIB.java  
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

import java.util.*;

import org.snmp4j.log.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.util.*;
import org.snmp4j.agent.security.*;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;

/**
 * This concrete implementation of the SNMP-VIEW-BASED-ACM-MIB (RFC 3415).
 * The configuration of the view access model can be changed programatically
 * (see {@link MutableVACM}) or via SNMP but an initial configuration must be
 * created programatically in order to allow any access to the agent via SNMP.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class VacmMIB implements MOGroup, MutableVACM {

  private static final LogAdapter logger = LogFactory.getLogger(VacmMIB.class);

  public static final OID vacmContextEntryOID =
      new OID(new int[] {1,3,6,1,6,3,16,1,1,1});

  public static final int colVacmGroupName = 3;
  public static final int colVacmSecurityToGroupStorageType = 4;
  public static final int colVacmSecurityToGroupRowStatus = 5;

  public static final int idxVacmGroupName = 0;
  public static final int idxVacmSecurityToGroupStorageType = 1;
  public static final int idxVacmSecurityToGroupRowStatus = 2;

  public static final OID vacmSecurityToGroupEntryOID =
      new OID(new int[] {1,3,6,1,6,3,16,1,2,1});

  public static final int colVacmAccessContextMatch = 4;
  public static final int colVacmAccessReadViewName = 5;
  public static final int colVacmAccessWriteViewName = 6;
  public static final int colVacmAccessNotifyViewName = 7;
  public static final int colVacmAccessStorageType = 8;
  public static final int colVacmAccessRowStatus = 9;

//  private static final int idxVacmAccessGroupName = 0;
  public static final int idxVacmAccessContextPrefix = 1;
  public static final int idxVacmAccessSecurityModel = 2;
  public static final int idxVacmAccessSecurityLevel = 3;
  public static final int idxVacmAccessContextMatch = 0;
  public static final int idxVacmAccessReadViewName = 1;
  public static final int idxVacmAccessWriteViewName = 2;
  public static final int idxVacmAccessNotifyViewName = 3;
  public static final int idxVacmAccessStorageType = 4;
  public static final int idxVacmAccessRowStatus = 5;

  public static final OID vacmAccessEntryOID =
      new OID(new int[] {1,3,6,1,6,3,16,1,4,1});

  public static final int vacmExactMatch = MutableVACM.VACM_MATCH_EXACT;
  public static final int vacmPrefixMatch = MutableVACM.VACM_MATCH_PREFIX;


  public static final OID vacmViewSpinLockOID =
      new OID(new int[] {1,3,6,1,6,3,16,1,5,1,0});

  public static final int colVacmViewTreeFamilyMask = 3;
  public static final int colVacmViewTreeFamilyType = 4;
  public static final int colVacmViewTreeFamilyStorageType = 5;
  public static final int colVacmViewTreeFamilyRowStatus = 6;

//  private static final int idxVacmViewTreeViewName = 0;
  public static final int idxVacmViewTreeSubtree = 1;

  public static final int idxVacmViewTreeFamilyMask = 0;
  public static final int idxVacmViewTreeFamilyType = 1;
  public static final int idxVacmViewTreeFamilyStorageType = 2;
  public static final int idxVacmViewTreeFamilyRowStatus = 3;

  public static final OID vacmViewTreeFamilyEntryOID =
      new OID(new int[] {1,3,6,1,6,3,16,1,5,2,1});

  public static final int vacmViewIncluded = MutableVACM.VACM_VIEW_INCLUDED;
  public static final int vacmViewExcluded = MutableVACM.VACM_VIEW_EXCLUDED;

  private static final int[] vacmViewTreeFamilyTypeValues = {
      vacmViewIncluded, vacmViewExcluded
  };

  private static MOTableSubIndex[] vacmViewTreeFamilyIndexes =
      new MOTableSubIndex[] {
      new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32),
      new MOTableSubIndex(SMIConstants.SYNTAX_OBJECT_IDENTIFIER, 0, 96)
  };

  private static MOTableIndex vacmViewTreeFamilyIndex =
      new MOTableIndex(vacmViewTreeFamilyIndexes);

  private static MOTableSubIndex[] vacmAccessIndexes = new MOTableSubIndex[] {
      new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32),
      new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 0, 32),
      new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER, 1, 1),
      new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER, 1, 1)
  };

  private static MOTableIndex vacmAccessIndex =
      new MOTableIndex(vacmAccessIndexes) {
    public boolean isValidIndex(OID index) {
      boolean ok = super.isValidIndex(index);
      if (ok) {
        SecurityModels secModels = SecurityModels.getInstance();
        Integer32 secModel = new Integer32(index.get(index.size()-2));
        if ((secModel.getValue() != SecurityModel.SECURITY_MODEL_ANY) &&
            (secModels.getSecurityModel(secModel) == null)) {
          return false;
        }
        int secLevel = index.get(index.size()-1);
        if ((secLevel < 1) || (secLevel > 3)) {
          return false;
        }
      }
      return ok;
    }
  };

  private MOServer[] server;

  private DefaultMOTable<MOTableRow, MOColumn, VacmContextTableModel> vacmContextTable;

  private DefaultMOTable<DefaultMOMutableRow2PC, MOColumn, DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
      vacmSecurityToGroupTable;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> vacmSecurityToGroupTableModel;

  private DefaultMOTable<DefaultMOMutableRow2PC, MOColumn, DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
      vacmAccessTable;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> vacmAccessTableModel;

  private TestAndIncr vacmViewSpinLock;

  private DefaultMOTable<DefaultMOMutableRow2PC, MOColumn, DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
      vacmViewTreeFamilyTable;
  private DefaultMOMutableTableModel<DefaultMOMutableRow2PC> vacmViewTreeFamilyTableModel;


  public VacmMIB(MOServer[] server) {
    this.server = server;
    createVacmContextTable();
    createVacmSecuritToGroupTable();
    createVacmAccessTable();
    createVacmViewTreeFamilyTable();
    vacmViewSpinLock = new TestAndIncr(vacmViewSpinLockOID);
  }

  public void registerMOs(MOServer server, OctetString context) throws
      DuplicateRegistrationException {
    server.register(vacmContextTable, context);
    server.register(vacmSecurityToGroupTable, context);
    server.register(vacmAccessTable, context);
    server.register(vacmViewSpinLock, context);
    server.register(vacmViewTreeFamilyTable, context);
  }

  private void createVacmContextTable() {
    MOTableSubIndex[] vacmContextTableIndexes = new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 0, 32) };
    MOTableIndex vacmContextTableIndex =
        new MOTableIndex(vacmContextTableIndexes);
    MOColumn[] vacmContextColumns = new MOColumn[] {
        new MOColumn(1, SMIConstants.SYNTAX_OCTET_STRING,
                     MOAccessImpl.ACCESS_READ_ONLY) };

    this.vacmContextTable =
        new DefaultMOTable<MOTableRow, MOColumn, VacmContextTableModel>(
            vacmContextEntryOID, vacmContextTableIndex, vacmContextColumns);
    this.vacmContextTable.setVolatile(true);
    this.vacmContextTable.setModel(new VacmContextTableModel());
  }

  private void createVacmSecuritToGroupTable() {
    MOTableSubIndex[] vacmSecurityToGroupIndexes = new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER, 1, 1),
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32) };

    MOTableIndex vacmSecurityToGroupIndex =
        new MOTableIndex(vacmSecurityToGroupIndexes) {
      public boolean isValidIndex(OID index) {
        boolean ok = super.isValidIndex(index);
        int securityModel = index.get(0);
        if (ok && (securityModel > SecurityModel.SECURITY_MODEL_SNMPv2c)) {
          SecurityModels secModels = SecurityModels.getInstance();
          if (secModels.getSecurityModel(new Integer32(securityModel)) == null) {
            return false;
          }
        }
        return ok;
      }
    };

    MOColumn[] vacmSecurityToGroupColumns = new MOColumn[] {
        new SnmpAdminString(colVacmGroupName,
                            MOAccessImpl.ACCESS_READ_CREATE, null, true, 1, 32),
        new StorageType(colVacmSecurityToGroupStorageType,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(StorageType.nonVolatile), true),
        new RowStatus(colVacmSecurityToGroupRowStatus)
    };

    this.vacmSecurityToGroupTable =
        new DefaultMOTable<DefaultMOMutableRow2PC, MOColumn, DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
            (vacmSecurityToGroupEntryOID, vacmSecurityToGroupIndex, vacmSecurityToGroupColumns);
    vacmSecurityToGroupTableModel = new DefaultMOMutableTableModel<DefaultMOMutableRow2PC>();
    vacmSecurityToGroupTableModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    this.vacmSecurityToGroupTable.setModel(vacmSecurityToGroupTableModel);
  }

  private void createVacmAccessTable() {
    MOColumn[] vacmAccessColumns = new MOColumn[] {
        new Enumerated<Integer32>(colVacmAccessContextMatch,
                        SMIConstants.SYNTAX_INTEGER32,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(vacmExactMatch), true,
                        new int[] { vacmExactMatch, vacmPrefixMatch }),
        new SnmpAdminString(colVacmAccessReadViewName,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new OctetString(), true, 0, 32),
        new SnmpAdminString(colVacmAccessWriteViewName,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new OctetString(), true, 0, 32),
        new SnmpAdminString(colVacmAccessNotifyViewName,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new OctetString(), true, 0, 32),
        new StorageType(colVacmAccessStorageType,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(StorageType.nonVolatile), true),
        new RowStatus(colVacmAccessRowStatus)
    };

    vacmAccessTable = new DefaultMOTable<DefaultMOMutableRow2PC, MOColumn, DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
        (vacmAccessEntryOID, vacmAccessIndex, vacmAccessColumns);
    vacmAccessTableModel = new DefaultMOMutableTableModel<DefaultMOMutableRow2PC>();
    vacmAccessTableModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    vacmAccessTable.setModel(vacmAccessTableModel);
  }

  private void createVacmViewTreeFamilyTable() {
    MOColumn[] vacmViewTreeFamilyColumns = new MOColumn[] {
        new SnmpAdminString(colVacmViewTreeFamilyMask,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new OctetString(), true, 0, 16),
        new Enumerated<Integer32>(colVacmViewTreeFamilyType,
                        SMIConstants.SYNTAX_INTEGER32,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(vacmViewIncluded), true,
                        vacmViewTreeFamilyTypeValues),
        new StorageType(colVacmViewTreeFamilyStorageType,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(StorageType.nonVolatile), true),
        new RowStatus(colVacmViewTreeFamilyRowStatus)
    };

    vacmViewTreeFamilyTable =
        new DefaultMOTable<DefaultMOMutableRow2PC, MOColumn, DefaultMOMutableTableModel<DefaultMOMutableRow2PC>>
            (vacmViewTreeFamilyEntryOID, vacmViewTreeFamilyIndex, vacmViewTreeFamilyColumns);
    vacmViewTreeFamilyTableModel = new DefaultMOMutableTableModel<DefaultMOMutableRow2PC>();
    vacmViewTreeFamilyTableModel.setRowFactory(new DefaultMOMutableRow2PCFactory());
    vacmViewTreeFamilyTable.setModel(vacmViewTreeFamilyTableModel);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    server.unregister(this.vacmContextTable, context);
    server.unregister(this.vacmSecurityToGroupTable, context);
    server.unregister(this.vacmAccessTable, context);
    server.unregister(vacmViewSpinLock, context);
    server.unregister(vacmViewTreeFamilyTable, context);
  }

  public int isAccessAllowed(OctetString context, OctetString securityName,
                             int securityModel, int securityLevel, int viewType,
                             OID oid) {
    if (logger.isDebugEnabled()) {
      logger.debug("VACM access requested for context="+context+
                   ", securityName="+securityName+
                   ", securityModel="+securityModel+
                   ", securityLevel="+securityLevel+
                   ", viewType="+viewType+
                   ", OID="+oid);
    }
    boolean supported = isContextSupported(context);
    if (!supported) {
      if (logger.isDebugEnabled()) {
        logger.debug("Context '"+context+"' ist not supported");
      }
      return VACM.VACM_NO_SUCH_CONTEXT;
    }
    OctetString groupName = getGroupName(securityName, securityModel);
    if (groupName == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("No group name for securityName="+securityName+
                     " and securityModel="+securityModel);
      }
      return VACM.VACM_NO_GROUP_NAME;
    }
    OctetString viewName = getViewNameByGroup(context, securityModel,
                                              securityLevel, viewType,
                                              groupName);
    if (viewName == null) {
      return VACM.VACM_NO_ACCESS_ENTRY;
    }
    if (viewName.length() == 0) {
      return VACM.VACM_NO_SUCH_VIEW;
    }
    return isAccessAllowed(viewName, oid);
  }

  private boolean isContextSupported(OctetString context) {
    boolean supported = false;
    for (MOServer aServer : server) {
      if (aServer.isContextSupported(context)) {
        supported = true;
        break;
      }
    }
    return supported;
  }

  public OctetString getViewName(OctetString context,
                                 OctetString securityName,
                                 int securityModel,
                                 int securityLevel,
                                 int viewType) {
    OctetString groupName = getGroupName(securityName, securityModel);
    if (groupName == null) {
      return null;
    }
    return getViewNameByGroup(context, securityModel, securityLevel,
                              viewType, groupName);
  }

  private OctetString getViewNameByGroup(OctetString context, int securityModel,
                                         int securityLevel, int viewType,
                                         OctetString groupName) {
    List<MOTableRow> accessEntries = getAccessEntries(groupName);

    if (logger.isDebugEnabled()) {
      logger.debug("Got views "+accessEntries+
                   " for group name '"+groupName+"'");
    }

    MOTableRow possibleMatch = null;
    boolean foundExactContextMatch = false;
    boolean foundMatchedSecModel = false;
    int foundContextPrefixLength = 0;
    int foundSecLevel = 0;

    for (MOTableRow accessEntry : accessEntries) {
      if (((Integer32) accessEntry.getValue(idxVacmAccessRowStatus)).getValue() !=
          RowStatus.active) {
        continue;
      }
      Variable[] indexValues = vacmAccessIndex.getIndexValues(accessEntry.getIndex());
      OctetString rowContext =
          (OctetString) indexValues[idxVacmAccessContextPrefix];
      int rowSecurityModel =
          ((Integer32) indexValues[idxVacmAccessSecurityModel]).getValue();
      int rowSecurityLevel =
          ((Integer32) indexValues[idxVacmAccessSecurityLevel]).getValue();
      int rowContextMatch =
          ((Integer32) accessEntry.getValue(idxVacmAccessContextMatch)).getValue();
      boolean exactContextMatch = rowContext.equals(context);
      boolean prefixMatch = (!exactContextMatch) &&
          ((rowContextMatch == vacmPrefixMatch) &&
              (context.startsWith(rowContext)));
      boolean matchSecModel = (rowSecurityModel == securityModel)
          || (rowSecurityModel == SecurityModel.SECURITY_MODEL_ANY);
      boolean matchSecLevel = (rowSecurityLevel <= securityLevel);
      if (logger.isDebugEnabled()) {
        logger.debug("Matching against access entry " + accessEntry +
            " with exactContextMatch=" + exactContextMatch +
            ", prefixMatch=" + prefixMatch +
            ", matchSecModel=" + matchSecModel +
            " and matchSecLevel=" + matchSecLevel);
      }
      if ((exactContextMatch || prefixMatch) &&
          (matchSecModel) &&
          matchSecLevel) {
        // check better match
        if ((possibleMatch == null) ||
            (((!foundMatchedSecModel) && (matchSecModel)) ||
                (((!foundMatchedSecModel) || (matchSecModel)) &&
                    ((!foundExactContextMatch) && (exactContextMatch)) ||
                    ((((!foundExactContextMatch) || (exactContextMatch)) &&
                        (foundContextPrefixLength < rowContext.length())) ||
                        ((foundContextPrefixLength == rowContext.length()) &&
                            (foundSecLevel < rowSecurityLevel)))))) {
          possibleMatch = accessEntry;
          foundExactContextMatch = exactContextMatch;
          if (prefixMatch) {
            foundContextPrefixLength = rowContext.length();
          }
          foundMatchedSecModel = matchSecModel;
          foundSecLevel = rowSecurityLevel;
        }
      }
    }
    if (possibleMatch != null) {
      OctetString viewName = null;
      switch (viewType) {
        case VACM.VIEW_READ: {
          viewName =
              (OctetString)possibleMatch.getValue(idxVacmAccessReadViewName);
          break;
        }
        case VACM.VIEW_WRITE: {
          viewName = (OctetString)
              possibleMatch.getValue(idxVacmAccessWriteViewName);
          break;
        }
        case VACM.VIEW_NOTIFY: {
          viewName = (OctetString)
              possibleMatch.getValue(idxVacmAccessNotifyViewName);
          break;
        }
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Matching view found for group name '"+groupName+"' is '"+
                     viewName+"'");
      }
      return viewName;
    }
    return null;
  }

  private OctetString getGroupName(OctetString securityName,
                                   int securityModel) {
    OID index = new OID();
    index.append(securityModel);
    index.append(securityName.toSubIndex(false));
    MOTableRow row = vacmSecurityToGroupTableModel.getRow(index);
    if (row != null) {
      OctetString groupName = (OctetString) row.getValue(idxVacmGroupName);
      if (logger.isDebugEnabled()) {
        logger.debug("Found group name '"+groupName+"' for secName '"+
                     securityName+"' and secModel "+securityModel);
      }
      return groupName;
    }
    return null;
  }

  public int isAccessAllowed(OctetString viewName, OID oid) {
    List views = getViews(viewName);
    if (views.size() == 0) {
      if (logger.isDebugEnabled()) {
        logger.debug("No view tree family entry for view '" + viewName + "'");
      }
      return VACM.VACM_NO_SUCH_VIEW;
    }
    // iterate from back to forth because the views list must be ordered by
    // subtree length (view name is the same for all entries) which is the
    // criteria to find the appropriate view access entry.
    for (int v=views.size()-1; v >= 0; v--) {
      MOTableRow row = (MOTableRow) views.get(v);
      if (((Integer32)row.getValue(idxVacmViewTreeFamilyRowStatus)).getValue()!=
          RowStatus.active) {
        // only active rows are relevant
        continue;
      }
      OID index = row.getIndex();
      Variable[] indexValues = vacmViewTreeFamilyIndex.getIndexValues(index);
      OID subtree = (OID) indexValues[idxVacmViewTreeSubtree];
      if (oid.size() < subtree.size()) {
        // no match
        continue;
      }
      OctetString mask = (OctetString) row.getValue(idxVacmViewTreeFamilyMask);
      boolean match = true;
      for (int i=0; i<subtree.size(); i++) {
        if ((subtree.get(i) != oid.get(i)) && isBitSet(i, mask)) {
          match = false;
          break;
        }
      }
      if (match) {
        // we found the matching entry
        if (((Integer32)row.getValue(idxVacmViewTreeFamilyType)).getValue() ==
            vacmViewIncluded) {
          if (logger.isDebugEnabled()) {
            logger.debug("Access allowed for view '"+viewName+"' by subtree "+
                         subtree+" for OID "+oid);
          }
          return VACM.VACM_OK;
        }
        else {
          // excluded
          if (logger.isDebugEnabled()) {
            logger.debug("Access denied for view '"+viewName+"' by subtree "+
                         subtree+" for OID "+oid);
          }
          return VACM.VACM_NOT_IN_VIEW;
        }
      }
    }
    return VACM.VACM_NOT_IN_VIEW;
  }

  /**
   * Adds a security model and name to group name mapping to this VACM. Any
   * already existing mapping for the security name and model will be silently
   * replaced.
   * @param securityModel
   *    the security model.
   * @param securityName
   *    the security name.
   * @param groupName
   *    the group name.
   * @param storageType
   *    the storage type for the new entry.
   */
  public void addGroup(int securityModel, OctetString securityName,
                       OctetString groupName, int storageType) {
    OID index = createGroupIndex(securityModel, securityName);
    Variable[] values = new Variable[vacmSecurityToGroupTable.getColumnCount()];
    values[idxVacmGroupName] = groupName;
    values[idxVacmSecurityToGroupStorageType] = new Integer32(storageType);
    values[idxVacmSecurityToGroupRowStatus] = new Integer32(RowStatus.active);
    DefaultMOMutableRow2PC row = vacmSecurityToGroupTable.createRow(index, values);
    vacmSecurityToGroupTable.addRow(row);
  }

  private static OID createGroupIndex(int securityModel,
                                      OctetString securityName) {
    OID index = new OID();
    index.append(securityModel);
    index.append(securityName.toSubIndex(false));
    return index;
  }

  /**
   * Removes a security model and name to group name mapping from this VACM.
   * @param securityModel
   *    the security model.
   * @param securityName
   *    the security name.
   * @return
   *    <code>true</code> when the entry has been removed or <code>false</code>
   *    if such a mapping could not be found.
   */
  public boolean removeGroup(int securityModel, OctetString securityName) {
    OID index = createGroupIndex(securityModel, securityName);
    return (vacmSecurityToGroupTable.removeRow(index) != null);
  }

  /**
   * Adds an access entry to this VACM and thus adds access rights for a group.
   * @param groupName
   *    the group for which access rights are to be added.
   * @param contextPrefix
   *    the context or context prefix.
   * @param securityModel
   *    the security model
   * @param securityLevel
   *    the security level
   * @param match
   *    indicates whether exact context match ({@link #vacmExactMatch})
   *    or prefix context match ({@link #vacmPrefixMatch}) should
   *    be used by the new entry.
   * @param readView
   *    the view name for read access (use a zero length OctetString to disable
   *    access).
   * @param writeView
   *    the view name for write access (use a zero length OctetString to disable
   *    access).
   * @param notifyView
   *    the view name for notify access (use a zero length OctetString to
   *    disable access).
   * @param storageType
   *    the {@link StorageType} for this access entry.
   */
  public void addAccess(OctetString groupName, OctetString contextPrefix,
                        int securityModel, int securityLevel,
                        int match,
                        OctetString readView, OctetString writeView,
                        OctetString notifyView, int storageType) {
    OID index = createAccessIndex(groupName, contextPrefix, securityModel,
                                  securityLevel);
    Variable[] values = new Variable[vacmAccessTable.getColumnCount()];
    values[idxVacmAccessContextMatch] = new Integer32(match);
    values[idxVacmAccessReadViewName] = readView;
    values[idxVacmAccessWriteViewName] = writeView;
    values[idxVacmAccessNotifyViewName] = notifyView;
    values[idxVacmAccessStorageType] = new Integer32(storageType);
    values[idxVacmAccessRowStatus] = new Integer32(RowStatus.active);
    vacmAccessTable.addRow(vacmAccessTableModel.createRow(index, values));
  }

  /**
   * Removes an access entry from this VACM.
   * @param groupName
   *    the group for which access rights are to be added.
   * @param contextPrefix
   *    the context or context prefix.
   * @param securityModel
   *    the security model
   * @param securityLevel
   *    the security level
   * @return
   *    <code>true</code> when the entry has been removed or <code>false</code>
   *    if no such entry could be found.
   */
  public boolean removeAccess(OctetString groupName, OctetString contextPrefix,
                              int securityModel, int securityLevel) {
    OID index = createAccessIndex(groupName, contextPrefix, securityModel,
                                  securityLevel);
    return (vacmAccessTable.removeRow(index) != null);
  }

  private static OID createAccessIndex(OctetString groupName,
                                       OctetString contextPrefix,
                                       int securityModel, int securityLevel) {
    OID index = groupName.toSubIndex(false);
    index.append(contextPrefix.toSubIndex(false));
    index.append(securityModel);
    index.append(securityLevel);
    return index;
  }

  /**
   * Adds a new view to this VACM. An already existing entry with the same
   * view name and subtree OID will be replaced silently.
   * @param viewName
   *    the view name.
   * @param subtree
   *    the subtree OID.
   * @param mask
   *    the bit mask which, in combination with <code>subtree</code>,
   *    defines a family of view subtrees.
   * @param type
   *    indicates whether the view defined by <code>subtree</code> and
   *    <code>mask</code> is included ({@link #vacmViewIncluded}) or excluded
   *    (@link #vacmViewExcluded}) from the MIB view.
   * @param storageType
   *    the {@link StorageType} for this access entry.
   */
  public void addViewTreeFamily(OctetString viewName, OID subtree,
                                OctetString mask, int type, int storageType) {
    OID index = createViewIndex(viewName, subtree);
    Variable[] values = new Variable[vacmViewTreeFamilyTable.getColumnCount()];
    values[idxVacmViewTreeFamilyMask] = mask;
    values[idxVacmViewTreeFamilyType] = new Integer32(type);
    values[idxVacmViewTreeFamilyStorageType] = new Integer32(storageType);
    values[idxVacmViewTreeFamilyRowStatus] = new Integer32(RowStatus.active);
    DefaultMOMutableRow2PC row = vacmViewTreeFamilyTableModel.createRow(index, values);
    vacmViewTreeFamilyTable.addRow(row);
  }

  /**
   * Removes a view tree family from this VACM.
   * @param viewName
   *    the view name.
   * @param subtree
   *    the subtree OID.
   * @return
   *    <code>true</code> when the entry has been removed or <code>false</code>
   *    if no such entry could be found.
   */
  public boolean removeViewTreeFamily(OctetString viewName, OID subtree) {
    OID index = createViewIndex(viewName, subtree);
    return (vacmViewTreeFamilyTable.removeRow(index) != null);
  }

  private static OID createViewIndex(OctetString viewName, OID subtree) {
    OID index = viewName.toSubIndex(false);
    index.append(subtree.toSubIndex(false));
    return index;
  }

  /**
   * Checks whether bit n of the supplied OctetString is set or not.
   * @param n
   *    denotes the bit to check starting from zero.
   * @param os OctetString
   * @return boolean
   */
  private static boolean isBitSet(final int n, final OctetString os) {
    return os.length() <= n / 8 || (os.get(n / 8) & (0x01 << (7 - (n % 8)))) > 0;
  }

  private List<MOTableRow> getAccessEntries(OctetString groupName) {
    OctetString upperBound = new OctetString(groupName);
    byte last = -1;
    if (upperBound.length() > 0) {
      last = upperBound.get(upperBound.length() - 1);
    }
    if (last == -1) {
      upperBound.append((byte)0);
    }
    else {
      upperBound.set(upperBound.length()-1, (byte)(last+1));
    }
    OID lowerOID = groupName.toSubIndex(false);
    OID upperOID = upperBound.toSubIndex(false);
    return vacmAccessTableModel.getRows(lowerOID, upperOID);
  }

  private List getViews(OctetString viewName) {
    if (viewName.length() == 0) {
      return Collections.EMPTY_LIST;
    }
    OctetString upperBound = new OctetString(viewName);
    byte last = upperBound.get(upperBound.length()-1);
    if (last == -1) {
      upperBound.append((byte)0);
    }
    else {
      upperBound.set(upperBound.length()-1, (byte)(last+1));
    }
    OID lowerOID = viewName.toSubIndex(false);
    OID upperOID = upperBound.toSubIndex(false);
    return vacmViewTreeFamilyTableModel.getRows(lowerOID, upperOID);
  }

  public static class VacmContextIterator implements Iterator<MOTableRow> {

    private int index = 0;
    private OctetString[] contexts;

    VacmContextIterator(OctetString[] contexts, int offset) {
      this.contexts = contexts;
      this.index = offset;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
      return (index < contexts.length);
    }

    public MOTableRow next() {
      if (index < contexts.length) {
        OctetString context = contexts[index++];
        return new DefaultMOTableRow(context.toSubIndex(false),
                              new Variable[] { context });
      }
      throw new NoSuchElementException();
    }

  }

  public MOTable getVacmSecurityToGroupTable() {
      return vacmSecurityToGroupTable;
  }


  private static OctetString getContextFromIndex(OID index) {
    if (index.size() > 0) {
      return new OctetString(index.toByteArray(), 1, index.size() - 1);
    }
    return new OctetString();
  }


  class VacmContextTableModel implements MOTableModel<MOTableRow> {

    public int getColumnCount() {
      return 1;
    }

    public int getRowCount() {
      int n = 0;
      for (MOServer aServer : server) {
        n += aServer.getContexts().length;
      }
      return n;
    }

    @Override
    public boolean isEmpty() {
      for (MOServer aServer : server) {
        if (aServer.getContexts().length > 0) {
          return false;
        }
      }
      return true;
    }

    public boolean containsRow(OID index) {
      return isContextSupported(getContextFromIndex(index));
    }

    public MOTableRow getRow(OID index) {
      if (index == null) {
        return null;
      }
      OctetString context = getContextFromIndex(index);
      if (isContextSupported(context)) {
        return new DefaultMOTableRow(index, new Variable[] { context });
      }
      return null;
    }

    public Iterator<MOTableRow> iterator() {
      return tailIterator(new OID());
    }

    public Iterator<MOTableRow> tailIterator(OID lowerBound) {
      OctetString[] contexts = getContexts();
      if (contexts == null) {
        return new VacmContextIterator(new OctetString[0], 0);
      }
      Arrays.<OctetString>sort(contexts, new LexicographicOctetStringComparator());
      int offset = 0;
      if (lowerBound != null) {
        offset = Arrays.binarySearch(contexts, getContextFromIndex(lowerBound));
      }
      if (offset < 0) {
        offset = -(offset+1);
      }
      return new VacmContextIterator(contexts, offset);
    }

    private OctetString[] getContexts() {
      ArrayList<OctetString> ctx = new ArrayList<OctetString>();
      for (MOServer aServer : server) {
        ctx.addAll(Arrays.asList(aServer.getContexts()));
      }
      return ctx.toArray(new OctetString[ctx.size()]);
    }

    public OID lastIndex() {
      OctetString[] contexts = getContexts();
      if ((contexts == null) || (contexts.length == 0)) {
        return null;
      }
      Arrays.sort(contexts, new LexicographicOctetStringComparator());
      return contexts[contexts.length-1].toSubIndex(false);
    }

    public OID firstIndex() {
      OctetString[] contexts = getContexts();
      if ((contexts == null) || (contexts.length == 0)) {
        return null;
      }
      Arrays.sort(contexts, new LexicographicOctetStringComparator());
      return contexts[0].toSubIndex(false);
    }

    public MOTableRow firstRow() {
      return getRow(firstIndex());
    }

    public MOTableRow lastRow() {
      return getRow(lastIndex());
    }
  }

}
