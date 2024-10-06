package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.beans.LikePublicacao;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.model.MensagemModel;
import br.dev.andersonandrade.centauri.model.PublicacaoModel;
import br.dev.andersonandrade.centauri.service.UsuarioService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class LoginController {


    private final MensagemModel mensagemModel;
    private final PublicacaoModel publicacaoModel;
    private final UsuarioService usuarioService;


    @Autowired
    public LoginController(MensagemModel mensagemModel,
                           UsuarioService usuarioService,
                           PublicacaoModel publicacaoModel) {
        this.mensagemModel = mensagemModel;
        this.publicacaoModel = publicacaoModel;
        this.usuarioService = usuarioService;
    }

    @PostMapping("minha-pagina")
    public String paginaUsuario(Authentication authentication, Model model) {

        if (authentication != null) {
            informacaoUsuario(model, authentication.getName(), usuarioService,
                    mensagemModel, publicacaoModel);
            return "usuario";
        }
        return "redirect:/";
    }

    @NotNull
    static void informacaoUsuario(Model model, String userName,
                                  UsuarioService usuarioService, MensagemModel mensagemModel,
                                  PublicacaoModel publicacaoModel) {
        Optional<Usuario> usuario = usuarioService.buscaPorEmail(userName);
        if (usuario.isPresent()) {
            LikePublicacao likePublicacao = publicacaoModel.rankComLike(usuario.get());
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("caixaDeMensagem", mensagemModel.criaCaixaMensagem(usuario.get()));
            model.addAttribute("publicacaoLikes", likePublicacao.getLikesPublicacoes());
        }
    }
}
