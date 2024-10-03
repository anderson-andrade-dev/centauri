package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.model.MensagemModel;
import br.dev.andersonandrade.centauri.model.PublicacaoModel;
import br.dev.andersonandrade.centauri.model.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static br.dev.andersonandrade.centauri.controller.LoginController.informacaoUsuario;


@Controller
public class PublicacaoController {

    private final PublicacaoModel publicacaoModel;
    private final UsuarioService usuarioService;
    private final MensagemModel mensagemModel;

    @Autowired
    public PublicacaoController(PublicacaoModel publicacaoModel,
                                UsuarioService usuarioService, MensagemModel mensagemModel) {
        this.publicacaoModel = publicacaoModel;
        this.usuarioService = usuarioService;
        this.mensagemModel = mensagemModel;
    }

    @PostMapping("publicacao")
    public String formPublicacao(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/";
        }
        informacaoUsuario(model, authentication.getName(), usuarioService, mensagemModel, publicacaoModel);
        return "cria-publicacao";
    }

    @PostMapping("postar")
    public String postar(@RequestParam("texto") String texto,
                         @RequestParam("imagem") MultipartFile imagem,
                         Authentication authentication, Model model) {

        if (authentication != null) {
            Usuario usuarioBanco = usuarioService.buscaPorEmail(authentication.getName());
            publicacaoModel.salvarPublicacao(usuarioBanco, texto, imagem);
            return "forward:minha-pagina";
        }
        return "redirect:/";
    }

    @PostMapping("edita")
    public String formEditar(Long id, Authentication authentication, Model model) {
        if (authentication != null) {
            Publicacao publicacao = publicacaoModel.buscaId(id);
            Usuario usuarioBanco = usuarioService.buscaPorEmail(authentication.getName());
            model.addAttribute("usuario", usuarioBanco);
            model.addAttribute("caixaDeMensagem", mensagemModel.criaCaixaMensagem(usuarioBanco));
            model.addAttribute("publicacao", publicacao);
            return "edita-publicacao";
        }
        return "redirect:/";
    }

    @PostMapping("alteraPublicacao")
    public String alteraPublicacao(@RequestParam("idPublicacao") Long idPublicacao,
                                   @RequestParam("texto") String texto,
                                   @RequestParam("imagem") MultipartFile imagem,
                                   Model model) {

        publicacaoModel.editar(idPublicacao, texto, imagem);

        return "forward:minha-pagina";
    }

    @PostMapping("excluir")
    public String excluirPublicacao(Long idPublicacao, Model model) {
        publicacaoModel.desativa(idPublicacao);
        return "forward:minha-pagina";
    }

    @PostMapping(path = "/like/{id}")
    @ResponseBody
    public ResponseEntity<?> like(@PathVariable Long id) {
        publicacaoModel.adicionarLike(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/dislike/{id}")
    @ResponseBody
    public ResponseEntity<?> dislike(@PathVariable Long id) {
        publicacaoModel.dislike(id);
        return ResponseEntity.ok().build();
    }
}
