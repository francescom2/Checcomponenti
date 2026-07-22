<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checomponenti</title>
    
    <!-- CSS Globale gestito tramite contextPath -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header>
    <div class="logo">
        Checomponenti
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a>
        <a href="${pageContext.request.contextPath}/carrello">Carrello</a>
        <a href="${pageContext.request.contextPath}/login">Accedi</a>
        <a href="${pageContext.request.contextPath}/StoricoOrdini">Storico ordini</a>
        
    </nav>
</header>