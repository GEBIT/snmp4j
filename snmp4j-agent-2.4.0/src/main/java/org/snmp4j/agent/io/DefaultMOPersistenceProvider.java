/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOPersistenceProvider.java  
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

import java.io.*;
import java.net.URI;
import org.snmp4j.agent.MOServer;

/**
 * The <code>DefaultMOPersistenceProvider</code> provides agent state
 * persistence using a file with {@link DefaultMOInput} and
 * {@link DefaultMOOutput} input/output.
 *
 * @author Frank Fock
 * @version 1.2
 */
public class DefaultMOPersistenceProvider implements MOPersistenceProvider {

  private MOServer[] server;
  private String defaultURI;

  /**
   * Creates a persistence provider for the supplied {@link MOServer} instances.
   * The content and state of the managed objects of those servers are subject
   * to {@link #store} and {@link #restore} operations.
   *
   * @param server
   *   an array of <code>MOServer</code> instances (possibly empty).
   * @param defaultURI
   *   the (optional) default URI (i.e., file path) to be used for this
   *   persistence provider.
   */
  public DefaultMOPersistenceProvider(MOServer[] server, String defaultURI) {
    this.server = server;
    this.defaultURI = defaultURI;
  }

  /**
   * Returns an unique ID of the persistence provider which should identify the
   * format and type of the persistence provider.
   *
   * @return "default".
   */
  public String getPersistenceProviderID() {
    return "default";
  }

  /**
   * Checks whether the supplied URI string is valid for this persistence
   * provider.
   *
   * @param uri a string identifying a persistent storage location for this
   *   storage provider.
   * @return <code>true</code> if the <code>uri</code> is valid,
   *   <code>false</code> otherwise.
   */
  public boolean isValidPersistenceURI(String uri) {
    try {
      File f = getFile(uri);
      return (f.isFile() && (f.canRead() || f.canWrite())) || !f.exists();
    }
    catch (Exception ex) {
      return false;
    }
  }

  private File getFile(String uri) {
    File f;
    if (uri.toUpperCase().startsWith("FILE:")) {
      URI u = URI.create(uri);
      f = new File(u);
    }
    else {
      f = new File(uri);
    }
    return f;
  }

  /**
   * Restore (load) agent state from the specified file URI or file name.
   *
   * @param uri a string pointing to the persistent storage file from which the
   *   agent state should be restored from. The format of he string is
   *   either a simple file name or an URI starting with "file:".
   * @param importMode specifies how the agent's current state should be
   *   update while restoring a previous state.
   * @throws IOException if the restore operation fails.
   */
  public void restore(String uri, int importMode) throws IOException {
    if (uri == null) {
      uri = getDefaultURI();
    }
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(new FileInputStream(getFile(uri)));
      DefaultMOInput is = new DefaultMOInput(ois);
      is.setOverwriteMode(importMode);
      MOServerPersistence p = new MOServerPersistence(server);
      p.loadData(is);
      ois.close();
    }
    catch (IOException iox) {
      throw iox;
    }
    finally {
      if (ois != null) {
        try { ois.close(); } catch (IOException iox) { }
      }
    }
  }

  /**
   * Stores the current agent state to persistent storage specified by the
   * supplied URI.
   *
   * @param uri a string pointing to the persistent storage file to use.
   *   The format of he string is either a simple file name or an URI
   *   starting with "file:".
   * @throws IOException if the store operation fails.
   */
  public void store(String uri) throws IOException {
    if (uri == null) {
      uri = getDefaultURI();
    }
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(getFile(uri)));
      DefaultMOOutput os = new DefaultMOOutput(oos);
      MOServerPersistence p = new MOServerPersistence(server);
      p.saveData(os);
      oos.flush();
      oos.close();
    }
    catch (IOException iox) {
      throw iox;
    }
    finally {
      if (oos != null) {
        try { oos.close(); } catch (IOException iox) { }
      }
    }
  }

  public String getDefaultURI() {
    return defaultURI;
  }
}
