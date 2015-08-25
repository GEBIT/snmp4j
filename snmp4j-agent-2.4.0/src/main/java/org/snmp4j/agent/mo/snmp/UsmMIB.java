/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - UsmMIB.java  
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

import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.event.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.PDU;
import org.snmp4j.agent.mo.DefaultMOTable.ChangeSet;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import java.util.Vector;

/**
 * The <code>UsmMIB</code> implements the SNMP-USER-BASED-SM-MIB defined in
 * RFC 3414. The MIB implementation is backed by a {@link USM} instance.
 * The configuration of the user based security model can be changed
 * using the underlying {@link USM} or via SNMP but at
 * least one user must be created programatically in order to allow any access
 * to the agent via SNMP.
 * <p>
 * When modifying the USM after having created this MIB, you will have to
 * register this object as {@link UsmUserListener} to the USM.
 * <p>
 * By using SNMP, a new users can only be created by cloning it from an existing
 * user with the same or higher security level.
 *
 * @author Frank Fock
 * @version 1.2
 */
public class UsmMIB
    implements
    MOGroup,
    CounterListener,
    MOValueValidationListener,
    MOTableRowListener<MOMutableRow2PC>,
    UsmUserListener {

  private static final LogAdapter logger = LogFactory.getLogger(UsmMIB.class);

  public static final OID noAuthProtocol =
      new OID(new int[] {1,3,6,1,6,3,10,1,1,1});
  public static final OID noPrivProtocol =
      new OID(new int[] {1,3,6,1,6,3,10,1,2,1});

  public static final OID usmUserSpinLockOID =
      new OID(new int[] { 1,3,6,1,6,3,15,1,2,1,0 });
  public static final OID usmUserEntryOID =
      new OID(new int[] { 1,3,6,1,6,3,15,1,2,2,1 });


  public static final int colUsmUserSecurityName = 0;
  public static final int colUsmUserCloneFrom = 1;
  public static final int colUsmUserAuthProtocol = 2;
  public static final int colUsmUserAuthKeyChange = 3;
  public static final int colUsmUserOwnAuthKeyChange = 4;
  public static final int colUsmUserPrivProtocol = 5;
  public static final int colUsmUserPrivKeyChange = 6;
  public static final int colUsmUserOwnPrivKeyChange = 7;
  public static final int colUsmUserPublic = 8;
  public static final int colUsmUserStorageType = 9;
  public static final int colUsmUserStatus = 10;

  // hidden virtual columns
  public static final int colUsmUserAuthPassword = 11;
  public static final int colUsmUserPrivPassword = 12;
  public static final int colUsmUserLocalizationEngineID = 13;
  public static final int colUsmUserAuthKey = 14;
  public static final int colUsmUserPrivKey = 15;

  private static final int[][] keyChangeColumns =
      { {colUsmUserAuthKeyChange, colUsmUserOwnAuthKeyChange},
        {colUsmUserPrivKeyChange, colUsmUserOwnPrivKeyChange}
  };
  private USM usm;
  private SecurityProtocols securityProtocols;

  private static final OID usmStatsPrefix =
      new OID(SnmpConstants.usmStatsUnsupportedSecLevels.getValue(), 0,
              SnmpConstants.usmStatsUnsupportedSecLevels.size()-2);

  private static final OID[] usmStatOIDs = new OID[] {
      SnmpConstants.usmStatsUnsupportedSecLevels,
      SnmpConstants.usmStatsNotInTimeWindows,
      SnmpConstants.usmStatsUnknownUserNames,
      SnmpConstants.usmStatsUnknownEngineIDs,
      SnmpConstants.usmStatsWrongDigests,
      SnmpConstants.usmStatsDecryptionErrors
  };

  private MOScalar[] usmStats;
  private TestAndIncr usmUserSpinLock;
  private DefaultMOTable<MOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<MOMutableRow2PC>> usmUserEntry;
  private UsmTableModel usmUserTableModel;

  private volatile boolean usmEventProcessing;

  /**
   * Creates a USM MIB implementation connected to the supplied USM. The MIB
   * contents will reflect any changes to the USM after completion of this
   * constructor if you register this object as {@link UsmUserListener} to the
   * USM!
   * @param usm
   *   a User-based Security Model.
   * @param securityProtocols
   *   the supported <code>SecurityProtocols</code>.
   */
  public UsmMIB(USM usm, SecurityProtocols securityProtocols) {
    this.usm = usm;
    this.securityProtocols = securityProtocols;
    usm.getCounterSupport().addCounterListener(this);
    createUsmStats();
    createUsmUser();
  }

  private void createUsmUser() {
    usmUserSpinLock = new TestAndIncr(usmUserSpinLockOID);
    MOTableSubIndex[] usmUserSubIndexes = new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 5, 32),
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
    };
    MOColumn[] usmUserColumns = new MOColumn[] {
        new SnmpAdminString(colUsmUserSecurityName + 3,
                            MOAccessImpl.ACCESS_READ_ONLY,
                            null, false),
        new UsmRowPointer(colUsmUserCloneFrom + 3,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null, true),
        new AutonomousType(colUsmUserAuthProtocol + 3,
                           MOAccessImpl.ACCESS_READ_CREATE,
                           noAuthProtocol, true),
        new UsmKeyChange(colUsmUserAuthKeyChange + 3,
                         MOAccessImpl.ACCESS_READ_CREATE,
                         UsmKeyChange.AUTH_KEY_CHANGE),
        new UsmOwnKeyChange(colUsmUserOwnAuthKeyChange + 3,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            UsmKeyChange.AUTH_KEY_CHANGE),
        new AutonomousType(colUsmUserPrivProtocol + 3,
                           MOAccessImpl.ACCESS_READ_CREATE,
                           noPrivProtocol, true),
        new UsmKeyChange(colUsmUserPrivKeyChange + 3,
                         MOAccessImpl.ACCESS_READ_CREATE,
                         UsmKeyChange.PRIV_KEY_CHANGE),
        new UsmOwnKeyChange(colUsmUserOwnPrivKeyChange + 3,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            UsmKeyChange.PRIV_KEY_CHANGE),
        new SnmpAdminString(colUsmUserPublic + 3,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new OctetString(), true, 0, 32),
        new StorageType(colUsmUserStorageType + 3,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(StorageType.nonVolatile), true),
        new RowStatus(colUsmUserStatus + 3, MOAccessImpl.ACCESS_READ_CREATE)
    };
    MOTableIndex usmUserIndex = new MOTableIndex(usmUserSubIndexes, false);
    usmUserTableModel = new UsmTableModel(usmUserIndex);
    usmUserEntry = new DefaultMOTable<MOMutableRow2PC,MOColumn,DefaultMOMutableTableModel<MOMutableRow2PC>>
        (usmUserEntryOID, usmUserIndex, usmUserColumns, usmUserTableModel) {
      protected void fireRowChanged(MOTableRowEvent<MOMutableRow2PC> event) {
        if (super.moTableRowListeners != null) {
          Vector<MOTableRowListener<MOMutableRow2PC>> listeners = super.moTableRowListeners;
          for (MOTableRowListener<MOMutableRow2PC> l : listeners) {
            if ((!usmEventProcessing) || (!(l instanceof UsmMIB))) {
              l.rowChanged(event);
            }
          }
        }
      }

    };
    ((AutonomousType)
     usmUserColumns[colUsmUserAuthProtocol]).addMOValueValidationListener(this);
    ((AutonomousType)
     usmUserColumns[colUsmUserPrivProtocol]).addMOValueValidationListener(this);
    ((UsmRowPointer)
     usmUserColumns[colUsmUserCloneFrom]).setTargetTable(usmUserEntry);
    usmUserEntry.addMOTableRowListener(this);
  }

  private void createUsmStats() {
    usmStats = new MOScalar[usmStatOIDs.length];
    for (int i=0; i<usmStats.length; i++) {
      usmStats[i] = new MOScalar<Counter32>(usmStatOIDs[i], MOAccessImpl.ACCESS_READ_ONLY,
                                 new Counter32(0));
    }
  }

  public void registerMOs(MOServer server, OctetString context) throws
      DuplicateRegistrationException {
    for (MOScalar usmStat : usmStats) {
      server.register(usmStat, context);
    }
    server.register(usmUserSpinLock, context);
    server.register(usmUserEntry, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    for (MOScalar usmStat : usmStats) {
      server.unregister(usmStat, context);
    }
    server.unregister(usmUserSpinLock, context);
    server.unregister(usmUserEntry, context);
  }

  public void incrementCounter(CounterEvent event) {
    if ((event.getOid().startsWith(usmStatsPrefix)) &&
        (event.getOid().size() > usmStatsPrefix.size())) {
      Counter32 current = (Counter32)
           usmStats[event.getOid().get(usmStatsPrefix.size())-1].getValue();
      current.increment();
      event.setCurrentValue((Counter32)current.clone());
    }
  }


  public void validate(MOValueValidationEvent validationEvent) {
    if (validationEvent.getSource() instanceof MOColumn) {
      MOColumn col = (MOColumn) validationEvent.getSource();
      switch (col.getColumnID()-4) {
        case colUsmUserAuthProtocol: {
          OID value = (OID)validationEvent.getNewValue();
          if (!noAuthProtocol.equals(value)) {
            AuthenticationProtocol authProtocol =
                SecurityProtocols.getInstance().
                getAuthenticationProtocol((OID) validationEvent.getNewValue());
            if (authProtocol == null) {
              validationEvent.setValidationStatus(SnmpConstants.
                                                  SNMP_ERROR_WRONG_VALUE);
            }
          }
          break;
        }
        case colUsmUserPrivProtocol: {
          OID value = (OID)validationEvent.getNewValue();
          if (!noPrivProtocol.equals(value)) {
            PrivacyProtocol privProtocol =
                SecurityProtocols.getInstance().getPrivacyProtocol(value);
            if (privProtocol == null) {
              validationEvent.setValidationStatus(SnmpConstants.
                                                  SNMP_ERROR_WRONG_VALUE);
            }
          }
          break;
        }
      }
    }
  }

  private Variable[] getValuesFromUsmUser(UsmUserEntry user) {
    Variable[] row = new Variable[usmUserEntry.getColumnCount()+5];
    int n = 0;
    row[n++] = user.getUsmUser().getSecurityName();
    row[n++] = null;
    row[n++] = user.getUsmUser().getAuthenticationProtocol();
    row[n++] = null;
    row[n++] = null;
    row[n++] = user.getUsmUser().getPrivacyProtocol();
    row[n++] = null;
    row[n++] = null;
    row[n++] = new OctetString();
    row[n++] = new Integer32(StorageType.nonVolatile);
    row[n++] = new Integer32(RowStatus.active);
    row[n++] = user.getUsmUser().getAuthenticationPassphrase();
    row[n++] = user.getUsmUser().getPrivacyPassphrase();
    row[n++] = user.getUsmUser().getLocalizationEngineID();
    row[n++] = (user.getAuthenticationKey() == null) ?
        null : new OctetString(user.getAuthenticationKey());
    row[n  ] = (user.getPrivacyKey() == null) ?
        null : new OctetString(user.getPrivacyKey());
    return row;
  }

  private OID createIndex(OctetString engineID, OctetString userName) {
    if (engineID == null || engineID.length() == 0) {
      engineID = usm.getLocalEngineID();
    }
    OID index = engineID.toSubIndex(false);
    index.append(userName.toSubIndex(false));
    return index;
  }

  public synchronized void usmUserChange(UsmUserEvent event) {
    usmEventProcessing = true;
    switch (event.getType()) {
      case UsmUserEvent.USER_ADDED: {
        Variable[] values = getValuesFromUsmUser(event.getUser());
        OID index = createIndex(event.getUser().getEngineID(),
                                event.getUser().getUserName());
        MOMutableRow2PC row = usmUserEntry.createRow(index, values);
        usmUserEntry.addRow(row);
        break;
      }
      case UsmUserEvent.USER_REMOVED: {
        if (event.getUser() == null) {
          usmUserTableModel.clear();
        }
        else {
          OID index = createIndex(event.getUser().getEngineID(),
                                  event.getUser().getUserName());
          usmUserEntry.removeRow(index);
        }
        break;
      }
      case UsmUserEvent.USER_CHANGED: {
        Variable[] values = getValuesFromUsmUser(event.getUser());
        OID index = createIndex(event.getUser().getEngineID(),
            event.getUser().getUserName());
        MOMutableRow2PC row = usmUserEntry.getModel().getRow(index);
        if (row != null) {
          for (int i=0; i<values.length; i++) {
            row.setValue(i, values[i]);
          }
        }
        else {
          row = usmUserEntry.createRow(index, values);
          usmUserEntry.addRow(row);
        }
        break;
      }
    }
    usmEventProcessing = false;
  }

  private static byte[] getKey(UsmUserEntry entry, int authVsPriv) {
    return (authVsPriv == 0) ?
        entry.getAuthenticationKey() :
        entry.getPrivacyKey();
  }

  public class UsmTableModel extends DefaultMOMutableTableModel<MOMutableRow2PC> {

    private MOTableIndex indexDef;

    public UsmTableModel(MOTableIndex indexDef) {
      super();
      this.indexDef = indexDef;
    }

    public MOMutableRow2PC createRow(OID index, Variable[] values) {
      if (values.length < colUsmUserPrivKey + 1) {
        Variable[] h = new Variable[colUsmUserPrivKey + 1];
        System.arraycopy(values, 0, h, 0, values.length);
        values = h;
      }
      return new UsmTableRow(this, index, values);
    }

    public MOTableIndex getIndexDef() {
      return indexDef;
    }
  }

  private static boolean isKeyChanged(MOTableRow changeSet, int keyChangeColumn) {
    OctetString value = (OctetString) changeSet.getValue(keyChangeColumn);
    return (value != null) && (value.length() > 0);
  }

  private static boolean isKeyChanged(MOTableRow changeSet) {
    return ((isKeyChanged(changeSet, colUsmUserOwnAuthKeyChange)) ||
            (isKeyChanged(changeSet, colUsmUserAuthKeyChange)) ||
            (isKeyChanged(changeSet, colUsmUserOwnPrivKeyChange)) ||
            (isKeyChanged(changeSet, colUsmUserPrivKeyChange)));
  }

  public class UsmTableRow extends DefaultMOMutableRow2PC {

    private UsmTableModel tableModel;
    private boolean cloned = false;

    public UsmTableRow(UsmTableModel model, OID index, Variable[] values) {
      super(index, values);
      this.tableModel = model;
    }

    public void setCloned(boolean cloned) {
      this.cloned = cloned;
    }

    public boolean isCloned() {
      return cloned;
    }

    public MOTableIndex getIndexDef() {
      return tableModel.getIndexDef();
    }

    public AuthenticationProtocol getAuthProtocol(MOTableRow changeSet)
    {
      OID authOID = getAuthProtocolOID(changeSet);
      AuthenticationProtocol a =
          securityProtocols.getAuthenticationProtocol(authOID);
      return a;
    }

    public PrivacyProtocol getPrivProtocol(MOTableRow changeSet)
    {
      OID privOID = getPrivProtocolOID(changeSet);
      return securityProtocols.getPrivacyProtocol(privOID);
    }

    /**
     * Gets the OID of the privacy protocol defined by the given
     * change set. If the change set defines {@link #noPrivProtocol}
     * <code>null</code> is returned.
     * @param preparedChanges
     *    a TableRow instance with UsmTableRow values.
     * @return
     *    a privacy protocol OID or <code>null</code> if no
     *    privacy protocol is defined by <code>changeSet</code>.
     */
    public OID getPrivProtocolOID(MOTableRow preparedChanges)
    {
      OID privID = null;
      if (preparedChanges.getValue(colUsmUserCloneFrom) == null) {
        privID = (OID) preparedChanges.getValue(colUsmUserPrivProtocol);
      }
      if (privID == null) {
        privID = (OID) getValue(colUsmUserPrivProtocol);
      }
      if (noPrivProtocol.equals(privID)) {
        privID = null;
      }
      return privID;
    }

    public void prepare(SubRequest subRequest,
                        MOTableRow preparedChanges, int column) {
      switch (column) {
        case colUsmUserAuthProtocol: {
          OID authProtocol = (OID) subRequest.getVariableBinding().getVariable();
          if (!authProtocol.equals(noAuthProtocol)) {
            subRequest.getStatus().setErrorStatus(PDU.inconsistentValue);
          }
          else {
            OID privProtocol = null;
            Variable privProtocolVariable =
                preparedChanges.getValue(colUsmUserPrivProtocol);
            if (privProtocolVariable instanceof OID) {
              privProtocol = (OID) privProtocolVariable;
            }
            else if (privProtocolVariable == null) {
              privProtocol = (OID) getValue(colUsmUserPrivProtocol);
            }
            if ((privProtocol == null) ||
                (!privProtocol.equals(noPrivProtocol))) {
              subRequest.getStatus().setErrorStatus(PDU.inconsistentValue);
            }
          }
          break;
        }
        case colUsmUserPrivProtocol: {
          OID privProtocol = (OID)subRequest.getVariableBinding().getVariable();
          if (!privProtocol.equals(noPrivProtocol)) {
            subRequest.getStatus().setErrorStatus(PDU.inconsistentValue);
          }
          break;
        }
      }
    }

    private OID getCloneFromIndex(MOTableRow changeSet) {
      OID cloneFrom = (OID) changeSet.getValue(colUsmUserCloneFrom);
      if (cloneFrom == null) {
        cloneFrom = (OID) getValue(colUsmUserCloneFrom);
      }
      if ((cloneFrom == null) || (cloneFrom.size() <= usmUserEntryOID.size())) {
        return null;
      }
      return new OID(cloneFrom.getValue(), usmUserEntryOID.size()+1,
                     cloneFrom.size() - (usmUserEntryOID.size()+1));
    }

    public synchronized void commitRow(SubRequest subRequest,
                                       MOTableRow changeSet) {
      if (subRequest.hasError()) {
        return;
      }
      Variable[] indexValues = getIndexDef().getIndexValues(getIndex());
      OctetString engineID = (OctetString) indexValues[0];
      OctetString userName = (OctetString) indexValues[1];
      UsmUserEntry oldUserEntry;
      OID cloneFromUserIndex = getCloneFromIndex(changeSet);
      if (cloneFromUserIndex != null) {
        Variable[] cloneFromIndexValues =
            getIndexDef().getIndexValues(cloneFromUserIndex);
        OctetString cloneFromEngineID = (OctetString) cloneFromIndexValues[0];
        OctetString cloneFromUserName = (OctetString) cloneFromIndexValues[1];

        oldUserEntry = usm.getUser(cloneFromEngineID, cloneFromUserName);
        // assign protocols
        if (oldUserEntry != null) {
          setValue(colUsmUserAuthProtocol,
                   oldUserEntry.getUsmUser().getAuthenticationProtocol());
          setValue(colUsmUserPrivProtocol,
                   oldUserEntry.getUsmUser().getPrivacyProtocol());
        }
      }
      else {
        oldUserEntry = usm.getUser(engineID, userName);
      }
      Integer32 newStatus =
          (Integer32)changeSet.getValue(colUsmUserStatus);
      if (((newStatus != null) &&
           ((newStatus.getValue() == RowStatus.active) ||
            (newStatus.getValue() == RowStatus.createAndGo))) ||
          ((getValue(colUsmUserStatus) != null) &&
           (((Integer32)getValue(colUsmUserStatus)).getValue() == RowStatus.active) &&
           (isKeyChanged(changeSet)))) {
        if (cloneFromUserIndex != null) {
          // save undo value
          setUserObject(oldUserEntry);
        }
        if (oldUserEntry == null) {
          subRequest.getStatus().setErrorStatus(PDU.commitFailed);
          return;
        }
        OctetString[] newKeys = new OctetString[2];
        OctetString[] oldKeys = new OctetString[2];

        AuthenticationProtocol a = getAuthProtocol(changeSet);
        if (a != null) {
          for (int p=0; p<2; p++) {
            byte[] k = getKey(oldUserEntry, p);
            oldKeys[p] = null;
            if (k != null) {
              oldKeys[p] = new OctetString(k);
              for (int i = 0; i < keyChangeColumns[p].length; i++) {
                OctetString keyChange =
                    (OctetString)getValue(keyChangeColumns[p][i]);
                if ((keyChange != null) && (keyChange.length() > 0)) {
                  int keyLength = a.getDigestLength();

                  OctetString doKey = oldKeys[p];
                  if (p == 1) {
                    // privacy protocol key change
                    PrivacyProtocol privProtocol = getPrivProtocol(changeSet);
                    keyLength = privProtocol.getMaxKeyLength();
                  }
                  newKeys[p] =
                      KeyChange.changeKey(a, doKey, keyChange, keyLength);
                  break; // only one key change per protocol
                }
              }
            }
          }
        }
        UsmUserEntry newEntry =
            new UsmUserEntry(engineID.getValue(), userName,
                             getAuthProtocolOID(changeSet),
                             (newKeys[0] == null) ?
                             ((oldKeys[0] == null) ? null : oldKeys[0].getValue())
                             : newKeys[0].getValue(),
                             getPrivProtocolOID(changeSet),
                             (newKeys[1] == null) ?
                             ((oldKeys[1] == null) ? null : oldKeys[1].getValue())
                             : newKeys[1].getValue());
        usm.updateUser(newEntry);
        setValue(colUsmUserCloneFrom, null);
        setValue(colUsmUserAuthKeyChange, null);
        setValue(colUsmUserOwnAuthKeyChange, null);
        setValue(colUsmUserPrivKeyChange, null);
        setValue(colUsmUserOwnPrivKeyChange, null);
      }
      if (newStatus != null) {
        switch (newStatus.getValue()) {
          case RowStatus.createAndWait:
          case RowStatus.createAndGo: {
            setValue(colUsmUserSecurityName, userName);
            break;
          }
        }
      }
    }

    /**
     * Gets the OID of the authentication protocol defined by the given
     * change set. If the change set defines {@link #noAuthProtocol}
     * <code>null</code> is returned.
     * @param changeSet
     *    a TableRow instance with UsmTableRow values.
     * @return
     *    an authentication protocol OID or <code>null</code> if no
     *    authentication protocol is defined by <code>changeSet</code>.
     */
    public OID getAuthProtocolOID(MOTableRow changeSet) {
      OID authID = null;
      if (changeSet.getValue(colUsmUserCloneFrom) == null) {
        authID = (OID) changeSet.getValue(colUsmUserAuthProtocol);
      }
      if (authID == null) {
        authID = (OID) getValue(colUsmUserAuthProtocol);
      }
      if (noAuthProtocol.equals(authID)) {
        authID = null;
      }
      return authID;
    }

    public void cleanupRow(SubRequest subRequest, ChangeSet changeSet) {
      setUserObject(null);
    }

    public void undoRow(SubRequest subRequest, ChangeSet changeSet) {
      if (getUserObject() != null) {
        usm.updateUser((UsmUserEntry)getUserObject());
      }
    }

    public Variable getValue(int column) {
      if (column > colUsmUserStatus) {
        UsmUserEntry userEntry = getUserEntry();
        if (userEntry != null) {
          switch (column) {
            case colUsmUserAuthPassword:
              setValue(column,
                       userEntry.getUsmUser().getAuthenticationPassphrase());
              break;
            case colUsmUserPrivPassword:
              setValue(column, userEntry.getUsmUser().getPrivacyPassphrase());
              break;
            case colUsmUserLocalizationEngineID:
              setValue(column, (userEntry.getUsmUser().isLocalized()) ?
                       userEntry.getUsmUser().getLocalizationEngineID() : null);
              break;
            case colUsmUserAuthKey: {
              byte[] key = userEntry.getAuthenticationKey();
              setValue(column, (key == null) ? null : new OctetString(key));
              break;
            }
            case colUsmUserPrivKey: {
              byte[] key = userEntry.getPrivacyKey();
              setValue(column, (key == null) ? null : new OctetString(key));
              break;
            }
          }
        }
      }
      return super.getValue(column);
    }

    public boolean containsHiddenValues() {
      for (int i=colUsmUserStatus+1; i<values.length; i++) {
        if (values[i] != null) {
          return true;
        }
      }
      return false;
    }

    private UsmUserEntry getUserEntry() {
      Variable[] indexValues = getIndexDef().getIndexValues(getIndex());
      OctetString engineID = (OctetString) indexValues[0];
      OctetString userName = (OctetString) indexValues[1];
      return usm.getUser(engineID, userName);
    }

    private OctetString[] getIndexValues() {
      Variable[] indexValues = getIndexDef().getIndexValues(getIndex());
      OctetString[] idxValues = new OctetString[2];
      idxValues[0] = (OctetString) indexValues[0];
      idxValues[1] = (OctetString) indexValues[1];
      return idxValues;
    }
  }

  public DefaultMOTable getUsmUserEntry() {
    return usmUserEntry;
  }

  class UsmRowPointer extends RowPointer {

    public UsmRowPointer(int columnID, MOAccess access, OID defaultValue,
                         boolean mutableInService) {
      super(columnID, access, defaultValue, mutableInService);
    }



    public void prepare(SubRequest subRequest, MOTableRow row,
                        MOTableRow changeSet, int column) {
      super.prepare(subRequest, row, changeSet, column);
      if (!subRequest.hasError()) {
        OID rowPointer = (OID) subRequest.getVariableBinding().getVariable();
        MOTableCellInfo cell = getTargetTable().getCellInfo(rowPointer);
        if (cell.getIndex().equals(row.getIndex())) {
          // cannot clone from self
          subRequest.getStatus().setErrorStatus(PDU.inconsistentValue);
        }
      }
    }

    public OID getValue(MOTableRow row, int column) {
      return SnmpConstants.zeroDotZero;
    }
  }

  public synchronized void rowChanged(MOTableRowEvent event) {
    UsmTableRow row = (UsmTableRow) event.getRow();
    if (event.getType() == MOTableRowEvent.CREATE) {
      // check if event needs to be processed
      if (!row.containsHiddenValues() &&
          ((row.getAuthProtocolOID(row) != null) ||
           (row.getPrivProtocolOID(row) != null))) {
        return;
      }
      // loaded row
      UsmUserEntry entry = row.getUserEntry();
      if (entry != null) {
        usm.removeAllUsers(entry.getUserName(), entry.getEngineID());
      }
      else {
        OctetString[] idxValues = row.getIndexValues();
        if ((row.size() > colUsmUserLocalizationEngineID) &&
            (row.getValue(colUsmUserAuthPassword) != null)) {
          UsmUser user = new UsmUser(idxValues[1],
                                     row.getAuthProtocolOID(row),
                                     (OctetString)row.getValue(colUsmUserAuthPassword),
                                     row.getPrivProtocolOID(row),
                                     (OctetString)row.getValue(colUsmUserPrivPassword),
                                     (OctetString)row.getValue(colUsmUserLocalizationEngineID));
          usm.addUser(idxValues[1], idxValues[0], user);
        }
        else if (row.size() > colUsmUserPrivKey) {
          OctetString authKey =
              (OctetString)row.getValue(colUsmUserAuthKey);
          OctetString privKey =
              (OctetString)row.getValue(colUsmUserPrivKey);
          usm.addLocalizedUser(idxValues[0].getValue(),
                               idxValues[1],
                               row.getAuthProtocolOID(row),
                               (authKey == null) ? null : authKey.getValue(),
                               row.getPrivProtocolOID(row),
                               (privKey == null) ? null : privKey.getValue());
        }
        else {
          logger.warn("Cannot add user '"+idxValues[1]+
                      "' to the USM from USM-MIB because key "+
                      "information is missing");
        }
      }
    }
    else if (event.getType() == MOTableRowEvent.DELETE) {
      UsmUserEntry entry = row.getUserEntry();
      if (entry != null) {
        usm.removeAllUsers(entry.getUserName(), entry.getEngineID());
      }
    }
  }
}
