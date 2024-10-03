package br.dev.andersonandrade.centauri.entity;

import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class MensagemUsuario implements Mensagem, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;
    private String texto;
    private LocalDateTime data;
    private boolean lida;

    @Deprecated
    protected MensagemUsuario() {
        //Obrigatorio JPA
        this.data = LocalDateTime.now();
        this.lida = false;
    }

    public MensagemUsuario(Usuario usuario, String texto) {
        this.usuario = usuario;
        this.texto = texto;
        this.data = LocalDateTime.now();
        this.lida = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String message) {
        this.texto = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public LocalDateTime getDataEnvio() {
        return null;
    }

    @Override
    public LocalDateTime getDataLeitura() {
        return null;
    }

    @Override
    public String getTitulo() {
        return "";
    }

    @Override
    public String getConteudo() {
        return "";
    }

//    @Override
//    public Remetente remetente() {
//        return null;
//    }

    @Override
    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MensagemUsuario that = (MensagemUsuario) o;
        return lida == that.lida && Objects.equals(id, that.id) && Objects.equals(usuario, that.usuario) && Objects.equals(texto, that.texto) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, texto, data, lida);
    }

    @Override
    public String toString() {
        return "MensagemSistema{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", texto='" + texto + '\'' +
                ", data=" + data +
                ", lida=" + lida +
                '}';
    }
}
