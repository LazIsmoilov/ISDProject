package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.Order;
import uts.isd.model.OrderItem;
import uts.isd.model.User;
import uts.isd.model.dao.DAO;
import uts.isd.model.dao.OrderDBManager;
import uts.isd.model.dao.OrderItemDBManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp; // Added for orderDate
import java.util.List;
import java.util.Date; // Added for current date

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private OrderDBManager orderDB;
    private OrderItemDBManager itemDB;

    @Override
    public void init() throws ServletException {
        // Initialization handled in doGet/doPost
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Retrieve DAO from session
        DAO dao = (DAO) session.getAttribute("db");
        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("db", dao);
            } catch (SQLException e) {
                throw new ServletException("Failed to initialize DAO", e);
            }
        }

        // Initialize DB managers if not already set
        if (orderDB == null || itemDB == null) {
            Connection conn = dao.getConnection();
            orderDB = new OrderDBManager(conn);
            itemDB = new OrderItemDBManager(conn);
        }

        // TEMP: Set dummy user if not already set (for testing)
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserId(1);
            user.setFullName("Test User");
            user.setEmail("test@example.com");
            user.setPassword("password");
            user.setPhone("0000000000");
            user.setRole("Customer");
            session.setAttribute("user", user);
        }

        String action = req.getParameter("action");
        try {
            if ("detail".equals(action)) {
                int orderId = Integer.parseInt(req.getParameter("id"));
                Order order = orderDB.getOrderById(orderId);
                List<OrderItem> items = itemDB.getItemsByOrderId(orderId);

                req.setAttribute("order", order);
                req.setAttribute("items", items);
                req.getRequestDispatcher("orderDetail.jsp").forward(req, resp);

            } else if ("cancel".equals(action)) {
                int orderId = Integer.parseInt(req.getParameter("id"));
                orderDB.updateOrderStatus(orderId, "Cancelled");
                resp.sendRedirect("order?action=list");

            } else {
                List<Order> list = orderDB.getOrdersByUserId(user.getUserId());
                req.setAttribute("orders", list);
                req.getRequestDispatcher("orderList.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            throw new ServletException("Error processing order action: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Retrieve DAO from session
        DAO dao = (DAO) session.getAttribute("db");
        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("db", dao);
            } catch (SQLException e) {
                throw new ServletException("Failed to initialize DAO", e);
            }
        }

        // Initialize DB managers if not already set
        if (orderDB == null || itemDB == null) {
            Connection conn = dao.getConnection();
            orderDB = new OrderDBManager(conn);
            itemDB = new OrderItemDBManager(conn);
        }

        // TEMP: Set dummy user if not already set (for testing)
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserId(1);
            user.setFullName("Test User");
            user.setEmail("test@example.com");
            user.setPassword("password");
            user.setPhone("0000000000");
            user.setRole("Customer");
            session.setAttribute("user", user);
        }

        try {
            double totalPrice = Double.parseDouble(req.getParameter("totalPrice"));
            // Set orderDate to current timestamp
            Timestamp orderDate = new Timestamp(new Date().getTime());
            Order order = new Order(0, user.getUserId(), totalPrice, "Pending", orderDate);

            int orderId = orderDB.addOrder(order);

            String[] prodIds = req.getParameterValues("productId");
            String[] qtys = req.getParameterValues("quantity");
            String[] prices = req.getParameterValues("unitPrice");

            if (prodIds != null && qtys != null && prices != null) {
                for (int i = 0; i < prodIds.length; i++) {
                    OrderItem item = new OrderItem(
                            0,
                            orderId,
                            Integer.parseInt(prodIds[i]),
                            Integer.parseInt(qtys[i]),
                            Double.parseDouble(prices[i])
                    );
                    itemDB.addOrderItem(item);
                }
            }

            resp.sendRedirect("order?action=list");
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            throw new ServletException("Error processing order submission: " + e.getMessage(), e);
        }
    }
}