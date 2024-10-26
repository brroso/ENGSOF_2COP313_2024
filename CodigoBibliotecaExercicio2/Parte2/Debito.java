package src;

public class Debito {
    int codigoAluno;

    public Debito(int aluno) {
        this.codigoAluno = aluno;
    }

    public boolean verificaDebito() {
        //codigo aleatorio para definir se o aluno tem d�bito
        //� necess�rio fazer a verifica��o de forma persistente
        return this.codigoAluno != 4;
    }

}