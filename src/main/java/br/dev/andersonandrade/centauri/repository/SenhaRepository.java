package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Senha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenhaRepository extends JpaRepository<Senha, Long> {
}
