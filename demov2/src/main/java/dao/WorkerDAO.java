package dao;
// Definisce il package in cui si trova questa classe

import model.Worker;
// Importa la classe modello Worker

import java.sql.*;
// Importa le classi JDBC per la connessione al database

import java.util.*;
// Importa le classi per le collezioni (List, ArrayList)

public class WorkerDAO {
    // Classe DAO per gestire l'accesso al database per i lavoratori

    // Metodo per ottenere tutti i lavoratori dal database
    public List<Worker> getAll() {

        List<Worker> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            // Ottiene la connessione al database tramite DBConnection

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM workers");
            // Esegue la query per selezionare tutti i record della tabella workers

            while (rs.next()) {
                // Itera su ogni riga del ResultSet
                list.add(new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("specialization_id")
                ));
                // Crea un oggetto Worker e lo aggiunge alla lista
            }
        } catch (Exception e) { e.printStackTrace(); }
        // Gestione eccezioni: stampa eventuali errori

        return list;
        // Restituisce la lista di lavoratori
    }

    // Metodo per inserire un nuovo lavoratore nel database
    public void insert(String name, int specializationId) {

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO workers(name, specialization_id) VALUES (?, ?)"
            );
            // PreparedStatement per prevenire SQL injection ed inserire valori dinamici

            ps.setString(1, name);
            ps.setInt(2, specializationId);
            // Imposta i valori dei parametri nella query

            ps.executeUpdate();
            // Esegue l'inserimento nel database
        } catch (Exception e) { e.printStackTrace(); }
        // Gestione eccezioni
    }

    // Metodo per eliminare un lavoratore dal database tramite id
    public void delete(int id) {

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM workers WHERE id=?");
            ps.setInt(1, id);
            // Imposta l'id del lavoratore da eliminare

            ps.executeUpdate();
            // Esegue l'eliminazione
        } catch (Exception e) { e.printStackTrace(); }
        // Gestione eccezioni
    }
}