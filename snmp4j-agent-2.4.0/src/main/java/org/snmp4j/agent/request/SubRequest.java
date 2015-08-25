/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SubRequest.java  
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

import org.snmp4j.smi.VariableBinding;
import org.snmp4j.agent.MOScope;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.MOQuery;

/**
 * The <code>SubRequest</code> interface defines general sub-request
 * properties and behavior.
 *
 * @author Frank Fock
 * @version 2.2
 */
public interface SubRequest {

  /**
   * Indicates whether this (sub-)request has an error.
   * @return
   *    <code>true</code> if this request (and thus also this sub-request) has
   *    an error status greater than zero.
   */
  boolean hasError();

  /**
   * Sets the error status for this sub-request. Calling this method is a
   * shortcut for <code>{@link #getStatus()}.setErrorStatus(int errorStatus)
   * </code>.
   * @param errorStatus
   *    a SNMPv2/v3 error status.
   * @since 1.0.1
   */
  void setErrorStatus(int errorStatus);

  /**
   * Gets the error status for this sub-request. Calling this method is a
   * shortcut for <code>{@link #getStatus()}.getErrorStatus()
   * </code>.
   * @return
   *    a SNMPv2/v3 error status.
   * @since 1.0.1
   */
  int getErrorStatus();

  /**
   * Gets the status object associated with this sub-request.
   * @return
   *    a RequestStatus instance.
   */
  RequestStatus getStatus();

  /**
   * Gets the scope for this subrequest. The scope is solely defined by this
   * sub-request and possible request processing that has already excluded
   * managed objects from the scope of this sub-request.
   * @return
   *    a <code>MOScope</code> instance.
   */
  MOScope getScope();

  /**
   * Returns the variable binding associated with the sub-request. In order to
   * process a sub-request this variable binding needs to be modified (if not
   * an error condition prevents that).
   * @return
   *    the <code>VariableBinding</code> that holds the sub-request result
   *    or operation parameter (in case of a SET request).
   */
  VariableBinding getVariableBinding();

  /**
   * Gets the request this sub-request belongs to.
   * @return
   *    a Request instance.
   */
  Request getRequest();

  /**
   * Gets the undo value object associated with this sub-request.
   * @return
   *    an Object that has previously been associated with this request by
   *    calling {@link #setUndoValue}.
   */
  Object getUndoValue();

  /**
   * Associates an undo value object with this sub-request. The undo
   * value is internally used by the SNMP4J-Agent API and should therefore
   * not be altered externally. One exception to this rule is an implementation
   * of the {@link ManagedObject} interface that does not extend any other
   * API class.
   *
   * @param undoInformation
   *    an object that represents/contains all necessary information to undo
   *    this sub-request.
   */
  void setUndoValue(Object undoInformation);

  /**
   * Marks the sub-request as completed. This is a shortcut for
   * calling {@link #getStatus()} and then
   * {@link RequestStatus#setPhaseComplete} to <code>true</code>.
   */
  void completed();

  /**
   * Checks whether the sub-request has been completed and needs no further
   * processing.
   * @return
   *    <code>true</code> if the sub-request has been finished and should not
   *    be processed any more.
   */
  boolean isComplete();

  /**
   * Sets the <code>ManagedObject</code> that is determined as the target object
   * of this sub-request by the agent framework. For SET requests the target
   * managed object is locked and referenced here to make sure that all locks
   * are released when a request is answered. In addition, SET requests are
   * processed in multiple phases and referencing the target managed objects
   * increases performance.
   *
   * @param managedObject
   *    the <code>ManagedObject</code> responsible for processing this sub-
   *    request.
   */
  void setTargetMO(ManagedObject managedObject);

  /**
   * Gets the <code>ManagedObject</code> that is responsible for processing
   * this sub-request.
   *
   * @return
   *    <code>ManagedObject</code> instance.
   */
  ManagedObject getTargetMO();

  /**
   * Returns the index of this subrequest in the request.
   * @return
   *    the zero based index.
   */
  int getIndex();

  /**
   * Sets the query associated with this subrequest. The query is not used
   * by the request itself but may be stored here for further reference
   * while processing this sub-requests.
   *
   * @param query
   *    a <code>MOQuery</code> instance representing the query resulting from
   *    this sub-request.
   */
  void setQuery(MOQuery query);

  /**
   * Gets the query previously associated with this sub-request. A sub-request
   * is associated during requests processing with a instrumentation query.
   * @return
   *    a <code>MOQuery</code> that describes which manage objects match
   *    this sub-request.
   */
  MOQuery getQuery();

  /**
   * Returns an iterator on the repetitions of this sub-request. On requests
   * other than GETBULK requests this method returns an empty iterator.
   * @return
   *    a SubRequestIterator enumerating the repetitions on this sub-requests
   *    starting with this sub-request.
   */
  SubRequestIterator<? extends SubRequest> repetitions();

  /**
   * Updates the next repetition's scope and reset any previously set query
   * to <code>null</code>. The scope of the next repetition is updated
   * according to the value of this variable binding. If this sub-request
   * has an error status or exception value, the following repetitions are
   * set to the same value and exception. Otherwise, the scope of the following
   * sub-request is the open interval from this sub-request's OID
   * (not-including) to any OID value.
   */
  void updateNextRepetition();

  /**
   * Gets the user object that has previously associated with this sub-request.
   *
   * @return
   *    an object.
   * @since 1.0.1
   */
  Object getUserObject();

  /**
   * Sets the user object. The user object can be used to associate resources
   * or any other type of information necessary for a managed object instance
   * to process a SNMP request. When the request is processed, this reference
   * will be set to <code>null</code>.
   *
   * @param userObject
   *    an object that is not processed or interpreted by the agent API.
   * @since 1.0.1
   */
  void setUserObject(Object userObject);
}
