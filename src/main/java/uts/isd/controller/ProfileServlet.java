package uts.isd.controller;

import uts.isd.model.dao.DAO;
import uts.isd.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    //    check login or not, if not ,redirect to login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        //        give user to profile for showing detail
        request.setAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    //    check login status again
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        //        get new info from web
        User user = (User) session.getAttribute("loggedInUser");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");

        // update
        user.setFullName(fullName);
        user.setPhone(phone);

        try {
            DAO dao = new DAO();

            // change password
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(newPassword);
                dao.Users().updatePassword(user.getUserId(), newPassword);
            }

            // update other info
            dao.Users().update(user, user); //
            session.setAttribute("loggedInUser", user);

            // change success  test
            request.setAttribute("success", "Your profile has been updated successfully.");
            request.setAttribute("loggedInUser", user);
            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}