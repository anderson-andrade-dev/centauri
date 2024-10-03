package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.entity.Login;
import br.dev.andersonandrade.centauri.entity.Senha;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.exceptions.EmailException;
import br.dev.andersonandrade.centauri.record.UsuarioRecord;
import br.dev.andersonandrade.centauri.repository.LoginRepository;
import br.dev.andersonandrade.centauri.repository.UsuarioRepository;
import br.dev.andersonandrade.centauri.service.EmailService;
import br.dev.andersonandrade.centauri.uteis.SenhaUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * Classe que representa o serviço de usuário.
 * Contém métodos para manipulação de usuários, como salvar, buscar e editar.
 * <p>
 * Autor: Anderson Andrade Dev
 * Email: andersonandradedev@outlook.com
 * Data: 29 de setembro de 2024
 */
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final LoginRepository loginRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService,
                          LoginRepository loginRepository) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.loginRepository = loginRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Salva um novo usuário no repositório e envia um e-mail de ativação.
     *
     * @param usuarioRecord O registro do usuário a ser salvo.
     * @return O usuário salvo ou null se o registro for nulo.
     */
    public Usuario salvar(UsuarioRecord usuarioRecord) {
        if (usuarioRecord == null) {
            throw new IllegalArgumentException("UsuarioRecord não pode ser nulo");
        }

        Usuario usuario = new Usuario(usuarioRecord, new Senha(passwordEncoder.encode(usuarioRecord.senha())));
        usuarioRepository.save(usuario);
        enviarEmailDeAtivacao(usuario);
        return usuario;
    }

    /**
     * Envia um e-mail de ativação para o usuário.
     *
     * @param usuario O usuário para o qual o e-mail de ativação será enviado.
     */
    private void enviarEmailDeAtivacao(Usuario usuario) {
        try {
            emailService.enviarEmailAtivacao(usuario);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar e-mail de ativação", e);
        }
    }

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email O e-mail do usuário.
     * @return O usuário encontrado ou null se não existir.
     */
    public Usuario buscaPorEmail(String email) {
        return usuarioRepository.findByLogin_Email(email).orElse(null);
    }

    /**
     * Busca um usuário pelo seu código.
     *
     * @param codigo O código do usuário a ser buscado.
     * @return O usuário encontrado ou null se não existir.
     */
    @Transactional
    public Usuario buscarPorCodigo(String codigo) {
        return usuarioRepository.findByCodigo(codigo).orElse(null);
    }

    /**
     * Busca um usuário pelo e-mail e senha.
     *
     * @param senha A senha do usuário.
     * @param email O e-mail do usuário.
     * @return O usuário encontrado ou null se não existir.
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorEmailESenha(String senha, String email) {
        if (senha == null || email == null) {
            throw new IllegalArgumentException("Verifique os parâmetros, eles não podem ser nulos!");
        }
        if (senha.isBlank() || email.isBlank()) {
            throw new IllegalArgumentException("Verifique os parâmetros, eles não podem ficar em branco!");
        }

        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getLogin().getEmail().equals(email))
                .filter(usuario -> usuario.getLogin().getSenha().getChave().equals(senha))
                .filter(usuario -> usuario.getLogin().isAtivo())
                .findFirst()
                .map(usuario -> {
                    usuario.getMessagesSystem().size();  // Carrega mensagens
                    usuario.getPublicacoes().size();      // Carrega publicações
                    return usuario;
                })
                .orElse(null);
    }

    /**
     * Salva um usuário existente no repositório.
     *
     * @param usuario O usuário a ser salvo.
     */
    public void salvarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    /**
     * Edita os dados de um usuário existente.
     *
     * @param codigoUsuario O código do usuário a ser editado.
     * @param record        O novo registro do usuário.
     * @return O usuário editado ou null se o usuário não existir ou o registro for nulo.
     */
    public Usuario editarUsuario(String codigoUsuario, UsuarioRecord record) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCodigo(codigoUsuario);
        if (usuarioOptional.isPresent() && record != null) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNome(record.nome());
            usuario.setSobreNome(record.sobreNome());

            Login login = usuario.getLogin();
            login.setNomeUsuario(record.nomeUsuario());

            Senha senha = login.getSenha();
            senha.setChave(SenhaUtil.criar(record.senha()).getChave());
            login.setSenha(senha);

            usuarioRepository.save(usuario);
            return usuario;
        }
        return null;
    }

    /**
     * Ativa um usuário pelo seu código.
     *
     * @param codigo O código do usuário a ser ativado.
     * @throws IllegalArgumentException Se o código não pertencer a nenhum usuário.
     */
    public void ativarUsuario(String codigo) {
        Usuario usuarioBanco = usuarioRepository.findByCodigo(codigo).orElse(null);
        if (usuarioBanco == null) {
            throw new IllegalArgumentException("Verifique o código, ele não pertence a nenhum usuário!");
        }
        usuarioBanco.getLogin().setAtivo(true);
        usuarioRepository.save(usuarioBanco);
    }

    /**
     * Verifica se um nome de usuário já existe no repositório.
     *
     * @param nomeUsuario O nome de usuário a ser verificado.
     * @return true se o usuário existir, false caso contrário.
     */
    public boolean usuarioExiste(String nomeUsuario) {
        return loginRepository.existsByNomeUsuario(nomeUsuario);
    }
}
