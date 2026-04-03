package esercizio.database;

import esercizio.tempo.*;
import java.sql.*;

public class TempoDAO {

    // CREATE MESE
    public int creaMese(Mese m) {
        String sql = "INSERT INTO Mese (mese_calendario, anno, numero_giorni, persona_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, m.getMeseCalendario());
            stmt.setInt(2, m.getAnno());
            stmt.setInt(3, m.getNumeroGiorni());
            stmt.setInt(4, m.getPersonaId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                m.setId(id); // Aggiorna l'oggetto Java con l'ID del DB
                System.out.println("[DB] Mese creato con ID: " + id);
                return id;
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE CREA MESE] " + e.getMessage());
        }
        return -1;
    }

    // CREATE/UPDATE GIORNO
    public void salvaOreGiorno(int meseId, int numeroGiorno, double ore, String note) {
        String sql = "INSERT INTO Giorno (mese_id, numero_giorno, ore_lavorate, note) " +
                     "VALUES (?, ?, ?, ?)"; // Semplificato per evitare errori di vincoli

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, meseId);
            stmt.setInt(2, numeroGiorno);
            stmt.setDouble(3, ore);
            stmt.setString(4, note);

            stmt.executeUpdate();
            System.out.println("[DB] Ore registrate per il giorno " + numeroGiorno);

        } catch (SQLException e) {
            System.err.println("[ERRORE SALVA ORE] " + e.getMessage());
        }
    }

    // READ
    public Mese getMeseCompleto(int meseId) {
        String sqlMese = "SELECT mese_calendario, anno, numero_giorni, persona_id FROM Mese WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtMese = conn.prepareStatement(sqlMese)) {

            stmtMese.setInt(1, meseId);
            ResultSet rsMese = stmtMese.executeQuery();

            if (rsMese.next()) {
                int meseCal = rsMese.getInt("mese_calendario");
                int anno = rsMese.getInt("anno");
                int numeroGiorni = rsMese.getInt("numero_giorni");
                int personaId = rsMese.getInt("persona_id");
                
                Mese m = new Mese(meseCal, anno, numeroGiorni, personaId);
                m.setId(meseId);

                String sqlGiorni = "SELECT numero_giorno, ore_lavorate, note FROM Giorno WHERE mese_id = ?";
                try (PreparedStatement stmtGiorni = conn.prepareStatement(sqlGiorni)) {
                    stmtGiorni.setInt(1, meseId);
                    ResultSet rsGiorni = stmtGiorni.executeQuery();

                    while (rsGiorni.next()) {
                        int numGiorno = rsGiorni.getInt("numero_giorno");
                        double ore    = rsGiorni.getDouble("ore_lavorate");
                        m.aggiungiOreGiorno(numGiorno, ore); // Aggiorna i giorni fittizi
                    }
                }
                return m;
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE GET MESE] " + e.getMessage());
        }
        return null;
    }
}