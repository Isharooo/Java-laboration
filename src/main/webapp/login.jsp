<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logga in - Webshop</title>
<%--    <style>--%>
<%--        * {--%>
<%--            margin: 0;--%>
<%--            padding: 0;--%>
<%--            box-sizing: border-box;--%>
<%--        }--%>

<%--        body {--%>
<%--            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;--%>
<%--            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);--%>
<%--            min-height: 100vh;--%>
<%--            display: flex;--%>
<%--            justify-content: center;--%>
<%--            align-items: center;--%>
<%--            padding: 20px;--%>
<%--        }--%>

<%--        .login-container {--%>
<%--            background: white;--%>
<%--            padding: 40px;--%>
<%--            border-radius: 10px;--%>
<%--            box-shadow: 0 10px 25px rgba(0,0,0,0.2);--%>
<%--            width: 100%;--%>
<%--            max-width: 400px;--%>
<%--        }--%>

<%--        h1 {--%>
<%--            color: #333;--%>
<%--            margin-bottom: 10px;--%>
<%--            text-align: center;--%>
<%--        }--%>

<%--        .subtitle {--%>
<%--            color: #666;--%>
<%--            text-align: center;--%>
<%--            margin-bottom: 30px;--%>
<%--            font-size: 14px;--%>
<%--        }--%>

<%--        .form-group {--%>
<%--            margin-bottom: 20px;--%>
<%--        }--%>

<%--        label {--%>
<%--            display: block;--%>
<%--            margin-bottom: 5px;--%>
<%--            color: #555;--%>
<%--            font-weight: 500;--%>
<%--        }--%>

<%--        input[type="text"],--%>
<%--        input[type="password"] {--%>
<%--            width: 100%;--%>
<%--            padding: 12px;--%>
<%--            border: 2px solid #e0e0e0;--%>
<%--            border-radius: 5px;--%>
<%--            font-size: 14px;--%>
<%--            transition: border-color 0.3s;--%>
<%--        }--%>

<%--        input[type="text"]:focus,--%>
<%--        input[type="password"]:focus {--%>
<%--            outline: none;--%>
<%--            border-color: #667eea;--%>
<%--        }--%>

<%--        .btn {--%>
<%--            width: 100%;--%>
<%--            padding: 12px;--%>
<%--            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);--%>
<%--            color: white;--%>
<%--            border: none;--%>
<%--            border-radius: 5px;--%>
<%--            font-size: 16px;--%>
<%--            font-weight: 600;--%>
<%--            cursor: pointer;--%>
<%--            transition: transform 0.2s;--%>
<%--        }--%>

<%--        .btn:hover {--%>
<%--            transform: translateY(-2px);--%>
<%--            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);--%>
<%--        }--%>

<%--        .error {--%>
<%--            background: #fee;--%>
<%--            border: 1px solid #fcc;--%>
<%--            color: #c33;--%>
<%--            padding: 10px;--%>
<%--            border-radius: 5px;--%>
<%--            margin-bottom: 20px;--%>
<%--            text-align: center;--%>
<%--        }--%>

<%--        .test-accounts {--%>
<%--            margin-top: 30px;--%>
<%--            padding: 15px;--%>
<%--            background: #f5f5f5;--%>
<%--            border-radius: 5px;--%>
<%--            font-size: 12px;--%>
<%--        }--%>

<%--        .test-accounts h3 {--%>
<%--            font-size: 14px;--%>
<%--            margin-bottom: 10px;--%>
<%--            color: #555;--%>
<%--        }--%>

<%--        .test-accounts p {--%>
<%--            margin: 5px 0;--%>
<%--            color: #666;--%>
<%--        }--%>

<%--        .test-accounts strong {--%>
<%--            color: #333;--%>
<%--        }--%>
<%--    </style>--%>
</head>
<body>
<div class="login-container">
    <h1>Webshop</h1>
    <p class="subtitle">Logga in för att handla</p>

    <%-- Visa felmeddelande om det finns --%>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <form action="login" method="post">
        <div class="form-group">
            <label for="username">Användarnamn</label>
            <input type="text" id="username" name="username" required autofocus>
        </div>

        <div class="form-group">
            <label for="password">Lösenord</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit" class="btn">Logga in</button>
    </form>

    <div class="test-accounts">
        <h3>Testkonton:</h3>
        <p><strong>Admin:</strong> admin / admin123</p>
        <p><strong>Kund:</strong> johndoe / password123</p>
        <p><strong>Lager:</strong> warehouse1 / warehouse123</p>
    </div>
</div>
</body>
</html>
