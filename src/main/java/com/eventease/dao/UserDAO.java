package com.eventease.dao;

import com.eventease.model.User;
import com.eventease.util.DBConnection;
import java.sql.*; 

public class UserDAO {
    public boolean registerUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null in registerUser");
                return false;
            }
            System.out.println("Database connection established for registerUser");
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    System.out.println("User registered with ID: " + user.getId());
                }
                return true;
            }
            System.err.println("No rows affected during user registration");
            return false;
        } catch (SQLException e) {
            System.err.println("SQLException in registerUser: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public User loginUser(String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.err.println("Database connection is null in loginUser");
                return null;
            }
            System.out.println("Database connection established for loginUser");
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                System.out.println("User logged in: " + user.getEmail());
                return user;
            }
            System.err.println("No user found with email: " + email);
            return null;
        } catch (SQLException e) {
            System.err.println("SQLException in loginUser: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}