package webshop.lab.se.javawebshop.bo;

import webshop.lab.se.javawebshop.db.UserDAO;
import webshop.lab.se.javawebshop.ui.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserFacade {

    private UserDAO userDAO;

    public UserFacade() {
        this.userDAO = new UserDAO();
    }

    public UserInfo login(String username, String password) {
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
            return convertToUserInfo(user);
        } else {
            System.out.println("Inloggning misslyckades via facade");
            return null;
        }
    }

    public List<UserInfo> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        return convertToUserInfoList(users);
    }

    public UserInfo getUserById(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return null;
        }

        User user = userDAO.findById(userId);
        return user != null ? convertToUserInfo(user) : null;
    }

    public UserInfo getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn saknas");
            return null;
        }

        User user = userDAO.findByUsername(username.trim());
        return user != null ? convertToUserInfo(user) : null;
    }

    public boolean createUser(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn måste anges");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("Lösenord måste anges");
            return false;
        }

        if (role == null || role.trim().isEmpty()) {
            System.err.println("Roll måste anges");
            return false;
        }

        User existing = userDAO.findByUsername(username.trim());
        if (existing != null) {
            System.err.println("Användarnamnet finns redan");
            return false;
        }

        User user = new User(username.trim(), password, role);
        return userDAO.createUser(user);
    }

    public boolean updateUser(int userId, String username, String password, String role) {
        if (userId <= 0) {
            System.err.println("Ogiltig användare - ID saknas");
            return false;
        }

        if (username == null || username.trim().isEmpty()) {
            System.err.println("Användarnamn måste anges");
            return false;
        }

        if (role == null || role.trim().isEmpty()) {
            System.err.println("Roll måste anges");
            return false;
        }

        User user = new User(userId, username.trim(), password, role);
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            System.err.println("Ogiltigt användar-ID");
            return false;
        }

        return userDAO.deleteUser(userId);
    }

    private UserInfo convertToUserInfo(User user) {
        return new UserInfo(
                user.getUserId(),
                user.getUsername(),
                user.getRole()
        );
    }

    private List<UserInfo> convertToUserInfoList(List<User> users) {
        List<UserInfo> userInfoList = new ArrayList<>();
        for (User user : users) {
            userInfoList.add(convertToUserInfo(user));
        }
        return userInfoList;
    }
}