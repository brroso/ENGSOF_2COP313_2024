package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DebitoDAO {
    private Connection conexao;

    public DebitoDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public void inserir(Debito debito) throws SQLException {
        String sql = "INSERT INTO debito (ra_aluno, valor) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, debito.getCodigoAluno());
            stmt.setFloat(2, debito.getValor());
            stmt.executeUpdate();
        }
    }

    public boolean excluirById(int id) throws SQLException {
        String sql = "DELETE FROM debito WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
        try (Statement stmt = conexao.createStatement();
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
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, RA);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
