package br.dev.andersonandrade.centauri.model;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.interfaces.ImagemService;
import br.dev.andersonandrade.centauri.uteis.CodigoUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável por gerenciar o upload e o armazenamento de imagens associadas a publicações.
 * Também fornece funcionalidades para carregar e retornar imagens armazenadas no servidor.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 */
@Component
public class ImagemModel implements ImagemService {

    private static final Logger logger = LoggerFactory.getLogger(ImagemModel.class);
    private static final String[] SUPPORTED_EXTENSIONS = {"jpg", "jpeg", "png"};

    private final ResourceLoader resourceLoader;
    private final Path local;

    @Autowired
    public ImagemModel(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.local = getDefaultPath();
        criarPastas();
    }

    /**
     * Define o caminho padrão para armazenamento de imagens, com suporte para múltiplos sistemas operacionais.
     *
     * @return O caminho onde as imagens serão armazenadas.
     */
    public static Path getDefaultPath() {
        Path pathDefault = Paths.get("/centauri-imagens/publicacao/");
        if (Files.notExists(pathDefault, LinkOption.NOFOLLOW_LINKS)) {
            String userHome = System.getProperty("user.home");
            pathDefault = Paths.get(userHome, "/centauri-imagens/publicacao/");
        }
        return pathDefault;
    }

    /**
     * Cria as pastas necessárias para o armazenamento das imagens.
     * Caso a criação falhe, uma exceção é lançada.
     */
    private void criarPastas() {
        try {
            Files.createDirectories(this.local);
            logger.debug("Pasta default: {}", this.local.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta para salvar arquivos.", e);
        }
    }

    /**
     * Faz o upload de uma imagem, verificando a extensão e o espaço disponível.
     *
     * @param imagem Arquivo de imagem a ser enviado.
     * @return O caminho completo onde o arquivo foi salvo.
     */
    public String upload(MultipartFile imagem) {
        final String codigo = CodigoUtil.gerarCodigo();
        String extensao = FilenameUtils.getExtension(imagem.getOriginalFilename()).toLowerCase();

        if (!isSupportedExtension(extensao)) {
            throw new RuntimeException("Formato de arquivo não suportado: " + extensao);
        }

        if (!hasSufficientSpace(imagem.getSize())) {
            throw new RuntimeException("Espaço em disco insuficiente.");
        }

        String caminhoData = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path diretorioPorData = local.resolve(caminhoData);
        criarPastasDiretorio(diretorioPorData);

        try (FileOutputStream output = new FileOutputStream(diretorioPorData + "/" + codigo + "." + extensao)) {
            output.write(imagem.getBytes());
            return diretorioPorData.toString().concat("/" + codigo + ".").concat(extensao);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagem " + e);
        }
    }

    /**
     * Retorna a imagem associada a uma publicação, com tratamento de erros.
     *
     * @param publicacao Publicacao que contém a URL da imagem.
     * @return ResponseEntity com a imagem ou uma mensagem de erro.
     */
    public ResponseEntity<?> imagemPublicacao(Publicacao publicacao) {
        if (publicacao == null) {
            return ResponseEntity.badRequest().body("Publicacao não encontrada!.");
        }

        String imagePath = "file://" + publicacao.getUrlImagem();

        try {
            Resource file = resourceLoader.getResource(imagePath);
            if (!file.exists()) {
                return ResponseEntity.badRequest().body("Não foi possível encontrar a imagem.");
            }

            String contentType = Files.probeContentType(file.getFile().toPath());
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentLength(file.contentLength())
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new InputStreamResource(file.getInputStream()));

        } catch (Exception e) {
            logger.error("Erro ao carregar a imagem", e);
            return ResponseEntity.badRequest().body("Não foi possível encontrar a imagem.");
        }
    }

    /**
     * Cria subdiretórios dinamicamente, caso ainda não existam.
     *
     * @param diretorio Diretório que deve ser criado.
     */
    private void criarPastasDiretorio(Path diretorio) {
        try {
            Files.createDirectories(diretorio);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar subdiretórios para armazenamento de imagem.", e);
        }
    }

    /**
     * Verifica se a extensão do arquivo de imagem é suportada.
     *
     * @param extensao Extensão do arquivo.
     * @return true se a extensão é suportada, false caso contrário.
     */
    private boolean isSupportedExtension(String extensao) {
        for (String ext : SUPPORTED_EXTENSIONS) {
            if (extensao.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se há espaço suficiente no disco para o upload da imagem.
     *
     * @param fileSize Tamanho do arquivo em bytes.
     * @return true se há espaço suficiente, false caso contrário.
     */
    private boolean hasSufficientSpace(long fileSize) {
        try {
            long usableSpace = Files.getFileStore(local).getUsableSpace();
            return usableSpace >= fileSize;
        } catch (IOException e) {
            logger.error("Erro ao verificar espaço em disco", e);
            return false;
        }
    }
}
