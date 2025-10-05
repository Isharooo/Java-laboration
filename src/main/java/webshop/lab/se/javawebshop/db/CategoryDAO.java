package webshop.lab.se.javawebshop.db;

import webshop.lab.se.javawebshop.bo.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object för kategorier
 */
public class CategoryDAO {

    private DBManager dbManager;

    public CategoryDAO() {
        this.dbManager = DBManager.getInstance();
    }

    /**
     * Hämtar alla kategorier
     *
     * @return Lista med alla kategorier
     */
    public List<Category> getAllCategories() {
        String sql = "SELECT category_id, name FROM categories ORDER BY name";
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av kategorier: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }

        return categories;
    }

    /**
     * Hämtar en kategori baserat på ID
     *
     * @param categoryId Kategori-ID
     * @return Category-objekt eller null
     */
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT category_id, name FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Fel vid hämtning av kategori: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Skapar en ny kategori
     * Används för admin-funktionalitet (betyg 5)
     *
     * @param category Category-objekt att skapa
     * @return true om kategorin skapades
     */
    public boolean createCategory(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, category.getName());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    category.setCategoryId(generatedKeys.getInt(1));
                }
                System.out.println("Kategori skapad: " + category.getName());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid skapande av kategori: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Uppdaterar en befintlig kategori
     * Används för admin-funktionalitet (betyg 5)
     *
     * @param category Category-objekt med uppdaterad information
     * @return true om kategorin uppdaterades
     */
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getCategoryId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Kategori uppdaterad: " + category.getName());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid uppdatering av kategori: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Tar bort en kategori
     * Används för admin-funktionalitet (betyg 5)
     * OBS: Misslyckas om det finns produkter i kategorin (foreign key constraint)
     *
     * @param categoryId ID för kategorin att ta bort
     * @return true om kategorin togs bort
     */
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Kategori borttagen med ID: " + categoryId);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Fel vid borttagning av kategori: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Hjälpmetod för att extrahera Category från ResultSet
     */
    private Category extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        return category;
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