public class Shipment {
    private int id;
    private int orderId;
    private String shipmentMethod;
    private String shipmentDate;
    private String shippingAddress;

    public Shipment() {}

    public Shipment(int id, int orderId, String shipmentMethod, String shipmentDate, String shippingAddress) {
        this.id = id;
        this.orderId = orderId;
        this.shipmentMethod = shipmentMethod;
        this.shipmentDate = shipmentDate;
        this.shippingAddress = shippingAddress;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getShipmentMethod() { return shipmentMethod; }
    public void setShipmentMethod(String shipmentMethod) { this.shipmentMethod = shipmentMethod; }

    public String getShipmentDate() { return shipmentDate; }
    public void setShipmentDate(String shipmentDate) { this.shipmentDate = shipmentDate; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
