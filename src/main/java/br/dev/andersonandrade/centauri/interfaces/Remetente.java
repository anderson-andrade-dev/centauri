package br.dev.andersonandrade.centauri.interfaces;

/**
 * Interface Remetente.
 * <p>
 * Esta interface define a estrutura básica de um remetente em um sistema
 * de chat. Implementações desta interface devem fornecer os detalhes
 * necessários para identificar um remetente, como nome e endereço.
 *
 * @Autor: Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public interface Remetente {

    /**
     * Obtém o nome do remetente.
     *
     * @return O nome do remetente.
     */
    String nome();

    /**
     * Obtém o endereço do remetente.
     *
     * @return O endereço do remetente, que pode ser um e-mail ou outro tipo de identificador.
     */
    String endereco();
}
