/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SNMPv2MIB.java  
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
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;
import org.snmp4j.event.CounterListener;
import org.snmp4j.event.CounterEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;

/**
 * The <code>SystemGroup</code> implements the objects in the SNMPv2-MIB
 * specification.
 * Since version 2.0, the SysUpTime class uses {@link System#nanoTime()}
 * instead {@link System#currentTimeMillis()} for up-time computation
 * which makes the implementation stable against system time changes.
 *
 * @author Frank Fock
 * @version 2.0
 */
public class SNMPv2MIB implements MOGroup, CounterListener, AgentCapabilityList {

  private static final LogAdapter LOGGER = LogFactory.getLogger(SNMPv2MIB.class);

  // Constants
  private static final OID snmpStatsPrefix =
      new OID(new int[] { 1,3,6,1,2,1,11 });
  private static final OID usmStatsPrefix =
      new OID(new int[] { 1,3,6,1,6,3,15,1,1 });


  private static final OID oidSnmpInPkts =
      SnmpConstants.snmpInPkts;
  private static final OID oidSnmpInBadVersions =
      SnmpConstants.snmpInBadVersions;
  private static final OID oidSnmpInBadCommunityNames =
      SnmpConstants.snmpInBadCommunityNames;
  private static final OID oidSnmpInBadCommunityUses =
      SnmpConstants.snmpInBadCommunityUses;
  private static final OID oidSnmpInASNParseErrs =
      SnmpConstants.snmpInASNParseErrs;
  private static final OID oidSnmpSilentDrops =
      SnmpConstants.snmpSilentDrops;
  private static final OID oidSnmpProxyDrops =
      SnmpConstants.snmpProxyDrops;

  private static final OID oidSysORLastChange =
    new OID(new int[] { 1,3,6,1,2,1,1,8,0 });

  private static final OID oidSnmpEnableAuthenTraps =
    new OID(new int[] { 1,3,6,1,2,1,11,30,0 });

  private static final OID oidSnmpSetSerialNo =
      new OID(new int[] { 1,3,6,1,6,3,1,1,6,1,0 });


  private MOScalar<OctetString> descr;
  private SysUpTimeImpl upTime;
  private MOScalar<OID> objectID;
  private MOScalar<OctetString> contact;
  private MOScalar<OctetString> name;
  private MOScalar<OctetString> location;
  private MOScalar<Integer32> services;
  private MOScalar<Integer32> snmpEnableAuthenTraps;
  private TestAndIncr snmpSetSerialNo;
  private MOScalar<TimeTicks> sysORLastChange;

  private static final OID[] snmpStatsOIDs = {
      oidSnmpInPkts, oidSnmpInBadVersions, oidSnmpInBadCommunityNames,
      oidSnmpInBadCommunityUses, oidSnmpInASNParseErrs,
      oidSnmpSilentDrops, oidSnmpProxyDrops
  };

  private MOScalar[] snmpStats;

  private static final OID oidSysOREntry =
      new OID(new int[] { 1,3,6,1,2,1,1,9,1 });

  // Column sub-identifier definitions for sysOREntry:
  private static final int colSysORID = 2;
  private static final int colSysORDescr = 3;
  private static final int colSysORUpTime = 4;

  // Column index definitions for sysOREntry:
  private static final int idxSysORID = 0;
  private static final int idxSysORDescr = 1;
  private static final int idxSysORUpTime = 2;
  private static MOTableSubIndex[] sysOREntryIndexes =
      new MOTableSubIndex[] {
      new MOTableSubIndex(SMIConstants.SYNTAX_INTEGER, 1, 1)  };

  private static MOTableIndex sysOREntryIndex =
      new MOTableIndex(sysOREntryIndexes, false);

  private DefaultMOTable<DefaultMOTableRow, MOColumn, DefaultMOMutableTableModel<DefaultMOTableRow>> sysOREntry;
  private DefaultMOMutableTableModel<DefaultMOTableRow> sysOREntryModel;

  // Notification originator for authenticationFailure trap
  private NotificationOriginator notificationOriginator;

  // Context to sysUpTime Map
  private static Map<OctetString, SysUpTimeImpl> sysUpTimes =
      Collections.synchronizedMap(new HashMap<OctetString, SysUpTimeImpl>());

  public SNMPv2MIB(OctetString sysDescr,
                   OID sysOID,
                   Integer32 sysServices) {
    this.descr = new MOScalar<OctetString>(new OID(SnmpConstants.sysDescr),
                              MOAccessImpl.ACCESS_READ_ONLY,
                              sysDescr);
    this.descr.setVolatile(true);
    this.upTime = new SysUpTimeImpl();
    this.upTime.setVolatile(true);
    this.objectID = new MOScalar<OID>(new OID(SnmpConstants.sysObjectID),
                                 MOAccessImpl.ACCESS_READ_ONLY,
                                 sysOID);
    this.objectID.setVolatile(true);
    this.contact =
        new DisplayStringScalar<OctetString>(new OID(SnmpConstants.sysContact),
                                MOAccessImpl.ACCESS_READ_WRITE,
                                new OctetString());
    this.name =
        new DisplayStringScalar<OctetString>(new OID(SnmpConstants.sysName),
                                MOAccessImpl.ACCESS_READ_WRITE,
                                new OctetString());
    this.location =
        new DisplayStringScalar<OctetString>(new OID(SnmpConstants.sysLocation),
                                MOAccessImpl.ACCESS_READ_WRITE,
                                new OctetString());
    this.services = new MOScalar<Integer32>(new OID(SnmpConstants.sysServices),
                                 MOAccessImpl.ACCESS_READ_ONLY,
                                 sysServices);
    this.services.setVolatile(true);
    snmpEnableAuthenTraps =
      new EnumeratedScalar<Integer32>(oidSnmpEnableAuthenTraps,
                           MOAccessImpl.ACCESS_READ_WRITE,
                           new Integer32(SnmpEnableAuthenTrapsEnum.enabled),
                           new int[] { SnmpEnableAuthenTrapsEnum.enabled,
                                       SnmpEnableAuthenTrapsEnum.disabled });
    snmpSetSerialNo = new TestAndIncr(oidSnmpSetSerialNo);
    snmpSetSerialNo.setVolatile(true);

    sysORLastChange =
        new TimeStampScalar(oidSysORLastChange,
                            MOAccessImpl.ACCESS_READ_ONLY, upTime);
    sysORLastChange.setVolatile(true);
    createSysOREntry();
    createSnmpStats();
  }

  private void createSnmpStats() {
    snmpStats = new MOScalar[snmpStatsOIDs.length];
    for (int i=0; i<snmpStatsOIDs.length; i++) {
      snmpStats[i] = new MOScalar<Counter32>(snmpStatsOIDs[i],
                                  MOAccessImpl.ACCESS_READ_ONLY,
                                  new Counter32(0));
    }
  }

  private void createSysOREntry() {
    MOColumn[] sysOREntryColumns = new MOColumn[3];
    sysOREntryColumns[idxSysORID] =
      new MOColumn(colSysORID,
                   SMIConstants.SYNTAX_OBJECT_IDENTIFIER,
                   MOAccessImpl.ACCESS_READ_ONLY);
    sysOREntryColumns[idxSysORDescr] =
      new DisplayString<OctetString>(colSysORDescr,
                        MOAccessImpl.ACCESS_READ_ONLY,
                        null,
                        true);
    sysOREntryColumns[idxSysORUpTime] =
      new MOColumn(colSysORUpTime,
                   SMIConstants.SYNTAX_TIMETICKS,
                   MOAccessImpl.ACCESS_READ_ONLY);

    sysOREntry =
      new SysOREntry(oidSysOREntry,
                     sysOREntryIndex,
                     sysOREntryColumns);
    sysOREntryModel = new DefaultMOMutableTableModel<DefaultMOTableRow>();
    sysOREntryModel.setRowFactory(new MOTableRowFactory<DefaultMOTableRow>() {
      @Override
      public DefaultMOTableRow createRow(OID index, Variable[] values) throws UnsupportedOperationException {
        return new DefaultMOTableRow(index, values);
      }

      @Override
      public void freeRow(DefaultMOTableRow row) {
      }
    });
    sysOREntry.setModel(sysOREntryModel);
    sysOREntry.setVolatile(true);
  }

  protected void updateSysORLastChange() {
    sysORLastChange.setValue(getUpTime());
  }

  public OID addSysOREntry(OID sysORID, OctetString sysORDescr) {
    OID index = new OID(new int[] { sysOREntryModel.getRowCount()+1 });
    Variable[] values = new Variable[sysOREntry.getColumnCount()];
    int n = 0;
    values[n++] = sysORID;
    values[n++] = sysORDescr;
    values[n  ] = upTime.get();
    DefaultMOTableRow row = new DefaultMOTableRow(index, values);
    sysOREntry.addRow(row);
    updateSysORLastChange();
    return index;
  }

  public MOTableRow removeSysOREntry(OID index) {
    updateSysORLastChange();
    return sysOREntry.removeRow(index);
  }

  public void registerMOs(MOServer server, OctetString context)
      throws DuplicateRegistrationException
  {
    server.register(descr, context);
    server.register(upTime, context);
    sysUpTimes.put(context, upTime);
    server.register(objectID, context);
    server.register(contact, context);
    server.register(name, context);
    server.register(location, context);
    server.register(services, context);
    server.register(snmpEnableAuthenTraps, context);
    server.register(snmpSetSerialNo, context);
    server.register(sysORLastChange, context);
    for (MOScalar snmpStat : snmpStats) {
      server.register(snmpStat, context);
    }
    server.register(sysOREntry, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    server.unregister(descr, context);
    server.unregister(upTime, context);
    sysUpTimes.remove(context);
    server.unregister(objectID, context);
    server.unregister(contact, context);
    server.unregister(name, context);
    server.unregister(location, context);
    server.unregister(services, context);
    server.unregister(snmpEnableAuthenTraps, context);
    server.unregister(snmpSetSerialNo, context);
    server.unregister(sysORLastChange, context);
    for (MOScalar snmpStat : snmpStats) {
      server.unregister(snmpStat, context);
    }
    server.unregister(sysOREntry, context);
  }

  public static final class SnmpEnableAuthenTrapsEnum {
    public static final int enabled = 1;
    public static final int disabled = 2;
  }

  public static class SysUpTimeImpl extends MOScalar<TimeTicks> implements SysUpTime {

    private long startTime;

    public SysUpTimeImpl() {
      super(new OID(SnmpConstants.sysUpTime), MOAccessImpl.ACCESS_READ_ONLY,
            new TimeTicks(0));
      startTime = System.nanoTime();
    }

    public TimeTicks getValue() {
      long ticks = (System.nanoTime() - startTime) / 10000000L;
      return new TimeTicks(ticks & 0xFFFFFFFFL);
    }

    public TimeTicks get() {
      return getValue();
    }

  }

  public OctetString getContact() {
    return contact.getValue();
  }

  public void setContact(OctetString contact) {
    this.contact.setValue(contact);
  }

  public OctetString getDescr() {
    return descr.getValue();
  }

  public OctetString getLocation() {
    return location.getValue();
  }

  public void setLocation(OctetString sysLocation) {
    this.location.setValue(sysLocation);
  }

  public OctetString getName() {
    return name.getValue();
  }

  public void setName(OctetString sysName) {
    this.name.setValue(sysName);
  }

  public OID getObjectID() {
    return objectID.getValue();
  }

  public Integer32 getServices() {
    return services.getValue();
  }

  public TimeTicks getUpTime() {
    return upTime.getValue();
  }

  public SysUpTime getSysUpTime() {
    return upTime;
  }

  public void incrementCounter(CounterEvent event) {
    if ((event.getOid().startsWith(snmpStatsPrefix)) &&
        (event.getOid().size() > snmpStatsPrefix.size())) {
      int suffix = event.getOid().get(snmpStatsPrefix.size());
      for (int i=0; i<snmpStatsOIDs.length; i++) {
        if (suffix == snmpStatsOIDs[i].get(snmpStatsPrefix.size())) {
          Counter32 current = (Counter32) snmpStats[i].getValue();
          current.increment();
          event.setCurrentValue((Counter32)current.clone());
        }
      }
    }
    OID eventOID = event.getOid();
    if ((eventOID.equals(SnmpConstants.snmpInBadCommunityNames)) ||
        ((eventOID.startsWith(usmStatsPrefix)) &&
        !SnmpConstants.usmStatsNotInTimeWindows.equals(eventOID) &&
        !SnmpConstants.usmStatsUnknownEngineIDs.equals(eventOID))) {
      if ((snmpEnableAuthenTraps.getValue()).getValue() ==
          SnmpEnableAuthenTrapsEnum.enabled) {
        if (notificationOriginator == null) {
          LOGGER.debug("Authentication failure trap not sent because "+
                       "NotificationOriginator not set in SNMPv2MIB");
        }
        else {
          notificationOriginator.notify(new OctetString(),
                                        SnmpConstants.authenticationFailure,
                                        new VariableBinding[0]);
        }
      }
    }
  }

  /**
   * Returns the sysUpTime for the supplied context.
   * @param context
   *    a context or <code>null</code> for the default context.
   * @return
   *    the SysUpTime instance associated with the given context or
   *    <code>null</code> if such a sysUpTime instance has not been
   *    registered yet.
   */
  public static SysUpTime getSysUpTime(OctetString context) {
    return sysUpTimes.get(context);
  }

  public class SysOREntry extends DefaultMOTable<DefaultMOTableRow, MOColumn,
                                                 DefaultMOMutableTableModel<DefaultMOTableRow>> implements AgentCapabilityList{

    public SysOREntry(OID oid, MOTableIndex index, MOColumn[] columns) {
      super(oid, index, columns);
    }

    public OID addSysOREntry(OID sysORID, OctetString sysORDescr) {
      return SNMPv2MIB.this.addSysOREntry(sysORID, sysORDescr);
    }

    public MOTableRow removeSysOREntry(OID index) {
      return SNMPv2MIB.this.removeSysOREntry(index);
    }

  }

  /**
   * Sets the <code>NotificationOriginator</code> to be used for sending the
   * authenticationFailure trap.
   * @param notificationOriginator
   *    a NotificationOriginator instance or <code>null</code> to disable
   *    authenticationFailure traps (default).
   */
  public void setNotificationOriginator(NotificationOriginator
                                        notificationOriginator) {
    this.notificationOriginator = notificationOriginator;
  }

  /**
   * Gets the notification originator used for sending authenticationFailure
   * traps.
   * @return
   *   a NotificationOriginator.
   * @since 1.2
   */
  public NotificationOriginator getNotificationOriginator() {
    return notificationOriginator;
  }

}
