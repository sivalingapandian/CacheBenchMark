package com.medhost.cache;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

/**
 * Created by pandian on 4/20/15.
 */
public class DistributedEmbededCache implements  CacheData {

    private static Cache<String, String> cache = null;

    static {
        cache = new DefaultCacheManager().getCache();
    }

    @Override
    public void loadData(String data, String value) {
        cache.put(data,value);
    }

    @Override
    public String getData(String key) {
        return cache.get(key);
    }

    @Override
    public void unloadData(String key) {
        cache.remove(key);
    }
}
