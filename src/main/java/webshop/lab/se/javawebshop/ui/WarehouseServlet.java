package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.Order;
import webshop.lab.se.javawebshop.db.OrderDAO;

import java.io.IOException;
import java.util.List;

/**
 * Warehouse servlet för orderhantering (betyg 5)
 * Lagerpersonal kan se ordrar och uppdatera status
 */
@WebServlet(name = "WarehouseServlet", urlPatterns = {"/warehouse/orders"})
public class WarehouseServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("view".equals(action)) {
            viewOrder(request, response);
        } else {
            listOrders(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            updateOrderStatus(request, response);
        } else {
            listOrders(request, response);
        }
    }

    /**
     * Lista alla ordrar (med optional filter på status)
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String statusFilter = request.getParameter("status");
        List<Order> orders;

        if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("all")) {
            orders = orderDAO.getOrdersByStatus(statusFilter);
            request.setAttribute("currentStatus", statusFilter);
        } else {
            orders = orderDAO.getAllOrders();
            request.setAttribute("currentStatus", "all");
        }

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/warehouse/orders.jsp").forward(request, response);
    }

    /**
     * Visa detaljer för en specifik order
     */
    private void viewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderDAO.getOrderById(orderId);

            if (order != null) {
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/warehouse/order-details.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/warehouse/orders");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouse/orders");
        }
    }

    /**
     * Uppdatera orderstatus (t.ex. "packa" en order)
     */
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String newStatus = request.getParameter("status");

            // Validera status
            if (!isValidStatus(newStatus)) {
                request.setAttribute("error", "Ogiltig status");
                listOrders(request, response);
                return;
            }

            boolean success = orderDAO.updateOrderStatus(orderId, newStatus);

            if (success) {
                System.out.println("Order " + orderId + " status uppdaterad till: " + newStatus);
                request.setAttribute("success", "Orderstatus uppdaterad till: " + newStatus);
            } else {
                request.setAttribute("error", "Kunde inte uppdatera orderstatus");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltigt order-ID");
        }

        listOrders(request, response);
    }

    /**
     * Validera att status är giltig
     */
    private boolean isValidStatus(String status) {
        return status != null && (status.equals("pending") || status.equals("packed"));}
}
