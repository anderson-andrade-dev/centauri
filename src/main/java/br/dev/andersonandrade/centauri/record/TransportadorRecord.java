package br.dev.andersonandrade.centauri.record;

import br.dev.andersonandrade.centauri.interfaces.Destinatario;
import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import br.dev.andersonandrade.centauri.interfaces.Remetente;
import br.dev.andersonandrade.centauri.interfaces.Transportador;
import jakarta.validation.constraints.NotNull;


public record TransportadorRecord(@NotNull Destinatario destinatario,
                                  @NotNull Remetente remetente, @NotNull Mensagem mensagem) implements Transportador {

}
