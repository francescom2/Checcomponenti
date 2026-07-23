package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.GestorePassword;

public class UtenteDAO {

    // Registrazione utente
    public synchronized void doSave(UtenteBean utente) throws SQLException {
        String insertSQL = "INSERT INTO Utente (username, email, imgPath, isAdmin, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnessioneDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, (utente.getImgPath() != null && !utente.getImgPath().isEmpty()) ? utente.getImgPath() : "default.jpg");
            preparedStatement.setBoolean(4, utente.isAdmin());
            preparedStatement.setString(5, GestorePassword.hashPassword(utente.getPassword()));

            preparedStatement.executeUpdate();
        }
    }

    // Login utente
    public synchronized UtenteBean doRetrieveByEmailAndPassword(String email, String passwordInChiaro) throws SQLException {
        UtenteBean utente = null;
        String selectSQL = "SELECT * FROM Utente WHERE email = ? AND password = ?";
        String passwordHash = GestorePassword.hashPassword(passwordInChiaro);

        try (Connection connection = ConnessioneDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, passwordHash);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    utente = new UtenteBean();
                    utente.setId(rs.getLong("id"));
                    utente.setUsername(rs.getString("username"));
                    utente.setEmail(rs.getString("email"));
                    utente.setImgPath(rs.getString("imgPath"));
                    utente.setAdmin(rs.getBoolean("isAdmin"));
                    utente.setPassword(rs.getString("password"));
                }
            }
        }
        return utente;
    }

    // Recupero utente tramite ID (utilissimo per la Sessione e gli Ordini)
    public synchronized UtenteBean doRetrieveById(long id) throws SQLException {
        UtenteBean utente = null;
        String selectSQL = "SELECT * FROM Utente WHERE id = ?";

        try (Connection connection = ConnessioneDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setLong(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    utente = new UtenteBean();
                    utente.setId(rs.getLong("id"));
                    utente.setUsername(rs.getString("username"));
                    utente.setEmail(rs.getString("email"));
                    utente.setImgPath(rs.getString("imgPath"));
                    utente.setAdmin(rs.getBoolean("isAdmin"));
                    utente.setPassword(rs.getString("password"));
                }
            }
        }
        return utente;
    }

    // Controllo disponibilità email via AJAX
    public synchronized boolean doCheckEmail(String email) throws SQLException {
        boolean esiste = false;
        String selectSQL = "SELECT id FROM Utente WHERE email = ?";

        try (Connection connection = ConnessioneDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    esiste = true;
                }
            }
        }
        return esiste;
    }
}