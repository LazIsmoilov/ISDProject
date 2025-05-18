package uts.isd.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DBConnector {
    private Connection connection;

    public DBConnector() {
        System.setProperty("org.sqlite.lib.verbose", "true");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(">> Working dir = " + new java.io.File(".").getAbsolutePath());


        String dbPath = "/Users/qinhaoranzhou/Desktop/ISDProject-main/iotBayDatabase.db";
        String url = "jdbc:sqlite:" + dbPath;

        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);
            System.out.println("Connected to database at: " + dbPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}