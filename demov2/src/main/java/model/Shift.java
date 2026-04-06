package model;
// Definisce il package in cui si trova questa classe

import java.time.LocalDate;
// Importa la classe LocalDate per gestire le date dei turni

public class Shift {
    // Classe modello che rappresenta un turno lavorativo

    private LocalDate date;
    // Data del turno

    private Worker worker;
    // Lavoratore assegnato a questo turno (oggetto Worker)

    private String shiftType;
    // Tipo di turno, ad esempio "Mattina", "Pomeriggio", "Notte"

    public Shift(LocalDate date, Worker worker, String shiftType) {
        // Costruttore per inizializzare un oggetto Shift con data, lavoratore e tipo di turno
        this.date = date;
        this.worker = worker;
        this.shiftType = shiftType;
    }

    public LocalDate getDate() { return date; }
    // Getter per ottenere la data del turno

    public Worker getWorker() { return worker; }
    // Getter per ottenere il lavoratore assegnato al turno

    public String getShiftType() { return shiftType; }
    // Getter per ottenere il tipo di turno
}