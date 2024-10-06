package br.dev.andersonandrade.centauri.model;


import br.dev.andersonandrade.centauri.beans.LikePublicacao;
import br.dev.andersonandrade.centauri.entity.Likes;
import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.repository.PublicacaoRepository;
import br.dev.andersonandrade.centauri.service.UsuarioService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class PublicacaoModel {

    private final PublicacaoRepository publicacaoRepository;
    private final UsuarioService usuarioService;
    private final ImagemModel imagemModel;
    private final LikeModel likeModel;


    @Autowired
    public PublicacaoModel(PublicacaoRepository publicacaoRepository, UsuarioService usuarioService,
                           ImagemModel imagemModel, LikeModel likeModel) {
        this.publicacaoRepository = publicacaoRepository;
        this.usuarioService = usuarioService;
        this.imagemModel = imagemModel;
        this.likeModel = likeModel;
    }

    @Transactional
    public void salvarPublicacao(Usuario usuario, String texto, MultipartFile imagem) {
        String urlImagem = imagemModel.upload(imagem);
        Optional<Usuario> usuarioBanco = usuarioService.buscarPorCodigo(usuario.getCodigo());

        if (usuarioBanco.isPresent()) {
            LocalDateTime dataPublicacao = LocalDateTime.now();
            Publicacao publicacao = new Publicacao(usuarioBanco.get(), urlImagem, texto, dataPublicacao, true);
            Likes like = new Likes(publicacao);

            likeModel.salvar(like);

            publicacaoRepository.save(publicacao);


        } else {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
    }

    public List<Publicacao> buscarPorData(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        Stream<Publicacao> stream = publicacaoRepository.findAll()
                .stream()
                .filter(publicacao -> publicacao.getDataPublicacao().isEqual(dataFinal))
                .filter(publicacao -> publicacao.getDataPublicacao().isEqual(dataInicial))
                .filter(publicacao -> publicacao.getDataPublicacao().isAfter(dataInicial))
                .filter(publicacao -> publicacao.getDataPublicacao().isBefore(dataFinal))
                .filter(Publicacao::isAtiva);
        return stream.toList();
    }

    public List<Publicacao> listaTodas() {
        List<Publicacao> publicacoes = publicacaoRepository.publicacaoesOrdDataDescr();
        return publicacoes.isEmpty() ? List.of() : publicacoes;

    }

    public List<Publicacao> listaUsuario(Usuario usuario) {
        LikePublicacao likePublicacoes = new LikePublicacao(likeModel.buscaPorUsuario(usuario));
        return likePublicacoes != null ? likePublicacoes.getPublicacoes() : List.of();
    }

    public Publicacao buscaId(Long id) {
        return publicacaoRepository.findById(id).orElse(null);
    }


    @Transactional
    public void editar(Long idPublicacao, String texto, MultipartFile imagem) {
        Publicacao publicacaoBanco = publicacaoRepository.findById(idPublicacao).orElse(null);
        try {
            publicacaoBanco.setAtiva(false);
            salvarPublicacao(publicacaoBanco.getUsuario(), texto, imagem);
        } catch (NullPointerException e) {
            throw new RuntimeException("Erro na busca do banco! " + e);
        }
    }


    @Transactional
    public void desativa(Long idPublicacao) {
        publicacaoRepository.findById(idPublicacao)
                .ifPresent(publicacao -> {
                    publicacao.setAtiva(false);
                    publicacaoRepository.save(publicacao);
                });
    }


    public List<Publicacao> listaRank() {
        LikePublicacao likesPublicacoes = new LikePublicacao(likeModel.listaRank());
        return likesPublicacoes.getPublicacoes();
    }

    public LikePublicacao rankComLike() {
        return new LikePublicacao(likeModel.listaRank());
    }

    public LikePublicacao rankComLike(Usuario usuario) {
        return new LikePublicacao(likeModel.buscaPorUsuario(usuario));
    }

    public void adicionarLike(@NotNull Long idPublicacao) {
        likeModel.adicionarLike(idPublicacao);
    }

    public void dislike(@NotNull Long idPublicacao) {
        likeModel.dislike(idPublicacao);
    }

}
