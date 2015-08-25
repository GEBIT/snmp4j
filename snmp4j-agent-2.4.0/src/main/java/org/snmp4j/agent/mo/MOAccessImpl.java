/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOAccessImpl.java  
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

import org.snmp4j.agent.MOAccess;

/**
 * The <code>MOAccessImpl</code> class implements an immutable
 * <code>MOAccess</code>. For special purposes, it can be sub-classed
 * to modify access dynamically, for example to build an simulation agent
 * where objects may be modified in a certain mode even if they are read-only
 * normally.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class MOAccessImpl implements MOAccess {

  public static final short ACCESSIBLE_FOR_READ    = 1;
  public static final short ACCESSIBLE_FOR_WRITE   = 2;
  public static final short ACCESSIBLE_FOR_CREATE   = 4;
  public static final short ACCESSIBLE_FOR_NOTIFY  = 8;

  public static final short ACCESSIBLE_FOR_READ_ONLY =
      ACCESSIBLE_FOR_READ | ACCESSIBLE_FOR_NOTIFY;
  public static final short ACCESSIBLE_FOR_READ_WRITE =
      ACCESSIBLE_FOR_WRITE | ACCESSIBLE_FOR_READ | ACCESSIBLE_FOR_NOTIFY;
  public static final short ACCESSIBLE_FOR_READ_CREATE =
      ACCESSIBLE_FOR_WRITE | ACCESSIBLE_FOR_CREATE |
      ACCESSIBLE_FOR_READ | ACCESSIBLE_FOR_NOTIFY;

  public static final MOAccess ACCESS_READ_ONLY =
      new MOAccessImpl(ACCESSIBLE_FOR_READ_ONLY);
  public static final MOAccess ACCESS_FOR_NOTIFY =
      new MOAccessImpl(ACCESSIBLE_FOR_NOTIFY);
  public static final MOAccess ACCESS_WRITE_ONLY =
      new MOAccessImpl(ACCESSIBLE_FOR_WRITE);
  public static final MOAccess ACCESS_READ_WRITE =
      new MOAccessImpl(ACCESSIBLE_FOR_READ_WRITE);
  public static final MOAccess ACCESS_READ_CREATE =
      new MOAccessImpl(ACCESSIBLE_FOR_READ_CREATE);

  private short access = ACCESSIBLE_FOR_READ | ACCESSIBLE_FOR_NOTIFY;

  public MOAccessImpl(int access) {
    this.access = (short)(access & 0xFFFF);
  }

  public boolean isAccessibleForRead() {
    return ((access & ACCESSIBLE_FOR_READ) > 0);
  }

  public boolean isAccessibleForWrite() {
    return ((access & ACCESSIBLE_FOR_WRITE) > 0);
  }

  public boolean isAccessibleForNotify() {
    return ((access & ACCESSIBLE_FOR_NOTIFY) > 0);
  }

  public boolean isAccessibleForCreate() {
    return ((access & ACCESSIBLE_FOR_CREATE) > 0);
  }

  /**
   * Returns the internal access ID.
   * @return
   *    a short value identifying the configured access level.
   */
  public final short getAccess() {
    return access;
  }

  /**
   * Returns the appropriate <code>MOAccess</code> instance for the supplied
   * access ID. If that ID matches one of the standard access levels defined
   * by this class, then that instance is returned. Otherwise, a new instance
   * will be created with that access ID.
   *
   * @param moAccess
   *    a bitwise OR combination of the basic access levels defined by this
   *    class.
   * @return
   *    a MOAccess instance.
   */
  public static MOAccess getInstance(int moAccess) {
    switch (moAccess) {
      case ACCESSIBLE_FOR_READ_ONLY: {
        return ACCESS_READ_ONLY;
      }
      case ACCESSIBLE_FOR_READ_CREATE: {
        return ACCESS_READ_CREATE;
      }
      case ACCESSIBLE_FOR_NOTIFY: {
        return ACCESS_FOR_NOTIFY;
      }
      case ACCESSIBLE_FOR_READ_WRITE: {
        return ACCESS_READ_WRITE;
      }
      case ACCESSIBLE_FOR_WRITE: {
        return ACCESS_WRITE_ONLY;
      }
      default: {
        return new MOAccessImpl(moAccess);
      }
    }
  }
}
