/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOServerPersistence.java  
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


package org.snmp4j.agent.io;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import java.io.IOException;
import java.util.LinkedHashMap;
import org.snmp4j.agent.ManagedObject;
import java.util.Iterator;
import org.snmp4j.agent.SerializableManagedObject;
import java.util.HashMap;
import java.util.Map.Entry;
import org.snmp4j.agent.MOScope;
import org.snmp4j.agent.MOContextScope;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.MOServer;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * The <code>MOServerPersistence</code> provides persistence operations
 * to load and save serialized MIB data.
 *
 * @author Frank Fock
 * @version 1.4
 */
public class MOServerPersistence {

  private static final LogAdapter logger =
      LogFactory.getLogger(MOServerPersistence.class);

  private MOServer[] servers;

  public MOServerPersistence(MOServer server) {
    this(new MOServer[] { server });
  }

  public MOServerPersistence(MOServer[] moServers) {
    this.servers = moServers;
  }

  private HashMap<OctetString, LinkedHashMap<OID, SerializableManagedObject>> buildCache() {
    HashMap<OctetString, LinkedHashMap<OID,  SerializableManagedObject>> serializableMO =
        new HashMap<OctetString, LinkedHashMap<OID,  SerializableManagedObject>>();
    serializableMO.clear();
    for (MOServer server : servers) {
      for (Iterator it = server.iterator(); it.hasNext();) {
        Entry entry = (Entry) it.next();
        MOScope scope = (MOScope) entry.getKey();
        ManagedObject value = (ManagedObject) entry.getValue();
        if ((value instanceof SerializableManagedObject) &&
            (!((SerializableManagedObject) value).isVolatile())) {
          OctetString context = null;
          if (scope instanceof MOContextScope) {
            context = ((MOContextScope) scope).getContext();
          }
          LinkedHashMap<OID, SerializableManagedObject> objects = serializableMO.get(context);
          if (objects == null) {
            objects = new LinkedHashMap<OID, SerializableManagedObject>();
            serializableMO.put(context, objects);
          }
          objects.put(((SerializableManagedObject) value).getID(), (SerializableManagedObject) value);
        }
      }
    }
    return serializableMO;
  }

  public synchronized void loadData(MOInput input) throws IOException {
    HashMap<OctetString, LinkedHashMap<OID, SerializableManagedObject>> serializableMO = buildCache();
    // load context independent data
    LinkedHashMap<OID, SerializableManagedObject> mos = serializableMO.get(null);
    if (mos != null) {
      readData(input, mos);
    }
    else {
      Sequence seq = input.readSequence();
      for (int i=0; i<seq.getSize(); i++) {
        MOInfo mo = input.readManagedObject();
        input.skipManagedObject(mo);
      }
    }
    // load contexts
    Sequence contextSequence = input.readSequence();
    if (contextSequence != null) {
      for (int i = 0; i < contextSequence.getSize(); i++) {
        Context context = input.readContext();
        boolean skip = true;
//      MOServer server = null;
        for (int s = 0; (skip) && (s < servers.length); s++) {
          if (servers[s].isContextSupported(context.getContext())) {
            skip = false;
//          server = servers[s];
          }
        }
        if (skip) {
          logger.warn("Context '" + context.getContext() +
                      "' is no longer supported by agent");
          input.skipContext(context);
          continue;
        }
        if (logger.isDebugEnabled()) {
          logger.debug("Loading data for context '" + context.getContext() +
                       "'");
        }
        mos = serializableMO.get(context.getContext());
        if (mos == null) {
          input.skipContext(context);
          continue;
        }
        readData(input, mos);
        input.skipContext(context);
      }
    }
  }

  private static void readData(MOInput input, LinkedHashMap<OID,SerializableManagedObject> mos)
      throws IOException
  {
    Sequence moGroup = input.readSequence();
    if (moGroup != null) {
      for (int j = 0; j < moGroup.getSize(); j++) {
        MOInfo moid = input.readManagedObject();
        if (logger.isDebugEnabled()) {
          logger.debug("Looking up object " + moid.getOID());
        }
        SerializableManagedObject mo = mos.get(moid.getOID());
        if (mo != null) {
          if (logger.isDebugEnabled()) {
            logger.debug("Loading data for object " + moid.getOID());
          }
          mo.load(input);
        }
        input.skipManagedObject(moid);
      }
    }
  }

  /**
   * Saves the serializable data of the associated servers to the given
   * {@link MOOutput}. This method can be called while the registrations
   * of the {@link MOServer}s are changed, because {@link MOServer#iterator()}
   * is synchronized and returns a copy tree of the registered objects.
   *
   * @param output
   *    a <code>MOOutput</code> instance to store the data.
   * @throws IOException
   *    if the output stream cannot be written.
   */
  public synchronized void saveData(MOOutput output) throws IOException {
    HashMap<OctetString, LinkedHashMap<OID, SerializableManagedObject>> serializableMO = buildCache();
    // write context independent data
    LinkedHashMap<? extends OID, SerializableManagedObject> mos = serializableMO.get(null);
    if (logger.isDebugEnabled()) {
      logger.debug("Trying to write MIB data for all contexts");
    }
    if (mos != null) {
      writeData(output, null, mos);
    }
    else {
      output.writeSequence(new Sequence(0));
    }
    Set<OctetString> contextSet = new HashSet<OctetString>();
    for (MOServer server : servers) {
      contextSet.addAll(Arrays.asList(server.getContexts()));
    }
    OctetString[] contexts = contextSet.toArray(new OctetString[contextSet.size()]);
    output.writeSequence(new Sequence(contexts.length));
    for (OctetString context : contexts) {
      Context c = new Context(context);
      output.writeContextBegin(c);
      if (logger.isDebugEnabled()) {
        logger.debug("Trying to write MIB data for context '" +
            c.getContext() + "'");
      }
      mos = serializableMO.get(c.getContext());
      if (mos != null) {
        writeData(output, c, mos);
      }
      output.writeContextEnd(c);
    }
  }

  private static void writeData(MOOutput output, Context c, LinkedHashMap<? extends OID, SerializableManagedObject> mos)
      throws IOException
  {
    if (logger.isDebugEnabled()) {
      if (c == null) {
        logger.debug("Writing " + mos.size() +
                     " context independent managed objects");
      }
      else {
        logger.debug("Writing " + mos.size() +
                     " managed objects for context '" +
                     c.getContext() + "'");
      }
    }
    output.writeSequence(new Sequence(mos.size()));
    for (SerializableManagedObject mo : mos.values()) {
      MOInfo moInfo = new MOInfo(mo.getID());
      output.writeManagedObjectBegin(moInfo);
      mo.save(output);
      output.writeManagedObjectEnd(moInfo);
    }
  }
}
