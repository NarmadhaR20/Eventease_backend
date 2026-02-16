package com.eventease.controller;

import com.eventease.dao.UserDAO;

import com.eventease.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.json.JSONObject;

public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println("Received POST request with action: " + action);

        try {
            if ("register".equals(action)) {
                // Registration
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                if (username == null || email == null || password == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("text/plain; charset=UTF-8");
                    response.getWriter().write("Missing parameters");
                    return;
                }

                User user = new User(0, username, email, password);
                if (userDAO.registerUser(user)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("text/plain; charset=UTF-8");
                    response.getWriter().write("Registration successful");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("text/plain; charset=UTF-8");
                    response.getWriter().write("Registration failed");
                }

            } else if ("login".equals(action)) {
                // Login
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                if (email == null || password == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json; charset=UTF-8");
                    JSONObject json = new JSONObject();
                    json.put("status", "fail");
                    json.put("message", "Missing credentials");
                    response.getWriter().write(json.toString());
                    return;
                }

                User user = userDAO.loginUser(email, password);
                response.setContentType("application/json; charset=UTF-8");
                JSONObject json = new JSONObject();

                if (user != null) {
                    // Store user in session
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);

                    // Send JSON response
                    json.put("status", "success");
                    JSONObject userJson = new JSONObject();
                    userJson.put("userId", user.getUserId());
                    userJson.put("username", user.getUsername());
                    userJson.put("email", user.getEmail());
                    json.put("user", userJson);

                    response.getWriter().write(json.toString());
                } else {
                    json.put("status", "fail");
                    json.put("message", "Invalid credentials");
                    response.getWriter().write(json.toString());
                }

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("Invalid action");
            }
        } catch (Exception e) {
            System.err.println("Exception in AuthServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            JSONObject json = new JSONObject();
            json.put("status", "error");
            json.put("message", e.getMessage());
            response.getWriter().write(json.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("GET method not supported for this endpoint");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            String email = request.getParameter("email");
            String newUsername = request.getParameter("username");
            String newPassword = request.getParameter("password");

            if (email == null || newUsername == null || newPassword == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing parameters");
                return;
            }
            response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            response.getWriter().write("Update not implemented yet");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid action");
        }
    }
}
