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

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO db = (DAO) session.getAttribute("db");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String dob = req.getParameter("dob");
        String gender = req.getParameter("gender");

        User existingUser = (User) session.getAttribute("loggedInUser");
        User newUser = new User(existingUser.getId(), name, email, password, dob, gender);

        try {
            db.Users().update(existingUser, newUser);
            session.setAttribute("loggedInUser", newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.sendRedirect("index.jsp");
    }
}

