package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCodigo(String codigo);

    Optional<Usuario> findByLogin_Email(String username);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.login.ativo = TRUE")
    Optional<Long> countAtivos();
}
