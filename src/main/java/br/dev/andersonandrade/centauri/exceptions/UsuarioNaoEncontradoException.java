package br.dev.andersonandrade.centauri.exceptions;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 05/10/2024
 */
public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
