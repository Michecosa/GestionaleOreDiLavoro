package esercizio.persona;

public class Tester extends Persona {
    private String tipoTest;

    public Tester(String nome, int eta, String tipoTest) {
        super(nome, eta);
        this.tipoTest = tipoTest;
    }

    public String getTipoTest() {
        return tipoTest;
    }

    public void setTipoTest(String tipoTest) {
        this.tipoTest = tipoTest;
    }

    @Override
    public String descriviRuolo() {
        return "Sono un Tester e faccio " + tipoTest;
    }
}