/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - MOTableModel.java  
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

import org.snmp4j.smi.OID;
import java.util.Iterator;

/**
 * The <code>MOTableModel</code> interface defines the base table
 * model interface needed for <code>MOTable</code>s. This model can be used
 * for read-only and read-write SNMP conceptual tables. For read-create tables
 * the {@link MOMutableTableModel} should be used instead.
 *
 * @author Frank Fock
 * @version 1.0
 */
public interface MOTableModel<R extends MOTableRow> {

  /**
   * Returns the number of columns currently in this table model.
   * @return
   *    the number of columns.
   */
  int getColumnCount();

  /**
   * Returns the number of rows currently in this table model.
   * @return
   *    the number of rows.
   */
  int getRowCount();

  /**
   * Returns <code>true</code> if the table model contains no rows.
   * @return
   *    <code>true</code> if the table model contains no rows.
   * @since 2.2
   */
  boolean isEmpty();

  /**
   * Checks whether this table model contains a row with the specified index.
   * @param index
   *    the index OID of the row to search.
   * @return
   *    <code>true</code> if this model has a row of with index
   *    <code>index</code> or <code>false</code> otherwise.
   */
  boolean containsRow(OID index);

  /**
   * Gets the row with the specified index.
   * @param index
   *    the row index.
   * @return
   *    the <code>MOTableRow</code> with the specified index and
   *    <code>null</code> if no such row exists.
   */
  R getRow(OID index);

  /**
   * Returns an iterator over the rows in this table model.
   * @return
   *    an <code>Iterator</code> returning <code>MOTableRow</code> instances.
   */
  Iterator<R> iterator();

  /**
   * Returns an iterator on a view of the rows of this table model
   * whose index values are greater or equal <code>lowerBound</code>.
   *
   * @param lowerBound
   *    the lower bound index (inclusive). If <code>lowerBound</code> is
   *    <code>null</code> the returned iterator is the same as returned by
   *    {@link #iterator()}.
   * @return
   *    an <code>Iterator</code> over the
   */
  Iterator<R> tailIterator(OID lowerBound);

  /**
   * Returns the last row index in this model.
   * @return
   *    the last index OID of this model.
   */
  OID lastIndex();

  /**
   * Returns the first row index in this model.
   * @return
   *    the first index OID of this model.
   */
  OID firstIndex();

  /**
   * Returns the first row contained in this model.
   * @return
   *    the <code>MOTableRow</code> with the smallest index or <code>null</code>
   *    if the model is empty.
   */
  R firstRow();

  /**
   * Returns the last row contained in this model.
   * @return
   *    the <code>MOTableRow</code> with the greatest index or <code>null</code>
   *    if the model is empty.
   */
  R lastRow();
}
