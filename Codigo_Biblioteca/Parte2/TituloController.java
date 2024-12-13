package src;

import java.sql.SQLException;
import java.util.List;

public class TituloController {
    private TituloDAO tituloDAO;

    public TituloController() {
        tituloDAO = new TituloDAO();
    }

    public void cadastrarTitulo(int prazo, String nome, int idAutor, int idArea) {
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios.");
        }
        try {
            Titulo titulo = new Titulo(prazo, nome, idAutor, idArea);
            tituloDAO.inserir(titulo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar titulo: " + e.getMessage());
        }
    }

    public List<Titulo> getTitulos() {
        try {
            return tituloDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos:" + e.getMessage());
        }
    }

    public Titulo getTituloById(int id) {
        try {
            return tituloDAO.getTituloById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno:" + e.getMessage());
        }
    }

    public void excluirTituloById(int id){
        try {
            tituloDAO.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
