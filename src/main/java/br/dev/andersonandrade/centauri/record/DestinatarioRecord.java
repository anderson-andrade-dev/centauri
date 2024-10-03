package br.dev.andersonandrade.centauri.record;

import br.dev.andersonandrade.centauri.interfaces.Destinatario;

public record DestinatarioRecord(String nome, String endereco) implements Destinatario {

}