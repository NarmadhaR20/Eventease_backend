package com.eventease.dao;

import com.eventease.model.Booking;

import java.sql.*;
import java.util.List;

public class BookingDAO {
    private Connection conn;

    public BookingDAO(Connection conn) {
        this.conn = conn;
    }

    // Insert booking into store_booking table
    public int createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO store_booking (user_id, event_name, total_amount, number_of_seats, status, ticket_downloaded) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, booking.getUserId());
            pst.setString(2, booking.getEventName());
            pst.setDouble(3, booking.getTotalAmount());
            pst.setInt(4, booking.getNumberOfSeats());
            pst.setString(5, booking.getStatus());
            pst.setInt(6, booking.getTicketDownloaded());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // return booking_id
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        }
    }

    // Insert booked seats into booking_seats table with transaction
    public void insertBookingSeats(int bookingId, List<String> seatIds) throws SQLException {
        if (seatIds == null || seatIds.isEmpty()) return;

        String sql = "INSERT INTO booking_seats (booking_id, seat_id) VALUES (?, ?)";
        boolean autoCommit = conn.getAutoCommit();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // start transaction

            for (String seatId : seatIds) {
                pst.setInt(1, bookingId);
                pst.setString(2, seatId); // seat_id as VARCHAR
                pst.addBatch();
            }
            pst.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback(); // rollback on error
            throw e;
        } finally {
            conn.setAutoCommit(autoCommit); // restore auto-commit
        }
    }
}
