package com.medhost.cache.tester;

import com.medhost.cache.*;
import com.medhost.cache.distributed.DistributedCacheLoader;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * CacheSampler is used to run Jmeter tests and measure performance of each call. This calss is designed to accrpt type
 * of operation and number of configuration value need to be tested.
 *
 * Created by pandian on 4/16/15.
 */
public class CacheSampler extends AbstractJavaSamplerClient{

    public static final String NOOFKEYS = "noofkeys";
    public static final String TYPE = "type";
    public static final String EHCACHE = "ehcache";
    public static final String DATABASECACHE = "databasecache";
    public static final String DISTRIBUTEDCACHE = "distributedcache";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String DISTRIBUTEDEMBEDEDCACHE = "distributedembededcache";
    public static final String DISTRIBUTEDEMBEDEDDATAGRIDCACHE = "distributeddatagridcache";
    public static final String TYPE_POSSIBLE_VALUES = "type_possible_values";
    public static final String SEPERATOR = " | ";
    public static final String TYPE_POSSIBLE_ARGUMENT_VALUES = EHCACHE + SEPERATOR + DATABASECACHE + SEPERATOR + DISTRIBUTEDCACHE
            + SEPERATOR + DISTRIBUTEDEMBEDEDCACHE + SEPERATOR + DISTRIBUTEDEMBEDEDDATAGRIDCACHE;

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(TYPE, EHCACHE);
        defaultParameters.addArgument(NOOFKEYS, "3");
        defaultParameters.addArgument(TYPE_POSSIBLE_VALUES, TYPE_POSSIBLE_ARGUMENT_VALUES);
        return defaultParameters;
    }

    private CacheData cacheData = null;

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        initializeCache(javaSamplerContext);

        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

        int noOfKeyValue = javaSamplerContext.getIntParameter(NOOFKEYS);

        for(int keyCount = 0; keyCount < noOfKeyValue ; keyCount++) {
            String key = KEY + keyCount;
            String value = VALUE + keyCount;
            String result = cacheData.getData(key);
            if( !value.equals(result)) {
                //Failed
                sampleResult.sampleEnd();
                sampleResult.setSuccessful(false);
                sampleResult.setResponseCode("500");
                sampleResult.setResponseMessage("Could not find cache entry");
                return sampleResult;
            }
        }
        //Passed
        sampleResult.sampleEnd();
        sampleResult.setSuccessful(true);
        sampleResult.setResponseCodeOK();
        sampleResult.setResponseMessage("Correct cache data found");
        return sampleResult;
    }

    private void initializeCache(JavaSamplerContext javaSamplerContext) {

        if(cacheData == null) {
            switch(javaSamplerContext.getParameter(TYPE)) {
                case EHCACHE:
                    cacheData = new EhCache();
                    break;
                case DATABASECACHE:
                    cacheData = new DatabaseCache();
                    break;
                case DISTRIBUTEDCACHE:
                    cacheData = new DistributedCache();
                    break;
                case DISTRIBUTEDEMBEDEDCACHE:
                    cacheData = new DistributedEmbededCache();
                    break;
                case DISTRIBUTEDEMBEDEDDATAGRIDCACHE:
                    cacheData = new DistributedCacheLoader();
                    break;
                default:
                    cacheData = new EhCache();
            }

            /*
            Do not load configuration into cache when it is data grid. This will be loaded
             by external JVM
             */

            if( !DISTRIBUTEDEMBEDEDDATAGRIDCACHE.equals(javaSamplerContext.getParameter(TYPE))) {
                int noOfKeyValue = javaSamplerContext.getIntParameter(NOOFKEYS);
                for (int keyCount = 0; keyCount < noOfKeyValue; keyCount++) {
                    String key = KEY + keyCount;
                    String value = VALUE + keyCount;
                    //cacheData.unloadData(key);
                    cacheData.loadData(key, value);
                }
            }

        }
    }
}
