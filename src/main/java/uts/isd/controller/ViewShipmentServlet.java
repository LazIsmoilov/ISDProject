package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.Shipment;
import uts.isd.model.dao.DAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/viewShipment")
public class ViewShipmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");

        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("dao", dao);
            } catch (Exception e) {
                throw new ServletException("Gagal inisialisasi DAO", e);
            }
        }

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            req.setAttribute("error", "Order ID tidak boleh kosong");
            req.getRequestDispatcher("shipmentList.jsp").forward(req, resp);
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            List<Shipment> shipments = dao.getShipmentManager().getShipmentsByOrderId(orderId);

            if (shipments.isEmpty()) {
                req.setAttribute("infoMsg", "No shipments found for this order");
            }

            req.setAttribute("shipments", shipments);
            req.setAttribute("orderId", orderId);

            // Success message
            String successMsg = (String) session.getAttribute("successMsg");
            if (successMsg != null) {
                req.setAttribute("successMsg", successMsg);
                session.removeAttribute("successMsg");
            }

            req.getRequestDispatcher("shipmentList.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Order ID harus berupa angka");
            req.getRequestDispatcher("shipmentList.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error getting shipments", e);
        }
    }
}
