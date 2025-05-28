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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        DAO db = (DAO) session.getAttribute("db");

        // If DAO not in session, create and store it
        if (db == null) {
            try {
                db = new DAO();
                session.setAttribute("db", db);
            } catch (SQLException e) {
                e.printStackTrace();
                session.setAttribute("error", "Database connection failed");
                resp.sendRedirect("login.jsp");
                return;
            }
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = db.Users().find(email, password);
            if (user != null) {
                if (!user.getIsActive()) {
                    session.setAttribute("error", "User is deactivated");
                    resp.sendRedirect("login.jsp");
                    return;
                }
                session.setAttribute("loggedInUser", user);
                if (user.isAdmin()) {
                    resp.sendRedirect("admin.jsp");
                } else {
                    resp.sendRedirect("welcome.jsp");
                }
            } else {
                session.setAttribute("error", "Invalid username or password");
                resp.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Database error occurred");
            resp.sendRedirect("login.jsp");
        }
    }
}
