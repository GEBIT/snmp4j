/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - NotificationOriginatorImpl.java  
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

import java.util.*;

import org.snmp4j.*;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.security.*;
import org.snmp4j.event.*;
import org.snmp4j.smi.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB.SnmpTargetAddrEntryRow;
import java.io.IOException;
import org.snmp4j.mp.SnmpConstants;

import java.net.InetAddress;

/**
 * The <code>NotificationOriginatorImpl</code> class implements a notification
 * originator application for SNMP4J.
 * <p>
 * See also RFC 3411 for a description of notification originators.
 * </p>
 * @author Frank Fock
 * @version 1.2.1
 */
public class NotificationOriginatorImpl implements NotificationOriginator {

  private static final LogAdapter logger =
      LogFactory.getLogger(NotificationOriginatorImpl.class);

  private Session session;
  private VACM vacm;
  private SnmpTargetMIB targetMIB;
  private SnmpNotificationMIB notificationMIB;
  private SnmpCommunityMIB communityMIB;
  private SysUpTime sysUpTime;
  private transient Vector<NotificationLogListener> notificationLogListeners;
  private long notificationEventID = 0;

  private static OctetString EMPTY_CONTEXT_ENGINE_ID = new OctetString();

  /**
   * Creates a notification originator.
   * @param session
   *    the SNMP Session instance to be used to send the notifications/informs.
   * @param vacm
   *    the VACM to be used to check access for notifications.
   * @param sysUpTime
   *    the sysUpTime instance to be used to determine sysUpTime.0 when
   *    sending notifications without specifically specified sysUpTime.
   * @param targetMIB
   *    the SnmpTargetMIB containing notification target information.
   * @param notificationMIB SnmpNotificationMIB
   */
  public NotificationOriginatorImpl(Session session,
                                    VACM vacm,
                                    SysUpTime sysUpTime,
                                    SnmpTargetMIB targetMIB,
                                    SnmpNotificationMIB notificationMIB) {
    this.session = session;
    this.sysUpTime = sysUpTime;
    this.vacm = vacm;
    this.targetMIB = targetMIB;
    this.notificationMIB = notificationMIB;
  }

  /**
   * Creates a notification originator.
   * @param session
   *    the Snmp instance to be used to send the notifications/informs.
   * @param vacm
   *    the VACM to be used to check access for notifications.
   * @param sysUpTime
   *    the sysUpTime instance to be used to determine sysUpTime.0 when
   *    sending notifications without specifically specified sysUpTime.
   * @param targetMIB
   *    the SnmpTargetMIB containing notification target information.
   * @param notificationMIB
   *    the SnmpNotificationMIB containing notification filtering information.
   * @param communityMIB
   *    the community MIB for coexistence information.
   */
  public NotificationOriginatorImpl(Session session,
                                    VACM vacm,
                                    SysUpTime sysUpTime,
                                    SnmpTargetMIB targetMIB,
                                    SnmpNotificationMIB notificationMIB,
                                    SnmpCommunityMIB communityMIB) {
    this(session, vacm, sysUpTime, targetMIB, notificationMIB);
    this.communityMIB = communityMIB;
  }

  /**
   * Sends notifications (traps) to all appropriate notification targets.
   *
   * @param context the context name of the context on whose behalf this
   *   notification has been generated.
   * @param notificationID the object ID that uniquely identifies this
   *   notification. For SNMPv1 traps, the notification ID has to be build
   *   using the rules provided by RFC 2576.
   * @param vbs an array of <code>VariableBinding</code> instances
   *   representing the payload of the notification.
   * @return an array of ResponseEvent instances. Since the
   *   <code>NotificationOriginator</code> determines on behalf of the
   *   SNMP-NOTIFICTON-MIB contents whether a notification is sent as
   *   trap/notification or as inform request, the returned array contains an
   *   element for each addressed target, but only a response PDU for inform
   *   targets.
   */
  public Object notify(OctetString context, OID notificationID,
                       VariableBinding[] vbs) {
    return notify(context, notificationID, null, vbs);
  }

  private ResponseEvent sendNotification(MOTableRow addr, MOTableRow paramsRow,
                                         OctetString context,
                                         OID notificationID,
                                         TimeTicks sysUpTime,
                                         VariableBinding[] vbs,
                                         int type,
                                         long notificationEventID) {
    Integer32 mpModel = (Integer32)
        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsMPModel);
    OctetString secName = (OctetString)
        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsSecurityName);
    Integer32 secLevel = (Integer32)
        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsSecurityLevel);
//    Integer32 secModel = (Integer32)
//        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsSecurityModel);
    OctetString community = secName;
    Address address = ((SnmpTargetAddrEntryRow)addr).getAddress();
    try {
      if (secName == null) {
        throw new IllegalArgumentException("Security name must not be null");
      }
      if (communityMIB != null) {
         community = communityMIB.getCommunity(secName, null, context);
      }
      if (address == null) {
        String errMessage = "Invalid address in row "+addr+", notification skipped";
        logger.debug(errMessage);
        throw new IllegalArgumentException(errMessage);
      }
      if ((mpModel.getValue() ==  MessageProcessingModel.MPv1 ||
           mpModel.getValue() ==  MessageProcessingModel.MPv2c) &&
          (community == null)) {
        throw new IllegalArgumentException("Security name '"+secName+
            "' is not mapped to any community, notification not sent (RFC 3584 §5.2.3)");
      }
    }
    catch (IllegalArgumentException iaex) {
      logger.warn("Notification not sent due to configuration error: " + iaex.getMessage());
      //return new ResponseEvent((Snmp)session, address, null, null, notificationID, iaex);
      return null;
    }
    Target t;
    PDU pdu;
    switch (mpModel.getValue()) {
      case MessageProcessingModel.MPv1: {
        t = new CommunityTarget(address, community);
        PDUv1 trap = new PDUv1();
        pdu = trap;
        if (sysUpTime != null) {
          trap.setTimestamp(sysUpTime.getValue());
        }
        else {
          trap.setTimestamp(this.sysUpTime.get().getValue());
        }
        int genericID = SnmpConstants.getGenericTrapID(notificationID);
        if (genericID < 0) {
          trap.setGenericTrap(6);
          if ((notificationID.size() > 2) &&
              (notificationID.get(notificationID.size() - 2) == 0)) {
            OID enterprise =
                new OID(notificationID.getValue(), 0,
                        notificationID.size() - 2);
            trap.setEnterprise(enterprise);
          }
          else {
            OID enterprise =
                new OID(notificationID.getValue(), 0,
                        notificationID.size() - 1);
            trap.setEnterprise(enterprise);
          }
          trap.setSpecificTrap(notificationID.last());
        }
        else {
          trap.setGenericTrap(genericID);
          trap.setEnterprise(new OID(new int[] { 0,0 }));
        }
        if (session instanceof Snmp) {
          TransportMapping tm =
              ((Snmp)session).getMessageDispatcher().getTransport(address);
          if ((tm != null) && (tm.getListenAddress() instanceof IpAddress)) {
            InetAddress localAddress =
                ((IpAddress) tm.getListenAddress()).getInetAddress();
            if (localAddress == null) {
              trap.setAgentAddress(new IpAddress("0.0.0.0"));
            }
            else {
              trap.setAgentAddress(new IpAddress(localAddress));
            }
          }
        }
        break;
      }
      case MessageProcessingModel.MPv2c: {
        t = new CommunityTarget(address, community);
        pdu = new PDU();
        break;
      }
      default: {
        byte[] authEngineID =
            (type == SnmpNotificationMIB.SnmpNotifyTypeEnum.inform) ?
            new byte[0] : targetMIB.getLocalEngineID();
        t = new UserTarget(address, secName, authEngineID, secLevel.getValue());
        ScopedPDU scopedPdu = new ScopedPDU();
        scopedPdu.setContextName(context);
        // context engine ID is always ID of the local engine if not overwritten
        // by a subclass
        setContextEngineID(scopedPdu, context, notificationID);
        pdu = scopedPdu;
      }
    }
    t.setVersion(mpModel.getValue());
    Integer32 timeout = (Integer32)
        addr.getValue(SnmpTargetMIB.idxSnmpTargetAddrTimeout);
    Integer32 retries = (Integer32)
        addr.getValue(SnmpTargetMIB.idxSnmpTargetAddrRetryCount);
    t.setTimeout(timeout.getValue() * 10);
    t.setRetries(retries.getValue());
    if (mpModel.getValue() != MessageProcessingModel.MPv1) {
      if (sysUpTime != null) {
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime, sysUpTime));
      }
      else {
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                                    this.sysUpTime.get()));
      }
      pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, notificationID));
    }
    pdu.addAll(vbs);
    pdu.setType((type == SnmpNotificationMIB.SnmpNotifyTypeEnum.inform) ?
                PDU.INFORM : (mpModel.getValue() == MessageProcessingModel.MPv1)
                ? PDU.V1TRAP : PDU.TRAP);
    try {
      OctetString localEngineID = new OctetString();
      OctetString contextEngineID = new OctetString();
      if (pdu instanceof ScopedPDU) {
        localEngineID.setValue(targetMIB.getLocalEngineID());
        contextEngineID = ((ScopedPDU)pdu).getContextEngineID();
      }
      ResponseEvent response = session.send(pdu, t);
      fireNotificationLogEvent(new NotificationLogEvent(this,
                               localEngineID,
                               t,
                               contextEngineID,
                               context, notificationID,
                               sysUpTime,
                               vbs, notificationEventID, true));
      logger.info("Sent notification with ID "+notificationEventID+
                  " "+pdu+" to "+t);
      return response;
    }
    catch (IOException iox) {
      logger.error("Failed to send notification: "+iox.getMessage(), iox);
    }
    return null;
  }

  /**
   * Sets the context engine ID of the scoped PDU to the local engine ID
   * provided by the <code>targetMIB</code> member.
   * @param scopedPDU
   *    the scopedPDU to modify.
   * @param context
   *    the context associated with the notification/inform PDU.
   * @param notificationID
   *    the notification ID of the notification/inform PDU.
   * @since 1.2.1
   */
  protected void setContextEngineID(ScopedPDU scopedPDU,
                                    OctetString context,
                                    OID notificationID) {
    scopedPDU.setContextEngineID(new OctetString(targetMIB.getLocalEngineID()));
  }

  private boolean isAccessGranted(MOTableRow addr, MOTableRow paramsRow,
                                  OctetString context,
                                  OID notificationID,
                                  VariableBinding[] vbs) {
    if (!notificationMIB.passesFilter(paramsRow.getIndex(), notificationID, vbs)) {
      if (logger.isInfoEnabled()) {
        logger.info("Notification " + notificationID + " did not pass filter " +
                    paramsRow.getIndex());
      }
      return false;
    }
//    Integer32 mpModel = (Integer32)
//        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsMPModel);
    OctetString secName = (OctetString)
        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsSecurityName);
    Integer32 secLevel = (Integer32)
        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsSecurityLevel);
    Integer32 secModel = (Integer32)
        paramsRow.getValue(SnmpTargetMIB.idxSnmpTargetParamsSecurityModel);
    int status = vacm.isAccessAllowed(context, secName,
                                      secModel.getValue(), secLevel.getValue(),
                                      VACM.VIEW_NOTIFY, notificationID);
    for (int i=0; (status == VACM.VACM_OK) && (i<vbs.length); i++) {
      status = vacm.isAccessAllowed(context, secName,
                                    secModel.getValue(), secLevel.getValue(),
                                    VACM.VIEW_NOTIFY, vbs[i].getOid());
    }
    return (status == VACM.VACM_OK);
  }

  public Object notify(OctetString context, OID notificationID,
                       TimeTicks sysUpTime, VariableBinding[] vbs) {
    if (logger.isInfoEnabled()) {
      logger.info("Notification " + notificationID + " reported with " +
                  Arrays.asList(vbs)+ " for context "+context);
    }
    if (context == null) {
      context = new OctetString();
    }
    List<ResponseEvent> responses = new LinkedList<ResponseEvent>();
    synchronized (this) {
      notificationEventID++;
    }
    OctetString localEngineID = null;
    if ((targetMIB != null) && (targetMIB.getLocalEngineID() != null)) {
      localEngineID = new OctetString(targetMIB.getLocalEngineID());
    }
    fireNotificationLogEvent(new NotificationLogEvent(this,
                             localEngineID,
                             null,
                             EMPTY_CONTEXT_ENGINE_ID,
                             context, notificationID,
                             sysUpTime,
                             vbs, notificationEventID, false));
    for (Iterator<DefaultMOMutableRow2PC> it = notificationMIB.getNotifyTable().getModel().iterator();
         it.hasNext(); ) {
      MOTableRow notifyRow = it.next();
      OctetString tag = (OctetString)
          notifyRow.getValue(SnmpNotificationMIB.idxSnmpNotifyTag);
      Integer32 type =
          (Integer32) notifyRow.getValue(SnmpNotificationMIB.idxSnmpNotifyType);
      Collection<SnmpTargetAddrEntryRow> addresses = targetMIB.getTargetAddrRowsForTag(tag);
      MOTableRowFilter<SnmpTargetAddrEntryRow> aFilter =
          new RowStatus.ActiveRowsFilter<SnmpTargetAddrEntryRow>(SnmpTargetMIB.idxSnmpTargetAddrRowStatus);
      for (SnmpTargetAddrEntryRow address : addresses) {
        if (aFilter.passesFilter(address)) {
          OctetString params =
              (OctetString) address.getValue(SnmpTargetMIB.idxSnmpTargetAddrParams);
          MOTableRow paramsRow = targetMIB.getTargetParamsRow(params);
          if (paramsRow != null) {
            if (RowStatus.isRowActive(paramsRow,
                SnmpTargetMIB.idxSnmpTargetParamsRowStatus)) {
              if (isAccessGranted(address, paramsRow, context, notificationID, vbs)) {
                ResponseEvent response =
                    sendNotification(address, paramsRow, context,
                        notificationID,
                        sysUpTime,
                        vbs, type.getValue(), notificationEventID);
                responses.add(response);
              } else {
                if (logger.isWarnEnabled()) {
                  logger.warn("Access denied by VACM for " + notificationID);
                }
              }
            } else {
              logger.warn("Found active target address but corresponding params " +
                  " are not active");
            }
          }
          else {
            if (logger.isWarnEnabled()) {
              logger.warn("Found active target address but not corresponding params row: '" + params + "'");
            }
          }
        }
      }
    }
    return responses.toArray(new ResponseEvent[responses.size()]);
  }

  /**
   * Sets the SNMP session to used by this notification originator for sending
   * notifications.
   * @param snmpSession
   *    the Snmp instance to be used to send the notifications/informs.
   * @since 1.9.1
   */
  public void setSession(Session snmpSession) {
    this.session = snmpSession;
  }


  public synchronized void addNotificationLogListener(NotificationLogListener l) {
    if (notificationLogListeners == null) {
      notificationLogListeners = new Vector<NotificationLogListener>(2);
    }
    notificationLogListeners.add(l);
  }

  public synchronized void removeNotificationLogListener(NotificationLogListener l) {
    if (notificationLogListeners != null) {
      notificationLogListeners.remove(l);
    }
  }

  protected void fireNotificationLogEvent(NotificationLogEvent event) {
    if (notificationLogListeners != null) {
      Vector<NotificationLogListener> listeners = notificationLogListeners;
      int count = notificationLogListeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.get(i)).notificationLogEvent(event);
      }
    }
  }

}
