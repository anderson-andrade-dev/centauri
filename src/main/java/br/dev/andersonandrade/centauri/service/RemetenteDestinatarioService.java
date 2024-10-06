package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.entity.RemetenteDestinatarios;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.exceptions.DestinatarioNaoEncontradoException;
import br.dev.andersonandrade.centauri.exceptions.RemetenteNaoEncotradoException;
import br.dev.andersonandrade.centauri.exceptions.UsuarioNaoEncontradoException;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.record.DestinatarioRecord;
import br.dev.andersonandrade.centauri.record.RemetenteRecord;
import br.dev.andersonandrade.centauri.repository.RemetenteDestinatariosRepository;
import br.dev.andersonandrade.centauri.repository.UsuarioRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    public RemetenteDestinatarioService(RemetenteDestinatariosRepository remetenteDestinatariosRepository,
                                        UsuarioRepository usuarioRepository, EmailService emailService) {
        this.remetenteDestinatariosRepository = remetenteDestinatariosRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    /**
     * Busca um remetente por endereço de e-mail.
     * <p>
     * Se o endereço não for encontrado, o sistema tentará buscar o usuário pelo e-mail fornecido.
     * Caso o usuário também não seja encontrado, lança uma exceção personalizada de "Usuário não Encontrado".
     * Se o usuário for encontrado, um novo remetente é criado e salvo no repositório.
     *
     * @param endereco o e-mail do remetente a ser buscado
     * @return uma instância de Remetente associada ao e-mail fornecido
     * @throws UsuarioNaoEncontradoException se o e-mail ou o usuário correspondente não for encontrado
     */
    public Remetente buscaRemetentePorEndereco(@NotNull @NotBlank String endereco) {

        RemetenteDestinatarios remetenteDestinatarios = remetenteDestinatariosRepository
                .findByEndereco(endereco)

                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findByLogin_Email(endereco)
                            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não Encontrado!"));

                    Remetente remetenteNovo = new RemetenteRecord(usuario.getNome(), usuario.getLogin().getEmail());

                    RemetenteDestinatarios remetenteDestinatariosNovo = new RemetenteDestinatarios(remetenteNovo);
                    remetenteDestinatariosRepository.save(remetenteDestinatariosNovo);

                    logger.info("Novo Remetente: {} Gravado na Base de Dados!", remetenteNovo.nome());

                    return remetenteDestinatariosNovo;
                });

        Usuario usuario = usuarioRepository.findByLogin_Email(remetenteDestinatarios.getEndereco())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não Encontrado!"));

        return new RemetenteRecord(usuario.getNome(), usuario.getLogin().getEmail());
    }

    /**
     * Busca uma lista de destinatários associados a um remetente específico.
     *
     * @param remetente O remetente cujo endereço será utilizado para buscar os destinatários.
     * @return Lista de destinatários associados ao remetente. Se nenhum destinatário for encontrado, retorna uma lista vazia.
     * @throws IllegalArgumentException se o remetente for nulo ou inválido.
     */
    public List<Destinatario> buscarDestinatariosPorRemetente(@NotNull Remetente remetente) {

        validaRemetente(remetente);

        Optional<RemetenteDestinatarios> result = remetenteDestinatariosRepository
                .findByEndereco(remetente.endereco());

        if (!result.isPresent()) {
            return List.of();
        }

        Set<String> enderecos = remetenteDestinatariosRepository
                .findByEnderecoIn(result.get().getEnderecoDestinatarios())
                .stream().map(remetenteDestinatarios -> remetenteDestinatarios.getEndereco())
                .collect(Collectors.toSet());

        List<Usuario> usuarios = usuarioRepository.findByEmails(enderecos);

        return usuarios.stream()
                .map(usuario -> new DestinatarioRecord(usuario.getNome(), usuario.getLogin().getEmail()))
                .collect(Collectors.toList());
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
     * @throws IllegalArgumentException Se o destinatário já estiver associado ao remetente
     * @throws RemetenteNaoEncotradoException Se o Remetente não for encontrado
     * @throws DestinatarioNaoEncontradoException Se o Destinatario não for encontrado
     */

    public void associar(@NotNull Remetente remetente,@NotNull Destinatario destinatario) {

        validaRemetenteDestinatario(remetente,destinatario);

        usuarioRepository.findByLogin_Email(remetente.endereco())
                .orElseThrow(() -> new RemetenteNaoEncotradoException("Remetente não Encotrado!"));
        usuarioRepository.findByLogin_Email(destinatario.endereco())
                .orElseThrow(() -> new DestinatarioNaoEncontradoException("Destinatario não Encotrado!"));

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

        logger.info("Validando associação do remetente {} com o destinatário {}",
                remetente.endereco(), destinatario.endereco());

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
    private static void validaRemetenteDestinatario(Remetente remetente, Destinatario destinatario) {
        validaRemetente(remetente);
        validaDestinatario(destinatario);
    }

    private static void validaRemetente(Remetente remetente) {
        Objects.requireNonNull(remetente, "Remetente não pode ser nulo!");

        if (remetente.endereco().isBlank()) {
            throw new IllegalArgumentException("Endereço do Remetente não pode ser nulo ou em branco!");
        }

        if (remetente.nome().isBlank()) {
            throw new IllegalArgumentException("Nome do Remetente não pode ser nulo ou em branco!");
        }
    }

    private static void validaDestinatario(Destinatario destinatario) {
        Objects.requireNonNull(destinatario, "Destinatário não pode ser nulo!");

        if (destinatario.endereco().isBlank()) {
            throw new IllegalArgumentException("Endereço do Destinatário não pode ser nulo ou em branco!");
        }

        if (destinatario.nome().isBlank()) {
            throw new IllegalArgumentException("Nome do Destinatário não pode ser nulo ou em branco!");
        }
    }

}
