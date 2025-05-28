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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            req.setAttribute("error", "Please log in to view orders.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        DAO dao = (DAO) session.getAttribute("db");
        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("db", dao);
            } catch (SQLException e) {
                req.setAttribute("error", "Database connection failed.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }
        }

        if (orderDB == null || itemDB == null) {
            Connection conn = dao.getConnection();
            orderDB = new OrderDBManager(conn);
            itemDB = new OrderItemDBManager(conn);
        }

        String action = req.getParameter("action");
        try {
            if ("detail".equals(action)) {
                int orderId = Integer.parseInt(req.getParameter("id"));
                Order order = orderDB.getOrderById(orderId);
                if (order == null || order.getUserId() != user.getUserId()) {
                    req.setAttribute("error", "Order not found or unauthorized.");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                    return;
                }
                List<OrderItem> items = itemDB.getItemsByOrderId(orderId);
                req.setAttribute("order", order);
                req.setAttribute("items", items);
                req.getRequestDispatcher("orderDetail.jsp").forward(req, resp);

            } else if ("cancel".equals(action)) {
                int orderId = Integer.parseInt(req.getParameter("id"));
                Order order = orderDB.getOrderById(orderId);
                if (order == null || order.getUserId() != user.getUserId()) {
                    req.setAttribute("error", "Order not found or unauthorized.");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                    return;
                }
                orderDB.updateOrderStatus(orderId, "Cancelled");
                resp.sendRedirect("order?action=list");

            } else {
                List<Order> list = orderDB.getOrdersByUserId(user.getUserId());
                req.setAttribute("orders", list);
                req.getRequestDispatcher("orderList.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error processing order: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            req.setAttribute("error", "Please log in to place an order.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        DAO dao = (DAO) session.getAttribute("db");
        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("db", dao);
            } catch (SQLException e) {
                req.setAttribute("error", "Database connection failed.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }
        }

        if (orderDB == null || itemDB == null) {
            Connection conn = dao.getConnection();
            orderDB = new OrderDBManager(conn);
            itemDB = new OrderItemDBManager(conn);
        }

        try {
            double totalAmount = Double.parseDouble(req.getParameter("totalAmount"));
            Timestamp orderDate = new Timestamp(new Date().getTime());
            Order order = new Order(0, user.getUserId(), totalAmount, "Pending", orderDate);

            String[] prodIds = req.getParameterValues("productId");
            String[] qtys = req.getParameterValues("quantity");
            String[] prices = req.getParameterValues("unitPrice");

            if (prodIds == null || qtys == null || prices == null ||
                    prodIds.length != qtys.length || qtys.length != prices.length) {
                req.setAttribute("error", "Invalid order item data.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }

            int orderId = orderDB.addOrder(order);

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

            resp.sendRedirect("order?action=list");
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error processing order: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}