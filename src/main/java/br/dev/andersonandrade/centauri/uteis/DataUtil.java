package br.dev.andersonandrade.centauri.uteis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DataUtil {

    private DataUtil() {
        super();
    }

    public static String formatarDataBR(LocalDateTime data) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formato);
    }

    public static String formatarDataHoraBR(LocalDateTime data) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return data.format(formato);
    }
}
