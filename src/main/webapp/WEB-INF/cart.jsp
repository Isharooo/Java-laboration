<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Varukorg</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        nav a {
            margin-right: 15px;
        }

        .cart-item {
            border: 1px solid black;
            padding: 10px;
            margin-bottom: 15px;
        }

        button {
            padding: 5px 10px;
            margin-top: 10px;
        }

        hr {
            margin: 20px 0;
        }
    </style>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/products">Produkter</a>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>Din varukorg</h1>

<c:choose>
    <c:when test="${cartInfo.isEmpty()}">
        <p>Din varukorg är tom</p>
        <a href="${pageContext.request.contextPath}/products">Börja handla</a>
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${cartInfo.items}">
            <div class="cart-item">
                <h3>${item.product.name}</h3>
                <p><strong>Kategori:</strong> ${item.product.categoryName}</p>
                <p><strong>Pris:</strong>
                    <fmt:formatNumber value="${item.product.price}" type="currency"
                                      currencySymbol="kr" maxFractionDigits="0"/> / st
                </p>
                <p><strong>Antal:</strong> ${item.quantity} st</p>
                <p><strong>Summa:</strong>
                    <fmt:formatNumber value="${item.subtotal}" type="currency"
                                      currencySymbol="kr" maxFractionDigits="0"/>
                </p>

                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="remove">
                    <input type="hidden" name="productId" value="${item.product.productId}">
                    <button type="submit">Ta bort</button>
                </form>
            </div>
        </c:forEach>

        <hr>

        <p><strong>Antal produkter:</strong> ${cartInfo.totalQuantity} st</p>
        <p><strong>Totalt:</strong>
            <fmt:formatNumber value="${cartInfo.totalPrice}" type="currency"
                              currencySymbol="kr" maxFractionDigits="0"/>
        </p>

        <a href="${pageContext.request.contextPath}/checkout">
            <button>Gå till kassan</button>
        </a>
    </c:otherwise>
</c:choose>
</body>
</html>