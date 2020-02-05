package com.example.usf;

import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import androidx.annotation.RequiresApi;

public class PostgreSQLHelper {

    public static String url;
    public static String user;
    public static String password;

    /**
     * formats strings for DB
     * @param s
     * @return
     */
    public static String fmtStrDB (String s) {
        if(s == "")
            return null;
        else
            return "'" + s + "'";
    }

    /**
     * executes an sql statement
     * @param query
     * @throws SQLException
     */
    public static void executedDB(String query) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.execute();
        conn.close();
    }

    /**
     * executes multiple statements in one shot
     * @param conn
     * @param queries
     * @throws SQLException
     */
    public static void executeMultipleQ(Connection conn, String[] queries) throws SQLException {
        Statement sts = conn.createStatement();
        for (String s : queries) {
            if (s != null)
                sts.addBatch(s);
        }
        sts.executeBatch();

        conn.close();
    }


    /**
     *  This for connecting to db
     * @return
     * @throws SQLException
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Datasbase Connection
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void establishDBConnection(InputStream input) {
        System.out.println("\n\nTrying to Establish Database Connection.....");

        Connection conn3 = null;

        try {
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

            conn3 = DriverManager.getConnection(url, user, password);

            if (conn3 != null) {
                System.out.println("\n\nConnected to database\n\n");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn3 != null && !conn3.isClosed()) {
                    conn3.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}