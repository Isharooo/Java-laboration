<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Användarhantering</title>
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

        button, a.button {
            padding: 5px 10px;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/products">Produkter</a>
    <a href="${pageContext.request.contextPath}/admin/categories">Kategorier</a>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>Användarhantering</h1>

<a href="${pageContext.request.contextPath}/admin/users?action=new">
    <button>Ny användare</button>
</a>

<table>
    <thead>
    <tr>
        <th>Användarnamn</th>
        <th>Roll</th>
        <th>Åtgärder</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.username}</td>
            <td>${user.role}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/users?action=edit&id=${user.userId}">
                    <button>Redigera</button>
                </a>

                <c:if test="${user.userId != sessionScope.user.userId}">
                    <a href="${pageContext.request.contextPath}/admin/users?action=delete&id=${user.userId}">
                        <button>Ta bort</button>
                    </a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>