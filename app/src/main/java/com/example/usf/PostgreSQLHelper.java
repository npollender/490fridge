package com.example.usf;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class PostgreSQLHelper {

    String URL = "";
    String Username = "";
    String Password = "";

    public static void main(String[] args) {

    }

    public Connection getConnection() {

        Connection result = null;

        try {
            result = DriverManager.getConnection(URL, Username, Password);
            DatabaseMetaData dbmd = result.getMetaData();

            System.out.println(dbmd.getDatabaseProductName());
        }
        catch (Exception ignored) {}

        return result;
    }
}