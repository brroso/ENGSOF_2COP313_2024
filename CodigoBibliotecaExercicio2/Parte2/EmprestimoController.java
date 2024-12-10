package src;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmprestimoController {
    private LivroController livroController = new LivroController();
    private AlunoController alunoController = new AlunoController();
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private EmprestimoItemDAO emprestimoItemDAO = new EmprestimoItemDAO();

    public List<Emprestimo> getEmprestimos() {
        try {
            List<Emprestimo> emprestimos = emprestimoDAO.listarTodos();

            for (int i = 0; i < emprestimos.size(); i++) {
                Emprestimo emprestimo = emprestimos.get(i);
                List<EmprestimoItem> itens = preencheItemEmprestimo(emprestimo);
                emprestimo.setItem(itens);
            }

            return emprestimos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<EmprestimoItem> preencheItemEmprestimo(Emprestimo e) {
        try {
            return emprestimoItemDAO.getEmprestimoItemsByEmprestimoId(e.id);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void finalizaEmprestimo(int id) {
        try {
            Date hoje = new Date();
            Emprestimo emprestimo = emprestimoDAO.getEmprestimoById(id);
            if (emprestimo != null) {
                if (emprestimo.getDataPrevista().before(hoje)) {
                    long diferencaEmMilissegundos = hoje.getTime() - emprestimo.getDataPrevista().getTime();
                    long diasDiff = diferencaEmMilissegundos / (1000 * 60 * 60 * 24);

                    System.out.println("O empréstimo está atrasado em " + diasDiff + " dias.");
                    alunoController.criaDebitoAluno(emprestimo.getRAAluno(), 4*diasDiff);
                } else {
                    System.out.println("Devolução no prazo.");
                }

                emprestimoDAO.excluirById(id);
            } else {
                System.out.println("Empréstimo não encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Emprestimo emprestar(int aluno, int[] codigos, int num) {
        boolean retorno = true;
        try {
            Aluno a = alunoController.getAlunoByRA(aluno);
            if (a == null) {
                System.out.println("Aluno não encontrado");
                return new Emprestimo(0);
            }

            System.out.println("Aluno encontrado: " + a.getNome());

            if (alunoController.verificaDebitoByRA(a.getRA())) {
                System.out.println("Aluno em Débito");
                retorno = false;
            }

            if (retorno) {
                Emprestimo e = new Emprestimo(a.RA);
                List<Livro> livros = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    Livro l = livroController.getLivroById(codigos[i]);
                    if (l == null) {
                        continue;
                    }
                    if  (l.verificaLivro()) {
                        livros.add(l);
                    }
                }

                if (!livros.isEmpty()) {
                    Emprestimo emprestimoFeito = emprestimoDAO.inserir(e.emprestar(livros));
                    for (int i = 0; i < livros.size(); i++) {
                        EmprestimoItem emprestimoItem = new EmprestimoItem(livros.get(i), emprestimoFeito);
                        emprestimoItemDAO.inserir(emprestimoItem);
                    }
                    return emprestimoFeito;
                } else {
                    System.out.println("Nenhum livro disponível para empréstimo");
                    return new Emprestimo(a.RA);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new Emprestimo(0);
    }
}
