package com.horizons.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This is a singleton class which manages our connection to the MYSQL Database
 */
public class AppDatabase {

    private static Connection databaseLink;

    private AppDatabase(){}

    /**
     * @return a connection to the MYSQL Database
     */
    public static synchronized Connection getConnection() {
        if (databaseLink == null) {
            String databaseName = "schoolDatabase";
            String databaseUser = "YOUR MYSQL CONNECTION USERNAME";
            String databasePassword = "YOUR MYSQL CONNECTION PASSWORD";
            String url = "jdbc:mysql://localhost:3306/" + databaseName + "?allowMultiQueries=true";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            return databaseLink;
        }
        return databaseLink;
    }

}
