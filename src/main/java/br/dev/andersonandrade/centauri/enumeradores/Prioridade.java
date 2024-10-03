package br.dev.andersonandrade.centauri.enumeradores;

public enum Prioridade {
    URGENTE("URGENTE"),
    NORMAL("NORMAL"),
    BAIXA("BAIXA");
    private final String prioridade;

    Prioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getPrioridade() {
        return this.prioridade;
    }
}
