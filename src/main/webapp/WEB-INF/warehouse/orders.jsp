<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orderhantering</title>
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
    </style>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/logout">Logga ut</a>
</nav>

<h1>Orderhantering</h1>

<table>
    <thead>
    <tr>
        <th>Order ID</th>
        <th>Kund</th>
        <th>Totalt</th>
        <th>Status</th>
        <th>Åtgärder</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${empty orders}">
            <tr>
                <td colspan="5" style="text-align: center; padding: 20px;">
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
                        <fmt:formatNumber value="${order.totalAmount}" type="currency"
                                          currencySymbol="kr" maxFractionDigits="0"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${order.status == 'pending'}">
                                Opackad
                            </c:when>
                            <c:otherwise>
                                Packad
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/warehouse/orders?action=view&id=${order.orderId}">
                            <button>Visa detaljer</button>
                        </a>

                        <c:if test="${order.status == 'pending'}">
                            <form action="${pageContext.request.contextPath}/warehouse/orders"
                                  method="post" style="display: inline;">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <input type="hidden" name="status" value="packed">
                                <button type="submit">Markera packad</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>
</body>
</html>