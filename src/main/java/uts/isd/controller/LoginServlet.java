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

@WebServlet ("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DAO db = (DAO) req.getSession().getAttribute("db");
        HttpSession session = req.getSession();
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = db.Users().find(email, password);
            if (user != null) {
                session.setAttribute("loggedInUser", user);
                resp.sendRedirect("welcome.jsp");
            } else {
                session.setAttribute("error", "Invalid username or password");
                resp.sendRedirect("login.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
