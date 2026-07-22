package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {
	
	
	// salva ordine
    public synchronized long doSaveOrder(long idUtente, long idInfoConsegna, List<Carrello.ItemCarrello> items) throws SQLException {
        String insertOrdineSQL = "INSERT INTO Ordine (idUtente, infoConsegna) VALUES (?, ?)";
        String insertItemSQL = "INSERT INTO OrderItem (nome, idProdotto, idOrdine, prezzo, quantita, IVA) VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement psOrdine = null;
        PreparedStatement psItem = null;
        long idOrdine = -1;

        try {
            connection = ConnessioneDB.getConnection();
            connection.setAutoCommit(false); // INIZIO TRANSAZIONE

            // 1. Inserimento Testata Ordine
            psOrdine = connection.prepareStatement(insertOrdineSQL, Statement.RETURN_GENERATED_KEYS);
            psOrdine.setLong(1, idUtente);
            psOrdine.setLong(2, idInfoConsegna);
            psOrdine.executeUpdate();

            try (ResultSet rs = psOrdine.getGeneratedKeys()) {
                if (rs.next()) {
                    idOrdine = rs.getLong(1);
                }
            }

            if (idOrdine == -1) {
                throw new SQLException("Impossibile recuperare l'ID Ordine generato.");
            }

            // 2. Inserimento di ogni singolo OrderItem (con Dati Storici)
            psItem = connection.prepareStatement(insertItemSQL);
            for (Carrello.ItemCarrello item : items) {
                ProdottoBean p = item.getProdotto();
                psItem.setString(1, p.getNome());
                psItem.setLong(2, p.getId());
                psItem.setLong(3, idOrdine);
                psItem.setDouble(4, p.getPrezzo());
                psItem.setInt(5, item.getQuantita());
                psItem.setString(6, p.getIva()); // ENUM ('4', '10', '22')

                psItem.addBatch(); // Batch per massima efficienza
            }

            psItem.executeBatch();

            connection.commit(); // CONFERMA TRANSAZIONE
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // ANNULLA IN CASO DI ERRORE
            }
            throw e;
        } finally {
            if (psOrdine != null) psOrdine.close();
            if (psItem != null) psItem.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                ConnessioneDB.releaseConnection(connection);
            }
        }

        return idOrdine;
    }

    // Recupera dal database lo storico degli ordini
    public List<OrdineBean> doRetrieveByUtente(long idUtente) throws SQLException {
        List<OrdineBean> ordini = new ArrayList<>();
        String selectOrdini = "SELECT o.id, o.idUtente, o.infoConsegna, o.dataOrdine, " +
                              "i.via, i.citta, i.cap, i.destinatario " +
                              "FROM Ordine o " +
                              "JOIN InfoConsegna i ON o.infoConsegna = i.id " +
                              "WHERE o.idUtente = ? ORDER BY o.dataOrdine DESC";

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = ConnessioneDB.getConnection();
            ps = connection.prepareStatement(selectOrdini);
            ps.setLong(1, idUtente);
            rs = ps.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setId(rs.getLong("id"));
                ordine.setIdUtente(rs.getLong("idUtente"));
                ordine.setInfoConsegna(rs.getLong("infoConsegna"));
                ordine.setDataOrdine(rs.getTimestamp("dataOrdine"));

                // Formattazione indirizzo di destinazione per la vista/fattura
                String indirizzo = rs.getString("destinatario") + " - " + 
                                   rs.getString("via") + ", " + 
                                   rs.getString("citta") + " (" + rs.getInt("cap") + ")";
                ordine.setIndirizzoConsegnaFormatted(indirizzo);

                // Recupero gli OrderItem legati a questo specifico ordine
                ordine.setItems(doRetrieveItemsByOrdine(ordine.getId(), connection));

                ordini.add(ordine);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (connection != null) {
                ConnessioneDB.releaseConnection(connection);
            }
        }

        return ordini;
    }
    
    
    /// ?????????????????????????????????
    private List<OrderItemBean> doRetrieveItemsByOrdine(long idOrdine, Connection connection) throws SQLException {
        List<OrderItemBean> items = new ArrayList<>();
        String selectItems = "SELECT * FROM OrderItem WHERE idOrdine = ?";

        try (PreparedStatement ps = connection.prepareStatement(selectItems)) {
            ps.setLong(1, idOrdine);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItemBean item = new OrderItemBean();
                    item.setId(rs.getLong("id"));
                    item.setNome(rs.getString("nome"));
                    item.setIdProdotto(rs.getLong("idProdotto"));
                    item.setIdOrdine(rs.getLong("idOrdine"));
                    item.setPrezzo(rs.getDouble("prezzo"));
                    item.setQuantita(rs.getInt("quantita"));
                    item.setIva(rs.getString("IVA"));

                    items.add(item);
                }
            }
        }
        return items;
    }
}