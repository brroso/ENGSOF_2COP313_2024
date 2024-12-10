package src;

import java.sql.SQLException;
import java.util.List;

public class DebitoController {
    private DebitoDAO debitoDAO;

    public DebitoController() {
        debitoDAO = new DebitoDAO();
    }

    public void cadastraDebito(int RA, float valor) {
        try {
            Debito debito = new Debito(RA, valor);
            debitoDAO.inserir(debito);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar debito: " + e.getMessage());
        }
    }

    public List<Debito> getAreas() {
        try {
            return debitoDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar debitos:" + e.getMessage());
        }
    }

    public Debito getDebitoById(int id) {
        try {
            return debitoDAO.getDebitoById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar debito:" + e.getMessage());
        }
    }

    public void excluirDebitoById(int id){
        try {
            debitoDAO.excluirById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verificaDebitoByRA(int ra){
        try {
            return verificaDebitoByRA(ra);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
