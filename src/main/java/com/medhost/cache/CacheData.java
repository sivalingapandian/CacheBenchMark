package com.medhost.cache;

import java.sql.SQLException;

/**
 * Interface to access different type of cache
 *
 * Created by pandian on 4/15/15.
 */
public interface CacheData {

    void loadData(String data, String value);
    String getData(String key);
    void unloadData(String key);

}
