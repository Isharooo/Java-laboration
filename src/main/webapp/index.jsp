<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Redirecta till login om användaren inte är inloggad
    // Annars redirecta till products
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
    } else {
        response.sendRedirect("products");
    }
%>
