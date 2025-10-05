package webshop.lab.se.javawebshop.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.db.OrderDAO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {

    @Mock
    private OrderDAO orderDAO;

    @InjectMocks
    private OrderFacade orderFacade;

    private Cart cart;
    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        Product testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));

        cart = new Cart();
        cart.addProduct(testProduct, 2);

        testOrderItem = new OrderItem();
        testOrderItem.setProductId(1);
        testOrderItem.setQuantity(2);
        testOrderItem.setPrice(new BigDecimal("99.99"));

        testOrder = new Order();
        testOrder.setOrderId(1);
        testOrder.setUserId(1);
        testOrder.setStatus("pending");
        testOrder.setTotalAmount(new BigDecimal("199.98"));
        testOrder.addOrderItem(testOrderItem);
    }

    @Test
    void createOrderFromCart_WithValidData_ShouldReturnOrder() {
        when(orderDAO.createOrder(any(Order.class))).thenReturn(true);

        Order result = orderFacade.createOrderFromCart(1, cart);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("pending", result.getStatus());
        assertEquals(new BigDecimal("199.98"), result.getTotalAmount());
        verify(orderDAO, times(1)).createOrder(any(Order.class));
    }

    @Test
    void createOrderFromCart_WithNullCart_ShouldReturnNull() {
        Order result = orderFacade.createOrderFromCart(1, null);

        assertNull(result);
        verify(orderDAO, never()).createOrder(any());
    }

    @Test
    void createOrderFromCart_WithEmptyCart_ShouldReturnNull() {
        Cart emptyCart = new Cart();

        Order result = orderFacade.createOrderFromCart(1, emptyCart);

        assertNull(result);
        verify(orderDAO, never()).createOrder(any());
    }

    @Test
    void createOrderFromCart_WithInvalidUserId_ShouldReturnNull() {
        Order result = orderFacade.createOrderFromCart(0, cart);

        assertNull(result);
        verify(orderDAO, never()).createOrder(any());
    }

    @Test
    void createOrderFromCart_WhenDAOFails_ShouldReturnNull() {
        when(orderDAO.createOrder(any(Order.class))).thenReturn(false);

        Order result = orderFacade.createOrderFromCart(1, cart);

        assertNull(result);
        verify(orderDAO, times(1)).createOrder(any(Order.class));
    }

    @Test
    void getOrderById_WithValidId_ShouldReturnOrder() {
        when(orderDAO.getOrderById(1)).thenReturn(testOrder);

        Order result = orderFacade.getOrderById(1);

        assertEquals(testOrder, result);
        verify(orderDAO, times(1)).getOrderById(1);
    }

    @Test
    void getOrderById_WithInvalidId_ShouldReturnNull() {
        Order result = orderFacade.getOrderById(0);

        assertNull(result);
        verify(orderDAO, never()).getOrderById(anyInt());
    }

    @Test
    void getOrdersByUser_WithValidUserId_ShouldReturnOrders() {
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderDAO.getOrdersByUser(1)).thenReturn(expectedOrders);

        List<Order> result = orderFacade.getOrdersByUser(1);

        assertEquals(expectedOrders, result);
        verify(orderDAO, times(1)).getOrdersByUser(1);
    }

    @Test
    void getOrdersByUser_WithInvalidUserId_ShouldReturnEmptyList() {
        List<Order> result = orderFacade.getOrdersByUser(0);

        assertTrue(result.isEmpty());
        verify(orderDAO, never()).getOrdersByUser(anyInt());
    }

    @Test
    void getOrdersByStatus_WithValidStatus_ShouldReturnOrders() {
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderDAO.getOrdersByStatus("pending")).thenReturn(expectedOrders);

        List<Order> result = orderFacade.getOrdersByStatus("pending");

        assertEquals(expectedOrders, result);
        verify(orderDAO, times(1)).getOrdersByStatus("pending");
    }

    @Test
    void getOrdersByStatus_WithNullStatus_ShouldReturnEmptyList() {
        List<Order> result = orderFacade.getOrdersByStatus(null);

        assertTrue(result.isEmpty());
        verify(orderDAO, never()).getOrdersByStatus(anyString());
    }

    @Test
    void getOrdersByStatus_WithEmptyStatus_ShouldReturnEmptyList() {
        List<Order> result = orderFacade.getOrdersByStatus("");

        assertTrue(result.isEmpty());
        verify(orderDAO, never()).getOrdersByStatus(anyString());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderDAO.getAllOrders()).thenReturn(expectedOrders);

        List<Order> result = orderFacade.getAllOrders();

        assertEquals(expectedOrders, result);
        verify(orderDAO, times(1)).getAllOrders();
    }

    @Test
    void updateOrderStatus_WithValidData_ShouldReturnTrue() {
        when(orderDAO.updateOrderStatus(1, "shipped")).thenReturn(true);

        boolean result = orderFacade.updateOrderStatus(1, "shipped");

        assertTrue(result);
        verify(orderDAO, times(1)).updateOrderStatus(1, "shipped");
    }

    @Test
    void updateOrderStatus_WithInvalidOrderId_ShouldReturnFalse() {
        boolean result = orderFacade.updateOrderStatus(0, "shipped");

        assertFalse(result);
        verify(orderDAO, never()).updateOrderStatus(anyInt(), anyString());
    }

    @Test
    void updateOrderStatus_WithNullStatus_ShouldReturnFalse() {
        boolean result = orderFacade.updateOrderStatus(1, null);

        assertFalse(result);
        verify(orderDAO, never()).updateOrderStatus(anyInt(), anyString());
    }

    @Test
    void updateOrderStatus_WithEmptyStatus_ShouldReturnFalse() {
        boolean result = orderFacade.updateOrderStatus(1, "");

        assertFalse(result);
        verify(orderDAO, never()).updateOrderStatus(anyInt(), anyString());
    }

    @Test
    void updateOrderStatus_WithInvalidStatus_ShouldReturnFalse() {
        boolean result = orderFacade.updateOrderStatus(1, "invalid_status");

        assertFalse(result);
        verify(orderDAO, never()).updateOrderStatus(anyInt(), anyString());
    }

    @Test
    void getOrderItems_WithValidOrderId_ShouldReturnOrderItems() {
        List<OrderItem> expectedItems = Arrays.asList(testOrderItem);
        when(orderDAO.getOrderItems(1)).thenReturn(expectedItems);

        List<OrderItem> result = orderFacade.getOrderItems(1);

        assertEquals(expectedItems, result);
        verify(orderDAO, times(1)).getOrderItems(1);
    }

    @Test
    void getOrderItems_WithInvalidOrderId_ShouldReturnEmptyList() {
        List<OrderItem> result = orderFacade.getOrderItems(0);

        assertTrue(result.isEmpty());
        verify(orderDAO, never()).getOrderItems(anyInt());
    }
}