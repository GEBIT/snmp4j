/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TextualConvention.java  
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

import org.snmp4j.smi.OID;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.smi.Variable;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOColumn;

/**
 * The <code>TextualConvention</code> interface defines the common properties
 * of SMI textual conventions needed to use them across different MIB modules
 * within an agent implementation.
 * <p>
 * A textual convention is characterized by its name and the MIB module name
 * where it has been defined. With these attributes a TC registry is able to
 * lookup TC implementations by name.
 * <p>
 * A <code>MOFactory</code> can then use a TC name to lookup its implementation
 * and then use one of the two factory methods of each TC to create either
 * a scalar or columnar <code>ManagedObject</code> instance.
 * <p>
 * If you need to use your own TC implementations (either replacing/extending
 * already provided ones or adding new ones) then register them to the
 * <code>MOFactory</code> you are using.
 *
 * @author Frank Fock
 * @version 2.0.5
 */
public interface TextualConvention<V extends Variable> {

  /**
   * Returns the MIB module name that defined this textual convention.
   * @return
   *    an unique module name
   */
  String getModuleName();

  /**
   * Returns the name of the textual convention as defined in the MIB module.
   * @return
   *    the unique name (within the MIB module) of the TC.
   */
  String getName();

  /**
   * Creates a MOScalar instance of this TC specified by OID, access, and
   * optional value.
   * @param oid
   *    the OID of the scalar isntance.
   * @param access
   *    the access definition.
   * @param value
   *    the <code>Variable</code> instance containing the value of the
   *    scalar. If <code>value</code> is <code>null</code>, the TC should create
   *    an initial value with {@link #createInitialValue()}.
   * @return
   *    a MOScalar instance.
   */
  MOScalar<V> createScalar(OID oid, MOAccess access, V value);

  /**
   * Creates a MOColumn instance of this TC specified by the column ID,
   * access, default value, and mutable flag.
   * @param columnID
   *    the column id as defined in the MIB module (typically starting at one).
   * @param syntax
   *    the SMI syntax supported by the column.
   * @param access
   *    the access definition.
   * @param defaultValue
   *    the default value or <code>null</code> if there is no DEFVAL clause for
   *    this column.
   * @param mutableInService
   *    <code>true</code> if this column may be modified while row is in
   *    service.
   * @return
   *    the MOColumn created.
   */
  MOColumn<V> createColumn(int columnID, int syntax, MOAccess access,
                           V defaultValue, boolean mutableInService);


  /**
   * Creates an initial value for an object instance of this textual convention.
   * @return
   *    a Variable instance with a valid value (according to this TC).
   * @since 1.3
   */
  V createInitialValue();
}
