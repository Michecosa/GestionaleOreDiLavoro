package esercizio.tempo;

public class Giorno {

    private int id; // corrisponde a 'id' nel db
    private int numeroGiorno;
    private double oreLavorate;
    private String note;
    private int medeId; // fk verso la tabella del mese

    // costruttore per nuovi inserimenti (senza id, gestito dal db)
    public Giorno(int numeroGiorno, double oreLavorate, String note, int meseId) {
        this.numeroGiorno = numeroGiorno;
        this.oreLavorate = oreLavorate;
        this.note = note;
        this.meseId = meseId;
    }

    // Costruttore per dati letti dal DB (con ID)
    public Giorno(int id, int numeroGiorno, double oreLavorate, String note, int meseId) {
        this.id = id;
        this.numeroGiorno = numeroGiorno;
        this.oreLavorate = oreLavorate;
        this.note = note;
        this.meseId = meseId;
    }
    
    public int getNumeroGiorno() {
        return numeroGiorno;
    }
    public double getOreLavorate() {
        return oreLavorate;
    }
    public void setNumeroGiorno(int numeroGiorno) {
        this.numeroGiorno = numeroGiorno;
    }
    public void setOreLavorate(double oreLavorate) {
        this.oreLavorate = oreLavorate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMeseId() { return meseId; }
    public void setMeseId(int meseId) { this.meseId = meseId; }



    public void aggiungiOre(double ore) {
        if (ore > 0) {
            oreLavorate += ore;
        } else {
            System.out.println("Ore non valide");
        }
    }

    
    public void resetta() {
        oreLavorate = 0;
    }

    
       @Override
    public String toString() {
        return "Giorno " + numeroGiorno +
               " | Ore: " + oreLavorate +
               " | Note: " + note;
    }


    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }
}