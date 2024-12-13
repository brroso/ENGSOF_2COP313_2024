package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TituloDAO {
    private Connection conexao;

    public TituloDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public void inserir(Titulo titulo) throws SQLException {
        String sql = "INSERT INTO titulo (prazo, nome, id_autor, id_area) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, titulo.getPrazo());
            stmt.setString(2, titulo.getNome());
            stmt.setInt(3, titulo.getAutor().getId());
            stmt.setInt(4, titulo.getArea().getId());
            stmt.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM titulo WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }

    public List<Titulo> listarTodos() throws SQLException {
        List<Titulo> titulos = new ArrayList<>();
        String sql = "SELECT * FROM titulo";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Titulo titulo = new Titulo(rs.getInt("prazo"), rs.getString("nome"), rs.getInt("id_autor"), rs.getInt("id_area"));
                titulo.setId(rs.getInt("id"));
                titulos.add(titulo);
            }
        }
        return titulos;
    }

    public Titulo getTituloById(int id) throws SQLException {
        String sql = "SELECT * FROM titulo WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Titulo titulo = new Titulo(rs.getInt("prazo"), rs.getString("nome"), rs.getInt("id_autor"), rs.getInt("id_area"));
                    titulo.setId(rs.getInt("id"));
                    return titulo;
                }
            }
        }
        return null;
    }
}
