package src;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private JTextField raField, numLivrosField;
    private JTextField[] codigoLivrosFields;
    private JPanel codigosPanel;
    private JTable tabelaAlunos = new JTable();
    private AutorController autorController = new AutorController();
    private AreaController areaController = new AreaController();
    private AlunoController alunoController = new AlunoController();
    private TituloController tituloController = new TituloController();
    private LivroController livroController = new LivroController();
    private EmprestimoController emprestimoController = new EmprestimoController();
    private ConfiguracoesGlobais conf = ConfiguracoesGlobais.getInstancia();



    public BibliotecaGUI() {
        setTitle("Sistema de Biblioteca");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        atualizarTabelaAlunos(this.tabelaAlunos);

        // Configuração do JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba Emprestar Livros
        tabbedPane.addTab("Emprestar Livros", criarPainelEmprestarLivros());

        // Aba Gerenciar Alunos
        tabbedPane.addTab("Gerenciar Alunos", criarPainelGerenciarAlunos());

        // Aba Gerenciar Autores
        tabbedPane.addTab("Gerenciar Autores", criarPainelGerenciarAutores());

        // Aba Gerenciar Livros
        tabbedPane.addTab("Gerenciar Livros", criarPainelGerenciarLivrosETitulos());

        // Aba Devolver Emprestimo
        tabbedPane.addTab("Devolver Livro", criarPainelGerenciarEmprestimos());

        add(tabbedPane, BorderLayout.CENTER);

    }

    private JPanel criarPainelEmprestarLivros() {
        JPanel painelEmprestar = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Digite o RA do Aluno:"));
        raField = new JTextField();
        topPanel.add(raField);

        topPanel.add(new JLabel("Digite o número de Livros a ser Emprestado:"));
        numLivrosField = new JTextField();
        topPanel.add(numLivrosField);

        painelEmprestar.add(topPanel, BorderLayout.NORTH);

        codigosPanel = new JPanel(new GridLayout(0, 1));
        painelEmprestar.add(new JScrollPane(codigosPanel), BorderLayout.CENTER);

        numLivrosField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                try {
                    int numLivros = Integer.parseInt(numLivrosField.getText());
                    if (numLivros > conf.getEmprestimoMaxLivros()) {
                        JOptionPane.showMessageDialog(painelEmprestar,
                                "O número máximo de livros permitido é " + conf.getEmprestimoMaxLivros() + ".",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        numLivrosField.setText(""); // Limpa o campo para corrigir
                    } else {
                        adicionarCamposCodigos();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(painelEmprestar,
                            "Por favor, insira um número válido.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    numLivrosField.setText("");
                }
            }
        });

        JPanel botaoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton emprestarButton = new JButton("Emprestar Livros");
        emprestarButton.addActionListener(e -> emprestarLivros());
        botaoPanel.add(emprestarButton);

        painelEmprestar.add(botaoPanel, BorderLayout.PAGE_END);

        painelEmprestar.revalidate();
        painelEmprestar.repaint();

        return painelEmprestar;
    }

    private JPanel criarPainelGerenciarEmprestimos() {
        JPanel painelGerenciarEmprestimos = new JPanel(new BorderLayout());

        JTable tabelaEmprestimos = new JTable();
        preencherTabelaEmprestimos(tabelaEmprestimos);

        JScrollPane scrollPane = new JScrollPane(tabelaEmprestimos);
        painelGerenciarEmprestimos.add(scrollPane, BorderLayout.CENTER);

        JPanel painelExclusao = new JPanel(new GridLayout(1, 3));
        painelExclusao.add(new JLabel("ID do Empréstimo a excluir:"));
        JTextField idExcluirField = new JTextField();
        painelExclusao.add(idExcluirField);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(e -> {
            String idText = idExcluirField.getText();
            if (!idText.isEmpty()) {
                try {
                    int id = Integer.parseInt(idText);
                    emprestimoController.finalizaEmprestimo(id);
                    preencherTabelaEmprestimos(tabelaEmprestimos);
                    idExcluirField.setText("");
                    JOptionPane.showMessageDialog(painelGerenciarEmprestimos, "Empréstimo excluído com sucesso!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(painelGerenciarEmprestimos, "Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(painelGerenciarEmprestimos, "Erro ao excluir empréstimo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(painelGerenciarEmprestimos, "O campo de ID está vazio.", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });
        painelExclusao.add(excluirButton);

        painelGerenciarEmprestimos.add(painelExclusao, BorderLayout.SOUTH);

        return painelGerenciarEmprestimos;
    }



    private void preencherTabelaEmprestimos(JTable tabelaEmprestimos) {
        List<Emprestimo> emprestimos = emprestimoController.getEmprestimos();

        String[] colunas = {"ID", "RA do Aluno", "Data de Empréstimo", "Data de Devolução", "Livro Emprestado"};

        int numLinhas = 0;
        for (Emprestimo emprestimo : emprestimos) {
            numLinhas += emprestimo.getItem().size();
        }

        String[][] dados = new String[numLinhas][5];
        int count = 0;

        for (Emprestimo emprestimo : emprestimos) {
            for (EmprestimoItem item : emprestimo.getItem()) {
                dados[count][0] = String.valueOf(emprestimo.getId());
                dados[count][1] = String.valueOf(emprestimo.getRAAluno());
                dados[count][2] = emprestimo.getDataEmprestimo().toString();
                dados[count][3] = emprestimo.getDataPrevista().toString();
                dados[count][4] = item.getLivro().getTitulo().getNome();
                count++;
            }
        }

        tabelaEmprestimos.setModel(new javax.swing.table.DefaultTableModel(dados, colunas));
        tabelaEmprestimos.revalidate();
        tabelaEmprestimos.repaint();
    }


    private JPanel criarPainelGerenciarAutores() {
        JPanel painelGerenciar = new JPanel(new GridLayout(1, 2));

        JPanel painelAreas = new JPanel(new BorderLayout());
        painelAreas.add(new JLabel("Gerenciar Áreas"), BorderLayout.NORTH);

        JPanel formAreas = new JPanel(new GridLayout(2, 2));
        JTextField idAreaField = new JTextField();
        JTextField nomeAreaField = new JTextField();

        formAreas.add(new JLabel("Id Área (Exclusão):"));
        formAreas.add(idAreaField);
        formAreas.add(new JLabel("Nome Área:"));
        formAreas.add(nomeAreaField);
        painelAreas.add(formAreas, BorderLayout.CENTER);

        JList<String> listaAreas = new JList<>(getAreas()); // Método para buscar áreas do banco
        JScrollPane scrollAreas = new JScrollPane(listaAreas);
        painelAreas.add(scrollAreas, BorderLayout.SOUTH);

        JButton criarAreaButton = new JButton("Criar Área");
        criarAreaButton.addActionListener(e -> {
            areaController.cadastrarArea(nomeAreaField.getText());
            atualizarListaAreas(listaAreas);
        });

        JButton excluirAreaButton = new JButton("Excluir Área");
        excluirAreaButton.addActionListener(e -> {
            areaController.excluirAreaById(Integer.parseInt(idAreaField.getText()));
            atualizarListaAreas(listaAreas);
        });

        JPanel botoesArea = new JPanel(new GridLayout(1, 2));
        botoesArea.add(criarAreaButton);
        botoesArea.add(excluirAreaButton);
        painelAreas.add(botoesArea, BorderLayout.NORTH);
        painelGerenciar.add(painelAreas);

        JPanel painelAutores = new JPanel(new BorderLayout());
        painelAutores.add(new JLabel("Gerenciar Autores"), BorderLayout.NORTH);

        JPanel formAutores = new JPanel(new GridLayout(2, 2));
        JTextField idAutorField = new JTextField();
        JTextField nomeAutorField = new JTextField();

        formAutores.add(new JLabel("ID Autor (Exclusão):"));
        formAutores.add(idAutorField);
        formAutores.add(new JLabel("Nome Autor:"));
        formAutores.add(nomeAutorField);
        painelAutores.add(formAutores, BorderLayout.CENTER);

        JList<String> listaAutores = new JList<>(getAutores()); // Método para buscar autores do banco
        JScrollPane scrollAutores = new JScrollPane(listaAutores);
        painelAutores.add(scrollAutores, BorderLayout.SOUTH);

        JButton criarAutorButton = new JButton("Criar Autor");
        criarAutorButton.addActionListener(e -> {
            autorController.cadastrarAutor(nomeAutorField.getText());
            atualizarListaAutores(listaAutores);
        });

        JButton excluirAutorButton = new JButton("Excluir Autor");
        excluirAutorButton.addActionListener(e -> {
            autorController.excluirAutorById(Integer.parseInt(idAutorField.getText()));
            atualizarListaAutores(listaAutores);
        });

        JPanel botoesAutor = new JPanel(new GridLayout(1, 2));
        botoesAutor.add(criarAutorButton);
        botoesAutor.add(excluirAutorButton);
        painelAutores.add(botoesAutor, BorderLayout.NORTH);
        painelGerenciar.add(painelAutores);

        return painelGerenciar;
    }

    private String[] getAreas() {
        List<Area> areas = areaController.getAreas();
        String[] areasArray = new String[areas.size()];

        for (int i = 0; i < areas.size(); i++) {
            Area area = areas.get(i);
            areasArray[i] = area.getId() + " - " + area.getNome(); // Formatando como "id - nome"
        }
        return areasArray;
    }

    private void atualizarListaAreas(JList<String> listaAreas) {
        listaAreas.setListData(getAreas());
    }

    private String[] getAutores() {
        List<Autor> autores = null;
        autores = autorController.getAutores();
        String[] autoresArray = new String[autores.size()];

        for (int i = 0; i < autores.size(); i++) {
            Autor autor = autores.get(i);
            autoresArray[i] = autor.getId() + " - " + autor.getNome();
        }
        return autoresArray;
    }

    private void atualizarListaAutores(JList<String> listaAutores) {
        listaAutores.setListData(getAutores());
    }

    private JPanel criarPainelGerenciarAlunos() {
        JPanel painelGerenciar = new JPanel(new BorderLayout());

        JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField raField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JButton cadastrarButton = new JButton("Cadastrar");
        JButton excluirButton = new JButton("Excluir");

        painelForm.add(new JLabel("Nome:"));
        painelForm.add(nomeField);
        painelForm.add(new JLabel("Email:"));
        painelForm.add(emailField);
        painelForm.add(new JLabel("RA (Apenas para exclusão):"));
        painelForm.add(raField);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoes.add(cadastrarButton);
        painelBotoes.add(excluirButton);

        painelForm.add(painelBotoes);

        JScrollPane scrollPane = new JScrollPane(this.tabelaAlunos);

        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();

            if (nome.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.");
            } else {
                try {
                    alunoController.cadastrarAluno(nome, email);
                    JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
                    atualizarTabelaAlunos(this.tabelaAlunos);
                    nomeField.setText("");
                    emailField.setText("");
                } catch (HeadlessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        excluirButton.addActionListener(e -> {
            String raText = raField.getText().trim();
            if (raText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o RA do aluno para excluir.");
            } else {
                try {
                    int ra = Integer.parseInt(raText);
                    if (alunoController.excluirAlunoByRA(ra)) {
                        JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Não foi possível excluir o Aluno.");
                    }
                    atualizarTabelaAlunos(this.tabelaAlunos);
                    raField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "RA deve ser um número válido.");
                }
            }
        });

        painelGerenciar.add(painelForm, BorderLayout.NORTH);
        painelGerenciar.add(scrollPane, BorderLayout.CENTER);

        atualizarTabelaAlunos(this.tabelaAlunos);

            return painelGerenciar;
    }

    private void atualizarTabelaAlunos(JTable tabela) {
        List<Aluno> alunos = alunoController.getAlunos();

        String[] colunas = {"RA", "Nome", "Email"};
        String[][] dados = new String[alunos.size()][3];
        for (int i = 0; i < alunos.size(); i++) {
            dados[i][0] = String.valueOf(alunos.get(i).getRA());
            dados[i][1] = alunos.get(i).getNome();
            dados[i][2] = alunos.get(i).getEmail();
        }

        tabela.setModel(new javax.swing.table.DefaultTableModel(dados, colunas));
    }

    private void adicionarCamposCodigos() {
        codigosPanel.removeAll();
        try {
            int num = Integer.parseInt(numLivrosField.getText().trim());
            codigoLivrosFields = new JTextField[num];

            for (int i = 0; i < num; i++) {
                JLabel label = new JLabel("Digite o código do livro " + (i + 1) + ": ");
                JTextField textField = new JTextField();
                codigoLivrosFields[i] = textField;
                codigosPanel.add(label);
                codigosPanel.add(textField);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número de livros inválido.");
        }

        codigosPanel.revalidate();
        codigosPanel.repaint();
    }

    private void emprestarLivros() {
        String aluno = raField.getText();
        int num;
        try {
            num = Integer.parseInt(numLivrosField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número de livros inválido.");
            return;
        }

        int[] codigos = new int[num];
        for (int i = 0; i < num; i++) {
            try {
                codigos[i] = Integer.parseInt(codigoLivrosFields[i].getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Código inválido no campo " + (i + 1) + ".");
                return;
            }
        }

        Emprestimo livrosEmprestados = emprestimoController.emprestar(Integer.parseInt(aluno), codigos, num);

        String[] colunas = {"Código", "Título do Livro", "Área", "Autor", "Data de Devolução"};
        String[][] dados = new String[livrosEmprestados.item.size()][5];

        for (int i = 0; i < livrosEmprestados.item.size(); i++) {
            dados[i][0] = String.valueOf(livrosEmprestados.item.get(i).livro.id);
            dados[i][1] = livrosEmprestados.item.get(i).livro.titulo.nome;
            dados[i][2] = livrosEmprestados.item.get(i).livro.titulo.area.nome;
            dados[i][3] = livrosEmprestados.item.get(i).livro.titulo.autor.nome;
            dados[i][4] = String.valueOf(livrosEmprestados.getDataPrevista());
        }

        JTable tabela = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("RA do Aluno: " + aluno), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Livros Emprestados", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel criarPainelGerenciarLivrosETitulos() {
        JPanel painelGerenciar = new JPanel(new GridLayout(1, 2));

        // Painel de Títulos
        JPanel painelTitulos = new JPanel(new BorderLayout());
        painelTitulos.add(new JLabel("Gerenciar Títulos"), BorderLayout.NORTH);

        JPanel formTitulos = new JPanel(new GridLayout(5, 2));
        JTextField idTituloField = new JTextField();
        JTextField nomeTituloField = new JTextField();
        JTextField areaIdField = new JTextField();
        JTextField prazoField = new JTextField();
        JTextField autorIdField = new JTextField();

        formTitulos.add(new JLabel("ID Título (Exclusão):"));
        formTitulos.add(idTituloField);
        formTitulos.add(new JLabel("Nome do Título:"));
        formTitulos.add(nomeTituloField);
        formTitulos.add(new JLabel("ID Área:"));
        formTitulos.add(areaIdField);
        formTitulos.add(new JLabel("Prazo:"));
        formTitulos.add(prazoField);
        formTitulos.add(new JLabel("ID Autor:"));
        formTitulos.add(autorIdField);
        painelTitulos.add(formTitulos, BorderLayout.CENTER);

        JList<String> listaTitulos = new JList<>(getTitulos()); // Método para buscar títulos do banco
        JScrollPane scrollTitulos = new JScrollPane(listaTitulos);
        painelTitulos.add(scrollTitulos, BorderLayout.SOUTH);

        JButton criarTituloButton = new JButton("Criar Título");
        criarTituloButton.addActionListener(e -> {
            tituloController.cadastrarTitulo(Integer.parseInt(prazoField.getText()), nomeTituloField.getText(), Integer.parseInt(autorIdField.getText()), Integer.parseInt(areaIdField.getText()));
            atualizarListaTitulos(listaTitulos);
        });

        JButton excluirTituloButton = new JButton("Excluir Título");
        excluirTituloButton.addActionListener(e -> {
            tituloController.excluirTituloById(Integer.parseInt(idTituloField.getText()));
            atualizarListaTitulos(listaTitulos);
        });

        JPanel botoesTitulo = new JPanel(new GridLayout(1, 2));
        botoesTitulo.add(criarTituloButton);
        botoesTitulo.add(excluirTituloButton);
        painelTitulos.add(botoesTitulo, BorderLayout.NORTH);
        painelGerenciar.add(painelTitulos);

        // Painel de Livros
        JPanel painelLivros = new JPanel(new BorderLayout());
        painelLivros.add(new JLabel("Gerenciar Livros"), BorderLayout.NORTH);

        JPanel formLivros = new JPanel(new GridLayout(3, 2));
        JTextField idLivroField = new JTextField();
        JTextField exemplarBibliotecaField = new JTextField(); // Campo booleano como texto
        JTextField idTituloLivroField = new JTextField();

        formLivros.add(new JLabel("ID Livro (Exclusão):"));
        formLivros.add(idLivroField);
        formLivros.add(new JLabel("Exemplar Biblioteca (true/false):"));
        formLivros.add(exemplarBibliotecaField);
        formLivros.add(new JLabel("ID Título:"));
        formLivros.add(idTituloLivroField);
        painelLivros.add(formLivros, BorderLayout.CENTER);

        JList<String> listaLivros = new JList<>(getLivros()); // Método para buscar livros do banco
        JScrollPane scrollLivros = new JScrollPane(listaLivros);
        painelLivros.add(scrollLivros, BorderLayout.SOUTH);

        JButton criarLivroButton = new JButton("Criar Livro");
        criarLivroButton.addActionListener(e -> {
            boolean exemplarBiblioteca = Boolean.parseBoolean(exemplarBibliotecaField.getText()); // Convertendo o texto para boolean
            livroController.cadastrarLivro(tituloController.getTituloById(Integer.parseInt(idTituloLivroField.getText())), exemplarBiblioteca);
            atualizarListaLivros(listaLivros);
        });

        JButton excluirLivroButton = new JButton("Excluir Livro");
        excluirLivroButton.addActionListener(e -> {
            livroController.excluirLivroById(Integer.parseInt(idLivroField.getText()));
            atualizarListaLivros(listaLivros);
        });

        JPanel botoesLivro = new JPanel(new GridLayout(1, 2));
        botoesLivro.add(criarLivroButton);
        botoesLivro.add(excluirLivroButton);
        painelLivros.add(botoesLivro, BorderLayout.NORTH);
        painelGerenciar.add(painelLivros);

        return painelGerenciar;
    }


    private String[] getTitulos() {
        List<Titulo> titulos = tituloController.getTitulos();
        String[] areasArray = new String[titulos.size()];

        for (int i = 0; i < titulos.size(); i++) {
            Titulo titulo = titulos.get(i);
            areasArray[i] = titulo.getId() + " - " + titulo.getNome() + " - " + titulo.getPrazo() + " - " + titulo.getArea().getNome() + " - " + titulo.getAutor().getNome();
        }
        return areasArray;
    }

    private void atualizarListaTitulos(JList<String> listaLivros) {
        listaLivros.setListData(getTitulos());
    }

    private String[] getLivros() {
        List<Livro> livros = null;
        livros = livroController.getLivros();
        String[] autoresArray = new String[livros.size()];

        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            autoresArray[i] = livro.getId() + " - " + livro.getTitulo().getNome() + " - " + livro.getExemplarBiblioteca();
        }
        return autoresArray;
    }

    private void atualizarListaLivros(JList<String> listaLivros) {
        listaLivros.setListData(getLivros());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BibliotecaGUI().setVisible(true));
    }
}
