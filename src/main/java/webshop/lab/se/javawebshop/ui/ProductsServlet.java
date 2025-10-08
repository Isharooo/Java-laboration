package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.ItemFacade;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductsServlet", urlPatterns = {"/products"})
public class ProductsServlet extends HttpServlet {

    private ItemFacade itemFacade;

    @Override
    public void init() throws ServletException {
        itemFacade = new ItemFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<CategoryInfo> categories = itemFacade.getAllCategories();
        request.setAttribute("categories", categories);

        String categoryParam = request.getParameter("category");
        List<ProductInfo> products;

        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryParam);
                products = itemFacade.getProductsByCategory(categoryId);

                CategoryInfo selectedCategory = itemFacade.getCategoryById(categoryId);
                request.setAttribute("selectedCategory", selectedCategory);

            } catch (NumberFormatException e) {
                products = itemFacade.getAllProducts();
            }
        } else {
            products = itemFacade.getAllProducts();
        }

        request.setAttribute("products", products);

        request.getRequestDispatcher("/WEB-INF/products.jsp").forward(request, response);
    }
}