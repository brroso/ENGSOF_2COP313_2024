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
            stmtEmprestimo.setDate(2, emprestimo.getDataEmprestimo());
            stmtEmprestimo.setDate(3, emprestimo.getDataPrevista());
            stmtEmprestimo.executeUpdate();

            ResultSet generatedKeys = stmtEmprestimo.getGeneratedKeys();
            emprestimo.setId(generatedKeys.getInt("id"));

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
                    return emprestimo;
                }
            }
        }
        return null;
    }
}
