/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - VersionInfo.java  
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

package org.snmp4j.agent.version;

/**
 * The <code>VersionInfo</code> object returns information about the version
 * of this SNMP4J-Agent release.
 *
 * @author Frank Fock
 * @version 1.2c
 */
public class VersionInfo {

  public static final int MAJOR = 2;
  public static final int MINOR = 4;
  public static final int UPDATE = 0;
  public static final String PATCH = "";

  public static final String VERSION =
      MAJOR + "." + MINOR + "." + UPDATE + PATCH;

  public static final int DEPENDENCY_SNMP4J_MAJOR = 2;
  public static final int DEPENDENCY_SNMP4J_MINOR = 3;
  public static final int DEPENDENCY_SNMP4J_UPDATE = 4;
  public static final String DEPENDENCY_SNMP4J_PATCH = "";

  /**
   * Gets the version string for this release.
   * @return
   *    a string of the form <code>major.minor.update[patch]</code>.
   */
  public static String getVersion() {
    return VERSION;
  }

  /**
   * Checks whether SNMP4J has the minimum required version.
   * @return
   *    <code>true</code> if the dependencies have the minimum required
   *    version(s).
   */
  public static boolean checkMinVersionOfDependencies() {
    return org.snmp4j.version.VersionInfo.MAJOR >= DEPENDENCY_SNMP4J_MAJOR
        && org.snmp4j.version.VersionInfo.MINOR >= DEPENDENCY_SNMP4J_MINOR
        && org.snmp4j.version.VersionInfo.UPDATE >= DEPENDENCY_SNMP4J_UPDATE
        && org.snmp4j.version.VersionInfo.PATCH.compareTo(DEPENDENCY_SNMP4J_PATCH)>=0;
  }

  private VersionInfo() {
  }

}
