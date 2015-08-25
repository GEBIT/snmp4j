package org.snmp4j.agent.mo.lock;

import org.snmp4j.agent.MOQuery;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.UpdatableManagedObject;

/**
 * The <code>DefaultMOLockStrategy</code> implements a simple default locking strategy that
 * requires a lock if a write access on a {@link ManagedObject} is intended or if the managed object
 * accessed is an instance of {@link UpdatableManagedObject}.
 * A managed object server that uses this lock strategy ensures that two concurrently received
 * SET requests will not modify the same managed object at the same time with probably undefined result.
 * In addition, managed objects that need to be updated regularly are protected against access while
 *
 *
 * @author Frank Fock
 * @since 2.4.0
 */
public class DefaultMOLockStrategy implements MOLockStrategy {

  @Override
  public boolean isLockNeeded(ManagedObject managedObjectLookedUp, MOQuery query) {
    return query.isWriteAccessQuery() || (managedObjectLookedUp instanceof UpdatableManagedObject);
  }
}
