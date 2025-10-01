package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.User;
import webshop.lab.se.javawebshop.db.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Testservlet för UserDAO
 * URL: http://localhost:8080/test-user-dao
 */
@WebServlet(name = "TestUserDAOServlet", urlPatterns = {"/test-user-dao"})
public class TestUserDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>UserDAO Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 50px; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("table { border-collapse: collapse; margin: 20px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>UserDAO Test</h1>");

        UserDAO userDAO = new UserDAO();

        try {
            // Test 1: Hämta alla användare
            out.println("<h2>Test 1: getAllUsers()</h2>");
            List<User> users = userDAO.getAllUsers();

            if (!users.isEmpty()) {
                out.println("<p class='success'>✓ Hittade " + users.size() + " användare</p>");
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Användarnamn</th><th>Roll</th></tr>");

                for (User user : users) {
                    out.println("<tr>");
                    out.println("<td>" + user.getUserId() + "</td>");
                    out.println("<td>" + user.getUsername() + "</td>");
                    out.println("<td>" + user.getRole() + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
            } else {
                out.println("<p class='error'>✗ Inga användare hittades</p>");
            }

            // Test 2: Hitta användare med användarnamn
            out.println("<h2>Test 2: findByUsername('admin')</h2>");
            User admin = userDAO.findByUsername("admin");

            if (admin != null) {
                out.println("<p class='success'>✓ Användare hittad</p>");
                out.println("<ul>");
                out.println("<li><strong>ID:</strong> " + admin.getUserId() + "</li>");
                out.println("<li><strong>Användarnamn:</strong> " + admin.getUsername() + "</li>");
                out.println("<li><strong>Roll:</strong> " + admin.getRole() + "</li>");
                out.println("<li><strong>Är admin:</strong> " + admin.isAdmin() + "</li>");
                out.println("</ul>");
            } else {
                out.println("<p class='error'>✗ Användare 'admin' hittades inte</p>");
            }

            // Test 3: Autentisera användare
            out.println("<h2>Test 3: authenticateUser('admin', 'admin123')</h2>");
            User authenticatedUser = userDAO.authenticateUser("admin", "admin123");

            if (authenticatedUser != null) {
                out.println("<p class='success'>✓ Autentisering lyckades för: "
                        + authenticatedUser.getUsername() + "</p>");
            } else {
                out.println("<p class='error'>✗ Autentisering misslyckades</p>");
            }

            // Test 4: Misslyckat autentiseringsförsök
            out.println("<h2>Test 4: authenticateUser('admin', 'felaktigt_lösenord')</h2>");
            User failedAuth = userDAO.authenticateUser("admin", "felaktigt_lösenord");

            if (failedAuth == null) {
                out.println("<p class='success'>✓ Autentisering avvisades korrekt</p>");
            } else {
                out.println("<p class='error'>✗ SÄKERHETSPROBLEM: Autentisering lyckades med fel lösenord!</p>");
            }

            // Test 5: Hitta användare med ID
            out.println("<h2>Test 5: findById(1)</h2>");
            User userById = userDAO.findById(1);

            if (userById != null) {
                out.println("<p class='success'>✓ Användare med ID 1 hittad: "
                        + userById.getUsername() + "</p>");
            } else {
                out.println("<p class='error'>✗ Användare med ID 1 hittades inte</p>");
            }

        } catch (Exception e) {
            out.println("<p class='error'>✗ Fel uppstod: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }

        out.println("<hr>");
        out.println("<p><a href='test-db'>Databas Test</a> | <a href='index.jsp'>Startsida</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}