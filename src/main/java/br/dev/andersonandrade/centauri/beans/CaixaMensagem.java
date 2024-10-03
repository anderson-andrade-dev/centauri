package br.dev.andersonandrade.centauri.beans;

import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Classe CaixaMensagem.
 * <p>
 * Representa uma caixa de mensagens associada a um usuário. Esta classe
 * armazena uma lista de mensagens e a data da última atualização, permitindo
 * o gerenciamento eficiente das mensagens de um usuário específico.
 *
 * @Autor: Anderson Andrade Dev
 * @Data: 29 de setembro de 2024
 * @Contato: andersonandradedev@outlook.com
 */
public class CaixaMensagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Mensagem> mensagens;
    private final Usuario usuario;
    private final LocalDateTime dataUltimaAtualizacao;

    /**
     * Construtor da classe CaixaMensagem.
     *
     * @param mensagens Lista de mensagens a serem armazenadas na caixa.
     * @param usuario   Usuário associado a esta caixa de mensagens.
     */
    public CaixaMensagem(@NotNull List<Mensagem> mensagens, @NotNull Usuario usuario) {
        Objects.requireNonNull(mensagens, "Lista de mensagens não pode ser nula!");
        Objects.requireNonNull(usuario, "Usuário não pode ser nulo!");

        // Copia a lista de mensagens para garantir a imutabilidade
        this.mensagens = List.copyOf(mensagens);
        this.usuario = usuario;
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }

    /**
     * Retorna a lista de mensagens contidas na caixa.
     *
     * @return Lista de mensagens.
     */
    public List<Mensagem> getMensagens() {
        return List.copyOf(mensagens);
    }

    /**
     * Retorna o usuário associado a esta caixa de mensagens.
     *
     * @return Usuário associado.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Retorna a data da última atualização da caixa de mensagens.
     *
     * @return Data da última atualização.
     */
    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    /**
     * Retorna uma nova instância de CaixaMensagem com a lista de mensagens atualizada.
     *
     * @param novasMensagens Nova lista de mensagens a ser armazenada.
     * @return Nova instância de CaixaMensagem.
     */
    public CaixaMensagem adicionarMensagens(List<Mensagem> novasMensagens) {
        Objects.requireNonNull(novasMensagens, "Lista de mensagens não pode ser nula!");
        return new CaixaMensagem(novasMensagens, this.usuario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaixaMensagem that = (CaixaMensagem) o;
        return Objects.equals(mensagens, that.mensagens) &&
                Objects.equals(usuario, that.usuario) &&
                Objects.equals(dataUltimaAtualizacao, that.dataUltimaAtualizacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mensagens, usuario, dataUltimaAtualizacao);
    }

    @Override
    public String toString() {
        return "CaixaMensagem{" +
                "mensagens=" + mensagens +
                ", usuario=" + usuario +
                ", dataUltimaAtualizacao=" + dataUltimaAtualizacao +
                '}';
    }
}
