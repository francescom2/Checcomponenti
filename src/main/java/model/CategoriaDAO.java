package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<CategoriaBean> doRetrieveAll() throws SQLException {
        List<CategoriaBean> categorie = new ArrayList<>();
        String sql = "SELECT * FROM Categoria ORDER BY nome ASC";

        try (Connection con = ConnessioneDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoriaBean c = new CategoriaBean();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                categorie.add(c);
            }
        }
        return categorie;
    }
}