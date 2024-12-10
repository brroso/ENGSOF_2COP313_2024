package src;

import java.util.Calendar;
import java.util.Date;

public class EmprestimoItem {
    Livro livro;
    Emprestimo emprestimo;
    int id;

    public EmprestimoItem(Livro livro, Emprestimo emprestimo) {
        this.livro = livro;
        this.emprestimo = emprestimo;
    }

    public Livro getLivro() {
        return livro;
    }
    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Emprestimo getEmprestimo() { return this.emprestimo; }
    public void setEmprestimo(Emprestimo emprestimo) { this.emprestimo = emprestimo; }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public Date calculaDataDevolucao(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DATE, livro.getPrazo());
        return calendar.getTime();
    }

}
