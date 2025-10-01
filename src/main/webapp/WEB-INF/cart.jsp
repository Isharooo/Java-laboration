<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="webshop.lab.se.javawebshop.bo.Cart" %>
<%
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null) {
        cart = new Cart();
        session.setAttribute("cart", cart);
    }
    request.setAttribute("cart", cart);
%>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Varukorg - Webshop</title>
<%--    <style>--%>
<%--        * {--%>
<%--            margin: 0;--%>
<%--            padding: 0;--%>
<%--            box-sizing: border-box;--%>
<%--        }--%>

<%--        body {--%>
<%--            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;--%>
<%--            background: #f5f5f5;--%>
<%--        }--%>

<%--        /* Navigation */--%>
<%--        nav {--%>
<%--            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);--%>
<%--            color: white;--%>
<%--            padding: 15px 0;--%>
<%--            box-shadow: 0 2px 5px rgba(0,0,0,0.1);--%>
<%--        }--%>

<%--        .nav-container {--%>
<%--            max-width: 1200px;--%>
<%--            margin: 0 auto;--%>
<%--            display: flex;--%>
<%--            justify-content: space-between;--%>
<%--            align-items: center;--%>
<%--            padding: 0 20px;--%>
<%--        }--%>

<%--        .logo {--%>
<%--            font-size: 24px;--%>
<%--            font-weight: bold;--%>
<%--        }--%>

<%--        .nav-links {--%>
<%--            display: flex;--%>
<%--            gap: 20px;--%>
<%--            align-items: center;--%>
<%--        }--%>

<%--        .nav-links a {--%>
<%--            color: white;--%>
<%--            text-decoration: none;--%>
<%--            padding: 8px 15px;--%>
<%--            border-radius: 5px;--%>
<%--            transition: background 0.3s;--%>
<%--        }--%>

<%--        .nav-links a:hover {--%>
<%--            background: rgba(255,255,255,0.2);--%>
<%--        }--%>

<%--        .cart-badge {--%>
<%--            background: #ff4444;--%>
<%--            padding: 3px 8px;--%>
<%--            border-radius: 10px;--%>
<%--            font-size: 12px;--%>
<%--            font-weight: bold;--%>
<%--        }--%>

<%--        /* Container */--%>
<%--        .container {--%>
<%--            max-width: 1000px;--%>
<%--            margin: 30px auto;--%>
<%--            padding: 0 20px;--%>
<%--        }--%>

<%--        h1 {--%>
<%--            margin-bottom: 30px;--%>
<%--            color: #333;--%>
<%--        }--%>

<%--        /* Empty Cart */--%>
<%--        .empty-cart {--%>
<%--            background: white;--%>
<%--            padding: 60px 20px;--%>
<%--            border-radius: 10px;--%>
<%--            text-align: center;--%>
<%--            box-shadow: 0 2px 10px rgba(0,0,0,0.1);--%>
<%--        }--%>

<%--        .empty-cart h2 {--%>
<%--            margin-bottom: 10px;--%>
<%--            color: #666;--%>
<%--        }--%>

<%--        .empty-cart a {--%>
<%--            display: inline-block;--%>
<%--            margin-top: 20px;--%>
<%--            padding: 12px 30px;--%>
<%--            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);--%>
<%--            color: white;--%>
<%--            text-decoration: none;--%>
<%--            border-radius: 5px;--%>
<%--            font-weight: 600;--%>
<%--        }--%>

<%--        /* Cart Items */--%>
<%--        .cart-items {--%>
<%--            background: white;--%>
<%--            border-radius: 10px;--%>
<%--            padding: 20px;--%>
<%--            box-shadow: 0 2px 10px rgba(0,0,0,0.1);--%>
<%--            margin-bottom: 20px;--%>
<%--        }--%>

<%--        .cart-item {--%>
<%--            display: flex;--%>
<%--            gap: 20px;--%>
<%--            padding: 20px 0;--%>
<%--            border-bottom: 1px solid #eee;--%>
<%--        }--%>

<%--        .cart-item:last-child {--%>
<%--            border-bottom: none;--%>
<%--        }--%>

<%--        .item-image {--%>
<%--            width: 100px;--%>
<%--            height: 100px;--%>
<%--            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);--%>
<%--            border-radius: 10px;--%>
<%--            display: flex;--%>
<%--            align-items: center;--%>
<%--            justify-content: center;--%>
<%--            color: white;--%>
<%--            font-size: 36px;--%>
<%--            flex-shrink: 0;--%>
<%--        }--%>

<%--        .item-details {--%>
<%--            flex: 1;--%>
<%--        }--%>

<%--        .item-name {--%>
<%--            font-size: 18px;--%>
<%--            font-weight: 600;--%>
<%--            margin-bottom: 5px;--%>
<%--            color: #333;--%>
<%--        }--%>

<%--        .item-category {--%>
<%--            font-size: 14px;--%>
<%--            color: #667eea;--%>
<%--            margin-bottom: 10px;--%>
<%--        }--%>

<%--        .item-price {--%>
<%--            font-size: 16px;--%>
<%--            color: #666;--%>
<%--        }--%>

<%--        .item-actions {--%>
<%--            display: flex;--%>
<%--            flex-direction: column;--%>
<%--            justify-content: space-between;--%>
<%--            align-items: flex-end;--%>
<%--        }--%>

<%--        .quantity-control {--%>
<%--            display: flex;--%>
<%--            align-items: center;--%>
<%--            gap: 10px;--%>
<%--            margin-bottom: 10px;--%>
<%--        }--%>

<%--        .quantity-control input {--%>
<%--            width: 60px;--%>
<%--            padding: 5px;--%>
<%--            text-align: center;--%>
<%--            border: 2px solid #e0e0e0;--%>
<%--            border-radius: 5px;--%>
<%--        }--%>

<%--        .update-btn, .remove-btn {--%>
<%--            padding: 6px 12px;--%>
<%--            border: none;--%>
<%--            border-radius: 5px;--%>
<%--            cursor: pointer;--%>
<%--            font-size: 14px;--%>
<%--            font-weight: 600;--%>
<%--        }--%>

<%--        .update-btn {--%>
<%--            background: #667eea;--%>
<%--            color: white;--%>
<%--        }--%>

<%--        .remove-btn {--%>
<%--            background: #ff4444;--%>
<%--            color: white;--%>
<%--        }--%>

<%--        .item-subtotal {--%>
<%--            font-size: 20px;--%>
<%--            font-weight: bold;--%>
<%--            color: #667eea;--%>
<%--        }--%>

<%--        /* Cart Summary */--%>
<%--        .cart-summary {--%>
<%--            background: white;--%>
<%--            border-radius: 10px;--%>
<%--            padding: 25px;--%>
<%--            box-shadow: 0 2px 10px rgba(0,0,0,0.1);--%>
<%--        }--%>

<%--        .summary-row {--%>
<%--            display: flex;--%>
<%--            justify-content: space-between;--%>
<%--            padding: 10px 0;--%>
<%--            font-size: 16px;--%>
<%--        }--%>

<%--        .summary-row.total {--%>
<%--            border-top: 2px solid #eee;--%>
<%--            margin-top: 10px;--%>
<%--            padding-top: 20px;--%>
<%--            font-size: 24px;--%>
<%--            font-weight: bold;--%>
<%--            color: #667eea;--%>
<%--        }--%>

<%--        .action-buttons {--%>
<%--            display: flex;--%>
<%--            gap: 15px;--%>
<%--            margin-top: 20px;--%>
<%--        }--%>

<%--        .btn {--%>
<%--            flex: 1;--%>
<%--            padding: 15px;--%>
<%--            border: none;--%>
<%--            border-radius: 5px;--%>
<%--            font-size: 16px;--%>
<%--            font-weight: 600;--%>
<%--            cursor: pointer;--%>
<%--            transition: transform 0.2s;--%>
<%--        }--%>

<%--        .btn:hover {--%>
<%--            transform: translateY(-2px);--%>
<%--        }--%>

<%--        .btn-primary {--%>
<%--            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);--%>
<%--            color: white;--%>
<%--        }--%>

<%--        .btn-secondary {--%>
<%--            background: #f5f5f5;--%>
<%--            color: #666;--%>
<%--            border: 2px solid #e0e0e0;--%>
<%--        }--%>
<%--    </style>--%>
</head>
<body>
<!-- Navigation -->
<nav>
    <div class="nav-container">
        <div class="logo">Webshop</div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/products">Produkter</a>
            <a href="${pageContext.request.contextPath}/cart">
                Varukorg
                <c:if test="${cart.totalQuantity > 0}">
                    <span class="cart-badge">${cart.totalQuantity}</span>
                </c:if>
            </a>
            <span>${sessionScope.username}</span>
            <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
        </div>
    </div>
</nav>

<!-- Content -->
<div class="container">
    <h1>Din varukorg</h1>

    <c:choose>
        <c:when test="${cart.isEmpty()}">
            <div class="empty-cart">
                <h2>Din varukorg är tom</h2>
                <p>Börja handla för att lägga till produkter i varukorgen!</p>
                <a href="${pageContext.request.contextPath}/products">Visa produkter</a>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Cart Items -->
            <div class="cart-items">
                <c:forEach var="entry" items="${cart.items}">
                    <c:set var="item" value="${entry.value}"/>
                    <c:set var="product" value="${item.product}"/>

                    <div class="cart-item">

                        <div class="item-details">
                            <div class="item-name">${product.name}</div>
                            <div class="item-category">${product.categoryName}</div>
                            <div class="item-price">
                                <fmt:formatNumber value="${product.price}" type="currency"
                                                  currencySymbol="kr" maxFractionDigits="0"/> / st
                            </div>
                        </div>

                        <div class="item-actions">
                            <div class="quantity-control">
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="productId" value="${product.productId}">
                                    <input type="number" name="quantity" value="${item.quantity}"
                                           min="1" max="${product.stock}">
                                    <button type="submit" class="update-btn">Uppdatera</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${product.productId}">
                                    <button type="submit" class="remove-btn">Ta bort</button>
                                </form>
                            </div>

                            <div class="item-subtotal">
                                <fmt:formatNumber value="${item.subtotal}" type="currency"
                                                  currencySymbol="kr" maxFractionDigits="0"/>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Cart Summary -->
            <div class="cart-summary">
                <div class="summary-row">
                    <span>Antal produkter:</span>
                    <span>${cart.totalQuantity} st</span>
                </div>

                <div class="summary-row total">
                    <span>Totalt:</span>
                    <span>
                            <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                              currencySymbol="kr" maxFractionDigits="0"/>
                        </span>
                </div>

                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">
                        Fortsätt handla
                    </a>

                    <form action="${pageContext.request.contextPath}/cart" method="post" style="flex: 1;">
                        <input type="hidden" name="action" value="clear">
                        <button type="submit" class="btn btn-secondary"
                                onclick="return confirm('Är du säker på att du vill tömma varukorgen?')">
                            Töm varukorg
                        </button>
                    </form>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary">
                        Gå till kassan
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
