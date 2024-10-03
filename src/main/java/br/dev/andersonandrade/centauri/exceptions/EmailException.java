package br.dev.andersonandrade.centauri.exceptions;

/**
 * Classe EmailException.
 * <p>
 * Esta classe representa uma exceção específica relacionada a erros
 * de e-mail no sistema. Extende a classe RuntimeException, permitindo
 * que erros relacionados a e-mails sejam tratados de maneira apropriada
 * sem a necessidade de declaração obrigatória em métodos.
 * <p>
 * Autor: Anderson Andrade Dev
 * Data de Criação: 29 de setembro de 2024
 */
public class EmailException extends RuntimeException {

    /**
     * Construtor padrão que cria uma nova instância de EmailException
     * sem uma mensagem de erro específica.
     */
    public EmailException() {
        super();
    }

    /**
     * Construtor que cria uma nova instância de EmailException com uma
     * mensagem de erro específica.
     *
     * @param message A mensagem de erro a ser exibida.
     */
    public EmailException(String message) {
        super(message);
    }

    /**
     * Construtor que cria uma nova instância de EmailException com uma
     * mensagem de erro específica e uma causa subjacente.
     *
     * @param message A mensagem de erro a ser exibida.
     * @param cause   A causa subjacente da exceção.
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construtor que cria uma nova instância de EmailException com uma
     * causa subjacente.
     *
     * @param cause A causa subjacente da exceção.
     */
    public EmailException(Throwable cause) {
        super(cause);
    }
}

