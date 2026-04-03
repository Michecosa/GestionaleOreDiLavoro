package esercizio.database;

import esercizio.tempo.*;
import java.sql.*;

public class TempoDAO {

    // CREATE MESE: Inserisce un nuovo Mese e restituisce l'ID generato
    public int creaMese(int numeroGiorni) {
        String sql = "INSERT INTO Mese (numero_giorni) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, numeroGiorni);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                System.out.println("[DB] Mese creato con ID: " + id);
                return id;
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE CREA MESE] " + e.getMessage());
        }
        return -1;
    }

    // CREATE/UPDATE GIORNO: Salva o aggiorna le ore di un giorno specifico
    public void salvaOreGiorno(int meseId, int numeroGiorno, double ore, String note) {
        String sql = "INSERT INTO Giorno (mese_id, numero_giorno, ore_lavorate, note) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE ore_lavorate = ?, note = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, meseId);
            stmt.setInt(2, numeroGiorno);
            stmt.setDouble(3, ore);
            stmt.setString(4, note);
            stmt.setDouble(5, ore);
            stmt.setString(6, note);

            stmt.executeUpdate();
            System.out.println("[DB] Ore registrate per il giorno " + numeroGiorno);

        } catch (SQLException e) {
            System.err.println("[ERRORE SALVA ORE] " + e.getMessage());
        }
    }

    // READ: Recupera il Mese completo con tutti i suoi Giorni dal DB
    public Mese getMeseCompleto(int meseId) {
        String sqlMese = "SELECT numero_giorni FROM Mese WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtMese = conn.prepareStatement(sqlMese)) {

            stmtMese.setInt(1, meseId);
            ResultSet rsMese = stmtMese.executeQuery();

            if (rsMese.next()) {
                int numeroGiorni = rsMese.getInt("numero_giorni");
                Mese m = new Mese(numeroGiorni);

                String sqlGiorni = "SELECT numero_giorno, ore_lavorate, note " +
                                   "FROM Giorno WHERE mese_id = ?";

                try (PreparedStatement stmtGiorni = conn.prepareStatement(sqlGiorni)) {
                    stmtGiorni.setInt(1, meseId);
                    ResultSet rsGiorni = stmtGiorni.executeQuery();

                    while (rsGiorni.next()) {
                        int numGiorno = rsGiorni.getInt("numero_giorno");
                        double ore    = rsGiorni.getDouble("ore_lavorate");
                        m.aggiungiOreGiorno(numGiorno, ore);
                    }
                }
                return m;
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE GET MESE] " + e.getMessage());
        }
        return null;
    }

    // DELETE: Elimina un Mese e tutti i suoi giorni (se CASCADE è attivo)
    public void deleteMese(int meseId) {
        String sql = "DELETE FROM Mese WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, meseId);
            int righeColpite = stmt.executeUpdate();
            if (righeColpite > 0) {
                System.out.println("[DB] Mese eliminato con successo.");
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE DELETE MESE] " + e.getMessage());
        }
    }
}