/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MutableVACM.java  
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


package org.snmp4j.agent.security;

import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
// JavaDoc
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.agent.mo.snmp.StorageType;

/**
 * The <code>MutableVACM</code> interface extends the basic {@link VACM}
 * by providing methods to change the configuration of the view-based access
 * model.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MutableVACM extends VACM {

  int VACM_MATCH_EXACT = 1;
  int VACM_MATCH_PREFIX = 2;

  int VACM_VIEW_INCLUDED = 1;
  int VACM_VIEW_EXCLUDED = 2;

  /**
   * Adds a security name to group mapping to the VACM.
   * @param securityModel
   *   the security model the mapping is based on, see
   *   {@link SecurityModel} for possible values.
   * @param securityName
   *   the security name to map to a group.
   * @param groupName
   *   the name of the group.
   * @param storageType
   *    the storage type to use for the entry. Possible values are defined
   *    by {@link StorageType}.
   */
  void addGroup(int securityModel,
                OctetString securityName,
                OctetString groupName,
                int storageType);

  /**
   * Removes a security name to group mapping from the VACM.
   * @param securityModel
   *   the security model the mapping is based on, see
   *   {@link SecurityModel} for possible values.
   * @param securityName
   *   the mapped security name.
   * @return
   *   <code>true</code> if the entry has been removed, <code>false</code>
   *   otherwise (i.e. if such an entry does not exists).
   */
  boolean removeGroup(int securityModel,
                      OctetString securityName);

  /**
   * Adds an access entry for the specified group name.
   * @param groupName
   *    the group name for which to create an access entry.
   * @param prefix
   *    if <code>match</code> is {@link #VACM_MATCH_PREFIX} the context name
   *    checked by the VACM must exatcly match this value, otherwise a prefix
   *    match is sufficient.
   * @param securityModel
   *   the security model that must be used to gain access on behalf of this
   *   entry, see {@link SecurityModel} for possible values.
   * @param securityLevel
   *   the minimum security level that must be used to gain access on behalf of
   *   this entry, see {@link SecurityLevel} for possible values.
   * @param match
   *   specifies the type of context match used by this entry. Possible values
   *   are {@link #VACM_MATCH_EXACT} and {@link #VACM_MATCH_PREFIX}.
   * @param readView
   *   the MIB view of the SNMP context to which this conceptual row authorizes
   *   read access. If the value is the empty string or if there is no active
   *   MIB view having this value of vacmViewTreeFamilyViewName, then no access
   *   is granted.
   * @param writeView
   *   the MIB view of the SNMP context to which this conceptual row authorizes
   *   write access. If the value is the empty string or if there is no active
   *   MIB view having this value of vacmViewTreeFamilyViewName, then no access
   *   is granted.
   * @param notifyView
   *   the MIB view of the SNMP context to which this conceptual row authorizes
   *   access for notifications. If the value is the empty string or if there
   *   is no active MIB view having this value of vacmViewTreeFamilyViewName,
   *   then no access is granted.
   * @param storageType
   *    the storage type to use for the entry. Possible values are defined
   *    by {@link StorageType}.
   */
  void addAccess(OctetString groupName,
                 OctetString prefix,
                 int securityModel,
                 int securityLevel,
                 int match,
                 OctetString readView,
                 OctetString writeView,
                 OctetString notifyView,
                 int storageType);

  /**
   * Removes an access entry from the VACM.
   * @param groupName
   *    the group name for which to remove an access entry.
   * @param prefix
   *    the context name or prefix of the access entry.
   * @param securityModel
   *   the security model that must be used to gain access on behalf of this
   *   entry, see {@link SecurityModel} for possible values.
   * @param securityLevel
   *   the minimum security level that must be used to gain access on behalf of
   *   this entry, see {@link SecurityLevel} for possible values.
   * @return
   *   <code>true</code> if the entry has been removed, <code>false</code>
   *   otherwise (i.e. if such an entry does not exists).
   */
  boolean removeAccess(OctetString groupName,
                       OctetString prefix,
                       int securityModel,
                       int securityLevel);

  /**
   * Adds a view tree family to an VACM view.
   * @param viewName
   *    the view name to which a tree family is to be added.
   * @param subtree
   *    the MIB subtree which when combined with the corresponding instance of
   *    <code>mask</code> (vacmViewTreeFamilyMask) defines a family of view
   *    subtrees.
   * @param mask
   *    The bit mask which, in combination with the corresponding instance of
   *    <code>subtree</code> (vacmViewTreeFamilySubtree), defines a family of
   *    view subtrees. See RFC 3415 vacmViewTreeFamilySubtree definition for
   *    more details on the bit mask.
   * @param type
   *    specifies whether the subtree is included {@link #VACM_VIEW_INCLUDED}
   *    or excluded {@link #VACM_VIEW_EXCLUDED} from the view.
   * @param storageType
   *    the storage type to use for the entry. Possible values are defined
   *    by {@link StorageType}.
   */
  void addViewTreeFamily(OctetString viewName,
                         OID subtree,
                         OctetString mask,
                         int type,
                         int storageType);

  /**
   * Removes a view tree family from a VACM view.
   * @param viewName
   *    the view name from which a subtree family is to be removed.
   * @param subtree
   *    the MIB subtree associated with this entry.
   * @return
   *   <code>true</code> if the entry has been removed, <code>false</code>
   *   otherwise (i.e. if such an entry does not exists).
   */
  boolean removeViewTreeFamily(OctetString viewName,
                               OID subtree);
}
