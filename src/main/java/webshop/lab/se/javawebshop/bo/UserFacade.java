package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.UserDAO;

import java.util.List;

public class UserFacade {

    private UserDAO userDAO;

    public UserFacade() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn saknas");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("Lösenord saknas");
            return null;
        }

        User user = userDAO.authenticateUser(username.trim(), password);

        if (user != null) {
            System.out.println("Användare inloggad via facade: " + user.getUsername());
        } else {
            System.out.println("Inloggning misslyckades via facade");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return null;
        }

        return userDAO.findById(userId);
    }

    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn saknas");
            return null;
        }

        return userDAO.findByUsername(username.trim());
    }

    public boolean createUser(User user) {
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

        User existing = userDAO.findByUsername(user.getUsername());
        if (existing != null) {
            System.err.println("Användarnamnet finns redan");
            return false;
        }

        return userDAO.createUser(user);
    }

    public boolean updateUser(User user) {
        if (user == null || user.getUserId() <= 0) {
            System.err.println("Ogiltig användare - ID saknas");
            return false;
        }

        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return false;
        }

        return userDAO.deleteUser(userId);
    }
}