package br.dev.andersonandrade.centauri.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 04/10/2024
 */

public interface RemetenteDestinatariosRepository extends JpaRepository<RemetenteDestinatarios,Long> {
    Optional<RemetenteDestinatarios> findByEndereco(String endereco);
    List<RemetenteDestinatarios> findByEnderecoIn(Set<String> enderecos);
}
