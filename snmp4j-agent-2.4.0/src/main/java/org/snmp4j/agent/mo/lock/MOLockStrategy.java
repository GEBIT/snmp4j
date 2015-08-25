package org.snmp4j.agent.mo.lock;

import org.snmp4j.agent.MOQuery;
import org.snmp4j.agent.ManagedObject;

/**
 * The <code>MOLockStrategy</code> interface defines a strategy for locking {@link org.snmp4j.agent.ManagedObject}
 * instances when they are accessed through a {@link org.snmp4j.agent.MOServer}.
 *
 * @author Frank Fock
 * @since 2.4.0
 */
public interface MOLockStrategy {

  /**
   * Check if the server access to the provided managed object needs a lock.
   * @param managedObjectLookedUp
   *    the ManagedObject instance that is looked up and potentially accessed.
   * @param query
   *    the query on which behalf the lookup took place. It also signals with
   *    {@link MOQuery#isWriteAccessQuery()} whether a write access is intended or not.
   * @return
   *    <code>true</code> if a lock is required to access the provided managed object,
   *    <code>false</code> otherwise.
   */
  boolean isLockNeeded(ManagedObject managedObjectLookedUp, MOQuery query);

}
