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
/*
 * import java.awt.BorderLayout;
 * import java.awt.Color;
 * import java.awt.Component;
 * import java.awt.Dimension;
 * import java.awt.FlowLayout;
 * import java.awt.Font;
 * import java.awt.Image;
 * import java.awt.Toolkit;
 * import java.awt.event.ActionEvent;
 * import java.awt.event.ActionListener;
 *
 * import javax.swing.BorderFactory;
 * import javax.swing.BoxLayout;
 * import javax.swing.ButtonGroup;
 * import javax.swing.JButton;
 * import javax.swing.JFrame;
 * import javax.swing.JLabel;
 * import javax.swing.JOptionPane;
 * import javax.swing.JPanel;
 * import javax.swing.JTextArea;
 * import javax.swing.JTextField;
 * import javax.swing.JToggleButton;
 * import javax.swing.SwingUtilities;
 * import javax.swing.border.EmptyBorder;
 * import javax.swing.border.LineBorder;
 *
 * public class DictionaryUI extends JFrame {
 *
 * // --- Components ---
 * private JToggleButton btnLinear, btnBinary, btnHash;
 * private ButtonGroup searchGroup; // For the one button selection
 * private JTextField txtSearch;
 * private JButton btnSearchAction;
 *
 * // Result Screen Components
 * private JPanel resultPanel;
 * private JLabel lblResultWord;
 * private JTextArea txtDefinition;
 * private JLabel lblExecutionTime;
 *
 * // --- COLORS ---
 * private final Color COLOR_DARK_BLUE = new Color(20, 45, 85);
 * private final Color COLOR_ACTIVE_BLUE = new Color(100, 180, 230); // Selected
 * button color
 * private final Color COLOR_YELLOW_BG = new Color(255, 245, 200);
 *
 * public DictionaryUI() {
 * // Window Settings
 * setTitle("Interactive Dictionary");
 * Image icon = Toolkit.getDefaultToolkit().getImage("dictionary.png");
 * setIconImage(icon);
 * setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * setSize(600, 700);
 * setLocationRelativeTo(null); // Open in the middle of the screen
 * setLayout(new BorderLayout());
 * getContentPane().setBackground(Color.WHITE);
 *
 * // Main Panel (Vertical alignment)
 * JPanel mainPanel = new JPanel();
 * mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
 * mainPanel.setBackground(Color.WHITE);
 * mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
 *
 * // 1. Title
 * JLabel lblTitle = new JLabel("Interactive Dictionary Interface");
 * lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
 * lblTitle.setForeground(COLOR_DARK_BLUE);
 * lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
 *
 * // 2. BUTONS (JToggleButton)
 * JPanel pnlSearchTypes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15,
 * 20));
 * pnlSearchTypes.setBackground(Color.WHITE);
 *
 * searchGroup = new ButtonGroup();
 * // Create Toggle Buttons
 * btnLinear = createToggleBtn("Linear Search");
 * btnBinary = createToggleBtn("Binary Search");
 * btnHash = createToggleBtn("Hash-Based Search");
 *
 * // Add Group (One selection only)
 * searchGroup.add(btnLinear);
 * searchGroup.add(btnBinary);
 * searchGroup.add(btnHash);
 *
 * pnlSearchTypes.add(btnLinear);
 * pnlSearchTypes.add(btnBinary);
 * pnlSearchTypes.add(btnHash);
 *
 * // Linear is selected by default
 * btnLinear.setSelected(true);
 * updateButtonColors(); // Update colors
 *
 * // 3. SEARCH INPUT FIELD
 * JPanel pnlInput = new JPanel(new BorderLayout(10, 0));
 * pnlInput.setMaximumSize(new Dimension(600, 45));
 * pnlInput.setBackground(Color.WHITE);
 *
 * txtSearch = new JTextField("Type a word...");
 * txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
 * txtSearch.setBorder(BorderFactory.createCompoundBorder(
 * new LineBorder(Color.LIGHT_GRAY, 1),
 * new EmptyBorder(5, 10, 5, 10)));
 *
 * btnSearchAction = new JButton("Search");
 * btnSearchAction.setBackground(COLOR_DARK_BLUE);
 * btnSearchAction.setForeground(Color.WHITE);
 * btnSearchAction.setFont(new Font("Segoe UI", Font.BOLD, 14));
 * btnSearchAction.setFocusPainted(false);
 * btnSearchAction.setPreferredSize(new Dimension(100, 40));
 *
 * pnlInput.add(txtSearch, BorderLayout.CENTER);
 * pnlInput.add(btnSearchAction, BorderLayout.EAST);
 *
 * // 4. Result Panel (Hide at start)
 * resultPanel = new JPanel(new BorderLayout());
 * resultPanel.setBackground(Color.WHITE);
 * // Frame design: Space -> Dark Line -> Inner Space
 * resultPanel.setBorder(BorderFactory.createCompoundBorder(
 * new EmptyBorder(30, 0, 0, 0),
 * BorderFactory.createCompoundBorder(
 * new LineBorder(COLOR_DARK_BLUE, 3),
 * new EmptyBorder(20, 20, 20, 20))));
 *
 * // Word Title (Blank starts)
 * lblResultWord = new JLabel("");
 * lblResultWord.setFont(new Font("Segoe UI", Font.BOLD, 28));
 * lblResultWord.setForeground(COLOR_DARK_BLUE);
 *
 * // Content Area - Definition and Time
 * JPanel pnlContent = new JPanel(new BorderLayout(0, 15));
 * pnlContent.setBackground(Color.WHITE);
 * pnlContent.setBorder(new EmptyBorder(20, 0, 0, 0));
 *
 * // Definition Area
 * JPanel pnlDefinition = new JPanel(new BorderLayout());
 * pnlDefinition.setBackground(Color.WHITE);
 * JLabel lblDefTitle = new JLabel("Definition:");
 * lblDefTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
 * lblDefTitle.setForeground(COLOR_DARK_BLUE);
 *
 * txtDefinition = new JTextArea();
 * txtDefinition.setFont(new Font("Segoe UI", Font.PLAIN, 14));
 * txtDefinition.setBackground(COLOR_YELLOW_BG);
 * txtDefinition.setLineWrap(true);
 * txtDefinition.setWrapStyleWord(true);
 * txtDefinition.setEditable(false);
 * txtDefinition.setBorder(new EmptyBorder(10, 10, 10, 10)); // Yellow box inner
 * space
 *
 * // Spacer panel around yellow box
 * JPanel yellowBoxWrapper = new JPanel(new BorderLayout());
 * yellowBoxWrapper.add(txtDefinition);
 * yellowBoxWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));
 *
 * pnlDefinition.add(lblDefTitle, BorderLayout.NORTH);
 * pnlDefinition.add(yellowBoxWrapper, BorderLayout.CENTER);
 *
 * // Time Area
 * JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
 * pnlTime.setBackground(Color.WHITE);
 * JLabel lblTimeTitle = new JLabel("Execution Time: ");
 * lblTimeTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
 * lblTimeTitle.setForeground(COLOR_DARK_BLUE);
 *
 * lblExecutionTime = new JLabel("");
 * lblExecutionTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
 * lblExecutionTime.setForeground(Color.DARK_GRAY);
 *
 * pnlTime.add(lblTimeTitle);
 * pnlTime.add(lblExecutionTime);
 *
 * // Add Components to Content Panel
 * pnlContent.add(pnlDefinition, BorderLayout.CENTER);
 * pnlContent.add(pnlTime, BorderLayout.SOUTH);
 *
 * resultPanel.add(lblResultWord, BorderLayout.NORTH);
 * resultPanel.add(pnlContent, BorderLayout.CENTER);
 *
 * // Set hide at start
 * resultPanel.setVisible(false);
 *
 * // Adding to Main Panel
 * mainPanel.add(lblTitle);
 * mainPanel.add(pnlSearchTypes);
 * mainPanel.add(pnlInput);
 * mainPanel.add(resultPanel);
 *
 * add(mainPanel);
 *
 * // --- ActionListener (EVENTS) ---
 *
 * // Event that changes button colors (every button)
 * ActionListener toggleListener = e -> updateButtonColors();
 * btnLinear.addActionListener(toggleListener);
 * btnBinary.addActionListener(toggleListener);
 * btnHash.addActionListener(toggleListener);
 *
 * // SEARCH BUTTON LOGIC
 * btnSearchAction.addActionListener(new ActionListener() {
 *
 * @Override
 * public void actionPerformed(ActionEvent e) {
 * performSearch(); // Calls the search method
 * }
 * });
 * }
 *
 * // --- Methods ---
 *
 * // Toggle Button Generator
 * private JToggleButton createToggleBtn(String text) {
 * JToggleButton btn = new JToggleButton(text);
 * btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
 * btn.setFocusPainted(false);
 * btn.setPreferredSize(new Dimension(150, 40));
 * btn.setBorder(new LineBorder(COLOR_DARK_BLUE, 2));
 * return btn;
 * }
 *
 * // Makes whichever button is selected Blue and the others White
 * private void updateButtonColors() {
 * updateSingleBtnColor(btnLinear);
 * updateSingleBtnColor(btnBinary);
 * updateSingleBtnColor(btnHash);
 * }
 *
 * private void updateSingleBtnColor(JToggleButton btn) {
 * if (btn.isSelected()) {
 * btn.setBackground(COLOR_ACTIVE_BLUE);
 * btn.setForeground(Color.WHITE);
 * btn.setBorder(new LineBorder(COLOR_ACTIVE_BLUE, 2));
 * } else {
 * btn.setBackground(Color.WHITE);
 * btn.setForeground(COLOR_DARK_BLUE);
 * btn.setBorder(new LineBorder(COLOR_DARK_BLUE, 2));
 * }
 * }
 *
 * // --- Main Search LOGIC ---
 * private void performSearch() {
 * String searchword = txtSearch.getText().trim();
 *
 * // Check Empty Input
 * if (searchword.isEmpty() || searchword.equals("Type a word...")) {
 * JOptionPane.showMessageDialog(this, "Please enter a word.");
 * return;
 * }
 *
 * String defres = "";
 * String timeres = "";
 *
 * // Which Search Method is Selected
 * if (btnLinear.isSelected()) {
 * defres = linearSearchAlgorithm(searchword);
 * timeres = "0.005 seconds";
 *
 * } else if (btnBinary.isSelected()) {
 * defres = binarySearchAlgorithm(searchword);
 * timeres = "0.002 seconds";
 *
 * } else if (btnHash.isSelected()) {
 * defres = hashSearchAlgorithm(searchword);
 * timeres = "0.001 seconds";
 *
 * } else {
 * JOptionPane.showMessageDialog(this, "Please select a search method.");
 * return;
 * }
 *
 * // Update Interface
 * lblResultWord.setText(searchword.toUpperCase());
 * txtDefinition.setText(defres);
 * lblExecutionTime.setText(timeres);
 *
 * // Make visible
 * resultPanel.setVisible(true);
 * revalidate(); // Refresh screen
 * repaint();
 * }
 *
 * // =====================================================================
 *
 * public String linearSearchAlgorithm(String word) {
 * // Kelimeyi bulamazsan "Words not found" gibib bir şey döndür.
 * return "Linear Search '" + word + "'searched.\n" +
 * "LINEAR .... ...";
 * }
 *
 * public String binarySearchAlgorithm(String word) {
 * // Not: Binary search için listenin sıralı olması gerekir.
 * return "Binary Search '" + word + "' searched.\n" +
 * "Binary .... ..";
 * }
 *
 * public String hashSearchAlgorithm(String word) {
 * return "Hash-Based Search '" + word + "' searched.\n" +
 * "HASH .... .";
 * }
 *
 * public static void main(String[] args) {
 * // Start the UI
 * SwingUtilities.invokeLater(() -> {
 * DictionaryUI frame = new DictionaryUI();
 * frame.setVisible(true);
 * });
 * }
 * }
 */
