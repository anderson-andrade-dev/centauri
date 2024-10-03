package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.service.AdminstracaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administracao")
public class AdministracaoController {

    private final AdminstracaoService adminstracaoService;

    public AdministracaoController(AdminstracaoService adminstracaoService) {
        this.adminstracaoService = adminstracaoService;
    }

    @GetMapping
    public String administracao(Model model) {
        Long totalUsuarios = adminstracaoService.getUsuariosCadastrados();
        Long totalAtivos = adminstracaoService.getUsuariosAtivos();
        Long totalInativos = adminstracaoService.getUsuariosInativos();
        Long totalPublicacoes = adminstracaoService.getPublicacaoUsuarios();
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalAtivos", totalAtivos);
        model.addAttribute("totalInativos", totalInativos);
        model.addAttribute("totalPublicacoes", totalPublicacoes);
        return "administracao";
    }

}
