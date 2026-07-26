<%@ page pageEncoding="UTF-8" isErrorPage="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>500 - Errore Server</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/errors.css">
</head>
<body>


    <main class="container error-container">
        <h1 class="error-code error-500">:( </h1>
        <h2>Errore nel server - Errore 500</h2>
        <p class="error-message">
            Si è verificato un errore imprevisto nel Server. <br>
            È incredibile come abbia funzionato così bene fino ad adesso, <br>
            grazie Tomcat.
        </p>
        <a href="${pageContext.request.contextPath}/catalogo" class="btn-save btn-error-return">
            Torna al Catalogo
        </a>
    </main>


</body>
</html>