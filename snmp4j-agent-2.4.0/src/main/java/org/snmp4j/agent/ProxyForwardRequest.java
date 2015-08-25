/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ProxyForwardRequest.java  
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

import org.snmp4j.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.mo.snmp.SnmpProxyMIB;
import org.snmp4j.agent.security.VACM;
import org.snmp4j.agent.request.SnmpRequest;
import org.snmp4j.agent.mo.snmp.CoexistenceInfo;

/**
 * To (proxy) forward a request or notification to a target, the
 * original command responder event, the context engine ID, and context
 * are required information. A response PDU is returned which has to
 * be send to request originator for confirmed class PDUs.
 *
 * @author Frank Fock
 * @version 1.8.3
 */
public class ProxyForwardRequest {

  private CommandResponderEvent commandEvent;
  private PDU responsePDU;
  private CoexistenceInfo coexistenceInfo;
  private int proxyType;

  public ProxyForwardRequest(CommandResponderEvent commandEvent,
                             CoexistenceInfo coexistenceInfo) {
    this.commandEvent = commandEvent;
    this.coexistenceInfo = coexistenceInfo;
    setProxyType();
  }

  public CommandResponderEvent getCommandEvent() {
    return commandEvent;
  }

  public void setResponsePDU(PDU responsePDU) {
    this.responsePDU = responsePDU;
  }

  public PDU getResponsePDU() {
    return responsePDU;
  }

  public OctetString getContextEngineID() {
    return coexistenceInfo.getContextEngineID();
  }

  public OctetString getContext() {
    return coexistenceInfo.getContextName();
  }

  public OctetString getSecurityName() {
    return coexistenceInfo.getSecurityName();
  }

  public CoexistenceInfo getCoexistenceInfo() {
    return coexistenceInfo;
  }

  public int getProxyType() {
    return proxyType;
  }

  private void setProxyType() {
    int viewType =
        SnmpRequest.getViewType(commandEvent.getPDU().getType());
    switch (viewType) {
      case VACM.VIEW_WRITE: {
        proxyType = SnmpProxyMIB.SnmpProxyTypeEnum.write;
        break;
      }
      case VACM.VIEW_NOTIFY: {
        if (commandEvent.getPDU().getType() == PDU.INFORM) {
          proxyType = SnmpProxyMIB.SnmpProxyTypeEnum.inform;
        }
        else {
          proxyType = SnmpProxyMIB.SnmpProxyTypeEnum.trap;
        }
        break;
      }
      default: {
        proxyType = SnmpProxyMIB.SnmpProxyTypeEnum.read;
      }
    }
  }

  public String toString() {
    return ProxyForwardRequest.class.getName()+
        "[coexistenceInfo="+coexistenceInfo+
        ",proxyType="+proxyType+
        ",commandEvent="+commandEvent+"]";
  }

}
