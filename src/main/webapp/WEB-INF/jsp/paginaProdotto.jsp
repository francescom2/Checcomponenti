<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.ProdottoBean"%>

<%
    ProdottoBean p = (ProdottoBean) request.getAttribute("prodotto");
    if (p == null) {
        response.sendRedirect(request.getContextPath() + "/catalogo");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= p.getNome() %> - Dettaglio Checomponenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/carrello.js" defer></script>
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <div class="back-line-wrapper">
            <a href="${pageContext.request.contextPath}/catalogo" class="back-link">← Torna al Catalogo</a>
        </div>

        <div class="detail-container">
            <div>
                <img src="${pageContext.request.contextPath}/<%= (p.getImgPath() != null && !p.getImgPath().isEmpty()) ? p.getImgPath() : "img/imgNonTrovata.png" %>" 
                     alt="<%= p.getNome() %>" 
                     class="detail-img"
                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/imgNonTrovata.png';">
            </div>

            <div class="detail-info">
                <h1><%= p.getNome() %></h1>
                <p class="product-descriptor"><%= p.getDescrizione() %></p>
                
                <% if (p.getQuantita() == 0) {%>
					<div class="price">€ <%= String.format("%.2f", p.getPrezzo()) %></div>
					<p class="text-out-of-stock"><small>Prodotto terminato!</small></p>
	                <button type="button" class="btn btn-sold-out" >									
						SOLD OUT
					</button>
				<%} else { %>
	                <div class="price price-large">€ <%= String.format("%.2f", p.getPrezzo()) %></div>
	                <p><strong>Disponibilità immediata:</strong> <%= p.getQuantita() %> pezzi</p>
	
					<button type="button" class="btn" onclick="aggiungiAlCarrello(this, <%= p.getId() %>, '${pageContext.request.contextPath}')">
					    Aggiungi al Carrello
					</button>
				<%} %>
					
            </div>
        </div>
    </div>

    <%@ include file="fragments/footer.jsp" %>

</body>
</html>