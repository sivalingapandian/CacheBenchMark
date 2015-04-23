package com.medhost.cache.tester;

import com.medhost.cache.DistributedCache;
import com.medhost.cache.distributed.DistributedCacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pandian on 4/20/15.
 */
public class CacheCommandLineTester implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(DistributedCacheLoader.class);
    private String operation = "load";
    private int numberOfProperty = 0;

    public static void main(String[] args) {
        if( "load".equals(args[0]) ) {
            //load
            new CacheCommandLineTester().load(Integer.parseInt(args[1]));
        } else {
            //Read
            new CacheCommandLineTester().read(Integer.parseInt(args[1]));
        }
    }

    public void load(int numberOfProperty) {
        //Load
        DistributedCacheLoader loader = new DistributedCacheLoader();
        for(int noOfKeyValue = 0; noOfKeyValue < numberOfProperty; noOfKeyValue++) {
            String key = "key" + noOfKeyValue;
            String value = "value" + noOfKeyValue;
            loader.loadData(key, value);
        }
        new Thread( this ).start();
    }

    public void read(int numberOfProperty) {
        //read
        operation = "read";
        this.numberOfProperty = numberOfProperty;
        new Thread( this ).start();
    }


    public void run() {
        while(true) {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                logger.error("Thread sleeping interrupted");
                return;
            }

            if( "read".equalsIgnoreCase(operation) ) {
                DistributedCacheLoader loader = new DistributedCacheLoader();
                for(int noOfKeyValue = 0; noOfKeyValue < numberOfProperty; noOfKeyValue++) {
                    String key = "key" + noOfKeyValue;
                    System.out.println(key + ":" + loader.getData(key));
                }
            }
        }
    }

}
