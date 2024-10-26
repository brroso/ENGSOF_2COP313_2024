package src;

public class Titulo {
    int prazo;
    String nome;
    Autor autor;
    Area area;

    public Titulo(int codigo) {

        this.prazo = codigo + 1;
        this.nome = codigo + " Titulo";
        this.autor = new Autor(String.valueOf(codigo));
        this.area = new Area(String.valueOf(codigo));

    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

}