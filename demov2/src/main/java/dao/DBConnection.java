package dao;
// Definisce il package in cui si trova questa classe

import java.sql.Connection;
import java.sql.DriverManager;
// Importa le classi JDBC necessarie per creare la connessione al database

public class DBConnection {
    // Classe helper per ottenere una connessione al database

    public static Connection getConnection() throws Exception {
        // Metodo statico che restituisce una connessione JDBC
        // Lancia eccezione in caso di errore di connessione

        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gestionaleore",
                // URL del database MySQL:
                // - host: localhost
                // - porta: 3306
                // - nome del database: gestionaleore

                "root",
                // Username del database

                "root"
                // Password del database
        );
    }
}