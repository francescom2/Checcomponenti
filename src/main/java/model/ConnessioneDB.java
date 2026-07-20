package model;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnessioneDB {

    private static DataSource ds;

    // Il blocco static viene eseguito una sola volta quando l'applicazione si avvia
    // Inizializza la connessione con il DB
    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            
            // Configurazione della connessione in sicurezza su file esterno
            ds = (DataSource) envCtx.lookup("jdbc/ecommerce");
            
        } catch (NamingException e) {
            System.out.println("Errore critico: DataSource non trovato!");
            e.printStackTrace();
        }
    }

    // Richiesta di connessione dai DAO
    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            throw new SQLException("Il DataSource non è stato inizializzato.");
        }
        return ds.getConnection();
    }

    // Rilascia la connessione e la riporta disponibile nel Connection Pool di Tomcat
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}