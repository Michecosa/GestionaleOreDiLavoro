package esercizio.database;

import esercizio.persona.*;
import java.sql.*;
import java.util.ArrayList;

public class PersonaDAO {

    // Classe di supporto per la visualizzazione completa nella tabella
    public static class PersonaInfo {
        public final int    id;
        public final String nome;
        public final int    eta;
        public final String ruolo;

        public PersonaInfo(int id, String nome, int eta, String ruolo) {
            this.id    = id;
            this.nome  = nome;
            this.eta   = eta;
            this.ruolo = ruolo;
        }
    }

    // CREATE: Inserisce una nuova persona (nella tabella base Persona)
    public void create(Persona p) {
        String sql = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getNome());
            stmt.setInt(2, p.getEta());
            stmt.executeUpdate();

            System.out.println("[DB] Persona inserita con successo.");

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE] " + e.getMessage());
        }
    }

    // CREATE SVILUPPATORE: Inserisce in Persona + Sviluppatore
    public boolean createSviluppatore(Sviluppatore s) {
        String sqlPersona = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";
        String sqlSviluppatore = "INSERT INTO Sviluppatore (id, linguaggio) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtP = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                stmtP.setString(1, s.getNome());
                stmtP.setInt(2, s.getEta());
                stmtP.executeUpdate();

                ResultSet keys = stmtP.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);

                    try (PreparedStatement stmtS = conn.prepareStatement(sqlSviluppatore)) {
                        stmtS.setInt(1, id);
                        stmtS.setString(2, s.getLinguaggio());
                        stmtS.executeUpdate();
                    }
                }
            }

            conn.commit();
            System.out.println("[DB] Sviluppatore inserito con successo.");
            return true;

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE SVILUPPATORE] " + e.getMessage());
            return false;
        }
    }

    // CREATE DESIGNER: Inserisce in Persona + Designer
    public boolean createDesigner(Designer d) {
        String sqlPersona = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";
        String sqlDesigner = "INSERT INTO Designer (id, tool) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtP = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                stmtP.setString(1, d.getNome());
                stmtP.setInt(2, d.getEta());
                stmtP.executeUpdate();

                ResultSet keys = stmtP.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);

                    try (PreparedStatement stmtD = conn.prepareStatement(sqlDesigner)) {
                        stmtD.setInt(1, id);
                        stmtD.setString(2, d.getTool());
                        stmtD.executeUpdate();
                    }
                }
            }

            conn.commit();
            System.out.println("[DB] Designer inserito con successo.");
            return true;

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE DESIGNER] " + e.getMessage());
            return false;
        }
    }

    // CREATE TESTER: Inserisce in Persona + Tester
    public boolean createTester(Tester t) {
        String sqlPersona = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";
        String sqlTester = "INSERT INTO Tester (id, tipo_test) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtP = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                stmtP.setString(1, t.getNome());
                stmtP.setInt(2, t.getEta());
                stmtP.executeUpdate();

                ResultSet keys = stmtP.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);

                    try (PreparedStatement stmtT = conn.prepareStatement(sqlTester)) {
                        stmtT.setInt(1, id);
                        stmtT.setString(2, t.getTipoTest());
                        stmtT.executeUpdate();
                    }
                }
            }

            conn.commit();
            System.out.println("[DB] Tester inserito con successo.");
            return true;

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE TESTER] " + e.getMessage());
            return false;
        }
    }

    // READ: Restituisce ID e Nome concatenati per mostrarli nel JOptionPane
    public ArrayList<String> getPersonePerMenu() {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM Persona";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Formato: "1 - Alice Rossi"
                lista.add(rs.getInt("id") + " - " + rs.getString("nome"));
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE READ] " + e.getMessage());
        }
        return lista;
    }

    // READ COMPLETO: Restituisce ID, Nome, Età e Ruolo per la tabella dipendenti
    public ArrayList<PersonaInfo> getPersoneCompleto() {
        ArrayList<PersonaInfo> lista = new ArrayList<>();
        String sql =
            "SELECT p.id, p.nome, p.eta, " +
            "       CASE " +
            "           WHEN s.id IS NOT NULL THEN 'Sviluppatore' " +
            "           WHEN d.id IS NOT NULL THEN 'Designer' " +
            "           WHEN t.id IS NOT NULL THEN 'Tester' " +
            "           ELSE 'N/D' " +
            "       END AS ruolo " +
            "FROM Persona p " +
            "LEFT JOIN Sviluppatore s ON p.id = s.id " +
            "LEFT JOIN Designer     d ON p.id = d.id " +
            "LEFT JOIN Tester       t ON p.id = t.id " +
            "ORDER BY p.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new PersonaInfo(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("eta"),
                    rs.getString("ruolo")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE READ COMPLETO] " + e.getMessage());
        }
        return lista;
    }

    // READ: Restituisce tutti i nomi dalla tabella Persona
    public ArrayList<String> getAllNames() {
        ArrayList<String> nomi = new ArrayList<>();
        String sql = "SELECT nome FROM Persona";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                nomi.add(rs.getString("nome"));
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE READ] " + e.getMessage());
        }
        return nomi;
    }

    // UPDATE: Aggiorna l'età tramite ID
    public boolean updateEta(int id, int nuovaEta) {
        String sql = "UPDATE Persona SET eta = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuovaEta);
            stmt.setInt(2, id);

            int righeColpite = stmt.executeUpdate();
            if (righeColpite > 0) {
                System.out.println("[DB] Età aggiornata per ID: " + id);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("[ERRORE UPDATE] " + e.getMessage());
            return false;
        }
    }

    // DELETE: Rimuove una persona tramite ID (CASCADE sulle sottotabelle)
    public boolean delete(int id) {
        String sql = "DELETE FROM Persona WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int righeColpite = stmt.executeUpdate();
            if (righeColpite > 0) {
                System.out.println("[DB] Persona eliminata con successo.");
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("[ERRORE DELETE] " + e.getMessage());
            return false;
        }
    }
}