package webshop.lab.se.javawebshop.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.bo.Product;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDAOTest {

    @Mock
    private DBManager dbManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private ProductDAO productDAO;
    private Product testProduct;

    @BeforeEach
    void setUp() throws Exception {
        productDAO = new ProductDAO();

        Field dbManagerField = ProductDAO.class.getDeclaredField("dbManager");
        dbManagerField.setAccessible(true);
        dbManagerField.set(productDAO, dbManager);

        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setCategoryId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStock(10);

        when(dbManager.getConnection()).thenReturn(connection);
    }

    @Test
    void getAllProducts_Success() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("product_id")).thenReturn(1);
        when(resultSet.getInt("category_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getBigDecimal("price")).thenReturn(new BigDecimal("99.99"));
        when(resultSet.getInt("stock")).thenReturn(10);
        when(resultSet.getString("category_name")).thenReturn("Test Category");

        List<Product> products = productDAO.getAllProducts();

        assertEquals(1, products.size());
        assertEquals(1, products.get(0).getProductId());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    void getAllProducts_Empty() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Product> products = productDAO.getAllProducts();

        assertTrue(products.isEmpty());
    }

    @Test
    void getProductsByCategory_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("product_id")).thenReturn(1);
        when(resultSet.getInt("category_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getBigDecimal("price")).thenReturn(new BigDecimal("99.99"));
        when(resultSet.getInt("stock")).thenReturn(10);
        when(resultSet.getString("category_name")).thenReturn("Test Category");

        List<Product> products = productDAO.getProductsByCategory(1);

        assertEquals(1, products.size());
        assertEquals(1, products.get(0).getCategoryId());
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void getProductById_Found() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("product_id")).thenReturn(1);
        when(resultSet.getInt("category_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getBigDecimal("price")).thenReturn(new BigDecimal("99.99"));
        when(resultSet.getInt("stock")).thenReturn(10);
        when(resultSet.getString("category_name")).thenReturn("Test Category");

        Product product = productDAO.getProductById(1);

        assertNotNull(product);
        assertEquals(1, product.getProductId());
        assertEquals("Test Product", product.getName());
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void getProductById_NotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Product product = productDAO.getProductById(1);

        assertNull(product);
    }

    @Test
    void createProduct_Success() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);

        boolean result = productDAO.createProduct(testProduct);

        assertTrue(result);
        assertEquals(123, testProduct.getProductId());
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).setString(2, "Test Product");
        verify(preparedStatement, times(1)).setBigDecimal(3, new BigDecimal("99.99"));
        verify(preparedStatement, times(1)).setInt(4, 10);
    }

    @Test
    void createProduct_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = productDAO.createProduct(testProduct);

        assertFalse(result);
    }

    @Test
    void updateProduct_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = productDAO.updateProduct(testProduct);

        assertTrue(result);
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).setString(2, "Test Product");
        verify(preparedStatement, times(1)).setBigDecimal(3, new BigDecimal("99.99"));
        verify(preparedStatement, times(1)).setInt(4, 10);
        verify(preparedStatement, times(1)).setInt(5, 1);
    }

    @Test
    void updateProduct_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = productDAO.updateProduct(testProduct);

        assertFalse(result);
    }

    @Test
    void deleteProduct_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = productDAO.deleteProduct(1);

        assertTrue(result);
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void deleteProduct_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = productDAO.deleteProduct(1);

        assertFalse(result);
    }

    @Test
    void updateStock_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = productDAO.updateStock(1, -2);

        assertTrue(result);
        verify(preparedStatement, times(1)).setInt(1, -2);
        verify(preparedStatement, times(1)).setInt(2, 1);
    }

    @Test
    void checkStock_WithProductObject() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("product_id")).thenReturn(1);
        when(resultSet.getInt("category_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getBigDecimal("price")).thenReturn(new BigDecimal("99.99"));
        when(resultSet.getInt("stock")).thenReturn(10);
        when(resultSet.getString("category_name")).thenReturn("Test Category");

        boolean result = productDAO.checkStock(1, 5);

        assertTrue(result);
    }
}