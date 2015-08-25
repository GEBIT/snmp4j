package org.snmp4j.security.nonstandard;

import org.snmp4j.smi.OID;

/**
 * This class is provided for interoperability with some broken AES 192bit implementations of major
 * network device manufactures which use a key extension algorithm that was specified for
 * {@link org.snmp4j.security.Priv3DES} but was never specified for AES 192 and 256 bit.
 *
 * Note: DO NOT USE THIS CLASS IF YOU WANT TO COMPLY WITH draft-blumenthal-aes-usm-04.txt!
 *
 * @author Frank Fock
 * @version 2.2.3
 * @since 2.2.3
 */
public class PrivAES192With3DESKeyExtension extends PrivAESWith3DESKeyExtension {

  /**
   * Unique ID of this privacy protocol.
   */
  public static final OID ID = new OID("1.3.6.1.4.1.4976.2.2.1.2.1");

  /**
   * Constructor.
   */
  public PrivAES192With3DESKeyExtension() {
    super(24);
  }
  /**
   * Gets the OID uniquely identifying the privacy protocol.
   * @return
   *    an <code>OID</code> instance.
   */
  public OID getID() {
    return (OID) ID.clone();
  }
}
