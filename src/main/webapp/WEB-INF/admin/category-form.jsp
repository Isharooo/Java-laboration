<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty requestScope.category ? 'Ny kategori' : 'Redigera kategori'}</title>
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

        input[type="text"] {
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
<h1>${empty requestScope.category ? 'Ny kategori' : 'Redigera kategori'}</h1>

<form action="${pageContext.request.contextPath}/admin/categories" method="post">
    <input type="hidden" name="action" value="${empty requestScope.category ? 'create' : 'update'}">
    <c:if test="${not empty requestScope.category}">
        <input type="hidden" name="categoryId" value="${requestScope.category.categoryId}">
    </c:if>

    <div class="form-group">
        <label for="name">Kategorinamn *</label>
        <input type="text" id="name" name="name"
               value="${requestScope.category.name}" required>
    </div>

    <a href="${pageContext.request.contextPath}/admin/categories">
        <button type="button">Avbryt</button>
    </a>
    <button type="submit">
        ${empty requestScope.category ? 'Skapa' : 'Uppdatera'}
    </button>
</form>
</body>
</html>