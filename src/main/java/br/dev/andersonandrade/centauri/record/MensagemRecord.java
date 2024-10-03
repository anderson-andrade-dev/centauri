package br.dev.andersonandrade.centauri.record;

public record MensagemRecord(DestinatarioRecord destinatario, RemetenteRecord remetente, String mensagem) {
}