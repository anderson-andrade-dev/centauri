package br.dev.andersonandrade.centauri.interfaces;

import br.dev.andersonandrade.centauri.enumeradores.Prioridade;

import java.util.List;

/**
 * Interface Mensageiro.
 * <p>
 * Esta interface define as operações relacionadas ao gerenciamento e
 * recebimento de mensagens entre remetentes e destinatários.
 * Implementações desta interface devem fornecer a lógica necessária
 * para gerenciar o fluxo de mensagens.
 *
 * @Autor: Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public interface Mensageiro {

    /**
     * Recebe uma mensagem de um remetente e a entrega ao destinatário
     * com uma prioridade específica.
     *
     * @param destinatario O destinatário da mensagem.
     * @param remetente    O remetente da mensagem.
     * @param mensagem     A mensagem a ser recebida.
     * @param prioridade   A prioridade da mensagem.
     */
    void recebeMensagem(Destinatario destinatario, Remetente remetente, Mensagem mensagem, Prioridade prioridade);

    /**
     * Gerencia o processamento das mensagens recebidas.
     * Este método deve incluir a lógica para organizar, armazenar
     * e possivelmente enviar notificações sobre novas mensagens.
     */
    void gerenciarMensagens();

    /**
     * Busca uma mensagem específica enviada a um destinatário.
     *
     * @param destinatario O destinatário da mensagem a ser buscada.
     * @param mensagem     A mensagem que se deseja encontrar.
     * @return A mensagem correspondente, ou null se não for encontrada.
     */
    Mensagem buscaMensagem(Destinatario destinatario, Mensagem mensagem);

    /**
     * Obtém todas as mensagens enviadas a um destinatário específico.
     *
     * @param destinatario O destinatário cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens recebidas pelo destinatário.
     */
    List<Mensagem> mensagens(Destinatario destinatario);

    /**
     * Obtém todas as mensagens enviadas por um remetente específico.
     *
     * @param remetente O remetente cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens enviadas pelo remetente.
     */
    List<Mensagem> mensagens(Remetente remetente);

    /**
     * Recupera todas as mensagens associadas a um remetente e um destinatario específico.
     * <p>
     * Este método busca nas mensagens armazenadas para encontrar aquelas que
     * têm o endereço do remetente especificado.
     *
     * @param remetente O remetente cujas mensagens devem ser recuperadas.
     * @param destinatario remetente cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao remetente e destinatario, ou uma lista vazia se não houver mensagens.
     */
    List<Mensagem> mensagens(Remetente remetente,Destinatario destinatario);
}
