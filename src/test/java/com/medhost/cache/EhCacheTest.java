package com.medhost.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by pandian on 4/16/15.
 */
public class EhCacheTest extends CacheTest{

    @Before
    public void setUp() throws Exception {
        cache = new EhCache();
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