package esercizio.main;

import esercizio.database.PersonaDAO;
import esercizio.database.TempoDAO;
import esercizio.persona.Sviluppatore;
import esercizio.tempo.Mese;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        PersonaDAO personaDAO = new PersonaDAO();
        TempoDAO tempoDAO = new TempoDAO();

        while (true) {
            String menu = "Benvenuto nel Gestionale Ore\n\n" +
                          "1. Aggiungi Sviluppatore\n" +
                          "2. Crea un nuovo Mese per una persona\n" +
                          "3. Inserisci ore in un giorno\n" +
                          "0. Esci\n\n" +
                          "Scegli un'opzione:";

            String sceltaStr = JOptionPane.showInputDialog(null, menu, "Menu Principale", JOptionPane.QUESTION_MESSAGE);

            // Gestione chiusura finestra o annulla
            if (sceltaStr == null || sceltaStr.equals("0")) {
                JOptionPane.showMessageDialog(null, "Arrivederci!");
                break;
            }

            int scelta = Integer.parseInt(sceltaStr);

            switch (scelta) {
                case 1:
                    String nome = JOptionPane.showInputDialog("Inserisci il nome:");
                    int eta = Integer.parseInt(JOptionPane.showInputDialog("Inserisci l'età:"));
                    String linguaggio = JOptionPane.showInputDialog("Inserisci il linguaggio:");
                    
                    Sviluppatore dev = new Sviluppatore(0, nome, eta, linguaggio);
                    personaDAO.createSviluppatore(dev);
                    JOptionPane.showMessageDialog(null, "Sviluppatore aggiunto con successo!");
                    break;

                case 2:
                    // Recupero le persone per far scegliere all'utente
                    ArrayList<String> persone = personaDAO.getPersonePerMenu();
                    String personaScelta = (String) JOptionPane.showInputDialog(null, 
                            "Seleziona il dipendente:", "Scelta Dipendente",
                            JOptionPane.QUESTION_MESSAGE, null, 
                            persone.toArray(), persone.get(0));

                    if (personaScelta != null) {
                        // Estraggo l'ID dalla stringa "1 - Alice Rossi"
                        int personaId = Integer.parseInt(personaScelta.split(" - ")[0]);
                        
                        int meseCal = Integer.parseInt(JOptionPane.showInputDialog("Inserisci numero mese (1-12):"));
                        int anno = Integer.parseInt(JOptionPane.showInputDialog("Inserisci anno (es. 2026):"));
                        int giorni = Integer.parseInt(JOptionPane.showInputDialog("Quanti giorni ha questo mese?"));

                        Mese nuovoMese = new Mese(meseCal, anno, giorni, personaId);
                        int nuovoMeseId = tempoDAO.creaMese(nuovoMese);
                        
                        JOptionPane.showMessageDialog(null, "Mese creato! L'ID di questo foglio ore è: " + nuovoMeseId);
                    }
                    break;

                case 3:
                    // Richiede l'ID del foglio ore (Mese)
                    int meseId = Integer.parseInt(JOptionPane.showInputDialog("Inserisci l'ID del Mese (foglio ore):"));
                    int giorno = Integer.parseInt(JOptionPane.showInputDialog("Quale giorno del mese?"));
                    double ore = Double.parseDouble(JOptionPane.showInputDialog("Quante ore?"));
                    String note = JOptionPane.showInputDialog("Inserisci le note:");

                    tempoDAO.salvaOreGiorno(meseId, giorno, ore, note);
                    JOptionPane.showMessageDialog(null, "Ore salvate nel database!");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Scelta non valida");
            }
        }
    }
}