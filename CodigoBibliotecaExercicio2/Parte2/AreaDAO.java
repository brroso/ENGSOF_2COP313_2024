package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {
    private Connection conexao;

    public AreaDAO() {
        try {
            this.conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aluno_user", "senha123");
        } catch (SQLException e) {
            System.out.println("Erro na conexão:" + e);
        }
    }

    public void inserir(Area area) throws SQLException {
        String sql = "INSERT INTO area (nome) VALUES (?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, area.getNome());
            stmt.executeUpdate();
        }
    }

    public boolean excluirAreaById(int id) throws SQLException {
        String sql = "DELETE FROM area WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException exception) {
            System.err.println("Erro ao excluir: " + exception.getMessage());
            return false;
        }
    }

    public List<Area> listarTodos() throws SQLException {
        List<Area> areas = new ArrayList<>();
        String sql = "SELECT * FROM area";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Area area = new Area(rs.getString("nome"));
                area.setId(rs.getInt("id"));
                areas.add(area);
            }
        }
        return areas;
    }

    public Area getAreaById(int id) throws SQLException {
        String sql = "SELECT * FROM area WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Area area = new Area(rs.getString("nome"));
                    area.setId(rs.getInt("id"));
                    return area;
                }
            }
        }
        return null;
    }
}
