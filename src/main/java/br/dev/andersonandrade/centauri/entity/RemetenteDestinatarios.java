package br.dev.andersonandrade.centauri.entity;

import br.dev.andersonandrade.centauri.interfaces.Remetente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 04/10/2024
 */
@Entity
public class RemetenteDestinatarios implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200, unique = true)
    private String endereco;
    @ElementCollection
    @CollectionTable(name = "enderecos_destinatarios", joinColumns = @JoinColumn(name = "remetente_destinatarios_id"))
    @Column(name = "endereco_destinatario")
    private Set<String> enderecoDestinatarios;

    @Deprecated
    protected RemetenteDestinatarios() {
        //Obrigatorio JPA
    }

    public RemetenteDestinatarios(@NotNull Remetente remetente){
        Objects.requireNonNull(remetente.endereco(), "Endereco do remetente não pode ser nulo!");
        this.endereco = remetente.endereco();
        enderecoDestinatarios = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Set<String> getEnderecoDestinatarios() {
        return enderecoDestinatarios;
    }

    public void setEnderecoDestinatarios(Set<String> enderecoDestinatarios) {
        this.enderecoDestinatarios = enderecoDestinatarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemetenteDestinatarios that = (RemetenteDestinatarios) o;
        return Objects.equals(id, that.id) && Objects.equals(endereco, that.endereco) && Objects.equals(enderecoDestinatarios, that.enderecoDestinatarios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, endereco, enderecoDestinatarios);
    }

    @Override
    public String toString() {
        return "RemetenteDestinatarios{" +
                "id=" + id +
                ", endereco='" + endereco + '\'' +
                ", enderecoDestinatarios=" + enderecoDestinatarios +
                '}';
    }
}
