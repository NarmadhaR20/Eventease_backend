package com.eventease.controller;

import com.eventease.dao.BookingDAO;
import com.eventease.model.Booking;
import com.eventease.util.DBConnection;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

@WebServlet("/bookSeat")
public class BookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DBConnection.getConnection()) {

            String userIdStr = request.getParameter("userId"); // must match frontend
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                response.setStatus(400);
                response.getWriter().write("Missing userId in request");
                return;
            }

            int userId = Integer.parseInt(userIdStr.trim()); // ✅ safe now

            String eventName = request.getParameter("eventName");
            if (eventName == null || eventName.trim().isEmpty()) {
                response.setStatus(400);
                response.getWriter().write("Missing eventName");
                return;
            }

            String totalAmountStr = request.getParameter("totalAmount");
            double totalAmount = totalAmountStr != null ? Double.parseDouble(totalAmountStr) : 0;

            String numberOfSeatsStr = request.getParameter("numberOfSeats");
            int numberOfSeats = numberOfSeatsStr != null ? Integer.parseInt(numberOfSeatsStr) : 1;

            String seatIdsStr = request.getParameter("seatIds");
            List<String> seatIds = new ArrayList<>();
            if (seatIdsStr != null && !seatIdsStr.isEmpty()) {
                for (String id : seatIdsStr.split(",")) {
                    seatIds.add(id.trim()); // now stored as String
                }
            }

            Booking booking = new Booking(userId, eventName, totalAmount, numberOfSeats);
            booking.setStatus("Pending");
            booking.setTicketDownloaded(0);

            BookingDAO bookingDAO = new BookingDAO(conn);
            int bookingId = bookingDAO.createBooking(booking);

            if (bookingId > 0 && !seatIds.isEmpty()) {
                bookingDAO.insertBookingSeats(bookingId, seatIds);
                String updateSql = "UPDATE store_booking SET status = ?, ticket_downloaded = ? WHERE booking_id = ?";
                try (PreparedStatement pst = conn.prepareStatement(updateSql)) {
                    pst.setString(1, "Confirmed"); // or "Paid"
                    pst.setInt(2, 1);              // ticket downloaded
                    pst.setInt(3, bookingId);
                    pst.executeUpdate();
                }
            }

            response.setContentType("text/plain");
            response.getWriter().write("Booking successful. ID: " + bookingId);

        } catch (NumberFormatException e) {
            response.setStatus(400);
            response.getWriter().write("Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("Booking failed: " + e.getMessage());
        }
    }
}
