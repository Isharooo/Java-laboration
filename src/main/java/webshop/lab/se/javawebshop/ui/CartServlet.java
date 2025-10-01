package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.Cart;
import webshop.lab.se.javawebshop.bo.Product;
import webshop.lab.se.javawebshop.db.ProductDAO;

import java.io.IOException;

/**
 * Servlet för varukorg (betyg 3)
 * Hanterar: lägg till, ta bort, uppdatera kvantitet
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    /**
     * GET - Visa varukorgen
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

        // Hämta eller skapa varukorg
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Forwarda till cart.jsp
        request.getRequestDispatcher("/WEB-INF/cart.jsp").forward(request, response);
    }

    /**
     * POST - Hantera åtgärder i varukorgen
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

        // Hämta eller skapa varukorg
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Hämta action-parameter
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        switch (action) {
            case "add":
                handleAddToCart(request, cart);
                break;

            case "remove":
                handleRemoveFromCart(request, cart);
                break;

            case "update":
                handleUpdateQuantity(request, cart);
                break;

            case "clear":
                cart.clear();
                System.out.println("Varukorg tömdes");
                break;

            default:
                break;
        }

        // Redirecta tillbaka till varukorgen
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    /**
     * Lägger till en produkt i varukorgen
     */
    private void handleAddToCart(HttpServletRequest request, Cart cart) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String quantityParam = request.getParameter("quantity");
            int quantity = (quantityParam != null) ? Integer.parseInt(quantityParam) : 1;

            // Hämta produkten från databasen
            Product product = productDAO.getProductById(productId);

            if (product != null && product.getStock() >= quantity) {
                cart.addProduct(product, quantity);
                System.out.println("Lade till i varukorg: " + product.getName() + " (antal: " + quantity + ")");
            } else {
                System.out.println("Kunde inte lägga till produkt - otillräckligt lager");
            }

        } catch (NumberFormatException e) {
            System.err.println("Ogiltigt productId eller quantity");
        }
    }

    /**
     * Tar bort en produkt från varukorgen
     */
    private void handleRemoveFromCart(HttpServletRequest request, Cart cart) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            cart.removeProduct(productId);
            System.out.println("Tog bort produkt från varukorg: " + productId);

        } catch (NumberFormatException e) {
            System.err.println("Ogiltigt productId");
        }
    }

    /**
     * Uppdaterar kvantitet för en produkt i varukorgen
     */
    private void handleUpdateQuantity(HttpServletRequest request, Cart cart) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Kontrollera lagersaldo
            Product product = productDAO.getProductById(productId);

            if (product != null && product.getStock() >= quantity) {
                cart.updateQuantity(productId, quantity);
                System.out.println("Uppdaterade kvantitet för produkt " + productId + ": " + quantity);
            } else {
                System.out.println("Kunde inte uppdatera - otillräckligt lager");
            }

        } catch (NumberFormatException e) {
            System.err.println("Ogiltigt productId eller quantity");
        }
    }
}
