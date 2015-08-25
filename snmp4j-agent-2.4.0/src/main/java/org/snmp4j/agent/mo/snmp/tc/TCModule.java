/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TCModule.java  
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

import java.util.Collection;

/**
 * A <code>TCModule</code> interface defines the common public properties of
 * a textual convention registration. By convention, there should be a
 * <code>TCModule</code> instance for each MIB module definition supported by
 * an agent. But it is also possible to have only a single instance,
 * for example, a single managed object factory.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface TCModule {

  /**
   * Returns the (unique) name of the module definition.
   * @return
   *    a unique SMI MODULE name (upper case string with hyphens).
   */
  String getName();

  /**
   * Gets the textual convention for the specified name.
   * @param name
   *    the object definition name of the TC MIB definition.
   * @return
   *    a <code>TextualConvention</code> instance.
   */
  TextualConvention getTextualConvention(String name);

  /**
   * Gets a collection of <code>TextualConvention</code> instances in this
   * <code>TCModule</code>.
   * @return
   *    a Collection of <code>TextualConvention</code> instances.
   */
  Collection getTextualConventions();

}
