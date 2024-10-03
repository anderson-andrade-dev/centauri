package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.beans.MensagemBean;
import br.dev.andersonandrade.centauri.entity.TransportadorEntity;
import br.dev.andersonandrade.centauri.interfaces.*;
import br.dev.andersonandrade.centauri.repository.TransportadorRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe ArmazemMensagens
 * <p>
 * Implementação da interface Armazem, responsável por armazenar e recuperar mensagens
 * entre remetentes e destinatários. Utiliza um repositório para gerenciar a persistência
 * das mensagens e mantém um log das operações realizadas.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 * @contact andersonandradedev@outlook.com
 */
@Component
@Transactional
public class ArmazemMensagens implements Armazem {

    private final TransportadorRepository transportadorRepository;
    private Set<TransportadorEntity> transportadores;
    private final Logger logger;
    private static int contadorInstancia = 0;

    /**
     * Construtor que inicializa a classe ArmazemMensagens.
     *
     * @param transportadorRepository O repositório para gerenciar a persistência das mensagens.
     */
    @Autowired
    public ArmazemMensagens(TransportadorRepository transportadorRepository) {
        this.transportadorRepository = transportadorRepository;
        atualizarMensagens();
        this.logger = LoggerFactory.getLogger(ArmazemMensagens.class);
        contadorInstancia++;
        logger.info("------------Quantidade de Armazem na Aplicação {}", contadorInstancia);
    }

    /**
     * Armazena uma lista de transportadores na base de dados.
     * <p>
     * Este método persiste as mensagens no repositório e registra um log
     * das operações realizadas.
     *
     * @param transportadores Lista de transportadores a serem armazenados.
     * @throws RuntimeException se ocorrer um erro durante a persistência das mensagens.
     */
    @Override
    public void armazenar(@NotNull @NotEmpty List<Transportador> transportadores) {
        try {
            transportadores.forEach(transportador ->
                    transportadorRepository.save(new TransportadorEntity(transportador)));
            logMensagens();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao transportar mensagem ", e);
        }
    }

    /**
     * Recupera todas as mensagens associadas a um destinatário específico.
     * <p>
     * Este método busca nas mensagens armazenadas para encontrar aquelas que
     * têm o endereço do destinatário especificado.
     *
     * @param destinatario O destinatário cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao destinatário, ou uma lista vazia se não houver mensagens.
     */
    @Override
    public List<Mensagem> mensagens(Destinatario destinatario) {
        atualizarMensagens();
        List<Mensagem> mensagens = new ArrayList<>();
        for (TransportadorEntity entity : transportadores) {
            if (entity.getEnderecoDestinatario().equals(destinatario.endereco())) {
                mensagens.add(new MensagemBean(entity.getTitulo(), entity.getConteudo(), entity.getDataEnvio()));
            }
        }
        return mensagens.isEmpty() ? Collections.emptyList() : List.copyOf(mensagens);
    }

    /**
     * Recupera todas as mensagens associadas a um remetente específico.
     * <p>
     * Este método busca nas mensagens armazenadas para encontrar aquelas que
     * têm o endereço do remetente especificado. A lista retornada contém
     * objetos do tipo Mensagem que representam as mensagens enviadas por
     * esse remetente.
     *
     * @param remetente O remetente cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao remetente, ou uma lista vazia se não houver mensagens.
     */
    @Override
    public List<Mensagem> mensagens(Remetente remetente) {

        if (transportadores == null || transportadores.isEmpty()) {
            atualizarMensagens();
        }

        List<Mensagem> mensagensRemetente = new ArrayList<>();


        for (TransportadorEntity entity : transportadores) {

            if (entity.getEnderecoRemetente().equals(remetente.endereco())) {
                mensagensRemetente.add(new MensagemBean(entity.getTitulo(),
                        entity.getConteudo(), entity.getDataEnvio()));
            }
        }

        return mensagensRemetente.isEmpty() ?
                Collections.emptyList() : List.copyOf(mensagensRemetente);
    }

    /**
     * Atualiza o conjunto de transportadores a partir do repositório.
     * <p>
     * Este método recupera todos os transportadores da base de dados e
     * armazena-os no conjunto de transportadores da classe.
     */
    private void atualizarMensagens() {
        this.transportadores = transportadorRepository.findAll().stream().collect(Collectors.toSet());
    }

    /**
     * Registra um log das mensagens armazenadas na base de dados.
     * <p>
     * Este método é chamado após o armazenamento de mensagens para registrar
     * informações sobre a operação, incluindo a data, a hora e a quantidade
     * de mensagens armazenadas.
     */
    private void logMensagens() {
        logger.info("\n===============Log de Mensagens================== \n" +
                        "Transportador Gravado na Base de Dados Data: {} " +
                        "\nHora: {} \n" +
                        "Quantidade de Mensagens: {} \n" +
                        "===============Log de Mensagens==================",
                LocalDate.now(), LocalTime.now(), transportadores.size());
    }
}
