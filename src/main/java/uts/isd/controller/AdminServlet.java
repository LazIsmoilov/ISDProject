package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.User;
import uts.isd.model.User.UserType;
import uts.isd.model.dao.DAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO db = (DAO) session.getAttribute("db");
        String action = req.getParameter("action");

        try {
            if ("search".equals(action)) {
                String name = req.getParameter("name");
                String phone = req.getParameter("phone");
                System.out.println("Admin Search - Name: " + name + ", Phone: " + phone);
                List<User> users = db.Users().searchUsers(name, phone);
                session.setAttribute("users", users);
                resp.sendRedirect("admin.jsp");
                return;
            } else if ("toggle".equals(action)) {
                int userId = Integer.parseInt(req.getParameter("id"));
                db.Users().toggleActiveStatus(userId);
                List<User> users = db.Users().getAllUsers();
                session.setAttribute("users", users);
                resp.sendRedirect("admin.jsp");
                return;
            } else if ("delete".equals(action)) {
                int userId = Integer.parseInt(req.getParameter("id"));
                User user = new User();
                user.setId(userId);
                db.Users().delete(user);
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
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String dob = req.getParameter("dob");
            String gender = req.getParameter("gender");
            String phoneNumber = req.getParameter("phoneNumber");
            String userTypeStr = req.getParameter("userType");
            String isActiveStr = req.getParameter("isActive");

            try {
                if (name == null || name.trim().isEmpty() || email == null || !email.contains("@") || password == null || password.length() < 6) {
                    session.setAttribute("error", "Invalid input: Name, email, and password (6+ chars) are required");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                UserType userType = UserType.valueOf(userTypeStr.toUpperCase());
                // Restrict ADMIN creation (optional)
                if (userType == UserType.ADMIN) {
                    session.setAttribute("error", "Creating admin users is restricted");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                boolean isActive = "on".equals(isActiveStr) || "true".equals(isActiveStr);
                User newUser = new User(name, email, password, dob, gender, userType, phoneNumber);
                newUser.setIsActive(isActive);

                DAO db = (DAO) session.getAttribute("db");
                if (db == null) {
                    session.setAttribute("error", "Database not initialized");
                    resp.sendRedirect("userManagement.jsp");
                    return;
                }

                db.Users().add(newUser); // Use add method
                session.setAttribute("success", "User created successfully: " + email);
                resp.sendRedirect("userManagement.jsp");
            } catch (SQLException e) {
                session.setAttribute("error", "Database error: " + e.getMessage());
                resp.sendRedirect("userManagement.jsp");
            } catch (IllegalArgumentException e) {
                session.setAttribute("error", "Invalid user type: " + userTypeStr);
                resp.sendRedirect("userManagement.jsp");
            }
        } else {
            session.setAttribute("error", "Invalid action");
            resp.sendRedirect("userManagement.jsp");
        }
    }
}