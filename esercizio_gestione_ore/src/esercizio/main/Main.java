package esercizio.main;

import esercizio.database.*;
import esercizio.persona.*;
import esercizio.tempo.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        PersonaDAO personaDAO = new PersonaDAO();
        TempoDAO tempoDAO = new TempoDAO();

        System.out.println("=== TEST PERSONA DAO ===\n");

        // 1. Creo le persone
        Sviluppatore s = new Sviluppatore("Mario Rossi", 30, "Java");
        Designer d = new Designer("Luca Bianchi", 25, "Figma");
        Tester t = new Tester("Anna Verdi", 28, "Unit Testing");

        personaDAO.createSviluppatore(s);
        personaDAO.createDesigner(d);
        personaDAO.createTester(t);

        // 2. Leggo tutti i nomi
        System.out.println("\n--- Nomi nel DB ---");
        ArrayList<String> nomi = personaDAO.getAllNames();
        for (String nome : nomi) {
            System.out.println("  - " + nome);
        }

        // 3. Aggiorno l'età della persona con ID 1
        System.out.println("\n--- Update età ID=1 ---");
        personaDAO.updateEta(1, 35);

        // 4. Elimino la persona con ID 3
        System.out.println("\n--- Delete ID=3 ---");
        personaDAO.delete(3);

        // 5. Rileggo i nomi dopo delete
        System.out.println("\n--- Nomi dopo delete ---");
        nomi = personaDAO.getAllNames();
        for (String nome : nomi) {
            System.out.println("  - " + nome);
        }

        System.out.println("\n=== TEST TEMPO DAO ===\n");

        // 6. Creo un mese di 30 giorni
        System.out.println("--- Creazione Mese ---");
        int meseId = tempoDAO.creaMese(30);

        // 7. Salvo le ore per alcuni giorni
        System.out.println("\n--- Salvataggio ore ---");
        tempoDAO.salvaOreGiorno(meseId, 1,  8.0, "Prima giornata");
        tempoDAO.salvaOreGiorno(meseId, 2,  7.5, "Riunione al mattino");
        tempoDAO.salvaOreGiorno(meseId, 3,  9.0, "Straordinario");
        tempoDAO.salvaOreGiorno(meseId, 10, 8.0, "");

        // 8. Aggiorno il giorno 2 (ON DUPLICATE KEY UPDATE)
        System.out.println("\n--- Update giorno 2 ---");
        tempoDAO.salvaOreGiorno(meseId, 2, 6.0, "Aggiornato: uscita anticipata");

        // 9. Recupero il mese completo e lo stampo
        System.out.println("\n--- Mese completo dal DB ---");
        Mese meseRecuperato = tempoDAO.getMeseCompleto(meseId);
        if (meseRecuperato != null) {
            meseRecuperato.stampaMese();
            System.out.println("Ore totali: " + meseRecuperato.calcolaOreTotali());
        }

        // 10. Elimino il mese
        System.out.println("\n--- Delete Mese ---");
        tempoDAO.deleteMese(meseId);

        // 11. Verifico che il mese sia stato eliminato
        System.out.println("\n--- Verifica delete Mese ---");
        Mese meseEliminato = tempoDAO.getMeseCompleto(meseId);
        if (meseEliminato == null) {
            System.out.println("[OK] Mese eliminato correttamente");
        }

        System.out.println("\n=== FINE TEST ===");
    }
}