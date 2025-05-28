package uts.isd.controller;

import uts.isd.model.dao.DAO;
import uts.isd.model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete")
public class DeleteUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

//        get user info from session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

//        update dao
        User user = (User) session.getAttribute("user");

        try {
            DAO dao = new DAO();
            dao.Users().delete(user);
        } catch (SQLException e) {
            throw new ServletException("Error deleting user", e);
        }

        session.invalidate();
        response.sendRedirect("login.jsp");
    }
}