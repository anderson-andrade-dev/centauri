package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.beans.LikePublicacao;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.model.MensagemModel;
import br.dev.andersonandrade.centauri.model.PublicacaoModel;
import br.dev.andersonandrade.centauri.model.UsuarioService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


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
        Usuario usuario = usuarioService.buscaPorEmail(userName);
        LikePublicacao likePublicacao = publicacaoModel.rankComLike(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("caixaDeMensagem", mensagemModel.criaCaixaMensagem(usuario));
        model.addAttribute("publicacaoLikes", likePublicacao.getLikesPublicacoes());
    }

}
