package src;

import java.sql.SQLException;
import java.util.List;

public class AlunoController {
    private AlunoDAO alunoDAO;
    private DebitoDAO debitoDAO;

    public AlunoController() {
        alunoDAO = new AlunoDAO();
        debitoDAO = new DebitoDAO();
    }

    public void cadastrarAluno(String nome, String email) {
        if (nome.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios.");
        }
        try {
            Aluno aluno = new Aluno(nome, email);
            alunoDAO.inserir(aluno);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public List<Aluno> getAlunos() {
        try {
            return alunoDAO.getAlunos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos:" + e.getMessage());
        }
    }

    public Aluno getAlunoByRA(int ra) {
        try {
            return alunoDAO.getAlunoFromRA(ra);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno:" + e.getMessage());
        }
    }

    public boolean excluirAlunoByRA(int ra){
        try {
            return alunoDAO.excluir(ra);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verificaDebitoByRA(int ra){
        try {
            return debitoDAO.verificaDebitoByRA(ra);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void criaDebitoAluno(int ra, float valor) {
        try {
            Debito debito = new Debito(ra, valor);
            debitoDAO.inserir(debito);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
