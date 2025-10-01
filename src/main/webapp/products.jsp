<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Produkter - Webshop</title>
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

        /* Navigation */
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

        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        /* Category Filter */
        .category-filter {
            max-width: 1200px;
            margin: 20px auto;
            padding: 0 20px;
        }

        .category-buttons {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .category-btn {
            padding: 10px 20px;
            background: white;
            border: 2px solid #667eea;
            color: #667eea;
            border-radius: 25px;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.3s;
            font-weight: 500;
        }

        .category-btn:hover,
        .category-btn.active {
            background: #667eea;
            color: white;
        }

        /* Products Grid */
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .products-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 25px;
        }

        .product-card {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0,0,0,0.15);
        }

        .product-image {
            width: 100%;
            height: 200px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 48px;
        }

        .product-info {
            padding: 20px;
        }

        .product-category {
            font-size: 12px;
            color: #667eea;
            font-weight: 600;
            text-transform: uppercase;
            margin-bottom: 5px;
        }

        .product-name {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 10px;
            color: #333;
        }

        .product-description {
            font-size: 14px;
            color: #666;
            margin-bottom: 15px;
            line-height: 1.4;
        }

        .product-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 15px;
        }

        .product-price {
            font-size: 24px;
            font-weight: bold;
            color: #667eea;
        }

        .stock-info {
            font-size: 12px;
            padding: 5px 10px;
            border-radius: 15px;
            font-weight: 600;
        }

        .in-stock {
            background: #d4edda;
            color: #155724;
        }

        .out-of-stock {
            background: #f8d7da;
            color: #721c24;
        }

        .add-to-cart {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            margin-top: 10px;
            transition: transform 0.2s;
        }

        .add-to-cart:hover:not(:disabled) {
            transform: scale(1.05);
        }

        .add-to-cart:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }

        .empty-state h2 {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<!-- Navigation -->
<nav>
    <div class="nav-container">
        <div class="logo">🛒 Webshop</div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/products">Produkter</a>
            <a href="${pageContext.request.contextPath}/cart">Varukorg (0)</a>
            <div class="user-info">
                <span>👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
            </div>
        </div>
    </div>
</nav>

<!-- Category Filter -->
<div class="category-filter">
    <div class="category-buttons">
        <a href="${pageContext.request.contextPath}/products"
           class="category-btn ${empty param.category ? 'active' : ''}">
            Alla produkter
        </a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}"
               class="category-btn ${param.category == cat.categoryId ? 'active' : ''}">
                    ${cat.name}
            </a>
        </c:forEach>
    </div>
</div>

<!-- Products -->
<div class="container">
    <h1>
        <c:choose>
            <c:when test="${not empty selectedCategory}">
                ${selectedCategory.name}
            </c:when>
            <c:otherwise>
                Alla produkter
            </c:otherwise>
        </c:choose>
    </h1>

    <c:choose>
        <c:when test="${empty products}">
            <div class="empty-state">
                <h2>Inga produkter hittades</h2>
                <p>Det finns inga produkter i denna kategori just nu.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="products-grid">
                <c:forEach var="product" items="${products}">
                    <div class="product-card">
                        <div class="product-image">
                            📦
                        </div>
                        <div class="product-info">
                            <div class="product-category">${product.categoryName}</div>
                            <div class="product-name">${product.name}</div>
                            <div class="product-description">${product.description}</div>

                            <div class="product-footer">
                                <div class="product-price">
                                    <fmt:formatNumber value="${product.price}" type="currency"
                                                      currencySymbol="kr" maxFractionDigits="0"/>
                                </div>
                                <c:choose>
                                    <c:when test="${product.stock > 0}">
                                            <span class="stock-info in-stock">
                                                ${product.stock} i lager
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                            <span class="stock-info out-of-stock">
                                                Slut i lager
                                            </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <form action="${pageContext.request.contextPath}/cart" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <button type="submit" class="add-to-cart"
                                    ${product.stock <= 0 ? 'disabled' : ''}>
                                    Lägg i varukorg
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>