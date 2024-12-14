package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    public Emprestimo inserir(Emprestimo emprestimo) throws SQLException {
        String sqlEmprestimo = "INSERT INTO emprestimo (ra_aluno, data_emprestimo, data_prevista) VALUES (?, ?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmtEmprestimo = conexao.prepareStatement(sqlEmprestimo, Statement.RETURN_GENERATED_KEYS)) {
            stmtEmprestimo.setInt(1, emprestimo.getRAAluno());
            stmtEmprestimo.setDate(2, emprestimo.getDataEmprestimoSql());
            stmtEmprestimo.setDate(3, emprestimo.getDataPrevistaSql());
            stmtEmprestimo.executeUpdate();

            try (ResultSet generatedKeys = stmtEmprestimo.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprestimo.setId(generatedKeys.getInt(1));
                }
            }
        }
        return emprestimo;
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
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
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nenhum empréstimo encontrado com o ID " + id + ".");
            }
        }
    }
}
