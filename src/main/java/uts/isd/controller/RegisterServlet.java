package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.dao.DAO;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // No logic for GET
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // —— 新增：确保 session 里有 db ——
        DAO db = (DAO) session.getAttribute("db");
        if (db == null) {
            try {
                db = new DAO();
                session.setAttribute("db", db);
            } catch (SQLException e) {
                throw new ServletException("Unable to create DAO", e);
            }
        }

        String name      = req.getParameter("name");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");
        String dob       = req.getParameter("dob");
        String gender    = req.getParameter("gender");
        boolean agreed   = req.getParameter("tos") != null;

        session.setAttribute("name", name);

        if (agreed) {
            User user = new User(name, email, password, dob, gender);
            session.setAttribute("loggedInUser", user);
            try {
                db.Users().add(user);
            } catch (SQLException e) {
                throw new ServletException("Error adding user", e);
            }
        }
        resp.sendRedirect("welcome.jsp");
    }
}


