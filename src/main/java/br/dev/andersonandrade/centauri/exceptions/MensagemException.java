package br.dev.andersonandrade.centauri.exceptions;

/**
 * Classe MensagemException.
 * <p>
 * Esta classe representa uma exceção específica relacionada a mensagens
 * no sistema de chat. Extende a classe RuntimeException, permitindo que
 * erros relacionados a mensagens sejam tratados de maneira apropriada
 * sem a necessidade de declaração obrigatória em métodos.
 * <p>
 * Autor: Anderson Andrade Dev
 * Data: 29 de setembro de 2024
 * Contato: andersonandradedev@outlook.com
 */
public class MensagemException extends RuntimeException {

    /**
     * Construtor padrão que cria uma nova instância de MensagemException
     * sem uma mensagem de erro específica.
     */
    public MensagemException() {
        super();
    }

    /**
     * Construtor que cria uma nova instância de MensagemException com uma
     * mensagem de erro específica.
     *
     * @param message A mensagem de erro a ser exibida.
     */
    public MensagemException(String message) {
        super(message);
    }

    /**
     * Construtor que cria uma nova instância de MensagemException com uma
     * mensagem de erro específica e uma causa subjacente.
     *
     * @param message A mensagem de erro a ser exibida.
     * @param cause   A causa subjacente da exceção.
     */
    public MensagemException(String message, Throwable cause) {
        super(message, cause);
    }
}
