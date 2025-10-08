package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.CategoryDAO;
import webshop.lab.se.javawebshop.db.ProductDAO;
import webshop.lab.se.javawebshop.ui.ProductInfo;
import webshop.lab.se.javawebshop.ui.CategoryInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemFacade {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ItemFacade() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    // ========== PRODUCT METHODS (returnerar ProductInfo) ==========

    public List<ProductInfo> getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        return convertToProductInfoList(products);
    }

    public List<ProductInfo> getProductsByCategory(int categoryId) {
        List<Product> products = productDAO.getProductsByCategory(categoryId);
        return convertToProductInfoList(products);
    }

    public ProductInfo getProductById(int productId) {
        Product product = productDAO.getProductById(productId);
        return product != null ? convertToProductInfo(product) : null;
    }

    public boolean createProduct(int categoryId, String name, BigDecimal price, int stock) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Ogiltig produkt - namn saknas");
            return false;
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("Priset kan inte vara negativt");
            return false;
        }

        if (stock < 0) {
            System.err.println("Lagersaldo kan inte vara negativt");
            return false;
        }

        Product product = new Product(categoryId, name.trim(), price, stock);
        return productDAO.createProduct(product);
    }

    public boolean updateProduct(int productId, int categoryId, String name, BigDecimal price, int stock) {
        if (productId <= 0) {
            System.err.println("Ogiltig produkt - ID saknas");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.err.println("Produktnamn saknas");
            return false;
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("Priset kan inte vara negativt");
            return false;
        }

        if (stock < 0) {
            System.err.println("Lagersaldo kan inte vara negativt");
            return false;
        }

        Product product = new Product(productId, categoryId, name.trim(), price, stock);
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

    // ========== CATEGORY METHODS (returnerar CategoryInfo) ==========

    public List<CategoryInfo> getAllCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        return convertToCategoryInfoList(categories);
    }

    public CategoryInfo getCategoryById(int categoryId) {
        Category category = categoryDAO.getCategoryById(categoryId);
        return category != null ? convertToCategoryInfo(category) : null;
    }

    public boolean createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Kategorinamn måste anges");
            return false;
        }

        Category category = new Category(name.trim());
        return categoryDAO.createCategory(category);
    }

    public boolean updateCategory(int categoryId, String name) {
        if (categoryId <= 0) {
            System.err.println("Ogiltig kategori - ID saknas");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.err.println("Kategorinamn måste anges");
            return false;
        }

        Category category = new Category(categoryId, name.trim());
        return categoryDAO.updateCategory(category);
    }

    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            System.err.println("Ogiltigt kategori-ID");
            return false;
        }

        return categoryDAO.deleteCategory(categoryId);
    }

    // ========== KONVERTERINGSMETODER (privata) ==========

    private ProductInfo convertToProductInfo(Product product) {
        return new ProductInfo(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategoryName()
        );
    }

    private List<ProductInfo> convertToProductInfoList(List<Product> products) {
        List<ProductInfo> productInfoList = new ArrayList<>();
        for (Product product : products) {
            productInfoList.add(convertToProductInfo(product));
        }
        return productInfoList;
    }

    private CategoryInfo convertToCategoryInfo(Category category) {
        return new CategoryInfo(
                category.getCategoryId(),
                category.getName()
        );
    }

    private List<CategoryInfo> convertToCategoryInfoList(List<Category> categories) {
        List<CategoryInfo> categoryInfoList = new ArrayList<>();
        for (Category category : categories) {
            categoryInfoList.add(convertToCategoryInfo(category));
        }
        return categoryInfoList;
    }
}