/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - IndexGenerator.java  
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

package org.snmp4j.agent.util;

import org.snmp4j.smi.Variable;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Counter64;

/**
 * The <code>IndexGenerator</code> class can be used to generate a sequence
 * of unique index values.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class IndexGenerator {

  private Variable seedSubIndex;
  private boolean impliedLength;

  /**
   * Creates a sub-index sequence generator based on a seed value.
   * @param seedSubIndex
   *    the seed value to start with (the seed itself will never be returned).
   */
  public IndexGenerator(Variable seedSubIndex) {
    if (!((seedSubIndex instanceof Integer32) ||
          (seedSubIndex instanceof UnsignedInteger32) ||
          (seedSubIndex instanceof Counter64))) {
      throw new IllegalArgumentException("A seed subindex of type "+
                                         seedSubIndex.getSyntaxString()+
                                         "is not supported");
    }
    this.seedSubIndex = seedSubIndex;
  }

  /**
   * Creates a sub-index sequence generator based on a seed value.
   * @param seedSubIndex
   *    the seed value to start with (the seed itself will never be returned).
   * @param impliedLength
   *    if <code>true</code> the length will not be included in the returned
   *    sub-index for variable-length types.
   */
  public IndexGenerator(Variable seedSubIndex, boolean impliedLength) {
    this(seedSubIndex);
    this.impliedLength = impliedLength;
  }

  /**
   * Creates the next sub-index OID value.
   * @return
   *    a sub-index value corresponding to the seed sub-index type.
   */
  public synchronized OID getNextSubIndex() {
    if (seedSubIndex instanceof Integer32) {
      Integer32 i = (Integer32)seedSubIndex;
      i.setValue(i.getValue()+1);
    }
    else if (seedSubIndex instanceof UnsignedInteger32) {
      UnsignedInteger32 ui = (UnsignedInteger32)seedSubIndex;
      ui.setValue(ui.getValue()+1);
    }
    else if (seedSubIndex instanceof Counter64) {
      Counter64 c = (Counter64)seedSubIndex;
      c.setValue(c.getValue()+1);
    }
    return seedSubIndex.toSubIndex(impliedLength);
  }
}
