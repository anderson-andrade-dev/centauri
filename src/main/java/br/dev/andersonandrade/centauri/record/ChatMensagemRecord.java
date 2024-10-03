package br.dev.andersonandrade.centauri.record;

import br.dev.andersonandrade.centauri.interfaces.Mensagem;

import java.util.List;

public record ChatMensagemRecord(List<Mensagem> mensagensRemetente, List<Mensagem> mensagensDestinatario) {
}
