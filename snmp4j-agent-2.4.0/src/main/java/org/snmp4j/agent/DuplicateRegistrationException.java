/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DuplicateRegistrationException.java  
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

/**
 * A <code>DuplicateRegistrationException</code> is thrown when a managed object
 * registration attempt conflicts with an existing registration because their
 * scope's overlap.
 *
 * @author Frank Fock
 * @version 1.1
 */
public class DuplicateRegistrationException extends MOException {

  private static final long serialVersionUID = 2067223495895115541L;

  private MOScope scope;
  private MOScope duplicate;

  public DuplicateRegistrationException() {
  }

  public DuplicateRegistrationException(String message) {
    super(message);
  }

  public DuplicateRegistrationException(MOScope registrationScope) {
    super(registrationScope.toString());
    this.scope = registrationScope;
  }

  public DuplicateRegistrationException(MOScope registrationScope,
                                        MOScope registeredScope) {
    super(registrationScope.toString());
    this.scope = registrationScope;
    this.duplicate = registeredScope;
  }

  /**
   * Returns the scope of the failed registration attempt.
   * @return
   *   a <code>MOScope</code> instance, typically a {@link MOContextScope}.
   */
  public MOScope getRegistrationScope() {
    return scope;
  }

  /**
   * Returns the scope that is already registered and overlaps with the scope
   * returned by {@link #getRegistrationScope()}.
   * @return
   *   a <code>MOScope</code> instance.
   * @since 1.1
   */
  public MOScope getRegisteredScope() {
    return duplicate;
  }

}
