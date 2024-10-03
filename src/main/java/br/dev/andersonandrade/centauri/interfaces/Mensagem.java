package br.dev.andersonandrade.centauri.interfaces;

import java.time.LocalDateTime;

/**
 * Interface Mensagem.
 * <p>
 * Esta interface define a estrutura básica de uma mensagem em um sistema
 * de chat. Implementações desta interface devem fornecer os detalhes
 * necessários para manipular mensagens, como título, conteúdo e status
 * de leitura.
 *
 * @Autor: Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public interface Mensagem {

    /**
     * Obtém a data e hora em que a mensagem foi enviada.
     *
     * @return A data e hora da mensagem enviada.
     */
    LocalDateTime getDataEnvio();

    /**
     * Obtém a data e hora em que a mensagem foi lida.
     *
     * @return A data e hora da leitura da mensagem, ou null se ainda não foi lida.
     */
    LocalDateTime getDataLeitura();

    /**
     * Obtém o título da mensagem.
     *
     * @return O título da mensagem.
     */
    String getTitulo();

    /**
     * Obtém o conteúdo da mensagem.
     *
     * @return O conteúdo textual da mensagem.
     */
    String getConteudo();

    /**
     * Verifica se a mensagem foi lida.
     *
     * @return true se a mensagem foi lida, false caso contrário.
     */
    boolean isLida();
}
