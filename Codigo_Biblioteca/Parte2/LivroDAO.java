package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    private TituloDAO tituloDAO = new TituloDAO();

    public void inserir(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (id_titulo, exemplar_biblioteca) VALUES (?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, livro.getTituloId());
            stmt.setBoolean(2, livro.getExemplarBiblioteca());
            stmt.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livro livro = new Livro(
                        tituloDAO.getTituloById(rs.getInt("id_titulo")),
                        rs.getBoolean("exemplar_biblioteca")
                );
                livro.setId(rs.getInt("id"));
                livros.add(livro);
            }
        }
        return livros;
    }

    public Livro getLivroById(int id) throws SQLException {
        String sql = "SELECT * FROM livro WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro(
                            tituloDAO.getTituloById(rs.getInt("id_titulo")),
                            rs.getBoolean("exemplar_biblioteca")
                    );
                    livro.setId(rs.getInt("id"));
                    return livro;
                }
            }
        }
        return null;
    }
}
