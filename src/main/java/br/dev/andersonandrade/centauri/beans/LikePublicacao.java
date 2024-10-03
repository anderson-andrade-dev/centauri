package br.dev.andersonandrade.centauri.beans;

import br.dev.andersonandrade.centauri.entity.Likes;
import br.dev.andersonandrade.centauri.entity.Publicacao;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LikePublicacao {
    private final List<LikePublicacao> likesPublicacoes;
    private final List<Publicacao> publicacoes;
    private Publicacao publicacao;
    private Integer qtdPositivos;
    private Integer qtdNegativos;

    public LikePublicacao(@NotNull Likes like) {
        this.likesPublicacoes = new ArrayList<>();
        this.publicacoes = new ArrayList<>();
        this.publicacao = like.getPublicacao();
        this.qtdPositivos = like.getQtdPositivo();
        this.qtdNegativos = like.getQtdNegativo();
    }

    public LikePublicacao(@NotNull List<Likes> likes) {
        this.likesPublicacoes = new ArrayList<>();
        this.publicacoes = new ArrayList<>();
        likes.forEach(like -> {
            var likePublicacao = new LikePublicacao(like);
            this.publicacoes.add(like.getPublicacao());
            this.likesPublicacoes.add(likePublicacao);
        });

    }

    public Publicacao getPublicacao() {
        return publicacao;
    }

    public Integer getQtdPositivos() {
        return qtdPositivos;
    }

    public Integer getQtdNegativos() {
        return qtdNegativos;
    }

    public List<LikePublicacao> getLikesPublicacoes() {
        if (this.likesPublicacoes.isEmpty()) {
            return List.of();
        }

        return this.likesPublicacoes;
    }

    public List<Publicacao> getPublicacoes() {
        return this.publicacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikePublicacao that = (LikePublicacao) o;
        return Objects.equals(publicacao, that.publicacao) && Objects.equals(qtdPositivos, that.qtdPositivos) && Objects.equals(qtdNegativos, that.qtdNegativos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likesPublicacoes, publicacoes, publicacao, qtdPositivos, qtdNegativos);
    }

    @Override
    public String toString() {
        return "LikePublicacao{" +
                "likesPublicacoes=" + likesPublicacoes +
                ", publicacoes=" + publicacoes +
                ", publicacao=" + publicacao +
                ", qtdPositivos=" + qtdPositivos +
                ", qtdNegativos=" + qtdNegativos +
                '}';
    }
}
