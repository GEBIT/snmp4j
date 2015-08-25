/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - DefaultMOTableModel.java  
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

import java.util.TreeMap;
import java.util.Collections;
import java.util.SortedMap;
import org.snmp4j.smi.OID;
import java.util.Iterator;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.io.IOException;

public class DefaultMOTableModel<R extends MOTableRow> implements MOTableModel<R> {

  protected SortedMap<OID, R> rows = Collections.synchronizedSortedMap(new TreeMap<OID, R>());
  protected int columnCount = 0;

  public DefaultMOTableModel() {
  }

  public synchronized R addRow(R row) {
    this.columnCount = Math.max(row.size(), columnCount);
    return rows.put(row.getIndex(), row);
  }

  public int getColumnCount() {
    return columnCount;
  }

  public int getRowCount() {
    return rows.size();
  }

  @Override
  public boolean isEmpty() {
    return rows.isEmpty();
  }

  public synchronized R getRow(OID index) {
    return rows.get(index);
  }

  public synchronized  OID firstIndex() {
    if (rows.size() > 0) {
      return rows.firstKey();
    }
    return null;
  }

  public synchronized Iterator<R> iterator() {
    return rows.values().iterator();
  }

  public synchronized R firstRow() {
    OID index = firstIndex();
    if (index != null) {
      return rows.get(index);
    }
    return null;
  }

  public synchronized OID lastIndex() {
    if (rows.size() > 0) {
      return rows.lastKey();
    }
    return null;
  }

  public synchronized R lastRow() {
    OID index = lastIndex();
    if (index != null) {
      return rows.get(index);
    }
    return null;
  }

  public boolean containsRow(OID index) {
    return rows.containsKey(index);
  }

  public synchronized Iterator<R> tailIterator(OID lowerBound) {
    if (lowerBound == null) {
      return iterator();
    }
    return rows.tailMap(lowerBound).values().iterator();
  }

  /**
   * Dumps a textual representation of the table model content to the specified
   * {@link OutputStream}.
   * @param os
   *    an {@link OutputStream} to write the model content to.
   * @throws IOException
   *    if a write operation on <code>os</code> fails with an IO exception.
   * @since 1.2.2
   */
  public synchronized void dump(OutputStream os) throws IOException {
    os.write(("Dump of "+getClass().getName()+":\n").getBytes());
    for (Entry<OID,R> e : rows.entrySet()) {
      os.write((e.getKey() + " # " + e.getValue() + "\n").getBytes());
    }
  }

}
