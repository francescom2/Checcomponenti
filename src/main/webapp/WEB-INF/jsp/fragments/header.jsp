<script src="${pageContext.request.contextPath}/js/ricerca.js" defer></script>
<%@ page import ="model.UtenteBean" %>
<%@ page import="model.Carrello" %>
<% 
	UtenteBean utente = (UtenteBean) session.getAttribute("utente"); 
	Carrello carrelloHeader = (Carrello) session.getAttribute("carrello");
	int totaleArticoli = (carrelloHeader != null) ? carrelloHeader.getTotaleArticoli() : 0;
%>


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


<header class="main-header">
    <a href="${pageContext.request.contextPath}/catalogo" class="logo">
        <span class="logo-accent">Chec</span>componenti
    </a>
    
	<form action="${pageContext.request.contextPath}/catalogo" method="GET" class="search-container">
	    <input type="text" 
	           name="q" 
	           id="search-input" 
	           placeholder="Cerca processori, GPU, RAM..." 
	           autocomplete="off" 
	           data-contextpath="${pageContext.request.contextPath}">
	    <div id="search-results" class="search-results-dropdown"></div>
	</form>    
    <nav class="nav-links">
    
        <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a>
        <a href="${pageContext.request.contextPath}/carrello" class="cart-link">
            Carrello
		<span id="cart-count" 
          class="cart-badge" 
          style="<%= (totaleArticoli == 0) ? "display: none;" : "" %>">
          <%= totaleArticoli %>
    	</span>           

        </a>
        
        <% if (utente == null) { %>
        	<a href="${pageContext.request.contextPath}/login" class="btn-header btn-login">Accedi</a>
            <a href="${pageContext.request.contextPath}/registrazione" class="btn-header btn-register">Registrati</a>
        <% } else { %>
			<div class="user-dropdown">
                <button type="button" class="user-dropdown-btn">
        	${utente.getUsername()}
        	</button>
        	
        	<div class="dropdown-content">
				<a href="${pageContext.request.contextPath}/StoricoOrdini">Storico Ordini</a>
							                	
		<% if (utente.isAdmin()) { %>
			<div class="dropdown-divider"></div>
			<span class="dropdown-header">Area Amministratore</span>
	        <a href="${pageContext.request.contextPath}/GestioneProdottiAdmin" class="btn-header btn-admin"> Gestione Prodotti </a>
	        <a href="${pageContext.request.contextPath}/GestioneOrdiniAdmin" class="btn-header btn-admin"> Gestione Ordini </a>

		<% } %>
		<div class="dropdown-divider"></div>
		<a href="${pageContext.request.contextPath}/logout" class="logout-link"> Logout</a>
			
    </div>
     </div>
        <% } %>
    </nav>
</header>