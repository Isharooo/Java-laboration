package webshop.lab.se.javawebshop.ui;

import java.math.BigDecimal;

public class CartItemInfo {
    private ProductInfo product;
    private int quantity;
    private BigDecimal subtotal;

    public CartItemInfo(ProductInfo product, int quantity, BigDecimal subtotal) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}