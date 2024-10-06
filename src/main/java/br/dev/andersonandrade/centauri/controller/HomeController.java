package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.model.MensagemModel;
import br.dev.andersonandrade.centauri.model.PublicacaoModel;
import br.dev.andersonandrade.centauri.service.EmailService;
import br.dev.andersonandrade.centauri.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping(path = "/")
public class HomeController {

    private final PublicacaoModel publicacaoModel;
    private final MensagemModel mensagemModel;
    private final UsuarioService usuarioService;


    @Autowired
    public HomeController(UsuarioService usuarioService, MensagemModel mensagemModel,
                          PublicacaoModel publicacaoModel, EmailService email) {
        this.usuarioService = usuarioService;
        this.mensagemModel = mensagemModel;
        this.publicacaoModel = publicacaoModel;
    }

    @GetMapping
    public String paginaInicial(Model model) {
        List<Publicacao> publicacoesBanco = publicacaoModel.listaRank();

        model.addAttribute("pageTitle", "Blog");
        model.addAttribute("texto", "página principal");
        model.addAttribute("listaPublicacao", publicacoesBanco);
        return "index";

    }

    @GetMapping("login")
    public String formLogin() {
        return "login";
    }

    @GetMapping("cadastro")
    public String formCadastro() {
        return "cadastro";
    }
}