package br.dev.andersonandrade.centauri.controller;


import br.dev.andersonandrade.centauri.beans.MensagemBean;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.model.ChatModel;
import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import br.dev.andersonandrade.centauri.record.DestinatarioRecord;
import br.dev.andersonandrade.centauri.record.MensagemRecord;
import br.dev.andersonandrade.centauri.service.RemetenteDestinatarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatModel chatModel;
    private final RemetenteDestinatarioService service;

    public ChatController(ChatModel chatModel, RemetenteDestinatarioService service) {
        this.chatModel = chatModel;
        this.service = service;
    }

    @GetMapping("/principal")
    public String chat(Model model,Authentication authentication) {
        Remetente remetente = service.buscaRemetentePorEndereco(authentication.getName());
        List<Destinatario> destinatarios = service.buscarDestinatariosPorRemetente(remetente);
        model.addAttribute("remetente", remetente);
        model.addAttribute("destinatarios", destinatarios);
        return "chat";
    }

    @PostMapping("/mensagens")
    public ResponseEntity<ChatMensagemRecord> getMessagesByContact(@RequestBody
                                                                       DestinatarioRecord destinatario,
                                                                   Authentication authentication) {
        ChatMensagemRecord chatMensagemRecord = chatModel.mensagens(authentication.getName(), destinatario);
        return ResponseEntity.ok(chatMensagemRecord);
    }

    @PostMapping("/enviar")
    public ResponseEntity<HttpStatus> enviarMensagem(@RequestBody MensagemRecord mensagemRecord, Authentication authentication) {

        chatModel.enviar(authentication.getName(), mensagemRecord.destinatario(),
                new MensagemBean("Chat", mensagemRecord.mensagem(), LocalDateTime.now()));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}