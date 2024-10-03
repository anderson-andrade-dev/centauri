package br.dev.andersonandrade.centauri.interfaces;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 29/09/2024
 */

public interface ImagemService {

    /**
     * Faz o upload de uma imagem e retorna o caminho onde ela foi salva.
     *
     * @param imagem Arquivo de imagem a ser enviado.
     * @return O caminho completo onde a imagem foi salva.
     */
    String upload(MultipartFile imagem);

    /**
     * Retorna a imagem associada a uma publicação.
     *
     * @param publicacao Publicacao que contém a URL da imagem.
     * @return ResponseEntity com a imagem ou uma mensagem de erro.
     */
    ResponseEntity<?> imagemPublicacao(Publicacao publicacao);
}
