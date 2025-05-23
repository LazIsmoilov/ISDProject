package uts.isd;

import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import uts.isd.model.Payment;
import uts.isd.model.PaymentStatus;
import uts.isd.model.dao.PaymentDBManager;
import uts.isd.controller.PaymentServlet;

public class PaymentTest {
    private PaymentServlet paymentServlet = new PaymentServlet();
    
    @Test
    public void testPaymentValidation() {
        // Test valid card number
        assertTrue(paymentServlet.validateCardNumber("4532015112830366"));
        assertTrue(paymentServlet.validateCardNumber("4532 0151 1283 0366"));
        assertTrue(paymentServlet.validateCardNumber("4532-0151-1283-0366"));
        
        // Test invalid card number
        assertFalse(paymentServlet.validateCardNumber("1234567890123456"));
        assertFalse(paymentServlet.validateCardNumber("453201511283036"));
        assertFalse(paymentServlet.validateCardNumber("45320151128303667"));
        assertFalse(paymentServlet.validateCardNumber("453201511283036a"));
    }
    
    @Test
    public void testExpiryDateValidation() {
        // Test valid expiry date
        assertTrue(paymentServlet.validateExpiryDate("12/25"));
        assertTrue(paymentServlet.validateExpiryDate("01/24"));
        
        // Test invalid expiry date
        assertFalse(paymentServlet.validateExpiryDate("13/25")); // Invalid month
        assertFalse(paymentServlet.validateExpiryDate("00/25")); // Invalid month
        assertFalse(paymentServlet.validateExpiryDate("12/22")); // Expired
        assertFalse(paymentServlet.validateExpiryDate("12-25")); // Wrong format
        assertFalse(paymentServlet.validateExpiryDate("1225")); // Wrong format
    }
    
    @Test
    public void testCVVValidation() {
        // Test valid CVV
        assertTrue(paymentServlet.validateCVV("123"));
        assertTrue(paymentServlet.validateCVV("1234"));
        
        // Test invalid CVV
        assertFalse(paymentServlet.validateCVV("12"));
        assertFalse(paymentServlet.validateCVV("12345"));
        assertFalse(paymentServlet.validateCVV("12a"));
    }
    
    @Test
    public void testPaymentAmountValidation() {
        // Test valid amount
        assertTrue(paymentServlet.validatePaymentAmount(10.00));
        assertTrue(paymentServlet.validatePaymentAmount(0.01));
        assertTrue(paymentServlet.validatePaymentAmount(999999.99));
        
        // Test invalid amount
        assertFalse(paymentServlet.validatePaymentAmount(0.00));
        assertFalse(paymentServlet.validatePaymentAmount(-10.00));
    }
    
    @Test
    public void testPaymentStatus() {
        Payment payment = new Payment();
        
        // Test default status
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        
        // Test status changes
        payment.setStatus(PaymentStatus.COMPLETED);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        
        payment.setStatus(PaymentStatus.CANCELLED);
        assertEquals(PaymentStatus.CANCELLED, payment.getStatus());
        
        payment.setStatus(PaymentStatus.FAILED);
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        
        payment.setStatus(PaymentStatus.REFUNDED);
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }
    
    @Test
    public void testPaymentDBOperations() throws SQLException {
        PaymentDBManager paymentDB = new PaymentDBManager();
        
        // Create test payment
        Payment payment = new Payment();
        payment.setOrderId(1);
        payment.setAmount(100.00);
        payment.setPaymentMethod("Credit Card");
        payment.setCardNumber("4532015112830366");
        payment.setExpiryDate("12/25");
        payment.setCvv("123");
        payment.setPaymentDate(new Date());
        payment.setStatus(PaymentStatus.COMPLETED);
        
        // Test adding payment
        int paymentId = paymentDB.addPayment(payment);
        assertTrue(paymentId > 0);
        
        // Test retrieving payment
        Payment retrievedPayment = paymentDB.getPaymentById(paymentId);
        assertNotNull(retrievedPayment);
        assertEquals(payment.getOrderId(), retrievedPayment.getOrderId());
        assertEquals(payment.getAmount(), retrievedPayment.getAmount(), 0.001);
        assertEquals(payment.getPaymentMethod(), retrievedPayment.getPaymentMethod());
        assertEquals(payment.getStatus(), retrievedPayment.getStatus());
        
        // Test updating payment
        retrievedPayment.setStatus(PaymentStatus.CANCELLED);
        paymentDB.updatePayment(retrievedPayment);
        
        Payment updatedPayment = paymentDB.getPaymentById(paymentId);
        assertEquals(PaymentStatus.CANCELLED, updatedPayment.getStatus());
        
        // Test searching payments
        Date startDate = new Date(System.currentTimeMillis() - 86400000); // 24 hours ago
        Date endDate = new Date();
        List<Payment> payments = paymentDB.searchPaymentsByDateRange((java.sql.Date) startDate, (java.sql.Date) endDate);
        assertFalse(payments.isEmpty());
        
        // Test getting payments by order ID
        List<Payment> orderPayments = paymentDB.getPaymentsByOrderId(1);
        assertFalse(orderPayments.isEmpty());
    }
    
    @Test
    public void testPaymentReceipt() {
        Payment payment = new Payment();
        payment.setOrderId(1);
        payment.setAmount(100.00);
        payment.setPaymentMethod("Credit Card");
        payment.setCardNumber("4532015112830366");
        payment.setExpiryDate("12/25");
        payment.setCvv("123");
        payment.setPaymentDate(new Date());
        payment.setStatus(PaymentStatus.COMPLETED);
        
        // Test receipt generation
        String receipt = generateReceipt(payment);
        assertNotNull(receipt);
        assertTrue(receipt.contains("Payment Receipt"));
        assertTrue(receipt.contains("Order ID: 1"));
        assertTrue(receipt.contains("Amount: $100.00"));
        assertTrue(receipt.contains("Status: Completed"));
    }
    
    private String generateReceipt(Payment payment) {
        // This is a simplified version of receipt generation for testing
        StringBuilder receipt = new StringBuilder();
        receipt.append("Payment Receipt\n");
        receipt.append("---------------\n");
        receipt.append("Order ID: ").append(payment.getOrderId()).append("\n");
        receipt.append("Amount: $").append(String.format("%.2f", payment.getAmount())).append("\n");
        receipt.append("Payment Method: ").append(payment.getPaymentMethod()).append("\n");
        receipt.append("Status: ").append(payment.getStatus().getDisplayName()).append("\n");
        receipt.append("Date: ").append(payment.getPaymentDate()).append("\n");
        return receipt.toString();
    }
} 