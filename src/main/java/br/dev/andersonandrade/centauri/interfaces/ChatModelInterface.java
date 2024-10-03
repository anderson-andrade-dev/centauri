package br.dev.andersonandrade.centauri.interfaces;

import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import jakarta.validation.constraints.NotNull;

/**
 * Interface para a classe ChatModel.
 * Define as operações que podem ser realizadas no modelo de chat.
 *
 * @author Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public interface ChatModelInterface {

    /**
     * Envia uma mensagem de um remetente para um destinatário.
     *
     * @param remetente    O remetente da mensagem.
     * @param destinatario O destinatário da mensagem.
     * @param mensagem     A mensagem a ser enviada.
     * @throws IllegalArgumentException se a mensagem estiver vazia ou se o remetente/destinatário forem nulos.
     */
    void enviar(@NotNull Remetente remetente, @NotNull Destinatario destinatario, @NotNull Mensagem mensagem);

    /**
     * Envia uma mensagem de um usuário identificado pelo e-mail do remetente para um destinatário.
     *
     * @param emailRemetente O e-mail do remetente.
     * @param destinatario   O destinatário da mensagem.
     * @param mensagem       A mensagem a ser enviada.
     * @throws IllegalArgumentException se o usuário não for encontrado ou se a mensagem estiver vazia.
     */
    void enviar(@NotNull String emailRemetente, @NotNull Destinatario destinatario, @NotNull Mensagem mensagem);

    /**
     * Responde a uma mensagem de um remetente para um destinatário.
     *
     * @param remetente    O remetente da mensagem.
     * @param destinatario O destinatário da mensagem.
     * @param mensagem     A mensagem a ser enviada como resposta.
     * @throws IllegalArgumentException se remetente, destinatário ou mensagem forem nulos.
     */
    void responder(@NotNull Remetente remetente, @NotNull Destinatario destinatario, @NotNull Mensagem mensagem);

    /**
     * Recupera todas as mensagens entre um usuário e um destinatário.
     *
     * @param email        O e-mail do usuário que deseja recuperar as mensagens.
     * @param destinatario O destinatário cujas mensagens devem ser recuperadas.
     * @return Um registro contendo as mensagens enviadas e recebidas entre o usuário e o destinatário.
     */
    ChatMensagemRecord mensagens(@NotNull String email, @NotNull Destinatario destinatario);
}
