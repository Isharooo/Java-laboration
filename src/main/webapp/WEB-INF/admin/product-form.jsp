<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty requestScope.product ? 'Ny produkt' : 'Redigera produkt'} - Admin</title>
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
            max-width: 1400px;
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
            max-width: 600px;
            margin: 30px auto;
            padding: 0 20px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .form-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #555;
        }

        input[type="text"],
        input[type="number"],
        textarea,
        select {
            width: 100%;
            padding: 10px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
            font-family: inherit;
        }

        textarea {
            min-height: 100px;
            resize: vertical;
        }

        input:focus,
        textarea:focus,
        select:focus {
            outline: none;
            border-color: #667eea;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }

        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .help-text {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<nav>
    <div class="nav-container">
        <div class="logo">Admin Panel</div>
    </div>
</nav>

<div class="container">
    <h1>${empty requestScope.product ? 'Ny produkt' : 'Redigera produkt'}</h1>

    <div class="form-card">
        <c:if test="${not empty error}">
            <div class="alert-error">${error}</div>
        </c:if>

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
                <label for="description">Beskrivning</label>
                <textarea id="description" name="description">${requestScope.product.description}</textarea>
            </div>

            <div class="form-group">
                <label for="price">Pris (kr) *</label>
                <input type="number" id="price" name="price"
                       value="${requestScope.product.price}"
                       step="0.01" min="0" required>
                <div class="help-text">Ange pris i kronor (t.ex. 299.99)</div>
            </div>

            <div class="form-group">
                <label for="stock">Lagersaldo *</label>
                <input type="number" id="stock" name="stock"
                       value="${requestScope.product.stock}"
                       min="0" required>
                <div class="help-text">Antal produkter i lager</div>
            </div>

            <div class="form-group">
                <label for="imageUrl">Bild-URL</label>
                <input type="text" id="imageUrl" name="imageUrl"
                       value="${requestScope.product.imageUrl}">
                <div class="help-text">URL till produktbild (valfritt)</div>
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">
                    Avbryt
                </a>
                <button type="submit" class="btn btn-primary">
                    ${empty requestScope.product ? 'Skapa' : 'Uppdatera'}
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>