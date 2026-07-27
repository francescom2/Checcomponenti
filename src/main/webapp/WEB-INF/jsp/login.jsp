<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Accedi - Checomponenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <div class="auth-container">
            <h2>Accedi al tuo account</h2>

            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div class="alert-error"><%= error %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="POST">
                
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="username" id="username" name="username" required autocomplete="text">
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required autocomplete="current-password">
                </div>

                <button type="submit" class="btn">Accedi</button>
            </form>

            <p class="auth-redirect-text">
                Non hai ancora un account? <a href="${pageContext.request.contextPath}/registrazione">Registrati qui</a>.
            </p>
        </div>
    </div>

    <%@ include file="fragments/footer.jsp" %>

</body>
</html>