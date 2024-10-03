package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.entity.Likes;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.repository.LikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class LikeModel {

    private final LikeRepository likeRepository;


    @Autowired
    public LikeModel(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Transactional
    public void adicionarLike(Long idDaPublicacao) {
        Optional<Likes> temLike = likeRepository.findAll().stream()
                .filter(like -> Objects.equals(like.getPublicacao().getId(), idDaPublicacao))
                .findFirst();
        if (temLike.isPresent()) {
            Likes likeExiste = temLike.get();
            likeExiste.setQtdPositivo(likeExiste.likePostivo());
            likeRepository.save(likeExiste);
        } else {
            throw new RuntimeException("Erro na função do like ");
        }
    }


    @Transactional
    public void dislike(Long idDaPublicacao) {
        Optional<Likes> temLike = likeRepository.findAll().stream()
                .filter(like -> Objects.equals(like.getPublicacao().getId(), idDaPublicacao))
                .findFirst();

        if (temLike.isPresent()) {
            Likes likeExiste = temLike.get();
            int novosLikes = likeExiste.getQtdPositivo() - 1;
            if (novosLikes < 0) {
                novosLikes = 0; // pro numero não ficar negativo na tabela likes
            }
            likeExiste.setQtdPositivo(novosLikes);
            int novosDislikes = likeExiste.getQtdNegativo() + 1;
            likeExiste.setQtdNegativo(novosDislikes);
            likeRepository.save(likeExiste);
        }
    }

    public List<Likes> listaRank() {
        List<Likes> likes = likeRepository.findAllOrderByQtdPositivo();
        if (likes.isEmpty()) {
            return List.of();
        }
        return likes;
    }

    public List<Likes> buscaPorUsuario(Usuario usuario) {
        List<Likes> likes = likeRepository.findByLikesPorUsuario(usuario);
        if (likes.isEmpty()) {
            return List.of();
        }
        return likes;
    }

    public void salvar(Likes likes) {
        likeRepository.save(likes);
    }
}
