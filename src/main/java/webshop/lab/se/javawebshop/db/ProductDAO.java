package webshop.lab.se.javawebshop.db;

import webshop.lab.se.javawebshop.bo.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private DBManager dbManager;

    public ProductDAO() {
        this.dbManager = DBManager.getInstance();
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT p.product_id, p.category_id, p.name, p.price, p.stock, " +
                "c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "ORDER BY c.name, p.name";

        List<Product> products = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av produkter: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return products;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        String sql = "SELECT p.product_id, p.category_id, p.name, p.price, p.stock, " +
                "c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "WHERE p.category_id = ? " +
                "ORDER BY p.name";

        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av produkter för kategori: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return products;
    }

    public Product getProductById(int productId) {
        String sql = "SELECT p.product_id, p.category_id, p.name, p.price, p.stock, " +
                "c.name AS category_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "WHERE p.product_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av produkt: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return null;
    }

    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (category_id, name, price, stock) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, product.getCategoryId());
            pstmt.setString(2, product.getName());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStock());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setProductId(generatedKeys.getInt(1));
                }
                System.out.println("Produkt skapad: " + product.getName());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid skapande av produkt: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET category_id = ?, name = ?, price = ?, stock = ? " +
                "WHERE product_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, product.getCategoryId());
            pstmt.setString(2, product.getName());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setInt(5, product.getProductId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produkt uppdaterad: " + product.getName());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid uppdatering av produkt: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produkt borttagen med ID: " + productId);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid borttagning av produkt: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public boolean updateStock(int productId, int quantityChange) {
        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Lagersaldo uppdaterat för produkt ID " + productId +
                        ": " + (quantityChange > 0 ? "+" : "") + quantityChange);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid uppdatering av lagersaldo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public boolean checkStock(int productId, int quantity) {
        Product product = getProductById(productId);
        return product != null && product.hasStock(quantity);
    }

    public boolean updateStock(Connection conn, int productId, int quantityChange) throws SQLException {
        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Lagersaldo uppdaterat för produkt ID " + productId +
                        ": " + (quantityChange > 0 ? "+" : "") + quantityChange);
                return true;
            }

            return false;

        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public boolean checkStock(Connection conn, int productId, int quantity) throws SQLException {
        String sql = "SELECT stock FROM products WHERE product_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                return stock >= quantity;
            }

            return false;

        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));

        product.setCategoryName(rs.getString("category_name"));

        return product;
    }

    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (conn != null) {
            dbManager.closeConnection(conn);
        }
    }
}