package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.Cart;
import webshop.lab.se.javawebshop.bo.Order;
import webshop.lab.se.javawebshop.bo.OrderItem;
import webshop.lab.se.javawebshop.bo.User;
import webshop.lab.se.javawebshop.db.OrderDAO;

import java.io.IOException;

/**
 * Servlet för att genomföra köp (betyg 3 & 4)
 * Använder transaktioner för att skapa order
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    /**
     * GET - Visa checkout-sidan
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kontrollera inloggning
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Hämta varukorg
        Cart cart = (Cart) session.getAttribute("cart");

        // Om varukorgen är tom, redirecta till produkter
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // Forwarda till checkout.jsp
        request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
    }

    /**
     * POST - Genomför köpet
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kontrollera inloggning
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        // Validera varukorg
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            // Skapa Order-objekt
            Order order = new Order();
            order.setUserId(user.getUserId());
            order.setStatus("pending");
            order.setTotalAmount(cart.getTotalPrice());

            // Lägg till orderrader från varukorgen
            for (Cart.CartItem cartItem : cart.getItems().values()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProduct().getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProduct().getPrice());
                order.addOrderItem(orderItem);
            }

            // Skapa ordern i databasen (MED TRANSAKTION!)
            boolean success = orderDAO.createOrder(order);

            if (success) {
                // Order skapades - töm varukorgen
                cart.clear();

                // Spara order-id i session för bekräftelsesidan
                session.setAttribute("lastOrderId", order.getOrderId());

                // Redirecta till bekräftelsesidan
                response.sendRedirect(request.getContextPath() + "/order-confirmation");

            } else {
                // Något gick fel
                request.setAttribute("error", "Kunde inte skapa order. Kontrollera lagersaldo och försök igen.");
                request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Fel vid orderläggning: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ett tekniskt fel uppstod. Vänligen försök igen.");
            request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
        }
    }
}