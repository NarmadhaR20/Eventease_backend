package com.eventease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/eventease_storage?useSSL=false";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "Narmadha@r123"; // Replace with your MySQL password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC Driver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established: " + URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("SQLException in getConnection: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
