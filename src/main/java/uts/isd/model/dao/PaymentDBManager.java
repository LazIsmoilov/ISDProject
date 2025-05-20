package uts.isd.model.dao;

import uts.isd.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDBManager {
    private Connection conn;

    public PaymentDBManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * Add a new payment record and return the generated payment ID
     */
    public int addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payments (orderId, paymentMethod, cardNumber, cardHolderName, " +
                    "expiryDate, cvv, amount, paymentDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getCardNumber());
            ps.setString(4, payment.getCardHolderName());
            ps.setString(5, payment.getExpiryDate());
            ps.setString(6, payment.getCvv());
            ps.setDouble(7, payment.getAmount());
            ps.setTimestamp(8, new Timestamp(payment.getPaymentDate().getTime()));
            ps.setString(9, payment.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    payment.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Creating payment failed, no ID obtained.");
    }

    /**
     * Get all payments for a specific order
     */
    public List<Payment> getPaymentsByOrderId(int orderId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getString("cardNumber"),
                        rs.getString("cardHolderName"),
                        rs.getString("expiryDate"),
                        rs.getString("cvv"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("paymentDate"),
                        rs.getString("status")
                    );
                    payments.add(payment);
                }
            }
        }
        return payments;
    }

    /**
     * Get a specific payment by its ID
     */
    public Payment getPaymentById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM Payments WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getString("cardNumber"),
                        rs.getString("cardHolderName"),
                        rs.getString("expiryDate"),
                        rs.getString("cvv"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("paymentDate"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Update payment details
     */
    public void updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE Payments SET paymentMethod = ?, cardNumber = ?, cardHolderName = ?, " +
                    "expiryDate = ?, cvv = ?, amount = ?, paymentDate = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, payment.getPaymentMethod());
            ps.setString(2, payment.getCardNumber());
            ps.setString(3, payment.getCardHolderName());
            ps.setString(4, payment.getExpiryDate());
            ps.setString(5, payment.getCvv());
            ps.setDouble(6, payment.getAmount());
            ps.setTimestamp(7, new Timestamp(payment.getPaymentDate().getTime()));
            ps.setString(8, payment.getStatus());
            ps.setInt(9, payment.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Delete a payment record
     */
    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payments WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.executeUpdate();
        }
    }

    /**
     * Search payments by date range
     */
    public List<Payment> searchPaymentsByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE paymentDate BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(startDate.getTime()));
            ps.setTimestamp(2, new Timestamp(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getString("cardNumber"),
                        rs.getString("cardHolderName"),
                        rs.getString("expiryDate"),
                        rs.getString("cvv"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("paymentDate"),
                        rs.getString("status")
                    );
                    payments.add(payment);
                }
            }
        }
        return payments;
    }
} 