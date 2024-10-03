package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
    boolean existsByNomeUsuario(String nomeUsuario);
}
