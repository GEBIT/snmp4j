/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOSubtreeProxy.java  
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

package org.snmp4j.agent.mo.snmp4j;

import org.snmp4j.PDU;
import org.snmp4j.Session;
import org.snmp4j.Target;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.request.SubRequest;
import org.snmp4j.agent.util.OIDTranslation;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;

import java.io.IOException;

/**
 * In contrast to {@link org.snmp4j.agent.ProxyForwarder}, the MOSubtreeProxy
 * provides a proxy implementation for a single subtree based on the
 * configuration provided by the {@link org.snmp4j.agent.mo.snmp4j.Snmp4jProxyMib}
 * {@link org.snmp4j.agent.mo.snmp.SnmpProxyMIB}
 * and {@link org.snmp4j.agent.mo.snmp.SnmpTargetMIB}.
 *
 * @author Frank Fock
 * @version 2.0
 * @since 2.0
 */
public class MOSubtreeProxy implements ManagedObject {

  private static final LogAdapter logger = LogFactory.getLogger(MOSubtreeProxy.class);

  private Session session;
  private SnmpTargetMIB targetMIB;
  private OctetString proxyName;
  private OctetString contextName;
  private OctetString contextEngineID;
  private DefaultMOScope scope;
  private OctetString targetName;
  private MOAccess access = MOAccessImpl.ACCESS_READ_WRITE;
  private OIDTranslation translation;
  private boolean enableUndo = true;

  private PDUFactory pduFactory = new DefaultPDUFactory();

  public MOSubtreeProxy(Session session, SnmpTargetMIB targetMIB,
                        OctetString proxyName,
                        OID subtreeOID, OctetString contextEngineID,
                        OctetString contextName, OctetString targetName) {
    this.session = session;
    this.targetMIB = targetMIB;
    this.proxyName = proxyName;
    this.contextName = contextName;
    this.scope = new DefaultMOScope(subtreeOID, true, subtreeOID.nextPeer(), false);
    this.targetName = targetName;
    this.contextEngineID = contextEngineID;
  }

  @Override
  public MOScope getScope() {
    return scope;
  }

  @Override
  public OID find(MOScope range) {
/*
    if (translation != null) {
      range = translate(range);
    }
    */
    if (access.isAccessibleForRead()) {
      OID lowerBound = range.getLowerBound();
      OID next = OID.max(((lowerBound == null) ? scope.getLowerBound() : lowerBound), scope.getLowerBound());
      if (scope.covers(next)) {
        return next;
      }
    }
    return null;
  }

  /**
   * Sets the remote sub-tree OID for the translation between local
   * agent OID and remote agent OID. If <tt>null</tt>, zero length,
   * or equal to 0.0, no translation takes place.
   *
   * @param remoteSubtree
   *    the OID sub-tree of the target agent to proxy.
   */
  public void setTargetSubtree(OID remoteSubtree) {
    this.translation = new OIDTranslator(remoteSubtree);
  }

  private MOScope translate(MOScope range) {
    if (translation != null) {
      MOScope translated =
          new DefaultMOScope(translation.forwardTranslate(scope.getLowerBound()), scope.isLowerIncluded(),
                             translation.forwardTranslate(scope.getUpperBound()), scope.isUpperIncluded());
      return translated;
    }
    return range;
  }

  @Override
  public void get(SubRequest request) {
    if (!access.isAccessibleForRead()) {
      request.setErrorStatus(PDU.noAccess);
      return;
    }
    OID oid = request.getVariableBinding().getOid();
    if (translation != null) {
      oid = translation.forwardTranslate(oid);
    }
    VariableBinding vb = new VariableBinding(oid);
    Target target = targetMIB.getTarget(targetName, contextEngineID, contextName);
    PDU pdu = pduFactory.createPDU(target);
    pdu.setType(PDU.GET);
    pdu.add(vb);
    try {
      ResponseEvent resp = session.send(pdu, target);
      if (resp.getResponse() != null) {
        PDU rpdu = resp.getResponse();
        if (rpdu.getErrorStatus() != PDU.noError) {
          request.setErrorStatus(rpdu.getErrorStatus());
        }
        else {
          request.getVariableBinding().setVariable(resp.getResponse().getVariable(oid));
        }
      }
    } catch (IOException e) {
      logger.error("IOException in GET sub-request "+request+" to "+target, e);
    }
    request.completed();
  }

  @Override
  public boolean next(SubRequest request) {
    if (!access.isAccessibleForRead()) {
      return false;
    }
    OID oid = request.getVariableBinding().getOid();
    if (translation != null) {
      oid = translation.forwardTranslate(oid);
    }
    VariableBinding vb = new VariableBinding(oid);
    Target target = targetMIB.getTarget(targetName, contextEngineID, contextName);
    PDU pdu = pduFactory.createPDU(target);
    pdu.setType(PDU.GETNEXT);
    pdu.add(vb);
    try {
      ResponseEvent resp = session.send(pdu, target);
      if (resp.getResponse() != null) {
        PDU rpdu = resp.getResponse();
        if (rpdu.getErrorStatus() != PDU.noError) {
          request.setErrorStatus(rpdu.getErrorStatus());
        }
        else {
          VariableBinding rvb = resp.getResponse().getVariableBindings().get(0);
          OID nextOID = rvb.getOid();
          if (translation != null) {
            nextOID = translation.backwardTranslate(nextOID);
            rvb.setOid(nextOID);
          }
          if (!scope.covers(nextOID)) {
            return false;
          }
          request.getVariableBinding().setOid(rvb.getOid());
          request.getVariableBinding().setVariable(rvb.getVariable());
          request.completed();
          return true;
        }
      }
    } catch (IOException e) {
      logger.error("IOException in NEXT sub-request " + request + " to " + target, e);
    }
    request.completed();
    return false;
  }

  @Override
  public void prepare(SubRequest request) {
    if (enableUndo) {
      OID oid = request.getVariableBinding().getOid();
      if (translation != null) {
        oid = translation.forwardTranslate(oid);
      }
      VariableBinding vb = new VariableBinding(oid);
      Target target = targetMIB.getTarget(targetName, contextEngineID, contextName);
      PDU pdu = pduFactory.createPDU(target);
      pdu.setType(PDU.GET);
      pdu.add(vb);
      try {
        ResponseEvent resp = session.send(pdu, target);
        if (resp.getResponse() != null) {
          PDU rpdu = resp.getResponse();
          if (rpdu.getErrorStatus() != PDU.noError) {
            request.setErrorStatus(rpdu.getErrorStatus());
          }
          VariableBinding rvb = rpdu.getVariableBindings().get(0);
          if ((rvb == null) || rvb.isException()) {
            request.setErrorStatus(PDU.noSuchName);
          }
          else {
            request.setUndoValue(rvb);
          }
        }
      } catch (IOException e) {
        request.setErrorStatus(PDU.genErr);
        logger.error("IOException in prepare SET sub-request " + request + " to " + target, e);
      }
    }
    else {
      request.setUndoValue(null);
    }
    request.getStatus().setPhaseComplete(true);
  }

  @Override
  public void commit(SubRequest request) {
    OID oid = request.getVariableBinding().getOid();
    if (translation != null) {
      oid = translation.forwardTranslate(oid);
    }
    VariableBinding vb = new VariableBinding(oid, request.getVariableBinding().getVariable());
    Target target = targetMIB.getTarget(targetName, contextEngineID, contextName);
    PDU pdu = pduFactory.createPDU(target);
    pdu.setType(PDU.SET);
    pdu.add(vb);
    try {
      ResponseEvent resp = session.send(pdu, target);
      if (resp.getResponse() != null) {
        PDU rpdu = resp.getResponse();
        if (rpdu.getErrorStatus() != PDU.noError) {
          request.setErrorStatus(rpdu.getErrorStatus());
        }
        request.getStatus().setPhaseComplete(true);
      }
      else {
        request.setErrorStatus(PDU.genErr);
      }
    } catch (IOException e) {
      request.setErrorStatus(PDU.genErr);
      logger.error("IOException in commit SET sub-request " + request + " to " + target, e);
    }
  }

  @Override
  public void undo(SubRequest request) {
    VariableBinding vb = (VariableBinding) request.getUndoValue();
    if (vb != null) {
      Target target = targetMIB.getTarget(targetName, contextEngineID, contextName);
      PDU pdu = pduFactory.createPDU(target);
      pdu.setType(PDU.SET);
      pdu.add(vb);
      try {
        ResponseEvent resp = session.send(pdu, target);
        if (resp.getResponse() != null) {
          PDU rpdu = resp.getResponse();
          if (rpdu.getErrorStatus() != PDU.noError) {
            request.setErrorStatus(PDU.undoFailed);
            logger.warn("Undo failed because target '"+target+"' returned error "+rpdu.getErrorStatusText());
          }
        }
        else {
          request.setErrorStatus(PDU.genErr);
        }
      } catch (IOException e) {
        request.setErrorStatus(PDU.genErr);
        logger.error("IOException in undo SET sub-request " + request + " to " + target, e);
      }
    }
    else if (enableUndo) {
      request.setErrorStatus(PDU.undoFailed);
    }
    request.getStatus().setPhaseComplete(true);
  }

  @Override
  public void cleanup(SubRequest request) {
    request.setUndoValue(null);
    request.getStatus().setPhaseComplete(true);
  }

  public OctetString getProxyName() {
    return proxyName;
  }

  public MOAccess getAccess() {
    return access;
  }

  public void setAccess(MOAccess access) {
    this.access = access;
  }

  public OIDTranslation getTranslation() {
    return translation;
  }

  /**
   * Sets a OID translation. By default it is not active (<tt>null</tt>).
   *
   * @param translation
   *    If not <tt>null</tt>, the {@link OIDTranslation} can be
   *    used to map between local OID and remote OID and vice
   *    versa.
   */
  public void setTranslation(OIDTranslation translation) {
    this.translation = translation;
  }

  @Override
  public String toString() {
    return "MOSubtreeProxy[" +
        "session=" + session +
        ", targetMIB=" + targetMIB +
        ", proxyName=" + proxyName +
        ", contextName=" + contextName +
        ", contextEngineID=" + contextEngineID +
        ", scope=" + scope +
        ", targetName=" + targetName +
        ", access=" + access +
        ", translation=" + translation +
        ", enableUndo=" + enableUndo +
        ", pduFactory=" + pduFactory +
        ']';
  }


  public class OIDTranslator implements OIDTranslation {

    private OID remoteOID;

    public OIDTranslator(OID remoteOID) {
      this.remoteOID = remoteOID;
    }

    @Override
    public OID forwardTranslate(OID oid) {
      if (oid != null) {
        OID translated;
        if ((oid.size() >= scope.getLowerBound().size()) && (oid.startsWith(scope.getLowerBound()))) {
          OID remote = new OID(remoteOID);
          int[] suffix = new int[oid.size() - scope.getLowerBound().size()];
          if (suffix.length > 0) {
            System.arraycopy(oid.getValue(), scope.getLowerBound().size(), suffix, 0, suffix.length);
            remote.append(new OID(suffix));
          }
          translated = remote;
        }
        else if (oid.compareTo(scope.getLowerBound()) < 0) {
          translated = new OID(remoteOID);
        }
        else {
          translated = remoteOID.nextPeer();
        }
        if (logger.isDebugEnabled()) {
          logger.debug("Forward OID translation '"+oid+"'->'"+translated);
        }
        return translated;
      }
      return null;
    }

    @Override
    public OID backwardTranslate(OID oid) {
      if (oid != null) {
        OID translated;
        if ((oid.size() >= remoteOID.size()) && (oid.startsWith(remoteOID))) {
          OID local = new OID(scope.getLowerBound());
          int[] suffix = new int[oid.size() - remoteOID.size()];
          if (suffix.length > 0) {
            System.arraycopy(oid.getValue(), remoteOID.size(), suffix, 0, suffix.length);
            local.append(new OID(suffix));
          }
          translated = local;
        }
        else if (oid.compareTo(this.remoteOID) > 0) {
          translated = new OID(scope.getUpperBound());
        }
        else {
          translated = new OID(scope.getUpperBound());
        }
        if (logger.isDebugEnabled()) {
          logger.debug("Backward OID translation '"+oid+"'->'"+translated);
        }
        return translated;
      }
      return null;
    }

    @Override
    public String toString() {
      return "OIDTranslator{" +
          "remoteOID=" + remoteOID +
          '}';
    }
  }
}
