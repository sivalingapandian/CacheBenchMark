package com.medhost.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Database is used as cache
 *
 * Created by pandian on 4/16/15.
 */
public class DatabaseCache implements CacheData {

    public static final String CONFVALUE = "confvalue";
    //private static String DB_URL = "jdbc:postgresql://10.1.5.110:5432/connex";
    private static String DB_URL = "jdbc:postgresql://localhost:5432/connex15r1";
    public static final String DB_USERNAME = "hms";
    //public static final String DB_PASSWORD = "887rpD9Ykn8846m";
    public static final String DB_PASSWORD = "password";
    private static final String INSERT = "insert into configuration_benchmark (confkey, confvalue) values (?,?)";
    private static final String SELECT = "select confvalue from configuration_benchmark where confkey = ? ";
    private static final String DELETE = "Delete from configuration_benchmark where confkey = ?";

    private static Logger logger = LoggerFactory.getLogger(DatabaseCache.class);

    private static Connection connection = null;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Unable to load postgresql", e);
        }

    }

    public void loadData(String data, String value) {

        try( PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, data);
            preparedStatement.setString(2, value);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("Unable to load data", e);
        }
    }

    public String getData(String key) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT)){
            preparedStatement.setString(1,key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if( resultSet.next() ) {
                return resultSet.getString(CONFVALUE);
            }
        } catch(SQLException sql) {
            logger.error("Unable to get data", sql);
        }
        return null;
    }

    @Override
    public void unloadData(String key) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE)){
            preparedStatement.setString(1, key);
            preparedStatement.execute();
        } catch(SQLException sqlEx) {
            logger.error("Unable to unload data", sqlEx);
        }
    }
}
