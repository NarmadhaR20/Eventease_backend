package com.eventease.controller;

import com.eventease.dao.SeatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/UpdateSeatServlet")
public class UpdateSeatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String seatsParam = request.getParameter("seatNumbers"); // Must match frontend name
        if (seatsParam == null || seatsParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "seatNumbers parameter missing");
            return;
        }

        // Split seat numbers into array/list
        String[] seatNumbers = seatsParam.split(",");

        // Call DAO to book seats
        SeatDAO dao = new SeatDAO();
        boolean success = false;
        try {
            success = dao.bookSeats(Arrays.asList(seatNumbers));
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB error");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"success\":" + success + "}");
    }
}

