package br.dev.andersonandrade.centauri.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    private static void validaSenha(String senha) {
        Objects.requireNonNull(senha, "Senha não pode ser nula!");
        if (senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser uma String vazia ou em branco!");
        }
    }
}
