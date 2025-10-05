package webshop.lab.se.javawebshop.db;

import webshop.lab.se.javawebshop.bo.Order;
import webshop.lab.se.javawebshop.bo.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private DBManager dbManager;
    private ProductDAO productDAO;

    public OrderDAO() {
        this.dbManager = DBManager.getInstance();
        this.productDAO = new ProductDAO();
    }

    public boolean createOrder(Order order) {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtOrderItem = null;

        try {
            conn = dbManager.getConnection();

            conn.setAutoCommit(false);
            System.out.println("Transaktion startad för order");

            for (OrderItem item : order.getOrderItems()) {
                if (!productDAO.checkStock(conn, item.getProductId(), item.getQuantity())) {
                    throw new SQLException("Otillräckligt lagersaldo för produkt ID: " + item.getProductId());
                }
            }
            System.out.println("Lagersaldokontroll OK");

            String sqlOrder = "INSERT INTO orders (user_id, status, total_amount) VALUES (?, ?, ?)";
            pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);

            pstmtOrder.setInt(1, order.getUserId());
            pstmtOrder.setString(2, order.getStatus());
            pstmtOrder.setBigDecimal(3, order.getTotalAmount());

            int rowsAffected = pstmtOrder.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Misslyckades med att skapa order");
            }

            ResultSet generatedKeys = pstmtOrder.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Misslyckades med att få order_id");
            }

            System.out.println("Order skapad med ID: " + order.getOrderId());

            String sqlOrderItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            pstmtOrderItem = conn.prepareStatement(sqlOrderItem);

            for (OrderItem item : order.getOrderItems()) {
                pstmtOrderItem.setInt(1, order.getOrderId());
                pstmtOrderItem.setInt(2, item.getProductId());
                pstmtOrderItem.setInt(3, item.getQuantity());
                pstmtOrderItem.setBigDecimal(4, item.getPrice());
                pstmtOrderItem.executeUpdate();

                boolean stockUpdated = productDAO.updateStock(conn, item.getProductId(), -item.getQuantity());
                if (!stockUpdated) {
                    throw new SQLException("Misslyckades med att uppdatera lagersaldo för produkt ID: " + item.getProductId());
                }
            }

            System.out.println("Order_items skapade och lagersaldo uppdaterat");

            conn.commit();
            System.out.println("Transaktion committad - Order " + order.getOrderId() + " är klar!");

            return true;

        } catch (SQLException e) {
            System.err.println("Fel vid skapande av order: " + e.getMessage());
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaktion rollbackad på grund av fel");
                } catch (SQLException ex) {
                    System.err.println("Fel vid rollback: " + ex.getMessage());
                }
            }

            return false;

        } finally {
            if (pstmtOrderItem != null) {
                try { pstmtOrderItem.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (pstmtOrder != null) {
                try { pstmtOrder.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Återställ autoCommit
                    dbManager.closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT o.order_id, o.user_id, o.status, o.total_amount, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.order_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(orderId));
                return order;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av order: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return null;
    }

    public List<Order> getOrdersByUser(int userId) {
        String sql = "SELECT o.order_id, o.user_id, o.status, o.total_amount, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.user_id = ? " +
                "ORDER BY o.order_id DESC";

        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(order.getOrderId()));
                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av ordrar för användare: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return orders;
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT o.order_id, o.user_id, o.status, o.total_amount, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "ORDER BY o.order_id DESC";

        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(order.getOrderId()));
                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av alla ordrar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return orders;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.user_id, o.status, o.total_amount, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.status = ? " +
                "ORDER BY o.order_id DESC";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av ordrar efter status: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order " + orderId + " status uppdaterad till: " + status);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid uppdatering av orderstatus: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.price, p.name AS product_name " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = ?";

        List<OrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                orderItems.add(extractOrderItemFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av orderrader: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return orderItems;
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setUsername(rs.getString("username"));

        return order;
    }

    private OrderItem extractOrderItemFromResultSet(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setProductName(rs.getString("product_name"));

        return item;
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (conn != null) {
            dbManager.closeConnection(conn);
        }
    }
}