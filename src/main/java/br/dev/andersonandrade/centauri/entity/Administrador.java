package br.dev.andersonandrade.centauri.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Administrador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Column(length = 20)
    private String nome;
    @Column(length = 30)
    private String sobrenome;
    @OneToOne
    private Login login;
    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL)
    private List<Permissao> permissoes;
    @OneToOne
    private Historico historico;

    @Deprecated
    protected Administrador() {
        //Obrigatorio JPA
    }

    public Administrador(@Nonnull String nome, @Nonnull String sobrenome, @Nonnull Login login,
                         @Nonnull List<Permissao> permissoes) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.login = login;
        this.permissoes = permissoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
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
        Administrador that = (Administrador) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(sobrenome, that.sobrenome)
                && Objects.equals(login, that.login) && Objects.equals(permissoes, that.permissoes) && Objects.equals(historico, that.historico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, sobrenome, login, permissoes, historico);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", login=" + login +
                ", permissoes=" + permissoes +
                ", historico=" + historico +
                '}';
    }
}
