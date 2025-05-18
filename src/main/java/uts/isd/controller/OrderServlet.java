package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import uts.isd.model.Order;
import uts.isd.model.OrderItem;
import uts.isd.model.User;
import uts.isd.model.dao.OrderDBManager;
import uts.isd.model.dao.OrderItemDBManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OrderServlet extends HttpServlet {
    private OrderDBManager orderDB;
    private OrderItemDBManager itemDB;

    @Override
    public void init() {
        orderDB = (OrderDBManager) getServletContext().getAttribute("orderDBManager");
        System.out.println("[OrderServlet] orderDBManager injected? " + (orderDB != null));
        itemDB  = (OrderItemDBManager) getServletContext().getAttribute("orderItemDBManager");
        System.out.println("[OrderServlet] orderItemDBManager injected? " + (itemDB != null));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(); // 确保 session 存在

        // ====== START TEMP TEST USER CODE - REMOVE BEFORE PRODUCTION ======
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(
                    1,                   // 临时测试用户 ID
                    "Test User",         // 临时测试用户名
                    "test@example.com",  // 临时测试邮箱
                    "password",          // 临时测试密码
                    "2000-01-01",        // 临时测试生日
                    "M"                  // 临时测试性别
            );
            session.setAttribute("user", user);
        }
        // ====== END TEMP TEST USER CODE ======

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
                List<Order> list = orderDB.getOrdersByUserId(user.getId());
                System.out.println(">>> 当前用户ID: " + user.getId());
                System.out.println(">>> Order detected: " + list.size());

                req.setAttribute("orders", list);
                req.getRequestDispatcher("orderList.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // ====== START TEMP TEST USER CODE - REMOVE BEFORE PRODUCTION ======
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(1, "Test User", "test@example.com", "password", "2000-01-01", "M");
            session.setAttribute("user", user);
        }
        // ====== END TEMP TEST USER CODE ======

        try {
            double totalPrice = Double.parseDouble(req.getParameter("totalPrice"));
            Order order = new Order(0, user.getId(), totalPrice, "Pending", null);
            int orderId = orderDB.addOrder(order);

            String[] prodIds = req.getParameterValues("productId");
            String[] qtys    = req.getParameterValues("quantity");
            String[] prices  = req.getParameterValues("unitPrice");

            if (prodIds != null) {
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

            // 清空购物车
            session.removeAttribute("cart");

            // 跳转至订单列表（或 shipment.jsp）
            resp.sendRedirect("order?action=list");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace(); // 可选：输出到控制台
            throw new ServletException("Failed to place order: " + e.getMessage(), e);
        }
    }

}
