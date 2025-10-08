package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.OrderFacade;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "WarehouseServlet", urlPatterns = {"/warehouse/orders"})
public class WarehouseServlet extends HttpServlet {

    private OrderFacade orderFacade;

    @Override
    public void init() throws ServletException {
        orderFacade = new OrderFacade();
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

    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<OrderInfo> orders = orderFacade.getAllOrders();
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/WEB-INF/warehouse/orders.jsp").forward(request, response);
    }

    private void viewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderInfo order = orderFacade.getOrderById(orderId);

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

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String newStatus = request.getParameter("status");

            boolean success = orderFacade.updateOrderStatus(orderId, newStatus);

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
}