package com.eventease.controller;

import com.eventease.dao.SeatDAO;
import com.eventease.model.Seat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/SeatServlet")
public class SeatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {

            SeatDAO seatDAO = new SeatDAO();
            List<Seat> seats = seatDAO.getAllSeats();

            if (seats == null || seats.isEmpty()) {
                out.write("[]");
                return;
            }

            StringBuilder json = new StringBuilder("[");

            for (int i = 0; i < seats.size(); i++) {

                Seat seat = seats.get(i);

                json.append("{")
                    .append("\"seatId\":").append(seat.getId()).append(",")
                    .append("\"seatNumber\":\"").append(seat.getSeatNumber()).append("\",")
                    .append("\"status\":\"").append(seat.isBooked() ? "Booked" : "Available").append("\"")
                    .append("}");

                if (i < seats.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            out.write(json.toString());

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            out.write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
