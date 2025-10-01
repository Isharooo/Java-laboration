package webshop.lab.se.javawebshop.db;

import webshop.lab.se.javawebshop.bo.Order;
import webshop.lab.se.javawebshop.bo.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object för ordrar
 * VIKTIGT: Använder transaktioner för betyg 4
 */
public class OrderDAO {

    private DBManager dbManager;
    private ProductDAO productDAO;

    public OrderDAO() {
        this.dbManager = DBManager.getInstance();
        this.productDAO = new ProductDAO();
    }

    /**
     * Skapar en order MED TRANSAKTION (betyg 4 krav!)
     *
     * Steg:
     * 1. Starta transaktion (setAutoCommit(false))
     * 2. Kontrollera lagersaldo för alla produkter
     * 3. Skapa order
     * 4. Skapa order_items
     * 5. Minska lagersaldo för varje produkt
     * 6. Commit transaktion
     * 7. Vid fel: Rollback
     *
     * @param order Order-objekt med orderItems
     * @return true om ordern skapades
     */
    public boolean createOrder(Order order) {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtOrderItem = null;

        try {
            conn = dbManager.getConnection();

            // VIKTIGT FÖR BETYG 4: Starta transaktion
            conn.setAutoCommit(false);
            System.out.println("Transaktion startad för order");

            // Steg 1: Kontrollera lagersaldo för alla produkter FÖRST
            for (OrderItem item : order.getOrderItems()) {
                if (!productDAO.checkStock(item.getProductId(), item.getQuantity())) {
                    throw new SQLException("Otillräckligt lagersaldo för produkt ID: " + item.getProductId());
                }
            }
            System.out.println("Lagersaldokontroll OK");

            // Steg 2: Skapa själva ordern
            String sqlOrder = "INSERT INTO orders (user_id, status, total_amount) VALUES (?, ?, ?)";
            pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);

            pstmtOrder.setInt(1, order.getUserId());
            pstmtOrder.setString(2, order.getStatus());
            pstmtOrder.setBigDecimal(3, order.getTotalAmount());

            int rowsAffected = pstmtOrder.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Misslyckades med att skapa order");
            }

            // Hämta genererat order_id
            ResultSet generatedKeys = pstmtOrder.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Misslyckades med att få order_id");
            }

            System.out.println("Order skapad med ID: " + order.getOrderId());

            // Steg 3: Skapa order_items och minska lagersaldo
            String sqlOrderItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            pstmtOrderItem = conn.prepareStatement(sqlOrderItem);

            for (OrderItem item : order.getOrderItems()) {
                // Lägg till orderrad
                pstmtOrderItem.setInt(1, order.getOrderId());
                pstmtOrderItem.setInt(2, item.getProductId());
                pstmtOrderItem.setInt(3, item.getQuantity());
                pstmtOrderItem.setBigDecimal(4, item.getPrice());
                pstmtOrderItem.executeUpdate();

                // Minska lagersaldo (negativt tal)
                boolean stockUpdated = productDAO.updateStock(item.getProductId(), -item.getQuantity());
                if (!stockUpdated) {
                    throw new SQLException("Misslyckades med att uppdatera lagersaldo för produkt ID: " + item.getProductId());
                }
            }

            System.out.println("Order_items skapade och lagersaldo uppdaterat");

            // VIKTIGT FÖR BETYG 4: Commit transaktion
            conn.commit();
            System.out.println("Transaktion committad - Order " + order.getOrderId() + " är klar!");

            return true;

        } catch (SQLException e) {
            System.err.println("Fel vid skapande av order: " + e.getMessage());
            e.printStackTrace();

            // VIKTIGT FÖR BETYG 4: Rollback vid fel
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
            // Stäng resurser och återställ autoCommit
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

    /**
     * Hämtar en order baserat på ID
     *
     * @param orderId Order-ID
     * @return Order-objekt med orderItems, eller null
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.order_id, o.user_id, o.order_date, o.status, o.total_amount, u.username " +
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
                // Hämta orderrader
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

    /**
     * Hämtar alla ordrar för en användare
     *
     * @param userId Användar-ID
     * @return Lista med ordrar
     */
    public List<Order> getOrdersByUser(int userId) {
        String sql = "SELECT o.order_id, o.user_id, o.order_date, o.status, o.total_amount, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.user_id = ? " +
                "ORDER BY o.order_date DESC";

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
                // Hämta orderrader för varje order
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

    /**
     * Hämtar alla ordrar (för admin/warehouse)
     *
     * @return Lista med alla ordrar
     */
    public List<Order> getAllOrders() {
        String sql = "SELECT o.order_id, o.user_id, o.order_date, o.status, o.total_amount, u.username " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "ORDER BY o.order_date DESC";

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
                // Hämta orderrader
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

    /**
     * Uppdaterar status för en order
     * Används för warehouse-personal att "packa" ordrar (betyg 5)
     *
     * @param orderId Order-ID
     * @param status Ny status ("pending", "packed", "shipped")
     * @return true om statusen uppdaterades
     */
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

    /**
     * Hämtar orderrader för en specifik order
     *
     * @param orderId Order-ID
     * @return Lista med OrderItem
     */
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

    /**
     * Hjälpmetod för att extrahera Order från ResultSet
     */
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));

        Timestamp timestamp = rs.getTimestamp("order_date");
        if (timestamp != null) {
            order.setOrderDate(timestamp.toLocalDateTime());
        }

        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setUsername(rs.getString("username"));

        return order;
    }

    /**
     * Hjälpmetod för att extrahera OrderItem från ResultSet
     */
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

    /**
     * Hjälpmetod för att stänga resurser
     */
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