<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="webshop.lab.se.javawebshop.bo.CartFacade" %>
<%@ page import="webshop.lab.se.javawebshop.ui.CartInfo" %>
<%
    CartFacade cartFacade = new CartFacade();
    CartInfo cartInfo = cartFacade.getCartInfo(session);
    request.setAttribute("cartInfo", cartInfo);
%>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kassa</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        .order-item {
            border: 1px solid black;
            padding: 15px;
            margin-bottom: 15px;
        }

        button {
            padding: 5px 10px;
            margin-top: 10px;
        }

        .error {
            background: #ffcccc;
            padding: 10px;
            margin: 10px 0;
        }

        hr {
            margin: 20px 0;
        }
    </style>
</head>
<body>
<h1>Slutför ditt köp</h1>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<c:forEach var="item" items="${cartInfo.items}">
    <div class="order-item">
        <h3>${item.product.name}</h3>
        <p><strong>Antal:</strong> ${item.quantity} st</p>
        <p><strong>Summa:</strong>
            <fmt:formatNumber value="${item.subtotal}" type="currency"
                              currencySymbol="kr" maxFractionDigits="0"/>
        </p>
    </div>
</c:forEach>

<hr>

<p><strong>Antal produkter:</strong> ${cartInfo.totalQuantity} st</p>
<p><strong>Totalt att betala:</strong>
    <fmt:formatNumber value="${cartInfo.totalPrice}" type="currency"
                      currencySymbol="kr" maxFractionDigits="0"/>
</p>

<form action="${pageContext.request.contextPath}/checkout" method="post">
    <a href="${pageContext.request.contextPath}/cart">
        <button type="button">Tillbaka till varukorg</button>
    </a>
    <button type="submit">Slutför köp</button>
</form>
</body>
</html>