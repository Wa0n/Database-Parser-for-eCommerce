package com.royalkid.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Maintains database connection operations.
 *
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class DBConnector {
    private static DBConnector thisInstance = new DBConnector();
    private Connection connection;

    public static DBConnector getInstance() {
        return thisInstance;
    }

    /**
     * Don't let anyone instantiate this class
     */
    private DBConnector() {
        this.connection = null;
        DBConfiguration.getInstance().setupDBConfiguration();
    }

    /**
     * Sets up configuration parameters and gets connection to the remote database.
     *
     * @return opened connection to the remote database
     */
    public Connection openConnection() {
        System.out.println("Connecting to database");

        if (connection != null) {
            System.out.println("Connection already opened");
            return connection;
        }

        DBConfiguration configuration = DBConfiguration.getInstance();

        String host = configuration.getHost();
        String port = configuration.getPort();
        String loginName = configuration.getLoginName();
        String password = configuration.getPassword();
        String schema = configuration.getSchema();
        String dbms = configuration.getDbms();

        try {
            Class.forName("com." + dbms + ".jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("Can not get JDBC driver");
            shutdown();
        }

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", loginName);
        connectionProperties.put("password", password);

        String URL = "jdbc:" + dbms + "://" + host + ":" + port + "/" + schema;

        try {
            connection = DriverManager.getConnection(URL, connectionProperties);
        } catch (SQLException e) {
            System.out.println("Can not connect to database");
            shutdown();
        }

        return connection;
    }

    /**
     * Closes connection to remote database if it is not closed.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Can not disconnect from database");
            }
            connection = null;
        }
    }

    /**
     * Prints system message and shuts down an application with error code 1.
     */
    private void shutdown() {
        System.out.print("Shutdown application");
        System.exit(1);
    }
}
