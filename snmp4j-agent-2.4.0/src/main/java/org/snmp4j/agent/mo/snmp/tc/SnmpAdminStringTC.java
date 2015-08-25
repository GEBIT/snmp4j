package org.snmp4j.agent.mo.snmp.tc;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.snmp.SnmpFrameworkMIB;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 * Created by fock on 31.01.2015.
 */
public class SnmpAdminStringTC implements TextualConvention<OctetString> {

  @Override
  public String getModuleName() {
    return SnmpFrameworkMIB.MODULE_NAME;
  }

  @Override
  public String getName() {
    return SnmpFrameworkMIB.SNMPADMINSTRING;
  }

  @Override
  public MOScalar<OctetString> createScalar(OID oid, MOAccess access, OctetString value) {
    return null;
  }

  @Override
  public MOColumn<OctetString> createColumn(int columnID, int syntax, MOAccess access, OctetString defaultValue, boolean mutableInService) {
    return null;
  }

  @Override
  public OctetString createInitialValue() {
    return null;
  }
}
