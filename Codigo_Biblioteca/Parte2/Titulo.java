package src;

import javax.swing.*;
import java.sql.SQLException;

public class Titulo {
    int id;
    int prazo;
    String nome;
    Autor autor;
    Area area;

    public Titulo(int prazo, String nome, int idAutor, int idArea) {
        this.prazo = prazo;
        this.nome = nome;

        try {
            // Tenta obter a lista de autores
            this.autor = new AutorDAO().getAutorById(idAutor);
            this.area = new AreaDAO().getAreaById(idArea);

        } catch (SQLException e) {
            // Trate o erro de SQL aqui, por exemplo, mostrando uma mensagem de erro
            JOptionPane.showMessageDialog(null, "Erro ao ligar autor ou area ao titulo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Registra o erro no console (pode ser removido em produção)
        }

    }

    public int getPrazo() {
        return this.prazo;
    }

    public String getNome() { return this.nome; }

    public Area getArea() { return this.area; }

    public Autor getAutor() { return this.autor; }

    public void setId(int id) { this.id = id; }

    public int getId() { return this.id; }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

}