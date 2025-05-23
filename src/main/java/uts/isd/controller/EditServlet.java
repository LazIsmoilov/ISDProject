package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.User;
import uts.isd.model.User.UserType;
import uts.isd.model.dao.UserDBManager;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String userIdStr = req.getParameter("userId");
        int idToEdit;
        try {
            idToEdit = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid user ID: " + userIdStr);
            resp.sendRedirect("index.jsp");
            return;
        }

        // Restrict non-admins to editing themselves
        if (!loggedInUser.isAdmin() && idToEdit != loggedInUser.getId()) {
            session.setAttribute("error", "Unauthorized: You can only edit your own profile");
            resp.sendRedirect("index.jsp");
            return;
        }

        try {
            UserDBManager userDbManager = (UserDBManager) getServletContext().getAttribute("userDBManager");
            if (userDbManager == null) {
                session.setAttribute("error", "Database not initialized");
                resp.sendRedirect("index.jsp");
                return;
            }
            User existingUser = userDbManager.getById(idToEdit);
            if (existingUser == null) {
                session.setAttribute("error", "User not found for ID: " + idToEdit);
                resp.sendRedirect("index.jsp");
                return;
            }

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String dob = req.getParameter("dob");
            String gender = req.getParameter("gender");
            String phoneNumber = req.getParameter("phoneNumber");
            String userTypeStr = req.getParameter("userType");
            String isActiveStr = req.getParameter("isActive");

            UserType userType = existingUser.getType();
            boolean isActive = existingUser.getIsActive();
            if (loggedInUser.isAdmin()) {
                userType = userTypeStr != null ? UserType.valueOf(userTypeStr) : userType;
                isActive = "on".equals(isActiveStr) || "true".equals(isActiveStr);
            }

            if (name == null || name.trim().isEmpty() || email == null || !email.contains("@")) {
                session.setAttribute("error", "Invalid input: Name and email are required");
                resp.sendRedirect("edit.jsp?userId=" + idToEdit);
                return;
            }

            String finalPassword = (password == null || password.trim().isEmpty()) ? existingUser.getPassword() : password;

            User newUser = new User(idToEdit, name, email, finalPassword, dob, gender, userType, phoneNumber, isActive);
            userDbManager.update(existingUser, newUser);

            if (idToEdit == loggedInUser.getId()) {
                session.setAttribute("loggedInUser", newUser);
            }

            session.setAttribute("success", "User ID " + idToEdit + " updated successfully");
            resp.sendRedirect("index.jsp");

        } catch (SQLException e) {
            session.setAttribute("error", "Database error: " + e.getMessage());
            resp.sendRedirect("edit.jsp?userId=" + idToEdit);
        }
    }
}