/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - RequestStatus.java  
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


package org.snmp4j.agent.request;

import java.util.*;

import org.snmp4j.mp.*;

/**
 * The <code>RequestStatus</code> models the (error) state of a SNMP request.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class RequestStatus {

  private int errorStatus = SnmpConstants.SNMP_ERROR_SUCCESS;
  private boolean phaseComplete = false;
  private boolean processed = false;
  private transient Vector<RequestStatusListener> requestStatusListeners;

  public RequestStatus() {
  }

  public int getErrorStatus() {
    return errorStatus;
  }

  public void setErrorStatus(int errorStatus) {
    this.errorStatus = errorStatus;
    boolean error = (errorStatus != SnmpConstants.SNMP_ERROR_SUCCESS);
    setPhaseComplete(error);
    fireRequestStatusChanged(new RequestStatusEvent(this, this));
  }

  public boolean isPhaseComplete() {
    return phaseComplete;
  }

  public boolean isProcessed() {
    return processed;
  }

  public void setPhaseComplete(boolean completionStatus) {
    this.phaseComplete = completionStatus;
    this.processed |= completionStatus;
  }

  public void setProcessed(boolean processed) {
    this.processed = processed;
  }

  public synchronized void addRequestStatusListener(RequestStatusListener l) {
    if (this.requestStatusListeners == null) {
      this.requestStatusListeners = new Vector<RequestStatusListener>(2);
    }
    this.requestStatusListeners.add(l);
  }

  public synchronized void removeRequestStatusListener(RequestStatusListener l) {
    if (this.requestStatusListeners != null) {
      this.requestStatusListeners.remove(l);
    }
  }

  protected void fireRequestStatusChanged(RequestStatusEvent event) {
    if (requestStatusListeners != null) {
      Vector<RequestStatusListener> listeners = requestStatusListeners;
      for (RequestStatusListener listener : listeners) {
        listener.requestStatusChanged(event);
      }
    }
  }

}
