<%@ page pageEncoding="UTF-8" isErrorPage="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>403 - PROIBITO</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/errors.css">
</head>
<body>

    <main class="container error-container">
        <h1 class="error-code error-403">:( </h1>
        <h2>Proibito - Errore 403</h2>
        <p class="error-message">
            Non hai i permessi per accedere alla pagina.
        </p>
        <a href="${pageContext.request.contextPath}/catalogo" class="btn-save btn-error-return">
            Torna al Catalogo
        </a>
    </main>



</body>
</html>