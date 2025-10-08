package webshop.lab.se.javawebshop.ui;

import java.math.BigDecimal;
import java.util.List;

public class CartInfo {
    private List<CartItemInfo> items;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private boolean empty;

    public CartInfo(List<CartItemInfo> items, BigDecimal totalPrice, int totalQuantity, boolean empty) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.empty = empty;
    }

    public List<CartItemInfo> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public boolean isEmpty() {
        return empty;
    }
}