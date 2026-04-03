package com.workinghoursmanagement.model;

public class Giorno {

    
    private int numeroGiorno;
    private double oreLavorate;
    private String note;

    
    public Giorno(int numeroGiorno,double oreLavorate,String note) {
        this.numeroGiorno = numeroGiorno;
        this.oreLavorate = 0;
        this.note=note;

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