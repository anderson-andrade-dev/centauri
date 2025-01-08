package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.entity.Senha;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import br.dev.andersonandrade.centauri.service.UsuarioService;
import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import br.dev.andersonandrade.centauri.record.DestinatarioRecord;
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
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ChatModelTest {


}
