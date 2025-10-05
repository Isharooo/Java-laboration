package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.ItemFacade;
import webshop.lab.se.javawebshop.bo.OrderFacade;
import webshop.lab.se.javawebshop.bo.UserFacade;

import java.io.IOException;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private UserFacade userFacade;
    private ItemFacade itemFacade;
    private OrderFacade orderFacade;

    @Override
    public void init() throws ServletException {
        userFacade = new UserFacade();
        itemFacade = new ItemFacade();
        orderFacade = new OrderFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int totalUsers = userFacade.getAllUsers().size();
        int totalProducts = itemFacade.getAllProducts().size();
        int totalCategories = itemFacade.getAllCategories().size();
        int totalOrders = orderFacade.getAllOrders().size();

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalCategories", totalCategories);
        request.setAttribute("totalOrders", totalOrders);

        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
    }
}