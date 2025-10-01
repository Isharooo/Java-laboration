package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.bo.*;
import webshop.lab.se.javawebshop.db.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

/**
 * Testservlet för alla DAO-klasser
 * URL: http://localhost:8080/test-all-dao
 */
@WebServlet(name = "TestAllDAOServlet", urlPatterns = {"/test-all-dao"})
public class TestAllDAOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>DAO Test - Alla klasser</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 30px; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".error { color: red; font-weight: bold; }");
        out.println("table { border-collapse: collapse; margin: 10px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("h2 { margin-top: 30px; border-bottom: 2px solid #4CAF50; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>DAO Test - Alla klasser</h1>");

        try {
            // Test CategoryDAO
            out.println("<h2>CategoryDAO Test</h2>");
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getAllCategories();

            out.println("<p class='success'>✓ Hämtade " + categories.size() + " kategorier</p>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Namn</th><th>Beskrivning</th></tr>");
            for (Category cat : categories) {
                out.println("<tr>");
                out.println("<td>" + cat.getCategoryId() + "</td>");
                out.println("<td>" + cat.getName() + "</td>");
                out.println("<td>" + cat.getDescription() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // Test ProductDAO
            out.println("<h2>ProductDAO Test</h2>");
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();

            out.println("<p class='success'>✓ Hämtade " + products.size() + " produkter</p>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Namn</th><th>Kategori</th><th>Pris</th><th>Lager</th></tr>");
            for (Product p : products) {
                String stockColor = p.isInStock() ? "green" : "red";
                out.println("<tr>");
                out.println("<td>" + p.getProductId() + "</td>");
                out.println("<td>" + p.getName() + "</td>");
                out.println("<td>" + p.getCategoryName() + "</td>");
                out.println("<td>" + p.getPrice() + " kr</td>");
                out.println("<td style='color:" + stockColor + "'>" + p.getStock() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // Test ProductDAO - checkStock
            out.println("<h3>Lagersaldokontroll</h3>");
            boolean stockAvailable = productDAO.checkStock(1, 5);
            out.println("<p class='success'>✓ checkStock(1, 5): " + stockAvailable + "</p>");

            // Test OrderDAO - Hämta ordrar
            out.println("<h2>OrderDAO Test - Hämta befintliga ordrar</h2>");
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.getAllOrders();

            out.println("<p class='success'>✓ Hämtade " + orders.size() + " ordrar</p>");
            out.println("<table>");
            out.println("<tr><th>Order ID</th><th>Användare</th><th>Status</th><th>Totalt</th><th>Antal rader</th></tr>");
            for (Order o : orders) {
                out.println("<tr>");
                out.println("<td>" + o.getOrderId() + "</td>");
                out.println("<td>" + o.getUsername() + "</td>");
                out.println("<td>" + o.getStatus() + "</td>");
                out.println("<td>" + o.getTotalAmount() + " kr</td>");
                out.println("<td>" + o.getOrderItems().size() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // Test OrderDAO - Skapa ny order MED TRANSAKTION (betyg 4!)
            out.println("<h2>OrderDAO Test - Skapa ny order MED TRANSAKTION</h2>");

            // Skapa en testorder
            Order newOrder = new Order();
            newOrder.setUserId(2); // johndoe
            newOrder.setStatus("pending");

            // Lägg till orderrader
            OrderItem item1 = new OrderItem();
            item1.setProductId(1); // Laptop Pro 15
            item1.setQuantity(1);
            item1.setPrice(new BigDecimal("12999.00"));
            newOrder.addOrderItem(item1);

            OrderItem item2 = new OrderItem();
            item2.setProductId(5); // T-shirt Basic
            item2.setQuantity(2);
            item2.setPrice(new BigDecimal("199.00"));
            newOrder.addOrderItem(item2);

            // Beräkna totalt
            BigDecimal total = item1.getSubtotal().add(item2.getSubtotal());
            newOrder.setTotalAmount(total);

            // Skapa ordern (MED TRANSAKTION!)
            boolean orderCreated = orderDAO.createOrder(newOrder);

            if (orderCreated) {
                out.println("<p class='success'>✓ Order skapad med ID: " + newOrder.getOrderId() + "</p>");
                out.println("<p class='success'>✓ Transaktion genomförd korrekt (setAutoCommit(false) + commit)</p>");
                out.println("<p><strong>Totalt:</strong> " + total + " kr</p>");
                out.println("<p><strong>Antal produkter:</strong> " + newOrder.getOrderItems().size() + "</p>");
            } else {
                out.println("<p class='error'>✗ Misslyckades med att skapa order</p>");
            }

            // Visa uppdaterat lagersaldo
            out.println("<h3>Lagersaldo efter order</h3>");
            Product laptop = productDAO.getProductById(1);
            Product tshirt = productDAO.getProductById(5);

            out.println("<table>");
            out.println("<tr><th>Produkt</th><th>Nytt lagersaldo</th></tr>");
            out.println("<tr><td>Laptop Pro 15</td><td>" + laptop.getStock() + "</td></tr>");
            out.println("<tr><td>T-shirt Basic</td><td>" + tshirt.getStock() + "</td></tr>");
            out.println("</table>");

        } catch (Exception e) {
            out.println("<p class='error'>✗ Fel uppstod: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }

        out.println("<hr>");
        out.println("<p><a href='test-user-dao'>UserDAO Test</a> | ");
        out.println("<a href='test-db'>DB Test</a> | ");
        out.println("<a href='index.jsp'>Startsida</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}