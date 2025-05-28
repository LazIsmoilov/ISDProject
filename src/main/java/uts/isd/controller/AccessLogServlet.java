package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.AccessLog;
import uts.isd.model.dao.DAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/logs")
public class AccessLogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if the user is logged in
        User user = (User) request.getSession().getAttribute("loggedInUser");
        DAO db = (DAO) request.getSession().getAttribute("db");

        if (user == null || db == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Optional: Get and parse the filter date from the query parameter
            String dateParam = request.getParameter("date");
            Date filterDate = null;

            if (dateParam != null && !dateParam.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                filterDate = sdf.parse(dateParam);
            }

            // Fetch access logs for the user with optional date filter
            List<AccessLog> logs = db.accessLogs().getLogsByUser(user.getUserId(), filterDate);

            // Pass logs to JSP
            request.setAttribute("logs", logs);
            request.getRequestDispatcher("logs.jsp").forward(request, response);

        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
            request.getRequestDispatcher("logs.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error while retrieving logs", e);
        }
    }
}
