/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - CoexistenceInfo.java  
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

import org.snmp4j.smi.*;

/**
 * A coexistence information object has attributes needed to map messages
 * between different versions of the SNMP protocol. A good portion of those
 * attributes are from the SNMP-COMMUNITY-MIB.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class CoexistenceInfo implements Comparable {

  private OctetString securityName;
  private OctetString contextEngineID;
  private OctetString contextName;
  private OctetString transportTag;
  private int maxMessageSize = Integer.MAX_VALUE;

  /**
   * Creates an context info object based on a security name, context engine ID,
   * and context name. The transport tag is not defined (= <code>null</code>).
   *
   * @param securityName
   *    a security name.
   * @param contextEngineID
   *    a context engine ID.
   * @param contextName
   *    a context name
   */
  public CoexistenceInfo(OctetString securityName,
                         OctetString contextEngineID,
                         OctetString contextName) {
    this.securityName = securityName;
    this.contextEngineID = contextEngineID;
    this.contextName = contextName;
  }

  /**
   * Creates an context info object based on a security name, context engine ID,
   * context name, and transport tag.
   *
   * @param securityName
   *    a security name.
   * @param contextEngineID
   *    a context engine ID.
   * @param contextName
   *    a context name
   * @param transportTag
   *    a tag identifying the transport within the SNMP-TARGET-MIB that is
   *    associated with the SNMP message on behalf of which this coexistence
   *    information is created.
   */
  public CoexistenceInfo(OctetString securityName,
                         OctetString contextEngineID,
                         OctetString contextName,
                         OctetString transportTag) {
    this(securityName, contextEngineID, contextName);
    this.transportTag = transportTag;
  }

  public void setTransportTag(OctetString transportTag) {
    this.transportTag = transportTag;
  }

  public void setMaxMessageSize(int maxMessageSize) {
    this.maxMessageSize = maxMessageSize;
  }

  public OctetString getSecurityName() {
    return securityName;
  }

  public OctetString getContextEngineID() {
    return contextEngineID;
  }

  public OctetString getContextName() {
    return contextName;
  }

  public OctetString getTransportTag() {
    return transportTag;
  }

  public int getMaxMessageSize() {
    return maxMessageSize;
  }

  public String toString() {
    return "CoexistenceInfo[securityName="+getSecurityName()+
        ",contextEngineID="+getContextEngineID()+
        ",contextName="+getContextName()+
        ",transportTag="+getTransportTag()+"]";
  }

  public boolean equals(Object o) {
    if (o instanceof CoexistenceInfo) {
      return (compareTo(o) == 0);
    }
    return false;
  }

  public int hashCode() {
    return securityName.hashCode();
  }

  /**
   * Compares this object with the specified object for order.
   *
   * @param o the Object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is
   *   less than, equal to, or greater than the specified object.
   */
  public int compareTo(Object o) {
    CoexistenceInfo other = (CoexistenceInfo)o;
    int c = other.getSecurityName().compareTo(getSecurityName());
    if (c == 0) {
      c = other.getContextEngineID().compareTo(getContextEngineID());
    }
    if (c == 0) {
      c = other.getContextName().compareTo(getContextName());
    }
    return c;
  }
}
