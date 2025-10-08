package webshop.lab.se.javawebshop.ui;

import java.math.BigDecimal;

public class OrderItemInfo {
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public OrderItemInfo(String productName, int quantity, BigDecimal price, BigDecimal subtotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}