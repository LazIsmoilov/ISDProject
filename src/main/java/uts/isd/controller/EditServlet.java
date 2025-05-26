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

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("db");
        if (dao == null) {
            session.setAttribute("error", "Database not initialized in session");
            resp.sendRedirect("index.jsp");
            return;
        }

        UserDBManager db = dao.Users();
        if (db == null) {
            session.setAttribute("error", "UserDBManager not initialized");
            resp.sendRedirect("index.jsp");
            return;
        }

        User existingUser = (User) session.getAttribute("loggedInUser");
        if (existingUser == null) {
            session.setAttribute("error", "Session expired or user not logged in");
            resp.sendRedirect("login.jsp");
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
            password = existingUser.getPassword();
        }

        try {
            User updatedUser = new User(
                    existingUser.getUserId(),
                    fullName,
                    email,
                    password,
                    dob,
                    gender,
                    phone,
                    role,
                    isActive
            );
            db.update(existingUser, updatedUser);
            session.setAttribute("loggedInUser", updatedUser);
            session.setAttribute("message", "Profile updated successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            session.setAttribute("error", "Error updating profile: " + e.getMessage());
            e.printStackTrace();
        }

        resp.sendRedirect("edit.jsp");
    }
}