package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.User;
import uts.isd.model.dao.AccessLogDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        get session
        HttpSession session = request.getSession(false);
//        session will have user if logged in
        if (session != null) {
            User user = (User) session.getAttribute("user");

            // update logout time

            if (user != null) {
                try {
                    new AccessLogDAO().logLogout(user.getUserId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}