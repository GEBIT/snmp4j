/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - ContextEvent.java  
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


package org.snmp4j.agent;

import java.util.*;

import org.snmp4j.smi.*;

/**
 * The <code>ContextEvent</code> object describes an event that added or removed
 * a context to/from a system.
 *
 * @author Frank Fock
 * @version 1.0
 */
public class ContextEvent extends EventObject {

  private static final long serialVersionUID = -7705596020456783972L;

  public static final int CONTEXT_ADDED = 1;
  public static final int CONTEXT_REMOVED = 2;

  private int type;
  private OctetString context;

  /**
   * Creates a context event.
   * @param source
   *    the source object that triggered the event.
   * @param type
   *    the event type, for example {@link #CONTEXT_ADDED} or
   *    {@link #CONTEXT_REMOVED}.
   * @param context
   *    the name of the context on whose behalf this event has been created.
   */
  public ContextEvent(Object source, int type, OctetString context) {
    super(source);
    this.type = type;
    this.context = context;
  }

  /**
   * Returns context name associated with this event object.
   * @return
   *    the context name.
   */
  public OctetString getContext() {
    return context;
  }

  /**
   * Returns the event type.
   * @return
   *    one of the CONTEXT_* constant values defined by this event object.
   */
  public int getType() {
    return type;
  }
}
