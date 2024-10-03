package br.dev.andersonandrade.centauri.entity;

import br.dev.andersonandrade.centauri.enumeradores.NivelPermisao;
import br.dev.andersonandrade.centauri.enumeradores.NomePermisao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Permissao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, nullable = false)
    private String nome;
    @PositiveOrZero(message = "Verifique o nivel da permisão ele tem que ser positivo ou zero!")
    @Max(value = 5, message = "Verifique o Valor da Permisão ele não pode ser maior que 5!")
    private int nivel;
    @ManyToOne(cascade = CascadeType.ALL)
    private Administrador administrador;

    @Deprecated
    protected Permissao() {
        //Obrigatorio JPA
    }

    public Permissao(NomePermisao nome, NivelPermisao nivel, Administrador administrador) {
        this.nome = nome.getNomePermisao();
        this.nivel = nivel.getNivelPermissao();
        this.administrador = administrador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @PositiveOrZero(message = "Verifique o nivel da permisão ele tem que ser positivo ou zero!")
    @Max(value = 5, message = "Verifique o Valor da Permisão ele não pode ser maior que 5!")
    public int getNivel() {
        return nivel;
    }

    public void setNivel(@PositiveOrZero(message = "Verifique o nivel da permisão ele tem que ser positivo ou zero!")
                         @Max(value = 5, message = "Verifique o Valor da Permisão ele não pode ser maior que 5!")
                         int nivel) {
        this.nivel = nivel;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permissao permissao = (Permissao) o;
        return nivel == permissao.nivel && Objects.equals(id, permissao.id) && Objects.equals(nome, permissao.nome) && Objects.equals(administrador, permissao.administrador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, nivel, administrador);
    }

    @Override
    public String toString() {
        return "Permissao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", nivel=" + nivel +
                ", administrador=" + administrador +
                '}';
    }
}
