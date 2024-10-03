package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
}
