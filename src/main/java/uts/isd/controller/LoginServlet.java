package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.DBConnector;
import uts.isd.model.dao.UserDBManager;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDBManager db = (UserDBManager) session.getAttribute("db");

        // ✅ Re-initialize db if it was cleared by logout
        if (db == null) {
            try {
                DBConnector connector = new DBConnector();
                db = new UserDBManager(connector.getConnection());
                session.setAttribute("db", db);
            } catch (Exception e) {
                throw new ServletException("Cannot initialize UserDBManager", e);
            }
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = db.findUser(email, password);

            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect("main.jsp");
            } else {
                session.setAttribute("loginError", "Invalid email or password.");
                response.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("loginError", "Login failed: " + e.getMessage());
            response.sendRedirect("login.jsp");
        }
    }
}
