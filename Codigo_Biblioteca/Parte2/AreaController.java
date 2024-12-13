package src;

import java.sql.SQLException;
import java.util.List;

public class AreaController {
    private AreaDAO areaDAO;

    public AreaController() {
        areaDAO = new AreaDAO();
    }

    public void cadastrarArea(String nome) {
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios.");
        }
        try {
            Area area = new Area(nome);
            areaDAO.inserir(area);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar area: " + e.getMessage());
        }
    }

    public List<Area> getAreas() {
        try {
            return areaDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar areas:" + e.getMessage());
        }
    }

    public Area getAreaById(int id) {
        try {
            return areaDAO.getAreaById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar area:" + e.getMessage());
        }
    }

    public void excluirAreaById(int id){
        try {
            areaDAO.excluirAreaById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
