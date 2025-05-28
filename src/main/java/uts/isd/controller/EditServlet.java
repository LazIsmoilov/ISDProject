package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.User;
import uts.isd.model.dao.DAO;
import uts.isd.model.dao.UserDBManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            session.setAttribute("error", "Session expired or user not logged in");
            resp.sendRedirect("login.jsp");
            return;
        }

        // Check if the user is an admin
        if (!loggedInUser.isAdmin()) {
            session.setAttribute("error", "Unauthorized access");
            resp.sendRedirect("index.jsp");
            return;
        }

        DAO dao = (DAO) session.getAttribute("db");
        if (dao == null) {
            session.setAttribute("error", "Database not initialized in session");
            resp.sendRedirect("admin.jsp");
            return;
        }

        UserDBManager db = dao.Users();
        if (db == null) {
            session.setAttribute("error", "UserDBManager not initialized");
            resp.sendRedirect("admin.jsp");
            return;
        }

        // Get userId from form
        String userIdParam = req.getParameter("userId");
        int userId;
        try {
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid user ID provided");
            resp.sendRedirect("admin.jsp");
            return;
        }

        // Fetch the user to edit
        User userToEdit;
        try {
            userToEdit = db.getById(userId);
            if (userToEdit == null) {
                session.setAttribute("error", "User not found for ID: " + userId);
                resp.sendRedirect("admin.jsp");
                return;
            }
        } catch (SQLException e) {
            session.setAttribute("error", "Database error: " + e.getMessage());
            resp.sendRedirect("admin.jsp");
            return;
        }

        String fullName = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String dob = req.getParameter("dob");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        String role = req.getParameter("role");
        boolean isActive = req.getParameter("isActive") != null;

        if (password == null || password.trim().isEmpty()) {
            password = userToEdit.getPassword(); // Retain existing password if not provided
        }

        try {
            User updatedUser = new User(
                    userId,
                    fullName,
                    email,
                    password,
                    dob,
                    gender,
                    phone,
                    role,
                    isActive
            );
            db.update(userToEdit, updatedUser);

            // Update loggedInUser only if the admin is editing their own profile
            if (userId == loggedInUser.getUserId()) {
                session.setAttribute("loggedInUser", updatedUser);
            }

            // Refresh user list for admin.jsp
            List<User> users = db.getAllUsers();
            session.setAttribute("users", users);

            session.setAttribute("message", "User profile updated successfully!");
            resp.sendRedirect("admin.jsp");
        } catch (SQLException | IllegalArgumentException e) {
            session.setAttribute("error", "Error updating profile: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("edit.jsp?userId=" + userId);
        }
    }
}