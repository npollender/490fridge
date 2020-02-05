package com.example.usf;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class PostgreSQLHelper {

    final static String URL = "";
    final static String Username = "";
    final static String Password = "";

    public static void main(String[] args) {
        getConnection();
    }

    //just a test
    public static Connection getConnection() {

        Connection result = null;

        try {
            result = DriverManager.getConnection(URL, Username, Password);
            DatabaseMetaData dbmd = result.getMetaData();

            System.out.println("Here: " + dbmd.getDatabaseProductName());
        }
        catch (Exception ignored) {System.out.println("Nothing found");}

        return result;
    }
}