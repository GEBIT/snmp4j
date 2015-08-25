/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SubRequestIteratorSupport.java  
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

/**
 * This support class allows to implement a <code>SubRequestIterator</code>
 * instance based on an <code>Iterator</code> that iterates on
 * {@link SubRequest} instances.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class SubRequestIteratorSupport<S extends SubRequest> implements SubRequestIterator<S> {

  private Iterator subRequests;

  /**
   * Creates a <code>SubRequestIterator</code> that decorates an
   * <code>Iterator</code>.
   *
   * @param subRequests
   *    an <code>Iterator</code> on {@link SubRequest} instances or instances
   *    of other objects if {@link #mapToSubRequest(Object element)} is
   *    implemented (overwritten) accordingly.
   */
  public SubRequestIteratorSupport(Iterator subRequests) {
    this.subRequests = subRequests;
  }

  public boolean hasNext() {
    return subRequests.hasNext();
  }

  public S next() {
    return mapToSubRequest(subRequests.next());
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the <code>SubRequest</code> contained or represented by the
   * supplied object (element of the iterator). The default implementation
   * simply casts the supplied object to <code>SubRequest</code>.
   *
   * @param element
   *    an Object from which a <code>SubRequest</code> can be deduced.
   * @return
   *    a <code>SubRequest</code> instance.
   */
  @SuppressWarnings("unchecked")
  protected S mapToSubRequest(Object element) {
    return (S)element;
  }
}
