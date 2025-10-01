package webshop.lab.se.javawebshop.db;

import webshop.lab.se.javawebshop.bo.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object för produkter
 * Hanterar lagersaldo (betyg 4)
 */
public class ProductDAO {

    private DBManager dbManager;

    public ProductDAO() {
        this.dbManager = DBManager.getInstance();
    }

    /**
     * Hämtar alla produkter med kategorinamn (JOIN)
     *
     * @return Lista med alla produkter
     */
    public List<Product> getAllProducts() {
        String sql = "SELECT p.product_id, p.category_id, p.name, p.description, " +
                "p.price, p.stock, p.image_url, p.created_at, c.name AS category_name " +
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

    /**
     * Hämtar produkter baserat på kategori
     *
     * @param categoryId Kategori-ID
     * @return Lista med produkter i kategorin
     */
    public List<Product> getProductsByCategory(int categoryId) {
        String sql = "SELECT p.product_id, p.category_id, p.name, p.description, " +
                "p.price, p.stock, p.image_url, p.created_at, c.name AS category_name " +
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

    /**
     * Hämtar en produkt baserat på ID
     *
     * @param productId Produkt-ID
     * @return Product-objekt eller null
     */
    public Product getProductById(int productId) {
        String sql = "SELECT p.product_id, p.category_id, p.name, p.description, " +
                "p.price, p.stock, p.image_url, p.created_at, c.name AS category_name " +
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

    /**
     * Skapar en ny produkt
     * Används för admin-funktionalitet (betyg 5)
     *
     * @param product Product-objekt att skapa
     * @return true om produkten skapades
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (category_id, name, description, price, stock, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, product.getCategoryId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setString(6, product.getImageUrl());

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

    /**
     * Uppdaterar en befintlig produkt
     * Används för admin-funktionalitet (betyg 5)
     *
     * @param product Product-objekt med uppdaterad information
     * @return true om produkten uppdaterades
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET category_id = ?, name = ?, description = ?, " +
                "price = ?, stock = ?, image_url = ? WHERE product_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, product.getCategoryId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setString(6, product.getImageUrl());
            pstmt.setInt(7, product.getProductId());

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

    /**
     * Tar bort en produkt
     * Används för admin-funktionalitet (betyg 5)
     *
     * @param productId ID för produkten att ta bort
     * @return true om produkten togs bort
     */
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

    /**
     * Uppdaterar lagersaldo för en produkt
     * VIKTIGT för betyg 4 - används vid orderläggning
     *
     * @param productId ID för produkten
     * @param quantityChange Förändring i lagersaldo (negativt för minskning)
     * @return true om lagersaldot uppdaterades
     */
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

    /**
     * Kontrollerar om tillräckligt lagersaldo finns (används vid orderläggning)
     * VIKTIGT för betyg 4
     *
     * @param productId Produkt-ID
     * @param quantity Önskad kvantitet
     * @return true om tillräckligt lager finns
     */
    public boolean checkStock(int productId, int quantity) {
        Product product = getProductById(productId);
        return product != null && product.hasStock(quantity);
    }

    /**
     * Hjälpmetod för att extrahera Product från ResultSet
     */
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            product.setCreatedAt(timestamp.toLocalDateTime());
        }

        // Kategorinamn från JOIN
        product.setCategoryName(rs.getString("category_name"));

        return product;
    }

    /**
     * Hjälpmetod för att stänga resurser
     */
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