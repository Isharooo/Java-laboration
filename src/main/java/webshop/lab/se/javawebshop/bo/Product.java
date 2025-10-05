package webshop.lab.se.javawebshop.bo;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private int categoryId;
    private String name;
    private BigDecimal price;
    private int stock;

    private String categoryName;

    public Product() {
    }

    public Product(int productId, int categoryId, String name, BigDecimal price, int stock) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product(int categoryId, String name, BigDecimal price, int stock) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

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