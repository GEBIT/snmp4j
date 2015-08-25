/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - Request.java  
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

import org.snmp4j.smi.OID;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.snmp4j.smi.OctetString;
// JavaDoc
import org.snmp4j.agent.ManagedObject;

/**
 * The <code>Request</code> interface defines common elements of SNMP related
 * operation requests.
 *
 * @author Frank Fock
 * @version 1.2
 */
public interface Request<S,R,U extends SubRequest> {

  int PHASE_INIT = -1;
  int PHASE_1PC = 0;
  int PHASE_2PC_PREPARE  = 1;
  int PHASE_2PC_COMMIT   = 2;
  int PHASE_2PC_UNDO     = 3;
  int PHASE_2PC_CLEANUP  = 4;

  /**
   * Finds the first sub-request whose OID starts with the supplied one.
   * @param prefix
   *    the OID prefix of the sub-request OID.
   * @return
   *    the first <code>SubRequest</code> instance of this request whose OID
   *    starts with <code>prefix</code>. If no such sub-request exits
   *    <code>null</code> is returned.
   */
  U find(OID prefix);

  /**
   * Returns the response object for this request.
   * @return
   *    an object containing the response for this request.
   */
  R getResponse();

  /**
   * Checks whether the response for this request is complete.
   * @return
   *    <code>true</code> if all required data has been collected to create
   *    a response for this request, <code>false</code> otherwise.
   */
  boolean isComplete();

  /**
   * Checks whether the current phase is complete.
   * @return
   *    <code>true</code> if all required processing has been finished for
   *    the current request phase. For single phase request types this method
   *    returns the same result as {@link #isComplete()}.
   */
  boolean isPhaseComplete();

  /**
   * Returns the initiating event object for the request.
   * @return
   *    an <code>Object</code> instance on whose behalf this request
   *    has been initiated.
   */
  S getSource();

  /**
   * Gets the context of the request.
   * @return
   *    an <code>OctetString</code> instance.
   */
  OctetString getContext();

  /**
   * Gets the sub-request at the specified index.
   * @param index
   *    an index <code>&gt;= 0 and &lt; size()</code>
   * @return
   *    a <code>SnmpSubRequest</code> instance.
   */
  U get(int index);

  /**
   * Gets the number of sub-requests in this request. For GETBULK requests
   * this number may increase over time.
   * @return
   *    a positive integer (greater or equal to zero).
   */
  int size();

  /**
   * Gets the phase identifier of the current Two-Phase-Commit (2PC) phase of
   * this request.
   * @return
   *    a 2PC identifier
   */
  int getPhase();

  /**
   * Initializes next phase and returns its identifier.
   * @return
   *    a phase identifier.
   * @throws NoSuchElementException if there is no next phase for this type
   * of request.
   */
  int nextPhase() throws NoSuchElementException;

  /**
   * Sets the request phase.
   * @param phase
   *    a phase identifier.
   * @throws NoSuchElementException if there is no such phase for this type
   * of request.
   */
  void setPhase(int phase) throws NoSuchElementException;

  /**
   * Returns an <code>Iterator</code> over the sub-requests of this request.
   * @return
   *    an <code>Iterator</code>
   */
  Iterator<U> iterator();

  void setViewName(OctetString viewName);

  OctetString getViewName();

  OctetString getSecurityName();

  int getMessageProcessingModel();

  int getSecurityModel();

  int getSecurityLevel();

  int getViewType();

  void setErrorStatus(int errorStatus);

  int getErrorStatus();

  int getErrorIndex();

  int getTransactionID();

  /**
   * Set the processed status of each (incomplete) sub-request to
   * <code>false</code>.
   */
  void resetProcessedStatus();

  /**
   * Returns the value of the reprocessing counter associated with this request.
   * The reprocessing counter can be used to detect and handle endless-loop
   * errors caused by instrumentation code not setting the completion status
   * of a sub-request correctly.
   * @return
   *    0 after the initial (and normally last) processing iteration and
   *    <code>n</code> after the <code>n</code>-th reprocessing iteration.
   * @see #incReprocessCounter()
   */
  int getReprocessCounter();

  /**
   * Increments the reprocess counter by one.
   * @see #getReprocessCounter()
   */
  void incReprocessCounter();

  /**
   * Gets the processing user object for the specified key. By default
   * there is no such object associated with a request, but
   * {@link ManagedObject}s may put their own objects into the request
   * (for example to avoid unnecessary updates on behalf of the same request).
   *
   * @param key
   *    an Object.
   * @return
   *    the associated Object or <code>null</code> if their exists no such
   *    association.
   * @since 1.2
   */
  Object getProcessingUserObject(Object key);

  /**
   * Sets an user object association.
   *
   * @param key
   *    a key Object.
   * @param value
   *    a value Object associated with <code>key</code>.
   * @return
   *    the previous value associated with <code>key</code> or <code>null</code>
   *    if no such association existed.
   * @since 1.2
   */
  Object setProcessingUserObject(Object key, Object value);
}
