package uts.isd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uts.isd.model.Payment;
import uts.isd.model.PaymentStatus;
import uts.isd.model.dao.PaymentDBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class PaymentTest {
    private PaymentDBManager paymentDB;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        // Initialize in-memory SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        paymentDB = new PaymentDBManager(connection);

        // Create Payments table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "orderId INTEGER NOT NULL," +
                "paymentMethod TEXT," +
                "cardNumber TEXT," +
                "cardHolderName TEXT," +
                "expiryDate TEXT," +
                "cvv TEXT," +
                "amount REAL," +
                "paymentDate TIMESTAMP," +
                "status TEXT)";
        connection.createStatement().execute(createTableSQL);
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testPaymentValidation() {
        Payment payment = new Payment();
        payment.setCardNumber("4532015112830366");
        assertTrue(validateCardNumber(payment.getCardNumber()));

        payment.setCardNumber("4532 0151 1283 0366");
        assertTrue(validateCardNumber(payment.getCardNumber()));

        payment.setCardNumber("4532-0151-1283-0366");
        assertTrue(validateCardNumber(payment.getCardNumber()));

        // Invalid card numbers
        assertFalse(validateCardNumber("1234567890123456"));
        assertFalse(validateCardNumber("453201511283036"));  // Too short
        assertFalse(validateCardNumber("45320151128303667")); // Too long
        assertFalse(validateCardNumber("453201511283036a"));  // Contains letter
    }

    @Test
    public void testExpiryDateValidation() {
        Payment payment = new Payment();
        payment.setExpiryDate("12/25");
        assertTrue(validateExpiryDate(payment.getExpiryDate()));

        payment.setExpiryDate("01/26");
        assertTrue(validateExpiryDate(payment.getExpiryDate()));

        // Invalid expiry dates
        assertFalse(validateExpiryDate("13/25"));  // Invalid month
        assertFalse(validateExpiryDate("00/25"));  // Invalid month
        assertFalse(validateExpiryDate("12/22"));  // Expired
        assertFalse(validateExpiryDate("12-25"));  // Wrong format
        assertFalse(validateExpiryDate("1225"));   // Wrong format
    }

    @Test
    public void testCVVValidation() {
        Payment payment = new Payment();
        payment.setCvv("123");
        assertTrue(validateCVV(payment.getCvv()));

        payment.setCvv("1234");
        assertTrue(validateCVV(payment.getCvv()));

        // Invalid CVV
        assertFalse(validateCVV("12"));
        assertFalse(validateCVV("12345"));
        assertFalse(validateCVV("12a"));
    }

    @Test
    public void testPaymentAmountValidation() {
        Payment payment = new Payment();
        payment.setAmount(10.00);
        assertTrue(validatePaymentAmount(payment.getAmount()));

        payment.setAmount(0.01);
        assertTrue(validatePaymentAmount(payment.getAmount()));

        payment.setAmount(999999.99);
        assertTrue(validatePaymentAmount(payment.getAmount()));

        // Invalid amounts
        assertFalse(validatePaymentAmount(0.00));
        assertFalse(validatePaymentAmount(-10.00));
    }

    @Test
    public void testPaymentStatus() {
        Payment payment = new Payment();
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

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
        Payment payment = new Payment();
        payment.setOrderId(1);
        payment.setAmount(100.00);
        payment.setPaymentMethod("Credit Card");
        payment.setCardNumber("4532015112830366");
        payment.setCardHolderName("John Doe");
        payment.setExpiryDate("12/25");
        payment.setCvv("123");
        payment.setPaymentDate(new Date());
        payment.setStatus(PaymentStatus.COMPLETED);

        int paymentId = paymentDB.addPayment(payment);
        assertTrue(paymentId > 0);

        Payment retrieved = paymentDB.getPaymentById(paymentId);
        assertNotNull(retrieved);
        assertEquals(payment.getOrderId(), retrieved.getOrderId());
        assertEquals(payment.getAmount(), retrieved.getAmount(), 0.001);
        assertEquals(payment.getPaymentMethod(), retrieved.getPaymentMethod());
        assertEquals(payment.getStatus(), retrieved.getStatus());

        // Update status
        retrieved.setStatus(PaymentStatus.CANCELLED);
        paymentDB.updatePayment(retrieved);

        Payment updated = paymentDB.getPaymentById(paymentId);
        assertEquals(PaymentStatus.CANCELLED, updated.getStatus());

        // Search by date range
        Date now = new Date();
        Date start = new Date(now.getTime() - 86400000); // 1 day before
        List<Payment> payments = paymentDB.searchPaymentsByDateRange(start, now);
        assertFalse(payments.isEmpty());

        // Get by order ID
        List<Payment> orderPayments = paymentDB.getPaymentsByOrderId(1);
        assertFalse(orderPayments.isEmpty());

        // Delete payment
        paymentDB.deletePayment(paymentId);
        Payment deleted = paymentDB.getPaymentById(paymentId);
        assertNull(deleted);
    }

    @Test
    public void testPaymentReceipt() {
        Payment payment = new Payment();
        payment.setOrderId(1);
        payment.setAmount(100.00);
        payment.setPaymentMethod("Credit Card");
        payment.setCardNumber("4532015112830366");
        payment.setCardHolderName("John Doe");
        payment.setExpiryDate("12/25");
        payment.setCvv("123");
        payment.setPaymentDate(new Date());
        payment.setStatus(PaymentStatus.COMPLETED);

        String receipt = generateReceipt(payment);
        assertNotNull(receipt);
        assertTrue(receipt.contains("Payment Receipt"));
        assertTrue(receipt.contains("Order ID: 1"));
        assertTrue(receipt.contains("Amount: $100.00"));
        assertTrue(receipt.contains("Status: Completed"));
        assertTrue(receipt.contains("User ID: 1"));
    }

    private boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null) return false;
        // Remove spaces and hyphens
        cardNumber = cardNumber.replaceAll("[\\s-]", "");
        // Check length (16 digits for most cards)
        if (cardNumber.length() != 16) return false;
        // Check if all characters are digits
        if (!cardNumber.matches("\\d+")) return false;
        // Basic Luhn algorithm check
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    private boolean validateExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/([2-9][0-9])")) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
            sdf.setLenient(false);
            Date date = sdf.parse(expiryDate);
            Date now = new Date();
            return date.after(now);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateCVV(String cvv) {
        if (cvv == null) return false;
        return cvv.matches("\\d{3,4}");
    }

    private boolean validatePaymentAmount(double amount) {
        return amount > 0;
    }

    private String generateReceipt(Payment payment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("""
            Payment Receipt
            ---------------
            Payment ID: %d
            User ID: %d
            Order ID: %d
            Amount: $%.2f
            Payment Method: %s
            Status: %s
            Date: %s
            """,
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus().getDisplayName(),
                sdf.format(payment.getPaymentDate()));
    }
}