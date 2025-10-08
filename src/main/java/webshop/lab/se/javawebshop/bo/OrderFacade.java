package webshop.lab.se.javawebshop.bo;

import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.db.OrderDAO;
import webshop.lab.se.javawebshop.ui.OrderInfo;
import webshop.lab.se.javawebshop.ui.OrderItemInfo;

import java.util.ArrayList;
import java.util.List;

public class OrderFacade {

    private OrderDAO orderDAO;

    public OrderFacade() {
        this.orderDAO = new OrderDAO();
    }

    public OrderInfo createOrderFromCart(int userId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            System.err.println("Varukorgen är tom");
            return null;
        }

        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return null;
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("pending");
        order.setTotalAmount(cart.getTotalPrice());

        for (Cart.CartItem cartItem : cart.getItems().values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProduct().getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            order.addOrderItem(orderItem);
        }

        boolean success = orderDAO.createOrder(order);

        if (success) {
            System.out.println("Order skapad via facade: Order ID " + order.getOrderId());
            return getOrderById(order.getOrderId());
        } else {
            System.err.println("Misslyckades med att skapa order via facade");
            return null;
        }
    }

    public OrderInfo getOrderById(int orderId) {
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return null;
        }

        Order order = orderDAO.getOrderById(orderId);
        return order != null ? convertToOrderInfo(order) : null;
    }

    public List<OrderInfo> getOrdersByUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return List.of();
        }

        List<Order> orders = orderDAO.getOrdersByUser(userId);
        return convertToOrderInfoList(orders);
    }

    public List<OrderInfo> getOrdersByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            System.err.println("Status saknas");
            return List.of();
        }

        List<Order> orders = orderDAO.getOrdersByStatus(status);
        return convertToOrderInfoList(orders);
    }

    public List<OrderInfo> getAllOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        return convertToOrderInfoList(orders);
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return false;
        }

        if (newStatus == null || newStatus.trim().isEmpty()) {
            System.err.println("Status saknas");
            return false;
        }

        if (!newStatus.equals("pending") && !newStatus.equals("packed") && !newStatus.equals("shipped")) {
            System.err.println("Ogiltig status: " + newStatus);
            return false;
        }

        return orderDAO.updateOrderStatus(orderId, newStatus);
    }

    // ========== KONVERTERINGSMETODER (privata) ==========

    private OrderInfo convertToOrderInfo(Order order) {
        List<OrderItemInfo> orderItemInfoList = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            OrderItemInfo itemInfo = new OrderItemInfo(
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSubtotal()
            );
            orderItemInfoList.add(itemInfo);
        }

        return new OrderInfo(
                order.getOrderId(),
                order.getUsername(),
                order.getStatus(),
                order.getTotalAmount(),
                orderItemInfoList
        );
    }

    private List<OrderInfo> convertToOrderInfoList(List<Order> orders) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (Order order : orders) {
            orderInfoList.add(convertToOrderInfo(order));
        }
        return orderInfoList;
    }
}