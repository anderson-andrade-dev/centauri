package br.dev.andersonandrade.centauri.beans.model;

import br.dev.andersonandrade.centauri.entity.Senha;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import br.dev.andersonandrade.centauri.model.ChatModel;
import br.dev.andersonandrade.centauri.model.UsuarioService;
import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import br.dev.andersonandrade.centauri.record.RemetenteRecord;
import br.dev.andersonandrade.centauri.record.UsuarioRecord;
import br.dev.andersonandrade.centauri.service.CorreioMensagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ChatModelTest {

    @Mock
    private CorreioMensagem correio;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ChatModel chatModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMensagens() {
        // Configura o comportamento de dependências externas
        String email = "usuario@email.com";
        Usuario usuario = new Usuario(new UsuarioRecord("Anderson", "Andrade",
                "anderson", "1234", email), new Senha("1234"));


        Destinatario destinatario = mock(Destinatario.class);

        Mensagem mensagem1 = mock(Mensagem.class);
        Mensagem mensagem2 = mock(Mensagem.class);

        when(mensagem1.getDataEnvio()).thenReturn(LocalDateTime.of(2023, 9, 26, 10, 0));
        when(mensagem2.getDataEnvio()).thenReturn(LocalDateTime.of(2023, 9, 26, 12, 0));

        // Simula o usuário retornado pela busca de email
        when(usuarioService.buscaPorEmail(email)).thenReturn(usuario);

        // Simula o correio retornando mensagens para o remetente e destinatário
        when(correio.mensagens(any(RemetenteRecord.class))).thenReturn(List.of(mensagem1));
        when(correio.mensagens(any(Destinatario.class))).thenReturn(List.of(mensagem2));

        // Executa o método de teste
        ChatMensagemRecord resultado = chatModel.mensagens(email, destinatario);
        long contRemetente = StreamSupport.stream(resultado.mensagensRemetente().spliterator(), false).count();
        long contDestinatario = StreamSupport.stream(resultado.mensagensDestinatario().spliterator(), false).count();
        // Verifica o resultado esperado
        assertNotNull(resultado);
        assertEquals(1, contRemetente);
        assertEquals(1, contDestinatario);

        // Verifica se as mensagens estão ordenadas corretamente
        assertEquals(mensagem1.getDataEnvio(), resultado.mensagensRemetente().iterator().next().getDataEnvio());
        assertEquals(mensagem2.getDataEnvio(), resultado.mensagensDestinatario().iterator().next().getDataEnvio());

        // Verifica interações com mocks
        verify(usuarioService, times(1)).buscaPorEmail(email);
        verify(correio, times(1)).mensagens(any(RemetenteRecord.class));
        verify(correio, times(1)).mensagens(any(Destinatario.class));
    }
}
