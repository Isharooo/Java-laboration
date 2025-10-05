package webshop.lab.se.javawebshop.bo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webshop.lab.se.javawebshop.db.UserDAO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserFacade userFacade;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
    }

    @Test
    void login_WithValidCredentials_ShouldReturnUser() {
        when(userDAO.authenticateUser("testuser", "password")).thenReturn(testUser);

        User result = userFacade.login("testuser", "password");

        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userDAO, times(1)).authenticateUser("testuser", "password");
    }

    @Test
    void login_WithNullUsername_ShouldReturnNull() {
        User result = userFacade.login(null, "password");

        assertNull(result);
        verify(userDAO, never()).authenticateUser(any(), any());
    }

    @Test
    void login_WithEmptyUsername_ShouldReturnNull() {
        User result = userFacade.login("", "password");

        assertNull(result);
        verify(userDAO, never()).authenticateUser(any(), any());
    }

    @Test
    void login_WithNullPassword_ShouldReturnNull() {
        User result = userFacade.login("testuser", null);

        assertNull(result);
        verify(userDAO, never()).authenticateUser(any(), any());
    }

    @Test
    void login_WithEmptyPassword_ShouldReturnNull() {
        User result = userFacade.login("testuser", "");

        assertNull(result);
        verify(userDAO, never()).authenticateUser(any(), any());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnNull() {
        when(userDAO.authenticateUser("testuser", "wrongpass")).thenReturn(null);

        User result = userFacade.login("testuser", "wrongpass");

        assertNull(result);
        verify(userDAO, times(1)).authenticateUser("testuser", "wrongpass");
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        List<User> expectedUsers = Arrays.asList(testUser);
        when(userDAO.getAllUsers()).thenReturn(expectedUsers);

        List<User> result = userFacade.getAllUsers();

        assertEquals(expectedUsers, result);
        verify(userDAO, times(1)).getAllUsers();
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUser() {
        when(userDAO.findById(1)).thenReturn(testUser);

        User result = userFacade.getUserById(1);

        assertEquals(testUser, result);
        verify(userDAO, times(1)).findById(1);
    }

    @Test
    void getUserById_WithInvalidId_ShouldReturnNull() {
        User result = userFacade.getUserById(0);

        assertNull(result);
        verify(userDAO, never()).findById(anyInt());
    }

    @Test
    void getUserByUsername_WithValidUsername_ShouldReturnUser() {
        when(userDAO.findByUsername("testuser")).thenReturn(testUser);

        User result = userFacade.getUserByUsername("testuser");

        assertEquals(testUser, result);
        verify(userDAO, times(1)).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_WithNullUsername_ShouldReturnNull() {
        User result = userFacade.getUserByUsername(null);

        assertNull(result);
        verify(userDAO, never()).findByUsername(anyString());
    }

    @Test
    void getUserByUsername_WithEmptyUsername_ShouldReturnNull() {
        User result = userFacade.getUserByUsername("");

        assertNull(result);
        verify(userDAO, never()).findByUsername(anyString());
    }

    @Test
    void createUser_WithValidUser_ShouldReturnTrue() {
        when(userDAO.findByUsername("testuser")).thenReturn(null);
        when(userDAO.createUser(testUser)).thenReturn(true);

        boolean result = userFacade.createUser(testUser);

        assertTrue(result);
        verify(userDAO, times(1)).findByUsername("testuser");
        verify(userDAO, times(1)).createUser(testUser);
    }

    @Test
    void createUser_WithNullUser_ShouldReturnFalse() {
        boolean result = userFacade.createUser(null);

        assertFalse(result);
        verify(userDAO, never()).findByUsername(anyString());
        verify(userDAO, never()).createUser(any());
    }

    @Test
    void createUser_WithEmptyUsername_ShouldReturnFalse() {
        testUser.setUsername("");

        boolean result = userFacade.createUser(testUser);

        assertFalse(result);
        verify(userDAO, never()).findByUsername(anyString());
        verify(userDAO, never()).createUser(any());
    }

    @Test
    void createUser_WithEmptyPassword_ShouldReturnFalse() {
        testUser.setPassword("");

        boolean result = userFacade.createUser(testUser);

        assertFalse(result);
        verify(userDAO, never()).findByUsername(anyString());
        verify(userDAO, never()).createUser(any());
    }

    @Test
    void createUser_WithExistingUsername_ShouldReturnFalse() {
        when(userDAO.findByUsername("testuser")).thenReturn(testUser);

        boolean result = userFacade.createUser(testUser);

        assertFalse(result);
        verify(userDAO, times(1)).findByUsername("testuser");
        verify(userDAO, never()).createUser(any());
    }

    @Test
    void updateUser_WithValidUser_ShouldReturnTrue() {
        when(userDAO.updateUser(testUser)).thenReturn(true);

        boolean result = userFacade.updateUser(testUser);

        assertTrue(result);
        verify(userDAO, times(1)).updateUser(testUser);
    }

    @Test
    void updateUser_WithNullUser_ShouldReturnFalse() {
        boolean result = userFacade.updateUser(null);

        assertFalse(result);
        verify(userDAO, never()).updateUser(any());
    }

    @Test
    void updateUser_WithInvalidId_ShouldReturnFalse() {
        testUser.setUserId(0);

        boolean result = userFacade.updateUser(testUser);

        assertFalse(result);
        verify(userDAO, never()).updateUser(any());
    }

    @Test
    void deleteUser_WithValidId_ShouldReturnTrue() {
        when(userDAO.deleteUser(1)).thenReturn(true);

        boolean result = userFacade.deleteUser(1);

        assertTrue(result);
        verify(userDAO, times(1)).deleteUser(1);
    }

    @Test
    void deleteUser_WithInvalidId_ShouldReturnFalse() {
        boolean result = userFacade.deleteUser(0);

        assertFalse(result);
        verify(userDAO, never()).deleteUser(anyInt());
    }
}