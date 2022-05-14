package com.horizons.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class AppDatabase {

    private static Connection databaseLink;

    private AppDatabase(){}

    public static synchronized Connection getConnection() {
        if (databaseLink == null) {
            String databaseName = "schoolDatabase";
            String databaseUser = "root";
            String databasePassword = "phrank232k";
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
