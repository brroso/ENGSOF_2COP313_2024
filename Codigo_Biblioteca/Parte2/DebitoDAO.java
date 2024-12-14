package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DebitoDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    public void inserir(Debito debito) throws SQLException {
        String sql = "INSERT INTO debito (ra_aluno, valor) VALUES (?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, debito.getCodigoAluno());
            stmt.setFloat(2, debito.getValor());
            stmt.executeUpdate();
        }
    }

    public boolean excluirById(int id) throws SQLException {
        String sql = "DELETE FROM debito WHERE id = ?";
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

    public List<Debito> listarTodos() throws SQLException {
        List<Debito> debitos = new ArrayList<>();
        String sql = "SELECT * FROM debito";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Debito debito = new Debito(rs.getInt("ra_aluno"), rs.getFloat("valor"));
                debito.setId(rs.getInt("id"));
                debitos.add(debito);
            }
        }
        return debitos;
    }

    public Debito getDebitoById(int id) throws SQLException {
        String sql = "SELECT * FROM debito WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Debito debito = new Debito(rs.getInt("ra_aluno"), rs.getFloat("valor"));
                    debito.setId(rs.getInt("id"));
                    return debito;
                }
            }
        }
        return null;
    }

    public boolean verificaDebitoByRA(int RA) throws SQLException {
        String sql = "SELECT * FROM debito WHERE ra_aluno = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, RA);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
