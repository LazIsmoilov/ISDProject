package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import uts.isd.model.Payment;
import uts.isd.model.Order;
import uts.isd.model.User;
import uts.isd.model.dao.PaymentDBManager;
import uts.isd.model.dao.OrderDBManager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PaymentServlet extends HttpServlet {
    private PaymentDBManager paymentDB;
    private OrderDBManager orderDB;

    @Override
    public void init() {
        paymentDB = (PaymentDBManager) getServletContext().getAttribute("paymentDBManager");
        orderDB = (OrderDBManager) getServletContext().getAttribute("orderDBManager");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("search".equals(action)) {
                // Handle payment history search
                String startDateStr = req.getParameter("startDate");
                String endDateStr = req.getParameter("endDate");
                
                if (startDateStr != null && endDateStr != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilStartDate = dateFormat.parse(startDateStr);
                    java.util.Date utilEndDate = dateFormat.parse(endDateStr);
                    
                    // Convert to SQL Date
                    Date startDate = new Date(utilStartDate.getTime());
                    Date endDate = new Date(utilEndDate.getTime());
                    
                    List<Payment> payments = paymentDB.searchPaymentsByDateRange(startDate, endDate);
                    session.setAttribute("payments", payments);
                }
                
                req.getRequestDispatcher("paymentHistory.jsp").forward(req, resp);
            } else {
                // Default: show payment history
                List<Payment> payments = paymentDB.getPaymentsByOrderId(
                    Integer.parseInt(req.getParameter("orderId"))
                );
                session.setAttribute("payments", payments);
                req.getRequestDispatcher("paymentHistory.jsp").forward(req, resp);
            }
        } catch (SQLException | ParseException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("process".equals(action)) {
                // Process new payment
                int orderId = Integer.parseInt(req.getParameter("orderId"));
                double amount = Double.parseDouble(req.getParameter("amount"));
                
                // Create payment object
                Payment payment = new Payment();
                payment.setOrderId(orderId);
                payment.setPaymentMethod(req.getParameter("paymentMethod"));
                payment.setCardNumber(req.getParameter("cardNumber"));
                payment.setCardHolderName(req.getParameter("cardHolderName"));
                payment.setExpiryDate(req.getParameter("expiryDate"));
                payment.setCvv(req.getParameter("cvv"));
                payment.setAmount(amount);
                payment.setPaymentDate(new Date(System.currentTimeMillis()));
                payment.setStatus("Pending");
                
                // Save payment
                int paymentId = paymentDB.addPayment(payment);
                
                // Update order status
                orderDB.updateOrderStatus(orderId, "Paid");
                
                // Redirect to payment history
                resp.sendRedirect("payment?action=search");
            } else if ("cancel".equals(action)) {
                // Cancel payment
                int paymentId = Integer.parseInt(req.getParameter("paymentId"));
                Payment payment = paymentDB.getPaymentById(paymentId);
                
                if (payment != null) {
                    payment.setStatus("Cancelled");
                    paymentDB.updatePayment(payment);
                    
                    // Update order status back to pending
                    orderDB.updateOrderStatus(payment.getOrderId(), "Pending");
                }
                
                resp.sendRedirect("payment?action=search");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
} 