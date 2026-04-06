package service;
// Definisce il package in cui si trova questa classe

import dao.ShiftDAO;
// Importa la classe ShiftDAO per accedere ai dati dei turni nel database

import model.Shift;
// Importa il modello Shift che rappresenta un turno lavorativo

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
// Import classi per gestire date, mesi e strutture dati come List e Map

public class ShiftService {
    // Classe di servizio che gestisce la logica dei turni
    // Interagisce con il DAO per ottenere e organizzare i dati dei turni

    private ShiftDAO dao = new ShiftDAO();
    // Crea un'istanza di ShiftDAO per comunicare con il database

    public List<Shift> getShiftsByMonth(YearMonth month) {
        // Restituisce tutti i turni di un determinato mese
        LocalDate start = month.atDay(1);
        // Primo giorno del mese

        LocalDate end = month.atEndOfMonth();
        // Ultimo giorno del mese

        return dao.getShiftsBetween(start, end);
        // Chiama il DAO per ottenere i turni tra start e end (compresi)
    }

    public Map<LocalDate, List<Shift>> groupByDate(List<Shift> shifts) {
        // Raggruppa una lista di turni per data
        Map<LocalDate, List<Shift>> map = new HashMap<>();
        // Mappa che associa ogni data alla lista dei turni di quel giorno

        for (Shift s : shifts)
            map.computeIfAbsent(s.getDate(), k -> new ArrayList<>()).add(s);
            // Per ogni turno:
            // - se la data non esiste nella mappa, crea una nuova lista vuota
            // - aggiunge il turno alla lista corrispondente alla sua data

        return map;
        // Restituisce la mappa completa: LocalDate -> List<Shift>
    }
}