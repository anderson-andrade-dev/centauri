package br.dev.andersonandrade.centauri.controller;


import br.dev.andersonandrade.centauri.beans.MensagemBean;
import br.dev.andersonandrade.centauri.exceptions.UsuarioNaoEncontradoException;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.model.ChatModel;
import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import br.dev.andersonandrade.centauri.record.DestinatarioRecord;
import br.dev.andersonandrade.centauri.record.MensagemRecord;
import br.dev.andersonandrade.centauri.record.MostarMensagemChat;
import br.dev.andersonandrade.centauri.service.RemetenteDestinatarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatModel chatModel;
    private final RemetenteDestinatarioService service;

    public ChatController(ChatModel chatModel, RemetenteDestinatarioService service) {

        this.chatModel = chatModel;
        this.service = service;

    }

    @GetMapping("/principal")
    public String chat(Model model, Authentication authentication) {

        Remetente remetente = service.buscaRemetentePorEndereco(authentication.getName());
        List<Destinatario> destinatarios = service.buscarDestinatariosPorRemetente(remetente);

        model.addAttribute("remetente", remetente);
        model.addAttribute("destinatarios", destinatarios);

        return "chat";
    }

    @PostMapping("/mensagens")
    public String getMessagesByContact(DestinatarioRecord destinatario,
                                       Authentication authentication, Model model) {

        Remetente remetente = service.buscaRemetentePorEndereco(authentication.getName());
        List<Destinatario> destinatarios = service.buscarDestinatariosPorRemetente(remetente);

        ChatMensagemRecord chatMensagemRecord = chatModel.mensagens(authentication.getName(), destinatario);

        logger.info("Mensagens do Destinatario: {} para o Remetente: {} recuperadas com sucesso!",
                destinatario.endereco(), authentication.getName());

        List<MostarMensagemChat> mensagensChat = new ArrayList<>();

        chatMensagemRecord.mensagensDestinatario()
                .forEach(mensagem -> mensagensChat.add(new MostarMensagemChat(mensagem, false)));

        chatMensagemRecord.mensagensRemetente()
                .forEach(mensagem -> mensagensChat.add(new MostarMensagemChat(mensagem, true)));

        mensagensChat.sort(Comparator.comparing(m -> m.mensagem().getDataEnvio()));
        model.addAttribute("mensagensChat", mensagensChat);
        model.addAttribute("remetente", remetente);
        model.addAttribute("destinatarios", destinatarios);
        model.addAttribute("destinatarioAtual",destinatario);
        return "chat";
    }

    @PostMapping("/enviar")
    public ResponseEntity<HttpStatus> enviarMensagem(@RequestBody MensagemRecord mensagemRecord,
                                                     Authentication authentication) {

        logger.info("Remetente: {} enviando mensagem para Destinatario: {}",
                authentication.getName(), mensagemRecord.destinatario().endereco());

        try {
            chatModel.enviar(authentication.getName(), mensagemRecord.destinatario(),
                    new MensagemBean("Chat", mensagemRecord.mensagem(), LocalDateTime.now()));

            logger.info("Mensagem enviada com sucesso!");

            return ResponseEntity.ok(HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Error ao enviar mensagem!", e);
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("destinatario")
    public String formCadastroRemetente(Authentication authentication,
                                        DestinatarioRecord destinatarioRecord, Model model) {
        return "cadastro-destinatario";

    }

    /**
     * Busca destinatários pelo endereço de e-mail fornecido.
     * <p>
     * Este método busca um remetente na base de dados com o endereço de e-mail fornecido.
     * Se encontrado, o remetente é transformado em um registro de destinatário e retornado como uma lista.
     * Caso o remetente não seja encontrado, uma lista vazia é retornada.
     *
     * @param email o endereço de e-mail do remetente a ser buscado. Não pode ser nulo, vazio ou conter espaços em branco.
     * @return uma lista contendo o destinatário correspondente ao remetente encontrado.
     * Retorna uma lista vazia se nenhum remetente for encontrado.
     * @throws NullPointerException     se o parâmetro de e-mail for nulo.
     * @throws IllegalArgumentException se o e-mail estiver vazio ou em branco, ou em caso de erro ao buscar o remetente.
     */

    @PostMapping("destinatario/busca")
    @ResponseBody
    public List<DestinatarioRecord> buscaDestinatario(@RequestParam("email") String email) {
        Objects.requireNonNull(email, "Verifique o parametro ele não pode ser nulo!");

        if (email.isEmpty() || email.isBlank()) {

            throw new IllegalArgumentException("A String do parametro não pode ser vazia ou estar em branco!");

        }

        try {

            logger.info("Buscando Remetente: {} na base de dados", email);

            Remetente remetente = service.buscaRemetentePorEndereco(email);


            DestinatarioRecord destinatario = new DestinatarioRecord(remetente.nome(),
                    remetente.endereco());

            logger.info("Enviando Destinatario: {} para o template", destinatario.endereco());

            return List.of(destinatario);

        } catch (UsuarioNaoEncontradoException e) {

            logger.info("Usuario: {} não encontrado na base de dados", e);

            return List.of();

        } catch (Exception e) {

            logger.error("Error ao enviar mensagem!", e);

            throw new IllegalArgumentException(e);

        }
    }


    @PostMapping("destinatario/adiciona")
    public ResponseEntity<?> enviarConviteDestinatario(@Valid @RequestParam("email") String email,
                                                       Authentication authentication) {
        try {
            logger.info("Buscando Remetente: {} na base de dados!", authentication.getName());

            Remetente remetente = service.buscaRemetentePorEndereco(authentication.getName());

            logger.info("Buscando Destinatario: {} na base de dados");
            Remetente remetenteViraDestinatario = service.buscaRemetentePorEndereco(email);

            Destinatario destinatario = new DestinatarioRecord(remetenteViraDestinatario.nome()
                    , remetenteViraDestinatario.endereco());

            logger.info("Verificando se o Remetente: {} tem o Destinatario: {} associado "
                    , authentication.getName(), destinatario.endereco());

            if (service.jaAssociado(remetente, destinatario)) {

                logger.error("Remetente e Destinatario já estão associados");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Remetente e Destinatario já estão associados");

            }

            service.associar(remetente, destinatario);

            logger.info("Remetente: {} e Destinatario: {} associados com sucesso", remetente.endereco(), destinatario.endereco());

            return ResponseEntity.ok(HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Error ao associar Remetente ao Destinatario", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ao associar Remetente ao Destinatario");
        }
    }
}