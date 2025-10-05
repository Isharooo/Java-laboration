package webshop.lab.se.javawebshop.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = new Cart();

        product1 = new Product();
        product1.setProductId(1);
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal("100.00"));

        product2 = new Product();
        product2.setProductId(2);
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal("50.00"));
    }

    @Test
    void testCartStartsEmpty() {
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    void testAddProduct_NewProduct() {
        cart.addProduct(product1, 2);

        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getTotalQuantity());
        assertEquals(new BigDecimal("200.00"), cart.getTotalPrice());
    }

    @Test
    void testAddProduct_SameProductIncreasesQuantity() {
        cart.addProduct(product1, 1);
        cart.addProduct(product1, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getTotalQuantity());
        assertEquals(new BigDecimal("300.00"), cart.getTotalPrice());
    }

    @Test
    void testAddMultipleDifferentProducts() {
        cart.addProduct(product1, 1);
        cart.addProduct(product2, 3);

        assertEquals(2, cart.getItems().size());
        assertEquals(4, cart.getTotalQuantity());
        assertEquals(new BigDecimal("250.00"), cart.getTotalPrice());
    }

    @Test
    void testRemoveProduct() {
        cart.addProduct(product1, 2);
        cart.addProduct(product2, 1);

        cart.removeProduct(product1.getProductId());

        assertEquals(1, cart.getItems().size());
        assertFalse(cart.getItems().containsKey(product1.getProductId()));
    }

    @Test
    void testUpdateQuantity_IncreaseAndDecrease() {
        cart.addProduct(product1, 2);
        cart.updateQuantity(product1.getProductId(), 5);

        assertEquals(5, cart.getItems().get(product1.getProductId()).getQuantity());
        assertEquals(new BigDecimal("500.00"), cart.getTotalPrice());

        cart.updateQuantity(product1.getProductId(), 1);
        assertEquals(1, cart.getItems().get(product1.getProductId()).getQuantity());
    }

    @Test
    void testUpdateQuantity_RemoveWhenZero() {
        cart.addProduct(product1, 2);
        cart.updateQuantity(product1.getProductId(), 0);

        assertTrue(cart.isEmpty());
    }

    @Test
    void testClearCart() {
        cart.addProduct(product1, 2);
        cart.addProduct(product2, 1);
        cart.clear();

        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalQuantity());
    }

    @Test
    void testCartItemSubtotal() {
        Cart.CartItem item = new Cart.CartItem(product1, 3);
        assertEquals(new BigDecimal("300.00"), item.getSubtotal());
    }
}