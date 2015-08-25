/*_############################################################################
  _## 
  _##  SNMP4J 2 - TestPrivAES.java  
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


package org.snmp4j.security;

import org.apache.log4j.*;
import junit.framework.*;
import org.snmp4j.smi.OctetString;


public class TestPrivAES
    extends TestCase {
  static Logger cat = Logger.getLogger(TestPrivAES.class);
  private SecurityProtocols secProts;

  public static String asHex(byte buf[]) {
    return new OctetString(buf).toHexString();
  }

  public TestPrivAES() {
    secProts = SecurityProtocols.getInstance();
    secProts.addDefaultProtocols();
  }

  public TestPrivAES(String p0) {
    super(p0);
  }

  protected void setUp() {
  }

  protected void tearDown() {
  }

  public static void testCrypt() {
    BasicConfigurator.configure();

    PrivAES128 pd = new PrivAES128();
    DecryptParams pp = new DecryptParams();
    byte[] key = {
        (byte) 0x66, (byte) 0x95, (byte) 0xfe, (byte) 0xbc,
        (byte) 0x92, (byte) 0x88, (byte) 0xe3, (byte) 0x62,
        (byte) 0x82, (byte) 0x23, (byte) 0x5f, (byte) 0xc7,
        (byte) 0x15, (byte) 0x1f, (byte) 0x12, (byte) 0x84//,
        /*
        (byte) 0x97, (byte) 0xb3, (byte) 0x8f, (byte) 0x3f,
        (byte) 0x50, (byte) 0x5E, (byte) 0x07, (byte) 0xEB,
        (byte) 0x9A, (byte) 0xF2, (byte) 0x55, (byte) 0x68,
        (byte) 0xFA, (byte) 0x1F, (byte) 0x5D, (byte) 0xBE*/
    };
    byte[] plaintext = "This is a secret message, nobody is allowed to read it!".
        getBytes();
    byte[] ciphertext = null;
    byte[] decrypted = null;
    int engine_boots = 0xdeadc0de;
    int engine_time = 0xbeefdede;

    cat.debug("Cleartext: " + asHex(plaintext));
    ciphertext = pd.encrypt(plaintext, 0, plaintext.length, key, engine_boots,
                            engine_time, pp);
    cat.debug("Encrypted: " + asHex(ciphertext));
    decrypted = pd.decrypt(ciphertext, 0, ciphertext.length, key, engine_boots, engine_time, pp);
    cat.debug("Cleartext: " + asHex(decrypted));

    assertEquals(asHex(plaintext), asHex(decrypted));

    cat.info("pp length is: " + pp.length);
    assertEquals(8, pp.length);
  }

  public void testAesKeyExtension() {
    SecurityProtocols.getInstance().addAuthenticationProtocol(new AuthSHA());
    SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES256());
    byte[] key =
      SecurityProtocols.getInstance().passwordToKey(PrivAES256.ID, AuthSHA.ID, new OctetString("maplesyrup"),
        new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte)0,
                    (byte) 0, (byte) 0, (byte) 0, (byte)0,
                    (byte) 0, (byte) 0, (byte) 0, (byte)2 });
    assertEquals("66:95:fe:bc:92:88:e3:62:82:23:5f:c7:15:1f:12:84:97:b3:8f:3f:50:5e:07:eb:9a:f2:55:68:fa:1f:5d:be",
        new OctetString(key).toHexString());
  }
}
