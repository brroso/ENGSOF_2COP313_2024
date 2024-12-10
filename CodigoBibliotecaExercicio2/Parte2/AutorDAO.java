package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    private Connection conexao;

    public AutorDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public void inserir(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (nome) VALUES (?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM autor WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
        try (Statement stmt = conexao.createStatement();
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
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
