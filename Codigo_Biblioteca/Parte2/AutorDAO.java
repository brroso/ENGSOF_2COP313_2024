package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    public void inserir(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (nome) VALUES (?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM autor WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }

    public List<Autor> listarTodos() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autor";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Autor autor = new Autor(rs.getString("nome"));
                autor.setId(rs.getInt("id"));
                autores.add(autor);
            }
        }
        return autores;
    }

    public Autor getAutorById(int id) throws SQLException {
        String sql = "SELECT * FROM autor WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor(rs.getString("nome"));
                    autor.setId(rs.getInt("id"));
                    return autor;
                }
            }
        }
        return null;
    }
}
