<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Produkthantering</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        nav a {
            margin-right: 15px;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid black;
            padding: 10px;
            text-align: left;
        }

        button {
            padding: 5px 10px;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/users">Användare</a>
    <a href="${pageContext.request.contextPath}/admin/categories">Kategorier</a>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>Produkthantering</h1>

<a href="${pageContext.request.contextPath}/admin/products?action=new">
    <button>+ Ny produkt</button>
</a>

<table>
    <thead>
    <tr>
        <th>Namn</th>
        <th>Kategori</th>
        <th>Pris</th>
        <th>Lager</th>
        <th>Åtgärder</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>${product.name}</td>
            <td>${product.categoryName}</td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="kr" maxFractionDigits="0"/>
            </td>
            <td>${product.stock} st</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${product.productId}">
                    <button>Redigera</button>
                </a>

                <a href="${pageContext.request.contextPath}/admin/products?action=delete&id=${product.productId}">
                    <button>Ta bort</button>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>