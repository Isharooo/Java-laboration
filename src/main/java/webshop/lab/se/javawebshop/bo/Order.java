package webshop.lab.se.javawebshop.bo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Business Object för ordrar
 * Status: pending, packed, shipped (betyg 4 & 5)
 */
public class Order {
    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private String status; // "pending", "packed", "shipped"
    private BigDecimal totalAmount;

    // Extra fält för att hålla användarnamn (från JOIN)
    private String username;

    // Lista med orderrader (fylls vid behov)
    private List<OrderItem> orderItems;

    // Konstruktorer
    public Order() {
        this.orderItems = new ArrayList<>();
    }

    public Order(int orderId, int userId, LocalDateTime orderDate,
                 String status, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderItems = new ArrayList<>();
    }

    public Order(int userId, String status, BigDecimal totalAmount) {
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderItems = new ArrayList<>();
    }

    // Getters och Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }

    // Hjälpmetoder för status
    public boolean isPending() {
        return "pending".equals(status);
    }

    public boolean isPacked() {
        return "packed".equals(status);
    }

    public boolean isShipped() {
        return "shipped".equals(status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}