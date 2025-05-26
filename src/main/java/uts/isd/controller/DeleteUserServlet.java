package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.UserDBManager;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/delete")
public class DeleteUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Retrieve DB connection from somewhere, maybe stored in session or context
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        if (connection == null) {
            throw new ServletException("Database connection not initialized.");
        }

        UserDBManager userDBManager;
        try {
            userDBManager = new UserDBManager(connection);
            userDBManager.delete(user);
        } catch (SQLException e) {
            throw new ServletException("Error deleting user", e);
        }

        session.invalidate();
        response.sendRedirect("login.jsp");
    }
}
