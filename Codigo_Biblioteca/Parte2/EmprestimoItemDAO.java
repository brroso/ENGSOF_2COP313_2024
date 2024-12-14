package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoItemDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    private LivroDAO livroDAO = new LivroDAO();
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    public void inserir(EmprestimoItem emprestimoItem) throws SQLException {
        String sql = "INSERT INTO emprestimo_item (id_livro, id_emprestimo) VALUES (?, ?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, emprestimoItem.getLivro().getId());
            stmt.setInt(2, emprestimoItem.getEmprestimo().getId());
            stmt.executeUpdate();
        }
    }

    public boolean excluirById(int id) throws SQLException {
        String sql = "DELETE FROM emprestimo_item WHERE id = ?";
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

    public List<EmprestimoItem> listarTodos() throws SQLException {
        List<EmprestimoItem> emprestimoItems = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo_item";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EmprestimoItem emprestimoItem = new EmprestimoItem(
                        livroDAO.getLivroById(rs.getInt("id_livro")),
                        emprestimoDAO.getEmprestimoById(rs.getInt("id_emprestimo"))
                );
                emprestimoItem.setId(rs.getInt("id"));
                emprestimoItems.add(emprestimoItem);
            }
        }
        return emprestimoItems;
    }

    public EmprestimoItem getEmprestimoItemById(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimo_item WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EmprestimoItem emprestimoItem = new EmprestimoItem(
                            livroDAO.getLivroById(rs.getInt("id_livro")),
                            emprestimoDAO.getEmprestimoById(rs.getInt("id_emprestimo"))
                    );
                    emprestimoItem.setId(rs.getInt("id"));
                    return emprestimoItem;
                }
            }
        }
        return null;
    }

    public List<EmprestimoItem> getEmprestimoItemsByEmprestimoId(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimo_item WHERE id_emprestimo = ?";
        List<EmprestimoItem> emprestimoItems = new ArrayList<>();
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmprestimoItem emprestimoItem = new EmprestimoItem(
                            livroDAO.getLivroById(rs.getInt("id_livro")),
                            emprestimoDAO.getEmprestimoById(rs.getInt("id_emprestimo"))
                    );
                    emprestimoItem.setId(rs.getInt("id"));
                    emprestimoItems.add(emprestimoItem);
                }
            }
        }
        return emprestimoItems;
    }
}
