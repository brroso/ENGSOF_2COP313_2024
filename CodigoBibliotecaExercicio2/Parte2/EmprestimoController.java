package src;

import java.util.ArrayList;
import java.util.List;

public class EmprestimoController {
    private LivroController livroController = new LivroController();
    private AlunoController alunoController = new AlunoController();
    private DebitoController debitoController = new DebitoController();

    public void inserir(Emprestimo emprestimo) {
    }

    public Emprestimo emprestar(int aluno, int[] codigos, int num) {
        boolean retorno = true;
        try {
            Aluno a = alunoController.getAlunoByRA(aluno);
            if (a == null) {
                System.out.println("Aluno não encontrado");
                return new Emprestimo(0); // Retorna um empréstimo vazio
            }

            System.out.println("Aluno encontrado: " + a.getNome());

            if (debitoController.verificaDebitoByRA(a.getRA())) {
                System.out.println("Aluno em Débito");
                retorno = false;
            }

            if (retorno) {
                Emprestimo e = new Emprestimo(a.RA);
                List<Livro> livros = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    Livro l = livroController.getLivroById(codigos[i]);
                    if (!l.verificaLivro()) {
                        livros.add(l);
                    }
                }

                if (!livros.isEmpty()) {
                    Emprestimo emprestimoFeito = e.emprestar(livros);
                    this.inserir(emprestimoFeito);
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
