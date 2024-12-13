package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    private Connection conexao;
    private TituloDAO tituloDAO = new TituloDAO();

    public LivroDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (id_titulo, exemplar_biblioteca) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, livro.getTituloId());
            stmt.setBoolean(2,livro.getExemplarBiblioteca());
            stmt.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livro livro = new Livro(tituloDAO.getTituloById(rs.getInt("id_titulo")), rs.getBoolean("exemplar_biblioteca"));
                livro.setId(rs.getInt("id"));
                livros.add(livro);
            }
        }
        return livros;
    }

    public Livro getLivroById(int id) throws SQLException {
        String sql = "SELECT * FROM livro WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro(tituloDAO.getTituloById(rs.getInt("id_titulo")), rs.getBoolean("exemplar_biblioteca"));
                    livro.setId(rs.getInt("id"));
                    return livro;
                }
            }
        }
        return null;
    }
}
