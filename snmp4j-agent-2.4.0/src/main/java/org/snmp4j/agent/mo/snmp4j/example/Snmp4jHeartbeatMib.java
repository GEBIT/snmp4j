/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Snmp4jHeartbeatMib.java  
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
package org.snmp4j.agent.mo.snmp4j.example;
//--AgentGen END

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import java.util.Timer;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimerTask;
import org.snmp4j.agent.mo.snmp4j.example.Snmp4jHeartbeatMib.
    Snmp4jAgentHBCtrlEntryRow;
import org.snmp4j.agent.mo.snmp4j.example.Snmp4jHeartbeatMib.HeartbeatTask;
import org.snmp4j.PDU;
import java.util.Date;
import org.snmp4j.agent.io.MOInput;
import java.io.IOException;
import org.snmp4j.agent.io.MOOutput;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.util.CommonTimer;

//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class Snmp4jHeartbeatMib
//--AgentGen BEGIN=_EXTENDS
//--AgentGen END
    implements MOGroup
//--AgentGen BEGIN=_IMPLEMENTS
    , RowStatusListener,
    MOTableRowListener<Snmp4jAgentHBCtrlEntryRow>
//--AgentGen END
{

  private static final LogAdapter LOGGER =
      LogFactory.getLogger(Snmp4jHeartbeatMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private static MOFactory moFactory = DefaultMOFactory.getInstance();

  // Constants
  public static final OID oidSnmp4jAgentHBRefTime =
      new OID(new int[] {1, 3, 6, 1, 4, 1, 4976, 10, 1, 1, 42, 2, 1, 1, 0});
  public static final OID oidSnmp4jAgentHBEvent =
      new OID(new int[] {1, 3, 6, 1, 4, 1, 4976, 10, 1, 1, 42, 2, 2, 0, 1});
  public static final OID oidTrapVarSnmp4jAgentHBCtrlEvents =
      new OID(new int[] {1, 3, 6, 1, 4, 1, 4976, 10, 1, 1, 42, 2, 1, 2, 1, 6});


  // Enumerations

  public static final class Snmp4jAgentHBCtrlStorageTypeEnum {
    /* -- eh? */
    public static final int other = 1;
    /* -- e.g., in RAM */
    public static final int _volatile = 2;
    /* -- e.g., in NVRAM */
    public static final int nonVolatile = 3;
    /* -- e.g., partially in ROM */
    public static final int permanent = 4;
    /* -- e.g., completely in ROM */
    public static final int readOnly = 5;
  }
  public static final class Snmp4jAgentHBCtrlRowStatusEnum {
    public static final int active = 1;
    /* -- the following value is a state:
-- this value may be read, but not written */
    public static final int notInService = 2;
    /* -- the following three values are
-- actions: these values may be written,
--   but are never read */
    public static final int notReady = 3;
    public static final int createAndGo = 4;
    public static final int createAndWait = 5;
    public static final int destroy = 6;
  }

  // TextualConventions
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_DATEANDTIME = "DateAndTime";

  // Scalars
  private MOScalar snmp4jAgentHBRefTime;

  // Tables
  public static final OID oidSnmp4jAgentHBCtrlEntry =
      new OID(new int[] {1, 3, 6, 1, 4, 1, 4976, 10, 1, 1, 42, 2, 1, 2, 1});

  // Column sub-identifer defintions for snmp4jAgentHBCtrlEntry:
  public static final int colSnmp4jAgentHBCtrlStartTime = 2;
  public static final int colSnmp4jAgentHBCtrlDelay = 3;
  public static final int colSnmp4jAgentHBCtrlPeriod = 4;
  public static final int colSnmp4jAgentHBCtrlMaxEvents = 5;
  public static final int colSnmp4jAgentHBCtrlEvents = 6;
  public static final int colSnmp4jAgentHBCtrlLastChange = 7;
  public static final int colSnmp4jAgentHBCtrlStorageType = 8;
  public static final int colSnmp4jAgentHBCtrlRowStatus = 9;

  // Column index defintions for snmp4jAgentHBCtrlEntry:
  public static final int idxSnmp4jAgentHBCtrlStartTime = 0;
  public static final int idxSnmp4jAgentHBCtrlDelay = 1;
  public static final int idxSnmp4jAgentHBCtrlPeriod = 2;
  public static final int idxSnmp4jAgentHBCtrlMaxEvents = 3;
  public static final int idxSnmp4jAgentHBCtrlEvents = 4;
  public static final int idxSnmp4jAgentHBCtrlLastChange = 5;
  public static final int idxSnmp4jAgentHBCtrlStorageType = 6;
  public static final int idxSnmp4jAgentHBCtrlRowStatus = 7;

  private static final MOTableSubIndex[] snmp4jAgentHBCtrlEntryIndexes =
      new MOTableSubIndex[] {
      moFactory.createSubIndex(null, SMIConstants.SYNTAX_OCTET_STRING, 1, 32)
  };

  private static final MOTableIndex snmp4jAgentHBCtrlEntryIndex =
      moFactory.createIndex(snmp4jAgentHBCtrlEntryIndexes,
                            true);

  private MOTable<Snmp4jAgentHBCtrlEntryRow,MOColumn,MOMutableTableModel<Snmp4jAgentHBCtrlEntryRow>>
      snmp4jAgentHBCtrlEntry;
  private MOMutableTableModel<Snmp4jAgentHBCtrlEntryRow> snmp4jAgentHBCtrlEntryModel;

//--AgentGen BEGIN=_MEMBERS
  private static final int[] PROTECTED_COLS =
      {
      idxSnmp4jAgentHBCtrlStartTime,
      idxSnmp4jAgentHBCtrlDelay,
      idxSnmp4jAgentHBCtrlPeriod
  };

  private CommonTimer heartbeatTimer = SNMP4JSettings.getTimerFactory().createTimer();
  private int heartbeatOffset = 0;
  private NotificationOriginator notificationOriginator;
  private OctetString context;
  private SysUpTime sysUpTime;
//--AgentGen END

  private Snmp4jHeartbeatMib() {
    snmp4jAgentHBRefTime =
        new Snmp4jAgentHBRefTime(oidSnmp4jAgentHBRefTime,
                                 MOAccessImpl.ACCESS_READ_WRITE);
    snmp4jAgentHBRefTime.addMOValueValidationListener(new
        Snmp4jAgentHBRefTimeValidator());
    createSnmp4jAgentHBCtrlEntry();
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
    ((RowStatus) snmp4jAgentHBCtrlEntry.getColumn(idxSnmp4jAgentHBCtrlRowStatus)).
        addRowStatusListener(this);
    snmp4jAgentHBCtrlEntry.addMOTableRowListener(this);
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
  public Snmp4jHeartbeatMib(NotificationOriginator notificationOriginator,
                            OctetString context, SysUpTime upTime) {
    this();

    this.notificationOriginator = notificationOriginator;
    this.context = context;
    this.sysUpTime = upTime;
    // make sure that the heartbeat timer related objects are not modified
    // while row is active:
    for (int i=0; i<PROTECTED_COLS.length; i++) {
      ((MOMutableColumn)
       snmp4jAgentHBCtrlEntry.getColumn(i)).setMutableInService(false);
    }
  }

//--AgentGen END


  public MOTable getSnmp4jAgentHBCtrlEntry() {
    return snmp4jAgentHBCtrlEntry;
  }

  private void createSnmp4jAgentHBCtrlEntry() {
    MOColumn[] snmp4jAgentHBCtrlEntryColumns = new MOColumn[8];
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlStartTime] =
        new DateAndTime(colSnmp4jAgentHBCtrlStartTime,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        null);
    ValueConstraint snmp4jAgentHBCtrlStartTimeVC = new ConstraintsImpl();
    ((ConstraintsImpl) snmp4jAgentHBCtrlStartTimeVC).add(new Constraint(8, 8));
    ((ConstraintsImpl) snmp4jAgentHBCtrlStartTimeVC).add(new Constraint(11, 11));
    ((MOMutableColumn) snmp4jAgentHBCtrlEntryColumns[
     idxSnmp4jAgentHBCtrlStartTime]).
        addMOValueValidationListener(new ValueConstraintValidator(
        snmp4jAgentHBCtrlStartTimeVC));
    // Although there is only a null default value, this column does not need
    // to be set:
    ((MOMutableColumn) snmp4jAgentHBCtrlEntryColumns[
     idxSnmp4jAgentHBCtrlStartTime]).setMandatory(false);
    ((MOMutableColumn) snmp4jAgentHBCtrlEntryColumns[
     idxSnmp4jAgentHBCtrlStartTime]).
        addMOValueValidationListener(new Snmp4jAgentHBCtrlStartTimeValidator());
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlDelay] =
        new MOMutableColumn(colSnmp4jAgentHBCtrlDelay,
                            SMIConstants.SYNTAX_GAUGE32,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new UnsignedInteger32(1000));
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlPeriod] =
        new MOMutableColumn(colSnmp4jAgentHBCtrlPeriod,
                            SMIConstants.SYNTAX_GAUGE32,
                            MOAccessImpl.ACCESS_READ_CREATE,
                            new UnsignedInteger32(60000));
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlMaxEvents] =
      new MOMutableColumn(colSnmp4jAgentHBCtrlMaxEvents,
                          SMIConstants.SYNTAX_GAUGE32,
                          MOAccessImpl.ACCESS_READ_CREATE,
                          new UnsignedInteger32(0));
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlEvents] =
      moFactory.createColumn(colSnmp4jAgentHBCtrlEvents,
                             SMIConstants.SYNTAX_COUNTER64,
                             MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlLastChange] =
        moFactory.createColumn(colSnmp4jAgentHBCtrlLastChange,
                               SMIConstants.SYNTAX_TIMETICKS,
                               MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlEvents] =
        moFactory.createColumn(colSnmp4jAgentHBCtrlEvents,
                               SMIConstants.SYNTAX_COUNTER64,
                               MOAccessImpl.ACCESS_READ_ONLY);
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlStorageType] =
        new StorageType(colSnmp4jAgentHBCtrlStorageType,
                        MOAccessImpl.ACCESS_READ_CREATE,
                        new Integer32(3));
    ValueConstraint snmp4jAgentHBCtrlStorageTypeVC = new EnumerationConstraint(
        new int[] {Snmp4jAgentHBCtrlStorageTypeEnum.other,
        Snmp4jAgentHBCtrlStorageTypeEnum._volatile,
        Snmp4jAgentHBCtrlStorageTypeEnum.nonVolatile,
        Snmp4jAgentHBCtrlStorageTypeEnum.permanent,
        Snmp4jAgentHBCtrlStorageTypeEnum.readOnly});
    ((MOMutableColumn)snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlStorageType]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jAgentHBCtrlStorageTypeVC));
    snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlRowStatus] =
        new RowStatus(colSnmp4jAgentHBCtrlRowStatus);
    ValueConstraint snmp4jAgentHBCtrlRowStatusVC = new EnumerationConstraint(
        new int[] {Snmp4jAgentHBCtrlRowStatusEnum.active,
        Snmp4jAgentHBCtrlRowStatusEnum.notInService,
        Snmp4jAgentHBCtrlRowStatusEnum.notReady,
        Snmp4jAgentHBCtrlRowStatusEnum.createAndGo,
        Snmp4jAgentHBCtrlRowStatusEnum.createAndWait,
        Snmp4jAgentHBCtrlRowStatusEnum.destroy});
    ((MOMutableColumn)snmp4jAgentHBCtrlEntryColumns[idxSnmp4jAgentHBCtrlRowStatus]).
      addMOValueValidationListener(new ValueConstraintValidator(snmp4jAgentHBCtrlRowStatusVC));

    snmp4jAgentHBCtrlEntryModel = new DefaultMOMutableTableModel<Snmp4jAgentHBCtrlEntryRow>();
    snmp4jAgentHBCtrlEntryModel.setRowFactory(new Snmp4jAgentHBCtrlEntryRowFactory());
    snmp4jAgentHBCtrlEntry =
        moFactory.createTable(oidSnmp4jAgentHBCtrlEntry,
                              snmp4jAgentHBCtrlEntryIndex,
                              snmp4jAgentHBCtrlEntryColumns,
                              snmp4jAgentHBCtrlEntryModel);
  }

  public void registerMOs(MOServer server, OctetString context) throws
      DuplicateRegistrationException {
    // Scalar Objects
    server.register(this.snmp4jAgentHBRefTime, context);
    server.register(this.snmp4jAgentHBCtrlEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.snmp4jAgentHBRefTime, context);
    server.unregister(this.snmp4jAgentHBCtrlEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications
  public void snmp4jAgentHBEvent(NotificationOriginator notificationOriginator,
                                 OctetString context, VariableBinding[] vbs) {
    if (vbs.length < 1) {
      throw new IllegalArgumentException("Too few notification objects: " +
                                         vbs.length + "<1");
    }
    if (!(vbs[0].getOid().startsWith(oidTrapVarSnmp4jAgentHBCtrlEvents))) {
      throw new IllegalArgumentException("Variable 0 has wrong OID: " +
                                         vbs[0].getOid() +
                                         " does not start with " +
                                         oidTrapVarSnmp4jAgentHBCtrlEvents);
    }
    if (!snmp4jAgentHBCtrlEntryIndex.isValidIndex(snmp4jAgentHBCtrlEntry.
                                                  getIndexPart(vbs[0].getOid()))) {
      throw new IllegalArgumentException(
          "Illegal index for variable 0 specified: " +
          snmp4jAgentHBCtrlEntry.getIndexPart(vbs[0].getOid()));
    }
    notificationOriginator.notify(context, oidSnmp4jAgentHBEvent, vbs);
  }

  // Scalars
  public class Snmp4jAgentHBRefTime extends DateAndTimeScalar<OctetString> {
    Snmp4jAgentHBRefTime(OID oid, MOAccess access) {
      super(oid, access, new OctetString());
//--AgentGen BEGIN=snmp4jAgentHBRefTime
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
          request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
        return valueOK;
      }
      OctetString os = (OctetString) newValue;
      if (!(((os.length() >= 8) && (os.length() <= 8)) ||
            ((os.length() >= 11) && (os.length() <= 11)))) {
        valueOK = SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
      }
      //--AgentGen BEGIN=snmp4jAgentHBRefTime::isValueOK
      //--AgentGen END
      return valueOK;
    }

    public OctetString getValue() {
      //--AgentGen BEGIN=snmp4jAgentHBRefTime::getValue
      GregorianCalendar gc = new GregorianCalendar();
      gc.add(Calendar.MILLISECOND, heartbeatOffset);
      super.setValue(DateAndTime.makeDateAndTime(gc));
      //--AgentGen END
      return super.getValue();
    }

    public int setValue(OctetString newValue) {
      //--AgentGen BEGIN=snmp4jAgentHBRefTime::setValue
      GregorianCalendar gc = DateAndTime.makeCalendar((OctetString) newValue);
      GregorianCalendar curGC = new GregorianCalendar();
      heartbeatOffset = (int) (gc.getTimeInMillis() - curGC.getTimeInMillis());
      //--AgentGen END
      return super.setValue(newValue);
    }

    //--AgentGen BEGIN=snmp4jAgentHBRefTime::_METHODS
    public void load(MOInput input) throws IOException {
      heartbeatOffset = ((Integer32)input.readVariable()).getValue();
    }

    public void save(MOOutput output) throws IOException {
      output.writeVariable(new Integer32(heartbeatOffset));
    }
    //--AgentGen END

  }

  // Value Validators
  /**
   * The <code>Snmp4jAgentHBRefTimeValidator</code> implements the value
   * validation for <code>Snmp4jAgentHBRefTime</code>.
   */
  static class Snmp4jAgentHBRefTimeValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString) newValue;
      if (!(((os.length() >= 8) && (os.length() <= 8)) ||
            ((os.length() >= 11) && (os.length() <= 11)))) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_LENGTH);
        return;
      }
      //--AgentGen BEGIN=snmp4jAgentHBRefTime::validate
      //--AgentGen END
    }
  }

  /**
   * The <code>Snmp4jAgentHBCtrlStartTimeValidator</code> implements the value
   * validation for <code>Snmp4jAgentHBCtrlStartTime</code>.
   */
  static class Snmp4jAgentHBCtrlStartTimeValidator implements
      MOValueValidationListener {

    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString) newValue;
      if (!(((os.length() >= 8) && (os.length() <= 8)) ||
            ((os.length() >= 11) && (os.length() <= 11)))) {
        validationEvent.setValidationStatus(SnmpConstants.
                                            SNMP_ERROR_WRONG_LENGTH);
        return;
      }
      //--AgentGen BEGIN=snmp4jAgentHBCtrlStartTime::validate
      //--AgentGen END
    }
  }

  // Rows and Factories

  public class Snmp4jAgentHBCtrlEntryRow extends DefaultMOMutableRow2PC {
    public Snmp4jAgentHBCtrlEntryRow(OID index, Variable[] values) {
      super(index, values);
    }

    public OctetString getSnmp4jAgentHBCtrlStartTime() {
      return (OctetString) getValue(idxSnmp4jAgentHBCtrlStartTime);
    }

    public void setSnmp4jAgentHBCtrlStartTime(OctetString newValue) {
      setValue(idxSnmp4jAgentHBCtrlStartTime, newValue);
    }

    public UnsignedInteger32 getSnmp4jAgentHBCtrlDelay() {
      return (UnsignedInteger32) getValue(idxSnmp4jAgentHBCtrlDelay);
    }

    public void setSnmp4jAgentHBCtrlDelay(UnsignedInteger32 newValue) {
      setValue(idxSnmp4jAgentHBCtrlDelay, newValue);
    }

    public UnsignedInteger32 getSnmp4jAgentHBCtrlPeriod() {
      return (UnsignedInteger32) getValue(idxSnmp4jAgentHBCtrlPeriod);
    }

    public void setSnmp4jAgentHBCtrlPeriod(UnsignedInteger32 newValue) {
      setValue(idxSnmp4jAgentHBCtrlPeriod, newValue);
    }

    public UnsignedInteger32 getSnmp4jAgentHBCtrlMaxEvents() {
      return (UnsignedInteger32) getValue(idxSnmp4jAgentHBCtrlMaxEvents);
    }

    public void setSnmp4jAgentHBCtrlMaxEvents(UnsignedInteger32 newValue) {
      setValue(idxSnmp4jAgentHBCtrlMaxEvents, newValue);
    }

    public Counter64 getSnmp4jAgentHBCtrlEvents() {
      return (Counter64) getValue(idxSnmp4jAgentHBCtrlEvents);
    }

    public void setSnmp4jAgentHBCtrlEvents(Counter64 newValue) {
      setValue(idxSnmp4jAgentHBCtrlEvents, newValue);
    }

    public TimeTicks getSnmp4jAgentHBCtrlLastChange() {
      return (TimeTicks) getValue(idxSnmp4jAgentHBCtrlLastChange);
    }

    public void setSnmp4jAgentHBCtrlLastChange(TimeTicks newValue) {
      setValue(idxSnmp4jAgentHBCtrlLastChange, newValue);
    }

    public Integer32 getSnmp4jAgentHBCtrlStorageType() {
      return (Integer32) getValue(idxSnmp4jAgentHBCtrlStorageType);
    }

    public void setSnmp4jAgentHBCtrlStorageType(Integer32 newValue) {
      setValue(idxSnmp4jAgentHBCtrlStorageType, newValue);
    }

    public Integer32 getSnmp4jAgentHBCtrlRowStatus() {
      return (Integer32) getValue(idxSnmp4jAgentHBCtrlRowStatus);
    }

    public void setSnmp4jAgentHBCtrlRowStatus(Integer32 newValue) {
      setValue(idxSnmp4jAgentHBCtrlRowStatus, newValue);
    }

    //--AgentGen BEGIN=snmp4jAgentHBCtrlEntry::Row
    //--AgentGen END
  }

  class Snmp4jAgentHBCtrlEntryRowFactory
      implements MOTableRowFactory<Snmp4jAgentHBCtrlEntryRow> {
    public synchronized Snmp4jAgentHBCtrlEntryRow createRow(OID index, Variable[] values) throws
        UnsupportedOperationException {
      Snmp4jAgentHBCtrlEntryRow row = new Snmp4jAgentHBCtrlEntryRow(index,
          values);
      //--AgentGen BEGIN=snmp4jAgentHBCtrlEntry::createRow
      row.setSnmp4jAgentHBCtrlLastChange(sysUpTime.get());
      row.setSnmp4jAgentHBCtrlEvents(new Counter64(0));
      //--AgentGen END
      return row;
    }

    public synchronized void freeRow(Snmp4jAgentHBCtrlEntryRow row) {
      //--AgentGen BEGIN=snmp4jAgentHBCtrlEntry::freeRow
      //--AgentGen END
    }

    //--AgentGen BEGIN=snmp4jAgentHBCtrlEntry::RowFactory
    //--AgentGen END
  }

//--AgentGen BEGIN=_METHODS
  public void rowStatusChanged(RowStatusEvent event) {
    if (event.isDeniable()) {
      if (event.isRowActivated()) {
        // check column interdependent consistency
        Snmp4jAgentHBCtrlEntryRow row =
            (Snmp4jAgentHBCtrlEntryRow) event.getRow();
        if ((row.getSnmp4jAgentHBCtrlDelay().getValue() == 0) &&
            ((row.getSnmp4jAgentHBCtrlStartTime() == null) ||
             (DateAndTime.makeCalendar(
                 row.getSnmp4jAgentHBCtrlStartTime()).getTimeInMillis()
              <= System.currentTimeMillis()))) {
          event.setDenyReason(PDU.inconsistentValue);
        }
      }
    }
    else if (event.isRowActivated()) {
      Snmp4jAgentHBCtrlEntryRow row =
          (Snmp4jAgentHBCtrlEntryRow) event.getRow();
      HeartbeatTask task = new HeartbeatTask(row);
      if (row.getSnmp4jAgentHBCtrlDelay().getValue() == 0) {
        long startTime = DateAndTime.makeCalendar(
                 row.getSnmp4jAgentHBCtrlStartTime()).getTimeInMillis() -
            heartbeatOffset;
        heartbeatTimer.schedule(task,
                                new Date(startTime),
                                row.getSnmp4jAgentHBCtrlPeriod().getValue());
      }
      else {
        heartbeatTimer.schedule(task,
                                row.getSnmp4jAgentHBCtrlDelay().getValue(),
                                row.getSnmp4jAgentHBCtrlPeriod().getValue());
      }
      row.setUserObject(task);
    }
    else if (event.isRowDeactivated()) {
      Snmp4jAgentHBCtrlEntryRow row =
          (Snmp4jAgentHBCtrlEntryRow) event.getRow();
      HeartbeatTask task = (HeartbeatTask) row.getUserObject();
      if (task != null) {
        task.cancel();
      }
    }
  }

  public void rowChanged(MOTableRowEvent event) {
    if (event.getRow() != null) {
      Snmp4jAgentHBCtrlEntryRow row =
          (Snmp4jAgentHBCtrlEntryRow) event.getRow();
      if (row.getSnmp4jAgentHBCtrlLastChange() != null) {
        row.getSnmp4jAgentHBCtrlLastChange().setValue(sysUpTime.get().getValue());
      }
    }
  }

  //--AgentGen END

//--AgentGen BEGIN=_CLASSES

  class HeartbeatTask extends TimerTask {

    private Snmp4jAgentHBCtrlEntryRow configRow;

    public HeartbeatTask(Snmp4jAgentHBCtrlEntryRow configRow) {
      this.configRow = configRow;
    }

    public void run() {
      if (configRow.getSnmp4jAgentHBCtrlRowStatus().getValue() ==
          RowStatus.active) {
        long maxEvents = configRow.getSnmp4jAgentHBCtrlMaxEvents().getValue();
        if ((maxEvents > 0) &&
            (configRow.getSnmp4jAgentHBCtrlEvents().getValue() < maxEvents)) {
          configRow.getSnmp4jAgentHBCtrlEvents().increment();
          OID instanceOID =
              ((DefaultMOTable) snmp4jAgentHBCtrlEntry).
              getCellOID(configRow.getIndex(),
                         idxSnmp4jAgentHBCtrlEvents);
          VariableBinding eventVB = new VariableBinding(instanceOID,
              configRow.getSnmp4jAgentHBCtrlEvents());
          snmp4jAgentHBEvent(notificationOriginator, context,
                             new VariableBinding[] {eventVB});
        }
        else {
          cancel();
          configRow.getSnmp4jAgentHBCtrlRowStatus().setValue(RowStatus.notInService);
        }
      }
      else {
        cancel();
      }
    }
  }

//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}
