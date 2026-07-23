<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registrazione - Checomponenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/registrazione.js" defer></script>
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <div class="auth-container">
            <h2>Crea un nuovo account</h2>

            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div class="alert-error"><%= error %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/registrazione" method="POST" id="regForm" onsubmit="return validaForm(event)">
                
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required autocomplete="username">
                    <small class="error-msg" id="usernameError"></small>
                </div>

                <div class="form-group">
                    <label for="email">E-mail:</label>
                    <input type="email" id="email" name="email" required autocomplete="email" onblur="verificaEmailAJAX()">
                    <small class="error-msg" id="emailError"></small>
                </div>

                <div class="form-group">
                    <label for="password">Password (min 6 caratteri):</label>
                    <input type="password" id="password" name="password" required autocomplete="new-password">
                    <small class="error-msg" id="passwordError"></small>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Conferma Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required autocomplete="new-password">
                    <small class="error-msg" id="confirmPasswordError"></small>
                </div>

                <button type="submit" class="btn" id="btnSubmit">Registrati</button>
            </form>

            <p class="auth-redirect-text">
                Hai già un account? <a href="${pageContext.request.contextPath}/login">Accedi qui</a>.
            </p>
        </div>
    </div>

    <%@ include file="fragments/footer.jsp" %>


</body>
</html>