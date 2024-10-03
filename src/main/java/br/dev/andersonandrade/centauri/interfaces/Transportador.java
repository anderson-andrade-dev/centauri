package br.dev.andersonandrade.centauri.interfaces;

import jakarta.validation.constraints.NotNull;

/**
 * Interface Transportador.
 * <p>
 * Esta interface define os métodos para acessar informações sobre
 * um transportador, que inclui um destinatário, um remetente e uma mensagem.
 * Implementações desta interface devem fornecer acesso a essas informações.
 * <p>
 * Autor: Anderson Andrade Dev
 * Data: 29 de setembro de 2024
 * Contato: andersonandradedev@outlook.com
 */
public interface Transportador {

    /**
     * Obtém o destinatário associado a este transportador.
     *
     * @return O destinatário.
     */
    @NotNull
    Destinatario destinatario();

    /**
     * Obtém o remetente associado a este transportador.
     *
     * @return O remetente.
     */
    @NotNull
    Remetente remetente();

    /**
     * Obtém a mensagem associada a este transportador.
     *
     * @return A mensagem.
     */
    @NotNull
    Mensagem mensagem();
}
