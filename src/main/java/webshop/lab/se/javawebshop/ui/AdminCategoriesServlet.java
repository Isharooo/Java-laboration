package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.Category;
import webshop.lab.se.javawebshop.bo.ItemFacade;

import java.io.IOException;
import java.util.List;

/**
 * Admin servlet för kategorihantering (betyg 5)
 */
@WebServlet(name = "AdminCategoriesServlet", urlPatterns = {"/admin/categories"})
public class AdminCategoriesServlet extends HttpServlet {

    private ItemFacade itemFacade;

    @Override
    public void init() throws ServletException {
        itemFacade = new ItemFacade();
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
                deleteCategory(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createCategory(request, response);
        } else if ("update".equals(action)) {
            updateCategory(request, response);
        } else {
            listCategories(request, response);
        }
    }

    /**
     * Lista alla kategorier
     */
    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = itemFacade.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/admin/categories.jsp").forward(request, response);
    }

    /**
     * Visa formulär för ny kategori
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.removeAttribute("category");
        request.getRequestDispatcher("/WEB-INF/admin/category-form.jsp").forward(request, response);
    }

    /**
     * Visa formulär för att redigera kategori
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            Category category = itemFacade.getCategoryById(categoryId);

            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/WEB-INF/admin/category-form.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/categories");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/categories");
        }
    }

    /**
     * Skapa ny kategori
     */
    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");

        // Validera
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Kategorinamn måste anges");
            request.getRequestDispatcher("/WEB-INF/admin/category-form.jsp").forward(request, response);
            return;
        }

        Category category = new Category(name.trim());
        boolean success = itemFacade.createCategory(category);

        if (success) {
            request.setAttribute("success", "Kategori skapad!");
        } else {
            request.setAttribute("error", "Kunde inte skapa kategori.");
        }

        listCategories(request, response);
    }

    /**
     * Uppdatera befintlig kategori
     */
    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");

            // Validera
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Kategorinamn måste anges");
                Category category = itemFacade.getCategoryById(categoryId);
                request.setAttribute("category", category);
                request.getRequestDispatcher("/WEB-INF/admin/category-form.jsp").forward(request, response);
                return;
            }

            Category category = new Category(categoryId, name.trim());
            boolean success = itemFacade.updateCategory(category);

            if (success) {
                request.setAttribute("success", "Kategori uppdaterad!");
            } else {
                request.setAttribute("error", "Kunde inte uppdatera kategori.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltigt kategori-ID");
        }

        listCategories(request, response);
    }

    /**
     * Ta bort kategori
     */
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            boolean success = itemFacade.deleteCategory(categoryId);

            if (success) {
                request.setAttribute("success", "Kategori borttagen!");
            } else {
                request.setAttribute("error", "Kunde inte ta bort kategori.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ogiltigt kategori-ID");
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint")) {
                request.setAttribute("error", "Kategorin kan inte tas bort eftersom det finns produkter kopplade till den.");
            } else {
                request.setAttribute("error", "Kunde inte ta bort kategori: " + e.getMessage());
            }
        }

        listCategories(request, response);
    }
}