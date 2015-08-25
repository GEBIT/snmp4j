/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOMutableRow2PC.java  
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

import org.snmp4j.agent.mo.DefaultMOTable.*;
import org.snmp4j.agent.request.*;

/**
 * The <code>MOMutableRow2PC</code> interface adds support for 2-Phase-Commit
 * to mutable table rows.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOMutableRow2PC extends MOMutableTableRow {

  /**
   * Prepares a row for changes described by the supplied change set. If the
   * modification cannot be successfully prepared, the error status of the
   * supplied <code>subRequest</code> should be set to the appropriate error
   * status value.
   * <p>
   * This method is called only once per modified row.
   * @param subRequest
   *    the sub-request that triggered the row change and that can be used
   *    to deny the commit phase by setting its error status.
   * @param changeSet
   *    a MOTableRow instance that represents the state of the row if all
   *    changes have been applied successfully.
   */
  void prepareRow(SubRequest subRequest, MOTableRow changeSet);

  /**
   * Prepares changing a single column.
   * @param subRequest
   *    the sub-request that corresponds to the column change. This object
   *    can be used to deny the commit phase by setting its error status.
   * @param changeSet
   *    a MOTableRow instance that represents the state of the row if all
   *    changes have been applied successfully.
   * @param column
   *    the column index of the column to be changed.
   */
  void prepare(SubRequest subRequest, MOTableRow changeSet, int column);

  /**
   * Commits changes to single column.
   * @param subRequest
   *    the sub-request that corresponds to the column change. This object
   *    can be used to deny the commit phase and triggering the undo phase
   *    by setting its error status.
   * @param changeSet
   *    a MOTableRow instance that represents the state of the row if all
   *    changes have been applied successfully.
   * @param column
   *    the column index of the column to be changed.
   */
  void commit(SubRequest subRequest, MOTableRow changeSet, int column);

  /**
   * Commits a row as described by the supplied change set. If the
   * modification cannot be successfully committed, the error status of the
   * supplied <code>subRequest</code> should be set to
   * <code>commitFailed</code>. Setting this error should be avoided under
   * any circumstances.
   * <p>
   * This method is called only once per modified row.
   * @param subRequest
   *    the sub-request that triggered the row change and that can be used
   *    to trigger the undo phase by setting its error status.
   * @param changeSet
   *    a MOTableRow instance that represents the state of the row if all
   *    changes have been applied successfully.
   */
  void commitRow(SubRequest subRequest, MOTableRow changeSet);

  /**
   * Cleanups resources holds for changes to a single column.
   * @param subRequest
   *    the sub-request that corresponds to the column change.
   * @param column
   *    the column index of the changed column.
   */
  void cleanup(SubRequest subRequest, int column);

  /**
   * Cleans up resources for a row.
   * <p>
   * This method is called only once per modified row.
   * @param request
   *    the sub-request that triggered the row change.
   * @param changeSet
   *    a MOTableRow instance that represents the state of the row if all
   *    changes have been applied successfully.
   */
  void cleanupRow(SubRequest request, ChangeSet changeSet);

  /**
   * Undos the changes to a single column.
   * @param subRequest
   *    the sub-request that corresponds to the column change.
   * @param column
   *    the column index of the changed column.
   */
  void undo(SubRequest subRequest, int column);

  /**
   * Undos changes to a row.
   * <p>
   * This method is called only once per modified row.
   * @param request
   *    the sub-request that triggered the row change.
   * @param changeSet
   *    a MOTableRow instance that represents the state of the row if all
   *    changes have been applied successfully.
   */
  void undoRow(SubRequest request, ChangeSet changeSet);

}
