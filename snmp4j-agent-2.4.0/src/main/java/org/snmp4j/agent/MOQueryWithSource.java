/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOQueryWithSource.java  
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

// For JavaDoc
import org.snmp4j.agent.request.Request;

/**
 * The <code>MOQueryWithSource</code> class is the implementation of a
 * managed object query for a {@link Request}. It is used to lookup managed
 * objects, for example in a {@link org.snmp4j.agent.MOServer} repository.
 *
 * @author Frank Fock
 * @version 2.0
 * @since 2.0
 */
public class MOQueryWithSource extends DefaultMOQuery {

  private Object source;

  /**
   * Creates a context aware query from a context aware OID scope.
   * @param scope
   *    a scope that defines the possible result set of OIDs from a specific
   *    context for this query.
   * @param isWriteAccessIntended
   *    indicates whether this query serves a write access on
   *    {@link org.snmp4j.agent.ManagedObject}s or not.
   * @param source
   *    the <code>Request</code> on whose behalf this query is being
   *    executed.
   * @since 2.0
   */
  public MOQueryWithSource(MOContextScope scope, boolean isWriteAccessIntended,
                           Object source) {
    super(scope, isWriteAccessIntended);
    this.source = source;
  }

  /**
   * Gets the source ({@link org.snmp4j.agent.request.Request}) object on whose behalf this query is
   * executed. This object reference can be used to determine whether a query
   * needs to update {@link org.snmp4j.agent.ManagedObject} content or not. When the reference
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
   * This method checks whether the supplied query and the given source
   * reference refer to the same source (request).
   *
   * @param query
   *    a <code>MOQuery</code> instance.
   * @param source
   *    any source object reference.
   * @return
   *    <code>true</code> only if <code>query</code> is a
   *    <code>DefaultMOQuery</code> instance and
   *    <code>{@link MOQueryWithSource#getSource()} == source</code> and source
   *    is not <code>null</code>.
   * @since 1.1
   */
  public static boolean isSameSource(MOQuery query, Object source) {
    if (query instanceof MOQueryWithSource) {
      return ((source != null) &&
              (((MOQueryWithSource)query).getSource() == source));
    }
    return false;
  }
}
