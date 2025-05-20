package uts.isd.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.Payment;
import uts.isd.model.dao.OrderDBManager;
import uts.isd.model.dao.PaymentDBManager;

public class PaymentServlet extends HttpServlet {
    private PaymentDBManager paymentDB;
    private OrderDBManager orderDB;
    
    @Override
    public void init() {
        try {
            paymentDB = (PaymentDBManager) getServletContext().getAttribute("paymentDB");
            orderDB = (OrderDBManager) getServletContext().getAttribute("orderDB");
        } catch (Exception e) {
            Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private boolean validateCardNumber(String cardNumber) {
        // Remove spaces and dashes
        cardNumber = cardNumber.replaceAll("[\\s-]", "");
        
        // Check if it's a valid number
        if (!cardNumber.matches("\\d+")) {
            return false;
        }
        
        // Luhn algorithm for card validation
        int sum = 0;
        boolean alternate = false;
        
        // Loop through values starting from the rightmost digit
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
    
    private boolean validateExpiryDate(String expiryDate) {
        // Check format MM/YY
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        
        // Check if month is valid
        if (month < 1 || month > 12) {
            return false;
        }
        
        // Get current year and month
        int currentYear = java.time.Year.now().getValue() % 100; // Get last 2 digits
        int currentMonth = java.time.LocalDate.now().getMonthValue();
        
        // Check if card is expired
        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateCVV(String cvv) {
        // Check if CVV is 3 or 4 digits
        return cvv.matches("\\d{3,4}");
    }
    
    private boolean validatePaymentAmount(double amount) {
        return amount > 0;
    }
    
    private List<String> validatePayment(Payment payment) {
        List<String> errors = new ArrayList<>();
        
        if (!validateCardNumber(payment.getCardNumber())) {
            errors.add("Invalid card number");
        }
        
        if (!validateExpiryDate(payment.getExpiryDate())) {
            errors.add("Invalid or expired card");
        }
        
        if (!validateCVV(payment.getCvv())) {
            errors.add("Invalid CVV");
        }
        
        if (!validatePaymentAmount(payment.getAmount())) {
            errors.add("Invalid payment amount");
        }
        
        return errors;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("order?action=list");
            return;
        }
        
        try {
            switch (action) {
                case "process":
                    processPayment(request, response, session);
                    break;
                case "cancel":
                    cancelPayment(request, response, session);
                    break;
                default:
                    response.sendRedirect("order?action=list");
            }
        } catch (SQLException e) {
            session.setAttribute("paymentError", "Database error: " + e.getMessage());
            response.sendRedirect("payment.jsp?orderId=" + request.getParameter("orderId"));
        }
    }
    
    private void processPayment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, SQLException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        String paymentMethod = request.getParameter("paymentMethod");
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");
        
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setCardNumber(cardNumber);
        payment.setExpiryDate(expiryDate);
        payment.setCvv(cvv);
        payment.setPaymentDate(new Date(System.currentTimeMillis()));
        payment.setStatus("Completed");
        
        // Validate payment
        List<String> errors = validatePayment(payment);
        if (!errors.isEmpty()) {
            session.setAttribute("paymentError", String.join(", ", errors));
            response.sendRedirect("payment.jsp?orderId=" + orderId);
            return;
        }
        
        // Process payment
        paymentDB.addPayment(payment);
        
        // Update order status
        orderDB.updateOrderStatus(orderId, "Paid");
        
        // Set success message and redirect to confirmation
        session.setAttribute("payment", payment);
        response.sendRedirect("paymentConfirmation.jsp");
    }
    
    private void cancelPayment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, SQLException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        Payment payment = paymentDB.getPaymentById(paymentId);
        
        if (payment != null) {
            payment.setStatus("Cancelled");
            paymentDB.updatePayment(payment);
            
            // Update order status back to pending
            orderDB.updateOrderStatus(payment.getOrderId(), "Pending");
        }
        
        response.sendRedirect("payment?action=search");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("order?action=list");
            return;
        }
        
        try {
            switch (action) {
                case "search":
                    searchPayments(request, response, session);
                    break;
                case "receipt":
                    generateReceipt(request, response, session);
                    break;
                default:
                    response.sendRedirect("order?action=list");
            }
        } catch (SQLException | ParseException e) {
            session.setAttribute("paymentError", "Error: " + e.getMessage());
            response.sendRedirect("payment.jsp");
        }
    }
    
    private void searchPayments(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, ParseException, IOException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilStartDate = dateFormat.parse(startDateStr);
        java.util.Date utilEndDate = dateFormat.parse(endDateStr);
        
        // Convert to SQL Date
        java.sql.Date startDate = new java.sql.Date(utilStartDate.getTime());
        java.sql.Date endDate = new java.sql.Date(utilEndDate.getTime());
        
        List<Payment> payments = paymentDB.searchPaymentsByDateRange(startDate, endDate);
        session.setAttribute("payments", payments);
        
        response.sendRedirect("paymentHistory.jsp");
    }
    
    private void generateReceipt(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws SQLException, IOException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        Payment payment = paymentDB.getPaymentById(paymentId);
        
        if (payment != null) {
            session.setAttribute("payment", payment);
            response.sendRedirect("paymentReceipt.jsp");
        } else {
            session.setAttribute("paymentError", "Payment not found");
            response.sendRedirect("payment?action=search");
        }
    }
} 