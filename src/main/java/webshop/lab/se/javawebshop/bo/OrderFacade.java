package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.OrderDAO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Facade för orderhantering (BO-lager)
 * Hanterar orders och checkout-process
 */
public class OrderFacade {

    private OrderDAO orderDAO;

    public OrderFacade() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Skapar en order från en varukorg (med transaktion!)
     *
     * @param userId Användar-ID
     * @param cart Varukorgen
     * @return Order-objekt om lyckat, annars null
     */
    public Order createOrderFromCart(int userId, Cart cart) {
        // Validering
        if (cart == null || cart.isEmpty()) {
            System.err.println("Varukorgen är tom");
            return null;
        }

        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return null;
        }

        // Skapa Order-objekt
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("pending");
        order.setTotalAmount(cart.getTotalPrice());

        // Konvertera CartItems till OrderItems
        for (Cart.CartItem cartItem : cart.getItems().values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProduct().getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            order.addOrderItem(orderItem);
        }

        // Skapa ordern i databasen (MED TRANSAKTION)
        boolean success = orderDAO.createOrder(order);

        if (success) {
            System.out.println("Order skapad via facade: Order ID " + order.getOrderId());
            return order;
        } else {
            System.err.println("Misslyckades med att skapa order via facade");
            return null;
        }
    }

    /**
     * Hämtar en specifik order
     */
    public Order getOrderById(int orderId) {
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return null;
        }

        return orderDAO.getOrderById(orderId);
    }

    /**
     * Hämtar alla ordrar för en användare
     */
    public List<Order> getOrdersByUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return List.of(); // Tom lista
        }

        return orderDAO.getOrdersByUser(userId);
    }

    /**
     * Hämtar alla ordrar (Admin/Warehouse)
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    /**
     * Hämtar ordrar efter status (Warehouse)
     */
    public List<Order> getOrdersByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            System.err.println("Status saknas");
            return List.of(); // Tom lista
        }

        return orderDAO.getOrdersByStatus(status);
    }

    /**
     * Uppdaterar orderstatus (Warehouse)
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        // Validering
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return false;
        }

        if (newStatus == null || newStatus.trim().isEmpty()) {
            System.err.println("Status saknas");
            return false;
        }

        // Validera att status är giltig
        if (!newStatus.equals("pending") && !newStatus.equals("packed") && !newStatus.equals("shipped")) {
            System.err.println("Ogiltig status: " + newStatus);
            return false;
        }

        return orderDAO.updateOrderStatus(orderId, newStatus);
    }

    /**
     * Hämtar orderrader för en order
     */
    public List<OrderItem> getOrderItems(int orderId) {
        if (orderId <= 0) {
            System.err.println("Ogiltigt order-ID");
            return List.of(); // Tom lista
        }

        return orderDAO.getOrderItems(orderId);
    }
}