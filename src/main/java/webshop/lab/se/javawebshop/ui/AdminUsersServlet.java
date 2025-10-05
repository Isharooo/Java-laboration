package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.User;
import webshop.lab.se.javawebshop.bo.UserFacade;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUsersServlet", urlPatterns = {"/admin/users"})
public class AdminUsersServlet extends HttpServlet {

    private UserFacade userFacade;

    @Override
    public void init() throws ServletException {
        userFacade = new UserFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        } else {
            listUsers(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userFacade.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/admin/users.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.removeAttribute("user");
        request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userFacade.getUserById(userId);

            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {

            request.setAttribute("error", "Alla fält måste fyllas i");
            request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
            return;
        }

        User user = new User(username.trim(), password, role);
        boolean success = userFacade.createUser(user);

        if (success) {
            request.setAttribute("success", "Användare skapad!");
        } else {
            request.setAttribute("error", "Kunde inte skapa användare. Användarnamnet kanske redan finns.");
        }

        listUsers(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            if (username == null || username.trim().isEmpty() ||
                    role == null || role.trim().isEmpty()) {

                request.setAttribute("error", "Användarnamn och roll måste fyllas i");
                User user = userFacade.getUserById(userId);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/admin/user-form.jsp").forward(request, response);
                return;
            }

            User user = new User(userId, username.trim(), password, role);
            boolean success = userFacade.updateUser(user);

            if (success) {
                request.setAttribute("success", "Användare uppdaterad!");
            } else {
                request.setAttribute("error", "Kunde inte uppdatera användare.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltigt användar-ID");
        }

        listUsers(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser.getUserId() == userId) {
                request.setAttribute("error", "Du kan inte ta bort ditt eget konto!");
            } else {
                boolean success = userFacade.deleteUser(userId);

                if (success) {
                    request.setAttribute("success", "Användare borttagen!");
                } else {
                    request.setAttribute("error", "Kunde inte ta bort användare.");
                }
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltigt användar-ID");
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                request.setAttribute("error", "Användaren kan inte tas bort eftersom det finns ordrar kopplade till kontot.");
            } else {
                request.setAttribute("error", "Kunde inte ta bort användare: " + e.getMessage());
            }
        }

        listUsers(request, response);
    }
}


