package com.medhost.cache;

import java.sql.SQLException;

/**
 * Created by pandian on 4/15/15.
 */
public interface CacheData {

    public void loadData(String data, String value);
    public String getData(String key);
    public void unloadData(String key);

}
