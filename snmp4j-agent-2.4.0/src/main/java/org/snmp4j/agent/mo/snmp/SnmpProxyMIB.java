/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpProxyMIB.java  
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
import org.snmp4j.agent.mo.snmp.*;
import java.util.List;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class SnmpProxyMIB
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  // Constants

  // Scalars

  // Tables
  public static final OID oidSnmpProxyEntry =
    new OID(new int[] { 1,3,6,1,6,3,14,1,2,1 });

  // Column sub-identifer defintions for snmpProxyEntry:
  public static final int colSnmpProxyType = 2;
  public static final int colSnmpProxyContextEngineID = 3;
  public static final int colSnmpProxyContextName = 4;
  public static final int colSnmpProxyTargetParamsIn = 5;
  public static final int colSnmpProxySingleTargetOut = 6;
  public static final int colSnmpProxyMultipleTargetOut = 7;
  public static final int colSnmpProxyStorageType = 8;
  public static final int colSnmpProxyRowStatus = 9;

  // Column index defintions for snmpProxyEntry:
  public static final int idxSnmpProxyType = 0;
  public static final int idxSnmpProxyContextEngineID = 1;
  public static final int idxSnmpProxyContextName = 2;
  public static final int idxSnmpProxyTargetParamsIn = 3;
  public static final int idxSnmpProxySingleTargetOut = 4;
  public static final int idxSnmpProxyMultipleTargetOut = 5;
  public static final int idxSnmpProxyStorageType = 6;
  public static final int idxSnmpProxyRowStatus = 7;
  private static MOTableSubIndex[] snmpProxyEntryIndexes =
    new MOTableSubIndex[] {
        new MOTableSubIndex(SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static MOTableIndex snmpProxyEntryIndex =
      new MOTableIndex(snmpProxyEntryIndexes, true);

  private DefaultMOTable<SnmpProxyRow,MOColumn,DefaultMOMutableTableModel<SnmpProxyRow>>
      snmpProxyEntry;
  private DefaultMOMutableTableModel<SnmpProxyRow> snmpProxyEntryModel;

//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  public SnmpProxyMIB() {
    createSnmpProxyEntry();
  }


  private void createSnmpProxyEntry() {
    MOColumn[] snmpProxyEntryColumns = new MOColumn[8];
    snmpProxyEntryColumns[idxSnmpProxyType] =
      new Enumerated<Integer32>(colSnmpProxyType,
                     SMIConstants.SYNTAX_INTEGER32,
                     MOAccessImpl.ACCESS_READ_CREATE,
                     null,
                     true,
                     new int[] {  1, 2, 3, 4 });
    snmpProxyEntryColumns[idxSnmpProxyContextEngineID] =
      new MOMutableColumn(colSnmpProxyContextEngineID,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpProxyEntryColumns[idxSnmpProxyContextEngineID]).
      addMOValueValidationListener(new SnmpProxyContextEngineIDValidator());
    snmpProxyEntryColumns[idxSnmpProxyContextName] =
      new MOMutableColumn(colSnmpProxyContextName,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpProxyEntryColumns[idxSnmpProxyContextName]).
      addMOValueValidationListener(new SnmpProxyContextNameValidator());
    snmpProxyEntryColumns[idxSnmpProxyTargetParamsIn] =
      new MOMutableColumn(colSnmpProxyTargetParamsIn,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpProxyEntryColumns[idxSnmpProxyTargetParamsIn]).
      addMOValueValidationListener(new SnmpProxyTargetParamsInValidator());
    snmpProxyEntryColumns[idxSnmpProxySingleTargetOut] =
      new MOMutableColumn(colSnmpProxySingleTargetOut,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpProxyEntryColumns[idxSnmpProxySingleTargetOut]).
      addMOValueValidationListener(new SnmpProxySingleTargetOutValidator());
    snmpProxyEntryColumns[idxSnmpProxyMultipleTargetOut] =
      new MOMutableColumn(colSnmpProxyMultipleTargetOut,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          null,
                          true);
    ((MOMutableColumn)snmpProxyEntryColumns[idxSnmpProxyMultipleTargetOut]).
      addMOValueValidationListener(new SnmpProxyMultipleTargetOutValidator());
    snmpProxyEntryColumns[idxSnmpProxyStorageType] =
      new StorageType(colSnmpProxyStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(3),
                      true);
    snmpProxyEntryColumns[idxSnmpProxyRowStatus] =
      new RowStatus(colSnmpProxyRowStatus);

    snmpProxyEntry =
      new DefaultMOTable<SnmpProxyRow,MOColumn,DefaultMOMutableTableModel<SnmpProxyRow>>
          (oidSnmpProxyEntry, snmpProxyEntryIndex, snmpProxyEntryColumns);
    snmpProxyEntryModel = new DefaultMOMutableTableModel<SnmpProxyRow>();
    snmpProxyEntry.setModel(snmpProxyEntryModel);
    snmpProxyEntryModel.setRowFactory(new SnmpProxyRowFactory());
  }


  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.snmpProxyEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmpProxyEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars

  // Value Validators

  /**
   * The <code>SnmpProxyContextEngineIDValidator</code> implements the value
   * validation for <code>SnmpProxyContextEngineID</code>.
   */
  static class SnmpProxyContextEngineIDValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 5) && (os.length() <= 32)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpProxyContextEngineID::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpProxyContextNameValidator</code> implements the value
   * validation for <code>SnmpProxyContextName</code>.
   */
  static class SnmpProxyContextNameValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpProxyContextName::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpProxyTargetParamsInValidator</code> implements the value
   * validation for <code>SnmpProxyTargetParamsIn</code>.
   */
  static class SnmpProxyTargetParamsInValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpProxyTargetParamsIn::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpProxySingleTargetOutValidator</code> implements the value
   * validation for <code>SnmpProxySingleTargetOut</code>.
   */
  static class SnmpProxySingleTargetOutValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpProxySingleTargetOut::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>SnmpProxyMultipleTargetOutValidator</code> implements the value
   * validation for <code>SnmpProxyMultipleTargetOut</code>.
   */
  static class SnmpProxyMultipleTargetOutValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmpProxyMultipleTargetOut::validate
     //--AgentGen END
    }
  }

  // Enumerations

  public static final class SnmpProxyTypeEnum {
    public static final int read = 1;
    public static final int write = 2;
    public static final int trap = 3;
    public static final int inform = 4;
  }

  // Rows and Factories

  class SnmpProxyRowFactory implements MOTableRowFactory<SnmpProxyRow> {
    public SnmpProxyRowFactory() {
    }

    public SnmpProxyRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      return new SnmpProxyRow(index, values);
    }

    @Override
    public void freeRow(SnmpProxyRow row) {

    }
  }

  public class SnmpProxyRow extends DefaultMOMutableRow2PC {
    public SnmpProxyRow(OID index, Variable[] values) {
      super(index, values);
    }

    public Integer32 getSnmpProxyType() {
       return (Integer32) getValue(idxSnmpProxyType);
     }

     public void setSnmpProxyType(Integer32 newValue) {
       setValue(idxSnmpProxyType, newValue);
     }

     public OctetString getSnmpProxyContextEngineID() {
       return (OctetString) getValue(idxSnmpProxyContextEngineID);
     }

     public void setSnmpProxyContextEngineID(OctetString newValue) {
       setValue(idxSnmpProxyContextEngineID, newValue);
     }

     public OctetString getSnmpProxyContextName() {
       return (OctetString) getValue(idxSnmpProxyContextName);
     }

     public void setSnmpProxyContextName(OctetString newValue) {
       setValue(idxSnmpProxyContextName, newValue);
     }

     public OctetString getSnmpProxyTargetParamsIn() {
       return (OctetString) getValue(idxSnmpProxyTargetParamsIn);
     }

     public void setSnmpProxyTargetParamsIn(OctetString newValue) {
       setValue(idxSnmpProxyTargetParamsIn, newValue);
     }

     public OctetString getSnmpProxySingleTargetOut() {
       return (OctetString) getValue(idxSnmpProxySingleTargetOut);
     }

     public void setSnmpProxySingleTargetOut(OctetString newValue) {
       setValue(idxSnmpProxySingleTargetOut, newValue);
     }

     public OctetString getSnmpProxyMultipleTargetOut() {
       return (OctetString) getValue(idxSnmpProxyMultipleTargetOut);
     }

     public void setSnmpProxyMultipleTargetOut(OctetString newValue) {
       setValue(idxSnmpProxyMultipleTargetOut, newValue);
     }

     public Integer32 getSnmpProxyStorageType() {
       return (Integer32) getValue(idxSnmpProxyStorageType);
     }

     public void setSnmpProxyStorageType(Integer32 newValue) {
       setValue(idxSnmpProxyStorageType, newValue);
     }

     public Integer32 getSnmpProxyRowStatus() {
       return (Integer32) getValue(idxSnmpProxyRowStatus);
     }

     public void setSnmpProxyRowStatus(Integer32 newValue) {
       setValue(idxSnmpProxyRowStatus, newValue);
     }

  }



//--AgentGen BEGIN=_METHODS

  /**
   * Returns the proxy configuration table defined as "snmpProxyTable".
   * @return
   *    a MOTable instance.
   */
  public MOTable getSnmpProxyEntry()   {
    return snmpProxyEntry;
  }

  /**
   * Adds a proxy entry to the snmpProxyTable (descriptions taken from
   * SNMP-PROXY-MIB definition).
   * @param name
   *    The locally arbitrary, but unique identifier associated
   *    with this snmpProxyEntry.
   * @param type
   *    The type of message that may be forwarded using
   *    the translation parameters defined by this entry.
   *    See {@link SnmpProxyTypeEnum} for possible values.
   * @param contextEngineId
   *    The contextEngineID contained in messages that
   *    may be forwarded using the translation parameters
   *    defined by this entry.
   * @param contextName
   *    The contextName contained in messages that may be
   *    forwarded using the translation parameters defined
   *    by this entry.
   *    This object is optional, and if not supported, the
   *    contextName contained in a message is ignored when
   *    selecting an entry in the snmpProxyTable.
   * @param targetParamsIn
   *    This object selects an entry in the snmpTargetParamsTable.
   *    The selected entry is used to determine which row of the
   *    snmpProxyTable to use for forwarding received messages.
   * @param singleTargetOut
   *    This object selects a management target defined in the
   *    snmpTargetAddrTable (in the SNMP-TARGET-MIB).  The
   *    selected target is defined by an entry in the
   *    snmpTargetAddrTable whose index value (snmpTargetAddrName)
   *    is equal to this object.
   *    This object is only used when selection of a single
   *    target is required (i.e. when forwarding an incoming
   *    read or write request).
   * @param multipleTargetOut
   *    This object selects a set of management targets defined
   *    in the snmpTargetAddrTable (in the SNMP-TARGET-MIB).
   *    This object is only used when selection of multiple
   *    targets is required (i.e. when forwarding an incoming
   *    notification).
   * @param storageType
   *    The storage type of this conceptual row.
   *    Conceptual rows having the value 'permanent' need not
   *    allow write-access to any columnar objects in the row. See
   *    {@link StorageType} for possible values.
   * @return
   *    <code>true</code> if the row could be added, <code>false</code>
   *    otherwise.
   */
  public boolean addProxyEntry(OctetString name,
                               int type,
                               OctetString contextEngineId,
                               OctetString contextName,
                               OctetString targetParamsIn,
                               OctetString singleTargetOut,
                               OctetString multipleTargetOut,
                               int storageType)
  {
      Variable[] var = new
          Variable[snmpProxyEntry.getColumnCount()];
      OID index = name.toSubIndex(true);
      var[idxSnmpProxyType] = new Integer32(type);
      var[idxSnmpProxyContextEngineID] = contextEngineId;
      var[idxSnmpProxyContextName] = contextName;
      var[idxSnmpProxyTargetParamsIn] = targetParamsIn;
      var[idxSnmpProxySingleTargetOut] = singleTargetOut;
      var[idxSnmpProxyMultipleTargetOut] = multipleTargetOut;
      var[idxSnmpProxyStorageType] = new Integer32(storageType);
      var[idxSnmpProxyRowStatus] = new Integer32(RowStatus.active);
      SnmpProxyRow row = snmpProxyEntry.createRow(index, var);
      return snmpProxyEntry.addRow(row);
  }

  /**
   * Removes the proxy entry with the specified name.
   * @param name
   *    the name of the proxy entry to remove from the proxy configuration.
   * @return
   *    the removed row or <code>null</code> if no such row exists.
   */
  public SnmpProxyRow removeProxyEntry(OctetString name)   {
    OID index = name.toSubIndex(true);
    SnmpProxyRow removedRow = snmpProxyEntry.removeRow(index);
    return removedRow;
  }


  public List<SnmpProxyRow> getProxyRows(final int proxyType,
                                         final OctetString contextEngineID,
                                         final OctetString context) {
    MOTableRowFilter<SnmpProxyRow> filter = new MOTableRowFilter<SnmpProxyRow>() {
      public boolean passesFilter(SnmpProxyRow row) {
        int rowStatus = row.getSnmpProxyRowStatus().getValue();
        if (rowStatus != RowStatus.active) {
          return false;
        }
        int type = row.getSnmpProxyType().getValue();
        if (type != proxyType) {
          return false;
        }
        OctetString cengineID = row.getSnmpProxyContextEngineID();
        if (!cengineID.equals(contextEngineID)) {
          return false;
        }
        OctetString cname = row.getSnmpProxyContextName();
        return !((cname != null) && (!cname.equals(context)));
      }
    };
    return this.snmpProxyEntryModel.getRows(null, null, filter);
  }

//--AgentGen END

//--AgentGen BEGIN=_CLASSES

//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


