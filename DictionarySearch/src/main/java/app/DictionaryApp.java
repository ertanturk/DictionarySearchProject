package main.java.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.java.loader.Loader;
import main.java.search.BinarySearch;
import main.java.search.LinearSearch;
import main.java.utils.ArrayList;
import main.java.utils.HashTable;
import main.java.utils.analysis.ExecutionTimeFormatter;
import main.java.utils.features.WordSuggester;

public class DictionaryApp extends JFrame {
  private HashTable<String, String> hashTable;
  private String[] dictionaryKeys;
  private WordSuggester wordSuggester;
  private ExecutionTimeFormatter timeFormatter;
  private BinarySearch<String> binarySearch;
  private LinearSearch<String> linearSearch;

  // Custom fonts
  private Font geistMonoRegular;
  private Font geistMonoBold;

  // UI Components
  private JTextField searchField;
  private JButton searchButton;
  private JTextArea definitionArea;
  private JLabel executionTimeLabel;
  private JPanel suggestionsPanel;
  private JComboBox<String> searchMethodCombo;

  public DictionaryApp() {
    loadCustomFonts();
    initializeData();
    initializeUI();
  }

  private void loadCustomFonts() {
    try {
      // Load Geist Mono Regular
      Font geistRegular = Font.createFont(Font.TRUETYPE_FONT,
          new File("DictionarySearch/fonts/GeistMono-Regular.ttf"));
      geistMonoRegular = geistRegular.deriveFont(Font.PLAIN, 15f);

      // Load Geist Mono Bold
      Font geistBold = Font.createFont(Font.TRUETYPE_FONT,
          new File("DictionarySearch/fonts/GeistMono-Bold.ttf"));
      geistMonoBold = geistBold.deriveFont(Font.BOLD, 16f);

      // Register fonts with GraphicsEnvironment
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(geistRegular);
      ge.registerFont(geistBold);

      System.out.println("Custom fonts loaded successfully");
    } catch (FontFormatException | IOException e) {
      System.err.println("Error loading custom fonts: " + e.getMessage());
      // Fallback to default fonts
      geistMonoRegular = new Font("Monospaced", Font.PLAIN, 14);
      geistMonoBold = new Font("Monospaced", Font.BOLD, 14);
    }
  }

  private void initializeData() {
    try {
      // Initialize time formatter
      timeFormatter = new ExecutionTimeFormatter(4);

      // Initialize search algorithms
      binarySearch = new BinarySearch<>();
      linearSearch = new LinearSearch<>();

      // Load dictionary into HashTable
      Loader<String, String> loader = new Loader<>("DictionarySearch/data/dict.csv");
      hashTable = loader.load();

      // Get all keys from HashTable and store in String[] array
      dictionaryKeys = hashTable.getKeys();

      // Sort the keys array for binary search
      binarySearch.sort(dictionaryKeys, 0, dictionaryKeys.length - 1);

      // Initialize WordSuggester with the HashTable
      wordSuggester = new WordSuggester(hashTable);

      System.out.println("Dictionary loaded successfully with " + dictionaryKeys.length + " words");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Error loading dictionary: " + e.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

  private void initializeUI() {
    setTitle("Dictionary Search Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(700, 600);
    setLocationRelativeTo(null);

    // Main panel
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    mainPanel.setBackground(new Color(240, 248, 255));

    // Top panel - Search section
    JPanel topPanel = createSearchPanel();
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // Center panel - Definition and Suggested Words
    JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
    centerPanel.setOpaque(false);

    JPanel definitionPanel = createDefinitionPanel();
    JPanel suggestedWordsPanel = createSuggestedWordsPanel();

    centerPanel.add(definitionPanel);
    centerPanel.add(suggestedWordsPanel);

    mainPanel.add(centerPanel, BorderLayout.CENTER);

    // Bottom panel - Execution time
    JPanel bottomPanel = createBottomPanel();
    mainPanel.add(bottomPanel, BorderLayout.SOUTH);

    add(mainPanel);

    // Add enter key listener to search field
    searchField.addActionListener(e -> performSearch());
  }

  private JPanel createSearchPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setOpaque(false);

    // Title
    JLabel titleLabel = new JLabel("Dictionary");
    titleLabel.setFont(geistMonoBold.deriveFont(24f));
    titleLabel.setForeground(new Color(25, 25, 112));

    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    titlePanel.setOpaque(false);
    titlePanel.add(titleLabel);

    panel.add(titlePanel, BorderLayout.NORTH);

    // Search input panel
    JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
    searchPanel.setOpaque(false);

    searchField = new JTextField();
    searchField.setFont(geistMonoRegular.deriveFont(16f));
    searchField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
        new EmptyBorder(8, 10, 8, 10)));

    // Search method combo box
    searchMethodCombo = new JComboBox<>(new String[] { "Binary Search", "Linear Search" });
    searchMethodCombo.setFont(geistMonoRegular);
    searchMethodCombo.setPreferredSize(new Dimension(150, 40));

    searchButton = new JButton("Search");
    searchButton.setFont(geistMonoBold);
    searchButton.setBackground(new Color(100, 149, 237));
    searchButton.setForeground(Color.WHITE);
    searchButton.setFocusPainted(false);
    searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    searchButton.setPreferredSize(new Dimension(100, 40));
    searchButton.addActionListener(e -> performSearch());

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    buttonPanel.setOpaque(false);
    buttonPanel.add(searchMethodCombo);
    buttonPanel.add(searchButton);

    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.add(buttonPanel, BorderLayout.EAST);

    panel.add(searchPanel, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createDefinitionPanel() {
    JPanel panel = new JPanel(new BorderLayout(5, 5));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
        new TitledBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            "Definition:",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            geistMonoBold,
            new Color(25, 25, 112)),
        new EmptyBorder(10, 10, 10, 10)));

    definitionArea = new JTextArea();
    definitionArea.setFont(geistMonoRegular);
    definitionArea.setLineWrap(true);
    definitionArea.setWrapStyleWord(true);
    definitionArea.setEditable(false);
    definitionArea.setBackground(new Color(255, 250, 240));
    definitionArea.setText("Search for a word to see its definition...");

    JScrollPane scrollPane = new JScrollPane(definitionArea);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createSuggestedWordsPanel() {
    JPanel panel = new JPanel(new BorderLayout(5, 5));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
        new TitledBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            "Suggested Words:",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            geistMonoBold,
            new Color(25, 25, 112)),
        new EmptyBorder(10, 10, 10, 10)));

    suggestionsPanel = new JPanel();
    suggestionsPanel.setLayout(new BoxLayout(suggestionsPanel, BoxLayout.Y_AXIS));
    suggestionsPanel.setBackground(new Color(255, 250, 240));

    JScrollPane scrollPane = new JScrollPane(suggestionsPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());

    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createBottomPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    panel.setBorder(new EmptyBorder(10, 0, 0, 0));

    executionTimeLabel = new JLabel("Execution Time: Ready");
    executionTimeLabel.setFont(geistMonoBold);
    executionTimeLabel.setForeground(new Color(25, 25, 112));

    panel.add(executionTimeLabel, BorderLayout.WEST);

    return panel;
  }

  private void performSearch() {
    String searchWord = searchField.getText().trim().toLowerCase();

    if (searchWord.isEmpty()) {
      JOptionPane.showMessageDialog(this,
          "Please enter a word to search",
          "Input Required",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Get selected search method
    boolean useBinarySearch = searchMethodCombo.getSelectedIndex() == 0;
    String methodName = (String) searchMethodCombo.getSelectedItem();

    // Perform search on the keys array with time measurement
    long startTime = System.nanoTime();
    int foundIndex = -1;

    if (useBinarySearch) {
      // Binary Search on dictionaryKeys array
      foundIndex = binarySearch.search(dictionaryKeys, searchWord);
    } else {
      // Linear Search on dictionaryKeys array
      foundIndex = linearSearch.search(dictionaryKeys, searchWord);
    }

    long endTime = System.nanoTime();

    // Calculate execution time
    long executionTimeNanos = endTime - startTime;
    double executionTimeMillis = executionTimeNanos / 1_000_000.0;
    String formattedTime = timeFormatter.formatMilliseconds(executionTimeMillis);
    executionTimeLabel.setText(String.format("Execution Time: %s (%s)", formattedTime, methodName));

    // If word exists, get definition from HashTable
    if (foundIndex != -1) {
      String definition = hashTable.get(searchWord);
      displayDefinition(searchWord, definition);
      // Clear suggestions panel when word is found
      clearSuggestions();
    } else {
      definitionArea.setText("Word not found in dictionary.");
      // Only display suggested words when word is NOT found
      displaySuggestions(searchWord);
    }
  }

  private void displayDefinition(String word, String definition) {
    StringBuilder sb = new StringBuilder();
    sb.append("Word: ").append(word.toUpperCase()).append("\n\n");
    sb.append("Definition:\n");
    sb.append(definition);

    definitionArea.setText(sb.toString());
    definitionArea.setCaretPosition(0);
  }

  private void clearSuggestions() {
    suggestionsPanel.removeAll();
    suggestionsPanel.revalidate();
    suggestionsPanel.repaint();
  }

  private void displaySuggestions(String searchWord) {
    suggestionsPanel.removeAll();

    // Get suggestions using WordSuggester
    ArrayList<String> suggestions = wordSuggester.suggest(searchWord);

    if (suggestions != null && suggestions.size() > 0) {
      // Limit to 8 suggestions
      int maxSuggestions = Math.min(8, suggestions.size());

      for (int i = 0; i < maxSuggestions; i++) {
        String suggestion = suggestions.get(i);
        JButton suggestionButton = new JButton(suggestion);
        suggestionButton.setFont(geistMonoRegular.deriveFont(13f));
        suggestionButton.setBackground(new Color(173, 216, 230));
        suggestionButton.setForeground(new Color(25, 25, 112));
        suggestionButton.setFocusPainted(false);
        suggestionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        suggestionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        suggestionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        suggestionButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            new EmptyBorder(5, 10, 5, 10)));

        // Click handler to search for suggested word
        suggestionButton.addActionListener(e -> {
          searchField.setText(suggestion);
          performSearch();
        });

        suggestionsPanel.add(suggestionButton);
        suggestionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
      }
    } else {
      JLabel noSuggestionsLabel = new JLabel("No suggestions available");
      noSuggestionsLabel.setFont(geistMonoRegular.deriveFont(Font.ITALIC, 13f));
      noSuggestionsLabel.setForeground(Color.GRAY);
      suggestionsPanel.add(noSuggestionsLabel);
    }

    suggestionsPanel.revalidate();
    suggestionsPanel.repaint();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }

      DictionaryApp app = new DictionaryApp();
      app.setVisible(true);
    });
  }
}
