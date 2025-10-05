package webshop.lab.se.javawebshop.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.bo.Category;

import java.lang.reflect.Field;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryDAOTest {

    @Mock
    private DBManager dbManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private CategoryDAO categoryDAO;

    @BeforeEach
    void setUp() throws Exception {
        categoryDAO = new CategoryDAO();

        Field dbManagerField = CategoryDAO.class.getDeclaredField("dbManager");
        dbManagerField.setAccessible(true);
        dbManagerField.set(categoryDAO, dbManager);

        when(dbManager.getConnection()).thenReturn(connection);
    }

    @Test
    void getAllCategories_Success() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("category_id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("Category1", "Category2");

        List<Category> categories = categoryDAO.getAllCategories();

        assertEquals(2, categories.size());
        assertEquals(1, categories.get(0).getCategoryId());
        assertEquals("Category1", categories.get(0).getName());
        verify(statement, times(1)).executeQuery(anyString());
    }

    @Test
    void getAllCategories_SQLException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("DB error"));

        List<Category> categories = categoryDAO.getAllCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    void getCategoryById_Found() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("category_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Category");

        Category category = categoryDAO.getCategoryById(1);

        assertNotNull(category);
        assertEquals(1, category.getCategoryId());
        assertEquals("Test Category", category.getName());
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void getCategoryById_NotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Category category = categoryDAO.getCategoryById(1);

        assertNull(category);
    }

    @Test
    void getCategoryById_SQLException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        Category category = categoryDAO.getCategoryById(1);

        assertNull(category);
    }

    @Test
    void createCategory_Success() throws SQLException {
        Category category = new Category();
        category.setName("New Category");

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);

        boolean result = categoryDAO.createCategory(category);

        assertTrue(result);
        assertEquals(123, category.getCategoryId());
        verify(preparedStatement, times(1)).setString(1, "New Category");
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void createCategory_NoRowsAffected() throws SQLException {
        Category category = new Category();
        category.setName("New Category");

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = categoryDAO.createCategory(category);

        assertFalse(result);
    }

    @Test
    void createCategory_SQLException() throws SQLException {
        Category category = new Category();
        category.setName("New Category");

        when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("DB error"));

        boolean result = categoryDAO.createCategory(category);

        assertFalse(result);
    }

    @Test
    void updateCategory_Success() throws SQLException {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Updated Category");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = categoryDAO.updateCategory(category);

        assertTrue(result);
        verify(preparedStatement, times(1)).setString(1, "Updated Category");
        verify(preparedStatement, times(1)).setInt(2, 1);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void updateCategory_NoRowsAffected() throws SQLException {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Updated Category");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = categoryDAO.updateCategory(category);

        assertFalse(result);
    }

    @Test
    void updateCategory_SQLException() throws SQLException {
        Category category = new Category();
        category.setCategoryId(1);
        category.setName("Updated Category");

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        boolean result = categoryDAO.updateCategory(category);

        assertFalse(result);
    }

    @Test
    void deleteCategory_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = categoryDAO.deleteCategory(1);

        assertTrue(result);
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void deleteCategory_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = categoryDAO.deleteCategory(1);

        assertFalse(result);
    }

    @Test
    void deleteCategory_SQLException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        boolean result = categoryDAO.deleteCategory(1);

        assertFalse(result);
    }
}