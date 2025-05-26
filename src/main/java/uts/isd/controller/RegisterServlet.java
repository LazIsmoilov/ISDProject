package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.DAO;
import uts.isd.model.dao.UserDBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO db = (DAO) session.getAttribute("db");
        if (db == null) {
            session.setAttribute("error", "Database not initialized in session");
            resp.sendRedirect("register.jsp");
            return;
        }

        UserDBManager userDbManager = db.Users();
        if (userDbManager == null) {
            session.setAttribute("error", "UserDBManager not initialized");
            resp.sendRedirect("register.jsp");
            return;
        }

        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String dob = req.getParameter("dob");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        String role = req.getParameter("role");
        boolean agreed = req.getParameter("tos") != null;

        // Convert role to lowercase to match UserDBManager storage
        if (role != null) {
            role = role.toLowerCase();
        } else {
            role = "customer"; // Default to customer if role is null
        }

        if (!agreed) {
            session.setAttribute("error", "You must agree to the Terms of Service");
            resp.sendRedirect("register.jsp");
            return;
        }

        try {
            // Check if email already exists
            User existingUser = userDbManager.find(email, null); // Null password to check email only
            if (existingUser != null) {
                session.setAttribute("error", "Email already registered: " + email);
                resp.sendRedirect("register.jsp");
                return;
            }

            User user = new User(fullName, email, password, dob, gender, phone, role);
            userDbManager.add(user);
            session.setAttribute("loggedInUser", user);

            if (user.getType() == User.UserType.ADMIN) {
                resp.sendRedirect("admin.jsp");
            } else {
                resp.sendRedirect("welcome.jsp");
            }
        } catch (SQLException e) {
            session.setAttribute("error", "Database error: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("register.jsp");
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", "Invalid input: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect("register.jsp");
        }
    }
}