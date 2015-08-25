/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Snmp4jLogMib.java  
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

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogLevel;

//--AgentGen BEGIN=_IMPORT
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.snmp4j.agent.io.MOInput;
import java.io.IOException;

//--AgentGen END

public class Snmp4jLogMib
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
    ,MOServerLookupListener
    ,MOTableRowListener
    ,RowStatusListener
//--AgentGen END
{

  private static final LogAdapter LOGGER =
      LogFactory.getLogger(Snmp4jLogMib.class);

//--AgentGen BEGIN=_STATIC
  private static final MOTableRowFilter<Snmp4jLogLoggerEntryRow> NOT_ACTIVE_ROW_FILTER =
      new MOTableRowFilter<Snmp4jLogLoggerEntryRow>() {
    public boolean passesFilter(Snmp4jLogLoggerEntryRow row) {
      Snmp4jLogLoggerEntryRow r = row;
      if (r.getSnmp4jLogLoggerRowStatus().getValue() == RowStatus.active) {
        return true;
      }
      return false;
    }
  };
//--AgentGen END

  // Factory
  private static MOFactory moFactory = DefaultMOFactory.getInstance();

  // Constants
  public  static final OID oidSnmp4jLogSysDescr =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,1,1,0 });
  public  static final OID oidSnmp4jLogFactory =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,1,2,0 });
  public  static final OID oidSnmp4jLogRootLevel =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,2,1,0 });

  // Scalars
  private MOScalar<OctetString> snmp4jLogSysDescr;
  private MOScalar<OctetString> snmp4jLogFactory;
  private MOScalar<Integer32> snmp4jLogRootLevel;

  // Tables
  public  static final OID oidSnmp4jLogLoggerEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,2,2,1 });

  // Column sub-identifier definitions for snmp4jLogLoggerEntry:
  public  static final int colSnmp4jLogLoggerIndex = 2;
  public  static final int colSnmp4jLogLoggerLevel = 3;
  public  static final int colSnmp4jLogLoggerEffectiveLevel = 4;
  public  static final int colSnmp4jLogLoggerStorageType = 5;
  public  static final int colSnmp4jLogLoggerRowStatus = 6;

  // Column index definitions for snmp4jLogLoggerEntry:
  public  static final int idxSnmp4jLogLoggerIndex = 0;
  public  static final int idxSnmp4jLogLoggerLevel = 1;
  public  static final int idxSnmp4jLogLoggerEffectiveLevel = 2;
  public  static final int idxSnmp4jLogLoggerStorageType = 3;
  public  static final int idxSnmp4jLogLoggerRowStatus = 4;
  private static MOTableSubIndex[] snmp4jLogLoggerEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 0, 127)
  };

  private static MOTableIndex snmp4jLogLoggerEntryIndex =
      moFactory.createIndex(snmp4jLogLoggerEntryIndexes,
                            true);


  private MOTable<Snmp4jLogLoggerEntryRow,MOColumn,MOMutableTableModel<Snmp4jLogLoggerEntryRow>>
      snmp4jLogLoggerEntry;
  private MOMutableTableModel<Snmp4jLogLoggerEntryRow> snmp4jLogLoggerEntryModel;
  public  static final OID oidSnmp4jLogLoggerToHandlerEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,2,3,1 });

  // Column sub-identifier definitions for snmp4jLogLoggerToHandlerEntry:
  public  static final int colSnmp4jLogLoggerToHandlerThreshold = 1;
  public  static final int colSnmp4jLogLoggerToHandlerStorageType = 2;
  public  static final int colSnmp4jLogLoggerToHandlerRowStatus = 3;

  // Column index definitions for snmp4jLogLoggerToHandlerEntry:
  public  static final int idxSnmp4jLogLoggerToHandlerThreshold = 0;
  public  static final int idxSnmp4jLogLoggerToHandlerStorageType = 1;
  public  static final int idxSnmp4jLogLoggerToHandlerRowStatus = 2;
  private static MOTableSubIndex[] snmp4jLogLoggerToHandlerEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_INTEGER, 1, 1),
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 0, 255)
  };

  private static MOTableIndex snmp4jLogLoggerToHandlerEntryIndex =
      moFactory.createIndex(snmp4jLogLoggerToHandlerEntryIndexes,
                            false);


  private MOTable<Snmp4jLogLoggerToHandlerEntryRow,MOColumn,MOMutableTableModel<Snmp4jLogLoggerToHandlerEntryRow>>
      snmp4jLogLoggerToHandlerEntry;
  private MOMutableTableModel<Snmp4jLogLoggerToHandlerEntryRow> snmp4jLogLoggerToHandlerEntryModel;
  public  static final OID oidSnmp4jLogHandlerEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,2,5,2,1 });

  // Column sub-identifier definitions for snmp4jLogHandlerEntry:
  public  static final int colSnmp4jLogHandlerType = 2;
  public  static final int colSnmp4jLogHandlerStorageType = 3;
  public  static final int colSnmp4jLogHandlerRowStatus = 4;

  // Column index definitions for snmp4jLogHandlerEntry:
  public  static final int idxSnmp4jLogHandlerType = 0;
  public  static final int idxSnmp4jLogHandlerStorageType = 1;
  public  static final int idxSnmp4jLogHandlerRowStatus = 2;
  private static MOTableSubIndex[] snmp4jLogHandlerEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 0, 255)
  };

  private static MOTableIndex snmp4jLogHandlerEntryIndex =
      moFactory.createIndex(snmp4jLogHandlerEntryIndexes,
                            false);


  private MOTable<Snmp4jLogHandlerEntryRow,MOColumn,MOMutableTableModel<Snmp4jLogHandlerEntryRow>>
      snmp4jLogHandlerEntry;
  private MOMutableTableModel<Snmp4jLogHandlerEntryRow> snmp4jLogHandlerEntryModel;
  public  static final OID oidSnmp4jLogFileHandlerEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,2,5,3,1,1 });

  // Column sub-identifier definitions for snmp4jLogFileHandlerEntry:
  public  static final int colSnmp4jLogFileHandlerPath = 1;
  public  static final int colSnmp4jLogFileHandlerAppend = 2;
  public  static final int colSnmp4jLogFileHandlerBufferedIO = 3;
  public  static final int colSnmp4jLogFileHandlerBufferSize = 4;

  // Column index definitions for snmp4jLogFileHandlerEntry:
  public  static final int idxSnmp4jLogFileHandlerPath = 0;
  public  static final int idxSnmp4jLogFileHandlerAppend = 1;
  public  static final int idxSnmp4jLogFileHandlerBufferedIO = 2;
  public  static final int idxSnmp4jLogFileHandlerBufferSize = 3;
  private static MOTableSubIndex[] snmp4jLogFileHandlerEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 0, 255)
  };

  private static MOTableIndex snmp4jLogFileHandlerEntryIndex =
      moFactory.createIndex(snmp4jLogFileHandlerEntryIndexes,
                            false);


  private MOTable<Snmp4jLogFileHandlerEntryRow,MOColumn,MOMutableTableModel<Snmp4jLogFileHandlerEntryRow>>
      snmp4jLogFileHandlerEntry;
  private MOMutableTableModel<Snmp4jLogFileHandlerEntryRow> snmp4jLogFileHandlerEntryModel;
  public  static final OID oidSnmp4jLogConsoleHandlerEntry =
    new OID(new int[] { 1,3,6,1,4,1,4976,10,1,1,1,1,2,5,3,2,1 });

  // Column sub-identifier definitions for snmp4jLogConsoleHandlerEntry:
  public  static final int colSnmp4jLogConsoleHandlerTarget = 1;

  // Column index definitions for snmp4jLogConsoleHandlerEntry:
  public  static final int idxSnmp4jLogConsoleHandlerTarget = 0;
  private static MOTableSubIndex[] snmp4jLogConsoleHandlerEntryIndexes =
    new MOTableSubIndex[] {
        moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 0, 127)
  };

  private static MOTableIndex snmp4jLogConsoleHandlerEntryIndex =
      moFactory.createIndex(snmp4jLogConsoleHandlerEntryIndexes,
                            false);


  private MOTable<Snmp4jLogConsoleHandlerEntryRow,MOColumn,MOMutableTableModel<Snmp4jLogConsoleHandlerEntryRow>>
      snmp4jLogConsoleHandlerEntry;
  private MOMutableTableModel<Snmp4jLogConsoleHandlerEntryRow> snmp4jLogConsoleHandlerEntryModel;


  //--AgentGen BEGIN=_MEMBERS
  private Map<Integer, LogAdapter> loggers = new HashMap<Integer, LogAdapter>();
  private Map<CharSequence, Integer> loggerNames2Index = new WeakHashMap<CharSequence, Integer>();
  private int nextLoggerIndex = 1;
  private Object lastLoggerUpdateSource;
  private long updateTimeout = 1000;
  private long lastLoggerUpdateTime = getTimestampMilliseconds()-updateTimeout;
  //--AgentGen END

  @SuppressWarnings("unchecked")
  public Snmp4jLogMib() {
    snmp4jLogSysDescr =
      moFactory.createScalar(oidSnmp4jLogSysDescr,
                             MOAccessImpl.ACCESS_READ_ONLY, new OctetString());
    snmp4jLogFactory =
      moFactory.createScalar(oidSnmp4jLogFactory,
                             MOAccessImpl.ACCESS_READ_ONLY, new OctetString());
    snmp4jLogRootLevel =
      new Snmp4jLogRootLevel(oidSnmp4jLogRootLevel, MOAccessImpl.ACCESS_READ_WRITE);
    snmp4jLogRootLevel.addMOValueValidationListener(new Snmp4jLogLevelValidator());
    createSnmp4jLogLoggerEntry();
    createSnmp4jLogLoggerToHandlerEntry();
    createSnmp4jLogHandlerEntry();
    createSnmp4jLogFileHandlerEntry();
    createSnmp4jLogConsoleHandlerEntry();
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }


  public MOTable getSnmp4jLogLoggerEntry() {
    return snmp4jLogLoggerEntry;
  }

  @SuppressWarnings("unchecked")
  private void createSnmp4jLogLoggerEntry() {
    MOColumn[] snmp4jLogLoggerEntryColumns = new MOColumn[5];
    snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerIndex] =
      new MOColumn(colSnmp4jLogLoggerIndex,
                   SMIConstants.SYNTAX_INTEGER32,
                   MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerLevel] =
      new MOMutableColumn(colSnmp4jLogLoggerLevel,
                          SMIConstants.SYNTAX_INTEGER,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new Integer32(1),
                          true);
    ((MOMutableColumn)snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerLevel]).
      addMOValueValidationListener(new Snmp4jLogLevelValidator());
    snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerEffectiveLevel] =
      new MOColumn(colSnmp4jLogLoggerEffectiveLevel,
                   SMIConstants.SYNTAX_INTEGER,
                   MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerStorageType] =
      new StorageType(colSnmp4jLogLoggerStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(2),
                      true);
    snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerRowStatus] =
      new RowStatus(colSnmp4jLogLoggerRowStatus);

    snmp4jLogLoggerEntryModel = new DefaultMOMutableTableModel<Snmp4jLogLoggerEntryRow>();
    snmp4jLogLoggerEntryModel.setRowFactory(new Snmp4jLogLoggerEntryRowFactory());
    snmp4jLogLoggerEntry =
      moFactory.createTable(oidSnmp4jLogLoggerEntry,
                            snmp4jLogLoggerEntryIndex,
                            snmp4jLogLoggerEntryColumns,
                            snmp4jLogLoggerEntryModel);
    ((RowStatus)snmp4jLogLoggerEntryColumns[idxSnmp4jLogLoggerRowStatus]).
        addRowStatusListener(this);
    snmp4jLogLoggerEntry.addMOTableRowListener(this);
    // Not needed any more (since 1.1):
    // updateLoggerTable();
  }

  public MOTable getSnmp4jLogLoggerToHandlerEntry() {
    return snmp4jLogLoggerToHandlerEntry;
  }


  @SuppressWarnings("unchecked")
  private void createSnmp4jLogLoggerToHandlerEntry() {
    MOColumn[] snmp4jLogLoggerToHandlerEntryColumns = new MOColumn[3];
    snmp4jLogLoggerToHandlerEntryColumns[idxSnmp4jLogLoggerToHandlerThreshold] =
      new MOMutableColumn(colSnmp4jLogLoggerToHandlerThreshold,
                          SMIConstants.SYNTAX_INTEGER,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new Integer32(2),
                          true);
    ((MOMutableColumn)snmp4jLogLoggerToHandlerEntryColumns[idxSnmp4jLogLoggerToHandlerThreshold]).
      addMOValueValidationListener(new Snmp4jLogLevelValidator());
    snmp4jLogLoggerToHandlerEntryColumns[idxSnmp4jLogLoggerToHandlerStorageType] =
      new StorageType(colSnmp4jLogLoggerToHandlerStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      new Integer32(2),
                      true);
    snmp4jLogLoggerToHandlerEntryColumns[idxSnmp4jLogLoggerToHandlerRowStatus] =
      new RowStatus(colSnmp4jLogLoggerToHandlerRowStatus);

    snmp4jLogLoggerToHandlerEntryModel = new DefaultMOMutableTableModel();
    snmp4jLogLoggerToHandlerEntryModel.setRowFactory(new Snmp4jLogLoggerToHandlerEntryRowFactory());
    snmp4jLogLoggerToHandlerEntry =
      moFactory.createTable(oidSnmp4jLogLoggerToHandlerEntry,
                            snmp4jLogLoggerToHandlerEntryIndex,
                            snmp4jLogLoggerToHandlerEntryColumns,
                            snmp4jLogLoggerToHandlerEntryModel);
  }

  public MOTable getSnmp4jLogHandlerEntry() {
    return snmp4jLogHandlerEntry;
  }

  @SuppressWarnings("unchecked")
  private void createSnmp4jLogHandlerEntry() {
    MOColumn[] snmp4jLogHandlerEntryColumns = new MOColumn[3];
    snmp4jLogHandlerEntryColumns[idxSnmp4jLogHandlerType] =
      new MOMutableColumn(colSnmp4jLogHandlerType,
                          SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OID("1.3.6.1.4.1.4976.10.1.1.1.1.2.5.1.2.1"),
                          true);
    snmp4jLogHandlerEntryColumns[idxSnmp4jLogHandlerStorageType] =
      new StorageType(colSnmp4jLogHandlerStorageType,
                      MOAccessImpl.ACCESS_READ_CREATE,
                      null,
                      true);
    snmp4jLogHandlerEntryColumns[idxSnmp4jLogHandlerRowStatus] =
      new RowStatus(colSnmp4jLogHandlerRowStatus);

    snmp4jLogHandlerEntryModel = new DefaultMOMutableTableModel();
    snmp4jLogHandlerEntryModel.setRowFactory(new Snmp4jLogHandlerEntryRowFactory());
    snmp4jLogHandlerEntry =
      moFactory.createTable(oidSnmp4jLogHandlerEntry,
                         snmp4jLogHandlerEntryIndex,
                            snmp4jLogHandlerEntryColumns,
                            snmp4jLogHandlerEntryModel);
  }

  public MOTable getSnmp4jLogFileHandlerEntry() {
    return snmp4jLogFileHandlerEntry;
  }

  @SuppressWarnings("unchecked")
  private void createSnmp4jLogFileHandlerEntry() {
    MOColumn[] snmp4jLogFileHandlerEntryColumns = new MOColumn[4];
    snmp4jLogFileHandlerEntryColumns[idxSnmp4jLogFileHandlerPath] =
      new MOMutableColumn(colSnmp4jLogFileHandlerPath,
                          SMIConstants.SYNTAX_OCTET_STRING,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new OctetString(new byte[] { (byte)115,(byte)110,(byte)109,(byte)112,(byte)52,(byte)106,(byte)46,(byte)108,(byte)111,(byte)103 }),
                          true);
    ((MOMutableColumn)snmp4jLogFileHandlerEntryColumns[idxSnmp4jLogFileHandlerPath]).
      addMOValueValidationListener(new Snmp4jLogFileHandlerPathValidator());
    snmp4jLogFileHandlerEntryColumns[idxSnmp4jLogFileHandlerAppend] =
      new MOMutableColumn(colSnmp4jLogFileHandlerAppend,
                          SMIConstants.SYNTAX_INTEGER,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new Integer32(1),
                          true);
    snmp4jLogFileHandlerEntryColumns[idxSnmp4jLogFileHandlerBufferedIO] =
      new MOMutableColumn(colSnmp4jLogFileHandlerBufferedIO,
                          SMIConstants.SYNTAX_INTEGER,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new Integer32(1),
                          true);
    snmp4jLogFileHandlerEntryColumns[idxSnmp4jLogFileHandlerBufferSize] =
      new MOMutableColumn(colSnmp4jLogFileHandlerBufferSize,
                          SMIConstants.SYNTAX_GAUGE32,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new UnsignedInteger32(16535),
                          true);

    snmp4jLogFileHandlerEntryModel = new DefaultMOMutableTableModel();
    snmp4jLogFileHandlerEntryModel.setRowFactory(new Snmp4jLogFileHandlerEntryRowFactory());
    snmp4jLogFileHandlerEntry =
      moFactory.createTable(oidSnmp4jLogFileHandlerEntry,
                         snmp4jLogFileHandlerEntryIndex,
                            snmp4jLogFileHandlerEntryColumns,
                            snmp4jLogFileHandlerEntryModel);
  }

  public MOTable getSnmp4jLogConsoleHandlerEntry() {
    return snmp4jLogConsoleHandlerEntry;
  }

  @SuppressWarnings("unchecked")
  private void createSnmp4jLogConsoleHandlerEntry() {
    MOColumn[] snmp4jLogConsoleHandlerEntryColumns = new MOColumn[1];
    snmp4jLogConsoleHandlerEntryColumns[idxSnmp4jLogConsoleHandlerTarget] =
      new Enumerated<Integer32>(colSnmp4jLogConsoleHandlerTarget,
                     SMIConstants.SYNTAX_INTEGER32,
                     MOAccessImpl.ACCESS_READ_CREATE,
                     new Integer32(1),
                     true,
                     new int[] { Snmp4jLogConsoleHandlerTargetEnum.systemOut,
                                 Snmp4jLogConsoleHandlerTargetEnum.systemErr });

    snmp4jLogConsoleHandlerEntryModel = new DefaultMOMutableTableModel<Snmp4jLogConsoleHandlerEntryRow>();
    snmp4jLogConsoleHandlerEntryModel.setRowFactory(new Snmp4jLogConsoleHandlerEntryRowFactory());
    snmp4jLogConsoleHandlerEntry =
      moFactory.createTable(oidSnmp4jLogConsoleHandlerEntry,
                         snmp4jLogConsoleHandlerEntryIndex,
                            snmp4jLogConsoleHandlerEntryColumns,
                            snmp4jLogConsoleHandlerEntryModel);
  }


  public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
  {
    // Scalar Objects
    server.register(this.snmp4jLogSysDescr, context);
    server.register(this.snmp4jLogFactory, context);
    server.register(this.snmp4jLogRootLevel, context);
    server.register(this.snmp4jLogLoggerEntry, context);
    /**@todo implement other tables
    server.register(this.snmp4jLogLoggerToHandlerEntry, context);
    server.register(this.snmp4jLogHandlerEntry, context);
    server.register(this.snmp4jLogFileHandlerEntry, context);
    server.register(this.snmp4jLogConsoleHandlerEntry, context);
    */
    //--AgentGen BEGIN=_registerMOs
    server.addLookupListener(this, this.snmp4jLogSysDescr);
    server.addLookupListener(this, this.snmp4jLogLoggerEntry);
    //--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmp4jLogSysDescr, context);
    server.unregister(this.snmp4jLogFactory, context);
    server.unregister(this.snmp4jLogRootLevel, context);
    server.unregister(this.snmp4jLogLoggerEntry, context);
    server.unregister(this.snmp4jLogLoggerToHandlerEntry, context);
    server.unregister(this.snmp4jLogHandlerEntry, context);
    server.unregister(this.snmp4jLogFileHandlerEntry, context);
    server.unregister(this.snmp4jLogConsoleHandlerEntry, context);
    //--AgentGen BEGIN=_unregisterMOs
    server.removeLookupListener(this, this.snmp4jLogSysDescr);
    server.removeLookupListener(this, this.snmp4jLogLoggerEntry);
    //--AgentGen END
  }

  // Notifications

  // Scalars
  class Snmp4jLogRootLevel extends MOScalar<Integer32> {
    Snmp4jLogRootLevel(OID oid, MOAccess access) {
      super(oid, access, new Integer32());
    }

    public void commit(SubRequest request) {
     //--AgentGen BEGIN=snmp4jLogRootLevel::commit
     Variable vb = request.getVariableBinding().getVariable();
     int v = ((Integer32)vb).getValue();
     LogAdapter logAdapter = LogFactory.getLogFactory().getRootLogger();
     logAdapter.setLogLevel(new LogLevel(v));
     //--AgentGen END
      super.commit(request);
    }

    public void load(MOInput input) throws IOException {
      super.load(input);
      int v = ((Integer32)getValue()).getValue();
      if (LogFactory.getLogFactory() != null) {
        LogAdapter logAdapter = LogFactory.getLogFactory().getRootLogger();
        if (v != LogLevel.LEVEL_NONE) {
          logAdapter.setLogLevel(new LogLevel(v));
        }
      }
    }

  }


  // Value Validators
  /**
   * The <code>Snmp4jLogRootLevelValidator</code> implements the value
   * validation for <code>Snmp4jLogRootLevel</code>.
   */
  static class Snmp4jLogLevelValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
     //--AgentGen BEGIN=snmp4jLogRootLevel::validate
     int v = ((Integer32)newValue ).getValue();
     if ((v < 1)  || (v > 8)) {
       validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_VALUE);
     }
     //--AgentGen END
    }
  }

  /**
   * The <code>Snmp4jLogFileHandlerPathValidator</code> implements the value
   * validation for <code>Snmp4jLogFileHandlerPath</code>.
   */
  static class Snmp4jLogFileHandlerPathValidator implements MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 1) && (os.length() <= 512)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=snmp4jLogFileHandlerPath::validate
     //--AgentGen END
    }
  }

  public static final class Snmp4jLogLoggerToHandlerThresholdEnum {
    /* -- no level has been specified */
    public static final int notSpecified = 0;
    public static final int off = 1;
    public static final int all = 2;
    public static final int trace = 3;
    public static final int debug = 4;
    public static final int info = 5;
    public static final int warn = 6;
    public static final int error = 7;
    public static final int fatal = 8;
  }
  public static final class Snmp4jLogFileHandlerAppendEnum {
    public static final int _true = 1;
    public static final int _false = 2;
  }
  public static final class Snmp4jLogFileHandlerBufferedIOEnum {
    public static final int _true = 1;
    public static final int _false = 2;
  }
  public static final class Snmp4jLogConsoleHandlerTargetEnum {
    public static final int systemOut = 1;
    public static final int systemErr = 2;
  }

  // Rows and Factories
  class Snmp4jLogLoggerEntryRowFactory
        implements MOTableRowFactory<Snmp4jLogLoggerEntryRow>
  {
    public Snmp4jLogLoggerEntryRowFactory() {}

    public Snmp4jLogLoggerEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
//      Snmp4jLogLoggerEntryRow row = new Snmp4jLogLoggerEntryRow(index, values);
      //--AgentGen BEGIN=snmp4jLogLoggerEntry::createRow
      int i = nextLoggerIndex++;
      Snmp4jLogLoggerEntryRow row =
          new Snmp4jLogLoggerRow(index, values, i, null);
      row.setSnmp4jLogLoggerIndex(new Integer32(i));
      loggerNames2Index.put(new OctetString(index.toByteArray()).toString(),i);
      //--AgentGen END
      return row;
    }

    public void freeRow(Snmp4jLogLoggerEntryRow row) {
     //--AgentGen BEGIN=snmp4jLogLoggerEntry::freeRow
     //--AgentGen END
    }
  }

  class Snmp4jLogLoggerEntryRow extends DefaultMOMutableRow2PC {
    public Snmp4jLogLoggerEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public Integer32 getSnmp4jLogLoggerIndex() {
      return (Integer32) getValue(idxSnmp4jLogLoggerIndex);
    }

    public void setSnmp4jLogLoggerIndex(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerIndex, newValue);
    }

    public Integer32 getSnmp4jLogLoggerLevel() {
      return (Integer32) getValue(idxSnmp4jLogLoggerLevel);
    }

    public void setSnmp4jLogLoggerLevel(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerLevel, newValue);
    }

    public Integer32 getSnmp4jLogLoggerEffectiveLevel() {
      return (Integer32) getValue(idxSnmp4jLogLoggerEffectiveLevel);
    }

    public void setSnmp4jLogLoggerEffectiveLevel(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerEffectiveLevel, newValue);
    }

    public Integer32 getSnmp4jLogLoggerStorageType() {
      return (Integer32) getValue(idxSnmp4jLogLoggerStorageType);
    }

    public void setSnmp4jLogLoggerStorageType(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerStorageType, newValue);
    }

    public Integer32 getSnmp4jLogLoggerRowStatus() {
      return (Integer32) getValue(idxSnmp4jLogLoggerRowStatus);
    }

    public void setSnmp4jLogLoggerRowStatus(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerRowStatus, newValue);
    }


     //--AgentGen BEGIN=snmp4jLogLoggerEntry::RowFactory
     public Variable getValue(int column) {
       if (column == idxSnmp4jLogLoggerEffectiveLevel) {
         values[column] =
             new Integer32(((Snmp4jLogLoggerRow)this).
                           getLogAdapter().getEffectiveLogLevel().getLevel());
       }
       return super.getValue(column);
     }
     //--AgentGen END
  }

  class Snmp4jLogLoggerToHandlerEntryRowFactory
        implements MOTableRowFactory<Snmp4jLogLoggerToHandlerEntryRow>
  {
    public Snmp4jLogLoggerToHandlerEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      Snmp4jLogLoggerToHandlerEntryRow row = new Snmp4jLogLoggerToHandlerEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jLogLoggerToHandlerEntry::createRow
     //--AgentGen END
      return row;
    }

    public void freeRow(Snmp4jLogLoggerToHandlerEntryRow row) {
     //--AgentGen BEGIN=snmp4jLogLoggerToHandlerEntry::freeRow
     //--AgentGen END
    }
  }

  class Snmp4jLogLoggerToHandlerEntryRow extends DefaultMOMutableRow2PC {
    public Snmp4jLogLoggerToHandlerEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public Integer32 getSnmp4jLogLoggerToHandlerThreshold() {
      return (Integer32) getValue(idxSnmp4jLogLoggerToHandlerThreshold);
    }

    public void setSnmp4jLogLoggerToHandlerThreshold(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerToHandlerThreshold, newValue);
    }

    public Integer32 getSnmp4jLogLoggerToHandlerStorageType() {
      return (Integer32) getValue(idxSnmp4jLogLoggerToHandlerStorageType);
    }

    public void setSnmp4jLogLoggerToHandlerStorageType(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerToHandlerStorageType, newValue);
    }

    public Integer32 getSnmp4jLogLoggerToHandlerRowStatus() {
      return (Integer32) getValue(idxSnmp4jLogLoggerToHandlerRowStatus);
    }

    public void setSnmp4jLogLoggerToHandlerRowStatus(Integer32 newValue) {
      setValue(idxSnmp4jLogLoggerToHandlerRowStatus, newValue);
    }


     //--AgentGen BEGIN=snmp4jLogLoggerToHandlerEntry::RowFactory
     //--AgentGen END
  }

  class Snmp4jLogHandlerEntryRowFactory
        implements MOTableRowFactory<Snmp4jLogHandlerEntryRow>
  {
    public Snmp4jLogHandlerEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      Snmp4jLogHandlerEntryRow row = new Snmp4jLogHandlerEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jLogHandlerEntry::createRow
     //--AgentGen END
      return row;
    }

    public void freeRow(Snmp4jLogHandlerEntryRow row) {
     //--AgentGen BEGIN=snmp4jLogHandlerEntry::freeRow
     //--AgentGen END
    }
  }

  class Snmp4jLogHandlerEntryRow extends DefaultMOMutableRow2PC {
    public Snmp4jLogHandlerEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public OID getSnmp4jLogHandlerType() {
      return (OID) getValue(idxSnmp4jLogHandlerType);
    }

    public void setSnmp4jLogHandlerType(OID newValue) {
      setValue(idxSnmp4jLogHandlerType, newValue);
    }

    public Integer32 getSnmp4jLogHandlerStorageType() {
      return (Integer32) getValue(idxSnmp4jLogHandlerStorageType);
    }

    public void setSnmp4jLogHandlerStorageType(Integer32 newValue) {
      setValue(idxSnmp4jLogHandlerStorageType, newValue);
    }

    public Integer32 getSnmp4jLogHandlerRowStatus() {
      return (Integer32) getValue(idxSnmp4jLogHandlerRowStatus);
    }

    public void setSnmp4jLogHandlerRowStatus(Integer32 newValue) {
      setValue(idxSnmp4jLogHandlerRowStatus, newValue);
    }


     //--AgentGen BEGIN=snmp4jLogHandlerEntry::RowFactory
     //--AgentGen END
  }

  class Snmp4jLogFileHandlerEntryRowFactory
        implements MOTableRowFactory<Snmp4jLogFileHandlerEntryRow>
  {
    public Snmp4jLogFileHandlerEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      Snmp4jLogFileHandlerEntryRow row = new Snmp4jLogFileHandlerEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jLogFileHandlerEntry::createRow
     //--AgentGen END
      return row;
    }

    public void freeRow(Snmp4jLogFileHandlerEntryRow row) {
     //--AgentGen BEGIN=snmp4jLogFileHandlerEntry::freeRow
     //--AgentGen END
    }
  }

  class Snmp4jLogFileHandlerEntryRow extends DefaultMOMutableRow2PC {
    public Snmp4jLogFileHandlerEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public OctetString getSnmp4jLogFileHandlerPath() {
      return (OctetString) getValue(idxSnmp4jLogFileHandlerPath);
    }

    public void setSnmp4jLogFileHandlerPath(OctetString newValue) {
      setValue(idxSnmp4jLogFileHandlerPath, newValue);
    }

    public Integer32 getSnmp4jLogFileHandlerAppend() {
      return (Integer32) getValue(idxSnmp4jLogFileHandlerAppend);
    }

    public void setSnmp4jLogFileHandlerAppend(Integer32 newValue) {
      setValue(idxSnmp4jLogFileHandlerAppend, newValue);
    }

    public Integer32 getSnmp4jLogFileHandlerBufferedIO() {
      return (Integer32) getValue(idxSnmp4jLogFileHandlerBufferedIO);
    }

    public void setSnmp4jLogFileHandlerBufferedIO(Integer32 newValue) {
      setValue(idxSnmp4jLogFileHandlerBufferedIO, newValue);
    }

    public UnsignedInteger32 getSnmp4jLogFileHandlerBufferSize() {
      return (UnsignedInteger32) getValue(idxSnmp4jLogFileHandlerBufferSize);
    }

    public void setSnmp4jLogFileHandlerBufferSize(UnsignedInteger32 newValue) {
      setValue(idxSnmp4jLogFileHandlerBufferSize, newValue);
    }


     //--AgentGen BEGIN=snmp4jLogFileHandlerEntry::RowFactory
     //--AgentGen END
  }

  class Snmp4jLogConsoleHandlerEntryRowFactory
        implements MOTableRowFactory<Snmp4jLogConsoleHandlerEntryRow>
  {
    public Snmp4jLogConsoleHandlerEntryRowFactory() {}

    public Snmp4jLogConsoleHandlerEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException
    {
      Snmp4jLogConsoleHandlerEntryRow row =
          new Snmp4jLogConsoleHandlerEntryRow(index, values);
     //--AgentGen BEGIN=snmp4jLogConsoleHandlerEntry::createRow

     //--AgentGen END
      return row;
    }

    public void freeRow(Snmp4jLogConsoleHandlerEntryRow row) {
     //--AgentGen BEGIN=snmp4jLogConsoleHandlerEntry::freeRow
     //--AgentGen END
    }
  }

  class Snmp4jLogConsoleHandlerEntryRow extends DefaultMOMutableRow2PC {
    public Snmp4jLogConsoleHandlerEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public Integer32 getSnmp4jLogConsoleHandlerTarget() {
      return (Integer32) getValue(idxSnmp4jLogConsoleHandlerTarget);
    }

    public void setSnmp4jLogConsoleHandlerTarget(Integer32 newValue) {
      setValue(idxSnmp4jLogConsoleHandlerTarget, newValue);
    }


     //--AgentGen BEGIN=snmp4jLogConsoleHandlerEntry::RowFactory
     //--AgentGen END
  }


//--AgentGen BEGIN=_METHODS

  private OctetString getLogSysDescr() {
    LogFactory logFactory = LogFactory.getLogFactory();
    if (logFactory == null) {
      return new OctetString();
    }
    return new OctetString(logFactory.toString());
  }

  private OctetString getLogFactory() {
    LogFactory logFactory = LogFactory.getLogFactory();
    if (logFactory == null) {
      return new OctetString();
    }
    return new OctetString(logFactory.getClass().getName());
  }

  public void lookupEvent(MOServerLookupEvent event) {
    if ((event.getLookupResult() == this.snmp4jLogSysDescr) ||
        (event.getLookupResult() == this.snmp4jLogFactory)){
      this.snmp4jLogSysDescr.setValue(getLogSysDescr());
      this.snmp4jLogFactory.setValue(getLogFactory());
    }
/* This table is updated by the queryEvent method below
    else if (event.getLookupResult() == this.snmp4jLogLoggerEntry) {
      updateLoggerTable();
    }
*/
  }

  public void queryEvent(MOServerLookupEvent event) {
    if ((event.getLookupResult() == this.snmp4jLogLoggerEntry) &&
        (!MOQueryWithSource.isSameSource(event.getQuery(),
                                         lastLoggerUpdateSource)) &&
        (lastLoggerUpdateTime + updateTimeout < getTimestampMilliseconds())) {
      lastLoggerUpdateSource = event.getSource();
      lastLoggerUpdateTime = getTimestampMilliseconds();
      updateLoggerTable();
    }
  }

  private static OID getLoggerIndex(LogAdapter logger) {
    String loggerName = logger.getName();
    if (loggerName.length() < 1) {
      loggerName = ".";
    }
    return new OctetString(loggerName).toSubIndex(true);
  }

  private static long getTimestampMilliseconds() {
    return System.nanoTime() / SnmpConstants.MILLISECOND_TO_NANOSECOND;
  }

  private Snmp4jLogLoggerRow createLoggerRow(int n, LogAdapter logger,
                                             int storageType) {
    OID index = getLoggerIndex(logger);
    Variable[] row = new Variable[5];
    int i=0;
    row[i++] = new Integer32(n);
    row[i++] = new Integer32(logger.getLogLevel().getLevel());
    row[i++] = new Integer32(logger.getEffectiveLogLevel().getLevel());
    row[i++] = new Integer32(storageType);
    row[i  ] = new Integer32(RowStatus.active);
    return new Snmp4jLogLoggerRow(index, row, n, logger);
  }

  private int getStorageType(LogAdapter logger) {
    Snmp4jLogLoggerRow row = (Snmp4jLogLoggerRow)
        snmp4jLogLoggerEntryModel.getRow(getLoggerIndex(logger));
    if (row != null) {
      return row.getSnmp4jLogLoggerStorageType().getValue();
    }
    return StorageType.volatile_;
  }

  public synchronized void updateLoggerTable() {
    snmp4jLogLoggerEntryModel.clear(NOT_ACTIVE_ROW_FILTER);
    if (LogFactory.getLogFactory() == null) {
      return;
    }
    Iterator loggers = LogFactory.getLogFactory().loggers();
    this.loggers = new HashMap<Integer, LogAdapter>();
    LogAdapter rootLogger = LogFactory.getLogFactory().getRootLogger();
    if (rootLogger != null) {
      snmp4jLogLoggerEntry.addRow(createLoggerRow(1, rootLogger,
                                                  getStorageType(rootLogger)));
    }
    while (loggers.hasNext()) {
      LogAdapter l = (LogAdapter) loggers.next();
      int i = 1;
      Integer index = loggerNames2Index.get(l.getName());
      if (index == null) {
        i = nextLoggerIndex++;
        loggerNames2Index.put(l.getName(), i);
      }
      else {
        i = index;
      }
      this.loggers.put(i, l);
      snmp4jLogLoggerEntry.addRow(createLoggerRow(i, l, getStorageType(l)));
    }
  }

  public void rowStatusChanged(RowStatusEvent event) {
    if (event.getTable().equals(snmp4jLogLoggerEntry)) {
      if ((event.getNewStatus() == RowStatus.active) ||
          (event.getNewStatus() == RowStatus.createAndGo)) {
        Snmp4jLogLoggerRow r = (Snmp4jLogLoggerRow) event.getRow();
        LogAdapter logAdapter = r.getLogAdapter();
        if (logAdapter == null) {
          OctetString loggerName = new OctetString(r.getIndex().toByteArray());
          logAdapter = LogFactory.getLogger(loggerName.toString());
          r.setLogAdapter(logAdapter);
        }
        logAdapter.setLogLevel(
            new LogLevel(r.getSnmp4jLogLoggerLevel().getValue()));
      }
    }
  }

  public void rowChanged(MOTableRowEvent event) {
    if (event.getTable().equals(snmp4jLogLoggerEntry)) {
      switch (event.getType()) {
        case MOTableRowEvent.UPDATED: {
          Snmp4jLogLoggerRow r = (Snmp4jLogLoggerRow) event.getRow();
          if (r.getLogAdapter() != null) {
            int level = r.getSnmp4jLogLoggerLevel().getValue();
            if (level != LogLevel.LEVEL_NONE) {
              r.getLogAdapter().setLogLevel(new LogLevel(level));
            }
          }
          break;
        }
        case MOTableRowEvent.ADD: {
          Snmp4jLogLoggerRow r = (Snmp4jLogLoggerRow) event.getRow();
          LogAdapter logAdapter = r.getLogAdapter();
          if (logAdapter == null) {
            OctetString loggerName = new OctetString(r.getIndex().toByteArray());
            logAdapter = LogFactory.getLogger(loggerName.toString());
            r.setLogAdapter(logAdapter);
          }
          int level = r.getSnmp4jLogLoggerLevel().getValue();
          if (level != LogLevel.LEVEL_NONE) {
            logAdapter.setLogLevel(new LogLevel(level));
          }
          break;
        }
      }
    }
  }

  //--AgentGen END

  //--AgentGen BEGIN=_CLASSES

  public class Snmp4jLogLoggerRow extends Snmp4jLogLoggerEntryRow {

    private LogAdapter logger;
    private int n;

    protected Snmp4jLogLoggerRow(OID index, Variable[] values,
                                 int n, LogAdapter logger) {
      super(index, values);
      this.n = n;
      this.logger = logger;
    }

    public void setLogAdapter(LogAdapter logAdapter) {
      this.logger = logAdapter;
    }

    public LogAdapter getLogAdapter() {
      return logger;
    }

  }

  //--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


