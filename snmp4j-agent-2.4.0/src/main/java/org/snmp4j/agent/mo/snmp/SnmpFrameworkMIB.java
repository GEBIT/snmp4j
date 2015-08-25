/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - SnmpFrameworkMIB.java  
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

import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.snmp.tc.SnmpAdminStringTC;
import org.snmp4j.agent.mo.snmp.tc.SnmpEngineIDTC;
import org.snmp4j.agent.mo.snmp.tc.TCModule;
import org.snmp4j.agent.mo.snmp.tc.TextualConvention;
import org.snmp4j.smi.OctetString;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Integer32;
import org.snmp4j.TransportMapping;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.snmp4j.security.USM;

/**
 * The SnmpFrameworkMIB implements the SNMP-FRAMEWORK-MIB as defined by
 * RFC 3411.
 *
 * @author Frank Fock
 * @version 2.3.2
 */
public class SnmpFrameworkMIB implements MOGroup, TCModule {

  public static final String MODULE_NAME = "SNMP-FRAMEWORK-MIB";

  private USM usm;
  private Collection<TransportMapping> transportMappings;

  private MOScalar<OctetString> snmpEngineID;
  private MOScalar<Integer32> snmpEngineBoots;
  private MOScalar<Integer32> snmpEngineTime;
  private MOScalar<Integer32> snmpEngineMaxMessageSize;
  private boolean updateUsmFromMIB;


  public static final String SNMPADMINSTRING = "SnmpAdminString";
  public static final String SNMPENGINEID = "SnmpEngineID";

  private Object[][] tcMapping;
  private Map<String, TextualConvention> textualConventions;

  public SnmpFrameworkMIB(USM usm, Collection<TransportMapping> transportMappings) {
    this(usm, transportMappings, false);
  }

  public SnmpFrameworkMIB(USM usm, Collection<TransportMapping> transportMappings, boolean updateUsmFromMIB) {
    this.usm = usm;
    this.transportMappings = transportMappings;
    this.updateUsmFromMIB = updateUsmFromMIB;
    createMOs();
    tcMapping = new Object[][] {
      { SNMPADMINSTRING, new SnmpAdminStringTC() },
      { SNMPENGINEID, new SnmpEngineIDTC(snmpEngineID.getValue()) }
    };
    textualConventions = new HashMap<String, TextualConvention>(tcMapping.length);
    for (Object[] aTcMapping : tcMapping) {
      textualConventions.put((String)aTcMapping[0], (TextualConvention)aTcMapping[1]);
    }
  }

  private void createMOs() {
    snmpEngineID = new MOScalar<OctetString>(new OID("1.3.6.1.6.3.10.2.1.1.0"),
                                MOAccessImpl.ACCESS_READ_ONLY,
                                null) {
      @Override
      public OctetString getValue() {
        return new OctetString(getUSM().getLocalEngineID());
      }

      @Override
      public int setValue(OctetString value) {
        int updateResult = super.setValue(value);
        if (updateUsmFromMIB) {
          getUSM().setLocalEngine(value, snmpEngineBoots.getValue().toInt(), snmpEngineTime.getValue().toInt());
        }
        return updateResult;
      }
    };
    snmpEngineBoots = new MOScalar<Integer32>(new OID("1.3.6.1.6.3.10.2.1.2.0"),
                                   MOAccessImpl.ACCESS_READ_ONLY,
                                   null) {
      @Override
      public Integer32 getValue() {
        return new Integer32(getUSM().getEngineBoots());
      }

      @Override
      public int setValue(Integer32 value) {
        // we increase the set value by one
        Integer32 newBoots = new Integer32(value.getValue()+1);
        int updateResult = super.setValue(newBoots);
        if (updateUsmFromMIB) {
          getUSM().setLocalEngine(snmpEngineID.getValue(), newBoots.toInt(), snmpEngineTime.getValue().toInt());
        }
        return updateResult;
      }
    };
    snmpEngineTime = new MOScalar<Integer32>(new OID("1.3.6.1.6.3.10.2.1.3.0"),
                                  MOAccessImpl.ACCESS_READ_ONLY,
                                  null) {
      @Override
      public Integer32 getValue() {
        return new Integer32(getUSM().getEngineTime());
      }
      @Override
      public int setValue(Integer32 value) {
        int updateResult = super.setValue(value);
        if (updateUsmFromMIB) {
          getUSM().setLocalEngine(snmpEngineID.getValue(), snmpEngineBoots.getValue().toInt(),value.toInt());
        }
        return updateResult;
      }
    };
    if (updateUsmFromMIB) {
      // we need to set all MIB objects to non-volatile in order to allow
      // persistent storage and restore of the engine values:
      snmpEngineID.setVolatile(false);
      snmpEngineBoots.setVolatile(false);
      snmpEngineTime.setVolatile(false);
    }
    Integer32 maxMsgSize = new Integer32(getMaxMessageSize());
    snmpEngineMaxMessageSize = new MOScalar<Integer32>(new OID("1.3.6.1.6.3.10.2.1.4.0"),
                                            MOAccessImpl.ACCESS_READ_ONLY,
                                            maxMsgSize);
  }

  private int getMaxMessageSize() {
    int totalMaxMessageSize = 2147483647;
    for (TransportMapping transportMapping : transportMappings) {
      int maxMsgSize = (transportMapping).getMaxInboundMessageSize();
      totalMaxMessageSize = Math.min(totalMaxMessageSize, maxMsgSize);
    }
    return totalMaxMessageSize;
  }

  public void registerMOs(MOServer server, OctetString context) throws
      DuplicateRegistrationException {
    if (usm != null) {
      server.register(snmpEngineID, context);
      server.register(snmpEngineBoots, context);
      server.register(snmpEngineTime, context);
    }
    server.register(snmpEngineMaxMessageSize, context);
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    server.unregister(snmpEngineID, context);
    server.unregister(snmpEngineBoots, context);
    server.unregister(snmpEngineTime, context);
    server.unregister(snmpEngineMaxMessageSize, context);
  }

  public MOScalar getSnmpEngineBoots() {
    return snmpEngineBoots;
  }

  public MOScalar getSnmpEngineID() {
    return snmpEngineID;
  }

  public MOScalar getSnmpEngineMaxMessageSize() {
    return snmpEngineMaxMessageSize;
  }

  public MOScalar getSnmpEngineTime() {
    return snmpEngineTime;
  }

  public USM getUSM() {
    return usm;
  }

  /**
   * Indicates whether changes on the {@link #getSnmpEngineID()}, {@link #getSnmpEngineBoots()}, and
   * {@link #getSnmpEngineTime()} values are propagated to the referenced USM instance. If true,
   * changes of those objects will be applied to the USM.
   *
   * @return
   *   <code>true</code> if changes are propagated.
   */
  public boolean isUpdateUsmFromMIB() {
    return updateUsmFromMIB;
  }

  @Override
  public String getName() {
    return MODULE_NAME;
  }

  @Override
  public TextualConvention getTextualConvention(String name) {
    return textualConventions.get(name);
  }

  @Override
  public Collection<TextualConvention> getTextualConventions() {
    return textualConventions.values();
  }
}
