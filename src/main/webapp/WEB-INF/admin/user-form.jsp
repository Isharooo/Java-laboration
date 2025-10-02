<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty user ? 'Ny användare' : 'Redigera användare'} - Admin</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }

        nav {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 0;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .nav-container {
            max-width: 1400px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
        }

        .logo {
            font-size: 24px;
            font-weight: bold;
        }

        .container {
            max-width: 600px;
            margin: 30px auto;
            padding: 0 20px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .form-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #555;
        }

        input[type="text"],
        input[type="password"],
        select {
            width: 100%;
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
        }

        input:focus,
        select:focus {
            outline: none;
            border-color: #667eea;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }

        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }
    </style>
</head>
<body>
<nav>
    <div class="nav-container">
        <div class="logo">Admin Panel</div>
    </div>
</nav>

<div class="container">
    <h1>${empty user ? 'Ny användare' : 'Redigera användare'}</h1>

    <div class="form-card">
        <c:if test="${not empty error}">
            <div class="alert-error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/users" method="post">
            <input type="hidden" name="action" value="${empty user ? 'create' : 'update'}">
            <c:if test="${not empty user}">
                <input type="hidden" name="userId" value="${user.userId}">
            </c:if>

            <div class="form-group">
                <label for="username">Användarnamn *</label>
                <input type="text" id="username" name="username"
                       value="${user.username}" required>
            </div>

            <div class="form-group">
                <label for="password">
                    Lösenord ${empty user ? '*' : '(lämna tomt för att behålla)'}
                </label>
                <input type="password" id="password" name="password"
                ${empty user ? 'required' : ''}>
            </div>

            <div class="form-group">
                <label for="role">Roll *</label>
                <select id="role" name="role" required>
                    <option value="customer" ${user.role == 'customer' ? 'selected' : ''}>Kund</option>
                    <option value="warehouse" ${user.role == 'warehouse' ? 'selected' : ''}>Lagerpersonal</option>
                    <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Administratör</option>
                </select>
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                    Avbryt
                </a>
                <button type="submit" class="btn btn-primary">
                    ${empty user ? 'Skapa' : 'Uppdatera'}
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
