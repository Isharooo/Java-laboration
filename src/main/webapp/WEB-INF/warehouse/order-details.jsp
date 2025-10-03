<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #${order.orderId} - Lager</title>
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
            max-width: 1000px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        .back-link:hover {
            text-decoration: underline;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .order-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #eee;
        }

        .order-info {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }

        .info-item {
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
        }

        .info-label {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
            text-transform: uppercase;
            font-weight: 600;
        }

        .info-value {
            font-size: 16px;
            color: #333;
            font-weight: 600;
        }

        .badge {
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 600;
        }

        .badge-pending {
            background: #ffc107;
            color: #333;
        }

        .badge-processing {
            background: #17a2b8;
            color: white;
        }

        .badge-shipped {
            background: #007bff;
            color: white;
        }

        .badge-delivered {
            background: #28a745;
            color: white;
        }

        .badge-cancelled {
            background: #dc3545;
            color: white;
        }

        table {
            width: 100%;
            margin-bottom: 20px;
        }

        thead {
            background: #f8f9fa;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            font-weight: 600;
            color: #555;
        }

        .total-row {
            font-weight: bold;
            font-size: 18px;
            color: #667eea;
            border-top: 2px solid #667eea;
        }

        .actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: transform 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        select {
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            margin-right: 10px;
            font-size: 14px;
        }
    </style>
</head>
<body>
<nav>
    <div class="nav-container">
        <div class="logo">Lagerhantering</div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/warehouse/orders">Ordrar</a>
            <a href="${pageContext.request.contextPath}/products">Kundvy</a>
            <span>${sessionScope.username}</span>
            <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
        </div>
    </div>
</nav>

<div class="container">
    <a href="${pageContext.request.contextPath}/warehouse/orders" class="back-link">
        ← Tillbaka till orderlista
    </a>

    <h1>Order #${order.orderId}</h1>

    <div class="order-card">
        <div class="order-header">
            <div>
                <h2>Orderdetaljer</h2>
            </div>
            <div>
                <c:choose>
                    <c:when test="${order.status == 'pending'}">
                        <span class="badge badge-pending">Opackad</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-delivered">Packad</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="order-info">
            <div class="info-item">
                <div class="info-label">Kund</div>
                <div class="info-value">${order.username}</div>
            </div>

            <div class="info-item">
                <div class="info-label">Orderdatum</div>
                <div class="info-value">
                    ${order.orderDate.toString().substring(0, 16).replace('T', ' ')}
                </div>
            </div>

            <div class="info-item">
                <div class="info-label">Order-ID</div>
                <div class="info-value">#${order.orderId}</div>
            </div>

            <div class="info-item">
                <div class="info-label">Totalt belopp</div>
                <div class="info-value">
                    <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                      currencySymbol="kr" maxFractionDigits="0"/>
                </div>
            </div>
        </div>

        <h3 style="margin-bottom: 15px;">Produkter att packa:</h3>

        <table>
            <thead>
            <tr>
                <th>Produkt</th>
                <th>Pris/st</th>
                <th>Antal</th>
                <th>Summa</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${order.orderItems}">
                <tr>
                    <td>${item.productName}</td>
                    <td>
                        <fmt:formatNumber value="${item.price}" type="currency"
                                          currencySymbol="kr" maxFractionDigits="0"/>
                    </td>
                    <td>${item.quantity} st</td>
                    <td>
                        <fmt:formatNumber value="${item.subtotal}" type="currency"
                                          currencySymbol="kr" maxFractionDigits="0"/>
                    </td>
                </tr>
            </c:forEach>
            <tr class="total-row">
                <td colspan="3">Totalt:</td>
                <td>
                    <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                      currencySymbol="kr" maxFractionDigits="0"/>
                </td>
            </tr>
            </tbody>
        </table>

        <div class="actions">
            <c:if test="${order.status == 'pending'}">
                <form action="${pageContext.request.contextPath}/warehouse/orders" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="updateStatus">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <input type="hidden" name="status" value="packed">
                    <button type="submit" class="btn btn-success">
                        Markera som packad
                    </button>
                </form>
            </c:if>

            <c:if test="${order.status == 'packed'}">
                <form action="${pageContext.request.contextPath}/warehouse/orders" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="updateStatus">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <input type="hidden" name="status" value="pending">
                    <button type="submit" class="btn btn-secondary">
                        Markera som opackad
                    </button>
                </form>
            </c:if>

            <a href="${pageContext.request.contextPath}/warehouse/orders" class="btn btn-secondary">
                Tillbaka
            </a>
        </div>
    </div>
</div>
</body>
</html>
