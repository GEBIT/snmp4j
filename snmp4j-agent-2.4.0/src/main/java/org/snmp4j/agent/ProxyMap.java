/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ProxyMap.java  
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

import java.util.Map;
import org.snmp4j.smi.OctetString;
import java.util.TreeMap;

/**
 * The <code>ProxyMap</code> maps context engine IDs in conjunction with a
 * proxy usage type to a ProxyForwarder instance.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class ProxyMap {

  private Map<ProxyKey, ProxyForwarder> proxies = new TreeMap<ProxyKey, ProxyForwarder>();

  public ProxyMap() {
  }

  public ProxyForwarder add(ProxyForwarder proxyForwarder,
                            OctetString contextEngineID,
                            int proxyType) {
    return this.proxies.put(new ProxyKey(contextEngineID, proxyType),
                     proxyForwarder);
  }

  public ProxyForwarder remove(OctetString contextEngineID,
                               int proxyType) {
    return this.proxies.remove(new ProxyKey(contextEngineID, proxyType));
  }

  public ProxyForwarder get(OctetString contextEngineID,
                            int proxyType) {
    ProxyForwarder proxy = this.proxies.get(new ProxyKey(contextEngineID, proxyType));
    if (proxy == null) {
      proxy = this.proxies.get(new ProxyKey(null, proxyType));
    }
    return proxy;
  }

  static class ProxyKey implements Comparable {

    private OctetString contextEngineID;
    private int proxyType;

    ProxyKey(OctetString contextEngineID, int proxyType) {
      this.contextEngineID = contextEngineID;
      this.proxyType = proxyType;
    }

    public int compareTo(Object o) {
      ProxyKey other = (ProxyKey)o;
      if (((contextEngineID == null) && (other.contextEngineID != null)) ||
          ((other.contextEngineID == null) && (contextEngineID != null)) ||
          ((other.contextEngineID == null) && (contextEngineID == null)) ||
          (contextEngineID.equals(other.contextEngineID))) {
        if ((proxyType == ProxyForwarder.PROXY_TYPE_ALL) ||
            (other.proxyType == ProxyForwarder.PROXY_TYPE_ALL)) {
          return 0;
        }
        return (proxyType - other.proxyType);
      }
      return contextEngineID.compareTo(other.contextEngineID);
    }

  }
}
