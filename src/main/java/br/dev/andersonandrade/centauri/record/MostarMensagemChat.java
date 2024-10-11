package br.dev.andersonandrade.centauri.record;

import br.dev.andersonandrade.centauri.interfaces.Mensagem;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 07/10/2024
 */
public record MostarMensagemChat(Mensagem mensagem, boolean remetente) {
}
