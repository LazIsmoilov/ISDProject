package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.UserDBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("user", session.getAttribute("user"));
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");

        user.setFullName(fullName);
        user.setPhone(phone);

        // Get DB connection from servlet context (make sure it's set up)
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        if (connection == null) {
            throw new ServletException("Database connection not initialized.");
        }

        try {
            UserDBManager userDBManager = new UserDBManager(connection);

            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(newPassword);
                userDBManager.updatePassword(user.getUserId(), newPassword);
            }

            User oldUser = (User) session.getAttribute("user"); // the old user details from session
            User newUser = user; // the updated user object you just changed

            userDBManager.update(oldUser, newUser);

            // update session with new user info
            session.setAttribute("user", newUser);

            request.setAttribute("success", "Your profile has been updated successfully.");
            request.setAttribute("user", user);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
