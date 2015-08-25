/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnapshotAgent.java  
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

package org.snmp4j.agent.test;

import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
import org.snmp4j.agent.security.MutableVACM;
import java.io.File;
import org.apache.log4j.BasicConfigurator;
import java.io.IOException;
import org.snmp4j.agent.io.ImportModes;
import org.snmp4j.mp.MPv3;
import org.snmp4j.util.ThreadPool;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.Integer32;
import java.util.List;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.smi.VariableBinding;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;
import org.snmp4j.agent.mo.ext.StaticMOGroup;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.TransportMappings;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB.*;

/**
 *
 * @author Frank Fock
 * @version 1.0
 */
public class SnapshotAgent extends BaseAgent {

  // initialize Log4J logging
  static {
    LogFactory.setLogFactory(new Log4jLogFactory());
  }

  private static final LogAdapter logger =
      LogFactory.getLogger(SnapshotAgent.class);

  protected String address;
  protected File snapshot;
  protected List<StaticMOGroup> groups = new ArrayList<StaticMOGroup>();

  public SnapshotAgent(File bootCounterFile, File configFile,
                       File snapshot) throws IOException {
    super(bootCounterFile, configFile,
          new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
// Alternatively:       OctetString.fromHexString("00:00:00:00:00:00:02", ':');
    agent.setWorkerPool(ThreadPool.create("RequestPool", 2));
    this.snapshot = snapshot;
  }


  /**
   * Adds community to security name mappings needed for SNMPv1 and SNMPv2c.
   *
   * @param communityMIB the SnmpCommunityMIB holding coexistence
   *   configuration for community based security models.
   */
  protected void addCommunities(SnmpCommunityMIB communityMIB) {
    Variable[] com2sec = new Variable[] {
        new OctetString("public"),              // community name
        new OctetString("cpublic"),              // security name
        getAgent().getContextEngineID(),        // local engine ID
        new OctetString(),              // default context name
        new OctetString(),                      // transport tag
        new Integer32(StorageType.nonVolatile), // storage type
        new Integer32(RowStatus.active)         // row status
    };
    SnmpCommunityEntryRow row =
        communityMIB.getSnmpCommunityEntry().createRow(
          new OctetString("public2public").toSubIndex(true), com2sec);
    communityMIB.getSnmpCommunityEntry().addRow(row);
  }

  /**
   * Adds initial notification targets and filters.
   *
   * @param targetMIB the SnmpTargetMIB holding the target configuration.
   * @param notificationMIB the SnmpNotificationMIB holding the notification
   *   (filter) configuration.
   */
  protected void addNotificationTargets(SnmpTargetMIB targetMIB,
                                        SnmpNotificationMIB notificationMIB) {
  }

  /**
   * Adds all the necessary initial users to the USM.
   *
   * @param usm the USM instance used by this agent.
   */
  protected void addUsmUser(USM usm) {
  }

  /**
   * Adds initial VACM configuration.
   *
   * @param vacm
   *    the VacmMIB holding the agent's view configuration.
   */
  protected void addViews(VacmMIB vacm) {
    vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv1,
                  new OctetString("cpublic"),
                  new OctetString("v1v2group"),
                  StorageType.nonVolatile);
    vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c,
                  new OctetString("cpublic"),
                  new OctetString("v1v2group"),
                  StorageType.nonVolatile);
    vacm.addGroup(SecurityModel.SECURITY_MODEL_USM,
                  new OctetString("SHADES"),
                  new OctetString("v3group"),
                  StorageType.nonVolatile);
    vacm.addGroup(SecurityModel.SECURITY_MODEL_USM,
                  new OctetString("TEST"),
                  new OctetString("v3test"),
                  StorageType.nonVolatile);
    vacm.addGroup(SecurityModel.SECURITY_MODEL_USM,
                  new OctetString("SHA"),
                  new OctetString("v3restricted"),
                  StorageType.nonVolatile);
    vacm.addGroup(SecurityModel.SECURITY_MODEL_USM,
                  new OctetString("v3notify"),
                  new OctetString("v3restricted"),
                  StorageType.nonVolatile);

    vacm.addAccess(new OctetString("v1v2group"), new OctetString(),
                   SecurityModel.SECURITY_MODEL_ANY,
                   SecurityLevel.NOAUTH_NOPRIV,
                   MutableVACM.VACM_MATCH_EXACT,
                   new OctetString("fullReadView"),
                   new OctetString("fullWriteView"),
                   new OctetString("fullNotifyView"),
                   StorageType.nonVolatile);
    vacm.addAccess(new OctetString("v3group"), new OctetString(),
                   SecurityModel.SECURITY_MODEL_USM,
                   SecurityLevel.AUTH_PRIV,
                   MutableVACM.VACM_MATCH_EXACT,
                   new OctetString("fullReadView"),
                   new OctetString("fullWriteView"),
                   new OctetString("fullNotifyView"),
                   StorageType.nonVolatile);
    vacm.addAccess(new OctetString("v3restricted"), new OctetString(),
                   SecurityModel.SECURITY_MODEL_USM,
                   SecurityLevel.NOAUTH_NOPRIV,
                   MutableVACM.VACM_MATCH_EXACT,
                   new OctetString("restrictedReadView"),
                   new OctetString("restrictedWriteView"),
                   new OctetString("restrictedNotifyView"),
                   StorageType.nonVolatile);
    vacm.addAccess(new OctetString("v3test"), new OctetString(),
                   SecurityModel.SECURITY_MODEL_USM,
                   SecurityLevel.AUTH_PRIV,
                   MutableVACM.VACM_MATCH_EXACT,
                   new OctetString("testReadView"),
                   new OctetString("testWriteView"),
                   new OctetString("testNotifyView"),
                   StorageType.nonVolatile);

    vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("fullNotifyView"), new OID("1.3"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);

    vacm.addViewTreeFamily(new OctetString("restrictedReadView"),
                           new OID("1.3.6.1.2"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("restrictedWriteView"),
                           new OID("1.3.6.1.2.1"),
                           new OctetString(),
                           VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("restrictedNotifyView"),
                           new OID("1.3.6.1.2"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("restrictedNotifyView"),
                           new OID("1.3.6.1.6.3.1"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);

    vacm.addViewTreeFamily(new OctetString("testReadView"),
                           new OID("1.3.6.1.2"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("testReadView"),
                           new OID("1.3.6.1.2.1.1"),
                           new OctetString(), VacmMIB.vacmViewExcluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("testWriteView"),
                           new OID("1.3.6.1.2.1"),
                           new OctetString(),
                           VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);
    vacm.addViewTreeFamily(new OctetString("testNotifyView"),
                           new OID("1.3.6.1.2"),
                           new OctetString(), VacmMIB.vacmViewIncluded,
                           StorageType.nonVolatile);

  }

  /**
   * Register additional managed objects at the agent's server.
   */
  protected void registerManagedObjects() {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(snapshot);
      ObjectInputStream ois = new ObjectInputStream(fis);
      @SuppressWarnings("unchecked")
      List<VariableBinding> l = (List) ois.readObject();
      ois.close();
      logger.info("Snapshot file '"+snapshot+
                  "' contains "+l.size()+" objects.");
      OctetString ctx = new OctetString();
      SortedMap<OID, OID> roots = new TreeMap<OID, OID>();
      OID last = null;
      for (VariableBinding vb : l) {
        if (last != null) {
          int min = Math.min(vb.getOid().size(), last.size());
          while (min > 0) {
            if (vb.getOid().leftMostCompare(min, last) == 0) {
              OID root = new OID(last.getValue(), 0, min);
              roots.put(root, root);
              break;
            }
            min--;
          }
        }
        last = vb.getOid();
      }
      logger.info("Identified the following sub-tree candidates: "+roots);
      SortedMap<OID, OID> rootsCopy = new TreeMap<OID, OID>();
      for (OID k : roots.keySet()) {
        if (k.size() > 1) {
          OID sk = new OID(k.getValue(), 0, k.size() - 1);
          while ((sk.size() > 0) && (roots.get(sk) == null)) {
            sk.trim(1);
          }
          if (sk.size() == 0) {
            rootsCopy.put(k, k);
          }
        }
      }
      logger.info("Identified the following sub-trees "+rootsCopy);
      for (OID root : rootsCopy.keySet()) {
        ArrayList<VariableBinding> subtree = new ArrayList<VariableBinding>();
        for (VariableBinding vb : l) {
          if (vb.getOid().size() >= root.size()) {
            if (vb.getOid().leftMostCompare(root.size(), root) == 0) {
              subtree.add(vb);
            }
          }
        }
        StaticMOGroup group =
            new StaticMOGroup(root, subtree.toArray(new VariableBinding[subtree.size()]));
        DefaultMOContextScope scope =
            new DefaultMOContextScope(ctx,
                root, true, root.nextPeer(), false);
        ManagedObject mo = server.lookup(new DefaultMOQuery(scope, false));
        if (mo != null) {
          logger.warn("Could not register snapshot subtree '" + root +
              "' with " + subtree + " because ManagedObject " + mo +
              " is already registered");
          for (VariableBinding vb : subtree) {
            group = new StaticMOGroup(vb.getOid(), new VariableBinding[]{ vb });
            scope = new DefaultMOContextScope(ctx, vb.getOid(), true, vb.getOid().nextPeer(), false);
            mo = server.lookup(new DefaultMOQuery(scope, false));
            if (mo != null) {
              logger.warn("Could not register single OID at "+vb.getOid()+" because ManagedObject "+mo+" is already registered.");
            }
            else {
              groups.add(group);
              server.register(group, null);
              logger.info("Registered snapshot subtree '" + root + "' with " + vb);
            }
          }
        } else {
          groups.add(group);
          server.register(group, null);
          logger.info("Registered snapshot subtree '" + root +
              "' with " + subtree);
        }
      }
    }
    catch (Exception ex) {
      logger.error("Error while reading snapshot file '"+snapshot+"':"+
                   ex.getMessage(), ex);
    }
  }

  /**
   * Unregister additional managed objects from the agent's server.
   */
  protected void unregisterManagedObjects() {
    for (StaticMOGroup mo : groups) {
      server.unregister(mo, null);
    }
  }

  protected void initTransportMappings() throws IOException {
    transportMappings = new TransportMapping[1];
    Address addr = GenericAddress.parse(address);
    TransportMapping tm =
        TransportMappings.getInstance().createTransportMapping(addr);
    transportMappings[0] = tm;
  }

  public static void main(String[] args) {
    String address;
    File snapshot = new File(args[0]);
    BasicConfigurator.configure();

    if (args.length > 1) {
      address = args[1];
    }
    else {
      address = "0.0.0.0/161";
    }
    try {
      SnapshotAgent testAgent1 =
          new SnapshotAgent(new File("SNMP4JSnapshotAgentBC.cfg"),
                            new File("SNMP4JSnapshotAgentConfig.cfg"),
                            snapshot);
      testAgent1.address = address;
      testAgent1.init();
      testAgent1.loadConfig(ImportModes.REPLACE_CREATE);
      testAgent1.addShutdownHook();
      testAgent1.getServer().addContext(new OctetString("public"));
      testAgent1.finishInit();
      testAgent1.run();
      testAgent1.sendColdStartNotification();
      while (true) {
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException ex1) {
          break;
        }
      }
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }


  }
}
