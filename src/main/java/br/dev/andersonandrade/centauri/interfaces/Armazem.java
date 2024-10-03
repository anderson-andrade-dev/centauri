package br.dev.andersonandrade.centauri.interfaces;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Interface Armazem.
 * <p>
 * Esta interface define os métodos para armazenar e recuperar mensagens
 * entre remetentes e destinatários. Implementações desta interface devem
 * gerenciar a persistência das mensagens e fornecer operações para
 * acesso às mensagens de acordo com o remetente ou destinatário.
 *
 * @Autor: Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public interface Armazem {

    /**
     * Armazena uma lista de transportadores na base de dados.
     * <p>
     * Este método persiste as mensagens no repositório.
     *
     * @param transportadores Lista de transportadores a serem armazenados.
     * @throws RuntimeException se ocorrer um erro durante a persistência das mensagens.
     */
    void armazenar(@NotNull @NotEmpty List<Transportador> transportadores);

    /**
     * Recupera todas as mensagens associadas a um destinatário específico.
     * <p>
     * Este método busca nas mensagens armazenadas para encontrar aquelas que
     * têm o endereço do destinatário especificado.
     *
     * @param destinatario O destinatário cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao destinatário, ou uma lista vazia se não houver mensagens.
     */
    List<Mensagem> mensagens(Destinatario destinatario);

    /**
     * Recupera todas as mensagens associadas a um remetente específico.
     * <p>
     * Este método busca nas mensagens armazenadas para encontrar aquelas que
     * têm o endereço do remetente especificado.
     *
     * @param remetente O remetente cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao remetente, ou uma lista vazia se não houver mensagens.
     */
    List<Mensagem> mensagens(Remetente remetente);
}
