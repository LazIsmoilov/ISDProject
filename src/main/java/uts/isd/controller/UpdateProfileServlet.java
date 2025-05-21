package uts.isd.controller;


import uts.isd.model.dao.UserDAO;
import uts.isd.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {

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

        try {
            UserDAO userDAO = new UserDAO();

            // change password
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(newPassword);
                userDAO.updatePassword(user.getUserId(), newPassword);
            }

            // update info
            userDAO.updateUser(user);
            session.setAttribute("user", user);
            response.sendRedirect("profile.jsp");

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}