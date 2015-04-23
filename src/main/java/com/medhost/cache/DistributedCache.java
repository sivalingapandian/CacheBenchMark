package com.medhost.cache;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pandian on 4/15/15.
 */
public class DistributedCache implements CacheData {

    private static String serverUrl = "http://localhost:60053/rest";
    private static String cacheName = "default";
    private static String PUT = "PUT";

    public void loadData(String data, String value) {
        doOperation("PUT", data, value);
    }

    public String getData(String key) {
        return doOperation("GET",key,null);
    }

    @Override
    public void unloadData(String key) {
        doOperation("DELETE", key, null);
    }

    private String doOperation(String method, String key, Object value) {

        URL url = null;
        try {
            url = new URL(serverUrl + "/" + cacheName + "/" + key);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "text/plain");
            int read = 0;
            byte[] buffer = new byte[1024 * 8];

            if (method.equals(PUT)) {
                connection.setDoOutput(true);
                String payload = value.toString();
                BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(payload.getBytes());
                output.close();
            }

            connection.connect();
            InputStream responseBodyStream = connection.getInputStream();
            StringBuffer responseBody = new StringBuffer();
            while ((read = responseBodyStream.read(buffer)) != -1) {
                responseBody.append(new String(buffer, 0, read));
            }
            connection.disconnect();
            String response = responseBody.toString();
            return response;
        } catch (FileNotFoundException fnfe) {
            // Could be that the key being queried does not exist. Return null.
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to Infinispan. URL:" + url + ".", e);
        }
    }
}
