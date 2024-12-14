package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USER = "aluno_user";
    private static final String PASSWORD = "senha123";

    public void inserir(Area area) throws SQLException {
        String sql = "INSERT INTO area (nome) VALUES (?)";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, area.getNome());
            stmt.executeUpdate();
        }
    }

    public boolean excluirAreaById(int id) throws SQLException {
        String sql = "DELETE FROM area WHERE id = ?";
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

    public List<Area> listarTodos() throws SQLException {
        List<Area> areas = new ArrayList<>();
        String sql = "SELECT * FROM area";
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
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
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
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
