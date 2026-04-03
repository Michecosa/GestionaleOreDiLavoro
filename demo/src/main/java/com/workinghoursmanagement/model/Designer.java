package com.workinghoursmanagement.model;

public class Designer extends Persona {
    private String tool;

    public Designer(String nome, int eta, String tool) {
        super(nome, eta);
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