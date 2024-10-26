package src;

import java.util.List;

public class Aluno {
    String RA;

    public Aluno(String nome) {
        super();
        //precisa ir no banco e criar o aluno
        this.RA = nome;

    }

    public String getNome() {
        return RA;
    }

    public void setNome(String nome) {
        this.RA = nome;
    }

    public boolean verficaAluno() {   //Se o RA � null � retorna erro - m�todo aleat�rio
        return !this.RA.equals("10");
    }

    public boolean verificaDebito() {       //instancia um objeto d�bito
        Debito debito = new Debito(Integer.parseInt(this.RA));
        return debito.verificaDebito();
        /* Aqui voc� deve chamar o metodo verificaDebito da classe debito*/
    }

    //Metodo que delega a funcionalidade de emprestar para a classe emprestimo
    public Emprestimo emprestar(List<Livro> livros) {
        Emprestimo e = new Emprestimo();
        return e.emprestar(livros);
    }
}
