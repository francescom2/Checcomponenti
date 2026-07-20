<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.ProdottoBean"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Checomponenti - Catalogo</title>

	<!-- ${pageContext.request.contextPath} evita il percorso relativo -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">	
	<script src="${pageContext.request.contextPath}/js/carrello.js" defer></script>
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <h1> Catalogo Componenti PC</h1>

        <div class="container">
            <% 
                @SuppressWarnings("unchecked")
                List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");

                if (prodotti != null && !prodotti.isEmpty()) {
                    for (ProdottoBean p : prodotti) {
            %>
                        <div class="card">
                            <div>
                            	<!-- aprire pagina prodotto -->
                                <a href="${pageContext.request.contextPath}/PaginaProdotto?id=<%= p.getId() %>">                                
                            
                               		<%-- Se non trova l'immagine associata --%>
	                                <img src="${pageContext.request.contextPath}/<%= (p.getImgPath() != null && !p.getImgPath().isEmpty()) ? p.getImgPath() : "img/imgNonTrovata.png" %>" 
									     alt="<%= p.getNome() %>" 
									     class="card-img"
									     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/img/imgNonTrovata.png';">
								
                                	<h3><%= p.getNome() %> </h3>
                                	
                                </a>
								     
                                <p><%= p.getDescrizione() %></p>
                            </div>
								<div>
								    <div class="price">€ <%= String.format("%.2f", p.getPrezzo()) %></div>
								    <p><small>Disponibilità: <%= p.getQuantita() %> pz</small></p>
								
								    <!-- uso di AJAX  -->
									<button type="button" class="btn" onclick="aggiungiAlCarrello(this, <%= p.getId() %>, '${pageContext.request.contextPath}')">
									    Aggiungi al Carrello
									</button>
								</div>
                        </div>
            <% 
                    }
                } else { 
            %>
                    <p class="empty-catalog-msg">Nessun prodotto disponibile al momento.</p>
            <% } %>
        </div>
    </div>

    <%@ include file="fragments/footer.jsp" %>
</body>
</html>