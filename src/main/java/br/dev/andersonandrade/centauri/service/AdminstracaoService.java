package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.repository.PublicacaoRepository;
import br.dev.andersonandrade.centauri.repository.TransportadorRepository;
import br.dev.andersonandrade.centauri.repository.UsuarioRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe UsuarioService
 * <p>
 * Responsável por gerenciar as informações dos usuários, incluindo contagens
 * de usuários cadastrados, ativos, inativos, publicações e mensagens.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 */
@Service
public class AdminstracaoService {

    private Long usuariosCadastrados;
    private Long usuariosAtivos;
    private Long usuariosInativos;
    private Long mensagensUsuarios;
    private Long publicacaoUsuarios;

    private final UsuarioRepository usuarioRepository;
    private final PublicacaoRepository publicacaoRepository;
    private final TransportadorRepository transportadorRepository;

    /**
     * Construtor que inicializa a classe UsuarioService.
     *
     * @param usuarioRepository       O repositório para gerenciar usuários.
     * @param publicacaoRepository    O repositório para gerenciar publicações.
     * @param transportadorRepository O repositório para gerenciar transportadores.
     */
    @Autowired
    public AdminstracaoService(@NotNull UsuarioRepository usuarioRepository,
                               @NotNull PublicacaoRepository publicacaoRepository,
                               @NotNull TransportadorRepository transportadorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.publicacaoRepository = publicacaoRepository;
        this.transportadorRepository = transportadorRepository;
        init();
    }

    /**
     * Inicializa as contagens de usuários, publicações e mensagens.
     * Este método é chamado no construtor.
     */
    private void init() {
        this.usuariosCadastrados = usuarioRepository.count();
        this.usuariosAtivos = usuarioRepository.countAtivos().orElse(0L); // Novo método no repositório
        this.usuariosInativos = usuariosCadastrados - usuariosAtivos;
        this.publicacaoUsuarios = publicacaoRepository.countAtivas().orElse(0L); // Novo método no repositório
        this.mensagensUsuarios = transportadorRepository.count();
    }

    /**
     * Retorna a quantidade total de mensagens de usuários.
     *
     * @return A quantidade de mensagens.
     */
    public Long getMensagensUsuarios() {
        return mensagensUsuarios;
    }

    /**
     * Retorna a quantidade total de usuários cadastrados.
     *
     * @return A quantidade de usuários cadastrados.
     */
    public Long getUsuariosCadastrados() {
        return usuariosCadastrados;
    }

    /**
     * Retorna a quantidade total de usuários ativos.
     *
     * @return A quantidade de usuários ativos.
     */
    public Long getUsuariosAtivos() {
        return usuariosAtivos;
    }

    /**
     * Retorna a quantidade total de usuários inativos.
     *
     * @return A quantidade de usuários inativos.
     */
    public Long getUsuariosInativos() {
        return usuariosInativos;
    }

    /**
     * Retorna a quantidade total de publicações de usuários.
     *
     * @return A quantidade de publicações.
     */
    public Long getPublicacaoUsuarios() {
        return publicacaoUsuarios;
    }
}
