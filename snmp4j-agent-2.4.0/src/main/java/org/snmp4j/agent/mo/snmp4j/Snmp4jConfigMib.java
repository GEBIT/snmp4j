/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Snmp4jConfigMib.java  
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

import java.io.*;

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.io.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import java.util.Map;
import java.util.LinkedHashMap;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class Snmp4jConfigMib
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
//--AgentGen END
{

  private static final LogAdapter logger =
      LogFactory.getLogger(Snmp4jConfigMib.class);

  // Factory
  private static MOFactory moFactory = DefaultMOFactory.getInstance();

  // Constants
  public  static final OID oidSnmp4jCfgSecSrcAddrValidation =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,2,1,1,1,0 });

  // Scalars
  private MOScalar snmp4jCfgSecSrcAddrValidation;

  // Tables
  public  static final OID oidSnmp4jCfgStorageEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,2,1,2,1,1 });

  // Column sub-identifer defintions for snmp4jCfgStorageEntry:
  public  static final int colSnmp4jCfgStoragePath = 2;
  public  static final int colSnmp4jCfgStorageID = 10;
  public  static final int colSnmp4jCfgStorageLastStore = 4;
  public  static final int colSnmp4jCfgStorageLastRestore = 5;
  public  static final int colSnmp4jCfgStorageRestoreMode = 6;
  public  static final int colSnmp4jCfgStorageOperation = 7;
  public  static final int colSnmp4jCfgStorageStorageType = 8;
  public  static final int colSnmp4jCfgStorageStatus = 9;

  // Column index defintions for snmp4jCfgStorageEntry:
  public  static final int idxSnmp4jCfgStoragePath = 0;
  public  static final int idxSnmp4jCfgStorageID = 7;
  public  static final int idxSnmp4jCfgStorageLastStore = 1;
  public  static final int idxSnmp4jCfgStorageLastRestore = 2;
  public  static final int idxSnmp4jCfgStorageRestoreMode = 3;
  public  static final int idxSnmp4jCfgStorageOperation = 4;
  public  static final int idxSnmp4jCfgStorageStorageType = 5;
  public  static final int idxSnmp4jCfgStorageStatus = 6;
  private static MOTableSubIndex[] snmp4jCfgStorageEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 0, 255)
  };

  private static MOTableIndex snmp4jCfgStorageEntryIndex =
      moFactory.createIndex(snmp4jCfgStorageEntryIndexes,
                            false);

  private MOTable<Snmp4jCfgStorageEntryRow,MOColumn,MOMutableTableModel<Snmp4jCfgStorageEntryRow>>
      snmp4jCfgStorageEntry;
  private MOMutableTableModel<Snmp4jCfgStorageEntryRow> snmp4jCfgStorageEntryModel;


//--AgentGen BEGIN=_MEMBERS
  public static final OID PRIMARY_INDEX =
      new OctetString("primary").toSubIndex(false);

  protected SnmpCommunityMIB snmpCommunityMIB;
  protected Map<CharSequence, MOPersistenceProvider> persistenceProvider = new LinkedHashMap<CharSequence, MOPersistenceProvider>();
  protected MOPersistenceProvider primaryPersistence;
  protected SysUpTime sysUpTime;

  private File configPathRoot;

//--AgentGen END

  public Snmp4jConfigMib(SysUpTime sysUpTime) {
    this.sysUpTime = sysUpTime;
    snmp4jCfgSecSrcAddrValidation =
      new Snmp4jCfgSecSrcAddrValidation(oidSnmp4jCfgSecSrcAddrValidation,
                                        MOAccessImpl.ACCESS_READ_WRITE);
    createSnmp4jCfgStorageEntry();
    configPathRoot = new File(System.getProperty("user.dir", ""));
  }


  public MOTable getSnmp4jCfgStorageEntry() {
    return snmp4jCfgStorageEntry;
  }

  public MOPersistenceProvider getPrimaryPersistence() {
    return primaryPersistence;
  }

  @SuppressWarnings("unchecked")
  private void createSnmp4jCfgStorageEntry() {
    MOColumn[] snmp4jCfgStorageEntryColumns = new MOColumn[8];
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStoragePath] =
      new DisplayString<OctetString>(colSnmp4jCfgStoragePath,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        null,
                        true,
                        0, 255);
    ((MOMutableColumn)snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStoragePath]).
      addMOValueValidationListener(new Snmp4jCfgStoragePathValidator());
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageID] =
      new MOMutableColumn(colSnmp4jCfgStorageID,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString("default"));
    ((MOMutableColumn)snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageID]).
      addMOValueValidationListener(new Snmp4jCfgStorageFormatValidator());
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageLastStore] =
      new MOColumn(colSnmp4jCfgStorageLastStore,
                   SMIConstants.SYNTAX_TIMETICKS,
                   MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageLastRestore] =
      new MOColumn(colSnmp4jCfgStorageLastRestore,
                   SMIConstants.SYNTAX_TIMETICKS,
                   MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageRestoreMode] =
      new Enumerated<Integer32>(colSnmp4jCfgStorageRestoreMode,
                     SMIConstants.SYNTAX_INTEGER32,
                     MOAccessImpl.ACCESS_READ_CREATE,
                     new Integer32(1),
                     true,
                     new int[] { Snmp4jCfgStorageRestoreModeEnum.replaceAndCreate,
                                 Snmp4jCfgStorageRestoreModeEnum.updateAndCreate,
                                 Snmp4jCfgStorageRestoreModeEnum.updateOnly,
                                 Snmp4jCfgStorageRestoreModeEnum.createOnly });
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageOperation] =
      new Enumerated<Integer32>(colSnmp4jCfgStorageOperation,
                     SMIConstants.SYNTAX_INTEGER32,
                     MOAccessImpl.ACCESS_READ_CREATE,
                     new Integer32(1),
                     true,
                     new int[] { Snmp4jCfgStorageOperationEnum.idle,
                                 Snmp4jCfgStorageOperationEnum.inProgress,
                                 Snmp4jCfgStorageOperationEnum.store,
                                 Snmp4jCfgStorageOperationEnum.restore });
    ((MOMutableColumn)snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageOperation]).
      addMOValueValidationListener(new Snmp4jCfgStorageOperationValidator());
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageStorageType] =
      new StorageType(colSnmp4jCfgStorageStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(3),
                      true);
    snmp4jCfgStorageEntryColumns[idxSnmp4jCfgStorageStatus] =
      new RowStatus(colSnmp4jCfgStorageStatus);

    snmp4jCfgStorageEntryModel = new DefaultMOMutableTableModel<Snmp4jCfgStorageEntryRow>();
    snmp4jCfgStorageEntryModel.setRowFactory(new Snmp4jCfgStorageEntryRowFactory());
    snmp4jCfgStorageEntry =
      moFactory.createTable(oidSnmp4jCfgStorageEntry,
                            snmp4jCfgStorageEntryIndex,
                            snmp4jCfgStorageEntryColumns,
                            snmp4jCfgStorageEntryModel);
  }



  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.snmp4jCfgSecSrcAddrValidation, context);
    server.register(this.snmp4jCfgStorageEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmp4jCfgSecSrcAddrValidation, context);
    server.unregister(this.snmp4jCfgStorageEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars
  public class Snmp4jCfgSecSrcAddrValidation extends EnumeratedScalar<Integer32> {
    Snmp4jCfgSecSrcAddrValidation(OID oid, MOAccess access) {
      super(oid, access, new Integer32(),
            new int[] { Snmp4jCfgSecSrcAddrValidationEnum.enabled,
                        Snmp4jCfgSecSrcAddrValidationEnum.disabled,
                        Snmp4jCfgSecSrcAddrValidationEnum.notAvailable });
//--AgentGen BEGIN=snmp4jCfgSecSrcAddrValidation
//--AgentGen END
    }

    public Integer32 getValue() {
      if (snmpCommunityMIB != null) {
        if (snmpCommunityMIB.isSourceAddressFiltering()) {
          setValue(new Integer32(Snmp4jCfgSecSrcAddrValidationEnum.enabled));
        }
        else {
          setValue(new Integer32(Snmp4jCfgSecSrcAddrValidationEnum.disabled));
        }
      }
      else {
        setValue(new Integer32(Snmp4jCfgSecSrcAddrValidationEnum.notAvailable));
      }
      return (Integer32) super.getValue().clone();
    }

    public void commit(SubRequest request) {
     //--AgentGen BEGIN=snmp4jCfgSecSrcAddrValidation::commit
     Integer32 newValue =
         (Integer32) request.getVariableBinding().getVariable();
     switch (newValue.getValue()) {
       case Snmp4jCfgSecSrcAddrValidationEnum.disabled:
         snmpCommunityMIB.setSourceAddressFiltering(false);
         break;
       case Snmp4jCfgSecSrcAddrValidationEnum.enabled:
         snmpCommunityMIB.setSourceAddressFiltering(true);
         break;
       default:
         request.getRequest().setErrorStatus(PDU.commitFailed);
     }
     //--AgentGen END
      super.commit(request);
    }

    public void cleanup(SubRequest request) {
     //--AgentGen BEGIN=snmp4jCfgSecSrcAddrValidation::cleanup
     //--AgentGen END
      super.cleanup(request);
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
     //--AgentGen BEGIN=snmp4jCfgSecSrcAddrValidation::isValueOK
     if (snmpCommunityMIB != null) {
       switch (((Integer32)newValue).getValue()) {
         case Snmp4jCfgSecSrcAddrValidationEnum.disabled:
         case Snmp4jCfgSecSrcAddrValidationEnum.enabled:
           break;
         default:
           return PDU.wrongValue;
       }
     }
     else if (((Integer32)newValue).getValue() !=
              Snmp4jCfgSecSrcAddrValidationEnum.notAvailable) {
       return PDU.inconsistentValue;
     }
     else {
       return PDU.wrongValue;
     }
     //--AgentGen END
      return super.isValueOK(request);
    }
  }


  // Value Validators

  /**
   * The <code>Snmp4jCfgStoragePathValidator</code> implements the value
   * validation for <code>Snmp4jCfgStoragePath</code>.
   */
  static class Snmp4jCfgStoragePathValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jCfgStoragePath::validate
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jCfgStorageFormatValidator</code> implements the value
   * validation for <code>Snmp4jCfgStorageFormat</code>.
   */
  static class Snmp4jCfgStorageFormatValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jCfgStorageFormat::validate
     if (((Integer32)newValue).getValue() != Snmp4jCfgStorageFormatEnum.binary) {
       validationEvent.setValidationStatus(PDU.wrongValue);
     }
     //--AgentGen END
    }
  }
  /**
   * The <code>Snmp4jCfgStorageOperationValidator</code> implements the value
   * validation for <code>Snmp4jCfgStorageOperation</code>.
   */
  static class Snmp4jCfgStorageOperationValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jCfgStorageOperation::validate
     switch (newValue.toInt()) {
       case Snmp4jCfgStorageOperationEnum.restore:
       case Snmp4jCfgStorageOperationEnum.store:
         break;
       default:
         validationEvent.setValidationStatus(PDU.wrongValue);
     }
     //--AgentGen END
    }
  }

  // Enumerations
  public static final class Snmp4jCfgSecSrcAddrValidationEnum {
    public static final int enabled = 1;
    public static final int disabled = 2;
    public static final int notAvailable = 3;
  }

  public static final class Snmp4jCfgStorageFormatEnum {
    /* -- Default format */
    public static final int binary = 1;
    public static final int xml = 2;
  }
  public static final class Snmp4jCfgStorageRestoreModeEnum {
    public static final int replaceAndCreate = 1;
    public static final int updateAndCreate = 2;
    public static final int updateOnly = 3;
    public static final int createOnly = 4;
  }
  public static final class Snmp4jCfgStorageOperationEnum {
    /* -- no action */
    public static final int idle = 1;
    /* -- (re)store operation in progress */
    public static final int inProgress = 2;
    /* -- store current configuration */
    public static final int store = 3;
    /* -- restore configuration */
    public static final int restore = 4;
  }

  // Rows and Factories
  class Snmp4jCfgStorageEntryRowFactory
        implements MOTableRowFactory<Snmp4jCfgStorageEntryRow>
  {
    public Snmp4jCfgStorageEntryRowFactory() {}

    public Snmp4jCfgStorageEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      Snmp4jCfgStorageEntryRow row = new Snmp4jCfgStorageEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jCfgStorageEntry::createRow
     ((Integer32)values[idxSnmp4jCfgStorageOperation]).
         setValue(Snmp4jCfgStorageOperationEnum.idle);
     //--AgentGen END
      return row;
    }

    public void freeRow(Snmp4jCfgStorageEntryRow row) {
     //--AgentGen BEGIN=snmp4jCfgStorageEntry::freeRow
     //--AgentGen END
    }
  }

  class Snmp4jCfgStorageEntryRow extends DefaultMOMutableRow2PC {

    public Snmp4jCfgStorageEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public OctetString getSnmp4jCfgStoragePath() {
      return (OctetString) getValue(idxSnmp4jCfgStoragePath);
    }

    public void setSnmp4jCfgStoragePath(OctetString newValue) {
      setValue(idxSnmp4jCfgStoragePath, newValue);
    }

    public OctetString getSnmp4jCfgStorageID() {
      return (OctetString) getValue(idxSnmp4jCfgStorageID);
    }

    public void setSnmp4jCfgStorageID(OctetString newValue) {
      setValue(idxSnmp4jCfgStorageID, newValue);
    }

    public TimeTicks getSnmp4jCfgStorageLastStore() {
      return (TimeTicks) getValue(idxSnmp4jCfgStorageLastStore);
    }

    public void setSnmp4jCfgStorageLastStore(TimeTicks newValue) {
      setValue(idxSnmp4jCfgStorageLastStore, newValue);
    }

    public TimeTicks getSnmp4jCfgStorageLastRestore() {
      return (TimeTicks) getValue(idxSnmp4jCfgStorageLastRestore);
    }

    public void setSnmp4jCfgStorageLastRestore(TimeTicks newValue) {
      setValue(idxSnmp4jCfgStorageLastRestore, newValue);
    }

    public Integer32 getSnmp4jCfgStorageRestoreMode() {
      return (Integer32) getValue(idxSnmp4jCfgStorageRestoreMode);
    }

    public void setSnmp4jCfgStorageRestoreMode(Integer32 newValue) {
      setValue(idxSnmp4jCfgStorageRestoreMode, newValue);
    }

    public Integer32 getSnmp4jCfgStorageOperation() {
      return (Integer32) getValue(idxSnmp4jCfgStorageOperation);
    }

    public void setSnmp4jCfgStorageOperation(Integer32 newValue) {
      setValue(idxSnmp4jCfgStorageOperation, newValue);
    }

    public Integer32 getSnmp4jCfgStorageStorageType() {
      return (Integer32) getValue(idxSnmp4jCfgStorageStorageType);
    }

    public void setSnmp4jCfgStorageStorageType(Integer32 newValue) {
      setValue(idxSnmp4jCfgStorageStorageType, newValue);
    }

    public Integer32 getSnmp4jCfgStorageStatus() {
      return (Integer32) getValue(idxSnmp4jCfgStorageStatus);
    }

    public void setSnmp4jCfgStorageStatus(Integer32 newValue) {
      setValue(idxSnmp4jCfgStorageStatus, newValue);
    }

    //--AgentGen BEGIN=snmp4jCfgStorageEntry::RowFactory

    public void prepareRow(SubRequest subRequest, MOTableRow changeSet) {
      if (PRIMARY_INDEX.equals(changeSet.getIndex())) {
        if (snmp4jCfgStorageEntryModel.getRow(PRIMARY_INDEX) == null) {
          subRequest.getRequest().setErrorStatus(PDU.noCreation);
        }
      }
    }

    public void commitRow(SubRequest subRequest, MOTableRow changeSet) {
      Integer32 operation = getSnmp4jCfgStorageOperation();
      OctetString providerID = getSnmp4jCfgStorageID();
      MOPersistenceProvider provider =
          getPersistenceProvider(providerID.toString());
      if (provider == null) {
        subRequest.getRequest().setErrorStatus(PDU.commitFailed);
      }
      else {
        Operation op =
            new Operation(this, provider,
                          getSnmp4jCfgStorageRestoreMode().getValue(),
                          operation.getValue());
        setValue(idxSnmp4jCfgStorageOperation,
                 new Integer32(Snmp4jCfgStorageOperationEnum.inProgress));
        op.start();
      }
    }

    //--AgentGen END
  }



//--AgentGen BEGIN=_METHODS

  public void setSnmpCommunityMIB(SnmpCommunityMIB snmpCommunityMIB) {
    this.snmpCommunityMIB = snmpCommunityMIB;
  }

  public void setPrimaryProvider(MOPersistenceProvider persistenceProvider) {
    this.primaryPersistence = persistenceProvider;

    Snmp4jCfgStorageEntryRow primary = (Snmp4jCfgStorageEntryRow)
        snmp4jCfgStorageEntryModel.getRow(PRIMARY_INDEX);
    if (primary == null) {
      Variable[] vbs = snmp4jCfgStorageEntry.getDefaultValues();
      vbs[idxSnmp4jCfgStorageStatus] = new Integer32(RowStatus.active);
      primary = (Snmp4jCfgStorageEntryRow)
          snmp4jCfgStorageEntry.createRow(PRIMARY_INDEX, vbs);
      primary.setSnmp4jCfgStorageStorageType(
          new Integer32(StorageType.permanent));
      primary.setSnmp4jCfgStorageOperation(
          new Integer32(Snmp4jCfgStorageOperationEnum.idle));
      snmp4jCfgStorageEntry.addRow(primary);
    }
    primary.setSnmp4jCfgStoragePath(
      new OctetString(primaryPersistence.getDefaultURI()));
    addPersistenceProvider(persistenceProvider);
  }

  public void addPersistenceProvider(MOPersistenceProvider provider) {
    persistenceProvider.put(provider.getPersistenceProviderID(), provider);
  }

  public MOPersistenceProvider getPersistenceProvider(String id) {
    return persistenceProvider.get(id);
  }

  public SnmpCommunityMIB getCoexistenceInfoProvider() {
    return this.snmpCommunityMIB;
  }

//--AgentGen END

//--AgentGen BEGIN=_CLASSES

  private class Operation extends Thread {

    private Snmp4jCfgStorageEntryRow row;
    private int operation;
    private int restoreType;
    private MOPersistenceProvider provider;

    public Operation(Snmp4jCfgStorageEntryRow row,
                     MOPersistenceProvider provider,
                     int restoreType,
                     int operation) {
      this.operation = operation;
      this.provider = provider;
      this.restoreType = restoreType;
      this.row = row;
    }

    public void run() {
      switch (operation) {
        case Snmp4jCfgStorageOperationEnum.store: {
          String path = row.getValue(idxSnmp4jCfgStoragePath).toString();
          try {
            provider.store(path);
            row.setValue(idxSnmp4jCfgStorageLastStore, sysUpTime.get());
            row.setValue(idxSnmp4jCfgStorageOperation,
                         new Integer32(Snmp4jCfgStorageOperationEnum.idle));
          }
          catch (Exception iox) {
            logger.error("Failed to store config to '"+path+"': "+iox.getMessage(), iox);
            row.setValue(idxSnmp4jCfgStorageOperation,
                         new Integer32(Snmp4jCfgStorageOperationEnum.idle));
          }
          break;
        }
        case Snmp4jCfgStorageOperationEnum.restore: {
          String f = row.getValue(idxSnmp4jCfgStoragePath).toString();
          try {
            provider.restore(f, restoreType);
            row.setValue(idxSnmp4jCfgStorageLastRestore, sysUpTime.get());
            row.setValue(idxSnmp4jCfgStorageOperation,
                         new Integer32(Snmp4jCfgStorageOperationEnum.idle));
          }
          catch (Exception iox) {
            logger.error("Failed to restore config from '" + f + "': "+
                        iox.getMessage(), iox);
            row.setValue(idxSnmp4jCfgStorageOperation,
                         new Integer32(Snmp4jCfgStorageOperationEnum.idle));
          }
          break;
        }
      }
    }
  }

//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


