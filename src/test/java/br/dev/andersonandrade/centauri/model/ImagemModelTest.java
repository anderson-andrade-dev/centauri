package br.dev.andersonandrade.centauri.beans.model;

import br.dev.andersonandrade.centauri.entity.Publicacao;
import br.dev.andersonandrade.centauri.entity.Senha;
import br.dev.andersonandrade.centauri.entity.Usuario;
import br.dev.andersonandrade.centauri.model.ImagemModel;
import br.dev.andersonandrade.centauri.record.UsuarioRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Classe de teste para a classe {@link ImagemModel}.
 * Testa os métodos de upload e recuperação de imagens associadas a publicações.
 * <p>
 * Autor: Anderson Andrade Dev
 * Data: 29 de setembro de 2024
 */
public class ImagemModelTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ImagemModel imagemModel;

    private UsuarioRecord record;
    private Usuario usuario;

    /**
     * Método de configuração inicial para os mocks utilizados nos testes.
     * Inicializa a instância de {@link ImagemModel} com o mock do {@link ResourceLoader}.
     * <p>
     * Autor: Anderson Andrade Dev
     * Data: 29 de setembro de 2024
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagemModel = new ImagemModel(resourceLoader);
        record = new UsuarioRecord("Anderson", "Andrade", "anderson",
                "1234", "andersonandradedev@outlook.com");
        usuario = new Usuario(record, new Senha(new BCryptPasswordEncoder().encode(record.senha())));
    }

    /**
     * Testa o upload de uma imagem com sucesso.
     * Verifica se a imagem é armazenada corretamente no caminho esperado.
     * <p>
     * Autor: Anderson Andrade Dev
     * Data: 29 de setembro de 2024
     *
     * @throws IOException Se houver erro ao escrever o arquivo.
     */
    @Test
    public void testUploadImagemComSucesso() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("imagem.jpg");
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

        String path = imagemModel.upload(multipartFile);

        assertNotNull(path);
        assertTrue(path.contains("centauri-imagens"));
    }

    /**
     * Testa o upload de uma imagem com extensão inválida.
     * Verifica se uma exceção é lançada ao tentar carregar um arquivo com formato não suportado.
     * <p>
     * Autor: Anderson Andrade Dev
     * Data: 29 de setembro de 2024
     */
    @Test
    public void testUploadImagemComExtensaoInvalida() {
        when(multipartFile.getOriginalFilename()).thenReturn("imagem.bmp");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagemModel.upload(multipartFile);
        });

        assertEquals("Formato de arquivo não suportado: bmp", exception.getMessage());
    }

    /**
     * Testa a recuperação de uma imagem associada a uma publicação existente.
     * Verifica se a imagem é retornada corretamente quando o caminho é válido.
     * <p>
     * Autor: Anderson Andrade Dev
     * Data: 29 de setembro de 2024
     *
     * @throws IOException Se houver erro ao carregar o arquivo.
     */
    @Test
    public void testImagemPublicacaoExistente() throws IOException {
        Publicacao publicacao = new Publicacao(usuario, "path/to/image.jpg", "Texto de teste", LocalDateTime.now(), true);

        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(true);
        when(resource.getFile()).thenReturn(new File("path/to/image.jpg")); // Caminho real ou mock
        when(resource.contentLength()).thenReturn(100L);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("Imagem".getBytes())); // Retorna um InputStream válido

        when(resourceLoader.getResource("file://path/to/image.jpg")).thenReturn(resource);

        ResponseEntity<?> response = imagemModel.imagemPublicacao(publicacao);
        assertEquals(200, response.getStatusCode().value());
    }


    /**
     * Testa a recuperação de uma imagem associada a uma publicação inexistente.
     * Verifica se uma mensagem de erro é retornada quando a imagem não é encontrada.
     * <p>
     * Autor: Anderson Andrade Dev
     * Data: 29 de setembro de 2024
     */
    @Test
    public void testImagemPublicacaoNaoExistente() {
        Publicacao publicacao = new Publicacao(usuario, "path/to/nonexistent.jpg",
                "Texto de teste", LocalDateTime.now(), true);

        when(resourceLoader.getResource("file://path/to/nonexistent.jpg"))
                .thenReturn(mock(Resource.class));

        ResponseEntity<?> response = imagemModel.imagemPublicacao(publicacao);
        assertEquals(400, response.getStatusCode().value());
    }
}
