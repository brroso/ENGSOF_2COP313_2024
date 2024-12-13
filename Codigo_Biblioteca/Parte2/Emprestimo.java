package src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Emprestimo {

    Date dataEmprestimo = new Date();
    Date dataPrevista = new Date();
    Date data_aux = new Date();
    List<EmprestimoItem> item = new ArrayList<EmprestimoItem>();
    int ra_aluno;
    int id;

    public Emprestimo(int RA) {
        this.ra_aluno = RA;
    }

    public java.sql.Date getDataEmprestimoSql() {
        return new java.sql.Date(dataEmprestimo.getTime());
    }

    public java.sql.Date getDataEmprestimo() {
        return (java.sql.Date) dataEmprestimo;
    }
    public java.sql.Date getDataPrevistaSql() { return new java.sql.Date(dataPrevista.getTime()); }
    public Date getDataPrevista() { return dataPrevista; }
    public List<EmprestimoItem> getItem() {return item; }
    public int getRAAluno() { return ra_aluno; }
    public int getId() { return id; }

    public void setRAAluno(int ra) { this.ra_aluno = ra; }
    public void setItem(List<EmprestimoItem> item) { this.item = item; }
    public void setDataPrevista(Date dataPrevista) { this.dataPrevista = dataPrevista; }
    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
    public void setId(int id) { this.id = id; }

    public Emprestimo emprestar(List<Livro> livros) {
        for (int i = 0; i < livros.size(); i++) {
            item.add(new EmprestimoItem(livros.get(i), this));
        }

        CalculaDataDevolucao();
        this.dataEmprestimo = new Date();
        return this;

    }

    private Date CalculaDataDevolucao() {
        Date date = new Date();

        for (int j = 0; j < item.size(); j++) {
            this.data_aux = item.get(j).calculaDataDevolucao(date);
            if (this.dataPrevista.compareTo(this.data_aux) < 0)
                this.dataPrevista = this.data_aux;
        }

        this.dataPrevista = this.AddDias(this.dataPrevista);

        return this.dataPrevista;
    }

    private Date AddDias(Date data) {
        if (item.size() > 2) {
            int tam = item.size() - 2;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.add(Calendar.DATE, (tam * 2));
            return calendar.getTime();
        }
        return data;
    }
}
