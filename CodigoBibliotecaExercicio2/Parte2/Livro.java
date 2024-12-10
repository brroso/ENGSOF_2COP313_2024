package src;


import javax.swing.*;
import java.sql.SQLException;

public class Livro {

    boolean exemplarBiblioteca;
    Titulo titulo;
    int id;

    public Livro(int idTitulo, boolean exemplarBiblioteca) {
        //inst�ncia um titulo e o associa ao livro

        try {
            // Tenta obter o titulo
            titulo = new TituloDAO().getTituloById(idTitulo);
        } catch (SQLException e) {
            // Trate o erro de SQL aqui, por exemplo, mostrando uma mensagem de erro
            JOptionPane.showMessageDialog(null, "Erro ao ligar titulo ao livro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Registra o erro no console (pode ser removido em produção)
        }

        this.exemplarBiblioteca = exemplarBiblioteca;
    }

    public boolean getExemplarBiblioteca() { return this.exemplarBiblioteca; }

    public int getTituloId() { return titulo.getId(); }

    public int getPrazo() { return titulo.getPrazo(); }

    public int setId(int id) { return this.id = id; }

    public boolean verificaLivro() {
        return exemplarBiblioteca;
    }

}