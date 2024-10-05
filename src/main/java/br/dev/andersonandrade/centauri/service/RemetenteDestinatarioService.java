package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.entity.RemetenteDestinatarios;
import br.dev.andersonandrade.centauri.entity.RemetenteDestinatariosRepository;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 05/10/2024
 */
@Service
public class RemetenteDestinatarioService {

    private final RemetenteDestinatariosRepository remetenteDestinatariosRepository;

    public RemetenteDestinatarioService(RemetenteDestinatariosRepository remetenteDestinatariosRepository) {
        this.remetenteDestinatariosRepository = remetenteDestinatariosRepository;
    }

    public void associar(Remetente remetente, Destinatario destinatario) {
        RemetenteDestinatarios remetenteDestinatarios = remetenteDestinatariosRepository
                .findByEndereco(remetente.endereco()).orElse(null);

        if (Objects.isNull(remetenteDestinatarios)) {
            remetenteDestinatariosRepository.save(new RemetenteDestinatarios(remetente, destinatario));

        } else if (!remetenteDestinatarios.getEnderecoDestinatarios().contains(destinatario.endereco())) {
            remetenteDestinatarios.getEnderecoDestinatarios().add(destinatario.endereco());
            remetenteDestinatariosRepository.save(remetenteDestinatarios);
        }
    }
}
