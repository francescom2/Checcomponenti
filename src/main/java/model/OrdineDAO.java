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
        String updateStockSQL = "UPDATE Prodotto SET quantita = quantita - ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement psOrdine = null;
        PreparedStatement psItem = null;
        PreparedStatement psStock = null;
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

            // 2. Inserimento di ogni singolo OrderItem 
            psItem = connection.prepareStatement(insertItemSQL);
            psStock = connection.prepareStatement(updateStockSQL);
            
            for (Carrello.ItemCarrello item : items) {
                ProdottoBean p = item.getProdotto();
                psItem.setString(1, p.getNome());
                psItem.setLong(2, p.getId());
                psItem.setLong(3, idOrdine);
                psItem.setDouble(4, p.getPrezzo());
                psItem.setInt(5, item.getQuantita());
                psItem.setString(6, p.getIva()); // ENUM ('4', '10', '22')

                psItem.addBatch(); // Batch per massima efficienza
                
                // Scala quantità
                psStock.setInt(1, item.getQuantita());
                psStock.setLong(2, p.getId());
                psStock.addBatch();
            }

            psItem.executeBatch();
            psStock.executeBatch();

            connection.commit(); // CONFERMA TRANSAZIONE
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // ANNULLA IN CASO DI ERRORE
            }
            throw e;
        } finally {
            if (psOrdine != null) psOrdine.close();
            if (psItem != null) psItem.close();
            if (psStock != null) psStock.close();
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
    
    
    // Recupera tutti gli ordini 
    public synchronized List<OrdineBean> doRetrieveAll() throws SQLException {
        List<OrdineBean> ordini = new ArrayList<>();
        String sql = "SELECT o.id, o.idUtente, o.infoConsegna, o.dataOrdine, " +
                "i.via, i.citta, i.cap, i.destinatario " +
                "FROM Ordine o " +
                "LEFT JOIN InfoConsegna i ON o.infoConsegna = i.id " +
                "ORDER BY o.dataOrdine DESC";

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setId(rs.getLong("id"));
                ordine.setIdUtente(rs.getLong("idUtente"));
                ordine.setInfoConsegna(rs.getLong("infoConsegna"));
                ordine.setDataOrdine(rs.getTimestamp("dataOrdine"));
                ordine.setItems(doRetrieveItemsByOrdine(ordine.getId(), con));
                
                if (rs.getString("destinatario") != null) {
                    String indirizzo = rs.getString("destinatario") + " - " + 
                                       rs.getString("via") + ", " + 
                                       rs.getString("citta") + " (" + rs.getInt("cap") + ")";
                    ordine.setIndirizzoConsegnaFormatted(indirizzo);
                }
                ordini.add(ordine);
            }
        }
        return ordini;
    }

    // Filtra gli ordini per intervallo di date e/o id utente
    public synchronized List<OrdineBean> doRetrieveByFilter(String dataInizio, String dataFine, String idUtenteStr) throws SQLException {
        List<OrdineBean> ordini = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT o.id, o.idUtente, o.infoConsegna, o.dataOrdine, ")
                .append("i.via, i.citta, i.cap, i.destinatario ")
                .append("FROM Ordine o ")
                .append("LEFT JOIN InfoConsegna i ON o.infoConsegna = i.id ")
                .append("WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        
        if (dataInizio != null && !dataInizio.isEmpty()) {
            sql.append(" AND DATE(o.dataOrdine) >= ?");
            parameters.add(java.sql.Date.valueOf(dataInizio));
        }
        if (dataFine != null && !dataFine.isEmpty()) {
            sql.append(" AND DATE(o.dataOrdine) <= ?");
            parameters.add(java.sql.Date.valueOf(dataFine));
        }
        if (idUtenteStr != null && !idUtenteStr.isEmpty()) {
            sql.append(" AND o.idUtente = ?");
            parameters.add(Long.parseLong(idUtenteStr));
        }

        sql.append(" ORDER BY dataOrdine DESC");

        try (Connection con = ConnessioneDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
        	
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            
            try(ResultSet rs = ps.executeQuery()){
	            while (rs.next()) {
	                OrdineBean ordine = new OrdineBean();
	                ordine.setId(rs.getLong("id"));
	                ordine.setIdUtente(rs.getLong("idUtente"));
	                ordine.setInfoConsegna(rs.getLong("infoConsegna"));
	                ordine.setDataOrdine(rs.getTimestamp("dataOrdine"));
	                ordine.setItems(doRetrieveItemsByOrdine(ordine.getId(), con));
	                
	                if (rs.getString("destinatario") != null) {
	                    String indirizzo = rs.getString("destinatario") + " - " + 
	                                       rs.getString("via") + ", " + 
	                                       rs.getString("citta") + " (" + rs.getInt("cap") + ")";
	                    ordine.setIndirizzoConsegnaFormatted(indirizzo);
	                }
	
	                ordini.add(ordine);
            }
        }
        return ordini;
        }
    }    private List<OrderItemBean> doRetrieveItemsByOrdine(long idOrdine, Connection connection) throws SQLException {
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