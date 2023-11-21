package model;

import java.util.Objects;

public class Usuario {
    private String nome;
    private String senha;

    public Usuario(String nome, String senha) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.senha = Objects.requireNonNull(senha, "Senha não pode ser nula");
        if (nome.isEmpty() || senha.isEmpty()) {
            throw new IllegalArgumentException("Nome e senha não podem ser vazios");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = Objects.requireNonNull(senha, "Senha não pode ser nula");
        if (senha.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
    }
}
