<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logga in - Webshop</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        input, button {
            display: block;
            margin: 5px 0;
            padding: 5px;
        }

        .error {
            background: #ffcccc;
            padding: 10px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<h1>Webshop</h1>
<p>Logga in för att handla</p>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
<div class="error"><%= error %></div>
<% } %>

<form action="login" method="post">
    <label for="username">Användarnamn</label>
    <input type="text" id="username" name="username" required autofocus>

    <label for="password">Lösenord</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">Logga in</button>
</form>

</body>
</html>