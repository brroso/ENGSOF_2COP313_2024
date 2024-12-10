package src;

public class Livro {

    boolean exemplarBiblioteca;
    Titulo titulo;
    int id;
    TituloController tituloController = new TituloController();

    public Livro(Titulo titulo, boolean exemplarBiblioteca) {
        this.titulo = titulo;
        this.exemplarBiblioteca = exemplarBiblioteca;
    }

    public boolean getExemplarBiblioteca() { return this.exemplarBiblioteca; }

    public int getTituloId() { return titulo.getId(); }

    public int getPrazo() { return titulo.getPrazo(); }

    public void setId(int id) { this.id = id; }

    public int getId() { return this.id; }

    public boolean verificaLivro() {
        return exemplarBiblioteca;
    }

}