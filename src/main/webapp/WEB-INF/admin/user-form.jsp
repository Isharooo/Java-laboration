<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty requestScope.user ? 'Ny användare' : 'Redigera användare'}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="password"],
        select {
            width: 300px;
            padding: 5px;
        }

        button {
            padding: 8px 15px;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<h1>${empty requestScope.user ? 'Ny användare' : 'Redigera användare'}</h1>

<form action="${pageContext.request.contextPath}/admin/users" method="post">
    <input type="hidden" name="action" value="${empty requestScope.user ? 'create' : 'update'}">
    <c:if test="${not empty requestScope.user}">
        <input type="hidden" name="userId" value="${requestScope.user.userId}">
    </c:if>

    <div class="form-group">
        <label for="username">Användarnamn *</label>
        <input type="text" id="username" name="username"
               value="${requestScope.user.username}" required>
    </div>

    <div class="form-group">
        <label for="password">
            Lösenord ${empty requestScope.user ? '*' : '(lämna tomt för att behålla)'}
        </label>
        <input type="password" id="password" name="password"
        ${empty requestScope.user ? 'required' : ''}>
    </div>

    <div class="form-group">
        <label for="role">Roll *</label>
        <select id="role" name="role" required>
            <option value="customer" ${requestScope.user.role == 'customer' ? 'selected' : ''}>Kund</option>
            <option value="warehouse" ${requestScope.user.role == 'warehouse' ? 'selected' : ''}>Lagerpersonal</option>
            <option value="admin" ${requestScope.user.role == 'admin' ? 'selected' : ''}>Administratör</option>
        </select>
    </div>

    <a href="${pageContext.request.contextPath}/admin/users">
        <button type="button">Avbryt</button>
    </a>
    <button type="submit">
        ${empty requestScope.user ? 'Skapa' : 'Uppdatera'}
    </button>
</form>
</body>
</html>