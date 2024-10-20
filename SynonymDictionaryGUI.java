
package wordsynonymdictionary;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SynonymDictionaryGUI {
    private SynonymDictionary dictionary;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField wordField;
    private JTextField synonymField;
    private JTextField searchField;

    public SynonymDictionaryGUI() {
        dictionary = new SynonymDictionary();
        dictionary.loadFromFile("synonyms.txt"); // Load from file on startup
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Synonym Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        // Create table
        String[] columnNames = {"Word", "Synonyms"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadTableData(); // Load data into the table

        // Make table scrollable
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components

        // Word input
        JLabel wordLabel = new JLabel("Word:");
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(wordLabel, gbc);

        wordField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(wordField, gbc);

        // Synonyms input
        JLabel synonymLabel = new JLabel("Synonyms (comma-separated):");
        synonymLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(synonymLabel, gbc);

        synonymField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(synonymField, gbc);

        // Search functionality
        JLabel searchLabel = new JLabel("Search Word:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(searchLabel, gbc);

        searchField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(searchField, gbc);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchButton.setBackground(Color.GRAY);
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> searchWord());
        gbc.gridx = 2;
        inputPanel.add(searchButton, gbc);

        // Insert and Delete buttons
        JButton insertButton = new JButton("Insert/Update");
        insertButton.setFont(new Font("Arial", Font.PLAIN, 16));
        insertButton.setBackground(Color.GRAY);
        insertButton.setForeground(Color.WHITE);
        insertButton.addActionListener(e -> insertWord());
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(insertButton, gbc);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteButton.setBackground(Color.GRAY);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteWord());
        gbc.gridx = 1;
        inputPanel.add(deleteButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exitButton.setBackground(Color.GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            dictionary.saveToFile("synonyms.txt"); // Save on exit
            System.exit(0);
        });
        frame.add(exitButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // Clear existing data
        Object[][] entries = dictionary.getAllEntries();
        for (Object[] entry : entries) {
            tableModel.addRow(entry);
        }
    }

    private void insertWord() {
        String word = wordField.getText().trim();
        List<String> synonyms = Arrays.asList(synonymField.getText().split(","));
        if (!word.isEmpty() && !synonyms.isEmpty()) {
            dictionary.insertWord(word, synonyms);
            loadTableData(); // Refresh table
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid word and synonyms.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String word = (String) tableModel.getValueAt(selectedRow, 0);
            dictionary.deleteWord(word);
            loadTableData(); // Refresh table
        } else {
            JOptionPane.showMessageDialog(null, "Please select a word to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchWord() {
        String searchTerm = searchField.getText().trim();
        List<String> synonyms = dictionary.searchWord(searchTerm);
        if (synonyms != null) {
            JOptionPane.showMessageDialog(null, "Synonyms for '" + searchTerm + "': " + String.join(", ", synonyms), "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Word not found.", "Search Result", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInputFields() {
        wordField.setText("");
        synonymField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SynonymDictionaryGUI::new);
    }
}






