package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.CartFacade;

import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private CartFacade cartFacade;

    @Override
    public void init() throws ServletException {
        cartFacade = new CartFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CartInfo cartInfo = cartFacade.getCartInfo(session);
        request.setAttribute("cartInfo", cartInfo);

        request.getRequestDispatcher("/WEB-INF/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        switch (action) {
            case "add":
                handleAddToCart(request, session);
                break;

            case "remove":
                handleRemoveFromCart(request, session);
                break;

            case "update":
                handleUpdateQuantity(request, session);
                break;

            case "clear":
                cartFacade.clearCart(session);
                break;

            default:
                break;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void handleAddToCart(HttpServletRequest request, HttpSession session) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String quantityParam = request.getParameter("quantity");
            int quantity = (quantityParam != null) ? Integer.parseInt(quantityParam) : 1;

            cartFacade.addProduct(session, productId, quantity);

        } catch (NumberFormatException e) {
            System.err.println("Ogiltigt productId eller quantity");
        }
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpSession session) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            cartFacade.removeProduct(session, productId);

        } catch (NumberFormatException e) {
            System.err.println("Ogiltigt productId");
        }
    }

    private void handleUpdateQuantity(HttpServletRequest request, HttpSession session) {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            cartFacade.updateQuantity(session, productId, quantity);

        } catch (NumberFormatException e) {
            System.err.println("Ogiltigt productId eller quantity");
        }
    }
}