package uts.isd.controller;

import uts.isd.model.Shipment;
import uts.isd.model.dao.DAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/editShipment")
public class UpdateShipmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            int orderId = Integer.parseInt(req.getParameter("orderId")); // ✅ Tambahkan ini
            String method = req.getParameter("method");
            String shipmentDate = req.getParameter("shipmentDate");
            String address = req.getParameter("address");

            Shipment shipment = new Shipment();
            shipment.setShipmentId(shipmentId);
            shipment.setOrderId(orderId); // ✅ Tambahkan ini juga
            shipment.setMethod(method);
            shipment.setShipmentDate(shipmentDate);
            shipment.setAddress(address);

            dao.getShipmentManager().updateShipment(shipment);

            // ✅ Redirect kembali ke shipment list berdasarkan orderId
            resp.sendRedirect("viewShipment?orderId=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Update failed: " + e.getMessage());
            req.getRequestDispatcher("editShipment.jsp").forward(req, resp);
        }
    }
}
