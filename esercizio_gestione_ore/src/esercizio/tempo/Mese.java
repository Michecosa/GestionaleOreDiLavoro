package esercizio.tempo;

import java.util.ArrayList;

public class Mese 
{   
    private int id; // id tabella mese
    private int personaId; // fk collegata alla tabella persona
    private int numeroGiorni; // colonna numero_giorni nel db
    private ArrayList<Giorno> giorni;




    // --- GETTER E SETTER ---
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonaId() {
        return personaId;
    }

    public ArrayList<Giorno> getGiorni() {
        return giorni; 
    }




    //costruttore
    public Mese(int numeroGiorni, int personaId) {
        this.numeroGiorni = numeroGiorni;
        this.personaId = personaId;
        this.giorni = new ArrayList<>();

        // Inizializzazione automatica dei giorni
        for (int i = 0; i < numeroGiorni; i++) {
            this.giorni.add(new Giorno(i + 1, 0, "")); 
        }
    }

    // aggiungo le ore a un giorno
    public void aggiungiOreGiorno(int numeroGiorno, double ore) {
        if (numeroGiorno >= 1 && numeroGiorno <= giorni.size()) {
            giorni.get(numeroGiorno - 1).aggiungiOre(ore);
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