package uts.isd.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAO {
    private Connection connection;
    private UserDBManager userDBManager;
    private DeviceDBManager deviceDBManager;
    private OrderDBManager orderDBManager;
    private OrderItemDBManager orderItemDBManager;
    private PaymentDBManager paymentDBManager;
    private ShipmentDBManager shipmentDBManager;


    public DAO() throws SQLException {
        this.connection = new DBConnector().getConnection();
        try {
            this.userDBManager = new UserDBManager(connection);
            this.deviceDBManager = new DeviceDBManager(connection);
            this.orderDBManager = new OrderDBManager(connection);
            this.orderItemDBManager = new OrderItemDBManager(connection);
            this.paymentDBManager = new PaymentDBManager(connection);
            this.shipmentDBManager = new ShipmentDBManager(connection);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public UserDBManager Users() {
        return userDBManager;
    }
    public DeviceDBManager Devices() {
        return deviceDBManager;
    }
    public OrderDBManager Orders() {
        return orderDBManager;
    }
    public OrderItemDBManager OrderItems() {
        return orderItemDBManager;
    }
    public PaymentDBManager Payments() {
        return paymentDBManager;
    }
    public ShipmentDBManager getShipmentManager() {
        return shipmentDBManager;
    }

}
