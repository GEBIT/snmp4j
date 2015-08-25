/*_############################################################################
  _##
  _##  SNMP4J-Agent 2 - SnmpEngineIDTC.java
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

package org.snmp4j.agent.mo.snmp.tc;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.io.MOInput;
import org.snmp4j.agent.io.MOOutput;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOMutableColumn;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.SnmpFrameworkMIB;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import java.io.IOException;

/**
 * Created by fock on 31.01.2015.
 */
public class SnmpEngineIDTC implements TextualConvention<OctetString> {

  private OctetString snmpEngineID;

  public SnmpEngineIDTC(OctetString snmpEngineID) {
    this.snmpEngineID = snmpEngineID;
  }

  @Override
  public String getModuleName() {
    return SnmpFrameworkMIB.MODULE_NAME;
  }

  @Override
  public String getName() {
    return SnmpFrameworkMIB.SNMPENGINEID;
  }

  @Override
  public MOScalar<OctetString> createScalar(OID oid, MOAccess access, OctetString value) {
    return new SnmpEngineIdScalar(oid, access, value);
  }

  @Override
  public MOColumn<OctetString> createColumn(int columnID, int syntax, MOAccess access,
                                            OctetString defaultValue, boolean mutableInService) {
    return new SnmpEngineIdColumn(columnID, syntax, access, defaultValue, mutableInService);
  }

  @Override
  public OctetString createInitialValue() {
    return snmpEngineID;
  }

  private class SnmpEngineIdScalar extends MOScalar<OctetString> {

    /**
     * Creates a scalar MO instance with OID, maximum access level and initial
     * value.
     *
     * @param id     the instance OID of the scalar instance (last sub-identifier should be
     *               zero).
     * @param access the maximum access level supported by this instance.
     * @param value  the initial value of the scalar instance. If the initial value is
     *               <code>null</code> or a Counter syntax, the scalar is created as a
     */
    public SnmpEngineIdScalar(OID id, MOAccess access, OctetString value) {
      super(id, access, value);
    }

    @Override
    public synchronized void load(MOInput input) throws IOException {
      super.load(input);
      // Replace an empty engine ID with the current local engine ID after load.
      if (getValue().length() == 0) {
        setValue(snmpEngineID);
      }
    }

    @Override
    public synchronized void save(MOOutput output) throws IOException {
      OctetString engineID = getValue();
      if (engineID == null || engineID.equals(snmpEngineID)) {
        output.writeVariable(new OctetString());
      }
      else {
        super.save(output);
      }
    }
  }

  private class SnmpEngineIdColumn extends MOMutableColumn<OctetString> {

    public SnmpEngineIdColumn(int columnID, int syntax, MOAccess access, Variable defaultValue,
                              boolean mutableInService) {
      super(columnID, syntax, access, defaultValue, mutableInService);
    }

    /**
     * Return the restore value for this column and the given row. If the engine ID is empty,
     * the local engine ID will be returned instead.
     * @param rowValues
     *    a row of the table where this column is part of.
     * @param column
     *    the column index of this column in <code>row</code>.
     * @return
     *    the restored value. By default this is <code>rowValues[column]</code>.
     * @since 2.4
     */
    @Override
    public Variable getRestoreValue(Variable[] rowValues, int column) {
      Variable variable = rowValues[column];
      if (variable == null || !(variable instanceof OctetString) ||
          (((OctetString)variable).length() == 0)) {
        return snmpEngineID;
      }
      return rowValues[column];
    }

    /**
     * Return the content of this column's value of the given row for persistent storage. If the
     * engine ID equals to local engine ID, an empty {@link org.snmp4j.smi.OctetString} will be returned.
     * @param row
     *    a row of the table where this column is part of.
     * @param column
     *    the column index of this column in <code>row</code>.
     * @return
     *    the value to be stored persistently for this <code>row</code> and <code>column</code>.
     * @since 2.4
     */
    @Override
    public Variable getStoreValue(MOTableRow row, int column) {
      if (snmpEngineID.equals(row.getValue(column))) {
        return new OctetString();
      }
      return row.getValue(column);
    }
  }
}
