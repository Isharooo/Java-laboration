package webshop.lab.se.javawebshop.ui;

import java.math.BigDecimal;
import java.util.List;

public class OrderInfo {
    private int orderId;
    private String username;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemInfo> orderItems;

    public OrderInfo(int orderId, String username, String status, BigDecimal totalAmount, List<OrderItemInfo> orderItems) {
        this.orderId = orderId;
        this.username = username;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItemInfo> getOrderItems() {
        return orderItems;
    }

    public boolean isPending() {
        return "pending".equals(status);
    }

    public boolean isPacked() {
        return "packed".equals(status);
    }
}