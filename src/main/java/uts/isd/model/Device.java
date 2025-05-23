package uts.isd.model;

public class Device {
    private int deviceId;
    private String name;
    private String type;
    private String description;
    private String unit;
    private double price;
    private int quantity;

    // Default constructor
    public Device() {}

    // Constructor for creating a new device (without ID)
    public Device(String name, String type, String description, String unit, double price, int quantity) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
    }

    // Full constructor for editing/loading
    public Device(int deviceId, String name, String type, String description, String unit, double price, int quantity) {
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
