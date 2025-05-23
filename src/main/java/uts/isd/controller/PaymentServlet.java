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
import uts.isd.model.PaymentStatus;
import uts.isd.model.User;
import uts.isd.model.dao.OrderDBManager;
import uts.isd.model.dao.PaymentDBManager;

public class PaymentServlet extends HttpServlet {
    private PaymentDBManager paymentDB;
    private OrderDBManager orderDB;
    private static final Logger LOGGER = Logger.getLogger(PaymentServlet.class.getName());
    
    @Override
    public void init() {
        try {
            paymentDB = (PaymentDBManager) getServletContext().getAttribute("paymentDB");
            orderDB = (OrderDBManager) getServletContext().getAttribute("orderDB");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing PaymentServlet", e);
        }
    }
    
    public boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        
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
    
    public boolean validateExpiryDate(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return false;
        }
        
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
    
    public boolean validateCVV(String cvv) {
        if (cvv == null || cvv.trim().isEmpty()) {
            return false;
        }
        // Check if CVV is 3 or 4 digits
        return cvv.matches("\\d{3,4}");
    }
    
    public boolean validatePaymentAmount(double amount) {
        return amount > 0 && amount <= 999999.99; // Add maximum amount limit
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
        User user = (User) session.getAttribute("user");
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
        
        // Validate payment
        List<String> errors = validatePayment(payment);
        if (!errors.isEmpty()) {
            session.setAttribute("paymentError", String.join(", ", errors));
            session.setAttribute("paymentRetry", payment); // Store payment details for retry
            response.sendRedirect("payment.jsp?orderId=" + orderId);
            return;
        }
        
        try {
            // Simulate payment processing (replace with actual payment gateway integration)
            boolean paymentSuccessful = processPaymentWithGateway(payment);
            
            if (paymentSuccessful) {
                payment.setStatus(PaymentStatus.COMPLETED);
                paymentDB.addPayment(payment);
                
                // Update order status
                orderDB.updateOrderStatus(orderId, "Paid");
                
                // Set success message and redirect to confirmation
                session.setAttribute("payment", payment);
                session.setAttribute("paymentSuccess", "Payment processed successfully!");
                response.sendRedirect("paymentConfirmation.jsp");
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                paymentDB.addPayment(payment);
                
                session.setAttribute("paymentError", "Payment was declined. Please check your card details and try again.");
                session.setAttribute("paymentRetry", payment); // Store payment details for retry
                response.sendRedirect("payment.jsp?orderId=" + orderId);
            }
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentDB.addPayment(payment);
            
            session.setAttribute("paymentError", "An error occurred while processing your payment. Please try again.");
            session.setAttribute("paymentRetry", payment); // Store payment details for retry
            response.sendRedirect("payment.jsp?orderId=" + orderId);
        }
    }
    
    private boolean processPaymentWithGateway(Payment payment) {
        // Simulate payment gateway processing
        // In a real implementation, this would integrate with a payment gateway
        // For testing, we'll randomly succeed or fail
        return Math.random() > 0.3; // 70% success rate for testing
    }
    
    private void cancelPayment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, SQLException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        Payment payment = paymentDB.getPaymentById(paymentId);
        
        if (payment != null) {
            payment.setStatus(PaymentStatus.CANCELLED);
            paymentDB.updatePayment(payment);
            
            // Update order status back to pending
            orderDB.updateOrderStatus(payment.getOrderId(), "Pending");
            
            session.setAttribute("paymentSuccess", "Payment cancelled successfully!");
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