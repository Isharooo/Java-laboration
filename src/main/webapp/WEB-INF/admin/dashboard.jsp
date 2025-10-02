<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Webshop</title>
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

        .nav-links {
            display: flex;
            gap: 20px;
            align-items: center;
        }

        .nav-links a {
            color: white;
            text-decoration: none;
            padding: 8px 15px;
            border-radius: 5px;
            transition: background 0.3s;
        }

        .nav-links a:hover {
            background: rgba(255,255,255,0.2);
        }

        .container {
            max-width: 1400px;
            margin: 30px auto;
            padding: 0 20px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .stat-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }

        .stat-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }

        .stat-number {
            font-size: 36px;
            font-weight: bold;
            color: #667eea;
            margin-bottom: 10px;
        }

        .stat-label {
            font-size: 16px;
            color: #666;
        }

        .admin-menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
        }

        .menu-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }

        .menu-card:hover {
            transform: translateY(-5px);
        }

        .menu-card h3 {
            margin-bottom: 15px;
            color: #333;
        }

        .menu-card p {
            color: #666;
            margin-bottom: 20px;
            line-height: 1.6;
        }

        .menu-card a {
            display: inline-block;
            padding: 10px 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 600;
            transition: transform 0.2s;
        }

        .menu-card a:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body>
<nav>
    <div class="nav-container">
        <div class="logo">Admin Panel</div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/users">Användare</a>
            <a href="${pageContext.request.contextPath}/admin/products">Produkter</a>
            <a href="${pageContext.request.contextPath}/admin/categories">Kategorier</a>
            <a href="${pageContext.request.contextPath}/products">Kundvy</a>
            <span>${sessionScope.username}</span>
            <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
        </div>
    </div>
</nav>

<div class="container">
    <h1>Admin Dashboard</h1>

    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon">👥</div>
            <div class="stat-number">${totalUsers}</div>
            <div class="stat-label">Användare</div>
        </div>

        <div class="stat-card">
            <div class="stat-icon">📦</div>
            <div class="stat-number">${totalProducts}</div>
            <div class="stat-label">Produkter</div>
        </div>

        <div class="stat-card">
            <div class="stat-icon">📁</div>
            <div class="stat-number">${totalCategories}</div>
            <div class="stat-label">Kategorier</div>
        </div>

        <div class="stat-card">
            <div class="stat-icon">🛒</div>
            <div class="stat-number">${totalOrders}</div>
            <div class="stat-label">Ordrar</div>
        </div>
    </div>

    <h2>Hantera</h2>
    <div class="admin-menu">
        <div class="menu-card">
            <h3>👥 Användarhantering</h3>
            <p>Skapa, redigera och ta bort användare. Hantera roller och behörigheter.</p>
            <a href="${pageContext.request.contextPath}/admin/users">Hantera användare</a>
        </div>

        <div class="menu-card">
            <h3>📦 Produkthantering</h3>
            <p>Lägg till nya produkter, uppdatera priser och lagersaldo, eller ta bort produkter.</p>
            <a href="${pageContext.request.contextPath}/admin/products">Hantera produkter</a>
        </div>

        <div class="menu-card">
            <h3>📁 Kategorihantering</h3>
            <p>Skapa och hantera produktkategorier för bättre organisation.</p>
            <a href="${pageContext.request.contextPath}/admin/categories">Hantera kategorier</a>
        </div>
    </div>
</div>
</body>
</html>