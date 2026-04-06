package controller;
// Definisce il package in cui si trova questa classe

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
// Importa le classi necessarie per creare servlet Jakarta EE

import java.io.IOException;
import java.time.*;
import java.util.*;
// Importa classi per gestione eccezioni I/O, date e collezioni

import model.*;
import service.ShiftService;
// Importa i modelli (Shift, CalendarMonth) e il servizio ShiftService

@WebServlet("/shifts")
// Mappa la servlet all’URL /shifts

public class ShiftCalendarController extends HttpServlet {
    // Servlet che gestisce la visualizzazione del calendario dei turni

    private ShiftService service = new ShiftService();
    // Istanza del service per gestire la logica dei turni

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Gestisce le richieste GET per visualizzare il calendario dei turni

        String monthParam = req.getParameter("month");
        // Legge il parametro "month" dalla richiesta (formato YearMonth: "2026-04")

        YearMonth month;
        try {
            month = (monthParam != null) ? YearMonth.parse(monthParam) : YearMonth.now();
            // Se il parametro è presente, lo converte in YearMonth
            // Altrimenti usa il mese corrente
        } catch (Exception e) {
            month = YearMonth.now();
            // In caso di formato errato, usa il mese corrente
        }

        List<Shift> shifts = service.getShiftsByMonth(month);
        // Recupera tutti i turni del mese dal service

        Map<LocalDate, List<Shift>> grouped = service.groupByDate(shifts);
        // Raggruppa i turni per data, utile per mostrare nel calendario

        CalendarMonth calendar = new CalendarMonth(month);
        // Crea un oggetto CalendarMonth per costruire la struttura del mese in settimane

        req.setAttribute("calendar", calendar);
        // Imposta l'oggetto calendario come attributo della richiesta

        req.setAttribute("shiftsByDate", grouped);
        // Imposta la mappa dei turni raggruppati per data come attributo della richiesta

        req.getRequestDispatcher("shifts.jsp").forward(req, resp);
        // Invia la richiesta e gli attributi alla JSP per la visualizzazione del calendario
    }
}