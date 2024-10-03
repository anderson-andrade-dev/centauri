package br.dev.andersonandrade.centauri.service;

import br.dev.andersonandrade.centauri.enumeradores.Prioridade;
import br.dev.andersonandrade.centauri.exceptions.MensagemException;
import br.dev.andersonandrade.centauri.interfaces.*;
import br.dev.andersonandrade.centauri.record.TransportadorRecord;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Classe CorreioMensagem
 * <p>
 * Implementação da interface Mensageiro, responsável por gerenciar o envio de mensagens
 * entre remetentes e destinatários. As mensagens são armazenadas em filas de acordo
 * com a prioridade e são enviadas para o armazém em intervalos regulares.
 *
 * @author Anderson Andrade Dev
 * @date 28/09/2024
 * @contact andersonandradedev@outlook.com
 */
@Service
@ApplicationScope
public class CorreioMensagem implements Mensageiro {

    @Qualifier("amarzemMensagens")
    private final Armazem armazem;

    private final Queue<Transportador> transportadoresUrgente;
    private final Queue<Transportador> transportadoresNormal;
    private final Queue<Transportador> transportadoresBaixa;
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Construtor que inicializa a classe CorreioMensagem.
     *
     * @param armazem O repositório para gerenciar a persistência das mensagens.
     */
    @Autowired
    public CorreioMensagem(@NotNull Armazem armazem) {
        this.transportadoresUrgente = new ConcurrentLinkedQueue<>();
        this.transportadoresNormal = new ConcurrentLinkedQueue<>();
        this.transportadoresBaixa = new ConcurrentLinkedQueue<>();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(4);
        this.armazem = armazem;
        this.gerenciarMensagens();
    }

    /**
     * Recebe uma mensagem e a adiciona à fila apropriada com base na prioridade.
     * <p>
     * Este método valida a mensagem, o destinatário e o remetente antes de
     * adicioná-los às filas de transporte.
     *
     * @param destinatario O destinatário da mensagem.
     * @param remetente    O remetente da mensagem.
     * @param mensagem     A mensagem a ser enviada.
     * @param prioridade   A prioridade da mensagem.
     */
    @Override
    public void recebeMensagem(Destinatario destinatario, Remetente remetente, Mensagem mensagem, Prioridade prioridade) {
        validarMensagem(mensagem);
        validarDestinatario(destinatario);
        validarRemetente(remetente);

        // Adiciona a mensagem à fila de acordo com a prioridade
        switch (prioridade) {
            case URGENTE -> transportadoresUrgente.add(new TransportadorRecord(destinatario, remetente, mensagem));
            case NORMAL -> transportadoresNormal.add(new TransportadorRecord(destinatario, remetente, mensagem));
            case BAIXA -> transportadoresBaixa.add(new TransportadorRecord(destinatario, remetente, mensagem));
        }
    }

    /**
     * Gerencia o envio de mensagens das filas para o armazém.
     * <p>
     * Este método utiliza um agendador para enviar mensagens das filas
     * com base na prioridade, armazenando as mensagens no armazém
     * em intervalos regulares.
     */
    @Override
    public void gerenciarMensagens() {
        Runnable prioridadeUrgente = () -> {
            if (!transportadoresUrgente.isEmpty()) {
                armazem.armazenar(transportadoresUrgente.stream().toList());
                transportadoresUrgente.clear();
            }
        };

        Runnable prioridadeNormal = () -> {
            if (!transportadoresNormal.isEmpty()) {
                armazem.armazenar(transportadoresNormal.stream().toList());
                transportadoresNormal.clear();
            }
        };

        Runnable prioridadeBaixa = () -> {
            if (!transportadoresBaixa.isEmpty()) {
                armazem.armazenar(transportadoresBaixa.stream().toList());
                transportadoresBaixa.clear();
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(prioridadeUrgente, 0, 60, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(prioridadeNormal, 0, 5 * 60, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(prioridadeBaixa, 0, 10 * 60, TimeUnit.SECONDS);
    }

    /**
     * Busca uma mensagem específica para um destinatário.
     * <p>
     * Este método verifica no armazém se a mensagem existe
     * com base no destinatário e retorna a mensagem correspondente.
     *
     * @param destinatario O destinatário da mensagem.
     * @param mensagem     A mensagem a ser buscada.
     * @return A mensagem encontrada, ou null se não houver correspondência.
     */
    @Override
    public Mensagem buscaMensagem(Destinatario destinatario, Mensagem mensagem) {
        var mensagens = armazem.mensagens(destinatario);
        return mensagens.stream()
                .filter(m -> m.getConteudo().equals(mensagem.getConteudo()))
                .filter(m -> m.getTitulo().equals(mensagem.getTitulo()))
                .filter(m -> m.getDataEnvio().isEqual(mensagem.getDataEnvio()))
                .findFirst().orElse(null);
    }

    /**
     * Recupera todas as mensagens associadas a um destinatário específico.
     * <p>
     * Este método busca no armazém todas as mensagens
     * relacionadas ao destinatário.
     *
     * @param destinatario O destinatário cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao destinatário, ou uma lista vazia se não houver mensagens.
     */
    @Override
    public List<Mensagem> mensagens(Destinatario destinatario) {
        var mensagens = armazem.mensagens(destinatario);
        return mensagens.isEmpty() ? List.of() : mensagens;
    }

    /**
     * Recupera todas as mensagens associadas a um remetente específico.
     * <p>
     * Este método busca no armazém todas as mensagens
     * relacionadas ao remetente.
     *
     * @param remetente O remetente cujas mensagens devem ser recuperadas.
     * @return Uma lista de mensagens associadas ao remetente, ou uma lista vazia se não houver mensagens.
     */
    @Override
    public List<Mensagem> mensagens(Remetente remetente) {
        // Recupera mensagens de remetentes e destinatários
        var mensagens = armazem.mensagens(remetente);
        return mensagens.isEmpty() ? List.of() : mensagens;
    }

    /**
     * Valida se a mensagem está em conformidade com as regras definidas.
     * <p>
     * Este método verifica se a mensagem possui título, conteúdo e data de envio válidos.
     *
     * @param mensagem A mensagem a ser validada.
     * @throws MensagemException se a mensagem não for válida.
     */
    private void validarMensagem(Mensagem mensagem) {
        if (mensagem.getTitulo() == null || mensagem.getTitulo().isEmpty()) {
            throw new MensagemException("Erro! título da mensagem não pode ser nulo ou em branco!");
        }
        if (mensagem.getConteudo() == null || mensagem.getConteudo().isEmpty() || mensagem.getConteudo().isBlank()) {
            throw new MensagemException("Erro! conteúdo da mensagem não pode ser nulo ou em branco!");
        }
        if (mensagem.getDataEnvio() == null) {
            throw new MensagemException("Erro! data de envio da mensagem não pode ser nula!");
        }
        if (mensagem.getDataEnvio().isAfter(LocalDateTime.now())) {
            throw new MensagemException("Erro! data de envio da mensagem não pode ser posterior à data atual!");
        }
    }

    /**
     * Valida se o destinatário está em conformidade com as regras definidas.
     * <p>
     * Este método verifica se o endereço e nome do destinatário são válidos.
     *
     * @param destinatario O destinatário a ser validado.
     * @throws MensagemException se o destinatário não for válido.
     */
    private void validarDestinatario(Destinatario destinatario) {
        if (destinatario.endereco() == null || destinatario.endereco().isBlank()) {
            throw new MensagemException("Erro! endereço do destinatário não pode ser nulo ou em branco!");
        }
        if (destinatario.nome() == null || destinatario.nome().isBlank()) {
            throw new MensagemException("Erro! nome do destinatário não pode ser nulo ou em branco!");
        }
    }

    /**
     * Valida se o remetente está em conformidade com as regras definidas.
     * <p>
     * Este método verifica se o endereço e nome do remetente são válidos.
     *
     * @param remetente O remetente a ser validado.
     * @throws MensagemException se o remetente não for válido.
     */
    private void validarRemetente(Remetente remetente) {
        if (remetente.endereco() == null || remetente.endereco().isBlank()) {
            throw new MensagemException("Erro! endereço do remetente não pode ser nulo ou em branco!");
        }
        if (remetente.nome() == null || remetente.nome().isBlank()) {
            throw new MensagemException("Erro! nome do remetente não pode ser nulo ou em branco!");
        }
    }
}