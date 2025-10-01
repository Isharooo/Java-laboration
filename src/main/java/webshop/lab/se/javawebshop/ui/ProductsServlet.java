package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.bo.Category;
import webshop.lab.se.javawebshop.bo.Product;
import webshop.lab.se.javawebshop.db.CategoryDAO;
import webshop.lab.se.javawebshop.db.ProductDAO;

import java.io.IOException;
import java.util.List;

/**
 * Servlet för att visa produkter (betyg 3)
 * URL: /products eller /products?category=X
 */
@WebServlet(name = "ProductsServlet", urlPatterns = {"/products"})
public class ProductsServlet extends HttpServlet {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kontrollera om användaren är inloggad
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Hämta alla kategorier för menyn
        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // Kolla om vi ska filtrera på kategori
        String categoryParam = request.getParameter("category");
        List<Product> products;

        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryParam);
                products = productDAO.getProductsByCategory(categoryId);

                // Hitta kategorinamnet för att visa i rubriken
                Category selectedCategory = categoryDAO.getCategoryById(categoryId);
                request.setAttribute("selectedCategory", selectedCategory);

            } catch (NumberFormatException e) {
                // Ogiltig kategori-id, visa alla produkter
                products = productDAO.getAllProducts();
            }
        } else {
            // Visa alla produkter
            products = productDAO.getAllProducts();
        }

        request.setAttribute("products", products);

        // Forwarda till JSP
        request.getRequestDispatcher("/WEB-INF/products.jsp").forward(request, response);
    }
}