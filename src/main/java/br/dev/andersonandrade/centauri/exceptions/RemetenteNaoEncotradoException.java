package br.dev.andersonandrade.centauri.exceptions;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 05/10/2024
 */
public class RemetenteNaoEncotradoException extends RuntimeException {
    public RemetenteNaoEncotradoException(String message) {
        super(message);
    }
}
