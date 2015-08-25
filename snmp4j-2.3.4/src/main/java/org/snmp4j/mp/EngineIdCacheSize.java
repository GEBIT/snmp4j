package org.snmp4j.mp;

/**
 * The interface <code>EngineIdCacheSize</code> defines the maximum engine ID cache size when the cache size
 * is about to increase.
 * @since 2.3.4
 */
public interface EngineIdCacheSize {
    /**
     * Gets the maximum number of transport address to engineID mappings to be hold in the cache.
     * @return
     *    the maximum size of the engine ID cache.
     * @since 2.3.4
     */
    int getMaxEngineIdCacheSize();
}
