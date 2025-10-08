package webshop.lab.se.javawebshop.ui;

public class CategoryInfo {
    private int categoryId;
    private String name;

    public CategoryInfo(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // Getters
    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}