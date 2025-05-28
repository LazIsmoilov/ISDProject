package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.User;
import uts.isd.model.dao.DAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DAO db = (DAO) req.getSession().getAttribute("db");
        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        try {
            if ("search".equals(action)) {
                String name = req.getParameter("fullName");
                String phone = req.getParameter("phone");

                List<User> users = db.Users().searchUsers(name, phone);
                session.setAttribute("users", users);
                resp.sendRedirect("admin.jsp");
                return;
            } else if ("toggle".equals(action)) {
                int userId = Integer.parseInt(req.getParameter("userId"));
                db.Users().toggleActiveStatus(userId);

                // Refresh user list after toggling status
                List<User> users = db.Users().getAllUsers();
                session.setAttribute("users", users);
                resp.sendRedirect("admin.jsp");
                return;
            } else if ("delete".equals(action)) {
                int userId = Integer.parseInt(req.getParameter("userId"));
                User user = new User();
                user.setUserId(userId);
                db.Users().delete(user);

                // Refresh user list after deletion
                List<User> users = db.Users().getAllUsers();
                session.setAttribute("users", users);
                resp.sendRedirect("admin.jsp");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Database error occurred.");
            resp.sendRedirect("admin.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null || !currentUser.isAdmin()) {
            session.setAttribute("error", "Unauthorized access");
            resp.sendRedirect("index.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("create".equals(action)) {
            String name = req.getParameter("fullName");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String dob = req.getParameter("dob");
            String gender = req.getParameter("gender");
            String phoneNumber = req.getParameter("phone");
            String role = req.getParameter("role");
            String isActiveStr = req.getParameter("isActive");

            try {
                if (name == null || name.trim().isEmpty()) {
                    session.setAttribute("error", "Invalid input: Name is required");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }
                if (email == null || !email.contains("@")) {
                    session.setAttribute("error", "Invalid input: Valid email is required");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }
                if (password == null || password.length() < 6) {
                    session.setAttribute("error", "Invalid input: Password must be 6 or more characters");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }
                if (gender == null || (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female") && !gender.equalsIgnoreCase("Other"))) {
                    session.setAttribute("error", "Invalid input: Gender must be Male, Female, or Other");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }
                if (phoneNumber != null && !phoneNumber.isEmpty() && !phoneNumber.matches("\\d{10}")) {
                    session.setAttribute("error", "Invalid input: Phone number must be 10 digits");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }
                if (role == null || role.trim().isEmpty()) {
                    session.setAttribute("error", "Invalid input: Role is required");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                User.UserType type;
                role = role.trim();
                if ("customer".equalsIgnoreCase(role)) {
                    type = User.UserType.CUSTOMER;
                } else if ("staff".equalsIgnoreCase(role)) {
                    type = User.UserType.STAFF;
                } else {
                    session.setAttribute("error", "Invalid role: " + role);
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                if (type == User.UserType.ADMIN) {
                    session.setAttribute("error", "Creating admin users is restricted");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                boolean isActive = "on".equals(isActiveStr) || "true".equals(isActiveStr);
                User newUser = new User();
                newUser.setFullName(name);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setDob(dob);
                newUser.setGender(gender);
                newUser.setType(type);
                newUser.setPhone(phoneNumber);
                newUser.setIsActive(isActive);

                DAO db = (DAO) session.getAttribute("db");
                if (db == null) {
                    session.setAttribute("error", "Database not initialized");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                db.Users().add(newUser);
                session.setAttribute("success", "User created successfully: " + email);
                resp.sendRedirect("userManagement.jsp");
            } catch (SQLException e) {
                session.setAttribute("error", "Database error: " + e.getMessage());
                resp.sendRedirect("userManagement.jsp");
            } catch (IllegalArgumentException e) {
                session.setAttribute("error", "Error creating user: " + e.getMessage());
                resp.sendRedirect("userManagement.jsp");
            }
        } else {
            session.setAttribute("error", "Invalid action");
            resp.sendRedirect("userManagement.jsp");
        }
    }
}