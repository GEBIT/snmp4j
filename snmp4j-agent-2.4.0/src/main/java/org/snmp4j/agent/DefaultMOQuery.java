/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOQuery.java  
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

package org.snmp4j.agent;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 * The <code>DefaultMOQuery</code> class is the default implementation of a
 * managed object query. It is used to lookup managed objects, for example in
 * a {@link MOServer} repository.
 * The constructor with source object reference has been pushed down into
 * the new subclass {@link MOQueryWithSource} (since version 2.0).
 *
 * @author Frank Fock
 * @version 2.0
 */
public class DefaultMOQuery implements MOQuery {

  private MOContextScope scope;
  private boolean writeAccessQuery;
  private Object source;

  /**
   * Creates a context aware query from a context aware OID scope.
   * @param scope
   *    a scope that defines the possible result set of OIDs from a specific
   *    context for this query.
   */
  public DefaultMOQuery(MOContextScope scope) {
    this.scope = scope;
  }

  /**
   * Creates a context aware query from a context aware OID scope.
   * @param scope
   *    a scope that defines the possible result set of OIDs from a specific
   *    context for this query.
   * @param isWriteAccessIntended
   *    indicates whether this query serves a write access on
   *    {@link ManagedObject}s or not.
   * @since 1.1
   */
  public DefaultMOQuery(MOContextScope scope, boolean isWriteAccessIntended) {
    this(scope);
    this.writeAccessQuery = isWriteAccessIntended;
  }

  /**
   * Creates a context aware query from a context aware OID scope.
   * @param scope
   *    a scope that defines the possible result set of OIDs from a specific
   *    context for this query.
   * @param isWriteAccessIntended
   *    indicates whether this query serves a write access on
   *    {@link ManagedObject}s or not.
   * @since 1.1
   */
  public DefaultMOQuery(MOContextScope scope, boolean isWriteAccessIntended,
                        Object source) {
    this(scope, isWriteAccessIntended);
    this.source = source;
  }

  /**
   * Gets the search range of this query.
   *
   * @return a <code>MORange</code> instance denoting upper and lower bound of
   *   this queries scope.
   */
  public MOContextScope getScope() {
    return scope;
  }

  /**
   * Gets the source ({@link org.snmp4j.agent.request.Request}) object on whose behalf this query is
   * executed. This object reference can be used to determine whether a query
   * needs to update {@link ManagedObject} content or not. When the reference
   * is the same as those from the last query then an update is not necessary.
   *
   * @return
   *    an Object on whose behalf this query is executed which will be in most
   *    cases a {@link org.snmp4j.agent.request.Request} instance, but code should not rely on that. If
   *    <code>null</code> is returned, the query source cannot be determined.
   * @since 1.1
   */
  public Object getSource() {
    return source;
  }

  /**
   * Checks whether a managed object matches the internal query criteria
   * defined by this query.
   *
   * @param managedObject the <code>ManagedObject</code> instance to check.
   * @return <code>true</code> if the <code>managedObject</code> matches the
   *   query.
   */
  public boolean matchesQuery(ManagedObject managedObject) {
    return true;
  }

  public void substractScope(MOScope scope) {
    if (this.scope instanceof MutableMOScope) {
      ((MutableMOScope)this.scope).substractScope(scope);
    }
    else {
      throw new UnsupportedOperationException();
    }
  }

  public String toString() {
    return getClass().getName()+"["+getScope().getContext()+"]="+
        getScope().getLowerBound()+"<"+
        (getScope().isLowerIncluded() ? "=" : "")+" x <"+
        (getScope().isUpperIncluded() ? "=" : "")+
        getScope().getUpperBound();
  }

  public boolean isWriteAccessQuery() {
    return writeAccessQuery;
  }

  @Override
  public OID getLowerBound() {
    return scope.getLowerBound();
  }

  @Override
  public OID getUpperBound() {
    return scope.getUpperBound();
  }

  @Override
  public boolean isLowerIncluded() {
    return scope.isLowerIncluded();
  }

  @Override
  public boolean isUpperIncluded() {
    return scope.isUpperIncluded();
  }

  @Override
  public boolean isCovered(MOScope other) {
    return scope.isCovered(other);
  }

  @Override
  public boolean isOverlapping(MOScope other) {
    return scope.isOverlapping(other);
  }

  @Override
  public boolean covers(OID oid) {
    return scope.covers(oid);
  }

  @Override
  public OctetString getContext() {
    return scope.getContext();
  }
}
