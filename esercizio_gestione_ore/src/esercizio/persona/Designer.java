package esercizio.persona;

public class Designer extends Persona {
    private String tool;

    public Designer(int id, String nome, int eta, String tool) {
        super(id, nome, eta);
        this.tool = tool;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    @Override
    public String descriviRuolo() {
        return "Sono un Designer e uso " + tool+" come tool";
    }
}