package br.dev.andersonandrade.centauri.beans.uteis;

import br.dev.andersonandrade.centauri.entity.Senha;
import br.dev.andersonandrade.centauri.uteis.SenhaUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UteisTest {
    @Test
    void verificaSeASenhaFoiCriptografada() {
        String atual = "olaMundo";
        Senha senha = SenhaUtil.criar(atual);
        assertEquals("415DF6F586365A790E3CB46E180ED030", senha.getChave());
    }

    @Test
    void inseridaASenhaCertaComparaComOBancoDevolveVerdadeiro() {
        String senhaDigitada = "olaMundo";
        Senha senhaDoBanco = SenhaUtil.criar(senhaDigitada);
        boolean resposta = SenhaUtil.isValida(senhaDigitada, senhaDoBanco);
        assertTrue(resposta);
    }
}
