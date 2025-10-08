package webshop.lab.se.javawebshop.ui;

public class UserInfo {
    private int userId;
    private String username;
    private String role;

    public UserInfo(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }

    public boolean isWarehouse() {
        return "warehouse".equals(role);
    }

    public boolean isCustomer() {
        return "customer".equals(role);
    }
}