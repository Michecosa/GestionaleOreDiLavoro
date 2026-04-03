package com.workinghoursmanagement.service;

import java.sql.*;
import java.util.ArrayList;

import com.workinghoursmanagement.model.*;

public class PersonaDAO {

    // CREATE: Inserisce una nuova persona (nella tabella base Persona)
    public void create(Persona p) {
        String sql = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";

        try (Connection conn = DbConn.getConnection();
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
    public void createSviluppatore(Sviluppatore s) {
        String sqlPersona = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";
        String sqlSviluppatore = "INSERT INTO Sviluppatore (id, linguaggio) VALUES (?, ?)";

        try (Connection conn = DbConn.getConnection()) {
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

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE SVILUPPATORE] " + e.getMessage());
        }
    }

    // CREATE DESIGNER: Inserisce in Persona + Designer
    public void createDesigner(Designer d) {
        String sqlPersona = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";
        String sqlDesigner = "INSERT INTO Designer (id, tool) VALUES (?, ?)";

        try (Connection conn = DbConn.getConnection()) {
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

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE DESIGNER] " + e.getMessage());
        }
    }

    // CREATE TESTER: Inserisce in Persona + Tester
    public void createTester(Tester t) {
        String sqlPersona = "INSERT INTO Persona (nome, eta) VALUES (?, ?)";
        String sqlTester = "INSERT INTO Tester (id, tipo_test) VALUES (?, ?)";

        try (Connection conn = DbConn.getConnection()) {
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

        } catch (SQLException e) {
            System.err.println("[ERRORE CREATE TESTER] " + e.getMessage());
        }
    }

    // READ: Restituisce tutti i nomi dalla tabella Persona
    public ArrayList<String> getAllNames() {
        ArrayList<String> nomi = new ArrayList<>();
        String sql = "SELECT nome FROM Persona";

        try (Connection conn = DbConn.getConnection();
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
    public void updateEta(int id, int nuovaEta) {
        String sql = "UPDATE Persona SET eta = ? WHERE id = ?";

        try (Connection conn = DbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuovaEta);
            stmt.setInt(2, id);

            int righeColpite = stmt.executeUpdate();
            if (righeColpite > 0) {
                System.out.println("[DB] Età aggiornata per ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE UPDATE] " + e.getMessage());
        }
    }

    // DELETE: Rimuove una persona tramite ID (CASCADE sulle sottotabelle)
    public void delete(int id) {
        String sql = "DELETE FROM Persona WHERE id = ?";

        try (Connection conn = DbConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int righeColpite = stmt.executeUpdate();
            if (righeColpite > 0) {
                System.out.println("[DB] Persona eliminata con successo.");
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE DELETE] " + e.getMessage());
        }
    }


    public String getNomeById(int id) {
        String sql = "SELECT nome FROM Persona WHERE id = ?";
        try (Connection conn = DbConn.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERRORE getNomeById] " + e.getMessage());
        }
        return "Utente Sconosciuto";
    }
}