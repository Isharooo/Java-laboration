package webshop.lab.se.javawebshop.bo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Business Object för produkter
 * Innehåller lagersaldo (stock) för betyg 4
 */
public class Product {
    private int productId;
    private int categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private LocalDateTime createdAt;

    // Extra fält för att hålla kategorinamn (från JOIN)
    private String categoryName;

    // Konstruktorer
    public Product() {
    }

    public Product(int productId, int categoryId, String name, String description,
                   BigDecimal price, int stock, String imageUrl) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Product(int categoryId, String name, String description,
                   BigDecimal price, int stock, String imageUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Getters och Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Hjälpmetod för att kontrollera om produkt finns i lager (betyg 4)
    public boolean isInStock() {
        return stock > 0;
    }

    public boolean hasStock(int quantity) {
        return stock >= quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}