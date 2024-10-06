package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.exceptions.UsuarioNaoEncontradoException;
import br.dev.andersonandrade.centauri.model.MensagemModel;
import br.dev.andersonandrade.centauri.model.PublicacaoModel;
import br.dev.andersonandrade.centauri.record.DestinatarioRecord;
import br.dev.andersonandrade.centauri.record.UsuarioRecord;
import br.dev.andersonandrade.centauri.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "cadastro")
public class CadastroController {
    private final MensagemModel mensagemModel;
    private final PublicacaoModel publicacaoModel;
    private final UsuarioService usuarioService;


    @Autowired
    public CadastroController(MensagemModel mensagemModel,
                              PublicacaoModel publicacaoModel, UsuarioService usuarioService) {
        this.mensagemModel = mensagemModel;
        this.publicacaoModel = publicacaoModel;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public String cadastro(UsuarioRecord usuario, Model model) {
        try {
            Usuario usuarioBanco = usuarioService.salva(usuario)
                    .orElseThrow(()-> new UsuarioNaoEncontradoException("Erro ao Cadastro Usuario!"));
            model.addAttribute("mensagem", "Entre no email para ativar o seu cadastro!");
            return "login";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("erroCadastro", true);
            model.addAttribute("mensagem", "Email já cadastrado no sistema!");
            return "cadastro";
        } catch (NullPointerException e) {
            model.addAttribute("erroCadastro", true);
            model.addAttribute("mensagem", "e");
            return "cadastro";
        }catch (UsuarioNaoEncontradoException e){
            model.addAttribute("erroCadastro", true);
            model.addAttribute("mensagem", "e");
            return "cadastro";
        }

    }


    @PostMapping("alteraUsuario")
    public String alterarDadosUsuario(
            @RequestParam("codigo") String codigoUsuario,
            @RequestParam("nome") String nome,
            @RequestParam("sobreNome") String sobreNome,
            @RequestParam("nomeUsuario") String nomeUsuario,
            @RequestParam("email") String email,
            @RequestParam(value = "senha", required = false) String senha,
            RedirectAttributes attributes) {

        UsuarioRecord record = new UsuarioRecord(nome, sobreNome, nomeUsuario, senha, email);
        Optional<Usuario> atualizarUsuario = usuarioService.editarUsuario(codigoUsuario, record);

        if (atualizarUsuario.isEmpty()) {
            attributes.addFlashAttribute("mensagem", "Erro ao atualizar usuário.");
        }
        return "redirect:/";
    }

    @GetMapping("ativar")
    public String ativarUsuario(@RequestParam("codigo") String codigo, RedirectAttributes attributes) {
        try {
            usuarioService.ativarUsuario(codigo);
            attributes.addAttribute("codigo", codigo);
            return "redirect:/minha-pagina";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }

    }

    @GetMapping("verificarNomeUsuario")
    @ResponseBody
    public int verificarNomeUsuario(@RequestParam("nomeUsuario") String nomeUsuario) {
        System.out.println("Verificando nome de usuário: " + nomeUsuario);
        boolean existe = usuarioService.usuarioExiste(nomeUsuario);
        System.out.println("Nome de usuário existe: " + existe);
        return existe ? 1 : 0;
    }

    @PostMapping("destinatario/busca")
    @ResponseBody
    public List<DestinatarioRecord> buscaDestinatario(@RequestParam("email")String email) {
        Objects.requireNonNull(email,"Verifique o parametro ele não pode ser nulo!");
        if(email.isEmpty() || email.isBlank()){
            throw new IllegalArgumentException("A String do parametro não pode ser vazia ou estar em branco!");
        }
        Optional<Usuario> usuario = usuarioService.buscaPorEmail(email);
        if (!usuario.isPresent()) {
            return List.of();
        }
        DestinatarioRecord destinatario = new DestinatarioRecord(usuario.get().getNome(), usuario.get().getLogin().getEmail());
        return List.of(destinatario);
    }

    @GetMapping("destinatario")
    public String formCadastroRemetente(Authentication authentication,DestinatarioRecord destinatarioRecord,Model model){

        return "cadastro-destinatario";

    }
}
