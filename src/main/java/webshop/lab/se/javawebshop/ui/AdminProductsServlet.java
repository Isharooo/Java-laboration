package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.Category;
import webshop.lab.se.javawebshop.bo.Product;
import webshop.lab.se.javawebshop.db.CategoryDAO;
import webshop.lab.se.javawebshop.db.ProductDAO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Admin servlet för produkthantering (betyg 5)
 */
@WebServlet(name = "AdminProductsServlet", urlPatterns = {"/admin/products"})
public class AdminProductsServlet extends HttpServlet {

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

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createProduct(request, response);
        } else if ("update".equals(action)) {
            updateProduct(request, response);
        } else {
            listProducts(request, response);
        }
    }

    /**
     * Lista alla produkter
     */
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/admin/products.jsp").forward(request, response);
    }

    /**
     * Visa formulär för ny produkt
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.removeAttribute("product");
        request.getRequestDispatcher("/WEB-INF/admin/product-form.jsp").forward(request, response);
    }

    /**
     * Visa formulär för att redigera produkt
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.getProductById(productId);
            List<Category> categories = categoryDAO.getAllCategories();

            if (product != null) {
                request.setAttribute("product", product);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/admin/product-form.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/products");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }

    /**
     * Skapa ny produkt
     */
    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String imageUrl = request.getParameter("imageUrl");

            // Validera
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Produktnamn måste anges");
            }

            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Priset kan inte vara negativt");
            }

            if (stock < 0) {
                throw new IllegalArgumentException("Lagersaldo kan inte vara negativt");
            }

            Product product = new Product(categoryId, name.trim(), description, price, stock, imageUrl);
            boolean success = productDAO.createProduct(product);

            if (success) {
                request.setAttribute("success", "Produkt skapad!");
            } else {
                request.setAttribute("error", "Kunde inte skapa produkt.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltiga värden i formuläret");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }

        listProducts(request, response);
    }

    /**
     * Uppdatera befintlig produkt
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String imageUrl = request.getParameter("imageUrl");

            // Validera
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Produktnamn måste anges");
            }

            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Priset kan inte vara negativt");
            }

            if (stock < 0) {
                throw new IllegalArgumentException("Lagersaldo kan inte vara negativt");
            }

            Product product = new Product(productId, categoryId, name.trim(), description, price, stock, imageUrl);
            boolean success = productDAO.updateProduct(product);

            if (success) {
                request.setAttribute("success", "Produkt uppdaterad!");
            } else {
                request.setAttribute("error", "Kunde inte uppdatera produkt.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltiga värden i formuläret");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }

        listProducts(request, response);
    }

    /**
     * Ta bort produkt
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            boolean success = productDAO.deleteProduct(productId);

            if (success) {
                request.setAttribute("success", "Produkt borttagen!");
            } else {
                request.setAttribute("error", "Kunde inte ta bort produkt.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltigt produkt-ID");
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                request.setAttribute("error", "Produkten kan inte tas bort eftersom det finns ordrar kopplade till den.");
            } else {
                request.setAttribute("error", "Kunde inte ta bort produkt: " + e.getMessage());
            }
        }

        listProducts(request, response);
    }
}