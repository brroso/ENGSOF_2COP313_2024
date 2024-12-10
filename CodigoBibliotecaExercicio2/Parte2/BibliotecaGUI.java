package src;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private JTextField raField, numLivrosField;
    private JTextField[] codigoLivrosFields;
    private JPanel codigosPanel;
    private JTextArea resultadoArea;
    private JTable tabelaAlunos = new JTable();
    private AutorController autorController = new AutorController();
    private AreaController areaController = new AreaController();
    private AlunoController alunoController = new AlunoController();
    private EmprestimoController emprestimoController = new EmprestimoController();



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
                adicionarCamposCodigos();
            }
        });

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        painelEmprestar.add(new JScrollPane(resultadoArea), BorderLayout.EAST);

        JButton emprestarButton = new JButton("Emprestar Livros");
        emprestarButton.addActionListener(e -> emprestarLivros());
        painelEmprestar.add(emprestarButton, BorderLayout.SOUTH);

        return painelEmprestar;
    }

    private JPanel criarPainelGerenciarAutores() {
        JPanel painelGerenciar = new JPanel(new GridLayout(1, 2)); // Divide o painel em duas colunas

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
            String raText = raField.getText().trim();
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();

            if (raText.isEmpty() || nome.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.");
            } else {
                try {
                    int ra = Integer.parseInt(raText);
                    alunoController.cadastrarAluno(nome, email);
                    JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
                    atualizarTabelaAlunos(this.tabelaAlunos);
                    raField.setText("");
                    nomeField.setText("");
                    emailField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "RA deve ser um número válido.");
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
                    alunoController.excluirAlunoByRA(ra);
                    JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
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
            dados[i][4] = String.valueOf(livrosEmprestados.item.get(i).getDataDevolucao());
        }

        JTable tabela = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("RA do Aluno: " + aluno), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Livros Emprestados", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BibliotecaGUI().setVisible(true));
    }
}
