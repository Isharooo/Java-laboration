<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kategorihantering</title>
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
    <a href="${pageContext.request.contextPath}/admin/products">Produkter</a>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>Kategorihantering</h1>

<a href="${pageContext.request.contextPath}/admin/categories?action=new">
    <button>+ Ny kategori</button>
</a>

<table>
    <thead>
    <tr>
        <th>Namn</th>
        <th>Åtgärder</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="category" items="${categories}">
        <tr>
            <td>${category.name}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/categories?action=edit&id=${category.categoryId}">
                    <button>Redigera</button>
                </a>

                <a href="${pageContext.request.contextPath}/admin/categories?action=delete&id=${category.categoryId}">
                    <button>Ta bort</button>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>