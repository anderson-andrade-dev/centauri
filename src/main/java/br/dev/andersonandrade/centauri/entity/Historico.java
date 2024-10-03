package br.dev.andersonandrade.centauri.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Historico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "historico", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Alteracao> alteracoes;

    @Deprecated
    protected Historico() {
        // Obrigatorio JPA
    }

    public Historico(List<Alteracao> alteracoes) {
        this.alteracoes = alteracoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Alteracao> getAlteracoes() {
        return alteracoes;
    }

    public void setAlteracoes(List<Alteracao> alteracoes) {
        this.alteracoes = alteracoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Historico historico = (Historico) o;
        return Objects.equals(id, historico.id) && Objects.equals(alteracoes, historico.alteracoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alteracoes);
    }

    @Override
    public String toString() {
        return "Historico{" +
                "id=" + id +
                ", alteracoes=" + alteracoes +
                '}';
    }
}
