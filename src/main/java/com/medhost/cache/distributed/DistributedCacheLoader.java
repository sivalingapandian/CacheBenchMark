package com.medhost.cache.distributed;

import com.medhost.cache.CacheData;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Distributed Cache loader helps to load/read/delete data into embedded data grid cache
 *
 * Created by pandian on 4/20/15.
 */
public class DistributedCacheLoader implements CacheData {

    private static Cache<String, String> cache = null;
    private static Logger logger = LoggerFactory.getLogger(DistributedCacheLoader.class);

    public static final String CACHE_NAME = "default";

    //TODO: move this code out of static block
    static {
        try {
            Configuration replicationConfiguration = new ConfigurationBuilder()
                    .clustering()
                    .cacheMode(CacheMode.REPL_SYNC)
                    .build();

            GlobalConfiguration configurationFile = GlobalConfigurationBuilder.defaultClusteredBuilder()
                    .transport().nodeName(null).addProperty("configurationFile", "/home/pandian/bin/jmeter/jgroups.xml")
                    .build();

            EmbeddedCacheManager cacheManager = new DefaultCacheManager(configurationFile, replicationConfiguration);

            // The only way to get the "repl" cache to be exactly the same as the default cache is to not define it at all
            cacheManager.defineConfiguration("dist", new ConfigurationBuilder()
                            .clustering()
                            .cacheMode(CacheMode.DIST_SYNC)
                            .hash().numOwners(2)
                            .build()
            );
            cache = cacheManager.getCache(CACHE_NAME);

        } catch(Throwable th) {
            logger.error("Unable to instantiate embeded cache", th);
        }
    }

    @Override
    public void loadData(String data, String value) {
        cache.put(data, value);
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
