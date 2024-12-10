package src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Emprestimo {

    Date dataEmprestimo = new Date();
    Date dataPrevista = new Date();
    Date data_aux = new Date();
    List<Item> item = new ArrayList<Item>();
    int ra_aluno;

    public Emprestimo(int RA) {
        this.ra_aluno = RA;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Emprestimo emprestar(List<Livro> livros) {
        for (int i = 0; i < livros.size(); i++) {
            item.add(new Item(livros.get(i)));
        }

        CalculaDataDevolucao();
        return this;

    }

    private Date CalculaDataDevolucao() {
        Date date = new Date();

        for (int j = 0; j < item.size(); j++) {
            data_aux = item.get(j).calculaDataDevolucao(date);
            if (dataPrevista.compareTo(data_aux) < 0)
                dataPrevista = data_aux;
        }

        dataPrevista = this.AddDias(dataPrevista);

        for (int j = 0; j < item.size(); j++)
            item.get(j).setDataDevolucao(dataPrevista);

        return dataPrevista;
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
