package br.dev.andersonandrade.centauri.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 05/10/2024
 */
@Service
public class SenhaService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SenhaService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }


    public String criptografar(@NotNull String senha) {
        validaSenha(senha);
        return bCryptPasswordEncoder.encode(senha);
    }


    public boolean verificarSenha(@NotNull String senha, @NotNull String senhaCriptografada) {
        validaSenha(senha);
        validaSenha(senhaCriptografada);
        return bCryptPasswordEncoder.matches(senha, senhaCriptografada);
    }

    /**
     * Valida a força de uma senha, garantindo que ela tenha pelo menos 8 caracteres e contenha um caractere especial.
     *
     * @param senha A senha a ser validada.
     * @throws IllegalArgumentException Se a senha não atender aos critérios mínimos de segurança.
     */
    public void validaSenha(String senha) {
        if (senha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }
        if (!senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um caractere especial.");
        }
    }
}
