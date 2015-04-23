package com.medhost.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by pandian on 4/20/15.
 */
public class DistributedEmbededCacheTest extends CacheTest{

    @Before
    public void setUp() throws Exception {
        cache = new DistributedEmbededCache();
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void shouldReturnSameValueStoredIntoCache() throws Exception {
        super.shouldReturnSameValueStoredIntoCache();
    }

}