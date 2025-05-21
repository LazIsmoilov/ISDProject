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
import java.util.List;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DAO db = (DAO) req.getSession().getAttribute("db");
        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        if ("search".equals(action)) {
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");

            // Debugging log
            System.out.println("Admin Search - Name: " + name + ", Phone: " + phone);

            List<User> users = db.Users().searchUsers(name, phone);
            session.setAttribute("users", users);
            resp.sendRedirect("admin.jsp");
            return;
        } else if ("toggle".equals(action)) {
            int userId = Integer.parseInt(req.getParameter("id"));
            db.Users().toggleActiveStatus(userId);

            // Refresh user list after toggling status
            List<User> users = db.Users().getAllUsers();
            session.setAttribute("users", users);
            resp.sendRedirect("admin.jsp");
            return;
        } else if ("delete".equals(action)) {
            int userId = Integer.parseInt(req.getParameter("id"));
            User user = new User();
            user.setId(userId);
            db.Users().delete(user);

            // Refresh user list after deletion
            List<User> users = db.Users().getAllUsers();
            session.setAttribute("users", users);
            resp.sendRedirect("admin.jsp");
            return;
        }
    }
}

