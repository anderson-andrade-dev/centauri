package br.dev.andersonandrade.centauri.interfaces;

import br.dev.andersonandrade.centauri.beans.SalaChat;

import java.util.List;

/**
 * Interface para a classe SalaChat.
 * Define as operações que podem ser realizadas em uma sala de chat.
 * <p>
 * Autor: Anderson Andrade Dev
 * Data: 29 de setembro de 2024
 * Contato: andersonandradedev@outlook.com
 */
public interface SalaChatInterface {


    /**
     * Abre uma nova sala de chat entre o remetente e o destinatário.
     * Se uma sala entre os dois já existir, retorna a sala existente.
     *
     * @param remetente    O remetente da sala de chat.
     * @param destinatario O destinatário da sala de chat.
     * @return A instância de SalaChat criada ou existente.
     */
    static SalaChat abrir(Remetente remetente, Destinatario destinatario) {
        throw new UnsupportedOperationException("Método não implementado.");
    }

    /**
     * Adiciona uma mensagem ao destinatário e armazena a mensagem.
     *
     * @param mensagem A mensagem a ser enviada ao destinatário.
     * @return A instância da SalaChat atual para encadeamento de métodos.
     */
    SalaChat adicionarMensagemDestinatario(Mensagem mensagem);

    /**
     * Adiciona uma mensagem ao remetente e armazena a mensagem.
     *
     * @param mensagem A mensagem a ser enviada ao remetente.
     * @return A instância da SalaChat atual para encadeamento de métodos.
     */
    SalaChat adicionarMensagemRemetente(Mensagem mensagem);

    /**
     * Obtém o remetente da sala de chat.
     *
     * @return O remetente da sala de chat.
     */
    Remetente getRemetente();

    /**
     * Obtém o destinatário da sala de chat.
     *
     * @return O destinatário da sala de chat.
     */
    Destinatario getDestinatario();

    /**
     * Obtém todas as mensagens enviadas pelo remetente.
     *
     * @return Uma lista imutável de mensagens enviadas pelo remetente.
     */
    List<Mensagem> getMensagensRemetente();

    /**
     * Obtém todas as mensagens enviadas ao destinatário.
     *
     * @return Uma lista imutável de mensagens enviadas ao destinatário.
     */
    List<Mensagem> getMensagensDestinatario();

    /**
     * Fecha todas as salas de chat abertas, removendo-as do conjunto de salas.
     */
    static void fecharTodas() {
        throw new UnsupportedOperationException("Método não implementado.");
    }

    /**
     * Retorna uma representação textual da sala de chat, incluindo remetente,
     * destinatário e contagem de mensagens trocadas.
     *
     * @return Uma string representando os detalhes da sala de chat.
     */
    String toString();
}
