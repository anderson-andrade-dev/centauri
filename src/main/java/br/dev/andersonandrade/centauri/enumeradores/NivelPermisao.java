package br.dev.andersonandrade.centauri.enumeradores;

public enum NivelPermisao {
    ZERO(0), UM(1), DOIS(2), TRES(3), QUATRO(4), CINCO(5);
    private final int nivel;

    NivelPermisao(int nivel) {
        this.nivel = nivel;
    }

    public int getNivelPermissao() {
        return nivel;
    }
}
