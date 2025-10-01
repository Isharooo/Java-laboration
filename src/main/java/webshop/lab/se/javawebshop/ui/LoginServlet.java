package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.User;
import webshop.lab.se.javawebshop.db.UserDAO;

import java.io.IOException;

/**
 * Servlet för inloggning (betyg 3)
 * Använder HttpSession för att hålla inloggad användare
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    /**
     * GET - Visa inloggningsformuläret
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forwarda till login.jsp
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * POST - Hantera inloggningsförsök
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Hämta formulärdata
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validera input
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            request.setAttribute("error", "Användarnamn och lösenord måste anges");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Autentisera användaren
        User user = userDAO.authenticateUser(username.trim(), password);

        if (user != null) {
            // Inloggning lyckades
            // Skapa session och spara användaren
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            System.out.println("Användare inloggad: " + user.getUsername() + " (roll: " + user.getRole() + ")");

            // Redirecta beroende på roll
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (user.isWarehouse()) {
                response.sendRedirect(request.getContextPath() + "/warehouse/orders");
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }

        } else {
            // Inloggning misslyckades
            request.setAttribute("error", "Felaktigt användarnamn eller lösenord");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}