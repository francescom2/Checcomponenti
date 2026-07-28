<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.ProdottoBean"%>
<%@ page import="model.CategoriaBean"%>

<%!
    // Metodo helper in JSP per assegnare le descrizioni personalizzate alle categorie
    private String getDescrizioneCategoria(String nome) {
        if (nome == null) return "Scopri i componenti hardware di alta qualità.";
        
        switch (nome.trim()) {
            case "Processore": 
                return "Intel oppure AMD?";
            case "Scheda Video": 
                return "Quanta potenza desideri?";
            case "Scheda Madre": 
                return "Nuovo processore?";
            case "RAM": 
                return "Più RAM, più programmi aperti.";
            case "Storage": 
                return "Sei a corto di GB?";
            case "Dissipatore": 
                return "Temperature troppo alte?";
            case "Case": 
                return "Con RGB o senza?";
            case "Alimentatore": 
                return "Aggiornata la scheda video?";
            default: 
                return "Scopri i migliori componenti selezionati per il tuo computer.";
        }
    }
%>


<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Checomponenti - Catalogo</title>

	<!-- ${pageContext.request.contextPath} evita il percorso relativo -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">	
	<script src="${pageContext.request.contextPath}/js/carrello.js" defer></script>
	<script src="${pageContext.request.contextPath}/js/carosello.js" defer></script>

</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <h1> Desideri un Upgrade?<br>Oppure un intero PC nuovo di zecca?</h1>

        <div class="container">
            <% 
                @SuppressWarnings("unchecked")
                List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
	            @SuppressWarnings("unchecked")    
	            List<CategoriaBean> categorie = (List<CategoriaBean>) request.getAttribute("categorie");
	             	
	            
	            if (categorie != null && !categorie.isEmpty() && prodotti != null && !prodotti.isEmpty()) {
	                for (CategoriaBean cat : categorie) {

	                    // Verifica se ci sono prodotti per questa categoria prima di mostrare la riga
	                    boolean haProdotti = false;
	                    for (ProdottoBean p : prodotti) {
	                        if (p.getIdCategoria() == cat.getId()) {
	                            haProdotti = true;
	                            break;
	                        }
	                    }

	                    if (haProdotti) {
	                        %>
                        <section class="category-row">
                            <!-- Info Categoria -->
                            <div class="category-info-box">
                                <h2><%= cat.getNome() %></h2>
                                <p><%= getDescrizioneCategoria(cat.getNome()) %></p>
                            </div>
                            
                            <!-- Carosello Prodotti -->
                        <div class="carousel-wrapper">
                                <!-- Pulsante scorri a sinistra -->
                                <button type="button" class="carousel-btn prev" onclick="scrollCarousel(this, -1)" aria-label="Indietro">&#10094;</button>

                                <div class="carousel-track">

									<% 
                                        for (ProdottoBean p : prodotti) {
                                            if (p.getIdCategoria() == cat.getId()) {
                                    %>

                                                <div class="card card-carousel">
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
													     
					                            </div>
													<div>
													<% if (p.getQuantita() == 0) {%>
													    <div class="price">€ <%= String.format("%.2f", p.getPrezzo()) %></div>
													    <p class="text-out-of-stock"><small>Prodotto terminato!</small></p>
													    
													    <!-- uso di AJAX  -->
														<button type="button" class="btn btn-sold-out" >
														    SOLD OUT
														</button>
													<%} else { %>
													    <div class="price">€ <%= String.format("%.2f", p.getPrezzo()) %></div>
													    <p><small>Disponibilità: <%= p.getQuantita() %> pz</small></p>
													
													    <!-- uso di AJAX  -->
														<button type="button" class="btn" onclick="aggiungiAlCarrello(this, <%= p.getId() %>, '${pageContext.request.contextPath}')">
														    Aggiungi al Carrello
														</button>
													<%} %>
												</div>
				                        </div>
                        
                                    <% 
                                            } 
                                        } 
                                    %>
                        </div>
						 <!-- Pulsante scorri a destra -->
                         <button type="button" class="carousel-btn next" onclick="scrollCarousel(this, 1)" aria-label="Avanti">&#10095;</button>
                     </div>
                 </section>
                        
            <% 			
            			}
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