package com.medhost.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.Cache;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * In-memory ehCache
 *
 * Created by pandian on 4/16/15.
 */
public class EhCache implements CacheData {

    public static final int maxEntriesLocalHeap = 2000 * 1024;
    public static final String TEST_CACHE = "testCache";
    private static CacheManager manager = null;

    static {
        //Create a singleton CacheManager using defaults
        manager = CacheManager.create();

        //Create a Cache specifying its configuration.
        Cache testCache = new Cache(
                new CacheConfiguration(TEST_CACHE, maxEntriesLocalHeap)
                        .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                        .eternal(false)
                        .timeToLiveSeconds(300)
                        .timeToIdleSeconds(300)
                        .diskExpiryThreadIntervalSeconds(0)
                        .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE)));
        manager.addCache(testCache);
    }

    public void loadData(String data, String value) {
        getCache().put(new Element(data, value));
    }

    public String getData(String key) {
        Element element = getCache().get(key);
        if( element == null ) {
            return null;
        }
        return (String)element.getObjectValue();
    }

    @Override
    public void unloadData(String key) {
        getCache().remove(key);
    }

    private Cache getCache() {
        return manager.getCache(TEST_CACHE);
    }
}
