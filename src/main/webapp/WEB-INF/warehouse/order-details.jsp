<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #${order.orderId}</title>
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

        .order-info {
            border: 1px solid black;
            padding: 15px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>Order #${order.orderId}</h1>

<div class="order-info">
    <p><strong>Kund:</strong> ${order.username}</p>
    <p><strong>Status:</strong>
        <c:choose>
            <c:when test="${order.status == 'pending'}">
                Opackad
            </c:when>
            <c:otherwise>
                Packad
            </c:otherwise>
        </c:choose>
    </p>
    <p><strong>Totalt belopp:</strong>
        <fmt:formatNumber value="${order.totalAmount}" type="currency" maxFractionDigits="0"/>
    </p>
</div>

<h3>Produkter att packa:</h3>

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
                <fmt:formatNumber value="${item.price}" type="currency" maxFractionDigits="0"/>
            </td>
            <td>${item.quantity} st</td>
            <td>
                <fmt:formatNumber value="${item.subtotal}" type="currency" maxFractionDigits="0"/>
            </td>
        </tr>
    </c:forEach>
    <tr style="font-weight: bold;">
        <td colspan="3">Totalt:</td>
        <td>
            <fmt:formatNumber value="${order.totalAmount}" type="currency" maxFractionDigits="0"/>
        </td>
    </tr>
    </tbody>
</table>

<div style="margin-top: 20px;">
    <c:if test="${order.status == 'pending'}">
        <form action="${pageContext.request.contextPath}/warehouse/orders" method="post" style="display: inline;">
            <input type="hidden" name="action" value="updateStatus">
            <input type="hidden" name="orderId" value="${order.orderId}">
            <input type="hidden" name="status" value="packed">
            <button type="submit">Markera som packad</button>
        </form>
    </c:if>

    <c:if test="${order.status == 'packed'}">
        <form action="${pageContext.request.contextPath}/warehouse/orders" method="post" style="display: inline;">
            <input type="hidden" name="action" value="updateStatus">
            <input type="hidden" name="orderId" value="${order.orderId}">
            <input type="hidden" name="status" value="pending">
            <button type="submit">Markera som opackad</button>
        </form>
    </c:if>

    <a href="${pageContext.request.contextPath}/warehouse/orders">
        <button>Tillbaka</button>
    </a>
</div>
</body>
</html>