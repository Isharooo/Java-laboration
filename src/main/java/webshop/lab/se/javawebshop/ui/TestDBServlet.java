package webshop.lab.se.javawebshop.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webshop.lab.se.javawebshop.db.DBManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Testservlet för att verifiera databasanslutning
 * URL: http://localhost:8080/test-db
 */
@WebServlet(name = "TestDBServlet", urlPatterns = {"/test-db"})
public class TestDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Databas Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 50px; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("table { border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Databasanslutning Test</h1>");

        DBManager dbManager = DBManager.getInstance();
        Connection conn = null;

        try {
            // Testa anslutning
            out.println("<h2>Anslutningstest</h2>");
            boolean connected = dbManager.testConnection();

            if (connected) {
                out.println("<p class='success'>✓ Anslutning till databas lyckades!</p>");
                out.println("<p><strong>URL:</strong> " + dbManager.getUrl() + "</p>");
                out.println("<p><strong>Användarnamn:</strong> " + dbManager.getUsername() + "</p>");
            } else {
                out.println("<p class='error'>✗ Anslutning till databas misslyckades!</p>");
            }

            // Hämta data från databasen
            out.println("<h2>Test: Hämta användare från databasen</h2>");

            conn = dbManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT user_id, username, role FROM users LIMIT 5");

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Användarnamn</th><th>Roll</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("user_id") + "</td>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("role") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            rs.close();
            stmt.close();

            out.println("<p class='success'>✓ Data hämtades framgångsrikt från databasen!</p>");

        } catch (Exception e) {
            out.println("<p class='error'>✗ Fel uppstod: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        } finally {
            dbManager.closeConnection(conn);
        }

        out.println("<p><a href='index.jsp'>Tillbaka till startsidan</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}