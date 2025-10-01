package webshop.lab.se.javawebshop.bo;

import java.time.LocalDateTime;

/**
 * Business Object för användare
 * Representerar en rad i users-tabellen
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String role; // "customer", "admin", "warehouse"
    private LocalDateTime createdAt;

    // Konstruktor - tom
    public User() {
    }

    // Konstruktor - med parametrar
    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Konstruktor - utan userId (för nya användare)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters och Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Hjälpmetoder för rollkontroll
    public boolean isAdmin() {
        return "admin".equals(role);
    }

    public boolean isWarehouse() {
        return "warehouse".equals(role);
    }

    public boolean isCustomer() {
        return "customer".equals(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}