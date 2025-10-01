package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet för utloggning
 * Tar bort sessionen och redirectar till login
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Hämta sessionen (skapa inte en ny om den inte finns)
        HttpSession session = request.getSession(false);

        if (session != null) {
            String username = (String) session.getAttribute("username");

            // Invalidera sessionen (tar bort alla attribut)
            session.invalidate();

            System.out.println("Användare utloggad: " + username);
        }

        // Redirecta till login-sidan
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

