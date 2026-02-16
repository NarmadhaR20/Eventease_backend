package com.eventease.dao;

import com.eventease.model.Seat;
import com.eventease.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

	public List<Seat> getAllSeats() throws SQLException {
	    List<Seat> seats = new ArrayList<>();
	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = DBConnection.getConnection();
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery("SELECT * FROM seats");

	        while (rs.next()) {
	            seats.add(new Seat(
	                rs.getInt("seat_id"),
	                rs.getString("seat_number"),
	                rs.getBoolean("available")
	            ));
	        }
	    } finally {
	        if (rs != null) rs.close();
	        if (stmt != null) stmt.close();
	        if (conn != null) conn.close();
	    }
	    return seats;
	}

	//handle avoid double booking
    public boolean bookSeats(List<String> seatNumbers) throws SQLException {
    	//here i update the available seat false to avoid double booking
        String sql = "UPDATE seats SET available = FALSE WHERE seat_number = ? AND available = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
        	// Start transaction
            conn.setAutoCommit(false);

            for (String seatNumber : seatNumbers) {
                stmt.setString(1, seatNumber);
                int rowsAffected = stmt.executeUpdate();
                // If seat already booked, rollback everything
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // Seat already booked
                }
            }
             // Commit only if all seats updated
            conn.commit();
            return true;
        }
    }
}
