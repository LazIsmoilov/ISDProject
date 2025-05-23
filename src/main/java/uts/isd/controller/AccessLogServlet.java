package uts.isd.controller;

import uts.isd.model.User;
import uts.isd.model.AccessLog;

import uts.isd.model.dao.AccessLogDAO;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        get current user if null return login
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

//        change date from jsp to java date
        Date filterDate = null;
        try {
            String dateParam = request.getParameter("date");
            if (dateParam != null && !dateParam.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                filterDate = sdf.parse(dateParam);
            }

//            use accesslogdao filter id and date, if null return all logs or show the chosen day
            List<AccessLog> logs = new AccessLogDAO().getLogsByUser(user.getUserId(), filterDate);

//            give the value to jsp
            request.setAttribute("logs", logs);
            request.getRequestDispatcher("logs.jsp").forward(request, response);

        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}