package src;

public class ConfiguracoesGlobais {
    private static ConfiguracoesGlobais instancia;

    private int emprestimoMaxLivros;
    private Float multaValorDiario;

    private ConfiguracoesGlobais() {
        this.emprestimoMaxLivros = 5;
        this.multaValorDiario = 4.0F;
    }

    public static ConfiguracoesGlobais getInstancia() {
        if (instancia == null) {
            synchronized (ConfiguracoesGlobais.class) {
                if (instancia == null) {
                    instancia = new ConfiguracoesGlobais();
                }
            }
        }
        return instancia;
    }

    public int getEmprestimoMaxLivros() {
        return emprestimoMaxLivros;
    }

    public void setEmprestimoMaxLivros(int valor) {
        this.emprestimoMaxLivros = valor;
    }

    public Float getmultaValorDiario() {
        return multaValorDiario;
    }

    public void semultaValorDiario(Float valor) {
        this.multaValorDiario = valor;
    }
}
