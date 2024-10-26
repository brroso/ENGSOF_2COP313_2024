package src;


public class Livro {

    int prazo;
    boolean exemplarBiblioteca;
    Titulo titulo;
    int codigoLivro;

    public Livro(int codigo) {
        super();
        //inst�ncia um titulo e o associa ao livro
        titulo = new Titulo(codigo);
        codigoLivro = codigo;
        //codigo aleat�rio para definir se o livro � exemplar unico
        if (codigo == 2 || codigo == 4)
            exemplarBiblioteca = true;
        else
            exemplarBiblioteca = false;
    }

    public int verPrazo() {
        return titulo.getPrazo();
    }


    public boolean verificaLivro() {
        return exemplarBiblioteca;

    }

}