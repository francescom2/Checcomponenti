package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InfoConsegnaDAO {

    // Salva un indirizzo di consegna e restituisce l'ID 
    public synchronized long doSave(InfoConsegnaBean info) throws SQLException {
        String insertSQL = "INSERT INTO InfoConsegna (citta, cap, via, altro, destinatario, idUtente) VALUES (?, ?, ?, ?, ?, ?)";
        long generatedId = -1;

        try (Connection connection = ConnessioneDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, info.getCitta());
            preparedStatement.setInt(2, info.getCap());
            preparedStatement.setString(3, info.getVia());
            preparedStatement.setString(4, info.getAltro());
            preparedStatement.setString(5, info.getDestinatario());
            preparedStatement.setLong(6, info.getIdUtente());

            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getLong(1);
                }
            }
        }
        return generatedId;
    }
}