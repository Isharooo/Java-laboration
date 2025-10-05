package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.CategoryDAO;
import webshop.lab.se.javawebshop.db.ProductDAO;

import java.util.List;

/**
 * Facade för produkter och kategorier (BO-lager)
 * Hanterar all affärslogik för items
 */
public class ItemFacade {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ItemFacade() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    // ==================== PRODUCT METHODS ====================

    /**
     * Hämtar alla produkter
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Hämtar produkter för en specifik kategori
     */
    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    /**
     * Hämtar en specifik produkt
     */
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    /**
     * Skapar en ny produkt (Admin)
     */
    public boolean createProduct(Product product) {
        // Här kan vi lägga till validering
        if (product == null || product.getName() == null || product.getName().trim().isEmpty()) {
            System.err.println("Ogiltig produkt - namn saknas");
            return false;
        }

        if (product.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            System.err.println("Priset kan inte vara negativt");
            return false;
        }

        return productDAO.createProduct(product);
    }

    /**
     * Uppdaterar en produkt (Admin)
     */
    public boolean updateProduct(Product product) {
        // Validering
        if (product == null || product.getProductId() <= 0) {
            System.err.println("Ogiltig produkt - ID saknas");
            return false;
        }

        return productDAO.updateProduct(product);
    }

    /**
     * Tar bort en produkt (Admin)
     */
    public boolean deleteProduct(int productId) {
        if (productId <= 0) {
            System.err.println("Ogiltigt produkt-ID");
            return false;
        }

        return productDAO.deleteProduct(productId);
    }

    /**
     * Kontrollerar om produkt finns i lager
     */
    public boolean checkStock(int productId, int quantity) {
        return productDAO.checkStock(productId, quantity);
    }

    // ==================== CATEGORY METHODS ====================

    /**
     * Hämtar alla kategorier
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    /**
     * Hämtar en specifik kategori
     */
    public Category getCategoryById(int categoryId) {
        return categoryDAO.getCategoryById(categoryId);
    }

    /**
     * Skapar en ny kategori (Admin)
     */
    public boolean createCategory(Category category) {
        // Validering
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            System.err.println("Kategorinamn måste anges");
            return false;
        }

        return categoryDAO.createCategory(category);
    }

    /**
     * Uppdaterar en kategori (Admin)
     */
    public boolean updateCategory(Category category) {
        if (category == null || category.getCategoryId() <= 0) {
            System.err.println("Ogiltig kategori - ID saknas");
            return false;
        }

        return categoryDAO.updateCategory(category);
    }

    /**
     * Tar bort en kategori (Admin)
     */
    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            System.err.println("Ogiltigt kategori-ID");
            return false;
        }

        return categoryDAO.deleteCategory(categoryId);
    }
}