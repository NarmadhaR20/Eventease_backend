package com.eventease.controller;

import com.eventease.dao.SeatDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BookSeatServlet extends HttpServlet {
    private SeatDAO seatDAO;

    @Override
    public void init() throws ServletException {
        seatDAO = new SeatDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String[] seatNumbers = request.getParameterValues("seatNumbers[]");
        if (seatNumbers == null || seatNumbers.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("No seats selected");
            return;
        }

        List<String> seatList = Arrays.asList(seatNumbers);
        try {
            if (seatDAO.bookSeats(seatList)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Seats booked successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Some seats are already booked");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}