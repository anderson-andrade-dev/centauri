package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.entity.RemetenteDestinatarios;
import br.dev.andersonandrade.centauri.entity.RemetenteDestinatariosRepository;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.record.RemetenteRecord;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Serviço responsável pela associação entre remetentes e destinatários.
 * Ele verifica se um destinatário já está associado a um remetente,
 * e realiza novas associações quando necessário.
 *
 * @author Anderson Andrade Dev
 * @Data de Criação 05/10/2024
 */
@Service
public class RemetenteDestinatarioService {

    private final RemetenteDestinatariosRepository remetenteDestinatariosRepository;
    private final Logger logger = LoggerFactory.getLogger(RemetenteDestinatarioService.class);

    public RemetenteDestinatarioService(RemetenteDestinatariosRepository remetenteDestinatariosRepository) {
        this.remetenteDestinatariosRepository = remetenteDestinatariosRepository;
    }

    /**
     * Associa um destinatário a um remetente. Se o remetente ou destinatário não existirem,
     * eles são criados. Além disso, é verificado se a associação já existe para evitar duplicações.
     *
     * <p>A busca por remetente e destinatário é realizada em uma única consulta, verificando ambos os endereços
     * ao mesmo tempo para otimizar o desempenho.</p>
     *
     * @param remetente Objeto que representa o remetente
     * @param destinatario Objeto que representa o destinatário
     * @throws IllegalArgumentException Se o destinatário já estiver associado ao remetente
     */
    public void associar(@NotNull Remetente remetente,@NotNull Destinatario destinatario) {

        validaRemetenteDestinatario(remetente,destinatario);

        Set<String> enderecos = Set.of(remetente.endereco(), destinatario.endereco());

        List<RemetenteDestinatarios> resultado = remetenteDestinatariosRepository.findByEnderecoIn(enderecos);

        Optional<RemetenteDestinatarios> remetenteExistente = resultado
                .stream()
                .filter(r -> r.getEnderecoDestinatarios().contains(remetente.endereco()))
                .findFirst();

        Optional<RemetenteDestinatarios> destinatarioExistente = resultado
                .stream()
                .filter(d-> d.getEnderecoDestinatarios().contains(destinatario.endereco()))
                .findFirst();


        remetenteExistente.orElseGet(() -> remetenteDestinatariosRepository.save(new RemetenteDestinatarios(remetente)));

        destinatarioExistente.orElseGet(() -> remetenteDestinatariosRepository
                .save(new RemetenteDestinatarios(new RemetenteRecord(destinatario.nome(), destinatario.endereco()))));


        if (jaAssociado(remetente, destinatario)) {
            logger.info("Destinatario: {} já associado ao Remetente: {}", destinatario.nome(), remetente.nome());
            throw new IllegalArgumentException("Destinatário já associado ao Remetente");
        }

        remetenteExistente.ifPresent(r -> {
            r.getEnderecoDestinatarios().add(destinatario.endereco());
            remetenteDestinatariosRepository.save(r);
            logger.info("Destinatário: {} cadastrado no Remetente: {}", destinatario.nome(), remetente.nome());
        });

        destinatarioExistente.ifPresent(d -> {
            d.getEnderecoDestinatarios().add(remetente.endereco());
            remetenteDestinatariosRepository.save(d);
            logger.info("Remetente: {} cadastrado no Destinatário: {}",remetente.nome(), destinatario.nome());
        });

    }

    /**
     * Verifica se o destinatário já está associado ao remetente.
     *
     * @param remetente Objeto que representa o remetente
     * @param destinatario Objeto que representa o destinatário
     * @return true se o destinatário já estiver associado ao remetente, caso contrário false
     */
    public boolean jaAssociado(Remetente remetente, Destinatario destinatario) {

        validaRemetenteDestinatario(remetente,destinatario);

        Optional<RemetenteDestinatarios> remetenteDestinatarios = remetenteDestinatariosRepository
                .findByEndereco(remetente.endereco());

        if (remetenteDestinatarios.isPresent()) {
            return remetenteDestinatarios.get().getEnderecoDestinatarios().contains(destinatario.endereco());
        }

        return false;
    }

    /**
     * Valida os objetos Remetente e Destinatário. Certifica-se de que nenhum campo necessário está vazio ou nulo.
     *
     * @param remetente Objeto que representa o remetente
     * @param destinatario Objeto que representa o destinatário
     * @throws IllegalArgumentException Se os campos obrigatórios estiverem vazios ou em branco
     */
    private static void validaRemetenteDestinatario(Remetente remetente, Destinatario destinatario){
        Objects.requireNonNull(remetente,"Remetente não pode ser nulo!");
        Objects.requireNonNull(destinatario,"Destinatário não pode ser nulo!");

        if(remetente.endereco().isBlank()){
            throw new IllegalArgumentException("Endereço do Remetente não pode ser nulo ou em branco!");
        }

        if(remetente.nome().isBlank()){
            throw new IllegalArgumentException("Nome do Remetente não pode ser nulo ou em branco!");
        }

        if(destinatario.endereco().isBlank()){
            throw new IllegalArgumentException("Endereço do Destinatário não pode ser nulo ou em branco!");
        }

        if(destinatario.nome().isBlank()){
            throw new IllegalArgumentException("Nome do Destinatário não pode ser nulo ou em branco!");
        }
    }
}
