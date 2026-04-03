package com.workinghoursmanagement.model;

public class Persona 
{
    private String nome;
    private int eta;

    public Persona(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    public String descriviRuolo() 
    {
        return "Sono una persona";
    }

       public String getNome() {
           return nome;
       }

       public void setNome(String nome) {
           this.nome = nome;
       }

       public int getEta() {
           return eta;
       }

       public void setEta(int eta) {
           this.eta = eta;
       }

    
}
