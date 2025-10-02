package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.User;

import java.io.IOException;

/**
 * Filter för att kontrollera användarroller
 * Skyddar admin och warehouse paths
 */
@WebFilter(filterName = "RoleFilter", urlPatterns = {"/admin/*", "/warehouse/*"})
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Kontrollera behörighet för admin-paths
        if (path.startsWith(contextPath + "/admin/")) {
            if (!user.isAdmin()) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Du har inte behörighet att komma åt denna sida. Endast administratörer.");
                return;
            }
        }

        // Kontrollera behörighet för warehouse-paths
        if (path.startsWith(contextPath + "/warehouse/")) {
            if (!user.isWarehouse() && !user.isAdmin()) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Du har inte behörighet att komma åt denna sida. Endast lagerpersonal.");
                return;
            }
        }

        // Användaren har rätt behörighet - fortsätt
        chain.doFilter(request, response);
    }
}
