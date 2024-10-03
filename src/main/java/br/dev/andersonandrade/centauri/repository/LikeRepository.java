package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Likes;
import br.dev.andersonandrade.centauri.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT l " +
            "FROM Likes l " +
            "WHERE l.publicacao.ativa = true " +
            "ORDER BY l.qtdPositivo desc")
    List<Likes> findAllOrderByQtdPositivo();

    @Query("SELECT l FROM Likes l " +
            "LEFT JOIN l.publicacao p " +
            "LEFT JOIN p.usuario u " +
            "WHERE p.usuario = :usuario " +
            "AND l.publicacao.ativa = true " +
            "ORDER BY l.qtdPositivo desc")
    List<Likes> findByLikesPorUsuario(@Param("usuario") Usuario usuario);
}
