package br.dev.andersonandrade.centauri.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Login implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 150, nullable = false, unique = true)
    private String email;
    @Column(length = 100, nullable = false, unique = true)
    private String nomeUsuario;
    private boolean ativo;
    @OneToOne(cascade = CascadeType.ALL)
    private Senha senha;

    @Deprecated
    protected Login() {
        //Obrigatorio JPA
    }

    public Login(String email, String nomeUsuario, boolean ativo, Senha senha) {
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.ativo = ativo;
        this.senha = senha;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Senha getSenha() {
        return senha;
    }

    public void setSenha(Senha senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return ativo == login.ativo && Objects.equals(id, login.id) && Objects.equals(email, login.email) && Objects.equals(nomeUsuario, login.nomeUsuario) && Objects.equals(senha, login.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, nomeUsuario, ativo, senha);
    }

    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", ativo=" + ativo +
                ", senha=" + senha +
                '}';
    }
}
