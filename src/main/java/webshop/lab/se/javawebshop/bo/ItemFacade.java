package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.CategoryDAO;
import webshop.lab.se.javawebshop.db.ProductDAO;

import java.util.List;

public class ItemFacade {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ItemFacade() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    public boolean createProduct(Product product) {
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

    public boolean updateProduct(Product product) {
        if (product == null || product.getProductId() <= 0) {
            System.err.println("Ogiltig produkt - ID saknas");
            return false;
        }

        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int productId) {
        if (productId <= 0) {
            System.err.println("Ogiltigt produkt-ID");
            return false;
        }

        return productDAO.deleteProduct(productId);
    }

    public boolean checkStock(int productId, int quantity) {
        return productDAO.checkStock(productId, quantity);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public Category getCategoryById(int categoryId) {
        return categoryDAO.getCategoryById(categoryId);
    }

    public boolean createCategory(Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            System.err.println("Kategorinamn måste anges");
            return false;
        }

        return categoryDAO.createCategory(category);
    }

    public boolean updateCategory(Category category) {
        if (category == null || category.getCategoryId() <= 0) {
            System.err.println("Ogiltig kategori - ID saknas");
            return false;
        }

        return categoryDAO.updateCategory(category);
    }

    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            System.err.println("Ogiltigt kategori-ID");
            return false;
        }

        return categoryDAO.deleteCategory(categoryId);
    }
}