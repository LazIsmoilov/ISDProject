package uts.isd.model.dao;

import uts.isd.model.Device;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceDBManager {
    private Connection conn;

    public DeviceDBManager(Connection conn) {
        this.conn = conn;
    }

    // Get all devices
    public List<Device> getAllDevices() throws SQLException {
        String query = "SELECT * FROM Devices";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        List<Device> devices = new ArrayList<>();
        while (rs.next()) {
            Device d = new Device(
                    rs.getInt("device_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("unit"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
            );
            devices.add(d);
        }

        rs.close();
        stmt.close();
        return devices;
    }

    // Search devices by name and type
    public List<Device> searchDevices(String keyword, String typeFilter) throws SQLException {
        List<Device> results = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM Devices WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            query.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + keyword + "%");
        }

        if (typeFilter != null && !typeFilter.trim().isEmpty()) {
            query.append(" AND type = ?");
            params.add(typeFilter);
        }

        PreparedStatement stmt = conn.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Device d = new Device(
                    rs.getInt("device_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("unit"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
            );
            results.add(d);
        }

        rs.close();
        stmt.close();
        return results;
    }

    // Get a single device by ID
    public Device getDeviceById(int id) throws SQLException {
        String query = "SELECT * FROM Devices WHERE device_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        Device device = null;
        if (rs.next()) {
            device = new Device(
                    rs.getInt("device_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("unit"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
            );
        }

        rs.close();
        stmt.close();
        return device;
    }

    // Add a new device
    public void addDevice(Device device) throws SQLException {
        String query = "INSERT INTO Devices (name, type, description, unit, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, device.getName());
        stmt.setString(2, device.getType());
        stmt.setString(3, device.getDescription());
        stmt.setString(4, device.getUnit());
        stmt.setDouble(5, device.getPrice());
        stmt.setInt(6, device.getQuantity());
        stmt.executeUpdate();
        stmt.close();
    }

    // Update an existing device
    public void updateDevice(Device device) throws SQLException {
        String query = "UPDATE Devices SET name = ?, type = ?, description = ?, unit = ?, price = ?, quantity = ? WHERE device_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, device.getName());
        stmt.setString(2, device.getType());
        stmt.setString(3, device.getDescription());
        stmt.setString(4, device.getUnit());
        stmt.setDouble(5, device.getPrice());
        stmt.setInt(6, device.getQuantity());
        stmt.setInt(7, device.getDeviceId());
        stmt.executeUpdate();
        stmt.close();
    }

    // Delete a device
    public void deleteDevice(int id) throws SQLException {
        String query = "DELETE FROM Devices WHERE device_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    // === DASHBOARD STATISTICS METHODS ===

    // Get total number of products
    public int getTotalProductCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Devices";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        stmt.close();
        return count;
    }

    // Get total quantity of all products
    public int getTotalQuantity() throws SQLException {
        String query = "SELECT SUM(quantity) FROM Devices";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        int total = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        stmt.close();
        return total;
    }

    // Get count of in-stock devices
    public int getInStockCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Devices WHERE quantity > 0";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        stmt.close();
        return count;
    }

    // Get count of out-of-stock devices
    public int getOutOfStockCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Devices WHERE quantity = 0";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        int count = rs.next() ? rs.getInt(1) : 0;
        rs.close();
        stmt.close();
        return count;
    }
}
