/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - AgentConfigManager.java  
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

package org.snmp4j.agent;

import java.io.*;
import java.util.*;

import org.snmp4j.*;
import org.snmp4j.agent.cfg.*;
import org.snmp4j.agent.io.*;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp4j.*;
import org.snmp4j.agent.security.*;
import org.snmp4j.agent.version.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.TLSTM;
import org.snmp4j.util.*;
import org.snmp4j.agent.mo.util.MOTableSizeLimit;
import org.snmp4j.agent.mo.snmp.NotificationLogMib.NlmConfigLogEntryRow;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.version.VersionInfo;

/**
 * The <code>AgentConfigManager</code> is the main component of a SNMP4J-Agent.
 * It puts together agent configuration and agent components like command
 * processor, message dispatcher, managed objects and server, USM, VACM, etc.
 *
 * @author Frank Fock
 * @version 1.2
 * @since 1.2
 */
public class AgentConfigManager implements Runnable {

  private static final LogAdapter logger =
      LogFactory.getLogger(AgentConfigManager.class);

  public static final int STATE_CREATED = 0;
  public static final int STATE_INITIALIZED = 10;
  public static final int STATE_CONFIGURED = 20;
  public static final int STATE_RESTORED = 30;
  public static final int STATE_SUSPENDED = 35;
  public static final int STATE_RUNNING = 40;
  public static final int STATE_UNSAVED_CHANGES = 45;
  public static final int STATE_SAVED = 50;
  public static final int STATE_SHUTDOWN = -1;

  protected CommandProcessor agent;
  protected WorkerPool workerPool;

  protected VACM vacm;
  protected USM usm;
  protected MOServer[] servers;
  protected Session session;
  protected MessageDispatcher dispatcher;
  protected OctetString engineID;
  protected ProxyForwarder proxyForwarder;
  protected NotificationOriginator notificationOriginator;

  protected MOInputFactory configuration;
  protected MOPersistenceProvider persistenceProvider;
  protected int persistenceImportMode = ImportModes.UPDATE_CREATE;

  protected EngineBootsProvider engineBootsProvider;

  // mandatory standard MIBs
  protected SNMPv2MIB snmpv2MIB;
  protected SnmpTargetMIB targetMIB;
  protected SnmpCommunityMIB communityMIB;
  protected SnmpNotificationMIB notificationMIB;
  protected SnmpFrameworkMIB frameworkMIB;
  protected UsmMIB usmMIB;
  protected VacmMIB vacmMIB;
  protected SnmpTlsTmMib tlsTmMib;

  // optional standard MIBs
  protected SnmpProxyMIB proxyMIB;

  // optional SNMP4J MIBs
  protected Snmp4jLogMib snmp4jLogMIB;
  protected Snmp4jConfigMib snmp4jConfigMIB;
  protected Snmp4jProxyMib snmp4jProxyMIB;
  protected NotificationLogMib notificationLogMIB;
  protected UnsignedInteger32 notificationLogDefaultLimit =
      new UnsignedInteger32(100);
  protected UnsignedInteger32 notificaitonLogGlobalLimit =
      new UnsignedInteger32(1000);
  protected UnsignedInteger32 notificaitonLogGlobalAge =
      new UnsignedInteger32(0);

  protected MOFactory moFactory = DefaultMOFactory.getInstance();

  protected OctetString sysDescr =
      new OctetString("SNMP4J-Agent "+
                      VersionInfo.getVersion()+" [" +
                      org.snmp4j.version.VersionInfo.getVersion()+"]"+
                      " - "+System.getProperty("os.name","")+
                      " - "+System.getProperty("os.arch")+
                      " - "+System.getProperty("os.version"));
  protected OID sysOID = new OID("1.3.6.1.4.1.4976.10");
  protected Integer32 sysServices = new Integer32(72);

  protected OctetString defaultContext;

  protected AgentState runState = new AgentState();

  protected MOTableSizeLimit<MOTableRow> tableSizeLimit;

  /**
   * Creates a SNMP agent configuration which can be run by calling
   * {@link #run()} later.
   *
   * @param agentsOwnEngineID
   *    the authoritative engine ID of the agent.
   * @param messageDispatcher
   *    the MessageDispatcher to use. The message dispatcher must be configured
   *    outside, i.e. transport mappings have to be added before this
   *    constructor is being called.
   * @param vacm
   *    a view access control model. Typically, this parameter is set to
   *    <code>null</code> to use the default VACM associated with the
   *    <code>VacmMIB</code>.
   * @param moServers
   *    the managed object server(s) that serve the managed objects available
   *    to this agent.
   * @param workerPool
   *    the <code>WorkerPool</code> to be used to process incoming request.
   * @param configurationFactory
   *    a <code>MOInputFactory</code> that creates a <code>MOInput</code> stream
   *    with containing serialized ManagedObject information with the agent's
   *    configuration or <code>null</code> otherwise.
   * @param persistenceProvider
   *    the primary <code>MOPersistenceProvider</code> to be used to load
   *    and store persistent MOs.
   * @param engineBootsProvider
   *    the provider of engine boots counter.
   */
  public AgentConfigManager(OctetString agentsOwnEngineID,
                            MessageDispatcher messageDispatcher,
                            VACM vacm,
                            MOServer[] moServers,
                            WorkerPool workerPool,
                            MOInputFactory configurationFactory,
                            MOPersistenceProvider persistenceProvider,
                            EngineBootsProvider engineBootsProvider) {
    this.engineID = agentsOwnEngineID;
    this.dispatcher = messageDispatcher;
    this.vacm = vacm;
    this.servers = moServers;
    this.workerPool = workerPool;
    this.configuration = configurationFactory;
    this.engineBootsProvider = engineBootsProvider;
    this.persistenceProvider = persistenceProvider;
  }

  /**
   * Creates a SNMP agent configuration which can be run by calling
   * {@link #run()} later.
   *
   * @param agentsOwnEngineID
   *    the authoritative engine ID of the agent.
   * @param messageDispatcher
   *    the MessageDispatcher to use. The message dispatcher must be configured
   *    outside, i.e. transport mappings have to be added before this
   *    constructor is being called.
   * @param vacm
   *    a view access control model. Typically, this parameter is set to
   *    <code>null</code> to use the default VACM associated with the
   *    <code>VacmMIB</code>.
   * @param moServers
   *    the managed object server(s) that serve the managed objects available
   *    to this agent.
   * @param workerPool
   *    the <code>WorkerPool</code> to be used to process incoming request.
   * @param configurationFactory
   *    a <code>MOInputFactory</code> that creates a <code>MOInput</code> stream
   *    with containing serialized ManagedObject information with the agent's
   *    configuration or <code>null</code> otherwise.
   * @param persistenceProvider
   *    the primary <code>MOPersistenceProvider</code> to be used to load
   *    and store persistent MOs.
   * @param engineBootsProvider
   *    the provider of engine boots counter.
   * @param moFactory
   *    the {@link MOFactory} to be used to create {@link ManagedObject}s
   *    created by this config manager. If <code>null</code> the
   *    {@link DefaultMOFactory} will be used.
   * @since 1.4
   */
  public AgentConfigManager(OctetString agentsOwnEngineID,
                            MessageDispatcher messageDispatcher,
                            VACM vacm,
                            MOServer[] moServers,
                            WorkerPool workerPool,
                            MOInputFactory configurationFactory,
                            MOPersistenceProvider persistenceProvider,
                            EngineBootsProvider engineBootsProvider,
                            MOFactory moFactory) {
    this(agentsOwnEngineID, messageDispatcher,vacm, moServers,
         workerPool, configurationFactory,
         persistenceProvider, engineBootsProvider);
    this.moFactory = (moFactory == null) ? this.moFactory : moFactory;
  }

  /**
   * Initializes, configures, restores agent state, and then launches the
   * SNMP agent depending on its current run state. For example, if
   * {@link #initialize()} has not yet been called it will be called before
   * the agent is being configured in the next step.
   * <p>
   * See also {@link #initialize()}, {@link #configure()},
   * {@link #restoreState()}, and {@link #launch()}.
   */
  public void run() {
    if (runState.getState() < STATE_INITIALIZED) {
      initialize();
    }
    if (runState.getState() < STATE_CONFIGURED) {
      configure();
    }
    if (runState.getState() < STATE_RESTORED) {
      restoreState();
    }
    if (runState.getState() < STATE_RUNNING) {
      launch();
    }
  }

  /**
   * Returns the state of the agent.
   * @return
   *    a integer constant from {@link #STATE_CREATED} thru
   *    {@link #STATE_RUNNING}.
   */
  public int getState() {
    return runState.getState();
  }

  /**
   * Returns the VACM used by this agent config manager.
   * @return
   *    the VACM instance of this agent.
   * @since 1.4
   */
  public VACM getVACM() {
    return vacm;
  }

  /**
   * Returns the SNMPv2-MIB implementation used by this config manager.
   * @return
   *    the SNMPv2MIB instance of this agent.
   * @since 1.4
   */
  public SNMPv2MIB getSNMPv2MIB() {
    return snmpv2MIB;
  }

  /**
   * Returns the SNMP-TARGET-MIB implementation used by this config manager.
   * @return
   *   the SnmpTargetMIB instance of this agent.
   * @since 1.4
   */
  public SnmpTargetMIB getSnmpTargetMIB() {
    return targetMIB;
  }

  /**
   * Returns the SNMP-NOTIFICATION-MIB implementation used by this config manager.
   * @return
   *   the SnmpNotificationMIB instance of this agent.
   * @since 1.4
   */
  public SnmpNotificationMIB getSnmpNotificationMIB() {
    return notificationMIB;
  }

  /**
   * Returns the SNMP-COMMUNITY-MIB implementation used by this config manager.
   * @return
   *   the SnmpCommunityMIB instance of this agent.
   * @since 1.4
   */
  public SnmpCommunityMIB getSnmpCommunityMIB() {
    return communityMIB;
  }

  /**
   * Returns the NOTIFICATION-LOG-MIB implementation used by this config
   * manager.
   * @return
   *    the NotificationLogMib instance of this agent.
   * @since 1.4.2
   */
  public NotificationLogMib getNotificationLogMIB() {
    return notificationLogMIB;
  }

  /**
   * Returns the SNMP4J-LOG-MIB implementation used by this config
   * manager.
   * @return
   *    the Snmp4jLogMib instance of this agent.
   * @since 1.4.2
   */
  public Snmp4jLogMib getSnmp4jLogMIB() {
    return snmp4jLogMIB;
  }

  /**
   * Returns the SNMP4J-CONFIG-MIB implementation used by this config
   * manager.
   * @return
   *    the Snmp4jConfigMib instance of this agent.
   * @since 1.4.2
   */
  public Snmp4jConfigMib getSnmp4jConfigMIB() {
    return snmp4jConfigMIB;
  }

  /**
   * Returns the SNMP4J-CONFIG-MIB implementation used by this config
   * manager.
   * @return
   *    the Snmp4jConfigMib instance of this agent.
   * @since 2.0
   */
  public Snmp4jProxyMib getSnmp4jProxyMIB() {
    return snmp4jProxyMIB;
  }

  /**
   * Launch the agent by registering and lauching (i.e., set to listen mode)
   * transport mappings.
   */
  protected void launch() {
    if (tableSizeLimit != null) {
      for (MOServer server : servers) {
        DefaultMOServer.unregisterTableRowListener(server, tableSizeLimit);
        DefaultMOServer.registerTableRowListener(server, tableSizeLimit);
      }
    }
    dispatcher.removeCommandResponder(agent);
    dispatcher.addCommandResponder(agent);
    registerTransportMappings();
    try {
      launchTransportMappings();
    }
    catch (IOException ex) {
      String txt =
          "Could not put all transport mappings in listen mode: "+
          ex.getMessage();
      logger.error(txt, ex);
      runState.addError(new ErrorDescriptor(txt, runState.getState(),
                                            STATE_RUNNING, ex));
    }
    runState.advanceState(STATE_RUNNING);
    fireLaunchNotifications();
  }

  /**
   * Fire notifications after agent start, i.e. sending a coldStart trap.
   */
  protected void fireLaunchNotifications() {
    if (notificationOriginator != null) {
      notificationOriginator.notify(new OctetString(), SnmpConstants.coldStart,
                                    new VariableBinding[0]);
    }
  }

  /**
   * Continues processing of SNMP requests by coupling message dispatcher and
   * agent. To succeed, the current state of the agent must be
   * {@link #STATE_SUSPENDED}.
   *
   * @return
   *    <code>true</code> if the running state could be restored,
   *    <code>false</code> otherwise.
   */
  public boolean continueProcessing() {
    if (runState.getState() == STATE_SUSPENDED) {
      dispatcher.removeCommandResponder(agent);
      dispatcher.addCommandResponder(agent);
      runState.setState(STATE_RUNNING);
      return true;
    }
    return false;
  }

  /**
   * Suspends processing of SNMP requests. This call decouples message
   * dispatcher and agent. All transport mappings remain unchanged and thus
   * all ports remain opened.
   */
  public void suspendProcessing() {
    dispatcher.removeCommandResponder(agent);
    runState.setState(STATE_SUSPENDED);
  }

  /**
   * Shutdown the agent by closing the internal SNMP session - including the
   * transport mappings provided through the configured
   * {@link MessageDispatcher} and then store the agent state to persistent
   * storage (if available).
   */
  public void shutdown() {
    logger.info("Shutdown agent: suspending request processing");
    suspendProcessing();
    try {
      if (dispatcher != null) {
        logger.info("Shutdown agent: closing transport mappings");
        stopTransportMappings(dispatcher.getTransportMappings());
      }
      if (session != null) {
        logger.info("Shutdown agent: closing session");
        session.close();
        session = null;
      }
    }
    catch (IOException ex) {
      logger.warn("Failed to close SNMP session: "+ex.getMessage());
    }
    logger.info("Shutdown agent: saving state");
    saveState();
    if (tableSizeLimit != null) {
      for (MOServer server : servers) {
        DefaultMOServer.unregisterTableRowListener(server, tableSizeLimit);
      }
    }
    logger.info("Shutdown agent: unregistering MIB objects");
    unregisterMIBs(null);
    runState.setState(STATE_SHUTDOWN);
    logger.info("Shutdown agent: finished");
  }

  /**
   * Registers a shutdown hook <code>Thread</code> at the {@link Runtime}
   * instance.
   */
  public void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        shutdown();
      }
    });
  }

  public void initSnmp4jLogMIB() {
    snmp4jLogMIB = new Snmp4jLogMib();
  }

  public void initSnmp4jConfigMIB(MOPersistenceProvider[] persistenceProvider) {
    snmp4jConfigMIB = new Snmp4jConfigMib(snmpv2MIB.getSysUpTime());
    snmp4jConfigMIB.setSnmpCommunityMIB(communityMIB);
    if (this.persistenceProvider != null) {
      snmp4jConfigMIB.setPrimaryProvider(this.persistenceProvider);
    }
    if (persistenceProvider != null) {
      for (MOPersistenceProvider aPersistenceProvider : persistenceProvider) {
        if (aPersistenceProvider != this.persistenceProvider) {
          snmp4jConfigMIB.addPersistenceProvider(aPersistenceProvider);
        }
      }
    }
  }

  public void initSnmp4jProxyMIB(OctetString context) {
    MOServer server = agent.getServer(context);
    snmp4jProxyMIB = new Snmp4jProxyMib(moFactory, session, server, targetMIB);
  }

  public VacmMIB getVacmMIB() {
    return vacmMIB;
  }

  public USM getUsm() {
    return usm;
  }

  public UsmMIB getUsmMIB() {
    return usmMIB;
  }

  protected void initNotificationLogMIB(VACM vacm,
                                        SnmpNotificationMIB notifyMIB) {
    notificationLogMIB = new NotificationLogMib(moFactory, vacm, notifyMIB);
    // init default log
    NlmConfigLogEntryRow row = notificationLogMIB.getNlmConfigLogEntry().
        createRow(new OID(new int[] { 0 }), new Variable[] {
      new OctetString(),
      notificationLogDefaultLimit,
      new Integer32(NotificationLogMib.NlmConfigLogAdminStatusEnum.enabled),
      new Integer32(),
      new Integer32(StorageType.permanent),
      new Integer32(RowStatus.active)
    });
    notificationLogMIB.getNlmConfigLogEntry().addRow(row);
    notificationLogMIB.getNlmConfigGlobalAgeOut().setValue(notificaitonLogGlobalAge);
    notificationLogMIB.getNlmConfigGlobalEntryLimit().setValue(notificaitonLogGlobalLimit);
    if (notificationOriginator instanceof NotificationOriginatorImpl) {
      ((NotificationOriginatorImpl)notificationOriginator).removeNotificationLogListener(notificationLogMIB);
      ((NotificationOriginatorImpl)notificationOriginator).addNotificationLogListener(notificationLogMIB);
    }
  }

  protected void initSecurityModels(EngineBootsProvider engineBootsProvider) {
    usm = createUSM();
    if (usm != null) {
      SecurityModels.getInstance().addSecurityModel(usm);
    }
    TSM tsm = createTSM();
    if (tsm != null) {
      SecurityModels.getInstance().addSecurityModel(tsm);
    }
    frameworkMIB = new SnmpFrameworkMIB(usm, dispatcher.getTransportMappings());
  }

  protected void initMessageDispatcherWithMPs(MessageDispatcher mp) {
    mp.addMessageProcessingModel(new MPv1());
    mp.addMessageProcessingModel(new MPv2c());
    MPv3 mpv3 = new MPv3(agent.getContextEngineID().getValue());
    mp.addMessageProcessingModel(mpv3);
  }

  protected void registerTransportMappings() {
    ArrayList<TransportMapping> l = new ArrayList<TransportMapping>(dispatcher.getTransportMappings());
    for (TransportMapping tm : l) {
      tm.removeTransportListener(dispatcher);
      tm.addTransportListener(dispatcher);
      if (tm instanceof TLSTM) {
        ((TLSTM)tm).setSecurityCallback(tlsTmMib);
      }
    }
  }

  protected void launchTransportMappings() throws IOException {
    launchTransportMappings(dispatcher.getTransportMappings());
  }

  /**
   * Puts a list of transport mappings into listen mode.
   * @param transportMappings
   *    a list of {@link TransportMapping} instances.
   * @throws IOException
   *    if a transport cannot listen to incoming messages.
   */
  protected static void launchTransportMappings(Collection<? extends TransportMapping> transportMappings)
      throws IOException
  {
    ArrayList<? extends TransportMapping> l = new ArrayList<TransportMapping>(transportMappings);
    for (TransportMapping tm : l) {
      if (!tm.isListening()) {
        tm.listen();
      }
    }
  }

  /**
   * Closes a list of transport mappings.
   * @param transportMappings
   *    a list of {@link TransportMapping} instances.
   * @throws IOException
   *    if a transport cannot be closed.
   */
  protected static void stopTransportMappings(Collection<? extends TransportMapping> transportMappings)
      throws IOException
  {
    ArrayList<TransportMapping> l = new ArrayList<TransportMapping>(transportMappings);
    for (TransportMapping tm : l) {
      tm.close();
    }
  }


  /**
   * Save the state of the agent persistently - if necessary persistent
   * storage is available.
   */
  public void saveState() {
    if (persistenceProvider != null) {
      try {
        persistenceProvider.store(persistenceProvider.getDefaultURI());
        runState.advanceState(STATE_SAVED);
      }
      catch (IOException ex) {
        String txt = "Failed to save agent state: "+ex.getMessage();
        logger.error(txt, ex);
        runState.addError(new ErrorDescriptor(txt, runState.getState(),
                                              STATE_SAVED, ex));
      }
    }
  }

  /**
   * Restore a previously persistently saved state - if available.
   * @return
   *    <code>true</code> if the agent state could be restored successfully,
   *    <code>false</code> otherwise.
   */
  public boolean restoreState() {
    if (persistenceProvider != null) {
      try {
        persistenceProvider.restore(persistenceProvider.getDefaultURI(),
                                    persistenceImportMode);
        runState.advanceState(STATE_RESTORED);
        return true;
      }
      catch (FileNotFoundException fnf) {
        String txt = "Saved agent state not found: "+fnf.getMessage();
        logger.warn(txt);
      }
      catch (IOException ex) {
        String txt = "Failed to load agent state: "+ex.getMessage();
        logger.error(txt, ex);
        runState.addError(new ErrorDescriptor(txt, runState.getState(),
                                              STATE_RESTORED, ex));
      }
    }
    return false;
  }

  /**
   * Configures components and managed objects.
   */
  public void configure() {
    if (configuration != null) {
      MOInput config = configuration.createMOInput();
      if (config == null) {
        logger.debug("No configuration returned by configuration factory "+
                     configuration);
        return;
      }
      MOServerPersistence serverPersistence = new MOServerPersistence(servers);
      try {
        serverPersistence.loadData(config);
      }
      catch (IOException ex) {
        String txt = "Failed to load agent configuration: "+ex.getMessage();
        logger.error(txt, ex);
        runState.addError(new ErrorDescriptor(txt, runState.getState(),
                                              STATE_CONFIGURED, ex));
        throw new RuntimeException(txt, ex);
      }
      finally {
        try {
          config.close();
        }
        catch (IOException ex1) {
          logger.warn("Failed to close config input stream: "+ex1.getMessage());
        }
      }
    }
    runState.advanceState(STATE_CONFIGURED);
  }

  protected void initMandatoryMIBs() {
    targetMIB = new SnmpTargetMIB(dispatcher);
    targetMIB.addDefaultTDomains();
    snmpv2MIB = new SNMPv2MIB(getSysDescr(), getSysOID(), getSysServices());
    notificationMIB = new SnmpNotificationMIB();
    vacmMIB = new VacmMIB(servers);
    usmMIB = new UsmMIB(usm, getSupportedSecurityProtocols());
    usm.addUsmUserListener(usmMIB);
    communityMIB = new SnmpCommunityMIB(targetMIB);
    tlsTmMib = new SnmpTlsTmMib(moFactory);
  }

  protected void linkCounterListener() {
    agent.removeCounterListener(snmpv2MIB);
    agent.addCounterListener(snmpv2MIB);
    usm.getCounterSupport().removeCounterListener(snmpv2MIB);
    usm.getCounterSupport().addCounterListener(snmpv2MIB);
    for (TransportMapping tm : dispatcher.getTransportMappings()) {
      if (tm instanceof TLSTM) {
        TLSTM tlstm = (TLSTM)tm;
        tlstm.getCounterSupport().removeCounterListener(tlsTmMib.getCounterListener());
        tlstm.getCounterSupport().addCounterListener(tlsTmMib.getCounterListener());
      }
    }
  }

  /**
   * Gets the set of security protocols supported by this agent configuration.
   *
   * @return
   *    {@link SecurityProtocols#getInstance()} by default after initialization
   *    by {@link SecurityProtocols#addDefaultProtocols()}.
   */
  protected SecurityProtocols getSupportedSecurityProtocols() {
    SecurityProtocols.getInstance().addDefaultProtocols();
    return SecurityProtocols.getInstance();
  }

  /**
   * Creates the USM used by this agent configuration.
   *
   * @return
   *    an USM initialized by the engine boots from the
   *    <code>engineBootsProvider</code> and <code>engineID</code>.
   */
  protected USM createUSM() {
    return new USM(getSupportedSecurityProtocols(), engineID,
                   engineBootsProvider.updateEngineBoots());
  }

  /**
   * Creates the TSM used by this agent configuration.
   *
   * @return
   *    an USM initialized by the engine boots from the
   *    <code>engineBootsProvider</code> and <code>engineID</code>.
   */
  protected TSM createTSM() {
    return new TSM(engineID, false);
  }

  /**
   * Gets the system services ID which can be modified by altering its value.
   *
   * @return
   *    72 by default.
   */
  public Integer32 getSysServices() {
    return sysServices;
  }

  /**
   * Gets the system OID which can be modified by altering its value.
   *
   * @return
   *    an OID - by default the SNMP4J root OID is returned.
   */
  public OID getSysOID() {
    return sysOID;
  }

  /**
   * Returns the sysDescr.0 value for this agent which can be modified by
   * altering its value.
   *
   * @return
   *    an OctetString describing the node of the form
   *    <pre>SNMP4J-Agent version [SNMP4J-version] -
   *         <os.name> - <os.arch> - <os.version></pre>.
   */
  public OctetString getSysDescr() {
    return sysDescr;
  }

  /**
   * Gets the sysUpTime.0 instance for the default context.
   *
   * @return
   *    a <code>SysUpTime</code> instance.
   */
  public SysUpTime getSysUpTime() {
    return snmpv2MIB.getSysUpTime();
  }

  /**
   * Returns the notification originator of this agent configuration.
   * To get the (multi-threaded) {@link NotificationOriginator} of the agent,
   * use {@link #getAgentNotificationOriginator} instead.
   * @return
   *    a <code>NotificationOriginator</code> instance.
   */
  public NotificationOriginator getNotificationOriginator() {
    return notificationOriginator;
  }

  /**
   * Returns the notification originator of the agent. Use this method to
   * get a {@link NotificationOriginator} for sending your notifications.
   * @return
   *    the <code>NotificationOriginator</code> instance.
   * @since 1.4
   */
  public NotificationOriginator getAgentNotificationOriginator() {
    return agent.getNotificationOriginator();
  }

  /**
   * Sets the notification originator of this agent configuration.
   * @param notificationOriginator
   *    a <code>NotificationOriginator</code> instance.
   */
  public void setNotificationOriginator(NotificationOriginator notificationOriginator) {
    this.notificationOriginator = notificationOriginator;
    if (agent != null) {
      agent.setNotificationOriginator(notificationOriginator);
    }
  }

  private VACM vacm() {
    if (vacm != null) {
      return vacm;
    }
    return vacmMIB;
  }

  public void initialize() {
    session = createSnmpSession(dispatcher);
    if (engineID == null) {
      engineID = new OctetString(MPv3.createLocalEngineID());
    }
    agent = createCommandProcessor(engineID);
    agent.setWorkerPool(workerPool);
    initSecurityModels(engineBootsProvider);
    initMessageDispatcherWithMPs(dispatcher);
    initMandatoryMIBs();
    linkCounterListener();
    // use VACM-MIB as VACM by default
    agent.setVacm(vacm());
    for (MOServer server : servers) {
      agent.addMOServer(server);
    }
    agent.setCoexistenceProvider(communityMIB);
    if (notificationOriginator == null) {
      notificationOriginator = createNotificationOriginator();
    }
    agent.setNotificationOriginator(notificationOriginator);
    // Use CommandProcessor instead notificationOriginator to send informs non
    // blocking.
    snmpv2MIB.setNotificationOriginator(agent);

    initOptionalMIBs();

    try {
      registerMIBs(getDefaultContext());
    }
    catch (DuplicateRegistrationException drex) {
      logger.error("Duplicate MO registration: "+drex.getMessage(), drex);
    }
    runState.advanceState(STATE_INITIALIZED);
  }

  /**
   * Sets the table size limits for the tables in this agent. If this method is
   * called while the agent's registration is being changed, a
   * <code>ConcurrentModificationException</code> might be thrown.
   * @param sizeLimits
   *    a set of properties as defined by {@link MOTableSizeLimit}.
   * @since 1.4
   */
  public void setTableSizeLimits(Properties sizeLimits) {
    if ((tableSizeLimit != null) && (servers != null)) {
      for (MOServer server : servers) {
        DefaultMOServer.unregisterTableRowListener(server, tableSizeLimit);
      }
    }
    tableSizeLimit = new MOTableSizeLimit<MOTableRow>(sizeLimits);
    if (getState() == STATE_RUNNING) {
      for (MOServer server : servers) {
        DefaultMOServer.registerTableRowListener(server, tableSizeLimit);
      }
    }
  }

  /**
   * Sets the table size limit for the tables in this agent. If this method is
   * called while the agent's registration is being changed, a
   * <code>ConcurrentModificationException</code> might be thrown.
   * @param sizeLimit
   *    the maximum size (numer of rows) of tables allowed for this agent.
   * @since 1.4
   */
  public void setTableSizeLimit(int sizeLimit) {
    if ((tableSizeLimit != null) && (servers != null)) {
      for (MOServer server : servers) {
        DefaultMOServer.unregisterTableRowListener(server, tableSizeLimit);
      }
    }
    tableSizeLimit = new MOTableSizeLimit<MOTableRow>(sizeLimit);
    if (getState() == STATE_RUNNING) {
      for (MOServer server : servers) {
        DefaultMOServer.registerTableRowListener(server, tableSizeLimit);
      }
    }
  }

  protected void initOptionalMIBs() {
    initSnmp4jLogMIB();
    initSnmp4jConfigMIB(null);
    if ((vacm() != null) && (notificationMIB != null)) {
      initNotificationLogMIB(vacm(), notificationMIB);
    }
    initSnmp4jProxyMIB(getDefaultContext());
  }

  /**
   * Returns the default context - which is the context that is used by the
   * base agent to register its MIB objects. By default it is <code>null</code>
   * which causes the objects to be registered virtually for all contexts.
   * In that case, subagents for example my not register their own objects
   * under the same subtree(s) in any context. To allow subagents to register
   * their own instances of those MIB modules, an empty <code>OctetString</code>
   * should be used as default context instead.
   * @return
   *    <code>null</code> or an <code>OctetString</code> (normally the empty
   *    string) denoting the context used for registering default MIBs.
   */
  public OctetString getDefaultContext() {
    return defaultContext;
  }

  /**
   * This method can be overwritten by a subagent to specify the contexts
   * each MIB module (group) will be registered to.
   *
   * @param mibGroup
   *    a group of {@link ManagedObject}s (i.e., a MIB module).
   * @param defaultContext
   *    the context to be used by default (i.e., the <code>null</code> context)
   * @return
   *    the context for which the module should be registered.
   */
  protected OctetString getContext(MOGroup mibGroup,
                                   OctetString defaultContext) {
    return defaultContext;
  }


  /**
   * Register the initialized MIB modules in the specified context of the agent.
   * @param context
   *    the context to register the internal MIB modules. This should be
   *    <code>null</code> by default.
   * @throws DuplicateRegistrationException if some of the MIB modules
   * registration regions conflict with already registered regions.
   */
  protected void registerMIBs(OctetString context) throws
      DuplicateRegistrationException
  {
    MOServer server = agent.getServer(context);
    targetMIB.registerMOs(server, getContext(targetMIB, context));
    notificationMIB.registerMOs(server, getContext(notificationMIB, context));
    vacmMIB.registerMOs(server, getContext(vacmMIB, context));
    usmMIB.registerMOs(server, getContext(usmMIB, context));
    snmpv2MIB.registerMOs(server, getContext(snmpv2MIB, context));
    frameworkMIB.registerMOs(server, getContext(frameworkMIB, context));
    communityMIB.registerMOs(server, getContext(communityMIB, context));
    if (snmp4jLogMIB != null) {
      snmp4jLogMIB.registerMOs(server, getContext(snmp4jLogMIB, context));
    }
    if (snmp4jConfigMIB != null) {
      snmp4jConfigMIB.registerMOs(server, getContext(snmp4jConfigMIB, context));
    }
    if (snmp4jProxyMIB != null) {
      snmp4jProxyMIB.registerMOs(server, getContext(snmp4jProxyMIB, context));
    }
    if (proxyMIB != null) {
      proxyMIB.registerMOs(server, getContext(proxyMIB, context));
    }
    if (notificationLogMIB != null) {
      notificationLogMIB.registerMOs(server, getContext(notificationLogMIB, context));
    }
    if (tlsTmMib != null) {
      tlsTmMib.registerMOs(server, getContext(tlsTmMib, context));
    }
  }

  /**
   * Unregister the initialized MIB modules from the default context of the
   * agent.
   * @param context
   *    the context where the MIB modules have been previously registered.
   */
  protected void unregisterMIBs(OctetString context) {
    MOServer server = agent.getServer(context);
    targetMIB.unregisterMOs(server, getContext(targetMIB, context));
    notificationMIB.unregisterMOs(server, getContext(notificationMIB, context));
    vacmMIB.unregisterMOs(server, getContext(vacmMIB, context));
    usmMIB.unregisterMOs(server, getContext(usmMIB, context));
    snmpv2MIB.unregisterMOs(server, getContext(snmpv2MIB, context));
    frameworkMIB.unregisterMOs(server, getContext(frameworkMIB, context));
    communityMIB.unregisterMOs(server, getContext(communityMIB, context));
    if (snmp4jLogMIB != null) {
      snmp4jLogMIB.unregisterMOs(server, getContext(snmp4jLogMIB, context));
    }
    if (snmp4jConfigMIB != null) {
      snmp4jConfigMIB.unregisterMOs(server, getContext(targetMIB, context));
    }
    if (proxyMIB != null) {
      proxyMIB.unregisterMOs(server, getContext(proxyMIB, context));
    }
    if (notificationLogMIB != null) {
      notificationLogMIB.unregisterMOs(server, getContext(notificationLogMIB, context));
    }
    if (tlsTmMib != null) {
      tlsTmMib.unregisterMOs(server, getContext(tlsTmMib, context));
    }
  }

  public void setupProxyForwarder() {
    proxyForwarder = createProxyForwarder(agent);
  }

  protected NotificationOriginator createNotificationOriginator() {
    return new NotificationOriginatorImpl(session, vacm(),
                                          snmpv2MIB.getSysUpTime(),
                                          targetMIB, notificationMIB);
  }

  /**
   * Creates and registers the default proxy forwarder application
   * ({@link ProxyForwarderImpl}).
   * @param agent
   *    the command processor that uses the proxy forwarder.
   * @return
   *    a ProxyForwarder instance.
   */
  protected ProxyForwarder createProxyForwarder(CommandProcessor agent) {
    proxyMIB = new SnmpProxyMIB();
    ProxyForwarderImpl pf =
        new ProxyForwarderImpl(session, proxyMIB, targetMIB);
    agent.addProxyForwarder(pf, null, ProxyForwarder.PROXY_TYPE_ALL);
    pf.addCounterListener(snmpv2MIB);
    return proxyForwarder;
  }


  /**
   * Creates the command processor.
   *
   * @param engineID
   *    the engine ID of the agent.
   * @return
   *    a new CommandProcessor instance.
   */
  protected CommandProcessor createCommandProcessor(OctetString engineID) {
    return new CommandProcessor(engineID);
  }

  /**
   * Creates the SNMP session to be used for this agent.
   *
   * @param dispatcher
   *    the message dispatcher to be associated with the session.
   * @return
   *    a SNMP session (a {@link Snmp} instance by default).
   */
  protected Session createSnmpSession(MessageDispatcher dispatcher) {
    return new Snmp(dispatcher);
  }

  /**
   * Sets the import mode for the {@link MOPersistenceProvider}.
   * @param importMode
   *    one of the import modes defined by {@link ImportModes}.
   * @since 1.4
   */
  public void setPersistenceImportMode(int importMode) {
    this.persistenceImportMode = importMode;
  }

  /**
   * Returns the currently active import mode for the
   * {@link MOPersistenceProvider}.
   * @return
   *    one of the import modes defined by {@link ImportModes}.
   * @since 1.4
   */
  public int getPersistenceImportMode() {
    return persistenceImportMode;
  }

  public class AgentState {
    private int state = STATE_CREATED;
    /**
     * Contains a list of ErrorDescription objects describing errors occured
     * since agent launched for the first time.
     */
    private List<ErrorDescriptor> errorsOccured = new LinkedList<ErrorDescriptor>();

    public int getState() {
      return state;
    }

    void setState(int newState) {
      this.state = newState;
      logger.info("Agent state set to "+newState);
    }

    void advanceState(int newState) {
      if (state < newState) {
        state = newState;
        logger.info("Agent state advanced to "+newState);
      }
    }

    void addError(ErrorDescriptor error) {
      errorsOccured.add(error);
    }

    public List<ErrorDescriptor> getErrors() {
      return new ArrayList<ErrorDescriptor>(errorsOccured);
    }
  }

  static class ErrorDescriptor {
    private Exception exception;
    private int sourceState;
    private int targetState;
    private String description;

    ErrorDescriptor(String descr, int sourceState, int targetState,
                    Exception ex) {
      this.description = descr;
      this.sourceState = sourceState;
      this.targetState = targetState;
      this.exception = ex;
    }

    public String getDescription() {
      return description;
    }

    public int getSourceState() {
      return sourceState;
    }

    public int getTargetState() {
      return targetState;
    }

    public Exception getException() {
      return exception;
    }
  }
}
