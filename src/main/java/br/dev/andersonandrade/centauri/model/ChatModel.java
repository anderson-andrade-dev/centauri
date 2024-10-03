package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.beans.SalaChat;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.enumeradores.Prioridade;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.record.ChatMensagemRecord;
import br.dev.andersonandrade.centauri.record.RemetenteRecord;
import br.dev.andersonandrade.centauri.service.CorreioMensagem;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Classe ChatModel
 * <p>
 * Esta classe gerencia a lógica de envio e recebimento de mensagens entre usuários em salas de chat.
 * Utiliza a classe SalaChat para armazenar e recuperar mensagens.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 * @contact andersonandradedev@outlook.com
 */
@Component
@ApplicationScope
public class ChatModel {
    private final CorreioMensagem correio;
    private final UsuarioService usuarioService;

    /**
     * Construtor que inicializa a classe ChatModel.
     *
     * @param correio        O serviço responsável pelo envio e recebimento de mensagens.
     * @param usuarioService O modelo de usuário utilizado para gerenciar os usuários do sistema.
     */
    public ChatModel(CorreioMensagem correio, UsuarioService usuarioService) {
        this.correio = correio;
        this.usuarioService = usuarioService;
    }

    /**
     * Envia uma mensagem de um remetente para um destinatário.
     * <p>
     * Este método verifica se o remetente, destinatário e mensagem são válidos.
     * Caso a mensagem esteja vazia, uma exceção é lançada.
     *
     * @param remetente    O remetente da mensagem.
     * @param destinatario O destinatário da mensagem.
     * @param mensagem     A mensagem a ser enviada.
     * @throws IllegalArgumentException se a mensagem estiver vazia ou se o remetente/destinatário forem nulos.
     */
    public void enviar(@NotNull Remetente remetente, @NotNull Destinatario destinatario, @NotNull Mensagem mensagem) {
        if (remetente == null || destinatario == null) {
            throw new IllegalArgumentException("Remetente e destinatário não podem ser nulos.");
        }
        if (mensagem.getConteudo() == null || mensagem.getConteudo().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode ser vazia.");
        }

        SalaChat salaChat = SalaChat.abrir(remetente, destinatario).adicionarMensagemDestinatario(mensagem);
        correio.recebeMensagem(destinatario, remetente, mensagem, Prioridade.URGENTE);
    }

    /**
     * Envia uma mensagem de um usuário identificado pelo e-mail do remetente para um destinatário.
     * <p>
     * Este método busca o usuário pelo e-mail fornecido e, se encontrado,
     * chama o método enviar para enviar a mensagem. Caso contrário, uma exceção é lançada.
     *
     * @param emailRemetente O e-mail do remetente.
     * @param destinatario   O destinatário da mensagem.
     * @param mensagem       A mensagem a ser enviada.
     * @throws IllegalArgumentException se o usuário não for encontrado ou se a mensagem estiver vazia.
     */
    public void enviar(@NotNull String emailRemetente, @NotNull Destinatario destinatario, @NotNull Mensagem mensagem) {
        Usuario usuario = usuarioService.buscaPorEmail(emailRemetente);
        if (usuario != null) {
            Remetente remetente = new RemetenteRecord(usuario.getNome(), usuario.getLogin().getEmail());
            enviar(remetente, destinatario, mensagem);
        } else {
            throw new IllegalArgumentException("Verifique os parâmetros! O usuário não foi encontrado.");
        }
    }

    /**
     * Responde a uma mensagem de um remetente para um destinatário.
     * <p>
     * Este método verifica se o remetente, destinatário e mensagem são válidos
     * antes de adicionar a mensagem à sala de chat.
     *
     * @param remetente    O remetente da mensagem.
     * @param destinatario O destinatário da mensagem.
     * @param mensagem     A mensagem a ser enviada como resposta.
     * @throws IllegalArgumentException se remetente, destinatário ou mensagem forem nulos.
     */
    public void responder(@NotNull Remetente remetente, @NotNull Destinatario destinatario, @NotNull Mensagem mensagem) {
        if (remetente == null || destinatario == null || mensagem == null) {
            throw new IllegalArgumentException("Remetente, destinatário e mensagem não podem ser nulos.");
        }

        SalaChat salaChat = SalaChat.abrir(remetente, destinatario).adicionarMensagemRemetente(mensagem);
        correio.recebeMensagem(destinatario, remetente, mensagem, Prioridade.URGENTE);
    }

    /**
     * Recupera todas as mensagens entre um usuário e um destinatário.
     * <p>
     * Este método busca o usuário pelo e-mail fornecido e, se encontrado,
     * recupera as mensagens enviadas e recebidas entre o usuário e o destinatário.
     * Se o usuário não for encontrado, um registro vazio é retornado.
     *
     * @param email        O e-mail do usuário que deseja recuperar as mensagens.
     * @param destinatario O destinatário cujas mensagens devem ser recuperadas.
     * @return Um registro contendo as mensagens enviadas e recebidas entre o usuário e o destinatário.
     */
    public ChatMensagemRecord mensagens(@NotNull String email, @NotNull Destinatario destinatario) {
        Usuario usuario = usuarioService.buscaPorEmail(email);
        if (usuario != null) {
            RemetenteRecord remetente = new RemetenteRecord(usuario.getNome(), usuario.getLogin().getEmail());
            SalaChat salaChat = SalaChat.abrir(remetente, destinatario);

            // Recupera mensagens enviadas e recebidas
            correio.mensagens(remetente).forEach(salaChat::adicionarMensagemRemetente);
            correio.mensagens(destinatario).forEach(salaChat::adicionarMensagemDestinatario);

            // Ordena as mensagens por data de envio
            List<Mensagem> mensagensOrdenadasRemetente = new ArrayList<>(Set.copyOf(salaChat.getMensagensRemetente()));
            List<Mensagem> mensagensOrdenadasDestinatario = new ArrayList<>(Set.copyOf(salaChat.getMensagensDestinatario()));

            mensagensOrdenadasRemetente.sort(Comparator.comparing(Mensagem::getDataEnvio));
            mensagensOrdenadasDestinatario.sort(Comparator.comparing(Mensagem::getDataEnvio));

            return new ChatMensagemRecord(List.copyOf(mensagensOrdenadasRemetente),
                    List.copyOf(mensagensOrdenadasDestinatario));
        }

        return new ChatMensagemRecord(List.of(), List.of());
    }
}
