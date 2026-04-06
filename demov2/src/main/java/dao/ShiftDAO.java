package dao;
// Definisce il package in cui si trova questa classe

import model.*;
// Importa le classi modello (Shift, Worker)

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
// Importa classi JDBC, LocalDate e collezioni

public class ShiftDAO {
    // Classe DAO per gestire l'accesso al database per i turni

    public List<Shift> getShiftsBetween(LocalDate start, LocalDate end) {
        // Metodo per ottenere tutti i turni tra due date

        List<Shift> shifts = new ArrayList<>();
        // Lista in cui verranno memorizzati i turni recuperati

        String sql = """
            SELECT s.work_date, s.shift_type,
                   w.id AS worker_id, w.name AS worker_name,
                   w.specialization_id AS spec_id
            FROM shifts s
            JOIN workers w ON s.worker_id = w.id
            WHERE s.work_date BETWEEN ? AND ?
        """;
        // Query SQL parametrizzata:
        // - seleziona la data del turno ed il tipo di turno
        // - unisce la tabella workers per ottenere informazioni sul lavoratore
        // - filtra i turni compresi tra start e end

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Ottiene la connessione al database e prepara la query

            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            // Imposta i parametri della query (date di inizio e fine)

            ResultSet rs = ps.executeQuery();
            // Esegue la query

            while (rs.next()) {
                // Itera su ogni riga del risultato

                Worker worker = new Worker(
                        rs.getInt("worker_id"),
                        rs.getString("worker_name"),
                        rs.getInt("spec_id")
                );
                // Crea un oggetto Worker per associare il turno al lavoratore

                Shift shift = new Shift(
                        rs.getDate("work_date").toLocalDate(),
                        worker,
                        rs.getString("shift_type")
                );
                // Crea un oggetto Shift con la data, il lavoratore e il tipo di turno

                shifts.add(shift);
                // Aggiunge il turno alla lista
            }
        } catch (Exception e) { e.printStackTrace(); }
        // Gestione delle eccezioni: stampa eventuali errori

        return shifts;
        // Restituisce la lista completa dei turni
    }
}