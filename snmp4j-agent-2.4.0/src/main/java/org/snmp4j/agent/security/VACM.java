/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - VACM.java  
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
// for JavaDoc
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityLevel;

/**
 * The View-based Access Control Model interface defines methods and constants
 * that a contrete implementation of such a model has to implement.
 * An example of such a concrete implementation is defined by RFC 3415 and
 * implemented by the VacmMIB class.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface VACM {

  int VIEW_NOTIFY = 0;
  int VIEW_READ = 1;
  int VIEW_WRITE = 2;

  int VACM_OK = 0;
  int VACM_NOT_IN_VIEW  = 1;
  int VACM_NO_SUCH_VIEW = 2;
  int VACM_NO_SUCH_CONTEXT = 3;
  int VACM_NO_GROUP_NAME = 4;
  int VACM_NO_ACCESS_ENTRY = 5;
  int VACM_OTHER_ERROR = 6;

  /**
   * Checks whether access is allowed in the specified context for the security
   * name, model, level, and view type for the supplied OID.
   * @param context
   *   the context for which access is requested.
   * @param securityName
   *   the security name.
   * @param securityModel
   *   the security model, see {@link SecurityModel} for possible values.
   * @param securityLevel
   *   the security level, see {@link SecurityLevel} for possible values.
   * @param viewType
   *   the requested view type, possible values are {@link #VIEW_NOTIFY},
   *   {@link #VIEW_READ}, and {@link #VIEW_WRITE}.
   * @param oid
   *   the OID of the object instance for which access is requested.
   * @return
   *   {@link #VACM_OK} if access is granted or one of the VACM errors defined
   *   by this interface if access is rejected.
   */
  int isAccessAllowed(OctetString context,
                      OctetString securityName,
                      int securityModel,
                      int securityLevel,
                      int viewType,
                      OID oid);

  /**
   * Checks if access is allowed for the given OID within the specified view.
   * @param viewName
   *    the name of an existing view, i.e. that has bee retrieved by
   *    {@link #getViewName} before.
   * @param oid
   *   the OID of the object instance for which access is requested.
   * @return
   *   {@link #VACM_OK} if access is granted or one of the VACM errors defined
   *   by this interface if access is rejected.
   */
  int isAccessAllowed(OctetString viewName, OID oid);

  /**
   * Gets the view name of the view defined by the supplied credentials.
   * @param context
   *   the context for which access is requested.
   * @param securityName
   *   the security name.
   * @param securityModel
   *   the security model, see {@link SecurityModel} for possible values.
   * @param securityLevel
   *   the security level, see {@link SecurityLevel} for possible values.
   * @param viewType
   *   the requested view type, possible values are {@link #VIEW_NOTIFY},
   *   {@link #VIEW_READ}, and {@link #VIEW_WRITE}.
   * @return
   *    the view name if the credentials can be mapped to an existing view.
   *    Otherwise, if no such view exists then <code>null</code> is returned.
   */
  OctetString getViewName(OctetString context,
                          OctetString securityName,
                          int securityModel,
                          int securityLevel,
                          int viewType);
}
