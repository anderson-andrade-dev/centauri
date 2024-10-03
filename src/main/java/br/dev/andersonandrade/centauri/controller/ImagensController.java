package br.dev.andersonandrade.centauri.controller;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.model.ImagemModel;
import br.dev.andersonandrade.centauri.model.PublicacaoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "imagens", method = RequestMethod.GET)
public class ImagensController {
    private final ImagemModel imagemModel;
    private final PublicacaoModel publicacaoModel;

    @Autowired
    public ImagensController(ImagemModel imagemModel, PublicacaoModel publicacaoModel) {
        this.imagemModel = imagemModel;
        this.publicacaoModel = publicacaoModel;
    }

    @ResponseBody
    @GetMapping("publicacao/{id}")
    public ResponseEntity<?> publicacaoImagem(@PathVariable Long id) {
        Publicacao publicacao = publicacaoModel.buscaId(id);
        return imagemModel.imagemPublicacao(publicacao);
    }


}
