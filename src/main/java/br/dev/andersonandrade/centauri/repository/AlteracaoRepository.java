package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Alteracao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlteracaoRepository extends JpaRepository<Alteracao, Long> {
}
