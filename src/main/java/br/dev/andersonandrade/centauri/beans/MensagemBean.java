package br.dev.andersonandrade.centauri.beans;

import br.dev.andersonandrade.centauri.interfaces.Mensagem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class MensagemBean implements Mensagem, Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataLeitura;
    private String titulo;
    private String conteudo;
    private boolean lida = false;


    public MensagemBean(@NotNull @NotBlank String titulo, @NotNull @NotBlank String conteudo, @NotNull LocalDateTime dataEnvio) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataEnvio(@NotNull LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public void setDataLeitura(@NotNull LocalDateTime dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public void setTitulo(@NotNull @NotBlank String titulo) {
        this.titulo = titulo;
    }

    public void setConteudo(@NotNull @NotBlank String conteudo) {
        this.conteudo = conteudo;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    @Override
    public LocalDateTime getDataEnvio() {
        return this.dataEnvio;
    }

    @Override
    public LocalDateTime getDataLeitura() {
        return this.dataLeitura;
    }

    @Override
    public String getTitulo() {
        return this.titulo;
    }

    @Override
    public String getConteudo() {
        return this.conteudo;
    }


    @Override
    public boolean isLida() {
        return this.lida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MensagemBean that = (MensagemBean) o;
        return lida == that.lida && Objects.equals(dataEnvio, that.dataEnvio) && Objects.equals(dataLeitura, that.dataLeitura) && Objects.equals(titulo, that.titulo) && Objects.equals(conteudo, that.conteudo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataEnvio, dataLeitura, titulo, conteudo, lida);
    }

    @Override
    public String toString() {
        return "MensagemBean{" +
                "id=" + id +
                ", dataEnvio=" + dataEnvio +
                ", dataLeitura=" + dataLeitura +
                ", titulo='" + titulo + '\'' +
                ", conteudo='" + conteudo + '\'' +
                ", lida=" + lida +
                '}';
    }
}
