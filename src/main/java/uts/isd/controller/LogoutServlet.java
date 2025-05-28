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

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);  // false = don't create if not existing

        if (session != null) {
            User user = (User) session.getAttribute("loggedInUser");
            DAO db = (DAO) session.getAttribute("db");

            if (user != null && db != null) {
                try {
                    db.accessLogs().logLogout(user.getUserId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new ServletException("Error logging logout", e);
                }
            }
            // Invalidate session to clear all data and avoid reuse
            session.invalidate();
        }

        // Redirect to login page after logout
        response.sendRedirect("login.jsp");
    }
}
