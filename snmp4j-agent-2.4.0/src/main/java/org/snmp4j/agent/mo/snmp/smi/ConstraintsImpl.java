/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ConstraintsImpl.java  
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


package org.snmp4j.agent.mo.snmp.smi;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import org.snmp4j.smi.Variable;
import org.snmp4j.PDU;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Integer32;

/**
 * The <code>ConstraintsImpl</code> class represents a collection of constraints
 * that are applied to a SNMP value in the order they have been added to this
 * constraints collection.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class ConstraintsImpl implements Constraints {

  private List<Constraint> constraints = new ArrayList<Constraint>();

  /**
   * Creates a new constraints collection.
   */
  public ConstraintsImpl() {
  }

  public void add(Constraint sizeConstraint) {
    constraints.add(sizeConstraint);
  }

  public void remove(Constraint sizeContraint) {
    constraints.remove(sizeContraint);
  }

  public Constraint[] getConstraints() {
    return (Constraint[])
        constraints.toArray(new Constraint[constraints.size()]);
  }

  public boolean isValidSize(long size) {
    if (constraints.size() > 0) {
      for (Constraint sc : constraints) {
        if ((size >= sc.getLowerBound()) && (size <= sc.getUpperBound())) {
          return true;
        }
      }
      return false;
    }
    else {
      return true;
    }
  }

  public int validate(Variable variable) {
    if (variable instanceof OctetString) {
      if (!isValidSize(((OctetString)variable).length())) {
        return PDU.wrongLength;
      }
    }
    else if (variable instanceof Integer32) {
      Integer32 i = (Integer32)variable;
      if (!isValidSize(i.getValue())) {
        return PDU.wrongValue;
      }
    }
    else if (variable instanceof UnsignedInteger32) {
      UnsignedInteger32 ui = (UnsignedInteger32)variable;
      if (!isValidSize(ui.getValue())) {
       return PDU.wrongValue;
      }
    }
    return PDU.noError;
  }

}
