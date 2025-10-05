package webshop.lab.se.javawebshop.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.bo.User;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOTest {

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

    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        userDAO = new UserDAO();

        Field dbManagerField = UserDAO.class.getDeclaredField("dbManager");
        dbManagerField.setAccessible(true);
        dbManagerField.set(userDAO, dbManager);

        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole("customer");

        when(dbManager.getConnection()).thenReturn(connection);
    }

    @Test
    void findByUsername_Found() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("role")).thenReturn("customer");

        User result = userDAO.findByUsername("testuser");

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("testuser", result.getUsername());
        verify(preparedStatement, times(1)).setString(1, "testuser");
    }

    @Test
    void findByUsername_NotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User result = userDAO.findByUsername("testuser");

        assertNull(result);
    }

    @Test
    void findById_Found() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("role")).thenReturn("customer");

        User result = userDAO.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("testuser", result.getUsername());
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void findById_NotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User result = userDAO.findById(1);

        assertNull(result);
    }

    @Test
    void getAllUsers_Success() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("role")).thenReturn("customer");

        List<User> users = userDAO.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getUserId());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    void getAllUsers_Empty() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<User> users = userDAO.getAllUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    void createUser_Success() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);

        boolean result = userDAO.createUser(testUser);

        assertTrue(result);
        assertEquals(123, testUser.getUserId());
        verify(preparedStatement, times(1)).setString(1, "testuser");
        verify(preparedStatement, times(1)).setString(2, "password");
        verify(preparedStatement, times(1)).setString(3, "customer");
    }

    @Test
    void createUser_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = userDAO.createUser(testUser);

        assertFalse(result);
    }

    @Test
    void updateUser_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userDAO.updateUser(testUser);

        assertTrue(result);
        verify(preparedStatement, times(1)).setString(1, "testuser");
        verify(preparedStatement, times(1)).setString(2, "password");
        verify(preparedStatement, times(1)).setString(3, "customer");
        verify(preparedStatement, times(1)).setInt(4, 1);
    }

    @Test
    void updateUser_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = userDAO.updateUser(testUser);

        assertFalse(result);
    }

    @Test
    void deleteUser_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userDAO.deleteUser(1);

        assertTrue(result);
        verify(preparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void deleteUser_NoRowsAffected() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = userDAO.deleteUser(1);

        assertFalse(result);
    }

    @Test
    void deleteUser_SQLException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        assertThrows(RuntimeException.class, () -> userDAO.deleteUser(1));
    }

    @Test
    void authenticateUser_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("role")).thenReturn("customer");

        User result = userDAO.authenticateUser("testuser", "password");

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void authenticateUser_WrongPassword() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("correctpassword");
        when(resultSet.getString("role")).thenReturn("customer");

        User result = userDAO.authenticateUser("testuser", "wrongpassword");

        assertNull(result);
    }

    @Test
    void authenticateUser_UserNotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User result = userDAO.authenticateUser("testuser", "password");

        assertNull(result);
    }
}