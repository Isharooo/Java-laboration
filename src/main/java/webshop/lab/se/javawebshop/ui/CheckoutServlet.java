package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.CartFacade;
import webshop.lab.se.javawebshop.bo.OrderFacade;

import java.io.IOException;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private OrderFacade orderFacade;
    private CartFacade cartFacade;

    @Override
    public void init() throws ServletException {
        orderFacade = new OrderFacade();
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

        if (cartInfo.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        request.setAttribute("cartInfo", cartInfo);
        request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserInfo user = (UserInfo) session.getAttribute("user");
        CartInfo cartInfo = cartFacade.getCartInfo(session);

        if (cartInfo.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            OrderInfo order = orderFacade.createOrderFromCart(user.getUserId(), session);

            if (order != null) {
                System.out.println("Order " + order.getOrderId() + " skapad - redirectar till produkter");

                cartFacade.clearCart(session);

                response.sendRedirect(request.getContextPath() + "/products");

            } else {
                request.setAttribute("error", "Kunde inte skapa order. Kontrollera lagersaldo och försök igen.");
                request.setAttribute("cartInfo", cartInfo);
                request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Fel vid orderläggning: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ett tekniskt fel uppstod. Vänligen försök igen.");
            request.setAttribute("cartInfo", cartInfo);
            request.getRequestDispatcher("/WEB-INF/checkout.jsp").forward(request, response);
        }
    }
}