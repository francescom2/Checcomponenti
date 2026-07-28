<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.OrdineBean" %>
<%@ page import="model.OrderItemBean" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Storico Ordini - Checomponenti</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/fattura.css">
    <script src="${pageContext.request.contextPath}/js/fattura.js" defer></script>
    
</head>
<body>

    <!-- Fragment Header JSP -->
    <%@ include file="fragments/header.jsp" %>

    <main class="ordini-main">
        <h2>Storico Ordini Effettuati</h2>

        <%
            List<OrdineBean> ordini = (List<OrdineBean>) request.getAttribute("ordini");
            if (ordini == null || ordini.isEmpty()) {
        %>
            <p class="no-ordini-msg">Non hai ancora effettuato ordini su Checomponenti.</p>
        <%
            } else {
                for (OrdineBean ordine : ordini) {
        %>
            <section class="ordine-box">
                <header class="ordine-header">
                    <h3>Ordine #<%= ordine.getId() %></h3>
                    <p class="order-info"><strong>Data:</strong> <%= ordine.getDataOrdine() %></p>
                    <p class="order-info"><strong>Destinazione:</strong> <%= ordine.getIndirizzoConsegnaFormatted() %></p>
                </header>
				<div class="table-responsive">
	                <table class="table-ordini">
	                    <thead>
	                        <tr>
	                            <th>Prodotto</th>
	                            <th>Prezzo Imponibile</th>
								<th>IVA</th>
	                   			<th>Prezzo Unitario</th>
	                            <th>Quantità</th>
	                            <th>Subtotale (Inc. IVA)</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                        <% for (OrderItemBean item : ordine.getItems()) { %>
	                            <tr>
	                                <td><%= item.getNome() %></td>
	                                <td>€ <%= String.format("%.2f", item.getPrezzoSenzaIva()) %></td>
	                                <td><%= item.getIva() %>%</td>
	                                <td>€ <%= String.format("%.2f", item.getPrezzo()) %></td>
	                                <td><%= item.getQuantita() %></td>
	                                <td>€ <%= String.format("%.2f", item.getPrezzo() * item.getQuantita() ) %></td>
	                            </tr>
	                        <% } %>
	                    </tbody>
	                </table>
				</div>
	
                <footer class="totale-section">
                    <p class="totale-testo">Totale Ordine: <strong>€ <%= String.format("%.2f", ordine.getTotaleOrdine()) %></strong></p>
					<button type="button" class="btn-stampa no-print" onclick="stampaFatturaSingola(this)">
					    Stampa Fattura PDF
					</button>                
				</footer>
            </section>
        <%
                }
            }
        %>
    </main>

    <!-- Fragment Footer JSP -->
    <%@ include file="fragments/footer.jsp" %>

</body>
</html>