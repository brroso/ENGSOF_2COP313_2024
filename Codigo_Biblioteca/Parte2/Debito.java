package src;

public class Debito {
    int codigoAluno;
    Float valor;
    int id;

    public Debito(int aluno, Float valor) {
        this.codigoAluno = aluno;
        this.valor = valor;
    }

    public int getId() { return this.id; }

    public int getCodigoAluno() { return this.codigoAluno; }

    public Float getValor() { return this.valor; }

    public void setId(int id) { this.id = id; }

    public void setCodigoAluno(int codigoAluno) { this.codigoAluno = codigoAluno; }

    public void setValor(Float valor) { this.valor = valor; }
}