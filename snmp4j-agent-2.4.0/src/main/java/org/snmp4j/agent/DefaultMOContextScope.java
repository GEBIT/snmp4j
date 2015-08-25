/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOContextScope.java  
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

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;

/**
 * The <code>DefaultMOContextScope</code> is the default implementation of
 * a {@link MOContextScope} representing an OID scope that distinguishes between
 * different contexts.
 *
 * @author Frank Fock
 * @version 1.1
 */
public class DefaultMOContextScope
    extends DefaultMOScope implements MOContextScope
{

  private OctetString context;

  /**
   * Creates a context scope from a context, upper, and lower bound OID.
   * @param context
   *    the context for which this scope is valid.
   * @param lowerBound
   *    the lower bound of the OID scope (must not be <code>null</code>).
   * @param lowerIncluded
   *    specifies whether the lower bound is included or not.
   * @param upperBound
   *    the upper bound of the OID scope (<code>null</code> for no upper limit).
   * @param upperIncluded
   *    specifies whether the upper bound is included or not.
   */
  public DefaultMOContextScope(OctetString context,
                               OID lowerBound, boolean lowerIncluded,
                               OID upperBound, boolean upperIncluded) {
    super(lowerBound, lowerIncluded, upperBound, upperIncluded);
    this.context = context;
  }

  /**
   * Creates a context scope from another context scope.
   * @param scope
   *    a MOContextScope instance whose context and bounds are copied by
   *    reference.
   */
  public DefaultMOContextScope(MOContextScope scope) {
    super(scope.getLowerBound(), scope.isLowerIncluded(), scope.getUpperBound(),
          scope.isUpperIncluded());
    this.context = scope.getContext();
  }

  /**
   * Creates a context scope from a plain OID scope.
   * @param context
   *    the context name for the new context scope.
   * @param extendedScope
   *    the OID scope that defines the OID range of the new scope (boundaries
   *    are copied by reference).
   */
  public DefaultMOContextScope(OctetString context,
                               MOScope extendedScope) {
    super(extendedScope);
    this.context = context;
  }

  /**
   * Gets the context of the scope.
   * @return
   *    the context name this scope applies to.
   */
  public OctetString getContext() {
    return context;
  }

  /**
   * Sets the context name for this scope.
   * @param context
   *    a context name.
   */
  public void setContext(OctetString context) {
    this.context = context;
  }

  /**
   * Indicates whether an object is equal to this one.
   * @param obj
   *    some object.
   * @return
   *    <code>true</code> only if <code>obj</code> is a {@link MOContextScope}
   *    and if context and scope equals this one's.
   */
  public boolean equals(Object obj) {
    if (obj instanceof MOContextScope) {
      MOContextScope other = (MOContextScope)obj;
      return (context.equals(other.getContext()) && super.equals(obj));
    }
    return false;
  }

  public int hashCode() {
    if (context != null) {
      int hash = super.hashCode();
      // One-at-a-time Hash adapted from Bob Jenkins
      for (int i = 0; i < context.length(); i++) {
          hash += context.get(i);
          hash += (hash << 10);
          hash ^= (hash >> 6);
      }
      hash += (hash << 3);
      hash ^= (hash >> 11);
      hash += (hash << 15);
      return hash;
    }
    return super.hashCode();
  }

  /**
   * Indicates whether the given scopes have a matching context. The context
   * does not match if both are {@link MOContextScope} instances and both
   * contexts are not <code>null</code> and different.
   * @param a
   *    a MOScope instance.
   * @param b
   *    another MOScope instance.
   * @return
   *    <code>true</code> if both scopes have matching contexts (or at least one
   *    has no context defined).
   * @since 1.1
   */
  public static boolean isContextMatching(MOScope a, MOScope b) {
    if (a instanceof MOContextScope) {
      OctetString ca = ((MOContextScope)a).getContext();
      if (b instanceof MOContextScope) {
         OctetString cb = ((MOContextScope)b).getContext();
         if ((ca != null) && (!ca.equals(cb))) {
           return false;
         }
      }
    }
    return true;
  }

  public boolean isCovered(MOScope other) {
    if ((context != null) && (other instanceof MOContextScope)) {
      if (!context.equals(((MOContextScope)other).getContext())) {
        return false;
      }
    }
    return covers(this, other);
  }

  public String toString() {
    return getClass().getName()+"[context="+context+
        ",lowerBound="+lowerBound+
        ",lowerIncluded="+lowerIncluded+
        ",upperBound="+upperBound+
        ",upperIncluded="+upperIncluded+"]";
  }

  public boolean isOverlapping(MOScope other) {
    if (!isContextMatching(this, other)) {
      return false;
    }
    return super.isOverlapping(other);
  }

}
