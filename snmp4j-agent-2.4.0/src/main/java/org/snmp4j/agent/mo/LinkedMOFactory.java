/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - LinkedMOFactory.java  
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
package org.snmp4j.agent.mo;

import org.snmp4j.agent.mo.*;
import org.snmp4j.smi.OID;

/**
 * The LinkedMOFactory extends the basic MOFactory interface by means
 * for associating managed objects with instrumentation helper objects.
 * For example, could the REFERENCE clause of a MIB module contain a machine
 * readable description of the value source (URI) of a particular managed
 * object. This factory could then provide that reference to the managed objects
 * created by this factory.
 *
 * @author Frank Fock
 * @version 1.2
 */
public interface LinkedMOFactory extends MOFactory {

  /**
   * Set a link between the supplied object ID of a managed object class (or
   * a set of managed object classes if the OID refers to a MIB sub-tree) to
   * the given helper object.
   * @param oid
   *    an OID of a managed object class or sub-tree.
   * @param instrumentationHelperObject
   *    an object that helps the factory or the objects created on its behalf
   *    to instrument the those objects.
   * @see #getLink(OID oid)
   */
  void setLink(OID oid, Object instrumentationHelperObject);

  /**
   * Gets the link for the given object ID or any sub-OID prefix thereof.
   * @param oid
   *    the oid of the managed object class (prefix) for which to returned the
   *    linked instrumentation helper class.
   * @return
   *    an instrumentation helper object (for example an URI) or
   *    <code>null</code> if no such link exists.
   */
  Object getLink(OID oid);

}

