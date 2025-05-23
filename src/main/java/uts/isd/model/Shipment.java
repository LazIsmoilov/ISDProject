package uts.isd.model;

public class Shipment {
    private int shipmentId;
    private int orderId;
    private String method;
    private String shipmentDate;
    private String address;

    public Shipment(int shipmentId, int orderId, String method, String shipmentDate, String address) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.method = method;
        this.shipmentDate = shipmentDate;
        this.address = address;
    }

    public Shipment() {}

    // Getters and Setters
    public int getShipmentId() { return shipmentId; }
    public void setShipmentId(int shipmentId) { this.shipmentId = shipmentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getShipmentDate() { return shipmentDate; }
    public void setShipmentDate(String shipmentDate) { this.shipmentDate = shipmentDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
