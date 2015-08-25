/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SNMPv2TC.java  
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

package org.snmp4j.agent.mo.snmp;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import org.snmp4j.agent.mo.snmp.tc.TCModule;
import org.snmp4j.agent.mo.snmp.tc.TextualConvention;
import org.snmp4j.agent.mo.snmp.tc.DateAndTimeTC;
import org.snmp4j.agent.mo.snmp.tc.TimeStampTC;
import org.snmp4j.agent.mo.snmp.tc.StorageTypeTC;
import org.snmp4j.agent.mo.snmp.tc.RowStatusTC;
import org.snmp4j.agent.mo.snmp.tc.DisplayStringTC;
import org.snmp4j.agent.mo.snmp.tc.TruthValueTC;
import org.snmp4j.agent.mo.snmp.tc.TestAndIncrTC;

public final class SNMPv2TC implements TCModule {

  public static final String MODULE_NAME = "SNMPv2-TC";

  public static final String AUTONOMOUSTYPE = "AutonomousType";
  public static final String TIMESTAMP = "TimeStamp";
  public static final String DISPLAYSTRING = "DisplayString";
  public static final String STORAGETYPE = "StorageType";
  public static final String DATEANDTIME = "DateAndTime";
  public static final String ROWSTATUS = "RowStatus";
  public static final String TRUTHVALUE = "TruthValue";
  public static final String TESTANDINCR = "TestAndIncr";

  private static Object[][] tcMapping = {
      { DATEANDTIME, new DateAndTimeTC() },
      { TIMESTAMP, new TimeStampTC() },
      { STORAGETYPE, new StorageTypeTC() },
      { DISPLAYSTRING, new DisplayStringTC() },
      { ROWSTATUS, new RowStatusTC() },
      { TRUTHVALUE, new TruthValueTC() },
      { TESTANDINCR, new TestAndIncrTC() }
  };

  private static Map<String, TextualConvention> textualConventions =
      new HashMap<String, TextualConvention>(tcMapping.length);

  static {
    for (Object[] aTcMapping : tcMapping) {
      textualConventions.put((String)aTcMapping[0], (TextualConvention)aTcMapping[1]);
    }
  }

  public SNMPv2TC() {
  }

  public final String getName() {
    return MODULE_NAME;
  }

  public TextualConvention getTextualConvention(String name) {
    return textualConventions.get(name);
  }

  public Collection<TextualConvention> getTextualConventions() {
    return textualConventions.values();
  }
}
