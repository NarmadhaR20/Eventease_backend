package com.eventease.dao;

import com.eventease.model.Seat;

import com.eventease.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

	public List<Seat> getAllSeats() {
	    List<Seat> seats = new ArrayList<>();

	    try {
	        Connection conn = DBConnection.getConnection();

	        String sql = "SELECT id, seat_number, is_booked FROM seats";

	        PreparedStatement stmt = conn.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {

	            Seat seat = new Seat();

	            seat.setId(rs.getInt("id")); // FIXED
	            seat.setSeatNumber(rs.getString("seat_number"));
	            seat.setBooked(rs.getBoolean("is_booked"));

	            seats.add(seat);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return seats;
	}


	//handle avoid double booking
//    public boolean bookSeats(List<String> seatNumbers) throws SQLException {
//    	//here i update the available seat false to avoid double booking
//        String sql = "UPDATE seats SET available = FALSE WHERE seat_number = ? AND available = TRUE";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//        	
//        	// Start transaction
//            conn.setAutoCommit(false);
//
//            for (String seatNumber : seatNumbers) {
//                stmt.setString(1, seatNumber);
//                int rowsAffected = stmt.executeUpdate();
//                // If seat already booked, rollback everything
//                if (rowsAffected == 0) {
//                    conn.rollback();
//                    return false; // Seat already booked
//                }
//            }
//             // Commit only if all seats updated
//            conn.commit();
//            return true;
//        }
//    }
	public boolean bookSeats(List<String> seatNumbers) throws SQLException {

	    String sql =
	    "UPDATE seats SET is_booked = TRUE WHERE seat_number = ? AND is_booked = FALSE";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        conn.setAutoCommit(false);

	        for (String seatNumber : seatNumbers) {

	            stmt.setString(1, seatNumber);

	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected == 0) {

	                conn.rollback();

	                return false;
	            }
	        }

	        conn.commit();

	        return true;
	    }
	}

}
