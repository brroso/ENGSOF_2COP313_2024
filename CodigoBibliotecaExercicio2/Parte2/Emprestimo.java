package src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Emprestimo {

    Date dataEmprestimo = new Date();
    Date dataPrevista = new Date();
    Date data_aux = new Date();
    //Cada emprestimo tem um conjutno de itens
    List<Item> item = new ArrayList<Item>();
    int emprestimo = 0;

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }


    // Metodo respons�vel por realizar o empr�stimo
    public Emprestimo emprestar(List<Livro> livros) {
        //Para o numero de livros a ser emprestado
        for (int i = 0; i < livros.size(); i++) {
			item.add(new Item(livros.get(i)));
			emprestimo += 1;
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
        if (item.size() > 2) {
            int tam = item.size() - 2;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataPrevista);
            calendar.add(Calendar.DATE, (tam * 2));
            dataPrevista = calendar.getTime();
        }
        for (int j = 0; j < item.size(); j++)
            item.get(j).setDataDevolucao(dataPrevista);

        return dataPrevista;

    }


}
