package uts.isd.controller;

import uts.isd.model.dao.DAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteShipment")
public class DeleteShipmentServlet extends HttpServlet {
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

        try {
            int shipmentId = Integer.parseInt(req.getParameter("shipmentId"));
            int orderId = Integer.parseInt(req.getParameter("orderId"));

            dao.getShipmentManager().deleteShipment(shipmentId);
            resp.sendRedirect("viewShipment?orderId=" + orderId);

        } catch (Exception e) {
            req.setAttribute("error", "Delete failed: " + e.getMessage());
            req.getRequestDispatcher("shipmentList.jsp").forward(req, resp);
        }
    }
}
