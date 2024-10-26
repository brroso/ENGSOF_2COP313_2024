package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class BibliotecaGUI extends JFrame {

    private JTextField raField;
    private JTextField numLivrosField;
    private JTextField[] codigoLivrosFields;
    private JPanel codigosPanel;
    private JButton emprestarButton;
    private JTextArea resultadoArea;

    public BibliotecaGUI() {
        setTitle("Emprestar Livros");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior para RA do Aluno e Número de Livros
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));

        topPanel.add(new JLabel("Digite o RA do Aluno:"));
        raField = new JTextField();
        topPanel.add(raField);

        topPanel.add(new JLabel("Digite o número de Livros a ser Emprestado:"));
        numLivrosField = new JTextField();
        topPanel.add(numLivrosField);

        add(topPanel, BorderLayout.NORTH);

        // Painel para inserir códigos dos livros
        codigosPanel = new JPanel();
        codigosPanel.setLayout(new GridLayout(0, 1));
        add(new JScrollPane(codigosPanel), BorderLayout.CENTER);

        // Adiciona evento de atualização ao perder foco do numLivrosField
        numLivrosField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                adicionarCamposCodigos();
            }
        });

        // Área para exibir informações dos livros emprestados
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        add(new JScrollPane(resultadoArea), BorderLayout.EAST);

        // Botão para emprestar livros
        emprestarButton = new JButton("Emprestar Livros");
        emprestarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emprestarLivros();
            }
        });
        add(emprestarButton, BorderLayout.SOUTH);
    }

    private void adicionarCamposCodigos() {
        codigosPanel.removeAll();
        try {
            int num = Integer.parseInt(numLivrosField.getText());
            codigoLivrosFields = new JTextField[num];

            for (int i = 0; i < num; i++) {
                JLabel label = new JLabel("Digite o código do livro " + (i + 1) + ": ");
                JTextField textField = new JTextField();
                codigoLivrosFields[i] = textField;
                codigosPanel.add(label);
                codigosPanel.add(textField);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite um número válido para a quantidade de livros.");
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

        Controle c = new Controle();
        Emprestimo livrosEmprestados = c.emprestar(aluno, codigos, num);

        // Preparar dados para exibição em tabela
        String[] colunas = {"Código", "Título do Livro", "Área", "Autor", "Data de Devolução"};
        String[][] dados = new String[livrosEmprestados.item.size()][5];

        for (int i = 0; i < livrosEmprestados.item.size(); i++) {
            dados[i][0] = String.valueOf(livrosEmprestados.item.get(i).livro.codigoLivro);
            dados[i][1] = livrosEmprestados.item.get(i).livro.titulo.nome;
            dados[i][2] = livrosEmprestados.item.get(i).livro.titulo.area.nome;
            dados[i][3] = livrosEmprestados.item.get(i).livro.titulo.autor.nome;
            dados[i][4] = String.valueOf(livrosEmprestados.item.get(i).getDataDevolucao());
        }

        // Criar JTable para exibir as informações dos livros
        JTable tabela = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Exibir a tabela e RA do aluno em um JOptionPane
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("RA do Aluno: " + aluno), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Livros Emprestados", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BibliotecaGUI().setVisible(true);
            }
        });
    }
}
