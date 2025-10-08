<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="webshop.lab.se.javawebshop.bo.CartFacade" %>
<%@ page import="webshop.lab.se.javawebshop.ui.CartInfo" %>
<%
    CartFacade cartFacade = new CartFacade();
    CartInfo navCartInfo = cartFacade.getCartInfo(session);
    int cartCount = navCartInfo.getTotalQuantity();
%>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Produkter</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        nav a {
            margin-right: 15px;
        }

        .product-card {
            border: 1px solid black;
            padding: 10px;
            margin-bottom: 20px;
        }

        button {
            padding: 5px 10px;
            margin-top: 10px;
        }

        .category-filter {
            margin: 20px 0;
        }

    </style>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/products">Produkter</a>
    <a href="${pageContext.request.contextPath}/cart">Varukorg (<%= cartCount %>)</a>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>
    <c:choose>
        <c:when test="${not empty selectedCategory}">
            ${selectedCategory.name}
        </c:when>
        <c:otherwise>
            Produkter
        </c:otherwise>
    </c:choose>
</h1>

<div class="category-filter">
    <strong>Kategorier:</strong>
    <a href="${pageContext.request.contextPath}/products">Alla</a>
    <c:forEach var="cat" items="${categories}">
        <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}">
                ${cat.name}
        </a>
    </c:forEach>
</div>

<c:choose>
    <c:when test="${empty products}">
        <p>Inga produkter hittades.</p>
    </c:when>
    <c:otherwise>
        <c:forEach var="product" items="${products}">
            <div class="product-card">
                <h3>${product.name}</h3>
                <p><strong>Kategori:</strong> ${product.categoryName}</p>
                <p><strong>Pris:</strong>
                    <fmt:formatNumber value="${product.price}" type="currency" maxFractionDigits="0"/>
                </p>
                <p><strong>Lager:</strong> ${product.stock} st</p>

                <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${product.productId}">
                    <button type="submit" ${product.stock <= 0 ? 'disabled' : ''}>
                        Lägg i varukorg
                    </button>
                </form>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>
</body>
</html>