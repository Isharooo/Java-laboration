<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="webshop.lab.se.javawebshop.bo.Cart" %>
<%
    Cart cart = (Cart) session.getAttribute("cart");
    request.setAttribute("cart", cart);
%>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kassa - Webshop</title>
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
            max-width: 1200px;
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
            max-width: 800px;
            margin: 30px auto;
            padding: 0 20px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .checkout-card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .section-title {
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 20px;
            color: #333;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }

        .order-item {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }

        .order-item:last-child {
            border-bottom: none;
        }

        .item-info {
            flex: 1;
        }

        .item-name {
            font-weight: 600;
            margin-bottom: 5px;
        }

        .item-details {
            font-size: 14px;
            color: #666;
        }

        .item-price {
            font-weight: 600;
            color: #667eea;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            font-size: 16px;
        }

        .summary-row.total {
            border-top: 2px solid #eee;
            margin-top: 10px;
            padding-top: 20px;
            font-size: 24px;
            font-weight: bold;
            color: #667eea;
        }

        .info-box {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
            border-left: 4px solid #667eea;
        }

        .info-box p {
            margin: 5px 0;
            color: #555;
        }

        .error {
            background: #fee;
            border: 1px solid #fcc;
            color: #c33;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }

        .btn {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
            transition: transform 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-secondary {
            background: #f5f5f5;
            color: #666;
            border: 2px solid #e0e0e0;
        }
    </style>
</head>
<body>
<nav>
    <div class="nav-container">
        <div class="logo">Webshop - Kassa</div>
    </div>
</nav>

<div class="container">
    <h1>Slutför ditt köp</h1>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <!-- Order Summary -->
    <div class="checkout-card">
        <div class="section-title">Din beställning</div>

        <c:forEach var="entry" items="${cart.items}">
            <c:set var="item" value="${entry.value}"/>
            <c:set var="product" value="${item.product}"/>

            <div class="order-item">
                <div class="item-info">
                    <div class="item-name">${product.name}</div>
                    <div class="item-details">
                        Antal: ${item.quantity} ×
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="kr" maxFractionDigits="0"/>
                    </div>
                </div>
                <div class="item-price">
                    <fmt:formatNumber value="${item.subtotal}" type="currency"
                                      currencySymbol="kr" maxFractionDigits="0"/>
                </div>
            </div>
        </c:forEach>

        <div class="summary-row">
            <span>Antal produkter:</span>
            <span>${cart.totalQuantity} st</span>
        </div>

        <div class="summary-row total">
            <span>Totalt att betala:</span>
            <span>
                    <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                      currencySymbol="kr" maxFractionDigits="0"/>
                </span>
        </div>
    </div>

    <!-- Customer Info -->
    <div class="checkout-card">
        <div class="section-title">Leveransinformation</div>
        <div class="info-box">
            <p><strong>Kund:</strong> ${sessionScope.username}</p>
            <p><strong>Orderstatus:</strong> Pending (Bearbetas)</p>
            <p><strong>Betalning:</strong> Faktura (30 dagar)</p>
        </div>
        <p style="color: #666; font-size: 14px; margin-top: 15px;">
            Genom att slutföra köpet godkänner du våra allmänna villkor.
            Ordern kommer att behandlas och lagersaldot uppdateras automatiskt.
        </p>
    </div>

    <!-- Actions -->
    <form action="${pageContext.request.contextPath}/checkout" method="post">
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">
                Tillbaka till varukorg
            </a>
            <button type="submit" class="btn btn-primary">
                Slutför köp
            </button>
        </div>
    </form>
</div>
</body>
</html>