package src;

import java.sql.SQLException;
import java.util.List;

public class AutorController {
    private AutorDAO autorDAO;

    public AutorController() {
        autorDAO = new AutorDAO();
    }

    public void cadastrarAutor(String nome) {
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios.");
        }
        try {
            Autor autor = new Autor(nome);
            autorDAO.inserir(autor);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar autor: " + e.getMessage());
        }
    }

    public List<Autor> getAutores() {
        try {
            return autorDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar autores:" + e.getMessage());
        }
    }

    public Autor getAutorById(int id) {
        try {
            return autorDAO.getAutorById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar area:" + e.getMessage());
        }
    }

    public void excluirAutorById(int id){
        try {
            autorDAO.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
