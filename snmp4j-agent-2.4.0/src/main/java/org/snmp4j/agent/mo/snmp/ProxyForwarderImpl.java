/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ProxyForwarderImpl.java  
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

import java.io.*;
import java.util.*;

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.SnmpProxyMIB.*;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.agent.security.*;
import org.snmp4j.event.*;
import org.snmp4j.log.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;
import org.snmp4j.util.*;
import java.net.InetAddress;

/**
 * The <code>ProxyForwarderImpl</code> class implements a proxy forwarder
 * instance as defined by RFC 3413. It is configured through the SNMP-PROXY-MIB
 * and SNMP-TARGET-MIB implementations provided on construction. It sends
 * notifications through the provided SNMP session.
 *
 * @author Frank Fock
 * @version 1.9.1
 */
public class ProxyForwarderImpl implements ProxyForwarder {

  private static final LogAdapter logger =
      LogFactory.getLogger(ProxyForwarderImpl.class);

  private Session session;
  private SnmpProxyMIB proxyMIB;
  private SnmpTargetMIB targetMIB;
  private transient Vector<CounterListener> counterListeners;

  /**
   * Creates a <code>ProxyForwarder</code> implementation based on a SNMP
   * session used to send the notifications and a SNMP-PROXY-MIB
   * and a SNMP-TARGET-MIB implementation used for its configuration.
   * @param session
   *    a SNMP session.
   * @param proxyMIB
   *    a <code>SnmpProxyMIB</code> implementation with the proxy configuration.
   * @param targetMIB
   *    a <code>SnmpTargetMIB</code> implementation with the target
   *    configuration.
   */
  public ProxyForwarderImpl(Session session,
                            SnmpProxyMIB proxyMIB, SnmpTargetMIB targetMIB) {
    this.session = session;
    this.proxyMIB = proxyMIB;
    this.targetMIB = targetMIB;
  }

  /**
   * Forwards a <code>Request</code> if it matches the criteria defined by the
   * SNMP-PROXY-MIB associated with this proxy forwarder.
   *
   * @param request
   *    a <code>ProxyForwardRequest</code> encapsuling the forwarding request.
   * @return
   *   <code>true</code> if the request has been forwarded,
   *   <code>false</code> otherwise.
   */
  public boolean forward(ProxyForwardRequest request) {
    int pduType = request.getCommandEvent().getPDU().getType();
    if (SnmpRequest.getViewType(pduType) == VACM.VIEW_NOTIFY) {
      return multipleForward(request);
    }
    else {
      ResponseEvent resp = singleForward(request);
      if ((resp != null) && (resp.getResponse() != null)) {
        PDU respPDU = resp.getResponse();
        PDU translatedResponse = DefaultPDUFactory.createPDU(
            request.getCommandEvent().getSecurityModel());
        if (!translatedResponse.getClass().equals(respPDU.getClass())) {
          // not required PDU instance -> copy data
          translatedResponse.setType(respPDU.getType());
          translatedResponse.addAll(respPDU.toArray());
          translatedResponse.setErrorIndex(respPDU.getErrorIndex());
          translatedResponse.setErrorStatus(respPDU.getErrorStatus());
        }
        else {
          translatedResponse = respPDU;
        }
        if (translatedResponse.getType() == PDU.RESPONSE) {
          translatedResponse.setRequestID(
              request.getCommandEvent().getPDU().getRequestID());
          if ((translatedResponse instanceof ScopedPDU) &&
              (request.getCommandEvent().getPDU() instanceof ScopedPDU)) {
            ScopedPDU scopedPDUReq =
                (ScopedPDU) request.getCommandEvent().getPDU();
            ScopedPDU scopedPDUResp = (ScopedPDU) translatedResponse;
            scopedPDUResp.setContextEngineID(scopedPDUReq.getContextEngineID());
            scopedPDUResp.setContextName(scopedPDUReq.getContextName());
          }
        }
        request.setResponsePDU(translatedResponse);
        return true;
      }
    }
    return false;
  }

  protected List getMatches(ProxyForwardRequest request) {
    List<SnmpProxyRow> matches =
        proxyMIB.getProxyRows(request.getProxyType(),
                              request.getContextEngineID(),
                              request.getContext());
    for (Iterator it = matches.iterator(); it.hasNext(); ) {
      SnmpProxyRow possibleMatch = (SnmpProxyRow) it.next();
      OctetString paramIn = possibleMatch.getSnmpProxyTargetParamsIn();
      if (logger.isDebugEnabled()) {
        logger.debug("Checking possible match for in parameter: "+paramIn);
      }
      if (!matchParameters(request, paramIn)) {
        it.remove();
      }
    }
    return matches;
  }

  protected boolean matchParameters(ProxyForwardRequest request,
                                    OctetString paramIn) {
    MOTableRow param2Match = targetMIB.getTargetParamsRow(paramIn, true);
    if (param2Match != null) {
      int mpModel = param2Match.getValue(
          SnmpTargetMIB.idxSnmpTargetParamsMPModel).toInt();
      int secModel = param2Match.getValue(
          SnmpTargetMIB.idxSnmpTargetParamsSecurityModel).toInt();
      int secLevel = param2Match.getValue(
          SnmpTargetMIB.idxSnmpTargetParamsSecurityLevel).toInt();
      OctetString secName = (OctetString) param2Match.getValue(
          SnmpTargetMIB.idxSnmpTargetParamsSecurityName);
      if (logger.isDebugEnabled()) {
        logger.debug("Matching request "+request+"  with mpModel="+mpModel+
                     ", secModel="+secModel+", secLevel="+secLevel+
                     ", secName="+secName);
      }
      if ((mpModel == request.getCommandEvent().getMessageProcessingModel()) &&
          (secName.equals(request.getSecurityName())) &&
          ((secModel == 0) ||
           (secModel == request.getCommandEvent().getSecurityModel())) &&
          (secLevel == request.getCommandEvent().getSecurityLevel())) {
        return true;
      }
    }
    return false;
  }

  protected ResponseEvent singleForward(ProxyForwardRequest request) {
    List matches = getMatches(request);
    if ((matches == null) || (matches.isEmpty())) {
      if (logger.isInfoEnabled()) {
        logger.info("No matching proxy entry found for contextEngineID="+
                    request.getContextEngineID()+
                    ", context="+request.getContext()+" and request="+
                    request);
      }
      return null;
    }
    OctetString outParam = (OctetString)
        ((MOTableRow)
         matches.get(0)).getValue(SnmpProxyMIB.idxSnmpProxySingleTargetOut);
    Target target = targetMIB.getTarget(outParam,
                                        request.getContextEngineID(),
                                        request.getContext());
    if (target == null) {
      if (logger.isInfoEnabled()) {
        logger.info("No matching target with name '" + outParam + "'");
      }
      return null;
    }
    // forwarding request
    if (logger.isInfoEnabled()) {
      logger.info("Forwarding proxy request "+request+" to "+target);
    }
    PDU reqPDU = request.getCommandEvent().getPDU();
    PDU pdu = DefaultPDUFactory.createPDU(target, reqPDU.getType());
    setScope(request, pdu);
    try {
      proxyForwardTranslation(request, reqPDU, pdu);
      ResponseEvent response;
      do {
        response = session.send(pdu, target);
        if (logger.isInfoEnabled()) {
          logger.info("Received proxy response from " +
                      response.getPeerAddress() +
                      " is " + response.getResponse());
        }
      }
      while (proxyBackwardTranslation(reqPDU, pdu, response));
      return response;
    }
    catch (Exception ex) {
      if (logger.isDebugEnabled()) {
        ex.printStackTrace();
      }
      logger.error("Failed to send proxy request to "+target+" because: "+
                   ex.getMessage());
      fireIncrementCounter(new CounterEvent(this, SnmpConstants.snmpProxyDrops));
      if (SNMP4JSettings.isForwardRuntimeExceptions()) {
        throw new RuntimeException(ex);
      }
      return null;
    }
  }

  protected boolean proxyBackwardTranslation(PDU reqPDU, PDU pdu,
                                             ResponseEvent response) {
    if (response.getResponse() == null) {
      return false;
    }
    PDU resp = response.getResponse();
    if ((resp.getErrorStatus() == PDU.tooBig) &&
        (reqPDU.getType() != PDU.GETBULK)) {
      response.getResponse().clear();
      response.getResponse().setErrorStatus(PDU.noError);
      response.getResponse().setErrorIndex(0);
      return false;
    }
    if ((resp.getErrorStatus() == PDU.tooBig) &&
        (reqPDU.getType() == PDU.GETBULK) && (pdu.getType() == PDU.GETBULK)) {
      /**implemented as defined in RFC 3584 4.3.1(3)*/
      if (pdu.size() == 1) {
        response.getResponse().clear();
        response.getResponse().setErrorStatus(PDU.noError);
        response.getResponse().setErrorIndex(0);
        return false;
      }
      else {
        while (pdu.size() > 1) {
          pdu.trim();
        }
        pdu.setType(PDU.GETNEXT);
        return true;
      }
    }
    if ((reqPDU instanceof PDUv1) &&
        (!(response.getResponse() instanceof PDUv1))) {
      boolean resendNeeded = false;
      for (int i=0; i<resp.size(); i++) {
        VariableBinding vb = resp.get(i);
        if (vb.getVariable() instanceof Counter64) {
          OID nextOID = new OID(vb.getOid());
          if (nextOID.last() < 65535) {
            nextOID.set(nextOID.size()-1, 65535);
          }
          else {
            nextOID.set(nextOID.size()-1, OID.MAX_SUBID_VALUE);
          }
          pdu.get(i).setOid(vb.getOid());
          resendNeeded = true;
        }
      }
      if (resendNeeded && (reqPDU.getType() != PDU.GETNEXT)) {
        throw new IllegalArgumentException(
            "GET response with Counter64 cannot be proxied");
      }
      else {
        return true;
      }
    }
    return false;
  }

  /**
   * Translates a source PDU into the supplied target PDU. The mapping between
   * the source PDU and the target PDU is done as defined by RFC 3584.
   * @param request
   *    the proxy forwarding request.
   * @param source
   *    the source PDU instance.
   * @param target
   *    the target PDU instance. The variable bindings of the source will
   *    replace any VBs of the target instance. If the source PDU cannot
   *    be converted to the target PDU <code>target</code> is not changed.
   *    Instead an intermediate PDU is returned.
   * @throws IllegalArgumentException
   *    if <code>source</code> contains an illegal notification PDU.
   */
  protected void proxyForwardTranslation(ProxyForwardRequest request,
                                         PDU source, PDU target)
      throws IllegalArgumentException
  {
    target.clear();
    target.setType(source.getType());
    if (!(target instanceof PDUv1) && !(source instanceof PDUv1)) {
      target.setMaxRepetitions(source.getMaxRepetitions());
      target.setNonRepeaters(source.getNonRepeaters());
    }
    if ((source.getType() == PDU.V1TRAP) &&
        (source instanceof PDUv1) &&
        (!(target instanceof PDUv1))) {
      PDUv1 sourceV1 = (PDUv1)source;
      target.setType(PDU.NOTIFICATION);
      target.add(new VariableBinding(SnmpConstants.sysUpTime,
                                     new TimeTicks(sourceV1.getTimestamp())));
      target.add(new VariableBinding(SnmpConstants.snmpTrapOID,
                                     SnmpConstants.getTrapOID(
                                         sourceV1.getEnterprise(),
                                         sourceV1.getGenericTrap(),
                                         sourceV1.getSpecificTrap())));
      target.addAll(source.toArray());
      target.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
                                     sourceV1.getAgentAddress()));
      target.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise,
                                     sourceV1.getEnterprise()));
      OctetString community =
          new OctetString(request.getCommandEvent().getSecurityName());
      target.add(new VariableBinding(SnmpConstants.snmpTrapCommunity,
                                     community));
    }
    else if (((source.getType() == PDU.NOTIFICATION) ||
              (source.getType() == PDU.INFORM)) &&
             (target instanceof PDUv1)) {
      PDUv1 targetV1 = (PDUv1)target;
      target.setType(PDU.V1TRAP);
      if ((source.size() < 2) ||
          (!(source.get(0).getVariable() instanceof TimeTicks)) ||
          (!(source.get(1).getVariable() instanceof OID))) {
        throw new IllegalArgumentException("Proxy source invalid notification PDU: "+
                                           source);
      }
      TimeTicks sysUpTime = (TimeTicks) source.get(0).getVariable();
      OID trapOID = (OID)source.get(1).getVariable();
      int genericID = SnmpConstants.getGenericTrapID(trapOID);
      // RFC 3584 Translating
      // SNMPv2 notification to SNMPv1 notification parameters
      if (genericID < 0) {
        targetV1.setGenericTrap(6);
        if ((trapOID.size() > 2) && (trapOID.get(trapOID.size() - 2) == 0)) {
          targetV1.setSpecificTrap(trapOID.get(trapOID.size() - 1));
          OID enterprise = new OID(trapOID);
          enterprise.trim(2);
          targetV1.setEnterprise(enterprise);
        }
        else if (trapOID.size() > 1) {
          targetV1.setSpecificTrap(trapOID.get(trapOID.size() - 1));
          OID enterprise = new OID(trapOID);
          enterprise.trim(1);
          targetV1.setEnterprise(enterprise);
        }
      }
      else {
        targetV1.setGenericTrap(genericID);
        targetV1.setSpecificTrap(0);
      }
      target.addAll(source.toArray());
      if (request.getCommandEvent().getPeerAddress() instanceof IpAddress) {
        InetAddress agentAddress = ((IpAddress)
           request.getCommandEvent().getPeerAddress()).getInetAddress();
        targetV1.setAgentAddress(new IpAddress(agentAddress));
      }
      else {
        targetV1.setAgentAddress(new IpAddress("0.0.0.0"));
      }
      targetV1.setTimestamp(sysUpTime.getValue());
    }
    else {
      target.addAll(source.toArray());
    }
  }

  protected boolean multipleForward(ProxyForwardRequest request) {
    List matches = getMatches(request);
    boolean allOK = true;
    for (Object matche : matches) {
      SnmpProxyRow item = (SnmpProxyRow) matche;
      OctetString outParam = item.getSnmpProxyMultipleTargetOut();
      Set tags = SnmpTagList.getTags(outParam);
      if (logger.isDebugEnabled()) {
        logger.debug("Proxy multiple targets out with tags " + tags);
      }
      for (Object tag1 : tags) {
        OctetString tag = (OctetString) tag1;
        Collection targets = this.targetMIB.getTargetAddrRowsForTag(tag);
        for (Object target1 : targets) {
          SnmpTargetAddrEntryRow targetRow =
              (SnmpTargetAddrEntryRow) target1;
          Target target = targetRow.getTarget(request.getContextEngineID(),
              request.getContext());
          if (target != null) {
            try {
              PDU reqPDU = request.getCommandEvent().getPDU();
              PDU pdu = DefaultPDUFactory.createPDU(target, reqPDU.getType());
              setScope(request, pdu);
              proxyForwardTranslation(request, reqPDU, pdu);
              ResponseEvent resp = session.send(pdu, target);
              if (logger.isInfoEnabled()) {
                logger.info("Forwarded " + request.getCommandEvent() +
                    " to target " + target + " with response " + resp);
              }
              if (request.getCommandEvent().getPDU().getType() == PDU.INFORM) {
                if ((resp.getResponse() == null) ||
                    (resp.getResponse().getType() == PDU.REPORT) ||
                    (resp.getResponse().getErrorStatus() != PDU.noError)) {
                  allOK = false;
                }
              }
            } catch (IOException ex) {
              if (logger.isDebugEnabled()) {
                ex.printStackTrace();
              }
              logger.error("Failed to forward request " + request +
                  " to target " + target);
              allOK = false;
            }
          } else {
            if (logger.isDebugEnabled()) {
              logger.debug("Parameters for target " + targetRow + " not found");
            }
          }
        }
      }
    }
    return allOK;
  }

  private void setScope(ProxyForwardRequest request, PDU pdu) {
    if (pdu instanceof ScopedPDU) {
      ScopedPDU scopedPDU = (ScopedPDU)pdu;
      scopedPDU.setContextEngineID(request.getContextEngineID());
      scopedPDU.setContextName(request.getContext());
    }
  }

  public synchronized void addCounterListener(CounterListener l) {
    if (counterListeners == null) {
      counterListeners = new Vector<CounterListener>(2);
    }
    counterListeners.add(l);
  }

  public synchronized void removeCounterListener(CounterListener l) {
    if (counterListeners != null) {
      counterListeners.remove(l);
    }
  }

  protected void fireIncrementCounter(CounterEvent event) {
    if (counterListeners != null) {
      Vector<CounterListener> listeners = counterListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.elementAt(i)).incrementCounter(event);
      }
    }
  }

  /**
   * Sets the SNMP session to used by this proxy forwarder for sending
   * SNMP messages.
   * @param snmpSession
   *    the Snmp instance to be used to send SNMP messages.
   * @since 1.9.1
   */
  public void setSession(Session snmpSession) {
    this.session = snmpSession;
  }

}
