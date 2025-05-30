package uts.isd.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int productId;
    private int quantity;
    private double unitPrice;

    public CartItem() { }

    public CartItem(int productId, int quantity, double unitPrice) {
        this.productId = productId;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
