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

<%@ page import ="model.UtenteBean" %>
<% UtenteBean utente = (UtenteBean) session.getAttribute("utente"); %>

<header>
    <div class="logo">
        Checomponenti
    </div>
    <nav>
    
        <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a>
        <a href="${pageContext.request.contextPath}/carrello">Carrello</a>
        
        <% if (utente != null) { %>
        	
        	${utente.getUsername()}
        
			<a href="${pageContext.request.contextPath}/StoricoOrdini">Storico Ordini</a>
			<a href="${pageContext.request.contextPath}/logout">Logout</a>
			                	
        <% } else { %>
       		 <a href="${pageContext.request.contextPath}/login" class="btn-header btn-login"> Accedi </a>
             
             <a href="${pageContext.request.contextPath}/registrazione" class="btn-header btn-register"> Registrati </a>
             
        <%} %>
        
        <% if (utente!= null && utente.isAdmin() == true) {%>
	        <a href="${pageContext.request.contextPath}/GestioneProdottiAdmin" class="btn-header btn-admin"> CRUD </a>
	        <a href="${pageContext.request.contextPath}/GestioneOrdiniAdmin" class="btn-header btn-admin"> Gestione Ordini Admin </a>

	<% } %>
     
        
    </nav>
</header>