package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.UserDBManager;
import uts.isd.model.dao.DBConnector;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDBManager db = (UserDBManager) session.getAttribute("db");

        // ✅ Lazy init if not yet available in session
        if (db == null) {
            try {
                DBConnector connector = new DBConnector();
                db = new UserDBManager(connector.getConnection());
                session.setAttribute("db", db);
            } catch (Exception e) {
                throw new ServletException("Cannot initialize UserDBManager", e);
            }
        }

        // ✅ Get form inputs
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String phone = request.getParameter("phone"); // optional if used

        try {
            // ✅ Check if user already exists
            User existing = db.findUser(email, password);
            if (existing != null) {
                session.setAttribute("registerError", "Email already registered.");
                response.sendRedirect("register.jsp");
                return;
            }

            // ✅ Create and register new user
            User newUser = new User(fullName, email, password, phone, role);
            db.addUser(newUser);

            session.setAttribute("registerSuccess", "Registration successful. Please login.");
            response.sendRedirect("login.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("registerError", "Registration failed: " + e.getMessage());
            response.sendRedirect("register.jsp");
        }
    }
}
