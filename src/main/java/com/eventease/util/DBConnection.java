package com.eventease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String HOST = "mysql.railway.internal";
    private static final String PORT = "3306";
    private static final String DATABASE = "railway";
    private static final String USER = "root";
    private static final String PASSWORD = "YGNERyiIrwWYfYFhXJzqWEMBDEWiCtZM";

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=true&requireSSL=true&verifyServerCertificate=false&allowPublicKeyRetrieval=true";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection FAILED");
            e.printStackTrace();
            return null;
        }
    }
}
