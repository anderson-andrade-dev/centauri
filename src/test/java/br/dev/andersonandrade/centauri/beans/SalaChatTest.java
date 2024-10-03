package br.dev.andersonandrade.centauri.beans;

import br.dev.andersonandrade.centauri.beans.SalaChat;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de teste para a classe SalaChat.
 * <p>
 * Esta classe contém testes unitários para verificar o comportamento da classe SalaChat,
 * utilizando o framework JUnit 5 e a biblioteca Mockito para criar mocks.
 * <p>
 * Os testes incluem a verificação da criação de salas de chat, envio de mensagens,
 * recuperação de remetente e destinatário, e a representação em string da sala de chat.
 * <p>
 * Exemplo de testes:
 * - Testar a abertura de uma nova sala de chat.
 * - Testar o envio de mensagens para destinatários e remetentes.
 * - Testar a recuperação de informações sobre remetentes e destinatários.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 */
class SalaChatTest {

    private Remetente remetenteMock;
    private Destinatario destinatarioMock;
    private Mensagem mensagemMock;

    /**
     * Configura os mocks antes de cada teste.
     * <p>
     * Este método é executado antes de cada método de teste e inicializa
     * os mocks para remetente, destinatário e mensagem, configurando
     * os comportamentos esperados dos mocks.
     */
    @BeforeEach
    void setUp() {
        remetenteMock = mock(Remetente.class);
        destinatarioMock = mock(Destinatario.class);
        mensagemMock = mock(Mensagem.class);

        // Configura os mocks para retornar endereços e nomes específicos
        when(remetenteMock.endereco()).thenReturn("remetente@dominio.com");
        when(destinatarioMock.endereco()).thenReturn("destinatario@dominio.com");
        when(remetenteMock.nome()).thenReturn("Remetente Anderson");
        when(destinatarioMock.nome()).thenReturn("Destinatario Andreza");
    }

    /**
     * Testa a abertura de uma nova sala de chat.
     * <p>
     * Verifica se uma nova sala de chat é criada corretamente e se
     * os endereços do remetente e destinatário estão corretos.
     */
    @Test
    void testAbrirNovaSala() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);
        assertNotNull(salaChat);
        assertEquals("remetente@dominio.com", salaChat.getRemetente().endereco());
        assertEquals("destinatario@dominio.com", salaChat.getDestinatario().endereco());
    }

    /**
     * Testa a devolução de uma sala de chat existente.
     * <p>
     * Verifica se a mesma sala de chat é retornada quando se tenta
     * abrir novamente a sala para o mesmo remetente e destinatário.
     */
    @Test
    void testDevolverSalaExistente() {
        SalaChat.fecharTodas();
        SalaChat salaChat1 = SalaChat.abrir(remetenteMock, destinatarioMock);
        SalaChat salaChat2 = SalaChat.abrir(remetenteMock, destinatarioMock);
        assertSame(salaChat1, salaChat2);
    }

    /**
     * Testa o envio de uma mensagem para o destinatário.
     * <p>
     * Verifica se uma mensagem é adicionada corretamente à lista de
     * mensagens do destinatário.
     */
    @Test
    void testEnviarMensagemParaDestinatario() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);
        Mensagem novaMensagem = mock(Mensagem.class);
        salaChat.adicionarMensagemDestinatario(novaMensagem);
        List<Mensagem> mensagens = salaChat.getMensagensDestinatario();
        assertEquals(1, mensagens.size());
        assertTrue(mensagens.contains(novaMensagem));
    }

    /**
     * Testa o envio de uma mensagem para o remetente.
     * <p>
     * Verifica se uma mensagem é adicionada corretamente à lista de
     * mensagens do remetente.
     */
    @Test
    void testEnviarMensagemParaRemetente() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);
        salaChat.adicionarMensagemRemetente(mensagemMock);
        List<Mensagem> mensagens = salaChat.getMensagensRemetente();
        assertEquals(1, mensagens.size());
        assertTrue(mensagens.contains(mensagemMock));
    }

    /**
     * Testa a recuperação do remetente da sala de chat.
     * <p>
     * Verifica se o remetente retornado é o mesmo que foi usado para
     * criar a sala de chat.
     */
    @Test
    void testGetRemetente() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);
        assertEquals(remetenteMock.endereco(), salaChat.getRemetente().endereco());
    }

    /**
     * Testa a recuperação do destinatário da sala de chat.
     * <p>
     * Verifica se o destinatário retornado é o mesmo que foi usado para
     * criar a sala de chat.
     */
    @Test
    void testGetDestinatario() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);
        assertEquals(destinatarioMock.endereco(), salaChat.getDestinatario().endereco());
    }

    /**
     * Testa se a sala de chat é criada sem mensagens.
     * <p>
     * Verifica se ambas as listas de mensagens do remetente e destinatário
     * estão vazias após a criação da sala de chat.
     */
    @Test
    void testSalaChatCriadaSemMensagens() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);

        List<Mensagem> mensagensRemetente = salaChat.getMensagensRemetente();
        List<Mensagem> mensagensDestinatario = salaChat.getMensagensDestinatario();

        // Então esperamos que ambas as listas estejam vazias
        assertTrue(mensagensRemetente.isEmpty(), "A lista de mensagens do remetente deve estar vazia.");
        assertTrue(mensagensDestinatario.isEmpty(), "A lista de mensagens do destinatário deve estar vazia.");
    }

    /**
     * Testa a representação em string da sala de chat.
     * <p>
     * Verifica se a string retornada pela implementação do método toString()
     * é a esperada, contendo as informações corretas sobre o remetente, destinatário
     * e a contagem de mensagens.
     */
    @Test
    void testToString() {
        SalaChat.fecharTodas();
        SalaChat salaChat = SalaChat.abrir(remetenteMock, destinatarioMock);
        salaChat.adicionarMensagemDestinatario(mensagemMock);

        String expectedString = "SalaChat{" +
                "Nome Remetente = Remetente Anderson" +
                ", Endereco Remetente = remetente@dominio.com" +
                ", Nome Destinatario = Destinatario Andreza" +
                ", Endereco Destinatario = destinatario@dominio.com" +
                ", Quantidade de Mensagens Destinatario = 1" +
                ", Quantidade de Mensagens Remetente = 0" +
                '}';

        assertEquals(expectedString, salaChat.toString());
    }
}
