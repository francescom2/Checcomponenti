package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

public class ProdottoDAO {

    private static final String TABLE_NAME = "prodotto";

    // 1. Recupera un singolo prodotto tramite ID 
    public synchronized ProdottoBean doRetrieveByKey(long id) throws SQLException {
        ProdottoBean bean = new ProdottoBean();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE ID = ?";

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(selectSQL)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean.setId(rs.getLong("ID"));
                    bean.setNome(rs.getString("NOME"));
                    bean.setDescrizione(rs.getString("DESCRIZIONE"));
                    bean.setPrezzo(rs.getDouble("PREZZO"));
                    bean.setQuantita(rs.getInt("QUANTITA"));
                    bean.setIva(rs.getString("IVA"));
                    bean.setImgPath(rs.getString("imgPath"));
                    bean.setIdCategoria(rs.getLong("idCategoria"));
                    bean.setVisibile(rs.getBoolean("visibile"));
                }
            }
        }
        return bean;
    }

    // 2. Recupera tutti i prodotti presenti nel Database
    public synchronized List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> prodotti = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProdottoBean bean = new ProdottoBean();
                bean.setId(rs.getLong("ID"));
                bean.setNome(rs.getString("NOME"));
                bean.setDescrizione(rs.getString("DESCRIZIONE"));
                bean.setPrezzo(rs.getDouble("PREZZO"));
                bean.setQuantita(rs.getInt("QUANTITA"));
                bean.setIva(rs.getString("IVA"));
                bean.setImgPath(rs.getString("imgPath"));
                bean.setIdCategoria(rs.getLong("idCategoria"));
                bean.setVisibile(rs.getBoolean("visibile"));

                prodotti.add(bean);
            }
        }
        return prodotti;
    }
    
    // 2. Recupera tutti i prodotti visibili
    public synchronized List<ProdottoBean> doRetrieveAllVisible() throws SQLException {
        List<ProdottoBean> prodotti = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME +
        				   " WHERE visibile = 1";

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProdottoBean bean = new ProdottoBean();
                bean.setId(rs.getLong("ID"));
                bean.setNome(rs.getString("NOME"));
                bean.setDescrizione(rs.getString("DESCRIZIONE"));
                bean.setPrezzo(rs.getDouble("PREZZO"));
                bean.setQuantita(rs.getInt("QUANTITA"));
                bean.setIva(rs.getString("IVA"));
                bean.setImgPath(rs.getString("imgPath"));
                bean.setIdCategoria(rs.getLong("idCategoria"));
                bean.setVisibile(rs.getBoolean("visibile"));

                prodotti.add(bean);
            }
        }
        return prodotti;
    }


    // SPECIFICI PER L'ADMIN

    // 3. Salva un nuovo prodotto nel Database 
    public synchronized void doSave(ProdottoBean product) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME 
                + " (NOME, DESCRIZIONE, PREZZO, QUANTITA, IVA, imgPath, idCategoria, visibile) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(insertSQL)) {

            ps.setString(1, product.getNome());
            ps.setString(2, product.getDescrizione());
            ps.setDouble(3, product.getPrezzo());
            ps.setInt(4, product.getQuantita());
            ps.setString(5, product.getIva());

            // Valore di sicurezza se l'immagine non è specificata
            String img = (product.getImgPath() != null && !product.getImgPath().trim().isEmpty()) 
                         ? product.getImgPath() 
                         : "img/imgNonTrovata.png";
            ps.setString(6, img);

            ps.setLong(7, product.getIdCategoria());
            ps.setBoolean(8, product.getVisibile());

            ps.executeUpdate();
        }
    }

    // 4. Elimina un prodotto tramite il suo ID
    public synchronized boolean doDelete(long id) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?";
        int result = 0;

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(deleteSQL)) {

            ps.setLong(1, id);
            result = ps.executeUpdate();
        }
        return (result != 0);
    }
    
    
    // 5. Aggiorna un prodotto esistente
    public synchronized void doUpdate(ProdottoBean p) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET nome = ?, descrizione = ?, quantita = ?, prezzo = ?, " +
                     "IVA = ?, imgPath = ?, idCategoria = ?, visibile = ? WHERE id = ?";

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescrizione());
            ps.setInt(3, p.getQuantita());
            ps.setDouble(4, p.getPrezzo());
            ps.setString(5, p.getIva());
            
            String img = (p.getImgPath() != null && !p.getImgPath().trim().isEmpty()) 
                         ? p.getImgPath() 
                         : "img/imgNonTrovata.png";
            ps.setString(6, img);

            ps.setLong(7, p.getIdCategoria());
            ps.setBoolean(8, p.getVisibile());
            ps.setLong(9, p.getId());

            ps.executeUpdate();
        }
    }
    
    // 6. Cambia visibilità
    public synchronized boolean setVisibilita(long id, boolean visibile) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET visibile = ? WHERE ID = ?";
        int result = 0;

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, visibile);
            ps.setLong(2, id);
            result = ps.executeUpdate();
        }
        return (result > 0);
    }
}