package esercizio.persona;

public class Sviluppatore extends Persona
{   
    private String linguaggio;

    public Sviluppatore(int id, String nome,int eta,String linguaggio) {
        super(id, nome, eta);
        this.linguaggio=linguaggio;
    }

    @Override
    public String descriviRuolo() 
    {
        return "Sono un Sviluppatore in " + linguaggio;
    }


    public String getLinguaggio() {
        return linguaggio;
    }

    public void setLinguaggio(String linguaggio) {
        this.linguaggio = linguaggio;
    }
    

    

    

  
}
