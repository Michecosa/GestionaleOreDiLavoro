package com.workinghoursmanagement.model;

import java.util.ArrayList;

public class Mese {   
    private int id; // L'id del mese nel database
    private int personaId; // Collega il mese a una Persona
    private ArrayList<Giorno> giorni;

    public Mese(int numeroGiorni, int personaId) {
        this.personaId = personaId; // Salva l'id della persona
        this.giorni = new ArrayList<>();

        for (int i = 0; i < numeroGiorni; i++) {
            // Passiamo 0 ore e nota vuota come base
            giorni.add(new Giorno(i + 1, 0, ""));
        }
    }

    // Getter e Setter per personaId
    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Giorno> getGiorni() {
        return giorni;
    }


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
}