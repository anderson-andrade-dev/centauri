package br.dev.andersonandrade.centauri.beans;

import br.dev.andersonandrade.centauri.beans.MensagemBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class MensagemBeanTest {

    private MensagemBean mensagem;

    @BeforeEach
    void setUp() {
        mensagem = new MensagemBean("Título de Teste", "Conteúdo de Teste", LocalDateTime.now());
    }

    @Test
    void testConstrutorMensagemComTituloEConteudo() {
        assertNotNull(mensagem.getDataEnvio(), "A data de envio deve ser preenchida automaticamente.");
        assertEquals("Título de Teste", mensagem.getTitulo(), "O título deve ser igual ao informado.");
        assertEquals("Conteúdo de Teste", mensagem.getConteudo(), "O conteúdo deve ser igual ao informado.");
        assertFalse(mensagem.isLida(), "A mensagem não deve estar marcada como lida ao ser criada.");
    }

    @Test
    void testSetGetId() {
        mensagem.setId(1L);
        assertEquals(1L, mensagem.getId(), "O ID da mensagem deve ser o mesmo que foi setado.");
    }

    @Test
    void testSetGetDataEnvio() {
        LocalDateTime novaDataEnvio = LocalDateTime.of(2023, 9, 26, 12, 0);
        mensagem.setDataEnvio(novaDataEnvio);
        assertEquals(novaDataEnvio, mensagem.getDataEnvio(), "A data de envio deve ser a mesma que foi setada.");
    }

    @Test
    void testSetGetDataLeitura() {
        LocalDateTime dataLeitura = LocalDateTime.of(2023, 9, 27, 14, 30);
        mensagem.setDataLeitura(dataLeitura);
        assertEquals(dataLeitura, mensagem.getDataLeitura(), "A data de leitura deve ser a mesma que foi setada.");
    }

    @Test
    void testSetGetTitulo() {
        String novoTitulo = "Novo Título";
        mensagem.setTitulo(novoTitulo);
        assertEquals(novoTitulo, mensagem.getTitulo(), "O título deve ser o mesmo que foi setado.");
    }

    @Test
    void testSetGetConteudo() {
        String novoConteudo = "Novo Conteúdo";
        mensagem.setConteudo(novoConteudo);
        assertEquals(novoConteudo, mensagem.getConteudo(), "O conteúdo deve ser o mesmo que foi setado.");
    }

    @Test
    void testSetGetLida() {
        mensagem.setLida(true);
        assertTrue(mensagem.isLida(), "A mensagem deve estar marcada como lida.");
    }

    @Test
    void testEqualsComDatasEStatusLeitura() {
        // Criando duas mensagens com os mesmos dados
        MensagemBean mensagem1 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        MensagemBean mensagem2 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());

        // Definindo a mesma data de envio e leitura para ambas as mensagens
        LocalDateTime dataEnvio = LocalDateTime.of(2023, 9, 26, 12, 0);
        LocalDateTime dataLeitura = LocalDateTime.of(2023, 9, 27, 14, 0);

        mensagem1.setDataEnvio(dataEnvio);
        mensagem1.setDataLeitura(dataLeitura);
        mensagem1.setLida(true);

        mensagem2.setDataEnvio(dataEnvio);
        mensagem2.setDataLeitura(dataLeitura);
        mensagem2.setLida(true);

        // Verificando se são iguais considerando datas e status de leitura
        assertEquals(mensagem1, mensagem2, "Duas mensagens com os mesmos campos, datas e status de leitura devem ser iguais.");
    }

    @Test
    void testNotEqualsComDatasDiferentes() {
        MensagemBean mensagem1 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        MensagemBean mensagem2 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());

        // Definindo diferentes datas de envio
        mensagem1.setDataEnvio(LocalDateTime.of(2023, 9, 26, 12, 0));
        mensagem2.setDataEnvio(LocalDateTime.of(2023, 9, 27, 12, 0));

        assertNotEquals(mensagem1, mensagem2, "Mensagens com datas de envio diferentes não devem ser iguais.");
    }

    @Test
    void testNotEqualsComStatusLeituraDiferente() {
        MensagemBean mensagem1 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        MensagemBean mensagem2 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());

        // Definindo o status de leitura como diferente
        mensagem1.setLida(true);
        mensagem2.setLida(false);

        assertNotEquals(mensagem1, mensagem2, "Mensagens com status de leitura diferentes não devem ser iguais.");
    }


    @Test
    void testEquals() {
        MensagemBean mensagem1 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        MensagemBean mensagem2 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        mensagem1.setDataEnvio(mensagem2.getDataEnvio()); // Para garantir que os campos sejam iguais
        assertEquals(mensagem1, mensagem2, "Duas mensagens com os mesmos campos devem ser iguais.");
    }

    @Test
    void testNotEquals() {
        MensagemBean mensagem1 = new MensagemBean("Título 1", "Conteúdo", LocalDateTime.now());
        MensagemBean mensagem2 = new MensagemBean("Título 2", "Conteúdo", LocalDateTime.now());
        assertNotEquals(mensagem1, mensagem2, "Mensagens com títulos diferentes não devem ser iguais.");
    }

    @Test
    void testHashCode() {
        MensagemBean mensagem1 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        MensagemBean mensagem2 = new MensagemBean("Título", "Conteúdo", LocalDateTime.now());
        mensagem1.setDataEnvio(mensagem2.getDataEnvio());
        assertEquals(mensagem1.hashCode(), mensagem2.hashCode(), "Mensagens com os mesmos campos devem ter o mesmo hashCode.");
    }

    @Test
    void testToString() {
        String expected = "MensagemBean{" +
                "id=" + mensagem.getId() +
                ", dataEnvio=" + mensagem.getDataEnvio() +
                ", dataLeitura=" + mensagem.getDataLeitura() +
                ", titulo='" + mensagem.getTitulo() + '\'' +
                ", conteudo='" + mensagem.getConteudo() + '\'' +
                ", lida=" + mensagem.isLida() +
                '}';

        assertEquals(expected, mensagem.toString(), "A representação em string deve estar correta.");
    }
}
