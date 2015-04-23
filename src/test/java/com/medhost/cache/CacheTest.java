package com.medhost.cache;

import org.junit.Assert;

import java.util.HashMap;

/**
 * Created by pandian on 4/20/15.
 */
public class CacheTest {

    public static final int TOTAL_ENTRY = 3;
    CacheData cache;
    HashMap<String,String> configuration = new HashMap<String, String>();

    public void setUp() throws Exception {
        for(int noOfKeyValue = 0; noOfKeyValue < TOTAL_ENTRY; noOfKeyValue++) {
            String key = "key" + noOfKeyValue;
            String value = "value" + noOfKeyValue;
            configuration.put(key, value);
            cache.loadData(key, value);
        }
    }

    public void tearDown() throws Exception {
        configuration.keySet().forEach(key -> cache.unloadData(key));
    }

    public void shouldReturnSameValueStoredIntoCache() throws Exception {
        configuration.keySet().forEach(key -> Assert.assertEquals(cache.getData(key), configuration.get(key)));
    }
}
