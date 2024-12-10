package src;

import java.util.List;

public class Aluno {
    int RA;
    String nome;
    String email;

    public Aluno(String nome, String email) {
        this.nome = nome;
        this.email = email;

    }

    public String getNome() {
        return this.nome;
    }

    public int getRA() {
        return this.RA;
    }

    public void setRA(int RA) {
        this.RA = RA;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
