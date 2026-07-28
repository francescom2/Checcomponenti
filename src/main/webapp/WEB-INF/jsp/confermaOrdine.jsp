<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Ordine Confermato - Checomponenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
	    <div class="confirmation-card">
	    
	        <h1 class="confirmation-title"> Ordine Confermato!</h1>
	        <p>Grazie per il tuo acquisto su <strong>Checomponenti</strong>.</p>
	        <p>Il tuo numero d'ordine è: <strong>#<%= request.getAttribute("idOrdine") %></strong></p>
	
	        <br>
	        <a href="${pageContext.request.contextPath}/catalogo" class="btn">Torna al Catalogo</a>
	    </div>
	</div>
    <%@ include file="fragments/footer.jsp" %>

</body>
</html>