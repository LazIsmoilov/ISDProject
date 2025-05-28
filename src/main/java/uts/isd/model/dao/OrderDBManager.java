package uts.isd.model.dao;

import uts.isd.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDBManager {
    private Connection conn;

    public OrderDBManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new order and returns the generated orderId
     */
    public int addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (userId, totalAmount, status, orderDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());
            ps.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    order.setOrderId(orderId);
                    return orderId;
                }
            }
        }
        throw new SQLException("Creating order failed, no ID obtained.");
    }

    /**
     * Retrieves all orders for a given userId
     */
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT orderId, userId, totalAmount, status, orderDate FROM Orders WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order(
                            rs.getInt("orderId"),
                            rs.getInt("userId"),
                            rs.getDouble("totalAmount"),
                            rs.getString("status"),
                            rs.getTimestamp("orderDate")
                    );
                    orders.add(o);
                }
            }
        }
        return orders;
    }

    /**
     * Retrieves a single order by orderId
     */
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT orderId, userId, totalAmount, status, orderDate FROM Orders WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("orderId"),
                            rs.getInt("userId"),
                            rs.getDouble("totalAmount"),
                            rs.getString("status"),
                            rs.getTimestamp("orderDate")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Updates the status of an order
     */
    public void updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE Orders SET status = ? WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes an order and its associated OrderItems
     */
    public void deleteOrder(int orderId) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // Delete OrderItems
            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM OrderItems WHERE orderId = ?")) {
                ps1.setInt(1, orderId);
                ps1.executeUpdate();
            }
            // Delete Order
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM Orders WHERE orderId = ?")) {
                ps2.setInt(1, orderId);
                ps2.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}