<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty requestScope.product ? 'Ny produkt' : 'Redigera produkt'}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="number"],
        select {
            width: 300px;
            padding: 5px;
        }

        button {
            padding: 8px 15px;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<h1>${empty requestScope.product ? 'Ny produkt' : 'Redigera produkt'}</h1>

<form action="${pageContext.request.contextPath}/admin/products" method="post">
    <input type="hidden" name="action" value="${empty requestScope.product ? 'create' : 'update'}">
    <c:if test="${not empty requestScope.product}">
        <input type="hidden" name="productId" value="${requestScope.product.productId}">
    </c:if>

    <div class="form-group">
        <label for="name">Produktnamn *</label>
        <input type="text" id="name" name="name"
               value="${requestScope.product.name}" required>
    </div>

    <div class="form-group">
        <label for="categoryId">Kategori *</label>
        <select id="categoryId" name="categoryId" required>
            <option value="">Välj kategori...</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat.categoryId}"
                    ${requestScope.product.categoryId == cat.categoryId ? 'selected' : ''}>
                        ${cat.name}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="price">Pris (kr) *</label>
        <input type="number" id="price" name="price"
               value="${requestScope.product.price}"
               step="0.01" min="0" required>
    </div>

    <div class="form-group">
        <label for="stock">Lagersaldo *</label>
        <input type="number" id="stock" name="stock"
               value="${requestScope.product.stock}"
               min="0" required>
    </div>

    <a href="${pageContext.request.contextPath}/admin/products">
        <button type="button">Avbryt</button>
    </a>
    <button type="submit">
        ${empty requestScope.product ? 'Skapa' : 'Uppdatera'}
    </button>
</form>
</body>
</html>