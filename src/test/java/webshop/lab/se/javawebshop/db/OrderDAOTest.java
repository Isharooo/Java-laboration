package webshop.lab.se.javawebshop.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.bo.Order;
import webshop.lab.se.javawebshop.bo.OrderItem;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDAOTest {

    @Mock
    private DBManager dbManager;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement pstmtOrder;

    @Mock
    private PreparedStatement pstmtOrderItem;

    @Mock
    private PreparedStatement pstmtGeneral;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private OrderDAO orderDAO;
    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() throws Exception {
        orderDAO = new OrderDAO();

        Field dbManagerField = OrderDAO.class.getDeclaredField("dbManager");
        dbManagerField.setAccessible(true);
        dbManagerField.set(orderDAO, dbManager);

        Field productDAOField = OrderDAO.class.getDeclaredField("productDAO");
        productDAOField.setAccessible(true);
        productDAOField.set(orderDAO, productDAO);

        testOrderItem = new OrderItem();
        testOrderItem.setProductId(1);
        testOrderItem.setQuantity(2);
        testOrderItem.setPrice(new BigDecimal("99.99"));

        testOrder = new Order();
        testOrder.setUserId(1);
        testOrder.setStatus("pending");
        testOrder.setTotalAmount(new BigDecimal("199.98"));
        testOrder.setOrderItems(List.of(testOrderItem));

        when(dbManager.getConnection()).thenReturn(connection);
    }

    @Test
    void createOrder_Success() throws SQLException {
        when(productDAO.checkStock(any(Connection.class), eq(1), eq(2))).thenReturn(true);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmtOrder);
        when(connection.prepareStatement(anyString())).thenReturn(pstmtOrderItem);
        when(pstmtOrder.executeUpdate()).thenReturn(1);
        when(pstmtOrder.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);
        when(pstmtOrderItem.executeUpdate()).thenReturn(1);
        when(productDAO.updateStock(any(Connection.class), eq(1), eq(-2))).thenReturn(true);

        boolean result = orderDAO.createOrder(testOrder);

        assertTrue(result);
        assertEquals(123, testOrder.getOrderId());
        verify(connection, times(1)).setAutoCommit(false);
        verify(connection, times(1)).commit();
        verify(productDAO, times(1)).checkStock(any(Connection.class), eq(1), eq(2));
        verify(productDAO, times(1)).updateStock(any(Connection.class), eq(1), eq(-2));
    }

    @Test
    void createOrder_InsufficientStock() throws SQLException {
        when(productDAO.checkStock(any(Connection.class), eq(1), eq(2))).thenReturn(false);

        boolean result = orderDAO.createOrder(testOrder);

        assertFalse(result);
        verify(connection, times(1)).rollback();
        verify(connection, never()).commit();
    }

    @Test
    void createOrder_FailToCreateOrder() throws SQLException {
        when(productDAO.checkStock(any(Connection.class), eq(1), eq(2))).thenReturn(true);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmtOrder);
        when(pstmtOrder.executeUpdate()).thenReturn(0);

        boolean result = orderDAO.createOrder(testOrder);

        assertFalse(result);
        verify(connection, times(1)).rollback();
        verify(connection, never()).commit();
    }

    @Test
    void createOrder_FailToGetGeneratedKeys() throws SQLException {
        when(productDAO.checkStock(any(Connection.class), eq(1), eq(2))).thenReturn(true);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmtOrder);
        when(pstmtOrder.executeUpdate()).thenReturn(1);
        when(pstmtOrder.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        boolean result = orderDAO.createOrder(testOrder);

        assertFalse(result);
        verify(connection, times(1)).rollback();
        verify(connection, never()).commit();
    }

    @Test
    void createOrder_FailToUpdateStock() throws SQLException {
        when(productDAO.checkStock(any(Connection.class), eq(1), eq(2))).thenReturn(true);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmtOrder);
        when(connection.prepareStatement(anyString())).thenReturn(pstmtOrderItem);
        when(pstmtOrder.executeUpdate()).thenReturn(1);
        when(pstmtOrder.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);
        when(pstmtOrderItem.executeUpdate()).thenReturn(1);
        when(productDAO.updateStock(any(Connection.class), eq(1), eq(-2))).thenReturn(false);

        boolean result = orderDAO.createOrder(testOrder);

        assertFalse(result);
        verify(connection, times(1)).rollback();
        verify(connection, never()).commit();
    }

    @Test
    void getOrderById_NotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(pstmtGeneral);
        when(pstmtGeneral.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Order result = orderDAO.getOrderById(1);

        assertNull(result);
    }

    @Test
    void getOrdersByStatus_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(pstmtGeneral);
        when(pstmtGeneral.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("order_id")).thenReturn(1);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("status")).thenReturn("pending");
        when(resultSet.getBigDecimal("total_amount")).thenReturn(new BigDecimal("199.98"));
        when(resultSet.getString("username")).thenReturn("testuser");

        List<Order> orders = orderDAO.getOrdersByStatus("pending");

        assertEquals(1, orders.size());
        assertEquals("pending", orders.get(0).getStatus());
        verify(pstmtGeneral, times(1)).setString(1, "pending");
    }

    @Test
    void updateOrderStatus_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(pstmtGeneral);
        when(pstmtGeneral.executeUpdate()).thenReturn(1);

        boolean result = orderDAO.updateOrderStatus(1, "shipped");

        assertTrue(result);
        verify(pstmtGeneral, times(1)).setString(1, "shipped");
        verify(pstmtGeneral, times(1)).setInt(2, 1);
    }

    @Test
    void updateOrderStatus_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(pstmtGeneral);
        when(pstmtGeneral.executeUpdate()).thenReturn(0);

        boolean result = orderDAO.updateOrderStatus(1, "shipped");

        assertFalse(result);
    }

    @Test
    void getOrderItems_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(pstmtGeneral);
        when(pstmtGeneral.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("order_item_id")).thenReturn(1);
        when(resultSet.getInt("order_id")).thenReturn(1);
        when(resultSet.getInt("product_id")).thenReturn(1);
        when(resultSet.getInt("quantity")).thenReturn(2);
        when(resultSet.getBigDecimal("price")).thenReturn(new BigDecimal("99.99"));
        when(resultSet.getString("product_name")).thenReturn("Test Product");

        List<OrderItem> items = orderDAO.getOrderItems(1);

        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getOrderItemId());
        assertEquals(1, items.get(0).getProductId());
        verify(pstmtGeneral, times(1)).setInt(1, 1);
    }

    @Test
    void getOrderItems_Empty() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(pstmtGeneral);
        when(pstmtGeneral.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<OrderItem> items = orderDAO.getOrderItems(1);

        assertTrue(items.isEmpty());
    }
}