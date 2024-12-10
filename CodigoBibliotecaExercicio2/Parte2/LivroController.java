package src;

import java.sql.SQLException;
import java.util.List;

public class LivroController {
    private LivroDAO livroDAO;

    public LivroController() {
        livroDAO = new LivroDAO();
    }

    public void cadastrarLivro(Titulo titulo, boolean exemplarBiblioteca) {
        try {
            Livro livro = new Livro(titulo, exemplarBiblioteca);
            livroDAO.inserir(livro);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    public List<Livro> getLivros() {
        try {
            return livroDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros:" + e.getMessage());
        }
    }

    public Livro getLivroById(int id) {
        try {
            return livroDAO.getLivroById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro:" + e.getMessage());
        }
    }

    public void excluirLivroById(int id){
        try {
            livroDAO.excluir(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
