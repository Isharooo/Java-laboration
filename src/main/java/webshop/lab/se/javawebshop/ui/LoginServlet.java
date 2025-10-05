package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.User;
import webshop.lab.se.javawebshop.bo.UserFacade;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserFacade userFacade;

    @Override
    public void init() throws ServletException {
        userFacade = new UserFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            request.setAttribute("error", "Användarnamn och lösenord måste anges");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userFacade.login(username.trim(), password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            System.out.println("Användare inloggad: " + user.getUsername() + " (roll: " + user.getRole() + ")");

            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (user.isWarehouse()) {
                response.sendRedirect(request.getContextPath() + "/warehouse/orders");
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }

        } else {
            request.setAttribute("error", "Felaktigt användarnamn eller lösenord");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}