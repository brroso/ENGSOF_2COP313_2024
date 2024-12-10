package src;

public class Autor {
    int id;
    String nome;
    public Autor(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
