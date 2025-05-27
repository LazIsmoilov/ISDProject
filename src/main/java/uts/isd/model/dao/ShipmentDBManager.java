package uts.isd.model.dao;

import uts.isd.model.Shipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDBManager {
    private final Connection conn;

    public ShipmentDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS Shipment (" +
                "shipmentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "orderId INTEGER NOT NULL, " +
                "method TEXT NOT NULL, " +
                "shipmentDate TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "finalised INTEGER DEFAULT 0)");
    }

    public void addShipment(int orderId, String method, String shipmentDate, String address) throws SQLException {
        String sql = "INSERT INTO Shipment (orderId, method, shipmentDate, address, finalised) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setString(2, method);
            stmt.setString(3, shipmentDate);
            stmt.setString(4, address);
            stmt.setBoolean(5, false);
            stmt.executeUpdate();

            System.out.println("✅ Shipment inserted: OrderID=" + orderId + ", Method=" + method);

            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException e) {
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
            throw e;
        }
    }

    public Shipment getShipmentById(int shipmentId) throws SQLException {
        String sql = "SELECT * FROM Shipment WHERE shipmentId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Shipment shipment = new Shipment();
                shipment.setShipmentId(rs.getInt("shipmentId"));
                shipment.setOrderId(rs.getInt("orderId"));
                shipment.setMethod(rs.getString("method"));
                shipment.setShipmentDate(rs.getString("shipmentDate"));
                shipment.setAddress(rs.getString("address"));
                shipment.setFinalised(rs.getInt("finalised") == 1);
                return shipment;
            }
            return null;
        }
    }

    public boolean isFinalised(int shipmentId) throws SQLException {
        String sql = "SELECT finalised FROM Shipment WHERE shipmentId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("finalised") == 1;
                }
                return false;
            }
        }
    }

    public List<Shipment> getShipmentsByOrderId(int orderId) throws SQLException {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM Shipment WHERE orderId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Shipment shipment = new Shipment();
                shipment.setShipmentId(rs.getInt("shipmentId"));
                shipment.setOrderId(rs.getInt("orderId"));
                shipment.setMethod(rs.getString("method"));
                shipment.setShipmentDate(rs.getString("shipmentDate"));
                shipment.setAddress(rs.getString("address"));
                shipment.setFinalised(rs.getInt("finalised") == 1);
                shipments.add(shipment);
            }
        }
        return shipments;
    }

    public void updateShipment(Shipment shipment) throws SQLException {
        String sql = "UPDATE Shipment SET method = ?, shipmentDate = ?, address = ? WHERE shipmentId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shipment.getMethod());
            stmt.setString(2, shipment.getShipmentDate());
            stmt.setString(3, shipment.getAddress());
            stmt.setInt(4, shipment.getShipmentId());
            stmt.executeUpdate();

            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException e) {
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
            throw e;
        }
    }

    public List<Shipment> searchShipments(String keyword) throws SQLException {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM Shipment WHERE " +
                "CAST(shipmentId AS TEXT) LIKE ? OR " +
                "CAST(orderId AS TEXT) LIKE ? OR " +
                "method LIKE ? OR " +
                "address LIKE ? OR " +
                "shipmentDate LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, searchPattern);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Shipment shipment = new Shipment();
                shipment.setShipmentId(rs.getInt("shipmentId"));
                shipment.setOrderId(rs.getInt("orderId"));
                shipment.setMethod(rs.getString("method"));
                shipment.setShipmentDate(rs.getString("shipmentDate"));
                shipment.setAddress(rs.getString("address"));
                shipment.setFinalised(rs.getInt("finalised") == 1);
                shipments.add(shipment);
            }
        }
        return shipments;
    }

    public void deleteShipment(int shipmentId) throws SQLException {
        String sql = "DELETE FROM Shipment WHERE shipmentId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipmentId);
            stmt.executeUpdate();

            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException e) {
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
            throw e;
        }
    }

    public List<Shipment> getAllShipments() throws SQLException {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM Shipment";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Shipment shipment = new Shipment();
                shipment.setShipmentId(rs.getInt("shipmentId"));
                shipment.setOrderId(rs.getInt("orderId"));
                shipment.setMethod(rs.getString("method"));
                shipment.setShipmentDate(rs.getString("shipmentDate"));
                shipment.setAddress(rs.getString("address"));
                shipment.setFinalised(rs.getInt("finalised") == 1);
                shipments.add(shipment);
            }
        }
        return shipments;
    }

    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
