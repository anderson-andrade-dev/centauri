package br.dev.andersonandrade.centauri.beans;

import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.interfaces.SalaChatInterface;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe SalaChat
 * <p>
 * Representa uma sala de chat entre um remetente e um destinatário.
 * Gerencia o envio e armazenamento de mensagens de ambos os lados.
 * <p>
 * A classe oferece funcionalidades para:
 * - Criar ou recuperar uma sala de chat existente entre um remetente e um destinatário.
 * - Adicionar mensagens enviadas por ambos os participantes.
 * - Recuperar mensagens trocadas entre o remetente e o destinatário.
 * - Fechar todas as salas de chat ativas.
 * <p>
 * Exemplos de uso:
 * 1. Criar ou abrir uma sala de chat:
 * SalaChat sala = SalaChat.abrir(remetente, destinatario);
 * <p>
 * 2. Adicionar mensagens:
 * sala.adicionarMensagemRemetente(mensagem1);
 * sala.adicionarMensagemDestinatario(mensagem2);
 * <p>
 * 3. Obter mensagens:
 * List<Mensagem> mensagensRemetente = sala.getMensagensRemetente();
 * List<Mensagem> mensagensDestinatario = sala.getMensagensDestinatario();
 * <p>
 * 4. Fechar todas as salas:
 * SalaChat.fecharTodas();
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 * @contact andersonandradedev@outlook.com
 */
public class SalaChat implements SalaChatInterface {

    private static final Set<SalaChat> salas = Collections.synchronizedSet(new HashSet<>());
    private final Remetente remetente;
    private final Destinatario destinatario;
    private final Set<Mensagem> mensagensDestinatario;
    private final Set<Mensagem> mensagensRemetente;

    /**
     * Construtor privado que inicializa uma nova sala de chat entre um remetente e um destinatário.
     *
     * @param remetente    O remetente da sala de chat.
     * @param destinatario O destinatário da sala de chat.
     * @throws IllegalArgumentException se o remetente e destinatário forem os mesmos.
     */
    private SalaChat(@NotNull Remetente remetente, @NotNull Destinatario destinatario) {
        if (remetente.endereco().equals(destinatario.endereco())) {
            throw new IllegalArgumentException("Remetente e destinatário não podem ser os mesmos.");
        }
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.mensagensDestinatario = new HashSet<>();
        this.mensagensRemetente = new HashSet<>();
    }

    /**
     * Abre uma nova sala de chat entre o remetente e o destinatário.
     * Se uma sala entre os dois já existir, retorna a sala existente.
     *
     * @param remetente    O remetente da sala de chat.
     * @param destinatario O destinatário da sala de chat.
     * @return A instância de SalaChat criada ou existente.
     */
    public static SalaChat abrir(@NotNull Remetente remetente, @NotNull Destinatario destinatario) {
        synchronized (salas) {
            return salas.stream()
                    .filter(sala -> sala.destinatario.endereco().equals(destinatario.endereco()) &&
                            sala.remetente.endereco().equals(remetente.endereco()))
                    .findFirst()
                    .orElseGet(() -> {
                        SalaChat novaSala = new SalaChat(remetente, destinatario);
                        salas.add(novaSala);
                        return novaSala;
                    });
        }
    }

    /**
     * Adiciona uma mensagem ao destinatário e armazena a mensagem.
     *
     * @param mensagem A mensagem a ser enviada ao destinatário.
     * @return A instância da SalaChat atual para encadeamento de métodos.
     */
    public SalaChat adicionarMensagemDestinatario(@NotNull Mensagem mensagem) {
        this.mensagensDestinatario.add(mensagem);
        return this;
    }

    /**
     * Adiciona uma mensagem ao remetente e armazena a mensagem.
     *
     * @param mensagem A mensagem a ser enviada ao remetente.
     * @return A instância da SalaChat atual para encadeamento de métodos.
     */
    public SalaChat adicionarMensagemRemetente(@NotNull Mensagem mensagem) {
        this.mensagensRemetente.add(mensagem);
        return this;
    }

    /**
     * Obtém o remetente da sala de chat.
     *
     * @return O remetente da sala de chat.
     */
    public Remetente getRemetente() {
        return this.remetente;
    }

    /**
     * Obtém o destinatário da sala de chat.
     *
     * @return O destinatário da sala de chat.
     */
    public Destinatario getDestinatario() {
        return this.destinatario;
    }

    /**
     * Obtém todas as mensagens enviadas pelo remetente.
     *
     * @return Uma lista imutável de mensagens enviadas pelo remetente.
     */
    public List<Mensagem> getMensagensRemetente() {
        return List.copyOf(this.mensagensRemetente);
    }

    /**
     * Obtém todas as mensagens enviadas ao destinatário.
     *
     * @return Uma lista imutável de mensagens enviadas ao destinatário.
     */
    public List<Mensagem> getMensagensDestinatario() {
        return List.copyOf(this.mensagensDestinatario);
    }

    /**
     * Fecha todas as salas de chat abertas, removendo-as do conjunto de salas.
     */
    public static void fecharTodas() {
        synchronized (salas) {
            salas.clear();
        }
    }

    /**
     * Retorna uma representação textual da sala de chat, incluindo remetente,
     * destinatário e contagem de mensagens trocadas.
     *
     * @return Uma string representando os detalhes da sala de chat.
     */
    @Override
    public String toString() {
        return "SalaChat{" +
                "Nome Remetente = " + remetente.nome() +
                ", Endereco Remetente = " + remetente.endereco() +
                ", Nome Destinatario = " + destinatario.nome() +
                ", Endereco Destinatario = " + destinatario.endereco() +
                ", Quantidade de Mensagens Destinatario = " + mensagensDestinatario.size() +
                ", Quantidade de Mensagens Remetente = " + mensagensRemetente.size() +
                '}';
    }
}
