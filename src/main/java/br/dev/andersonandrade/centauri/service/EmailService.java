package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * Classe EmailService
 * <p>
 * Responsável pelo envio de emails de ativação para usuários.
 * Esta classe utiliza o JavaMailSender para construir e enviar
 * mensagens de email formatadas com o Thymeleaf.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 * @contact andersonandradedev@outlook.com
 */
@Service
public class EmailService {

    private static final String NOME_TEMPLATE = "email"; // Nome do template de email
    private static final String IMAGEM_LOGO = "static/images/logo.jpg"; // Caminho da imagem do logo
    private static final String PNG_MIME = "image/png"; // Tipo MIME para imagem PNG
    private static final String MAIL_SUBJECT = "Seja bem vindo(a)"; // Assunto do email

    private final Environment environment; // Injeção de dependência para acessar propriedades do ambiente
    private final JavaMailSender mailSender; // Injeção de dependência para enviar emails
    private final TemplateEngine htmlTemplateEngine; // Injeção de dependência para processar templates Thymeleaf
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class); // Logger para registrar eventos

    /**
     * Construtor da classe EmailService.
     *
     * @param environment        O ambiente que fornece as propriedades de configuração.
     * @param mailSender         O componente para enviar emails.
     * @param htmlTemplateEngine O motor de template Thymeleaf.
     */
    public EmailService(Environment environment, JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.environment = environment;
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    /**
     * Envia um email de ativação para o usuário especificado.
     * <p>
     * Este método configura e envia um email formatado com informações
     * do usuário, utilizando um template Thymeleaf. O email contém um link
     * de ativação e é enviado com uma imagem de logo em linha.
     *
     * @param usuario O usuário para o qual o email de ativação será enviado.
     * @throws MessagingException           Se ocorrer um erro ao enviar o email.
     * @throws UnsupportedEncodingException Se a codificação do remetente não for suportada.
     */
    public void enviarEmailAtivacao(Usuario usuario) throws MessagingException, UnsupportedEncodingException {
        // Validação dos dados do usuário
        validarUsuario(usuario);

        // Recupera o endereço de email do remetente a partir das propriedades do ambiente
        String enderecoEmail = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String nomeDoRemetente = environment.getProperty("mail.from.name", "Identity");

        // Cria uma nova mensagem MIME
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Configura o destinatário, assunto e remetente
        email.setTo(usuario.getLogin().getEmail());
        email.setSubject(MAIL_SUBJECT);
        email.setFrom(new InternetAddress(enderecoEmail, nomeDoRemetente));

        // Cria o contexto para o template de email
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", usuario.getLogin().getEmail());
        ctx.setVariable("nome", usuario.getNome());
        ctx.setVariable("codigo", usuario.getCodigo());
        ctx.setVariable("imagemLogo", IMAGEM_LOGO);

        // Processa o template Thymeleaf para gerar o conteúdo HTML do email
        String htmlContent = this.htmlTemplateEngine.process(NOME_TEMPLATE, ctx);

        // Define o conteúdo do email
        email.setText(htmlContent, true);

        // Adiciona a imagem do logo como um recurso em linha
        ClassPathResource clr = new ClassPathResource(IMAGEM_LOGO);
        email.addInline("imagemLogo", clr, PNG_MIME);

        // Envia o email
        mailSender.send(mimeMessage);
        logger.info("Email de ativação enviado para: {}", usuario.getLogin().getEmail());
    }

    /**
     * Valida o formato de um e-mail.
     *
     * @param email O e-mail a ser validado.
     * @throws EmailException Se o formato do e-mail for inválido.
     */
    public void validaEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(emailRegex)) {
            throw new EmailException("Formato de e-mail inválido: " + email);
        }
    }

    /**
     * Valida os dados do usuário para garantir que não estejam nulos ou vazios.
     *
     * @param usuario O usuário a ser validado.
     * @throws MessagingException se os dados do usuário não forem válidos.
     */
    private void validarUsuario(Usuario usuario) throws MessagingException {
        if (usuario == null || usuario.getLogin() == null) {
            throw new MessagingException("Usuário ou informações de login não podem ser nulos.");
        }
        if (Objects.isNull(usuario.getLogin().getEmail()) || usuario.getLogin().getEmail().isEmpty()) {
            throw new MessagingException("Email do usuário não pode ser nulo ou vazio.");
        }
        if (Objects.isNull(usuario.getNome()) || usuario.getNome().isEmpty()) {
            throw new MessagingException("Nome do usuário não pode ser nulo ou vazio.");
        }
        if (Objects.isNull(usuario.getCodigo()) || usuario.getCodigo().isEmpty()) {
            throw new MessagingException("Código de ativação não pode ser nulo ou vazio.");
        }
    }
}
