package br.dev.andersonandrade.centauri.beans.uteis;

import br.dev.andersonandrade.centauri.uteis.DataUtil;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataUtilTest {
    @Test
    void verificaSeDataFoiFormatada() {
        LocalDateTime data = LocalDateTime.of(2024, 8, 7, 12, 0, 0);
        assertEquals("07-08-2024", DataUtil.formatarDataBR(data));
    }

    @Test
    void verificaSeDAtaFoiFormatadaComHoras() {
        LocalDateTime data = LocalDateTime.of(2024, 8, 7, 12, 0, 0);
        assertEquals("07-08-2024 12:00:00", DataUtil.formatarDataHoraBR(data));
    }
}
