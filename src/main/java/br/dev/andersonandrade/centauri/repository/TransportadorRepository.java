package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.TransportadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportadorRepository extends JpaRepository<TransportadorEntity, Long> {
}