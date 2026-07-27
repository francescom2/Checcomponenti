<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.OrdineBean" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Ordini - Admin Checomponenti</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css">
</head>
<body>

    <%@ include file="../fragments/header.jsp" %>

    <main class="crud-container">
        <h2> Pannello Admin - Gestione Ordini Clienti</h2>

        <!-- Form dei Filtri -->
        <section class="form-section">
            <h3> Filtra Ordini</h3>
            <form action="${pageContext.request.contextPath}/GestioneOrdiniAdmin" method="GET" class="filter-form">
                <div class="form-group">
                    <label for="dataInizio">Data Inizio</label>
                    <input type="date" id="dataInizio" name="dataInizio" value="${param.dataInizio}">
                </div>
                <div class="form-group">
                    <label for="dataFine">Data Fine</label>
                    <input type="date" id="dataFine" name="dataFine" value="${param.dataFine}">
                </div>
                <div class="form-group">
                    <label for="idUtente">ID Cliente</label>
                    <input type="number" id="idUtente" name="idUtente" placeholder="es. 5" value="${param.idUtente}">
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-save">Filtra</button>
                    <a href="${pageContext.request.contextPath}/GestioneOrdiniAdmin" class="btn-cancel">Resetta</a>
                </div>
            </form>
        </section>

        <!-- Tabella Ordini -->
        <section class="table-section">
            <div class="table-responsive">
                <table class="crud-table">
                    <thead>
                        <tr>
                            <th>ID Ordine</th>
                            <th>ID Utente</th>
                            <th>Data Ordine</th>
                            <th>Indirizzo Consegna</th>
                            <th>N° Articoli</th>
                            <th>Totale Ordine</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<OrdineBean> ordini = (List<OrdineBean>) request.getAttribute("ordini");
                            if (ordini == null || ordini.isEmpty()) {
                        %>
                            <tr>
                                <td colspan="6" class="empty-table">Nessun ordine trovato.</td>
                            </tr>
                        <%
                            } else {
                                for (OrdineBean o : ordini) {
                        %>
                            <tr>
                                <td><strong>#<%= o.getId() %></strong></td>
                                <td><%= o.getIdUtente() %></td>
                                <td><%= o.getDataOrdine() != null ? o.getDataOrdine() : "-" %></td>
                                <td><%= o.getIndirizzoConsegnaFormatted() != null ? o.getIndirizzoConsegnaFormatted() : "N/D" %></td>
                                <td><%= o.getItems() != null ? o.getItems().size() : 0 %> </td>
                                <td><strong>€ <%= String.format("%.2f", o.getTotaleOrdine()) %></strong></td>
                            </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </section>
    </main>

    <%@ include file="../fragments/footer.jsp" %>

</body>
</html>