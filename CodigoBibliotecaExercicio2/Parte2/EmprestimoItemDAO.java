package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoItemDAO {
    private Connection conexao;
    private LivroDAO livroDAO = new LivroDAO();
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    public EmprestimoItemDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public void inserir(EmprestimoItem emprestimoItem) throws SQLException {
        String sql = "INSERT INTO emprestimo_item (id_livro, id_emprestimo) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setFloat(1, emprestimoItem.getLivro().getId());
            stmt.setInt(2, emprestimoItem.getEmprestimo().getId());
            stmt.executeUpdate();
        }
    }

    public boolean excluirById(int id) throws SQLException {
        String sql = "DELETE FROM emprestimo_item WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }

    public List<EmprestimoItem> listarTodos() throws SQLException {
        List<EmprestimoItem> emprestimoItems = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo_item";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EmprestimoItem emprestimoItem = new EmprestimoItem(livroDAO.getLivroById(rs.getInt("id_livro")), emprestimoDAO.getEmprestimoById(rs.getInt("id_emprestimo")));
                emprestimoItem.setId(rs.getInt("id"));
                emprestimoItems.add(emprestimoItem);
            }
        }
        return emprestimoItems;
    }

    public EmprestimoItem getEmprestimoItemById(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimo_item WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EmprestimoItem emprestimoItem = new EmprestimoItem(livroDAO.getLivroById(rs.getInt("id_livro")), emprestimoDAO.getEmprestimoById(rs.getInt("id_emprestimo")));
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
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmprestimoItem emprestimoItem = new EmprestimoItem(livroDAO.getLivroById(rs.getInt("id_livro")), emprestimoDAO.getEmprestimoById(rs.getInt("id_emprestimo")));
                    emprestimoItem.setId(rs.getInt("id"));
                    emprestimoItems.add(emprestimoItem);
                }
            }
        }
        return emprestimoItems;
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
