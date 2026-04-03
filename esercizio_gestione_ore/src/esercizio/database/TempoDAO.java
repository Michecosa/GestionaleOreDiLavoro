package esercizio.database;

import esercizio.tempo.*;
import java.sql.*;
import java.util.ArrayList;

public class TempoDAO {

    // Classe di supporto per popolare la combo dei mesi
    public static class MeseInfo {
        public final int    id;
        public final String etichetta;

        public MeseInfo(int id, int meseCalendario, int anno) {
            this.id       = id;
            String[] nomi = {"","Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
                              "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"};
            this.etichetta = nomi[meseCalendario] + " " + anno + "  (ID foglio: " + id + ")";
        }

        @Override public String toString() { return etichetta; }
    }

    // READ: Restituisce i mesi registrati per un dato dipendente
    public ArrayList<MeseInfo> getMesiPerPersona(int personaId) {
        ArrayList<MeseInfo> lista = new ArrayList<>();
        String sql = "SELECT id, mese_calendario, anno FROM Mese " +
                     "WHERE persona_id = ? ORDER BY anno, mese_calendario";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, personaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new MeseInfo(
                    rs.getInt("id"),
                    rs.getInt("mese_calendario"),
                    rs.getInt("anno")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE GET MESI PER PERSONA] " + e.getMessage());
        }
        return lista;
    }

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
    public boolean salvaOreGiorno(int meseId, int numeroGiorno, double ore, String note) {
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
            return true;

        } catch (SQLException e) {
            System.err.println("[ERRORE SALVA ORE] " + e.getMessage());
            return false;
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
                        String note   = rsGiorni.getString("note");
                        m.aggiungiOreGiorno(numGiorno, ore, note);
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