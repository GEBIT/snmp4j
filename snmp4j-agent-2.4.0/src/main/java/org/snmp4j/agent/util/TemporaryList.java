/*_############################################################################
  _## 
  _##  SNMP4J-Agent 2 - TemporaryList.java  
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

package org.snmp4j.agent.util;

import org.snmp4j.mp.SnmpConstants;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * The <code>TemporaryList</code> implements a list whose items are
 * automatically removed after a predefined timeout. When an item is
 * removed by timeout, listener can be called to handle the remove
 * operation.
 *
 * @author Frank Fock
 */
public class TemporaryList<T> {

  // Default timeout is 5min.
  public static final int DEFAULT_ITEM_TIMEOUT = 300000;

  // default timeout for entries in 1/1000 seconds.
  private int timeout = DEFAULT_ITEM_TIMEOUT;
  private LinkedList<TemporaryListItem<T>> list = new LinkedList<TemporaryListItem<T>>();

  public TemporaryList() {
  }

  /**
   * Creates a temporary list with the given timeout in milliseconds.
   * @param timeout
   *    the milliseconds to wait before an entry may get removed from this list.
   */
  public TemporaryList(int timeout) {
    this.timeout = timeout;
  }

  public synchronized void add(T o) {
    long now = System.nanoTime();
    if ((list.size() > 0) &&
        ((list.getFirst()).atMaturity(now))) {
      list.removeFirst();
    }
    if ((list.size() > 0) &&
        ((list.getLast()).atMaturity(now))) {
      list.removeLast();
    }
    list.addFirst(new TemporaryListItem<T>(o));
  }

  public synchronized boolean contains(T o) {
    for (TemporaryListItem item : list) {
      if (item.getItem().equals(o)) {
        return true;
      }
    }
    return false;
  }

  public synchronized boolean remove(T o) {
    long now = System.nanoTime();
    for (Iterator<TemporaryListItem<T>> it = list.iterator(); it.hasNext(); ) {
      TemporaryListItem item = it.next();
      if (item.getItem().equals(o)) {
        it.remove();
        return true;
      }
      else if (item.atMaturity(now)) {
        it.remove();
      }
    }
    return false;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public int getTimeout() {
    return timeout;
  }

  public Iterator<T> iterator() {
    return new TemporaryListIterator<T>(list);
  }

  public int size() {
    return list.size();
  }


  public synchronized void clear() {
    list.clear();
  }


  class TemporaryListItem<T> {
    private T item;
    private long timeOfMaturity;

    public TemporaryListItem(T item) {
      this.item = item;
      this.timeOfMaturity = System.nanoTime() + (timeout * SnmpConstants.MILLISECOND_TO_NANOSECOND);
    }

    /**
     * Gets the time of maturity, i.e. the time in nano seconds when this
     * entry may get removed earliest.
     * @return
     *    the time of maturity in nanoseconds.
     */
    public long getTimeOfMaturity() {
      return timeOfMaturity;
    }

    public T getItem() {
      return item;
    }

    public boolean equals(Object obj) {
      return item.equals(obj);
    }

    public int hashCode() {
      return item.hashCode();
    }

    /**
     * Checks if the given reference time in nanoseconds is later than
     * the maturity time of this item.
     * @param referenceTime
     *    the reference time in nanoseconds.
     * @return
     *    <code>true</code> if the item is subject to removal from list,
     *    <code>false</code> otherwise.
     */
    public boolean atMaturity(long referenceTime) {
      return (referenceTime > timeOfMaturity);
    }
  }

  class TemporaryListIterator<T> implements Iterator<T> {

    private Iterator<TemporaryListItem<T>> iterator;

    public TemporaryListIterator(LinkedList<TemporaryListItem<T>> list) {
      iterator = list.iterator();
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    public T next() {
      return iterator.next().getItem();
    }

    public void remove() {
      iterator.remove();
    }

  }
}
