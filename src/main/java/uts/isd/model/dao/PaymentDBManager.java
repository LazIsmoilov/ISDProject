package uts.isd.model.dao;

import uts.isd.model.Payment;
import uts.isd.model.PaymentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentDBManager {
    private Connection conn;

    public PaymentDBManager(Connection conn) {
        this.conn = conn;
    }

    public int addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payments (userId, orderId, paymentMethod, cardNumber, cardHolderName, " +
                "expiryDate, cvv, amount, paymentDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getUserId());
            ps.setInt(2, payment.getOrderId());
            ps.setString(3, payment.getPaymentMethod());
            ps.setString(4, payment.getCardNumber());
            ps.setString(5, payment.getCardHolderName());
            ps.setString(6, payment.getExpiryDate());
            ps.setString(7, payment.getCvv());
            ps.setDouble(8, payment.getAmount());
            ps.setTimestamp(9, new Timestamp(payment.getPaymentDate().getTime()));
            ps.setString(10, payment.getStatus().getDisplayName());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    payment.setPaymentId(generatedId);
                    return generatedId;
                }
            }
        }
        throw new SQLException("Creating payment failed, no ID obtained.");
    }

    public List<Payment> getPaymentsByOrderId(int orderId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                            rs.getInt("id"),
                            rs.getInt("userId"),
                            rs.getInt("orderId"),
                            rs.getString("paymentMethod"),
                            rs.getString("cardNumber"),
                            rs.getString("cardHolderName"),
                            rs.getString("expiryDate"),
                            rs.getString("cvv"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("paymentDate"),
                            PaymentStatus.fromString(rs.getString("status"))
                    );
                    payments.add(payment);
                }
            }
        }
        return payments;
    }

    public Payment getPaymentById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM Payments WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("id"),
                            rs.getInt("userId"),
                            rs.getInt("orderId"),
                            rs.getString("paymentMethod"),
                            rs.getString("cardNumber"),
                            rs.getString("cardHolderName"),
                            rs.getString("expiryDate"),
                            rs.getString("cvv"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("paymentDate"),
                            PaymentStatus.fromString(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    public void updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE Payments SET userId = ?, orderId = ?, paymentMethod = ?, cardNumber = ?, cardHolderName = ?, " +
                "expiryDate = ?, cvv = ?, amount = ?, paymentDate = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getUserId());
            ps.setInt(2, payment.getOrderId());
            ps.setString(3, payment.getPaymentMethod());
            ps.setString(4, payment.getCardNumber());
            ps.setString(5, payment.getCardHolderName());
            ps.setString(6, payment.getExpiryDate());
            ps.setString(7, payment.getCvv());
            ps.setDouble(8, payment.getAmount());
            ps.setTimestamp(9, new Timestamp(payment.getPaymentDate().getTime()));
            ps.setString(10, payment.getStatus().getDisplayName());
            ps.setInt(11, payment.getPaymentId());
            ps.executeUpdate();
        }
    }

    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payments WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.executeUpdate();
        }
    }

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
                            rs.getInt("userId"),
                            rs.getInt("orderId"),
                            rs.getString("paymentMethod"),
                            rs.getString("cardNumber"),
                            rs.getString("cardHolderName"),
                            rs.getString("expiryDate"),
                            rs.getString("cvv"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("paymentDate"),
                            PaymentStatus.fromString(rs.getString("status"))
                    );
                    payments.add(payment);
                }
            }
        }
        return payments;
    }
}
