<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.ProdottoBean" %>
<%@ page import="model.CategoriaBean" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Catalogo - Admin Checomponenti</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/crud.css">
	<script src="${pageContext.request.contextPath}/js/crud.js" defer></script>
</head>
<body>

    <%@ include file="../fragments/header.jsp" %>

    <main class="crud-container">
        <h2>Pannello Amministratore - Gestione Prodotti</h2>

        <% 
            String msg = request.getParameter("msg");
            String error = request.getParameter("error");
            
            if ("hidden".equals(msg)) { 
        %>
            <div class="alert alert-success">Prodotto nascosto dal catalogo utenti</div>
        <% } else if ("shown".equals(msg)) { %>
            <div class="alert alert-success">Prodotto reso nuovamente visibile nel catalogo</div>
        <% } else if ("deleted".equals(msg)) { %>
            <div class="alert alert-success">Prodotto eliminato definitivamente dal Database</div>
        <% } else if ("fk_constraint".equals(error)) { %>
            <div class="alert alert-danger">
                 <strong>Impossibile eliminare:</strong> questo prodotto è presente in ordini passati. Usa il pulsante <strong>"Nascondi"</strong> per rimuoverlo dal catalogo.
            </div>
        <% } %>

        <!-- Form di inserimento modifica -->
        <section class="form-section">
            <h3 id="form-title">Aggiungi Nuovo Prodotto</h3>
            
            <form action="${pageContext.request.contextPath}/GestioneProdottiAdmin" method="POST" id="crud-form" class="crud-form">
                <!-- Action hidden: 'add' o 'update' -->
                <input type="hidden" name="action" id="form-action" value="add">
                <input type="hidden" name="id" id="prodotto-id" value="">

                <div class="form-grid">
                    <div class="form-group">
                        <label for="nome">Nome Prodotto *</label>
                        <input type="text" id="nome" name="nome" placeholder="es. Processore Intel i7" required>
                        <span class="error-msg" id="err-nome"></span>
                    </div>

                    <div class="form-group">
                        <label for="idCategoria">Categoria *</label>
                        <select id="idCategoria" name="idCategoria" required>
                            <option value="">-- Seleziona Categoria --</option>
                            <% 
                                List<CategoriaBean> categorie = (List<CategoriaBean>) request.getAttribute("categorie");
                                if (categorie != null) {
                                    for (CategoriaBean cat : categorie) {
                            %>
                                <option value="<%= cat.getId() %>"><%= cat.getNome() %></option>
                            <% 
                                    }
                                } 
                            %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="prezzo">Prezzo (€) *</label>
                        <input type="number" step="0.01" min="0" id="prezzo" name="prezzo" placeholder="es. 199.99" required>
                    </div>

                    <div class="form-group">
                        <label for="iva">Aliquota IVA (%) *</label>
                        <select id="iva" name="iva" required>
                            <option value="22">22% (Standard)</option>
                            <option value="10">10% (Ridotta)</option>
                            <option value="4">4% (Super Ridotta)</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="quantita">Quantità in Stock *</label>
                        <input type="number" min="0" id="quantita" name="quantita" placeholder="es. 50" required>
                    </div>

                    <div class="form-group">
                        <label for="imgPath">Nome File Immagine *</label>
                        <input type="text" id="imgPath" name="imgPath" placeholder="es. cpu-i7.jpg" value="default.jpg" required>
                    </div>
                </div>

                <div class="form-group full-width">
                    <label for="descrizione">Descrizione Dettagliata *</label>
                    <textarea id="descrizione" name="descrizione" rows="4" placeholder="Inserisci le specifiche tecniche..." required></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" id="btn-submit" class="btn-save">Salva Prodotto</button>
                    <button type="button" id="btn-reset" class="btn-cancel" onclick="resetForm()">Annulla Modifica</button>
                </div>
            </form>
        </section>

        <!-- Tabella prodotti -->
        <section class="table-section">
            <h3>Prodotti nel Catalogo</h3>

            <div class="table-responsive">
                <table class="crud-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Anteprima</th>
                            <th>Nome</th>
                            <th>Categoria</th>
                            <th>Prezzo</th>
                            <th>IVA</th>
                            <th>Stock</th>
                            <th>Stato</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<ProdottoBean> prodotti = (List<ProdottoBean>) request.getAttribute("prodotti");
                            if (prodotti == null || prodotti.isEmpty()) {
                        %>
                            <tr>
                                <td colspan="8" class="empty-table">Nessun prodotto trovato nel catalogo.</td>
                            </tr>
                        <%
                            } else {
                                for (ProdottoBean p : prodotti) {
                                    boolean isVisibile = (p.getVisibile() == true);
                        %>
                            <tr class="<%= isVisibile ? "" : "row-hidden" %>">
                                <td>#<%= p.getId() %></td>
                                <td>
                                    <img src="img/<%= p.getImgPath() %>" alt="<%= p.getNome() %>" class="table-img">
                                </td>
                                <td><strong><%= p.getNome() %></strong></td>
								<td>
								    <% 
								        String nomeCategorie = "";
								        if (categorie != null) {
								            for (CategoriaBean cat : categorie) {
								                if (cat.getId() == p.getIdCategoria()) {
								                    nomeCategorie = cat.getNome();
								                    break;
								                }
								            }
								        }
								    %>
								    <%= nomeCategorie %>
								</td>                                
								<td>€ <%= String.format("%.2f", p.getPrezzo()) %></td>
                                <td><span class="badge-iva"><%= p.getIva() %>%</span></td>
                                <td><%= p.getQuantita() %> pz</td>
                                <td>
                                    <% if (isVisibile) { %>
                                        <span class="badge status-online">Visibile</span>
                                    <% } else { %>
                                        <span class="badge status-offline">Nascosto</span>
                                    <% } %>
                                </td>
                                <td class="actions-cell">
                                    <!-- Tasto modifica -->
                                    <button class="btn-action edit" onclick='caricaInForm(<%= p.getId() %>, "<%= p.getNome().replace("\"", "\\\"") %>", <%= p.getIdCategoria() %>, <%= p.getPrezzo() %>, "<%= p.getIva() %>", <%= p.getQuantita() %>, "<%= p.getImgPath() %>", "<%= p.getDescrizione().replace("\n", " ").replace("\"", "\\\"") %>")'>
                                        Modifica
                                    </button>

                                    <!--  Tasto nascondi/mostra -->
                                    <% if (isVisibile) { %>
                                        <a href="${pageContext.request.contextPath}/GestioneProdottiAdmin?action=hide&id=<%= p.getId() %>" 
                                           class="btn-action hide">
                                            Nascondi
                                        </a>
                                    <% } else { %>
                                        <a href="${pageContext.request.contextPath}/GestioneProdottiAdmin?action=show&id=<%= p.getId() %>" 
                                           class="btn-action show">
                                            Mostra
                                        </a>
                                    <% } %>

                                    <!-- Tasto elimina dal Database -->
                                    <a href="${pageContext.request.contextPath}/GestioneProdottiAdmin?action=delete&id=<%= p.getId() %>" 
                                       class="btn-action delete"
                                       onclick="return confirm('ATTENZIONE: Eliminare definitivamente dal Database?\nSe il prodotto è presente in ordini passati, l\'operazione verrà bloccata.');">
                                        Elimina
                                    </a>
                                </td>
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