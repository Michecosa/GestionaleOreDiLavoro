package model;
// Definisce il package in cui si trova questa classe

import java.time.*;
import java.util.*;
// Importa classi per la gestione di date e collezioni

public class CalendarMonth {
    // Classe modello che rappresenta un mese del calendario organizzato in settimane

    private YearMonth yearMonth;
    // Il mese e l'anno sono rappresentati da questa istanza

    private List<List<LocalDate>> weeks;
    // Lista di settimane, dove ogni settimana è una lista di LocalDate
    // I giorni vuoti (prima o dopo il mese) sono rappresentati con null

    public CalendarMonth(YearMonth yearMonth) {
        // Costruttore che riceve un YearMonth
        this.yearMonth = yearMonth;
        this.weeks = generateCalendar();
        // Genera automaticamente la struttura settimanale del mese
    }

    private List<List<LocalDate>> generateCalendar() {
        // Metodo privato per creare la lista delle settimane del mese
        List<List<LocalDate>> calendar = new ArrayList<>();

        LocalDate firstDay = yearMonth.atDay(1);
        // Primo giorno del mese

        int startDay = firstDay.getDayOfWeek().getValue();
        // Giorno della settimana del primo giorno (1=Monday, 7=Sunday)

        List<LocalDate> week = new ArrayList<>();
        for (int i = 1; i < startDay; i++) week.add(null);
        // Inserisce null per i giorni vuoti prima del primo giorno del mese nella prima settimana

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            week.add(yearMonth.atDay(day));
            // Aggiunge il giorno corrente alla settimana

            if (week.size() == 7) {
                calendar.add(week);
                // Se la settimana ha 7 giorni, l'aggiunge al calendario
                week = new ArrayList<>();
                // Inizia una nuova settimana
            }
        }

        while (week.size() < 7) week.add(null);
        // Completa l'ultima settimana con null se necessario
        calendar.add(week);
        // Aggiunge l'ultima settimana al calendario

        return calendar;
        // Restituisce la lista completa di settimane
    }

    public List<List<LocalDate>> getWeeks() { return weeks; }
    // Getter per ottenere le settimane del mese

    public YearMonth getPreviousMonth() { return yearMonth.minusMonths(1); }
    // Getter per ottenere il mese precedente

    public YearMonth getNextMonth() { return yearMonth.plusMonths(1); }
    // Getter per ottenere il mese successivo

    public YearMonth getYearMonth() { return yearMonth; }
    // Getter per ottenere l'oggetto YearMonth corrente
}