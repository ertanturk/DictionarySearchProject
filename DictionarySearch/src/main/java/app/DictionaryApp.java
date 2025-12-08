package main.java.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import main.java.loader.Loader;
import main.java.search.BinarySearch;
import main.java.search.HashSearch;
import main.java.search.LinearSearch;
import main.java.utils.ArrayList;
import main.java.utils.HashTable;
import main.java.utils.analysis.ExecutionTimeFormatter;
import main.java.utils.features.WordSuggester;

public class DictionaryApp extends JFrame {
  private HashTable<String, String> hashTable;
  private String[] dictionaryKeys;
  private ExecutionTimeFormatter timeFormatter;
  private BinarySearch<String> binarySearch;
  private LinearSearch<String> linearSearch;
  private HashSearch<String> hashSearch;
  private WordSuggester wordSuggester;

  // Custom fonts
  private Font geistMonoBold;

  // --- Components ---
  private JToggleButton btnLinear, btnBinary, btnHash;
  private ButtonGroup searchGroup;
  private JTextField txtSearch;
  private JButton btnSearchAction;

  // Result Screen Components
  private JPanel resultPanel;
  private JLabel lblResultWord;
  private JTextArea txtDefinition;
  private JLabel lblExecutionTime;

  // Suggestions Panel
  private JPanel suggestionsPanel;
  private JPanel suggestionsListPanel;

  // --- COLORS ---
  private final Color COLOR_DARK_BLUE = new Color(28, 45, 89); // Deep navy
  private final Color COLOR_ACTIVE_BLUE = new Color(82, 123, 202); // Soft slate blue
  private final Color COLOR_YELLOW_BG = new Color(248, 244, 229); // Warm ivory
  private final Color COLOR_SUGGESTION_BG = new Color(236, 240, 245); // Light gray-blue
  private final Color COLOR_SUGGESTION_HOVER = new Color(219, 226, 235); // Gentle hover tone

  public DictionaryApp() {
    loadCustomFonts();
    initializeData();
    initializeUI();
  }

  private void loadCustomFonts() {
    try {
      // Load Geist Mono Bold
      Font geistBold = Font.createFont(Font.TRUETYPE_FONT,
          new File("DictionarySearch/fonts/GeistMono-Bold.ttf"));
      geistMonoBold = geistBold.deriveFont(Font.BOLD, 16f);

      // Register fonts with GraphicsEnvironment
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(geistBold);

      System.out.println("Custom fonts loaded successfully");
    } catch (FontFormatException | IOException e) {
      System.err.println("Error loading custom fonts: " + e.getMessage());
      // Fallback to default font
      geistMonoBold = new Font("Monospaced", Font.BOLD, 16);
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

      // Initialize hash search
      hashSearch = new HashSearch<>(hashTable);

      // Initialize word suggester
      wordSuggester = new WordSuggester(hashTable);

      // Get all keys from HashTable and store in String[] array
      dictionaryKeys = hashTable.getKeys();

      // Sort the keys array for binary search
      binarySearch.sort(dictionaryKeys, 0, dictionaryKeys.length - 1);

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
    // Window Settings
    setTitle("Interactive Dictionary");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(850, 750);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    getContentPane().setBackground(Color.WHITE);

    // Set application icon
    try {
      ImageIcon icon = new ImageIcon("DictionarySearch/src/main/java/app/icon.png");
      setIconImage(icon.getImage());
    } catch (Exception e) {
      System.err.println("Error loading icon: " + e.getMessage());
    }

    // Main Panel (Vertical alignment)
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

    // Top section container
    JPanel topSection = new JPanel();
    topSection.setLayout(new BorderLayout());
    topSection.setBackground(Color.WHITE);

    // 1. Title
    JLabel lblTitle = new JLabel("Interactive Dictionary Interface");
    lblTitle.setFont(geistMonoBold.deriveFont(28f));
    lblTitle.setForeground(COLOR_DARK_BLUE);
    lblTitle.setHorizontalAlignment(JLabel.CENTER);

    JPanel titlePanel = new JPanel();
    titlePanel.setBackground(Color.WHITE);
    titlePanel.add(lblTitle);

    // 2. BUTTONS (JToggleButton)
    JPanel pnlSearchTypes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
    pnlSearchTypes.setBackground(Color.WHITE);

    searchGroup = new ButtonGroup();

    // Create Toggle Buttons
    btnLinear = createToggleBtn("Linear Search");
    btnBinary = createToggleBtn("Binary Search");
    btnHash = createToggleBtn("Hash-Based Search");

    // Add to Group (One selection only)
    searchGroup.add(btnLinear);
    searchGroup.add(btnBinary);
    searchGroup.add(btnHash);

    pnlSearchTypes.add(btnLinear);
    pnlSearchTypes.add(btnBinary);
    pnlSearchTypes.add(btnHash);

    // Hash is selected by default
    btnHash.setSelected(true);
    updateButtonColors();

    // 3. SEARCH INPUT FIELD
    JPanel pnlInput = new JPanel(new BorderLayout(10, 0));
    pnlInput.setMaximumSize(new Dimension(600, 45));
    pnlInput.setBackground(Color.WHITE);
    pnlInput.setBorder(new EmptyBorder(10, 0, 20, 0));

    txtSearch = new JTextField("Type a word...");
    txtSearch.setFont(geistMonoBold.deriveFont(Font.PLAIN, 16f));
    txtSearch.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(new Color(203, 213, 225), 2),
        new EmptyBorder(8, 12, 8, 12)));

    btnSearchAction = new JButton("Search");
    btnSearchAction.setBackground(COLOR_DARK_BLUE);
    btnSearchAction.setForeground(Color.WHITE);
    btnSearchAction.setFont(geistMonoBold.deriveFont(15f));
    btnSearchAction.setFocusPainted(false);
    btnSearchAction.setPreferredSize(new Dimension(130, 45));
    btnSearchAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnSearchAction.setBorder(BorderFactory.createEmptyBorder());

    pnlInput.add(txtSearch, BorderLayout.CENTER);
    pnlInput.add(btnSearchAction, BorderLayout.EAST);

    // Add all top components
    topSection.add(titlePanel, BorderLayout.NORTH);
    topSection.add(pnlSearchTypes, BorderLayout.CENTER);
    topSection.add(pnlInput, BorderLayout.SOUTH);

    // Center Panel - Split between Result and Suggestions
    JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
    centerPanel.setBackground(Color.WHITE);

    // 4. Result Panel (Hidden at start)
    resultPanel = new JPanel(new BorderLayout());
    resultPanel.setBackground(Color.WHITE);
    resultPanel.setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(30, 0, 0, 0),
        BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_DARK_BLUE, 3),
            new EmptyBorder(20, 20, 20, 20))));

    // Word Title (Blank at start)
    lblResultWord = new JLabel("");
    lblResultWord.setFont(geistMonoBold.deriveFont(28f));
    lblResultWord.setForeground(COLOR_DARK_BLUE);

    // Content Area - Definition and Time
    JPanel pnlContent = new JPanel(new BorderLayout(0, 15));
    pnlContent.setBackground(Color.WHITE);
    pnlContent.setBorder(new EmptyBorder(20, 0, 0, 0));

    // Definition Area with ScrollPane
    JPanel pnlDefinition = new JPanel(new BorderLayout());
    pnlDefinition.setBackground(Color.WHITE);

    JLabel lblDefTitle = new JLabel("Definition:");
    lblDefTitle.setFont(geistMonoBold.deriveFont(15f));
    lblDefTitle.setForeground(COLOR_DARK_BLUE);

    txtDefinition = new JTextArea();
    txtDefinition.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
    txtDefinition.setBackground(COLOR_YELLOW_BG);
    txtDefinition.setLineWrap(true);
    txtDefinition.setWrapStyleWord(true);
    txtDefinition.setEditable(false);
    txtDefinition.setBorder(new EmptyBorder(12, 12, 12, 12));
    txtDefinition.setRows(6);

    // Add ScrollPane for definition
    JScrollPane definitionScrollPane = new JScrollPane(txtDefinition);
    definitionScrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));
    definitionScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    definitionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    // Wrapper panel around yellow box
    JPanel yellowBoxWrapper = new JPanel(new BorderLayout());
    yellowBoxWrapper.setBackground(Color.WHITE);
    yellowBoxWrapper.add(definitionScrollPane);
    yellowBoxWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));

    pnlDefinition.add(lblDefTitle, BorderLayout.NORTH);
    pnlDefinition.add(yellowBoxWrapper, BorderLayout.CENTER);

    // Time Area
    JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    pnlTime.setBackground(Color.WHITE);

    JLabel lblTimeTitle = new JLabel("Execution Time: ");
    lblTimeTitle.setFont(geistMonoBold.deriveFont(14f));
    lblTimeTitle.setForeground(COLOR_DARK_BLUE);

    lblExecutionTime = new JLabel("");
    lblExecutionTime.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
    lblExecutionTime.setForeground(new Color(100, 116, 139));

    pnlTime.add(lblTimeTitle);
    pnlTime.add(lblExecutionTime);

    // Add Components to Content Panel
    pnlContent.add(pnlDefinition, BorderLayout.CENTER);
    pnlContent.add(pnlTime, BorderLayout.SOUTH);

    resultPanel.add(lblResultWord, BorderLayout.NORTH);
    resultPanel.add(pnlContent, BorderLayout.CENTER);

    // Set hidden at start
    resultPanel.setVisible(false);

    // 5. Suggestions Panel (Right side)
    suggestionsPanel = new JPanel(new BorderLayout());
    suggestionsPanel.setBackground(Color.WHITE);
    suggestionsPanel.setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(30, 0, 0, 0),
        BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_DARK_BLUE, 2),
            new EmptyBorder(15, 15, 15, 15))));
    suggestionsPanel.setPreferredSize(new Dimension(220, 0));

    JLabel lblSuggestionsTitle = new JLabel("Suggestions");
    lblSuggestionsTitle.setFont(geistMonoBold.deriveFont(15f));
    lblSuggestionsTitle.setForeground(COLOR_DARK_BLUE);
    lblSuggestionsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

    suggestionsListPanel = new JPanel();
    suggestionsListPanel.setLayout(new BoxLayout(suggestionsListPanel, BoxLayout.Y_AXIS));
    suggestionsListPanel.setBackground(Color.WHITE);

    JScrollPane suggestionsScroll = new JScrollPane(suggestionsListPanel);
    suggestionsScroll.setBorder(BorderFactory.createEmptyBorder());
    suggestionsScroll.getVerticalScrollBar().setUnitIncrement(16);

    suggestionsPanel.add(lblSuggestionsTitle, BorderLayout.NORTH);
    suggestionsPanel.add(suggestionsScroll, BorderLayout.CENTER);
    suggestionsPanel.setVisible(false);

    // Add result and suggestions to center panel
    centerPanel.add(resultPanel, BorderLayout.CENTER);
    centerPanel.add(suggestionsPanel, BorderLayout.EAST);

    // Add all to main panel
    mainPanel.add(topSection, BorderLayout.NORTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER);

    add(mainPanel);

    // --- ActionListener (EVENTS) ---

    // Event that changes button colors (every button)
    ActionListener toggleListener = e -> updateButtonColors();
    btnLinear.addActionListener(toggleListener);
    btnBinary.addActionListener(toggleListener);
    btnHash.addActionListener(toggleListener);

    // SEARCH BUTTON LOGIC
    btnSearchAction.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        performSearch();
      }
    });

    // Add enter key listener to search field
    txtSearch.addActionListener(e -> performSearch());
  }

  // --- Methods ---

  // Toggle Button Generator
  private JToggleButton createToggleBtn(String text) {
    JToggleButton btn = new JToggleButton(text);
    btn.setFont(geistMonoBold.deriveFont(16f));
    btn.setFocusPainted(false);
    btn.setPreferredSize(new Dimension(200, 42));
    btn.setBorder(new LineBorder(COLOR_DARK_BLUE, 2));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return btn;
  }

  // Makes whichever button is selected Blue and the others White
  private void updateButtonColors() {
    updateSingleBtnColor(btnLinear);
    updateSingleBtnColor(btnBinary);
    updateSingleBtnColor(btnHash);
  }

  private void updateSingleBtnColor(JToggleButton btn) {
    if (btn.isSelected()) {
      btn.setBackground(COLOR_ACTIVE_BLUE);
      btn.setForeground(Color.WHITE);
      btn.setBorder(new LineBorder(COLOR_ACTIVE_BLUE, 2));
    } else {
      btn.setBackground(Color.WHITE);
      btn.setForeground(COLOR_DARK_BLUE);
      btn.setBorder(new LineBorder(COLOR_DARK_BLUE, 2));
    }
  }

  // --- Main Search LOGIC ---
  private void performSearch() {
    String searchWord = txtSearch.getText().trim();

    // Check Empty Input
    if (searchWord.isEmpty() || searchWord.equals("Type a word...")) {
      JOptionPane.showMessageDialog(this, "Please enter a word.");
      return;
    }

    // Convert to lowercase for consistency
    searchWord = searchWord.toLowerCase();

    String defResult = "";
    long startTime, endTime;
    double executionTimeMillis;
    boolean wordFound = false;

    // Which Search Method is Selected
    if (btnLinear.isSelected()) {
      startTime = System.nanoTime();
      int foundIndex = linearSearch.search(dictionaryKeys, searchWord);
      endTime = System.nanoTime();

      if (foundIndex != -1) {
        defResult = hashTable.get(searchWord);
        wordFound = true;
      } else {
        defResult = "Word not found in dictionary.";
      }

    } else if (btnBinary.isSelected()) {
      startTime = System.nanoTime();
      int foundIndex = binarySearch.search(dictionaryKeys, searchWord);
      endTime = System.nanoTime();

      if (foundIndex != -1) {
        defResult = hashTable.get(searchWord);
        wordFound = true;
      } else {
        defResult = "Word not found in dictionary.";
      }

    } else if (btnHash.isSelected()) {
      startTime = System.nanoTime();
      defResult = hashSearch.searchInHashTable(searchWord);
      endTime = System.nanoTime();

      if (defResult != null) {
        wordFound = true;
      } else {
        defResult = "Word not found in dictionary.";
      }

    } else {
      JOptionPane.showMessageDialog(this, "Please select a search method.");
      return;
    }

    // Calculate execution time
    executionTimeMillis = (endTime - startTime) / 1_000_000.0;
    String formattedTime = timeFormatter.formatMilliseconds(executionTimeMillis);

    // Update Interface
    lblResultWord.setText(searchWord.toUpperCase());
    txtDefinition.setText(defResult);
    txtDefinition.setCaretPosition(0); // Scroll to top
    lblExecutionTime.setText(formattedTime);

    // Make result visible
    resultPanel.setVisible(true);

    // Show suggestions if word not found
    if (!wordFound) {
      displaySuggestions(searchWord);
    } else {
      suggestionsPanel.setVisible(false);
    }

    revalidate();
    repaint();
  }

  private void displaySuggestions(String searchWord) {
    ArrayList<String> suggestions = wordSuggester.suggest(searchWord);

    suggestionsListPanel.removeAll();

    if (suggestions.size() > 0) {
      // Limit to 5 suggestions
      int maxSuggestions = Math.min(5, suggestions.size());

      for (int i = 0; i < maxSuggestions; i++) {
        String suggestion = suggestions.get(i);
        JPanel suggestionItem = createSuggestionPanel(suggestion);
        suggestionsListPanel.add(suggestionItem);

        // Add spacing between items
        if (i < maxSuggestions - 1) {
          suggestionsListPanel.add(javax.swing.Box.createVerticalStrut(6));
        }
      }

      suggestionsPanel.setVisible(true);
    } else {
      JLabel noSuggestions = new JLabel("No suggestions");
      noSuggestions.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
      noSuggestions.setForeground(new Color(148, 163, 184));
      noSuggestions.setAlignmentX(JLabel.LEFT_ALIGNMENT);
      suggestionsListPanel.add(noSuggestions);
      suggestionsPanel.setVisible(true);
    }

    suggestionsListPanel.revalidate();
    suggestionsListPanel.repaint();
  }

  private JPanel createSuggestionPanel(String word) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(COLOR_SUGGESTION_BG);
    panel.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(new Color(191, 219, 254), 1),
        new EmptyBorder(8, 10, 8, 10)));
    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    panel.setMaximumSize(new Dimension(190, 36));
    panel.setPreferredSize(new Dimension(190, 36));

    JLabel label = new JLabel(word);
    label.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
    label.setForeground(COLOR_DARK_BLUE);

    panel.add(label, BorderLayout.CENTER);

    // Hover effect
    panel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        panel.setBackground(COLOR_SUGGESTION_HOVER);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        panel.setBackground(COLOR_SUGGESTION_BG);
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        txtSearch.setText(word);
        performSearch();
      }
    });

    return panel;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }

      DictionaryApp frame = new DictionaryApp();
      frame.setVisible(true);
    });
  }
}
