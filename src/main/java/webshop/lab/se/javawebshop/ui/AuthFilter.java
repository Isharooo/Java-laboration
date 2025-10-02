package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter för att kontrollera att användare är inloggad
 * Skyddar alla sidor utom login
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Paths som INTE kräver inloggning
        boolean isLoginPage = path.equals(contextPath + "/login");
        boolean isLoginServlet = path.equals(contextPath + "/login");
        boolean isPublicResource = path.startsWith(contextPath + "/css/") ||
                path.startsWith(contextPath + "/js/") ||
                path.startsWith(contextPath + "/images/");

        // Kontrollera om användaren är inloggad
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // Om sidan kräver inloggning och användaren inte är inloggad
        if (!isLoginPage && !isPublicResource && !isLoggedIn) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        // Fortsätt med request
        chain.doFilter(request, response);
    }
}
