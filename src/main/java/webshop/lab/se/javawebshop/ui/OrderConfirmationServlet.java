package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.Order;
import webshop.lab.se.javawebshop.db.OrderDAO;

import java.io.IOException;

/**
 * Servlet för orderbekräftelse
 */
@WebServlet(name = "OrderConfirmationServlet", urlPatterns = {"/order-confirmation"})
public class OrderConfirmationServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Hämta order-id från session
        Integer orderId = (Integer) session.getAttribute("lastOrderId");

        if (orderId == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // Hämta ordern från databasen
        Order order = orderDAO.getOrderById(orderId);

        if (order != null) {
            request.setAttribute("order", order);
        }

        // Ta bort från session efter att den visats
        session.removeAttribute("lastOrderId");

        request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
    }
}