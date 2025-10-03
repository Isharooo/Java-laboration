<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orderhantering - Lager</title>
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

        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .filters {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .filter-buttons {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .filter-btn {
            padding: 10px 20px;
            border: 2px solid #667eea;
            background: white;
            color: #667eea;
            border-radius: 25px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s;
        }

        .filter-btn:hover,
        .filter-btn.active {
            background: #667eea;
            color: white;
        }

        table {
            width: 100%;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        th, td {
            padding: 15px;
            text-align: left;
        }

        tbody tr {
            border-bottom: 1px solid #eee;
        }

        tbody tr:hover {
            background: #f8f9fa;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
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

        .btn {
            display: inline-block;
            padding: 8px 15px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: 600;
            transition: transform 0.2s;
            border: none;
            cursor: pointer;
            font-size: 14px;
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

        select {
            padding: 8px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            margin-right: 10px;
        }

        .action-form {
            display: flex;
            align-items: center;
            gap: 10px;
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
    <h1>Orderhantering</h1>

    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <div class="filters">
        <h3 style="margin-bottom: 15px;">Filtrera efter status:</h3>
        <div class="filter-buttons">
            <a href="${pageContext.request.contextPath}/warehouse/orders"
               class="filter-btn ${currentStatus == 'all' ? 'active' : ''}">
                Alla ordrar
            </a>
            <a href="${pageContext.request.contextPath}/warehouse/orders?status=pending"
               class="filter-btn ${currentStatus == 'pending' ? 'active' : ''}">
                Opackade
            </a>
            <a href="${pageContext.request.contextPath}/warehouse/orders?status=packed"
               class="filter-btn ${currentStatus == 'packed' ? 'active' : ''}">
                Packade
            </a>
        </div>
    </div>

    <table>
        <thead>
        <tr>
            <th>Order ID</th>
            <th>Kund</th>
            <th>Datum</th>
            <th>Totalt</th>
            <th>Status</th>
            <th>Åtgärder</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty orders}">
                <tr>
                    <td colspan="6" style="text-align: center; padding: 40px; color: #666;">
                        Inga ordrar att visa
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>#${order.orderId}</td>
                        <td>${order.username}</td>
                        <td>
                                ${order.orderDate.toString().substring(0, 16).replace('T', ' ')}
                        </td>
                        <td>
                            <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                              currencySymbol="kr" maxFractionDigits="0"/>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${order.status == 'pending'}">
                                    <span class="badge badge-pending">Opackad</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-delivered">Packad</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/warehouse/orders?action=view&id=${order.orderId}"
                               class="btn btn-primary">
                                Visa detaljer
                            </a>

                            <c:if test="${order.status == 'pending'}">
                                <form action="${pageContext.request.contextPath}/warehouse/orders"
                                      method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="updateStatus">
                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                    <input type="hidden" name="status" value="packed">
                                    <button type="submit" class="btn btn-success">
                                        Markera packad
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
</body>
</html>
