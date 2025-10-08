package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

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

        UserInfo user = (UserInfo) session.getAttribute("user");

        if (path.startsWith(contextPath + "/admin/")) {
            if (!user.isAdmin()) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Du har inte behörighet att komma åt denna sida. Endast administratörer.");
                return;
            }
        }

        if (path.startsWith(contextPath + "/warehouse/")) {
            if (!user.isWarehouse() && !user.isAdmin()) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Du har inte behörighet att komma åt denna sida. Endast lagerpersonal.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}