package br.dev.andersonandrade.centauri.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Likes implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Publicacao publicacao;

    private int qtdPositivo = 0;

    private int qtdNegativo = 0;

    @Deprecated
    protected Likes() {
        //Obrigatorio JPA
    }


    public Likes(@NotNull Publicacao publicacao) {
        this.publicacao = publicacao;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Publicacao getPublicacao() {
        return publicacao;
    }


    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }


    public int getQtdPositivo() {
        return qtdPositivo;
    }


    public void setQtdPositivo(int qtdPositivo) {
        this.qtdPositivo = qtdPositivo;
    }


    public int getQtdNegativo() {
        return qtdNegativo;
    }


    public void setQtdNegativo(int qtdNegativo) {
        this.qtdNegativo = qtdNegativo;
    }


    public int likePostivo() {
        return ++qtdPositivo;
    }

    public int dislike() {
        if (qtdNegativo > 0) {
            return --qtdNegativo;
        }
        return qtdNegativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Likes likes = (Likes) o;
        return qtdPositivo == likes.qtdPositivo && qtdNegativo == likes.qtdNegativo && Objects.equals(id, likes.id) && Objects.equals(publicacao, likes.publicacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicacao, qtdPositivo, qtdNegativo);
    }

    @Override
    public String toString() {
        return "Likes{" +
                "id=" + id +
                ", publicacao=" + publicacao +
                ", qtdPositivo=" + qtdPositivo +
                ", qtdNegativo=" + qtdNegativo +
                '}';
    }
}
