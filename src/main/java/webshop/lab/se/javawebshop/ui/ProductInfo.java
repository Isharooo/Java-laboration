package webshop.lab.se.javawebshop.ui;

import java.math.BigDecimal;

public class ProductInfo {
    private int productId;
    private String name;
    private BigDecimal price;
    private int stock;
    private String categoryName;

    public ProductInfo(int productId, String name, BigDecimal price, int stock, String categoryName) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getCategoryName() {
        return categoryName;
    }
}