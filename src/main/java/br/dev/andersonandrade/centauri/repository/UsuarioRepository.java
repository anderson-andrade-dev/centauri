package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCodigo(String codigo);

    Optional<Usuario> findByLogin_Email(String email);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.login.ativo = TRUE")
    Optional<Long> countAtivos();

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.login.ativo = true " +
            "AND u.login.email =: email " +
            "AND u.login.senha.chave =: senha")
    @EntityGraph(attributePaths = {"messagesSytem","publicacoes"})
    Optional<Usuario> findByEmailAndSenha(String email,String Senha);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.login.email IN :emails ")
    List<Usuario> findByEmails(Set<String> emails);

}
