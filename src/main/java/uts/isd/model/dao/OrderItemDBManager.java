package uts.isd.model.dao;

import uts.isd.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDBManager {
    private Connection conn;

    public OrderItemDBManager(Connection conn) {
        this.conn = conn;
    }

    /** 插入一条订单明细 */
    public void addOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO OrderItems (orderId, productId, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            ps.executeUpdate();
        }
    }

    /** 根据订单ID查询所有明细 */
    public List<OrderItem> getItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT id, orderId, productId, quantity, unitPrice FROM OrderItems WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("id"),
                            rs.getInt("orderId"),
                            rs.getInt("productId"),
                            rs.getInt("quantity"),
                            rs.getDouble("unitPrice")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    /** 删除某个订单的所有明细（例如在取消订单时使用） */
    public void deleteItemsByOrderId(int orderId) throws SQLException {
        String sql = "DELETE FROM OrderItems WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }
    }

    /** 更新某条明细的数量和单价 */
    public void updateOrderItem(OrderItem item) throws SQLException {
        String sql = "UPDATE OrderItems SET quantity = ?, unitPrice = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getQuantity());
            ps.setDouble(2, item.getUnitPrice());
            ps.setInt(3, item.getOrderId());
            ps.executeUpdate();
        }
    }
}
