package br.dev.andersonandrade.centauri.record;

import br.dev.andersonandrade.centauri.interfaces.Remetente;

public record RemetenteRecord(String nome, String endereco) implements Remetente {
}
