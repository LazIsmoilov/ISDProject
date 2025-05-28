package uts.isd.controller;

import uts.isd.model.Shipment;
import uts.isd.model.dao.DAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AddShipmentServlet")
public class AddShipmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // ✅ Get or initialize DAO from session
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("dao", dao);
            } catch (SQLException e) {
                e.printStackTrace(); // ✅ Debugging log
                throw new ServletException("❌ Failed to initialize DAO", e);
            }
        }

        try {
            // ✅ Retrieve form parameters
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String method = request.getParameter("method");
            String shipmentDate = request.getParameter("shipmentDate");
            String address = request.getParameter("address");

            // ✅ Add shipment to DB
            dao.getShipmentManager().addShipment(orderId, method, shipmentDate, address);

            // ✅ Reload updated shipment list
            List<Shipment> shipments = dao.getShipmentManager().getShipmentsByOrderId(orderId);
            request.setAttribute("shipments", shipments);
            request.setAttribute("orderId", orderId);
            request.setAttribute("successMsg", "Shipment added successfully");

            // ✅ Forward to list page
            request.getRequestDispatcher("shipmentList.jsp").forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace(); // ✅ Debugging log
            session.setAttribute("error", "❌ Failed to add shipment: " + e.getMessage());
            response.sendRedirect("shipmentForm.jsp?orderId=" + request.getParameter("orderId"));
        }
    }
}
