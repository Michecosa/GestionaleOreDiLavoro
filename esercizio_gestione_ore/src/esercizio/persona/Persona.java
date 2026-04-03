package esercizio.persona;

public class Persona 
{
    private int id; // aggiunto per matchare il db
    private String nome;
    private int eta;

    // Costruttore per quando crei una persona nuova (ID gestito dal DB)
    public Persona(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    // Costruttore per quando carichi una persona dal DB (ID già esistente)
    public Persona(int id, String nome, int eta) {
        this.id = id;
        this.nome = nome;
        this.eta = eta;
    }

    // Getter e Setter per l'ID
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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
