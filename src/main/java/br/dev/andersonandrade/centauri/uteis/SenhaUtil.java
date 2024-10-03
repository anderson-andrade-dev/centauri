package br.dev.andersonandrade.centauri.uteis;

import br.dev.andersonandrade.centauri.entity.Senha;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SenhaUtil {
    public static Senha criar(String senha) {
        try {
            return new Senha(criptografar(senha));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao Criar Senha " + e);
        }
    }

    public static boolean isValida(String senhaDigitada, Senha senha) {
        try {

            String senhaAtual = criptografar(senhaDigitada);
            String senhaBanco = senha.getChave();

            return senhaAtual.equals(senhaBanco);

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Erro ao Descriptografar Senha " + e);

        }
    }

    private static String criptografar(String senha) throws NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte[] messageDigest = algorithm.digest(senha.getBytes(StandardCharsets.UTF_8));
        StringBuilder valorCriptografado = new StringBuilder();

        for (byte b : messageDigest) {
            valorCriptografado.append(String.format("%02X", 0xFF & b));
        }

        return valorCriptografado.toString();
    }
}
