/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOScalar.java  
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


package org.snmp4j.agent.mo;

import java.io.*;
import java.util.*;

import org.snmp4j.agent.*;
import org.snmp4j.agent.io.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.log.LogFactory;

/**
 * The <code>MOScalar</code> class represents scalar SNMP managed objects.
 *
 * @author Frank Fock
 * @version 1.4
 */
public class MOScalar<V extends Variable> implements ManagedObject, MOScope,
    SerializableManagedObject, ManagedObjectValueAccess {

  private static LogAdapter logger = LogFactory.getLogger(MOScalar.class);

  private OID oid;
  private volatile OID lowerBound;
  private volatile OID upperBound;
  private V value;
  private MOAccess access;
  private boolean isVolatile;
  private transient Vector<MOValueValidationListener> moValueValidationListeners;
  private transient Vector<MOChangeListener> moChangeListeners;

  /**
   * Creates a scalar MO instance with OID, maximum access level and initial
   * value.
   * @param id
   *    the instance OID of the scalar instance (last sub-identifier should be
   *    zero).
   * @param access
   *    the maximum access level supported by this instance.
   * @param value
   *    the initial value of the scalar instance. If the initial value is
   *    <code>null</code> or a Counter syntax, the scalar is created as a
   *    volatile (non-persistent) instance by default.
   */
  public MOScalar(OID id, MOAccess access, V value) {
    this.oid = id;
    this.access = access;
    this.value = value;
    this.isVolatile = isVolatileByDefault(value);
  }

  private static boolean isVolatileByDefault(Variable value) {
    if (value == null) {
      return true;
    }
    switch (value.getSyntax()) {
      case SMIConstants.SYNTAX_COUNTER32:
      case SMIConstants.SYNTAX_COUNTER64: {
        return true;
      }
      default:
        return false;
    }
  }

  /**
   * Returns the scope of OIDs that are covered by this scalar's object
   * registration. This range is
   * <code>1.3.6...n</code> <= x <  <code>1.3.6...n+1</code> where n is the
   * last subidentifier of the OID registered by the corresponding OBJECT-TYPE
   * definition. Prior to version 1.1.2, this method returned a scope equal
   * to the scope now returned by {@link #getSingleInstanceScope()}.
   *
   * @return
   *    a MOScope that covers the OIDs by this scalar object registration.
   */
  public MOScope getScope() {
    return this;
  }

  /**
   * Returns a scope that covers only the scalar instance itself without any
   * possible OIDs down in the tree or at the same level.
   * @return
   *    a scope that covers exactly the OID of this scalar.
   * @since 1.1.2
   */
  public MOScope getSingleInstanceScope() {
    return new DefaultMOScope(oid, true, oid, true);
  }

  public OID find(MOScope range) {
    if (access.isAccessibleForRead() &&
        range.isCovered(getSingleInstanceScope())) {
      if (logger.isDebugEnabled()) {
        logger.debug("MOScalar '"+oid+"' is in scope '"+range+"'");
      }
      return oid;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("MOScalar '"+oid+"' is not in scope '"+range+"'");
    }
    return null;
  }

  public void get(SubRequest request) {
    RequestStatus status = request.getStatus();
    if (checkRequestScope(request)) {
      if (access.isAccessibleForRead()) {
        VariableBinding vb = request.getVariableBinding();
        vb.setOid(getOid());
        vb.setVariable((Variable) getValue().clone());
        request.completed();
      }
      else {
        status.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_ACCESS);
      }
    }
  }

  /**
   * Gets the access object for this scalar.
   * @return
   *   the access instance associated with this scalar.
   * @since 1.2
   */
  public MOAccess getAccess() {
    return access;
  }

  /**
   * Checks whether the request is within the scope of this scalar or not.
   * @param request
   *    a SubRequest.
   * @return
   *    <code>true</code> if the request is within scope and <code>false</code>
   *    otherwise. In the latter case, the variable of the request is set
   *    to {@link Null#noSuchInstance} and the request is marked completed.
   */
  protected boolean checkRequestScope(SubRequest request) {
    if (!request.getVariableBinding().getOid().equals(oid)) {
      VariableBinding vb = request.getVariableBinding();
      vb.setOid(getOid());
      vb.setVariable(Null.noSuchInstance);
      request.completed();
      return false;
    }
    return true;
  }

  public boolean next(SubRequest request) {
    if (access.isAccessibleForRead() &&
        (request.getScope().isCovered(getSingleInstanceScope()))) {
      VariableBinding vb = request.getVariableBinding();
      vb.setOid(getOid());
      Variable variable = getValue();
      if (variable == null) {
        vb.setVariable(Null.noSuchObject);
      }
      else {
        vb.setVariable((Variable) variable.clone());
      }
      request.completed();
      if (logger.isDebugEnabled()) {
        logger.debug("Processed GETNEXT/BULK request '"+request+"' by '"+
                     getOid());
      }
      return true;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Skipped '"+
                   getOid()+"' for GETNEXT/BULK request '"+request+"'");
    }
    return false;
  }

  /**
   * Checks whether the new value contained in the supplied sub-request is a
   * valid value for this object. The checks are performed by firing a
   * {@link MOValueValidationEvent} the registered listeners.
   *
   * @param request
   *    the <code>SubRequest</code> with the new value.
   * @return
   *    {@link SnmpConstants#SNMP_ERROR_SUCCESS} if the new value is OK,
   *    any other appropriate SNMPv2/v3 error status if not.
   */
  public int isValueOK(SubRequest request) {
    if (moValueValidationListeners != null) {
      Variable oldValue = value;
      Variable newValue =
           request.getVariableBinding().getVariable();
      MOValueValidationEvent event =
          new MOValueValidationEvent(this, oldValue, newValue);
      fireValidate(event);
      return event.getValidationStatus();
    }
    return SnmpConstants.SNMP_ERROR_SUCCESS;
  }

  public void prepare(SubRequest request) {
    RequestStatus status = request.getStatus();
    if (oid.equals(request.getVariableBinding().getOid())) {
      if (access.isAccessibleForWrite()) {
        VariableBinding vb = request.getVariableBinding();
        if (vb.getVariable().getSyntax() != getValue().getSyntax()) {
          status.setErrorStatus(SnmpConstants.SNMP_ERROR_WRONG_TYPE);
          return;
        }
        if (moChangeListeners != null) {
          MOChangeEvent event =
              new MOChangeEvent(this, this,
                                request.getVariableBinding().getOid(),
                                getValue(),
                                request.getVariableBinding().getVariable(),
                                true);
          fireBeforePrepareMOChange(event);
          if (event.getDenyReason() != SnmpConstants.SNMP_ERROR_SUCCESS) {
            status.setErrorStatus(event.getDenyReason());
            status.setPhaseComplete(true);
            return;
          }
        }
        int valueOK = isValueOK(request);
        if ((moChangeListeners != null) &&
            (valueOK == SnmpConstants.SNMP_ERROR_SUCCESS)) {
          MOChangeEvent event =
              new MOChangeEvent(this, this,
                                request.getVariableBinding().getOid(),
                                getValue(),
                                request.getVariableBinding().getVariable(),
                                true);
          fireAfterPrepareMOChange(event);
          valueOK = event.getDenyReason();
        }
        status.setErrorStatus(valueOK);
        status.setPhaseComplete(true);
      }
      else {
        status.setErrorStatus(SnmpConstants.SNMP_ERROR_NOT_WRITEABLE);
      }
    }
    else {
      status.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_CREATION);
    }
  }

  @SuppressWarnings("unchecked")
  public void commit(SubRequest request) {
    RequestStatus status = request.getStatus();
    VariableBinding vb = request.getVariableBinding();
    if (moChangeListeners != null) {
      MOChangeEvent event =
          new MOChangeEvent(this, this, vb.getOid(), getValue(),
                            vb.getVariable(), false);
      fireBeforeMOChange(event);
    }
    request.setUndoValue(getValue());
    changeValue((V)vb.getVariable());
    status.setPhaseComplete(true);
    if (moChangeListeners != null) {
      MOChangeEvent event =
          new MOChangeEvent(this, this, request.getVariableBinding().getOid(),
                            (Variable)request.getUndoValue(),
                            vb.getVariable(), false);
      fireAfterMOChange(event);
    }
  }

  /**
   * Changes the value of this scalar on behalf of a commit or undo operation.
   * Overwrite this method for easy and simple instrumentation. By default
   * {@link #setValue(Variable value)} is called.
   * @param value
   *    the new value.
   * @return
   *    a SNMP error status if the operation failed (should be avoided).
   * @since 1.2
   */
  protected int changeValue(V value) {
    return setValue(value);
  }

  @SuppressWarnings("unchecked")
  public void undo(SubRequest request) {
    RequestStatus status = request.getStatus();
    if ((request.getUndoValue() != null) &&
        (request.getUndoValue() instanceof Variable)) {
      int errorStatus = changeValue((V)request.getUndoValue());
      status.setErrorStatus(errorStatus);
      status.setPhaseComplete(true);
    }
    else {
      status.setErrorStatus(SnmpConstants.SNMP_ERROR_UNDO_FAILED);
    }
  }

  public void cleanup(SubRequest request) {
    request.setUndoValue(null);
    request.getStatus().setPhaseComplete(true);
  }

  /**
   * Gets the instance OID of this scalar managed object.
   * @return
   *    the instance OID (by reference).
   */
  public OID getOid() {
    return oid;
  }

  @Override
  public OID getLowerBound() {
    if (lowerBound == null) {
      lowerBound = new OID(oid.getValue(), 0, oid.size()-1);
    }
    return lowerBound;
  }

  @Override
  public OID getUpperBound() {
    if (upperBound == null) {
      upperBound = new OID(getLowerBound().nextPeer());
    }
    return upperBound;
  }

  @Override
  public boolean isCovered(MOScope other) {
    return (other.getLowerBound().startsWith(oid) &&
            (other.getLowerBound().size() > oid.size() ||
             other.isLowerIncluded())) &&
            (other.getUpperBound().startsWith(oid) &&
             ((other.getUpperBound().size() > oid.size()) ||
              other.isUpperIncluded()));
  }

  @Override
  public boolean isLowerIncluded() {
    return true;
  }

  @Override
  public boolean isUpperIncluded() {
    return false;
  }

  /**
   * Returns the actual value of this scalar managed object. For a basic
   * instrumentation, overwrite this method to provide always the actual
   * value and/or to update the internal <code>value</code> member and
   * then call <code>super.</code>{@link #getValue()} in the derived class.
   *
   * @return
   *    a non <code>null</code> Variable with the same syntax defined for
   *    this scalar object.
   */
  public V getValue() {
    return value;
  }

  @Override
  public boolean isVolatile() {
    return isVolatile;
  }

  /**
   * Sets the value of this scalar managed object without checking it for
   * the correct syntax.
   * @param value
   *    a Variable with the with the same syntax defined for
   *    this scalar object (not checked).
   * @return
   *    a SNMP error code (zero indicating success by default).
   */
  public int setValue(V value) {
    this.value = value;
    return SnmpConstants.SNMP_ERROR_SUCCESS;
  }

  /**
   * Sets the volatile flag for this instance.
   * @param isVolatile
   *    if <code>true</code> the state of this object will not be persistently
   *    stored, otherwise the agent may save the state of this object
   *    persistently.
   */
  public void setVolatile(boolean isVolatile) {
    this.isVolatile = isVolatile;
  }

  @Override
  public boolean isOverlapping(MOScope other) {
    return DefaultMOScope.overlaps(this, other);
  }

  /**
   * Adds a value validation listener to check new values.
   * @param l
   *   a <code>MOValueValidationListener</code> instance.
   */
  public synchronized void addMOValueValidationListener(
      MOValueValidationListener l) {
      if (moValueValidationListeners == null) {
        moValueValidationListeners = new Vector<MOValueValidationListener>(2);
      }
      moValueValidationListeners.add(l);
  }

  /**
   * Removes a value validation listener
   * @param l
   *    a <code>MOValueValidationListener</code> instance.
   */
  public synchronized void removeMOValueValidationListener(
      MOValueValidationListener l) {
      if (moValueValidationListeners != null) {
        moValueValidationListeners.remove(l);
      }
  }

  protected void fireValidate(MOValueValidationEvent validationEvent) {
    if (moValueValidationListeners != null) {
      Vector<MOValueValidationListener> listeners = moValueValidationListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        (listeners.elementAt(i)).validate(
            validationEvent);
      }
    }
  }

  @Override
  public OID getID() {
    return getOid();
  }

  @SuppressWarnings("unchecked")
  public synchronized void load(MOInput input) throws IOException {
    Variable v = input.readVariable();
    setValue((V)v);
  }

  public synchronized void save(MOOutput output) throws IOException {
    output.writeVariable(value);
  }

  public boolean covers(OID oid) {
    return oid.startsWith(this.oid);
  }

  public String toString() {
    return getClass().getName()+"[oid="+getOid()+",access="+access+
        ",value="+getValue()+
        ",volatile="+isVolatile()+toStringDetails()+"]";
  }

  protected String toStringDetails() {
    return "";
  }

  /**
   * Adds a <code>MOChangeListener</code> that needs to be informed about
   * state changes of this scalar.
   * @param l
   *    a <code>MOChangeListener</code> instance.
   * @since 1.1
   */
  public synchronized void addMOChangeListener(MOChangeListener l) {
    if (moChangeListeners == null) {
      moChangeListeners = new Vector<MOChangeListener>(2);
    }
    moChangeListeners.add(l);
  }

  /**
   * Removes a <code>MOChangeListener</code>.
   * @param l
   *    a <code>MOChangeListener</code> instance.
   * @since 1.1
   */
  public synchronized void removeMOChangeListener(MOChangeListener l) {
    if (moChangeListeners != null) {
      moChangeListeners.remove(l);
    }
  }

  protected void fireBeforePrepareMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      for (MOChangeListener listener : listeners) {
        listener.beforePrepareMOChange(
            changeEvent);
      }
    }
  }

  protected void fireAfterPrepareMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      for (MOChangeListener listener : listeners) {
        listener.afterPrepareMOChange(changeEvent);
      }
    }
  }

  protected void fireBeforeMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      for (MOChangeListener listener : listeners) {
        listener.beforeMOChange(changeEvent);
      }
    }
  }

  protected void fireAfterMOChange(MOChangeEvent changeEvent) {
    if (moChangeListeners != null) {
      Vector<MOChangeListener> listeners = moChangeListeners;
      for (MOChangeListener listener : listeners) {
        listener.afterMOChange(changeEvent);
      }
    }
  }

  public Variable getValue(OID instanceOID) {
    if (getOid().equals(instanceOID)) {
      return getValue();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public boolean setValue(VariableBinding newValueAndInstancceOID) {
    if (getOid().equals(newValueAndInstancceOID.getOid())) {
      return (setValue((V)newValueAndInstancceOID.getVariable()) ==
              SnmpConstants.SNMP_ERROR_SUCCESS);
    }
    return false;
  }
}
