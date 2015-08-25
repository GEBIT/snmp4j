/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - StaticMOGroup.java  
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

package org.snmp4j.agent.mo.ext;

import org.snmp4j.agent.*;
import org.snmp4j.smi.*;
import org.snmp4j.agent.request.SubRequest;
import java.util.SortedMap;
import java.util.TreeMap;
import org.snmp4j.PDU;

import java.util.Iterator;

/**
 * The <code>StaticMOGroup</code> can be used to easily implement static
 * (read-only) managed objects.
 * <p>
 * Note: Dynamic variables (see {@link Variable#isDynamic}) cannot be used to
 * when using default {@link VariableBinding}s since {@link Variable}s are
 * cloned when added to them. In order to use dynamic objects (i.e., objects
 * that may change their value when being accessed), a sub-class of
 * {@link VariableBinding} needs to be used that overwrites its
 * {@link VariableBinding#setVariable} method.
 * </p>
 *
 * @author Frank Fock
 * @version 1.2
 * @since 1.2
 */
public class StaticMOGroup implements ManagedObject, MOGroup {

  private SortedMap<OID, Variable> vbs = new TreeMap<OID, Variable>();
  private OID root;
  private MOScope scope;

  /**
   * Creates a static managed object group for the sub-tree with the specified
   * root OID.
   * @param root
   *    the root OID of the sub-tree to be registered by this managed object.
   * @param vbs
   *    the variable bindings to be returned in this sub-tree.
   */
  public StaticMOGroup(OID root, VariableBinding[] vbs) {
    this.root = root;
    this.scope = new DefaultMOScope(root, true, root.nextPeer(), false);
    for (VariableBinding vb : vbs) {
      if ((vb.getOid() != null) && (vb.getVariable() != null)) {
        if ((vb.getOid().size() >= root.size()) &&
            (vb.getOid().leftMostCompare(root.size(), root) == 0)) {
          this.vbs.put(vb.getOid(), vb.getVariable());
        }
      }
    }
  }

  public void registerMOs(MOServer server, OctetString context) throws
      DuplicateRegistrationException {
    server.register(this, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    server.unregister(this, context);
  }

  public MOScope getScope() {
    return scope;
  }

  public OID find(MOScope range) {
    SortedMap<OID, Variable> tail = vbs.tailMap(range.getLowerBound());
    OID first = tail.firstKey();
    if (range.getLowerBound().equals(first) && (!range.isLowerIncluded())) {
      if (tail.size() > 1) {
        Iterator<OID> it = tail.keySet().iterator();
        it.next();
        return it.next();
      }
    }
    else {
      return first;
    }
    return null;
  }

  public void get(SubRequest request) {

    OID oid = request.getVariableBinding().getOid();
    Variable vb = vbs.get(oid);
    if (vb == null) {
      request.getVariableBinding().setVariable(Null.noSuchInstance);
    }
    else {
      request.getVariableBinding().setVariable(vb);
    }
    request.completed();
  }

  public boolean next(SubRequest request) {
    MOScope scope = request.getQuery().getScope();
    SortedMap<OID, Variable> tail = vbs.tailMap(scope.getLowerBound());
    OID first = tail.firstKey();
    if (scope.getLowerBound().equals(first) && (!scope.isLowerIncluded())) {
      if (tail.size() > 1) {
        Iterator<OID> it = tail.keySet().iterator();
        it.next();
        first = it.next();
      }
      else {
        return false;
      }
    }
    if (first != null) {
      Variable vb = vbs.get(first);
      if (vb == null) {
        request.getVariableBinding().setVariable(Null.noSuchInstance);
      }
      request.getVariableBinding().setOid(first);
      request.getVariableBinding().setVariable(vb);
      request.completed();
      return true;
    }
    return false;
  }

  /**
   * Sets the error status of the request to {@link PDU#notWritable}.
   * @param request
   *    a request to process prepare SET request for.
   */
  public void prepare(SubRequest request) {
    request.setErrorStatus(PDU.notWritable);
  }

  /**
   * Sets the error status of the request to {@link PDU#commitFailed}.
   * @param request
   *    a request to process commit SET request for.
   */
  public void commit(SubRequest request) {
    request.setErrorStatus(PDU.commitFailed);
  }

  /**
   * Sets the error status of the request to {@link PDU#undoFailed}.
   * @param request
   *    a request to process undo SET request for.
   */
  public void undo(SubRequest request) {
    request.setErrorStatus(PDU.undoFailed);
  }

  public void cleanup(SubRequest request) {
  }

  public String toString() {
    return "StaticMOGroup[root="+root+",vbs="+vbs+"]";
  }
}
