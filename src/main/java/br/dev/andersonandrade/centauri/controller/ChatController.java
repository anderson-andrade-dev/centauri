package br.dev.andersonandrade.centauri.controller;


import br.dev.andersonandrade.centauri.beans.MensagemBean;
import br.dev.andersonandrade.centauri.model.ChatModel;
import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import br.dev.andersonandrade.centauri.record.DestinatarioRecord;
import br.dev.andersonandrade.centauri.record.MensagemRecord;
import br.dev.andersonandrade.centauri.record.RemetenteRecord;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/principal")
    public String chat(Model model) {
        List<DestinatarioRecord> destinatarios = new ArrayList<>();
        RemetenteRecord remetente = new RemetenteRecord("Anderson", "teste@teste");
        destinatarios.add(new DestinatarioRecord("Anderson", "endereco_do_destinatario@example.com"));
        destinatarios.add(new DestinatarioRecord("Giovana", "teste@teste2"));
        destinatarios.add(new DestinatarioRecord("Aurora", "teste@teste3"));
        destinatarios.add(new DestinatarioRecord("natalie", "teste@teste4"));
        destinatarios.add(new DestinatarioRecord("natalie2", "teste@teste40"));
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