package com.workinghoursmanagement.model;

import java.util.ArrayList;

public class Mese 
{   
    private ArrayList<Giorno> giorni;

    //costruttore
    public Mese(int numeroGiorni) {
        giorni = new ArrayList<>();

        // Corretto: aggiunge i giorni con ore iniziali 0 e note vuote
        for (int i = 0; i < numeroGiorni; i++) {
            giorni.add(new Giorno(i + 1, 0, ""));
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