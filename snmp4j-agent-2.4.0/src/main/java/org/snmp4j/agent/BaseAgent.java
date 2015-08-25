/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - BaseAgent.java  
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

import org.snmp4j.*;
import org.snmp4j.agent.io.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp4j.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.*;
import java.net.URI;

/**
 * The <code>BaseAgent</code> abstract class defines a framework for writing
 * SNMP agents using the SNMP4J-Agent API. To implement your own SNMP agent,
 * extend this class and implement the abstract methods defined by BaseAgent.
 * The hook methods do not need any specific implementation. They only provide
 * a defined mechanism to customize your agent.
 *
 * @author Frank Fock
 * @version 1.0
 */
public abstract class BaseAgent implements Runnable {

  private static final LogAdapter logger =
      LogFactory.getLogger(BaseAgent.class);

  public static final int STATE_CREATED = 0;
  public static final int STATE_INIT_STARTED = 10;
  public static final int STATE_INIT_FINISHED = 20;
  public static final int STATE_RUNNING = 40;
  public static final int STATE_STOPPED = 30;

  protected SNMPv2MIB snmpv2MIB;
  protected SnmpFrameworkMIB snmpFrameworkMIB;
  protected SnmpTargetMIB snmpTargetMIB;
  protected SnmpNotificationMIB snmpNotificationMIB;
  protected SnmpProxyMIB snmpProxyMIB;
  protected SnmpCommunityMIB snmpCommunityMIB;
  protected Snmp4jLogMib snmp4jLogMIB;
  protected Snmp4jConfigMib snmp4jConfigMIB;
  protected UsmMIB usmMIB;
  protected VacmMIB vacmMIB;
  protected DefaultMOServer server;
  protected Snmp session;
  protected TransportMapping[] transportMappings;
  protected MessageDispatcherImpl dispatcher;
  protected CommandProcessor agent;

  protected MPv3 mpv3;
  protected USM usm;

  protected File bootCounterFile;
  protected NotificationOriginator notificationOriginator;
  protected ProxyForwarder defaultProxyForwarder;

  protected OctetString sysDescr =
      new OctetString("SNMP4J-Agent - "+
                      System.getProperty("os.name","")+
                      " - "+System.getProperty("os.arch")+
                      " - "+System.getProperty("os.version"));
  protected OID sysOID = new OID("1.3.6.1.4.1.4976");
  protected Integer32 sysServices = new Integer32(10);

  protected int agentState = STATE_CREATED;

  protected OctetString defaultContext;

  protected DefaultMOPersistenceProvider defaultPersistenceProvider;
  protected String configFileURI;

  /**
   * Creates a base agent with a {@link DefaultMOServer} as {@link MOServer}.
   * To use a different server implementation, modify the {@link #server} member
   * after construction.
   * @param configURI
   *    the URI of the config file holding persistent data for this agent. If
   *    persistent data is not used then set this parameter to
   *    <code>null</code>.
   */
  protected BaseAgent(String configURI) {
    this.configFileURI = configURI;
    this.server = new DefaultMOServer();
    this.defaultPersistenceProvider =
        new DefaultMOPersistenceProvider(new MOServer[] { this.server },
                                         configURI);
  }

  /**
   * Creates a base agent with boot-counter, config file, and a CommandProcessor
   * for processing SNMP requests.
   *
   * @param bootCounterFile
   *    a file with serialized boot-counter information (read/write). If the
   *    file does not exist it is created on shutdown of the agent.
   * @param configFile
   *    a file with serialized configuration information (read/write). If the
   *    file does not exist it is created on shutdown of the agent.
   * @param commandProcessor
   *    the <code>CommandProcessor</code> instance that handles the SNMP
   *    requests.
   */
  protected BaseAgent(File bootCounterFile,
                      File configFile,
                      CommandProcessor commandProcessor) {
    this((configFile == null) ? null : configFile.getPath());
    this.bootCounterFile = bootCounterFile;
    this.agent = commandProcessor;
  }

  /**
   * Initialize transport mappings, message dispatcher, basic MIB modules,
   * proxy forwarder, VACM and USM security, and custom MIB modules and objects
   * provided by sub-classes.
   *
   * @throws IOException
   *    if initialization fails because transport initialization fails.
   */
  public void init() throws IOException {
    agentState = STATE_INIT_STARTED;
    initTransportMappings();
    initMessageDispatcher();
    server.addContext(new OctetString());
    snmpv2MIB = new SNMPv2MIB(sysDescr, sysOID, sysServices);

    // register Snmp counters for updates
    dispatcher.addCounterListener(snmpv2MIB);
    agent.addCounterListener(snmpv2MIB);
    snmpFrameworkMIB =
        new SnmpFrameworkMIB((USM)
                             mpv3.getSecurityModel(SecurityModel.SECURITY_MODEL_USM),
                             dispatcher.getTransportMappings());
    usmMIB = new UsmMIB(usm, SecurityProtocols.getInstance());
    usm.addUsmUserListener(usmMIB);

    vacmMIB = new VacmMIB(new MOServer[] { server });
    snmpTargetMIB = new SnmpTargetMIB(dispatcher);
    snmpNotificationMIB = new SnmpNotificationMIB();
    snmpCommunityMIB = new SnmpCommunityMIB(snmpTargetMIB);
    initConfigMIB();
    snmpProxyMIB = new SnmpProxyMIB();
    notificationOriginator =
        new NotificationOriginatorImpl(session, vacmMIB,
                                       snmpv2MIB.getSysUpTime(),
                                       snmpTargetMIB, snmpNotificationMIB, snmpCommunityMIB);
    snmpv2MIB.setNotificationOriginator(agent);

    setupDefaultProxyForwarder();
    // add USM users
    addUsmUser(usm);
    // add SNMPv1/v2c community to SNMPv3 security name mappings
    addCommunities(snmpCommunityMIB);
    addViews(vacmMIB);
    addNotificationTargets(snmpTargetMIB, snmpNotificationMIB);

    registerSnmpMIBs();
  }

  protected void initConfigMIB() {
    snmp4jLogMIB = new Snmp4jLogMib();
    if ((configFileURI != null) && (defaultPersistenceProvider != null)) {
      snmp4jConfigMIB = new Snmp4jConfigMib(snmpv2MIB.getSysUpTime());
      snmp4jConfigMIB.setSnmpCommunityMIB(snmpCommunityMIB);
      snmp4jConfigMIB.setPrimaryProvider(defaultPersistenceProvider);
    }
  }

  /**
   * This method can be overwritten by a subagent to specify the contexts
   * each MIB module (group) will be registered to.
   *
   * @param mibGroup
   *   a group of {@link ManagedObject}s (i.e., a MIB module).
   * @return
   *   the context for which the module should be registered.
   * @since 1.1
   */
  protected OctetString getContext(MOGroup mibGroup) {
    return getDefaultContext();
  }

  /**
   * Register the basic MIB modules at the agent's <code>MOServer</code>.
   */
  protected void registerSnmpMIBs() {
    try {
      snmpTargetMIB.registerMOs(server, getContext(snmpTargetMIB));
      snmpNotificationMIB.registerMOs(server, getContext(snmpNotificationMIB));
      vacmMIB.registerMOs(server, getContext(vacmMIB));
      usmMIB.registerMOs(server, getContext(usmMIB));
      snmpv2MIB.registerMOs(server, getContext(snmpv2MIB));
      snmpFrameworkMIB.registerMOs(server, getContext(snmpFrameworkMIB));
      snmpCommunityMIB.registerMOs(server, getContext(snmpCommunityMIB));
      snmp4jLogMIB.registerMOs(server, getContext(snmp4jLogMIB));
      if (snmp4jConfigMIB != null) {
        snmp4jConfigMIB.registerMOs(server, getContext(snmp4jConfigMIB));
      }
      snmpProxyMIB.registerMOs(server, getContext(snmpProxyMIB));
      registerManagedObjects();
    }
    catch (DuplicateRegistrationException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Unregister the basic MIB modules from the agent's <code>MOServer</code>.
   */
  protected void unregisterSnmpMIBs() {
    snmpTargetMIB.unregisterMOs(server, getContext(snmpTargetMIB));
    snmpNotificationMIB.unregisterMOs(server, getContext(snmpNotificationMIB));
    vacmMIB.unregisterMOs(server, getContext(vacmMIB));
    usmMIB.unregisterMOs(server, getContext(usmMIB));
    snmpv2MIB.unregisterMOs(server, getContext(snmpv2MIB));
    snmpFrameworkMIB.unregisterMOs(server, getContext(snmpFrameworkMIB));
    snmpCommunityMIB.unregisterMOs(server, getContext(snmpCommunityMIB));
    snmp4jLogMIB.unregisterMOs(server, getContext(snmp4jLogMIB));
    if (snmp4jConfigMIB != null) {
      snmp4jConfigMIB.unregisterMOs(server, getContext(snmp4jConfigMIB));
    }
    snmpProxyMIB.unregisterMOs(server, getContext(snmpProxyMIB));
    unregisterManagedObjects();
  }

  /**
   * Register additional managed objects at the agent's server.
   */
  protected abstract void registerManagedObjects();

  /**
   * Unregister additional managed objects from the agent's server.
   */
  protected abstract void unregisterManagedObjects();

  /**
   * Creates and registers the default proxy forwarder application
   * ({@link ProxyForwarderImpl}).
   */
  protected void setupDefaultProxyForwarder() {
    defaultProxyForwarder = new ProxyForwarderImpl(session, snmpProxyMIB,
                                                   snmpTargetMIB);
    agent.addProxyForwarder(defaultProxyForwarder,
                            null, ProxyForwarder.PROXY_TYPE_ALL);
    ((ProxyForwarderImpl)defaultProxyForwarder).addCounterListener(snmpv2MIB);
  }

  /**
   * Loads the configuration using the specified import mode from the set
   * config file.
   * @param importMode
   *    one of the import modes defined by {@link ImportModes}.
   */
  public void loadConfig(int importMode) {
    try {
      defaultPersistenceProvider.restore(null, importMode);
    }
    catch (IOException ex) {
      logger.error(ex);
    }
  }

  /**
   * Save the current (serializable) managed object configuration into
   * the config file.
   */
  public void saveConfig() {
    try {
      defaultPersistenceProvider.store(configFileURI);
    }
    catch (IOException ex) {
      logger.error(ex);
    }
  }

  /**
   * Adds a shutdown hook that saves the internal config into the config file
   * when a SIGTERM (Ctrl-C) is terminating the agent.
   */
  protected void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        saveConfig();
      }
    });
  }

  /**
   * Finishes intialization of the agent by connecting server and command
   * processor, setting up USM, VACM, notification targets, and finally sending
   * a coldStart notification to configured targets.
   */
  protected void finishInit() {
    if (agentState < STATE_INIT_STARTED) {
      logger.fatal("Agent initialization finish is called before "+
                   "initialization, current state is "+agentState);
    }
    agent.setNotificationOriginator(notificationOriginator);
    agent.addMOServer(server);
    agent.setCoexistenceProvider(snmpCommunityMIB);
    agent.setVacm(vacmMIB);
    dispatcher.addCommandResponder(agent);
    agentState = STATE_INIT_FINISHED;
  }

  protected void sendColdStartNotification() {
    notificationOriginator.notify(new OctetString(), SnmpConstants.coldStart,
                                  new VariableBinding[0]);
  }

  /**
   * Starts the agent by let it listen on the configured SNMP agent ports
   * (transpot mappings).
   */
  public void run() {
    if (agentState < STATE_INIT_FINISHED) {
      logger.fatal("Agent is run uninitialized, current state is "+agentState);
    }
    if (agentState == STATE_STOPPED) {
      initSnmpSession();
    }
    try {
      session.listen();
      agentState = STATE_RUNNING;
    }
    catch (IOException iox) {
      logger.error(iox);
    }
  }

  /**
   * Stops the agent by closing the SNMP session and associated transport
   * mappings.
   * @since 1.1
   */
  public void stop() {
    if (agentState != STATE_RUNNING) {
      logger.error("Agent is stopped although it is not running, "+
                   "current state is "+agentState);
    }
    try {
      session.close();
    }
    catch (IOException ex) {
      logger.warn("Closing agent session threw IOException: "+ex.getMessage());
    }
    session = null;
    agentState = STATE_STOPPED;
  }

  /**
   * Initializes the message dispatcher ({@link MessageDispatcherImpl}) with
   * the transport mappings.
   */
  protected void initMessageDispatcher() {
    dispatcher = new MessageDispatcherImpl();
    mpv3 = new MPv3(agent.getContextEngineID().getValue());
    usm = new USM(SecurityProtocols.getInstance(),
                  agent.getContextEngineID(),
                  updateEngineBoots());
    SecurityModels.getInstance().addSecurityModel(usm);
    SecurityProtocols.getInstance().addDefaultProtocols();
    dispatcher.addMessageProcessingModel(new MPv1());
    dispatcher.addMessageProcessingModel(new MPv2c());
    dispatcher.addMessageProcessingModel(mpv3);
    initSnmpSession();
  }

  protected void initSnmpSession() {
    session = new Snmp(dispatcher);
    for (TransportMapping transportMapping : transportMappings) {
      try {
        session.addTransportMapping(transportMapping);
      }
      catch (Exception ex) {
        logger.warn("Failed to initialize transport mapping '" +
            transportMapping + "' with: " + ex.getMessage());
      }
    }
    updateSession(session);
  }

  /**
   * Updates all objects with a new session instance. This method must be
   * overwritten, if non-default SNMP MIB instances are created by a subclass.
   * @param session
   *    a SNMP Session instance.
   */
  protected void updateSession(Session session) {
    if (notificationOriginator instanceof NotificationOriginatorImpl) {
      ((NotificationOriginatorImpl)notificationOriginator).setSession(session);
    }
    if (defaultProxyForwarder instanceof ProxyForwarderImpl) {
      ((ProxyForwarderImpl)defaultProxyForwarder).setSession(session);
    }
  }

  /**
   * Updates the engine boots counter and returns the actual value.
   * @return
   *    the actual boots counter value.
   */
  protected int updateEngineBoots() {
    int boots = getEngineBoots();
    boots++;
    if (boots <= 0) {
      boots = 1;
    }
    setEngineBoots(boots);
    return boots;
  }

  /**
   * Reads the engine boots counter from the corresponding input stream (file).
   * @return
   *    the boots counter value read or zero if it could not be read.
   */
  protected int getEngineBoots() {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(bootCounterFile);
      ObjectInputStream ois = new ObjectInputStream(fis);
      int boots = ois.readInt();
      if (logger.isInfoEnabled()) {
        logger.info("Engine boots is: "+boots);
      }
      return boots;
    }
    catch (FileNotFoundException ex) {
      logger.warn("Could not find boot counter file: "+bootCounterFile);
    }
    catch (IOException iox) {
      if (logger.isDebugEnabled()) {
        iox.printStackTrace();
      }
      logger.error("Failed to read boot counter: "+iox.getMessage());
    }
    finally {
      if (fis != null) {
        try {
          fis.close();
        }
        catch (IOException ex1) {
          logger.warn(ex1);
        }
      }
    }
    return 0;
  }

  protected void setEngineBoots(int engineBoots) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(bootCounterFile);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeInt(engineBoots);
      oos.close();
      if (logger.isInfoEnabled()) {
        logger.info("Wrote boot counter: " + engineBoots);
      }
    }
    catch (FileNotFoundException fnfex) {
      logger.error("Boot counter configuration file not found: "+
                   fnfex.getMessage());
    }
    catch (IOException iox) {
      logger.error("Failed to write boot counter: "+iox.getMessage());
    }
    finally {
      if (fos != null) {
        try {
          fos.close();
        }
        catch (IOException ex1) {
          logger.warn(ex1);
        }
      }
    }
  }

  /**
   * Adds all the necessary initial users to the USM.
   * @param usm
   *    the USM instance used by this agent.
   */
  protected abstract void addUsmUser(USM usm);

  /**
   * Adds initial notification targets and filters.
   * @param targetMIB
   *    the SnmpTargetMIB holding the target configuration.
   * @param notificationMIB
   *    the SnmpNotificationMIB holding the notification (filter)
   *    configuration.
   */
  protected abstract void addNotificationTargets(SnmpTargetMIB targetMIB,
                                                 SnmpNotificationMIB notificationMIB);

  /**
   * Adds initial VACM configuration.
   * @param vacmMIB
   *    the VacmMIB holding the agent's view configuration.
   */
  protected abstract void addViews(VacmMIB vacmMIB);

  /**
   * Adds community to security name mappings needed for SNMPv1 and SNMPv2c.
   * @param communityMIB
   *    the SnmpCommunityMIB holding coexistence configuration for community
   *    based security models.
   */
  protected abstract void addCommunities(SnmpCommunityMIB communityMIB);

  /**
   * Initializes the transport mappings (ports) to be used by the agent.
   * @throws IOException
   */
  protected void initTransportMappings() throws IOException {
    transportMappings = new TransportMapping[1];
    transportMappings[0] =
        new DefaultUdpTransportMapping(new UdpAddress("0.0.0.0/161"));
  }

  public NotificationOriginator getNotificationOriginator() {
    return notificationOriginator;
  }

  public void setDefaultProxyForwarder(ProxyForwarder defaultProxyForwarder) {
    this.defaultProxyForwarder = defaultProxyForwarder;
  }

  public void setSysDescr(OctetString sysDescr) {
    this.sysDescr.setValue(sysDescr.getValue());
  }

  public void setSysOID(OID sysOID) {
    this.sysOID.setValue(sysOID.getValue());
  }

  public void setSysServices(Integer32 sysServices) {
    this.sysServices.setValue(sysServices.getValue());
  }

  public void setAgent(CommandProcessor agent) {
    this.agent = agent;
  }

  public void setBootCounterFile(File bootCounterFile) {
    this.bootCounterFile = bootCounterFile;
  }

  public void setConfigFile(File configFile) {
    this.configFileURI = configFile.getPath();
  }

  /**
   * Sets the default context for this base agent. By setting this value before
   * any MIB modules have been registered at the internal server, the context
   * for which the registration is performed can be changed. The default context
   * is <code>null</code> which causes MIB objects to be virtually registered
   * for all contexts.
   *
   * @param defaultContext
   *    the context for default MIB objects.
   * @since 1.1
   */
  public void setDefaultContext(OctetString defaultContext) {
    this.defaultContext = defaultContext;
  }

  public ProxyForwarder getDefaultProxyForwarder() {
    return defaultProxyForwarder;
  }

  public OctetString getSysDescr() {
    return sysDescr;
  }

  public OID getSysOID() {
    return sysOID;
  }

  public Integer32 getSysServices() {
    return sysServices;
  }

  public CommandProcessor getAgent() {
    return agent;
  }

  public File getBootCounterFile() {
    return bootCounterFile;
  }

  public File getConfigFile() {
    return new File(configFileURI);
  }

  public Snmp4jConfigMib getSnmp4jConfigMIB() {
    return snmp4jConfigMIB;
  }

  public Snmp4jLogMib getSnmp4jLogMIB() {
    return snmp4jLogMIB;
  }

  public SnmpCommunityMIB getSnmpCommunityMIB() {
    return snmpCommunityMIB;
  }

  public SnmpFrameworkMIB getSnmpFrameworkMIB() {
    return snmpFrameworkMIB;
  }

  public SnmpNotificationMIB getSnmpNotificationMIB() {
    return snmpNotificationMIB;
  }

  public SnmpProxyMIB getSnmpProxyMIB() {
    return snmpProxyMIB;
  }

  public SnmpTargetMIB getSnmpTargetMIB() {
    return snmpTargetMIB;
  }

  public SNMPv2MIB getSnmpv2MIB() {
    return snmpv2MIB;
  }

  public UsmMIB getUsmMIB() {
    return usmMIB;
  }

  public VacmMIB getVacmMIB() {
    return vacmMIB;
  }

  public Snmp getSession() {
    return session;
  }

  public DefaultMOServer getServer() {
    return server;
  }

  public MPv3 getMPv3() {
    return mpv3;
  }

  public USM getUsm() {
    return usm;
  }

  /**
   * Returns the agent's state.
   * @return
   *    one of the state's starting from {@link #STATE_CREATED} to
   *    {@link #STATE_RUNNING}.
   * @since 1.1
   */
  public int getAgentState() {
    return agentState;
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
   * @since 1.1
   */
  public OctetString getDefaultContext() {
    return defaultContext;
  }
}
