package br.dev.andersonandrade.centauri.uteis;

import java.time.LocalDate;

public class CodigoUtil {

    private CodigoUtil() {

    }

    public static String gerarCodigo() {
        long pegarMilis = System.currentTimeMillis();
        LocalDate pegarAno = LocalDate.now();
        int converterAno = pegarAno.getYear();

        String conversaoAno = String.valueOf(converterAno);
        String convercao = String.valueOf(pegarMilis);
        String codigoFinal = conversaoAno + convercao;

        return codigoFinal;
    }


}
