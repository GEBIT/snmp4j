/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOQuery.java  
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

/**
 * A managed object query is used to lookup managed objects, for example in
 * a {@link MOServer} repository.
 *
 * @author Frank Fock
 * @version 1.1
 */
public interface MOQuery extends MOContextScope {

  /**
   * Gets the search range of this query.
   * @return
   *    a <code>MORange</code> instance denoting upper and lower bound
   *    of this queries scope.
   */
  MOContextScope getScope();

  /**
   * Checks whether a managed object matches the internal query criteria
   * defined by this query.
   *
   * @param managedObject
   *    the <code>ManagedObject</code> instance to check.
   * @return
   *    <code>true</code> if the <code>managedObject</code> matches the query.
   */
  boolean matchesQuery(ManagedObject managedObject);

  /**
   * Changes the query to no longer match (cover) the specified scope.
   * If the query's scope is immutable (does not implement the
   * {@link MutableMOScope} interface, then this method will throw an
   * {@link UnsupportedOperationException}.
   * @param scope
   *    a <code>MOScope</code> instance that defines the range of OIDs
   *    that should be no longer in the scope of this query.
   */
  void substractScope(MOScope scope);

  /**
   * Indicates whether this query is issued on behalf of an intended write
   * access on the ManagedObjects matched by this query. This information can
   * be used to optimize query evaluation or to control resource allocation.
   *
   * @return
   *    <code>true</code> if this query is performed to change or create a
   *    managed object matching this query and <code>false</code> if the
   *    query is for read-only access on the matched managed objects.
   * @since 1.1
   */
  boolean isWriteAccessQuery();
}
