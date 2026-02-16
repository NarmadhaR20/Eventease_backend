package com.eventease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://ballast.proxy.rlwy.net:34780/railway" +
        "?useSSL=true&requireSSL=true&verifyServerCertificate=false&allowPublicKeyRetrieval=true";

    private static final String USER = "root";

    private static final String PASSWORD = "YGNERyiIrwWYfYFhXJzqWEMBDEWiCtZM";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database connected successfully");

        } catch (SQLException e) {

            System.out.println("Database connection failed");
            e.printStackTrace();
        }

        return conn;
    }
}
