package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.OrderDAO;

import java.util.List;

public class OrderFacade {

    private OrderDAO orderDAO;

    public OrderFacade() {
        this.orderDAO = new OrderDAO();
    }

    public Order createOrderFromCart(int userId, Cart cart) {
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
            return order;
        } else {
            System.err.println("Misslyckades med att skapa order via facade");
            return null;
        }
    }

    public Order getOrderById(int orderId) {
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return null;
        }

        return orderDAO.getOrderById(orderId);
    }

    public List<Order> getOrdersByUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return List.of();
        }

        return orderDAO.getOrdersByUser(userId);
    }

    public List<Order> getOrdersByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            System.err.println("Status saknas");
            return List.of();
        }

        return orderDAO.getOrdersByStatus(status);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
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

    public List<OrderItem> getOrderItems(int orderId) {
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return List.of(); // Tom lista
        }

        return orderDAO.getOrderItems(orderId);
    }
}