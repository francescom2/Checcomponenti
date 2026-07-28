<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Checkout - Checomponenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <h2>Completa l'Ordine</h2>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="alert-error"><%= error %></div>
        <% } %>

		<% Carrello cart = (Carrello) session.getAttribute("carrello"); %>
        <div class="checkout-layout">
            <!-- Form Dati Spedizione -->
            <div class="cart-summary-box checkout-form-container">
                <h3>Indirizzo di Spedizione</h3>
                <form action="${pageContext.request.contextPath}/checkout" method="POST">
                    
                    <div class="form-group">
                        <label for="destinatario">Nome e Cognome Destinatario:</label>
                        <input type="text" id="destinatario" name="destinatario" required>
                    </div>

                    <div class="form-group">
                        <label for="via">Indirizzo e Numero Civico:</label>
                        <input type="text" id="via" name="via" required>
                    </div>

                    <div class="form-group">
                        <label for="citta">Città:</label>
                        <input type="text" id="citta" name="citta" required>
                    </div>

                    <div class="form-group">
                        <label for="cap">CAP:</label>
                        <input type="text" id="cap" name="cap" pattern="[0-9]{5}" placeholder="es. 80015" required>
                    </div>

                    <div class="form-group">
                        <label for="altro">Note di consegna (Opzionale):</label>
                        <input type="text" id="altro" name="altro" placeholder="Citofono, interno, ecc.">
                    </div>

                    <button type="submit" class="btn">Conferma e Paga</button>
                </form>
            </div>

            <!-- Riepilogo Articoli -->
            <div class="cart-summary-box">
                <h3>Riepilogo Carrello</h3>
                <% if (cart != null) { %>
                    <ul class="checkout-item-list">
						<% for (Carrello.ItemCarrello item : cart.getItems()) { %>                            <li>
                                <span><%= item.getProdotto().getNome() %> (x<%= item.getQuantita() %>)</span>
                                <strong><%= item.getTotaleParziale() %> €</strong>
                            </li>
                        <% } %>
                    </ul>
                    <hr>
                    <p class="total-price">Totale: <%= String.format("%.2f", cart.getTotale()) %> €</p>
                <% } %>
            </div>
        </div>
    </div>

    <%@ include file="fragments/footer.jsp" %>

</body>
</html>