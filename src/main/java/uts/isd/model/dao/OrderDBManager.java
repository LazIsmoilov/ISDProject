package uts.isd.model.dao;

import uts.isd.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDBManager {
    private Connection conn;

    public OrderDBManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * 插入一条新订单，并返回生成的 orderId
     */
    public int addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (userId, totalPrice, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());
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
     * 根据 userId 查询该用户的所有订单
     */
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, userId, totalPrice, status, orderDate FROM Orders WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order(
                            rs.getInt("id"),
                            rs.getInt("userId"),
                            rs.getDouble("totalPrice"),
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
     * 根据 orderId 查询单个订单详情
     */
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT id, userId, totalPrice, status, orderDate FROM Orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("id"),
                            rs.getInt("userId"),
                            rs.getDouble("totalPrice"),
                            rs.getString("status"),
                            rs.getTimestamp("orderDate")
                    );
                }
            }
        }
        return null;
    }

    /**
     * 更新订单状态（如取消、已支付、已发货等）
     */
    public void updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    /**
     * 删除某个订单，同时先删除它的所有 OrderItems 条目
     */
    public void deleteOrder(int orderId) throws SQLException {
        // 先删明细
        try (PreparedStatement ps1 = conn.prepareStatement(
                "DELETE FROM OrderItems WHERE orderId = ?")) {
            ps1.setInt(1, orderId);
            ps1.executeUpdate();
        }
        // 再删主表
        try (PreparedStatement ps2 = conn.prepareStatement(
                "DELETE FROM Orders WHERE id = ?")) {
            ps2.setInt(1, orderId);
            ps2.executeUpdate();
        }
    }
}

