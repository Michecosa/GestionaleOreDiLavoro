package esercizio.tempo;

import java.util.ArrayList;

public class Mese {   
    private int id;                 // id univoco generato dal db
    private int meseCalendario;     // 1=Gennaio, 2=Febbraio, ecc...
    private int anno;               // es. 2026
    private int personaId;          // fk collegata alla tabella persona
    private int numeroGiorni;       // colonna numero_giorni nel db
    private ArrayList<Giorno> giorni;

    // --- COSTRUTTORE ---
    public Mese(int meseCalendario, int anno, int numeroGiorni, int personaId) {
        this.meseCalendario = meseCalendario;
        this.anno = anno;
        this.numeroGiorni = numeroGiorni;
        this.personaId = personaId;
        this.giorni = new ArrayList<>();

        // Inizializzazione automatica dei giorni
        for (int i = 0; i < numeroGiorni; i++) {
            this.giorni.add(new Giorno(i + 1, 0, "", 0)); 
        }
    }

    // --- GETTER E SETTER ---
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeseCalendario() {
        return meseCalendario;
    }

    public void setMeseCalendario(int meseCalendario) {
        this.meseCalendario = meseCalendario;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public int getNumeroGiorni() {
        return numeroGiorni;
    }

    public void setNumeroGiorni(int numeroGiorni) {
        this.numeroGiorni = numeroGiorni;
    }

    public ArrayList<Giorno> getGiorni() {
        return giorni; 
    }

    public void setGiorni(ArrayList<Giorno> giorni) {
        this.giorni = giorni;
    }


    // Aggiungo le ore a un giorno
    public void aggiungiOreGiorno(int numeroGiorno, double ore) {
        aggiungiOreGiorno(numeroGiorno, ore, "");
    }

    // Aggiungo le ore e la nota a un giorno
    public void aggiungiOreGiorno(int numeroGiorno, double ore, String nota) {
        if (numeroGiorno >= 1 && numeroGiorno <= giorni.size()) {
            Giorno g = giorni.get(numeroGiorno - 1);
            g.aggiungiOre(ore);
            g.setNote(nota);
        } else {
            System.out.println("Giorno non valido");
        }
    }

    public double calcolaOreTotali() {
        double totale = 0;
        for (Giorno g : giorni) {
            totale += g.getOreLavorate();
        }
        return totale;
    }

    public void stampaMese() {
        // stampa per includere il mese, l'anno e la persona
        System.out.println("Prospetto Mese: " + meseCalendario + "/" + anno + " | Persona ID: " + personaId);
        for (Giorno g : giorni) {
            System.out.println(g);
        }
    }

    public void resettaMese() {
        for (Giorno g : giorni) {
            g.resetta();
        }
    }
}