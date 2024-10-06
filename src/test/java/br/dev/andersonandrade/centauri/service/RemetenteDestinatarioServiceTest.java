package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.entity.RemetenteDestinatarios;
import br.dev.andersonandrade.centauri.repository.RemetenteDestinatariosRepository;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 05/10/2024
 */
class RemetenteDestinatarioServiceTest {

    @Mock
    private RemetenteDestinatariosRepository remetenteDestinatariosRepository;

    @InjectMocks
    private RemetenteDestinatarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveValidarRemetenteEDestinatarioComCamposNulos() {
        // Cenário: Remetente e destinatário com campos nulos
        Remetente remetente = mock(Remetente.class);
        Destinatario destinatario = mock(Destinatario.class);

        when(remetente.endereco()).thenReturn("");
        when(remetente.nome()).thenReturn("Remetente");
        when(destinatario.endereco()).thenReturn("destinatario@endereco.com");
        when(destinatario.nome()).thenReturn("");

        // Execução e Verificação da exceção
        IllegalArgumentException remetenteExcecao = assertThrows(IllegalArgumentException.class, () -> service.associar(remetente, destinatario));
        assertEquals("Endereço do Remetente não pode ser nulo ou em branco!", remetenteExcecao.getMessage());

        // Verifica o destinatário com campo inválido
        when(remetente.endereco()).thenReturn("remetente@endereco.com");
        IllegalArgumentException destinatarioExcecao = assertThrows(IllegalArgumentException.class, () -> service.associar(remetente, destinatario));
        assertEquals("Nome do Destinatário não pode ser nulo ou em branco!", destinatarioExcecao.getMessage());
    }

    @Test
    void deveRetornarFalseQuandoDestinatarioNaoAssociado() {
        // Cenário: Remetente e destinatário sem associação
        Remetente remetente = mock(Remetente.class);
        Destinatario destinatario = mock(Destinatario.class);

        when(remetente.endereco()).thenReturn("remetente@endereco.com");
        when(remetente.nome()).thenReturn("Anderson");
        when(destinatario.endereco()).thenReturn("destinatario@endereco.com");
        when(destinatario.nome()).thenReturn("Andreza");

        when(remetenteDestinatariosRepository.findByEndereco(remetente.endereco())).thenReturn(Optional.empty());

        boolean resultado = service.jaAssociado(remetente, destinatario);

        assertFalse(resultado);
    }
}