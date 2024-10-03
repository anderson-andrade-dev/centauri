package br.dev.andersonandrade.centauri.interfaces;

/**
 * Interface Destinatario.
 * <p>
 * Esta interface define a estrutura básica de um destinatário em um sistema
 * de chat. Implementações desta interface devem fornecer os detalhes
 * necessários para identificar um destinatário, como nome e endereço.
 *
 * @Autor: Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public interface Destinatario {

    /**
     * Obtém o nome do destinatário.
     *
     * @return O nome do destinatário.
     */
    String nome();

    /**
     * Obtém o endereço do destinatário.
     *
     * @return O endereço do destinatário, que pode ser um e-mail ou outro tipo de identificador.
     */
    String endereco();
}

