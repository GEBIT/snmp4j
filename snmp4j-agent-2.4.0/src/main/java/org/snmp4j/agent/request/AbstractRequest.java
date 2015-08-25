/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - AbstractRequest.java  
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

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.PDU;

/**
 * The <code>AbstractRequest</code> implements common elements of SNMP and
 * AgentX requests and might be also used for other sub-agent request types.
 *
 * @author Frank Fock
 * @version 1.0
 */
public abstract class AbstractRequest<U extends SubRequest, S, R>
    implements Request<S,R,U> {

  protected List<U> subrequests;
  protected S source;
  protected R response;
  protected int phase = PHASE_INIT;
  protected int errorStatus = 0;
  protected int repeaterStartIndex;
  protected int repeaterRowSize;
  protected int reprocessCounter = 0;
  protected int transactionID;

  public AbstractRequest(S source) {
    this.source = source;
  }

  @Override
  public S getSource() {
    return source;
  }

  @Override
  public R getResponse() {
    return response;
  }

  public abstract boolean isBulkRequest();

  public U find(OID prefix) {
    for (Iterator<U> it = iterator(); it.hasNext(); ) {
      U sreq = it.next();
      if (sreq.getVariableBinding().getOid().startsWith(prefix)) {
        return sreq;
      }
    }
    return null;
  }

  protected synchronized void initSubRequests() {
    if (subrequests == null) {
      setupSubRequests();
    }
  }

  abstract protected void setupSubRequests();

  abstract protected int getMaxPhase();

  public int nextPhase() {
    if (phase >= getMaxPhase()) {
      throw new NoSuchElementException("Requested phase does not exists");
    }
    resetCompletionStatus();
    switch (phase) {
      case Request.PHASE_2PC_PREPARE: {
        if (getErrorStatus() != PDU.noError) {
          phase = Request.PHASE_2PC_CLEANUP;
        }
        else {
          phase = Request.PHASE_2PC_COMMIT;
        }
        break;
      }
      case Request.PHASE_2PC_COMMIT: {
        if (getErrorStatus() != PDU.noError) {
          phase = Request.PHASE_2PC_UNDO;
        }
        else {
          phase = Request.PHASE_2PC_CLEANUP;
        }
        break;
      }
      case Request.PHASE_2PC_UNDO: {
        phase = Request.PHASE_2PC_CLEANUP;
        break;
      }
      default: {
        phase = Request.PHASE_2PC_PREPARE;
        break;
      }
    }
    return phase;
  }

  public boolean isComplete() {
    return ((getErrorStatus() != PDU.noError) ||
            ((getPhase() >= getMaxPhase()) && isPhaseComplete()));
  }

  public U get(int index) {
    return subrequests.get(index);
  }

  public int getPhase() {
    return phase;
  }

  public int getErrorIndex() {
    if (errorStatus == SnmpConstants.SNMP_ERROR_SUCCESS) {
      return 0;
    }
    initSubRequests();
    int index = 1;
    for (Iterator<U> it = subrequests.iterator(); it.hasNext(); index++) {
      SubRequest sreq = it.next();
      if (sreq.getStatus().getErrorStatus() !=
          SnmpConstants.SNMP_ERROR_SUCCESS) {
        return index;
      }
    }
    return 0;
  }

  public int getErrorStatus() {
    initSubRequests();
    if (errorStatus == SnmpConstants.SNMP_ERROR_SUCCESS) {
      for (SubRequest sreq : subrequests) {
        if (sreq.getStatus().getErrorStatus() !=
            SnmpConstants.SNMP_ERROR_SUCCESS) {
          return sreq.getStatus().getErrorStatus();
        }
      }
    }
    return errorStatus;
  }

  public int getTransactionID() {
    return transactionID;
  }

  public void setPhase(int phase) throws NoSuchElementException {
    if ((phase < 0) || (phase > getMaxPhase())) {
      throw new NoSuchElementException("Illegal phase identifier: "+phase);
    }
    if (this.phase != phase) {
      resetCompletionStatus();
    }
    this.phase = phase;
  }

  protected void resetCompletionStatus() {
    initSubRequests();
    for (SubRequest subReq : subrequests) {
      subReq.getStatus().setPhaseComplete(false);
      subReq.getStatus().setProcessed(false);
    }
  }

  public synchronized void resetProcessedStatus() {
    for (SubRequest sreq : subrequests) {
      sreq.getStatus().setProcessed(sreq.getStatus().isPhaseComplete());
    }
  }

  public void setErrorStatus(int errorStatus) {
    this.errorStatus = errorStatus;
  }

  public boolean equals(Object obj) {
    return (obj instanceof Request) && (transactionID == ((Request) obj).getTransactionID());
  }

  public int hashCode() {
    return transactionID;
  }

  public int getReprocessCounter() {
    return reprocessCounter;
  }

  public void incReprocessCounter() {
    ++reprocessCounter;
  }

  public String toString() {
    return getClass().getName()+"[phase="+phase+",errorStatus="+errorStatus+
        ",source="+source+
        ",response="+response+
        ",transactionID="+transactionID+
        ",repeaterStartIndex="+repeaterStartIndex+
        ",repeaterRowSize="+repeaterRowSize+
        ",reprocessCounter="+reprocessCounter+
        ",subrequests="+subrequests+"]";
  }


}
