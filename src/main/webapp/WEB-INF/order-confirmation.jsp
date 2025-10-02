<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orderbekräftelse - Webshop</title>
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
            margin: 50px auto;
            padding: 0 20px;
        }

        .confirmation-card {
            background: white;
            border-radius: 10px;
            padding: 40px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }

        .success-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }

        h1 {
            color: #28a745;
            margin-bottom: 10px;
        }

        .order-number {
            font-size: 18px;
            color: #666;
            margin-bottom: 30px;
        }

        .order-details {
            text-align: left;
            margin: 30px 0;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 5px;
        }

        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .detail-row:last-child {
            border-bottom: none;
        }

        .detail-label {
            font-weight: 600;
            color: #555;
        }

        .detail-value {
            color: #333;
        }

        .order-items {
            margin: 20px 0;
        }

        .item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .total-row {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            font-size: 20px;
            font-weight: bold;
            color: #667eea;
            border-top: 2px solid #e0e0e0;
            margin-top: 10px;
        }

        .btn {
            display: inline-block;
            padding: 15px 40px;
            margin: 10px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: 600;
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
        <div class="logo">Webshop</div>
    </div>
</nav>

<div class="container">
    <div class="confirmation-card">
        <div class="success-icon">✓</div>
        <h1>Tack för din beställning!</h1>
        <c:if test="${not empty order}">
            <p class="order-number">Ordernummer: #${order.orderId}</p>
        </c:if>

        <p style="color: #666; margin-bottom: 30px;">
            Din order har tagits emot och behandlas nu. En bekräftelse har skickats till din e-post.
        </p>

        <c:if test="${not empty order}">
            <div class="order-details">
                <div class="detail-row">
                    <span class="detail-label">Status:</span>
                    <span class="detail-value">${order.status}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Orderdatum:</span>
                    <span class="detail-value">
                            ${order.orderDate.toString().substring(0, 16).replace('T', ' ')}
                        </span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Kund:</span>
                    <span class="detail-value">${order.username}</span>
                </div>

                <div class="order-items">
                    <h3 style="margin: 20px 0 10px 0;">Beställda produkter:</h3>
                    <c:forEach var="item" items="${order.orderItems}">
                        <div class="item">
                            <span>${item.productName} × ${item.quantity}</span>
                            <span>
                                    <fmt:formatNumber value="${item.subtotal}" type="currency"
                                                      currencySymbol="kr" maxFractionDigits="0"/>
                                </span>
                        </div>
                    </c:forEach>

                    <div class="total-row">
                        <span>Totalt:</span>
                        <span>
                                <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                                  currencySymbol="kr" maxFractionDigits="0"/>
                            </span>
                    </div>
                </div>
            </div>
        </c:if>

        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                Fortsätt handla
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">
                Logga ut
            </a>
        </div>
    </div>
</div>
</body>
</html>