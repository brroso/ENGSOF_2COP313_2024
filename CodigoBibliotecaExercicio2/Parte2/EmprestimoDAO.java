package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    private Connection conexao;

    public EmprestimoDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public Emprestimo inserir(Emprestimo emprestimo) throws SQLException {
        String sqlEmprestimo = "INSERT INTO emprestimo (ra_aluno, data_emprestimo, data_prevista) VALUES (?, ?, ?)";
        try (PreparedStatement stmtEmprestimo = conexao.prepareStatement(sqlEmprestimo, Statement.RETURN_GENERATED_KEYS)) {
            stmtEmprestimo.setInt(1, emprestimo.getRAAluno());
            stmtEmprestimo.setDate(2, emprestimo.getDataEmprestimoSql());
            stmtEmprestimo.setDate(3, emprestimo.getDataPrevistaSql());
            stmtEmprestimo.executeUpdate();

            ResultSet generatedKeys = stmtEmprestimo.getGeneratedKeys();

            if (generatedKeys.next()) {  // Movemos para o primeiro resultado
                emprestimo.setId(generatedKeys.getInt(1));  // Use o índice correto
            }

            return emprestimo;
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo(rs.getInt("ra_aluno"));
                emprestimo.setId(rs.getInt("id"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo"));
                emprestimo.setDataPrevista(rs.getDate("data_prevista"));
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }

    public Emprestimo getEmprestimoById(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Emprestimo emprestimo = new Emprestimo(rs.getInt("ra_aluno"));
                    emprestimo.setId(rs.getInt("id"));
                    emprestimo.setRAAluno(rs.getInt("ra_aluno"));
                    emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo"));
                    emprestimo.setDataPrevista(rs.getDate("data_prevista"));
                    return emprestimo;
                }
            }
        }
        return null;
    }

    public void excluirById(int id) throws SQLException {
        String sql = "DELETE FROM emprestimo WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Empréstimo com ID " + id + " excluído com sucesso.");
            } else {
                System.out.println("Nenhum empréstimo encontrado com o ID " + id + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o empréstimo: " + e.getMessage());
            throw e;
        }
    }
}
