<%@ page pageEncoding="UTF-8" isErrorPage="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>404 - Pagina non trovata</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/errors.css">
</head>
<body>


    <main class="container error-container">
        <h1 class="error-code error-404">:( </h1>
        <h2>Pagina non trovata - Errore 404</h2>
        <p class="error-message">
            La pagina che stai cercando potrebbe essere stata rimossa o non è mai esistita.
        </p>
        <a href="${pageContext.request.contextPath}/catalogo" class="btn-save btn-error-return">
            Torna al Catalogo
        </a>
    </main>


</body>
</html>