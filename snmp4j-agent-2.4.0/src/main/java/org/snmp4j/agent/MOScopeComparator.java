/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOScopeComparator.java  
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

import java.util.Comparator;
import org.snmp4j.smi.OctetString;

/**
 * The <code>MOScopeComparator</code> compares two scopes with each other.
 * <p>
 * Two scopes are compared by their context (if both are {@link MOContextScope}
 * instances) first and then by their lower bound.
 * <p>
 * A scope is compared with a query by comparing the scope with the queries
 * scope and then if both are deemed to be equal, the upper bound of the scope
 * is checked. If it is unbounded (upper bound is <code>null</code), then
 * the scoped is deemed to be greater than the query. Otherwise, the upper bound
 * of the scope is compared with the lower bound of the query. Scope and query
 * are deemed to be equal if both bounds are equal and both are included.
 * Otherwise the scope is deemed to be less than the query.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOScopeComparator implements Comparator<MOScope> {

  public MOScopeComparator() {
  }

  /**
   * Compares a scope with another scope or query. See also the class
   * description how comparison is done.
   * @param o1
   *   a MOscope instance.
   * @param o2
   *   a MOscope instance.
   * @return
   *   an integer less than zero if <code>o1</code> is less than <code>o2</code>
   *   and zero if both values are deemed to be equal and a value greater than
   *   zero if <code>o1</code> is greater than <code>o2</code>.
   */
  public int compare(MOScope o1, MOScope o2) {
    if (o1 == o2) {
      return 0; // ensure identity is equal
    }
    int result = 0;
    if (o2 instanceof MOQuery) {
      result = compareQueryWithScope(o1, (MOQuery)o2);
    }
    else if (o1 instanceof MOQuery) {
      result = -compareQueryWithScope(o2, (MOQuery)o1);
    }
    else {
      if (o2 instanceof MOContextScope) {
        result = compareContextScope(o1, (MOContextScope) o2);
      }
      else if (o1 instanceof MOContextScope) {
        result = -compareContextScope(o2, (MOContextScope) o1);
      }
      else {
        result = compareScope(o1, o2);
      }
    }
    return result;
  }

  private static int compareContextScope(MOScope scope, MOContextScope scope2) {
    if (scope == scope2) {
      return 0;
    }
    int result = compareScope(scope, scope2);
    if ((result == 0) && (scope instanceof MOContextScope)) {
      OctetString c1 = ((MOContextScope)scope).getContext();
      OctetString c2 = scope2.getContext();
      if ((c1 != null) && (c2 != null)) {
        result = c1.compareTo(c2);
      }
    }
    return result;
  }

  private static int compareScope(MOScope scope, MOScope scope2) {
    if ((scope == scope2) || scope.equals(scope2)) {
      return 0;
    }
    int result = 0;
    if (scope.getUpperBound() == null) {
      result = 1;
      if (scope2.getUpperBound() == null) {
        result = scope.getLowerBound().compareTo(scope2.getLowerBound());
        if (result == 0) {
          result += (scope.isLowerIncluded() ? -1 : 0);
          result += (scope2.isLowerIncluded() ? 1 : 0);
        }
      }
    }
    else {
      result = scope.getUpperBound().compareTo(scope2.getUpperBound());
      if (result == 0) {
        result += (scope.isUpperIncluded() ? -1 : 0);
        result += (scope2.isUpperIncluded() ? 1 : 0);
        if (result == 0) {
          result = scope.getUpperBound().compareTo(scope2.getLowerBound());
          if (result == 0) {
            if ((!scope.isUpperIncluded()) ||
                (!scope2.isLowerIncluded())) {
              return -1;
            }
          }
        }
      }
    }
    return result;
  }

  private static int compareQueryWithScope(MOScope scope, MOQuery scope2) {
    int result = 0;
    if (scope.getUpperBound() == null) {
      return 1;
    }
    else {
      result = scope.getUpperBound().compareTo(scope2.getLowerBound());
      if (result == 0) {
        if ((!scope.isUpperIncluded()) ||
            (!scope2.isLowerIncluded())) {
          return -1;
        }
      }
    }
    if (result == 0) {
      if (scope instanceof MOContextScope) {
        OctetString c1 = ((MOContextScope)scope).getContext();
        OctetString c2 = scope2.getContext();
        if ((c1 != null) && (c2 != null)) {
          result = c1.compareTo(c2);
        }
      }
    }
    return result;
  }

  /**
   * Indicates whether the given query's context matches the context of the given
   * scope.
   * @param a
   *    a MOQuery instance.
   * @param b
   *    another MOScope instance.
   * @return
   *    <code>true</code> if the query's context is <tt>null</tt> or if both contexts
   *    match or if the context of <tt>scope</tt> is the empty string.
   * @since 2.0.2
   */
  public static boolean isQueryContextMatching(MOQuery a, MOScope b) {
    OctetString ca = a.getContext();
    if (ca == null) {
      return true;
    }
    if (b instanceof MOContextScope) {
      OctetString cb = ((MOContextScope)b).getContext();
      if (!ca.equals(cb)) {
        return false;
      }
    }
    return true;
  }


  public boolean equals(Object obj) {
    if (obj instanceof MOScopeComparator) {
      return (this == obj);
    }
    return false;
  }

  public int hashCode() {
    return super.hashCode();
  }
}
