package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.UserDAO;

import java.util.List;

/**
 * Facade för användarhantering (BO-lager)
 * Hanterar inloggning och användaradministration
 */
public class UserFacade {

    private UserDAO userDAO;

    public UserFacade() {
        this.userDAO = new UserDAO();
    }

    /**
     * Loggar in en användare
     */
    public User login(String username, String password) {
        // Validering
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn saknas");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("Lösenord saknas");
            return null;
        }

        // Autentisera via DAO
        User user = userDAO.authenticateUser(username.trim(), password);

        if (user != null) {
            System.out.println("Användare inloggad via facade: " + user.getUsername());
        } else {
            System.out.println("Inloggning misslyckades via facade");
        }

        return user;
    }

    /**
     * Hämtar alla användare (Admin)
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * Hämtar en specifik användare
     */
    public User getUserById(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return null;
        }

        return userDAO.findById(userId);
    }

    /**
     * Hittar användare baserat på användarnamn
     */
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn saknas");
            return null;
        }

        return userDAO.findByUsername(username.trim());
    }

    /**
     * Skapar en ny användare (Admin)
     */
    public boolean createUser(User user) {
        // Validering
        if (user == null) {
            System.err.println("User-objekt saknas");
            return false;
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            System.err.println("Användarnamn måste anges");
            return false;
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            System.err.println("Lösenord måste anges");
            return false;
        }

        // Kontrollera om användarnamnet redan finns
        User existing = userDAO.findByUsername(user.getUsername());
        if (existing != null) {
            System.err.println("Användarnamnet finns redan");
            return false;
        }

        return userDAO.createUser(user);
    }

    /**
     * Uppdaterar en användare (Admin)
     */
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() <= 0) {
            System.err.println("Ogiltig användare - ID saknas");
            return false;
        }

        return userDAO.updateUser(user);
    }

    /**
     * Tar bort en användare (Admin)
     */
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return false;
        }

        return userDAO.deleteUser(userId);
    }
}