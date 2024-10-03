package br.dev.andersonandrade.centauri.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Senha implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200, nullable = false)
    private String chave;

    @Deprecated
    public Senha() {
        //Obrigatorio JPA
    }

    public Senha(String chave) {
        this.chave = chave;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Senha senha = (Senha) o;
        return Objects.equals(id, senha.id) && Objects.equals(chave, senha.chave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chave);
    }

    @Override
    public String toString() {
        return "Senha{" +
                "id=" + id +
                ", chave='" + chave + '\'' +
                '}';
    }
}
