package br.dev.andersonandrade.centauri.repository;

import br.dev.andersonandrade.centauri.entity.RemetenteDestinatarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 04/10/2024
 */

public interface RemetenteDestinatariosRepository extends JpaRepository<RemetenteDestinatarios,Long> {
    Optional<RemetenteDestinatarios> findByEndereco(String endereco);

    @Query("SELECT r FROM RemetenteDestinatarios r " +
            "WHERE r.endereco IN :enderecos ")
    List<RemetenteDestinatarios> findByEnderecoIn(Set<String> enderecos);
}
