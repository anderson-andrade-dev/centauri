package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.entity.Login;
import br.dev.andersonandrade.centauri.entity.Senha;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.exceptions.EmailException;
import br.dev.andersonandrade.centauri.exceptions.UsuarioNaoEncontradoException;
import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.record.UsuarioRecord;
import br.dev.andersonandrade.centauri.repository.LoginRepository;
import br.dev.andersonandrade.centauri.repository.UsuarioRepository;
import br.dev.andersonandrade.centauri.service.EmailService;
import br.dev.andersonandrade.centauri.service.RemetenteDestinatarioService;
import br.dev.andersonandrade.centauri.service.SenhaService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
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
    private final SenhaService senhaService;
    private final RemetenteDestinatarioService remetenteDestinatarioService;
    private final LoginRepository loginRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, EmailService emailService,
                          SenhaService senhaService,
                          RemetenteDestinatarioService remetenteDestinatarioService, LoginRepository loginRepository) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.senhaService = senhaService;
        this.remetenteDestinatarioService = remetenteDestinatarioService;
        this.loginRepository = loginRepository;
    }

    /**
     * Salva um novo usuário no repositório e envia um e-mail de ativação.
     *
     * @param usuarioRecord O registro do usuário a ser salvo.
     * @return O usuário salvo ou null se o registro for nulo.
     */
    public Optional<Usuario> salva(UsuarioRecord usuarioRecord) {
        validaUsuarioRecord(usuarioRecord);

        Usuario usuario = new Usuario(usuarioRecord,
                new Senha(senhaService.criptografar(usuarioRecord.senha())));
        usuarioRepository.save(usuario);
        enviarEmailDeAtivacao(usuario);
        return Optional.of(usuario);
    }

    public Optional<Usuario> salva(@NotNull Usuario usuario){
        Objects.requireNonNull(usuario,"Usuario não pode ser nulo!");
        usuarioRepository.save(usuario);
        return Optional.of(usuario);
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
    public Usuario buscarPorEmailESenha(@NotNull String senha, @NotNull String email) {
        validaString(senha, "senha");
        validaString(email,"email");

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
     * Edita os dados de um usuário existente.
     *
     * @param codigoUsuario O código do usuário a ser editado.
     * @param record        O novo registro do usuário.
     * @return O usuário editado ou null se o usuário não existir devolve um Optional.
     * @throws NullPointerException Se o objeto record for nulo ou o codigoUsuario for nulo.
     * @throws IllegalArgumentException Se qualquer um dos campos obrigatórios
     * (nome, sobrenome, nome de usuário senha, ou codigo usuário) estiver em branco.
     */
    public Optional<Usuario> editarUsuario(String codigoUsuario, @NotNull UsuarioRecord record) {
        validaString(codigoUsuario,"codigoUsuario");
        validaUsuarioRecord(record);
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCodigo(codigoUsuario);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNome(record.nome());
            usuario.setSobreNome(record.sobreNome());

            Login login = usuario.getLogin();
            login.setNomeUsuario(record.nomeUsuario());

            Senha senha = login.getSenha();
            senha.setChave(senhaService.criptografar(record.senha()));
            login.setSenha(senha);

            usuarioRepository.save(usuario);
        }
        return usuarioOptional;
    }

    /**
     * Ativa um usuário pelo seu código.
     *
     * @param codigo O código do usuário a ser ativado.
     * @throws NullPointerException Se o código estiver nulo.
     * @throws IllegalArgumentException Se o código etiver vazio ou em branco.
     * @throws UsuarioNaoEncontradoException Se o código não pertencer a nenhum usuário.
     */
    public void ativarUsuario(@NotNull String codigo) {
        validaString(codigo,"código");

        Usuario usuarioBanco = usuarioRepository
                .findByCodigo(codigo)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Verifique o código, ele não pertence a nenhum usuário!"));

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

    /**
     * Associa um remetente a um destinatário no sistema.
     * <p>
     * Este método verifica se já existe uma associação entre o remetente e destinatário.
     * Caso a associação não exista, ela é criada e salva no banco de dados. Se a associação
     * já existir, mas o destinatário ainda não estiver associado ao remetente, ele será adicionado
     * à lista de destinatários associados e a entidade será atualizada.
     *
     * @param remetente    O remetente que deseja associar um destinatário. Não pode ser nulo.
     * @param destinatario O destinatário a ser associado ao remetente. Não pode ser nulo.
     * @throws UsuarioNaoEncontradoException Se o remetente ou o destinatário não forem encontrados no banco de dados.
     */
    public void associarRemetenteDestinatario(@NotNull Remetente remetente, @NotNull Destinatario destinatario) {

        Usuario usuarioRemetente = usuarioRepository.findByLogin_Email(remetente.endereco())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Remetente não encontrado: " + remetente.endereco()));

        Usuario usuarioDestinatario = usuarioRepository.findByLogin_Email(destinatario.endereco())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Destinatário não encontrado: " + destinatario.endereco()));

        remetenteDestinatarioService.associar(remetente, destinatario);
    }

    /**
     * Valida uma string, garantindo que ela não seja nula e que não esteja em branco.
     *
     * @param string        A string a ser validada.
     * @param nomeParametro O nome do parâmetro, usado na mensagem de erro.
     * @throws NullPointerException Se a string for nula.
     * @throws IllegalArgumentException Se a string estiver em branco ou vazia.
     */
    private static void validaString(String string, String nomeParametro) {
        Objects.requireNonNull(string,"Verifique a String do parâmetro: "+nomeParametro+", ela não pode ser nula!");

        if (string.isBlank()) {
            throw new IllegalArgumentException("Verifique a String do parâmetro: "+nomeParametro+", ela não pode ser em branco ou vazia!");
        }

    }

    /**
     * Valida um objeto UsuarioRecord, garantindo que ele não seja nulo
     * e que todos os seus campos obrigatórios estejam preenchidos corretamente.
     *
     * @param record O objeto UsuarioRecord a ser validado.
     * @throws NullPointerException Se o objeto record for nulo.
     * @throws IllegalArgumentException Se qualquer um dos campos obrigatórios (nome, sobrenome, nome de usuário ou senha) estiver em branco.
     */
    private static void validaUsuarioRecord(UsuarioRecord record) {
        Objects.requireNonNull(record,"O parâmetro record não pode ser nulo!");
        validaString(record.nomeUsuario(),"nomeUsuario");
        validaString(record.sobreNome(),"sobreNome");
        validaString(record.nome(),"nome");
        validaString(record.senha(),"Senha");
    }


}
