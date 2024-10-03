package br.dev.andersonandrade.centauri.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Alteracao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataAlteracao;
    private String campo;
    private String valorAnterior;
    private String alteracao;
    @ManyToOne(cascade = CascadeType.ALL)
    private Historico historico;

    @Deprecated
    protected Alteracao() {
        //Obrigatorio JPA
    }

    public Alteracao(LocalDateTime dataAlteracao, String campo, String valorAnterior, String alteracao, Historico historico) {
        this.dataAlteracao = dataAlteracao;
        this.campo = campo;
        this.valorAnterior = valorAnterior;
        this.alteracao = alteracao;
        this.historico = historico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getAlteracao() {
        return alteracao;
    }

    public void setAlteracao(String alteracao) {
        this.alteracao = alteracao;
    }

    public Historico getHistorico() {
        return historico;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alteracao alteracao1 = (Alteracao) o;
        return Objects.equals(id, alteracao1.id) && Objects.equals(dataAlteracao, alteracao1.dataAlteracao) && Objects.equals(campo, alteracao1.campo) && Objects.equals(valorAnterior, alteracao1.valorAnterior) && Objects.equals(alteracao, alteracao1.alteracao) && Objects.equals(historico, alteracao1.historico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataAlteracao, campo, valorAnterior, alteracao, historico);
    }

    @Override
    public String toString() {
        return "Alteracao{" +
                "id=" + id +
                ", dataAlteracao=" + dataAlteracao +
                ", campo='" + campo + '\'' +
                ", valorAnterior='" + valorAnterior + '\'' +
                ", alteracao='" + alteracao + '\'' +
                ", historico=" + historico +
                '}';
    }
}
