package br.dev.andersonandrade.centauri.enumeradores;

public enum NomePermisao {
    MINIMA("MINIMA"), MEDIA("MAXIMA"), ALTA("TOTAL");

    private final String nomePermisao;

    NomePermisao(String nome) {
        nomePermisao = nome;
    }

    public String getNomePermisao() {
        return nomePermisao;
    }
}
