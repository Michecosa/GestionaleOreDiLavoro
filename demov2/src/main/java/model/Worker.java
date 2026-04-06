package model;
// Definisce il package in cui si trova la classe

public class Worker {
    // Classe modello che rappresenta un lavoratore nel sistema

    private int id;
    // Identificativo unico del lavoratore

    private String name;
    // Nome del lavoratore

    private int specializationId;
    // Identificativo della specializzazione del lavoratore (es. Full-Stack Developer, Tester)

    public Worker(int id, String name, int specializationId) {
        // Costruttore per inizializzare un oggetto Worker con id, nome e specializzazione
        this.id = id;
        this.name = name;
        this.specializationId = specializationId;
    }

    public int getId() { return id; }
    // Getter per ottenere l'id del lavoratore

    public String getName() { return name; }
    // Getter per ottenere il nome del lavoratore

    public int getSpecializationId() { return specializationId; }
    // Getter per ottenere l'id della specializzazione del lavoratore
}