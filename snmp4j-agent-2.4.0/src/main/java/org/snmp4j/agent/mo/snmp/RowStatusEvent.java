/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - RowStatusEvent.java  
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

import org.snmp4j.agent.mo.*;
import org.snmp4j.PDU;
import org.snmp4j.smi.OctetString;
import org.snmp4j.agent.request.Request;
import org.snmp4j.agent.request.SubRequest;

public class RowStatusEvent extends DeniableEventObject {

//  private static final long serialVersionUID = 8808826350825049569L;

  private MOTable table;
  private MOTableRow row;
  private MOTableRow changeSet;
  private int oldStatus;
  private int newStatus;
  private SubRequest request;

  public RowStatusEvent(Object source,
                        MOTable table, MOTableRow row, MOTableRow changeSet,
                        int oldStatus, int newStatus) {
    this(source, table, row, changeSet, oldStatus, newStatus, false);
  }

  public RowStatusEvent(Object source,
                        MOTable table, MOTableRow row, MOTableRow changeSet,
                        int oldStatus, int newStatus, boolean deniable) {
    super(source, deniable);
    this.table = table;
    this.row = row;
    this.changeSet = changeSet;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
  }

  /**
   * Creates a row status event and associates a security name with the
   * status change.
   * @param source
   *    the source that triggered the event.
   * @param table
   *    the MOTable instance the RowStatus belongs to.
   * @param row
   *    the MOTableRow instance the RowStatus belongs to.
   * @param changeSet
   *    the change set this status change is part of.
   * @param oldStatus
   *    the old row status.
   * @param newStatus
   *    the new row status.
   * @param deniable
   *    defines whether status change can be denied or not.
   * @param request
   *    the request that triggered this event.
   * @since 1.4
   */
  public RowStatusEvent(Object source,
                        MOTable table, MOTableRow row, MOTableRow changeSet,
                        int oldStatus, int newStatus, boolean deniable,
                        SubRequest request) {
    this(source, table, row, changeSet, oldStatus, newStatus, deniable);
    this.request = request;
  }

  public int getNewStatus() {
    return newStatus;
  }

  public int getOldStatus() {
    return oldStatus;
  }

  public MOTableRow getRow() {
    return row;
  }

  public MOTable getTable() {
    return table;
  }

  public MOTableRow getChangeSet() {
    return changeSet;
  }

  /**
   * Checks whether the row event represents an activation of a row.
   * To distinguish between a committing and a preparing row status event,
   * use {@link #isDeniable()}. If that method returns <code>true</code>,
   * then the event is fired on behalf of the preparation phase.
   * @return
   *    <code>true</code> if the new row status is createAndGo(4) or active(1)
   *    and the old status is not active(1).
   */
  public boolean isRowActivated() {
    return (((getNewStatus() == RowStatus.createAndGo) ||
             (getNewStatus() == RowStatus.active)) &&
            (getOldStatus() != RowStatus.active));
  }

  /**
   * Checks whether the row event represents a deactivation of a row.
   * To distinguish between a committing and a preparing row status event,
   * use {@link #isDeniable()}. If that method returns <code>true</code>,
   * then the event is fired on behalf the preparation phase.
   * @return
   *    <code>true</code> if the new row status is destroy(6) or notInService(2)
   *    and the old status is active(1).
   */
  public boolean isRowDeactivated() {
    return (((getNewStatus() == RowStatus.destroy) ||
             (getNewStatus() == RowStatus.notInService)) &&
            (getOldStatus() == RowStatus.active));
  }

  /**
   * Returns the request (SNMP or AgentX) associated with this event.
   * @return
   *    the request that triggered this status change or
   *    <code>null</code> if the request is not known/has not been provided.
   * @since 1.4
   */
  public SubRequest getRequest() {
    return request;
  }
}
