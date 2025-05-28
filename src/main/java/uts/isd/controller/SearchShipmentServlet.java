package uts.isd.controller;

import uts.isd.model.Shipment;
import uts.isd.model.dao.DAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/searchShipment")
public class SearchShipmentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");

        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("dao", dao);
            } catch (SQLException e) {
                throw new ServletException("DAO init failed", e);
            }
        }

        String keyword = req.getParameter("keyword");
        try {
            List<Shipment> results = dao.getShipmentManager().searchShipments(keyword);
            req.setAttribute("searchResults", results);
            req.getRequestDispatcher("shipmentList.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Search failed: " + e.getMessage());
            req.getRequestDispatcher("shipmentList.jsp").forward(req, resp);
        }
    }
}
