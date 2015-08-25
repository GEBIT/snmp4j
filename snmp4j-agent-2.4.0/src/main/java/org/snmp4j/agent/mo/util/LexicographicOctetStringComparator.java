/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - LexicographicOctetStringComparator.java  
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


package org.snmp4j.agent.mo.util;

import java.util.*;
import org.snmp4j.smi.OctetString;

public class LexicographicOctetStringComparator implements Comparator<OctetString> {

  private boolean impliedLength = false;

  public LexicographicOctetStringComparator() {
  }

  public LexicographicOctetStringComparator(boolean impliedLength) {
    this.impliedLength = impliedLength;
  }

  /**
   * Compares its two OctetString instances for lexicographic order.
   *
   * @param os1 the first object to be compared.
   * @param os2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first
   *   argument is less than, equal to, or greater than the second.
   */
  public int compare(OctetString os1, OctetString os2) {
    int result = 0;
    if (!impliedLength) {
      result = os1.length() - os2.length();
    }
    if (result == 0) {
      result = os1.compareTo(os2);
    }
    return result;
  }
}
