package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    public void inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO aluno (nome, email) VALUES (?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.executeUpdate();
        }
    }

    public List<Aluno> getAlunos() throws SQLException {
        List<Aluno> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM aluno";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Aluno aluno = new Aluno(rs.getString("nome"), rs.getString("email"));
                aluno.setRA(rs.getInt("RA"));
                usuarios.add(aluno);
            }
        }
        return usuarios;
    }

    public Aluno getAlunoFromRA(int RA) throws SQLException {
        String sql = "SELECT * FROM aluno WHERE RA = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, RA);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno(rs.getString("nome"), rs.getString("email"));
                    aluno.setRA(rs.getInt("RA"));
                    return aluno;
                }
            }
        }
        return null;
    }

    public boolean excluir(int RA) throws SQLException {
        String sql = "DELETE FROM aluno WHERE RA = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, RA);
            stmt.executeUpdate();
            return true;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }
}
