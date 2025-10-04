<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        a {
            display: block;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<h1>Admin Dashboard</h1>

<a href="${pageContext.request.contextPath}/admin/users">Användare</a>
<a href="${pageContext.request.contextPath}/admin/products">Produkter</a>
<a href="${pageContext.request.contextPath}/admin/categories">Kategorier</a>
<a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</body>
</html>