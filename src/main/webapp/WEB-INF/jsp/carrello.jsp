<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello"%>
<%@ page import="model.Carrello.ItemCarrello"%>

<%
    Carrello carrello = (Carrello) session.getAttribute("carrello");
    if (carrello == null) {
        carrello = new Carrello();
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Il tuo Carrello - Checomponenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="fragments/header.jsp" %>

    <div class="main-content">
        <h1>🛒 Il tuo Carrello</h1>

        <% if (carrello.isEmpty()) { %>
            <div class="empty-cart-container">
                <p class="empty-catalog-msg">Il carrello è attualmente vuoto.</p>
                <a href="${pageContext.request.contextPath}/catalogo" class="btn">Torna al Catalogo</a>
            </div>
        <% } else { %>
            <div class="cart-container">
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo Unitario</th>
                            <th>Quantità</th>
                            <th>Totale</th>
                            <th>Azione</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (ItemCarrello item : carrello.getItems()) { %>
                            <tr>
                                <td>
                                    <strong><%= item.getProdotto().getNome() %></strong>
                                </td>
                                <td>€ <%= String.format("%.2f", item.getProdotto().getPrezzo()) %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/carrello" method="POST" class="qty-form">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="id" value="<%= item.getProdotto().getId() %>">
                                        <input type="number" name="quantita" value="<%= item.getQuantita() %>" min="1" max="<%= item.getProdotto().getQuantita() %>" onchange="this.form.submit()" class="qty-input">
                                    </form>
                                </td>
                                <td>€ <%= String.format("%.2f", item.getTotaleParziale()) %></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/carrello?action=remove&id=<%= item.getProdotto().getId() %>" class="btn-remove">❌ Rimuovi</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>

                <div class="cart-summary">
                    <h3>Totale Carrello: <span class="total-price">€ <%= String.format("%.2f", carrello.getTotale()) %></span></h3>
                    <div class="cart-actions">
                        <a href="${pageContext.request.contextPath}/carrello?action=clear" class="btn btn-secondary">Svuota Carrello</a>
                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success">Procedi all'Acquisto →</a>
                    </div>
                </div>
            </div>
        <% } %>
    </div>

    <%@ include file="fragments/footer.jsp" %>

</body>
</html>