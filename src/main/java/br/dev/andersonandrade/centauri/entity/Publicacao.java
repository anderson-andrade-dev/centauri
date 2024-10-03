package br.dev.andersonandrade.centauri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Publicacao implements Serializable {

    @Serial
    private static final long serialVersionUID = -5821233874776374593L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255)
    private String urlImagem;
    @Column(length = 300, nullable = false)
    private String titulo;
    @Column(length = 5000, nullable = false)
    private String texto;
    private LocalDateTime dataPublicacao;
    private boolean ativa = true;
    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario usuario;

    @Deprecated
    protected Publicacao() {
        //Obrigatorio JPA
    }

    public Publicacao(Usuario usuario, String urlImagem, String texto, LocalDateTime dataPublicacao, boolean ativa) {
        this.usuario = usuario;
        this.urlImagem = urlImagem;
        this.titulo = devolverTitulo(texto);
        this.texto = removeTitulo(texto);
        this.dataPublicacao = dataPublicacao;
        this.ativa = ativa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private String devolverTitulo(@NotNull String texto) {
        String titulo = texto.split("\n")[0];
        if (titulo == null || titulo.isEmpty()) {
            return texto;
        }
        return titulo;
    }

    private String removeTitulo(@NotNull String texto) {
        String[] linhas = texto.split("\n");
        String textoSemtitulo;
        if (linhas.length > 0) {
            textoSemtitulo = texto.replace(linhas[0], "");
            return textoSemtitulo;
        }
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacao that = (Publicacao) o;
        return ativa == that.ativa && Objects.equals(id, that.id) && Objects.equals(urlImagem, that.urlImagem) && Objects.equals(titulo, that.titulo) && Objects.equals(texto, that.texto) && Objects.equals(dataPublicacao, that.dataPublicacao) && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlImagem, titulo, texto, dataPublicacao, ativa, usuario);
    }

    @Override
    public String toString() {
        return "Publicacao{" +
                "id=" + id +
                ", urlImagem='" + urlImagem + '\'' +
                ", titulo='" + titulo + '\'' +
                ", texto='" + texto + '\'' +
                ", dataPublicacao=" + dataPublicacao +
                ", ativa=" + ativa +
                ", usuario=" + usuario +
                '}';
    }
}
