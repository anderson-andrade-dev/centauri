package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Long> {
    List<Publicacao> findByDataPublicacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Publicacao> findByUsuario(Usuario usuario);

    @Query("select p from Publicacao p " +
            "left join p.usuario " +
            "where p.usuario.codigo = :codigo " +
            "and p.ativa = true" +
            " order by p.id desc")
    List<Publicacao> findByPublicacaoUsuarioOrdemDecrescente(@Param("codigo") String codigo);

    @Query("select p from Publicacao p order by p.dataPublicacao desc")
    List<Publicacao> publicacaoesOrdDataDescr();

    @Query("select count(p) from Publicacao  p where p.ativa = true")
    Optional<Long> countAtivas();

}
