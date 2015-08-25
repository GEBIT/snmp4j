/*_############################################################################
  _## 
  _##  SNMP4J 2 - TestTimeTicks.java  
  _## 
  _##  Copyright (C) 2003-2013  Frank Fock and Jochen Katz (SNMP4J.org)
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


package org.snmp4j.smi;

import junit.framework.*;


public class TestTimeTicks extends TestCase {

  public TestTimeTicks(String s) {
    super(s);
  }

  protected void setUp() {
  }

  protected void tearDown() {
  }

  public void testToString() {
    TimeTicks timeticks = new TimeTicks();
    String stringRet = timeticks.toString();
    assertEquals("0:00:00.00", stringRet);
  }

  public void testToMaxValue() {
    TimeTicks timeticks = new TimeTicks(4294967295L);
    String stringRet = timeticks.toString();
    System.out.println(stringRet);
    assertEquals("497 days, 2:27:52.95", stringRet);
  }

}
