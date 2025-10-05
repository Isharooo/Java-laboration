package webshop.lab.se.javawebshop.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.db.CategoryDAO;
import webshop.lab.se.javawebshop.db.ProductDAO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemFacadeTest {

    @Mock
    private ProductDAO productDAO;

    @Mock
    private CategoryDAO categoryDAO;

    @InjectMocks
    private ItemFacade itemFacade;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));

        testCategory = new Category();
        testCategory.setCategoryId(1);
        testCategory.setName("Test Category");
    }

    @Test
    void getAllProducts_ShouldReturnProductList() {
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productDAO.getAllProducts()).thenReturn(expectedProducts);

        List<Product> result = itemFacade.getAllProducts();

        assertEquals(expectedProducts, result);
        verify(productDAO, times(1)).getAllProducts();
    }

    @Test
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        int categoryId = 1;
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productDAO.getProductsByCategory(categoryId)).thenReturn(expectedProducts);

        List<Product> result = itemFacade.getProductsByCategory(categoryId);

        assertEquals(expectedProducts, result);
        verify(productDAO, times(1)).getProductsByCategory(categoryId);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        int productId = 1;
        when(productDAO.getProductById(productId)).thenReturn(testProduct);

        Product result = itemFacade.getProductById(productId);

        assertEquals(testProduct, result);
        verify(productDAO, times(1)).getProductById(productId);
    }

    @Test
    void createProduct_WithValidProduct_ShouldReturnTrue() {
        when(productDAO.createProduct(testProduct)).thenReturn(true);

        boolean result = itemFacade.createProduct(testProduct);

        assertTrue(result);
        verify(productDAO, times(1)).createProduct(testProduct);
    }

    @Test
    void createProduct_WithNullProduct_ShouldReturnFalse() {
        boolean result = itemFacade.createProduct(null);

        assertFalse(result);
        verify(productDAO, never()).createProduct(any());
    }

    @Test
    void createProduct_WithEmptyName_ShouldReturnFalse() {
        testProduct.setName("");

        boolean result = itemFacade.createProduct(testProduct);

        assertFalse(result);
        verify(productDAO, never()).createProduct(any());
    }

    @Test
    void createProduct_WithNegativePrice_ShouldReturnFalse() {
        testProduct.setPrice(new BigDecimal("-10.0"));

        boolean result = itemFacade.createProduct(testProduct);

        assertFalse(result);
        verify(productDAO, never()).createProduct(any());
    }

    @Test
    void updateProduct_WithValidProduct_ShouldReturnTrue() {
        when(productDAO.updateProduct(testProduct)).thenReturn(true);

        boolean result = itemFacade.updateProduct(testProduct);

        assertTrue(result);
        verify(productDAO, times(1)).updateProduct(testProduct);
    }

    @Test
    void updateProduct_WithInvalidId_ShouldReturnFalse() {
        testProduct.setProductId(0);

        boolean result = itemFacade.updateProduct(testProduct);

        assertFalse(result);
        verify(productDAO, never()).updateProduct(any());
    }

    @Test
    void deleteProduct_WithValidId_ShouldReturnTrue() {
        int productId = 1;
        when(productDAO.deleteProduct(productId)).thenReturn(true);

        boolean result = itemFacade.deleteProduct(productId);

        assertTrue(result);
        verify(productDAO, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteProduct_WithInvalidId_ShouldReturnFalse() {
        boolean result = itemFacade.deleteProduct(0);

        assertFalse(result);
        verify(productDAO, never()).deleteProduct(anyInt());
    }

    @Test
    void checkStock_ShouldReturnResultFromDAO() {
        int productId = 1;
        int quantity = 5;
        when(productDAO.checkStock(productId, quantity)).thenReturn(true);

        boolean result = itemFacade.checkStock(productId, quantity);

        assertTrue(result);
        verify(productDAO, times(1)).checkStock(productId, quantity);
    }

    @Test
    void getAllCategories_ShouldReturnCategoryList() {
        List<Category> expectedCategories = Arrays.asList(testCategory);
        when(categoryDAO.getAllCategories()).thenReturn(expectedCategories);

        List<Category> result = itemFacade.getAllCategories();

        assertEquals(expectedCategories, result);
        verify(categoryDAO, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        int categoryId = 1;
        when(categoryDAO.getCategoryById(categoryId)).thenReturn(testCategory);

        Category result = itemFacade.getCategoryById(categoryId);

        assertEquals(testCategory, result);
        verify(categoryDAO, times(1)).getCategoryById(categoryId);
    }

    @Test
    void createCategory_WithValidCategory_ShouldReturnTrue() {
        when(categoryDAO.createCategory(testCategory)).thenReturn(true);

        boolean result = itemFacade.createCategory(testCategory);

        assertTrue(result);
        verify(categoryDAO, times(1)).createCategory(testCategory);
    }

    @Test
    void createCategory_WithEmptyName_ShouldReturnFalse() {
        testCategory.setName("");

        boolean result = itemFacade.createCategory(testCategory);

        assertFalse(result);
        verify(categoryDAO, never()).createCategory(any());
    }

    @Test
    void updateCategory_WithValidCategory_ShouldReturnTrue() {
        when(categoryDAO.updateCategory(testCategory)).thenReturn(true);

        boolean result = itemFacade.updateCategory(testCategory);

        assertTrue(result);
        verify(categoryDAO, times(1)).updateCategory(testCategory);
    }

    @Test
    void deleteCategory_WithValidId_ShouldReturnTrue() {
        int categoryId = 1;
        when(categoryDAO.deleteCategory(categoryId)).thenReturn(true);

        boolean result = itemFacade.deleteCategory(categoryId);

        assertTrue(result);
        verify(categoryDAO, times(1)).deleteCategory(categoryId);
    }
}