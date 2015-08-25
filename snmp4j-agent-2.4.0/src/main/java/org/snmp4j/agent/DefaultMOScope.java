/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOScope.java  
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

import org.snmp4j.smi.*;

/**
 * The <code>DefaultMOScope</code> is the default {@link MOScope} and
 * {@link MutableMOScope} implementation.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class DefaultMOScope implements MOScope, MutableMOScope {

  protected OID lowerBound;
  protected OID upperBound;
  protected boolean lowerIncluded;
  protected boolean upperIncluded;

  /**
   * Creates an OID scope from lower and upper bound.
   * @param lowerBound
   *    the lower bound of the scope.
   * @param lowerIncluded
   *    indicates whether the lower bound is included in the scope or not.
   * @param upperBound
   *    the upper bound of the scope, <code>null</code> can be specified to
   *    set no upper limit.
   * @param upperIncluded
   *    indicates whether the upper bound is included in the scope or not.
   */
  public DefaultMOScope(OID lowerBound, boolean lowerIncluded,
                        OID upperBound, boolean upperIncluded) {
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.lowerIncluded = lowerIncluded;
    this.upperIncluded = upperIncluded;
  }

  /**
   * Creates a scope from another scope by referencing its bound values.
   * @param other
   *    another scope.
   */
  public DefaultMOScope(MOScope other) {
    this.lowerBound = other.getLowerBound();
    this.upperBound = other.getUpperBound();
    this.lowerIncluded = other.isLowerIncluded();
    this.upperIncluded = other.isUpperIncluded();
  }

  public OID getLowerBound() {
    return lowerBound;
  }

  public OID getUpperBound() {
    return upperBound;
  }

  public boolean isLowerIncluded() {
    return lowerIncluded;
  }

  public boolean isUpperIncluded() {
    return upperIncluded;
  }

  public boolean isCovered(MOScope other) {
    return covers(this, other);
  }

  public boolean isOverlapping(MOScope other) {
    return overlaps(this, other);
  }

  public void setLowerBound(OID lowerBound) {
    this.lowerBound = lowerBound;
  }
  public void setLowerIncluded(boolean lowerIncluded) {
    this.lowerIncluded = lowerIncluded;
  }
  public void setUpperBound(OID upperBound) {
    this.upperBound = upperBound;
  }
  public void setUpperIncluded(boolean upperIncluded) {
    this.upperIncluded = upperIncluded;
  }

  public boolean equals(Object obj) {
    if (obj instanceof MOScope) {
      MOScope other = (MOScope)obj;
      return (lowerBound.equals(other.getLowerBound()) &&
              (((upperBound == null) && (other.getUpperBound() == null)) ||
               (upperBound.equals(other.getUpperBound()))) &&
              (lowerIncluded == other.isLowerIncluded()) &&
              (upperIncluded == other.isUpperIncluded()));
    }
    return false;
  }

  public int hashCode() {
    return lowerBound.hashCode();
  }

  /**
   * Indicates whether this scope covers by the supplied one, that is whether
   * the lower bound of this scope is less or equal to the lower bound of the
   * covered scope and if the upper bound is greater or equal to the upper
   * bound of the covered scope.
   *
   * @param covered
   *    a MOScope instance.
   * @return
   *    <code>true</code> if this OID scope covers the supplied one.
   */
  public boolean covers(MOScope covered) {
    return covers(this, covered);
  }

  /**
   * Indicates whether the first supplied scope covers by second one.
   * @param scope
   *    the covering scope.
   * @param covered
   *    the covered scope.
   * @return
   *    <code>true</code> if the lower bound of <code>scope</code> is less or
   *    equal to the lower bound of <code>covered</code> and if the upper bound
   *    is greater or equal to the upper bound of <code>covered</code>.
   */
  public static boolean covers(MOScope scope, MOScope covered) {
    int lowerResult = scope.getLowerBound().compareTo(covered.getLowerBound());
    if ((lowerResult < 0) ||
        ((lowerResult == 0) && (scope.isLowerIncluded()))) {
      if (scope.getUpperBound() == null) {
        return true;
      }
      int upperResult =
          scope.getUpperBound().compareTo(covered.getLowerBound());
      if ((upperResult > 0) ||
          ((upperResult == 0) && (scope.isUpperIncluded()) &&
           (covered.isLowerIncluded()))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Indicates whether the first scope supplied overlaps with the second one.
   * If both scopes are instances of MOContextScope their context must match
   *
   * @param scope
   *    a MOScope instance.
   * @param intersected
   *    the presumable intersected MOScope.
   * @return
   *    <code>true</code> if <code>scope</code> overlaps any bound of
   *    <code>intersected</code>. This is always the case, if the upper bound
   *    of both scopes is <code>null</code>.
   */
  public static boolean overlaps(MOScope scope, MOScope intersected) {
    OID iUpper = intersected.getUpperBound();
    if (iUpper == null) {
      if (scope.getUpperBound() == null) {
        return true;
      }
      int upperResult =
          scope.getUpperBound().compareTo(intersected.getLowerBound());
      return ((upperResult > 0) ||
              ((upperResult == 0) &&
               (scope.isUpperIncluded() && intersected.isLowerIncluded())));
    }
    int lowerResult = scope.getLowerBound().compareTo(iUpper);
    int upperResult = 1;
    if (scope.getUpperBound() != null) {
      upperResult = scope.getUpperBound().compareTo(intersected.getLowerBound());
    }
    if ((lowerResult == 0) &&
        (scope.isLowerIncluded()) && (intersected.isUpperIncluded())) {
      return true;
    }
    if ((upperResult == 0) &&
        (scope.isUpperIncluded()) && (intersected.isLowerIncluded())) {
      return true;
    }
    return (lowerResult < 0) && (upperResult > 0);
  }

  public void substractScope(MOScope scope) {
    if (lowerBound.compareTo(scope.getUpperBound()) <= 0) {
      lowerBound = scope.getUpperBound();
      lowerIncluded = !scope.isUpperIncluded();
    }
  }

  public boolean covers(OID oid) {
    if (oid == null) {
      return false;
    }
    return (((getLowerBound().compareTo(oid) < 0) ||
             (isLowerIncluded() && getLowerBound().equals(oid))) &&
            ((getUpperBound() == null) ||
             (getUpperBound().compareTo(oid) > 0) ||
             (isUpperIncluded() && getUpperBound().equals(oid))));
  }

  /**
   * Checks if this scope is empty or not. An empty scope cannot cover any
   * OID (i.e. lower bound is greater than upper bound).
   * @return
   *    <code>true</code> if lower bound is greater than upper bound or if
   *    both bounds equal but one of the bounds is not-included.
   */
  public boolean isEmpty() {
    return (((lowerBound != null) && (upperBound != null)) &&
            ((lowerBound.compareTo(upperBound) > 0) ||
             (lowerBound.equals(upperBound) &&
              !(isLowerIncluded() && isUpperIncluded()))));
  }

  public String toString() {
    return getClass().getName()+
        "[lowerBound="+lowerBound+",lowerIncluded="+
        lowerIncluded+",upperBound="+upperBound+
        ",upperIncluded="+upperIncluded+"]";
  }

}
