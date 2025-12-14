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
import main.java.utils.analysis.ExecutionTimeAnalyzer;
import main.java.utils.analysis.ExecutionTimeFormatter;
import main.java.utils.features.WordSuggester;

public class DictionaryApp extends JFrame {
  private HashTable<String, String> hashTable;
  private String[] dictionaryKeys;
  private ExecutionTimeFormatter timeFormatter;
  private ExecutionTimeAnalyzer timeAnalyzer;
  private BinarySearch<String> binarySearch;
  private LinearSearch<String> linearSearch;
  private HashSearch<String> hashSearch;
  private WordSuggester wordSuggester;

  // Custom fonts
  private Font geistMonoBold;

  private JToggleButton btnLinear, btnBinary, btnHash;
  private ButtonGroup searchGroup;
  private JTextField txtSearch;
  private JButton btnSearchAction;

  private JPanel resultPanel;
  private JLabel lblResultWord;
  private JTextArea txtDefinition;
  private JLabel lblExecutionTime;

  private JPanel suggestionsPanel;
  private JPanel suggestionsListPanel;

  // Color palette for the UI
  private final Color COLOR_PRIMARY = new Color(15, 23, 42);
  private final Color COLOR_ACCENT = new Color(99, 102, 241);
  private final Color COLOR_ACCENT_HOVER = new Color(79, 70, 229);
  private final Color COLOR_BACKGROUND = new Color(248, 250, 252);
  private final Color COLOR_CARD = new Color(255, 255, 255);
  private final Color COLOR_BORDER = new Color(226, 232, 240);
  private final Color COLOR_TEXT_PRIMARY = new Color(15, 23, 42);
  private final Color COLOR_TEXT_SECONDARY = new Color(100, 116, 139);
  private final Color COLOR_TEXT_MUTED = new Color(148, 163, 184);
  private final Color COLOR_DEFINITION_BG = new Color(241, 245, 249);
  private final Color COLOR_SUGGESTION_BG = new Color(241, 245, 249);
  private final Color COLOR_SUGGESTION_HOVER = new Color(226, 232, 240);

  public DictionaryApp() {
    loadCustomFonts();
    initializeData();
    initializeUI();
  }

  private void loadCustomFonts() {
    try {
      Font geistBold = Font.createFont(Font.TRUETYPE_FONT,
          new File("DictionarySearch/fonts/GeistMono-Bold.ttf"));
      geistMonoBold = geistBold.deriveFont(Font.BOLD, 16f);

      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(geistBold);

      System.out.println("Custom fonts loaded successfully");
    } catch (FontFormatException | IOException e) {
      System.err.println("Error loading custom fonts: " + e.getMessage());
      geistMonoBold = new Font("Monospaced", Font.BOLD, 16);
    }
  }

  private void initializeData() {
    try {
      timeFormatter = new ExecutionTimeFormatter(4);
      timeAnalyzer = new ExecutionTimeAnalyzer();

      binarySearch = new BinarySearch<>();
      linearSearch = new LinearSearch<>();

      Loader<String, String> loader = new Loader<>("DictionarySearch/data/dict.csv");
      hashTable = loader.load();

      hashSearch = new HashSearch<>(hashTable);
      wordSuggester = new WordSuggester(hashTable);

      dictionaryKeys = hashTable.getKeys();

      // Keys need to be sorted for binary search to work
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
    setTitle("Interactive Dictionary");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(850, 750);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    getContentPane().setBackground(COLOR_BACKGROUND);

    try {
      ImageIcon icon = new ImageIcon("DictionarySearch/src/main/java/app/icon.png");
      setIconImage(icon.getImage());
    } catch (Exception e) {
      System.err.println("Error loading icon: " + e.getMessage());
    }

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(COLOR_BACKGROUND);
    mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

    JPanel topSection = new JPanel();
    topSection.setLayout(new BorderLayout());
    topSection.setBackground(COLOR_BACKGROUND);

    JLabel lblTitle = new JLabel("Interactive Dictionary Interface");
    lblTitle.setFont(geistMonoBold.deriveFont(28f));
    lblTitle.setForeground(COLOR_PRIMARY);
    lblTitle.setHorizontalAlignment(JLabel.CENTER);

    JPanel titlePanel = new JPanel();
    titlePanel.setBackground(COLOR_BACKGROUND);
    titlePanel.add(lblTitle);

    JPanel pnlSearchTypes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 30));
    pnlSearchTypes.setBackground(COLOR_BACKGROUND);

    searchGroup = new ButtonGroup();

    btnLinear = createToggleBtn("Linear Search");
    btnBinary = createToggleBtn("Binary Search");
    btnHash = createToggleBtn("Hash-Based Search");

    searchGroup.add(btnLinear);
    searchGroup.add(btnBinary);
    searchGroup.add(btnHash);

    pnlSearchTypes.add(btnLinear);
    pnlSearchTypes.add(btnBinary);
    pnlSearchTypes.add(btnHash);

    btnHash.setSelected(true);
    updateButtonColors();

    JPanel pnlInput = new JPanel(new BorderLayout(10, 0));
    pnlInput.setMaximumSize(new Dimension(600, 45));
    pnlInput.setBackground(COLOR_BACKGROUND);
    pnlInput.setBorder(new EmptyBorder(10, 0, 20, 0));

    txtSearch = new JTextField("Type a word...");
    txtSearch.setFont(geistMonoBold.deriveFont(Font.PLAIN, 16f));
    txtSearch.setBackground(COLOR_CARD);
    txtSearch.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(COLOR_BORDER, 2),
        new EmptyBorder(8, 12, 8, 12)));

    btnSearchAction = new JButton("Search");
    btnSearchAction.setBackground(COLOR_ACCENT);
    btnSearchAction.setForeground(COLOR_CARD);
    btnSearchAction.setFont(geistMonoBold.deriveFont(15f));
    btnSearchAction.setFocusPainted(false);
    btnSearchAction.setPreferredSize(new Dimension(130, 45));
    btnSearchAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnSearchAction.setBorder(BorderFactory.createEmptyBorder());

    pnlInput.add(txtSearch, BorderLayout.CENTER);
    pnlInput.add(btnSearchAction, BorderLayout.EAST);

    topSection.add(titlePanel, BorderLayout.NORTH);
    topSection.add(pnlSearchTypes, BorderLayout.CENTER);
    topSection.add(pnlInput, BorderLayout.SOUTH);

    JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
    centerPanel.setBackground(COLOR_BACKGROUND);

    // Result panel starts hidden until first search
    resultPanel = new JPanel(new BorderLayout());
    resultPanel.setBackground(COLOR_CARD);
    resultPanel.setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(30, 0, 0, 0),
        BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1),
            new EmptyBorder(20, 20, 20, 20))));

    lblResultWord = new JLabel("");
    lblResultWord.setFont(geistMonoBold.deriveFont(28f));
    lblResultWord.setForeground(COLOR_ACCENT);

    JPanel pnlContent = new JPanel(new BorderLayout(0, 15));
    pnlContent.setBackground(COLOR_CARD);
    pnlContent.setBorder(new EmptyBorder(20, 0, 0, 0));

    JPanel pnlDefinition = new JPanel(new BorderLayout());
    pnlDefinition.setBackground(COLOR_CARD);

    JLabel lblDefTitle = new JLabel("Definition:");
    lblDefTitle.setFont(geistMonoBold.deriveFont(15f));
    lblDefTitle.setForeground(COLOR_TEXT_PRIMARY);

    txtDefinition = new JTextArea();
    txtDefinition.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
    txtDefinition.setForeground(COLOR_TEXT_PRIMARY);
    txtDefinition.setBackground(COLOR_DEFINITION_BG);
    txtDefinition.setLineWrap(true);
    txtDefinition.setWrapStyleWord(true);
    txtDefinition.setEditable(false);
    txtDefinition.setBorder(new EmptyBorder(12, 12, 12, 12));
    txtDefinition.setRows(6);

    JScrollPane definitionScrollPane = new JScrollPane(txtDefinition);
    definitionScrollPane.setBorder(new LineBorder(COLOR_BORDER, 1));
    definitionScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    definitionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    JPanel yellowBoxWrapper = new JPanel(new BorderLayout());
    yellowBoxWrapper.setBackground(COLOR_CARD);
    yellowBoxWrapper.add(definitionScrollPane);
    yellowBoxWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));

    pnlDefinition.add(lblDefTitle, BorderLayout.NORTH);
    pnlDefinition.add(yellowBoxWrapper, BorderLayout.CENTER);

    JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    pnlTime.setBackground(COLOR_CARD);

    JLabel lblTimeTitle = new JLabel("Execution Time: ");
    lblTimeTitle.setFont(geistMonoBold.deriveFont(14f));
    lblTimeTitle.setForeground(COLOR_TEXT_PRIMARY);

    lblExecutionTime = new JLabel("");
    lblExecutionTime.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
    lblExecutionTime.setForeground(COLOR_TEXT_SECONDARY);

    pnlTime.add(lblTimeTitle);
    pnlTime.add(lblExecutionTime);

    pnlContent.add(pnlDefinition, BorderLayout.CENTER);
    pnlContent.add(pnlTime, BorderLayout.SOUTH);

    resultPanel.add(lblResultWord, BorderLayout.NORTH);
    resultPanel.add(pnlContent, BorderLayout.CENTER);
    resultPanel.setVisible(false);

    suggestionsPanel = new JPanel(new BorderLayout());
    suggestionsPanel.setBackground(COLOR_CARD);
    suggestionsPanel.setBorder(BorderFactory.createCompoundBorder(
        new EmptyBorder(30, 0, 0, 0),
        BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1),
            new EmptyBorder(15, 15, 15, 15))));
    suggestionsPanel.setPreferredSize(new Dimension(220, 0));

    JLabel lblSuggestionsTitle = new JLabel("Suggestions");
    lblSuggestionsTitle.setFont(geistMonoBold.deriveFont(15f));
    lblSuggestionsTitle.setForeground(COLOR_TEXT_PRIMARY);
    lblSuggestionsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

    suggestionsListPanel = new JPanel();
    suggestionsListPanel.setLayout(new BoxLayout(suggestionsListPanel, BoxLayout.Y_AXIS));
    suggestionsListPanel.setBackground(COLOR_CARD);

    JScrollPane suggestionsScroll = new JScrollPane(suggestionsListPanel);
    suggestionsScroll.setBorder(BorderFactory.createEmptyBorder());
    suggestionsScroll.getVerticalScrollBar().setUnitIncrement(16);

    suggestionsPanel.add(lblSuggestionsTitle, BorderLayout.NORTH);
    suggestionsPanel.add(suggestionsScroll, BorderLayout.CENTER);
    suggestionsPanel.setVisible(false);

    centerPanel.add(resultPanel, BorderLayout.CENTER);
    centerPanel.add(suggestionsPanel, BorderLayout.EAST);

    mainPanel.add(topSection, BorderLayout.NORTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER);

    add(mainPanel);

    ActionListener toggleListener = e -> updateButtonColors();
    btnLinear.addActionListener(toggleListener);
    btnBinary.addActionListener(toggleListener);
    btnHash.addActionListener(toggleListener);

    btnSearchAction.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        performSearch();
      }
    });

    txtSearch.addActionListener(e -> performSearch());
  }

  private JToggleButton createToggleBtn(String text) {
    JToggleButton btn = new JToggleButton(text);
    btn.setFont(geistMonoBold.deriveFont(16f));
    btn.setFocusPainted(false);
    btn.setPreferredSize(new Dimension(200, 42));
    btn.setBorder(new LineBorder(COLOR_BORDER, 2));
    btn.setBackground(COLOR_CARD);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return btn;
  }

  private void updateButtonColors() {
    updateSingleBtnColor(btnLinear);
    updateSingleBtnColor(btnBinary);
    updateSingleBtnColor(btnHash);
  }

  private void updateSingleBtnColor(JToggleButton btn) {
    if (btn.isSelected()) {
      btn.setBackground(COLOR_ACCENT);
      btn.setForeground(COLOR_CARD);
      btn.setBorder(new LineBorder(COLOR_ACCENT, 2));
    } else {
      btn.setBackground(COLOR_CARD);
      btn.setForeground(COLOR_TEXT_PRIMARY);
      btn.setBorder(new LineBorder(COLOR_BORDER, 2));
    }
  }

  private void performSearch() {
    String searchWord = txtSearch.getText().trim();

    if (searchWord.isEmpty() || searchWord.equals("Type a word...")) {
      JOptionPane.showMessageDialog(this, "Please enter a word.");
      return;
    }

    searchWord = searchWord.toLowerCase();

    String defResult = "";
    boolean wordFound = false;

    if (btnLinear.isSelected()) {
      final String finalSearchWord = searchWord;
      final int[] foundIndex = new int[1];

      timeAnalyzer.run(() -> {
        foundIndex[0] = linearSearch.search(dictionaryKeys, finalSearchWord);
      });

      if (foundIndex[0] != -1) {
        defResult = hashTable.get(searchWord);
        wordFound = true;
      } else {
        defResult = "Word not found in dictionary.";
      }

    } else if (btnBinary.isSelected()) {
      final String finalSearchWord = searchWord;
      final int[] foundIndex = new int[1];

      timeAnalyzer.run(() -> {
        foundIndex[0] = binarySearch.search(dictionaryKeys, finalSearchWord);
      });

      if (foundIndex[0] != -1) {
        defResult = hashTable.get(searchWord);
        wordFound = true;
      } else {
        defResult = "Word not found in dictionary.";
      }

    } else if (btnHash.isSelected()) {
      final String finalSearchWord = searchWord;
      final String[] result = new String[1];

      timeAnalyzer.run(() -> {
        result[0] = hashSearch.searchInHashTable(finalSearchWord);
      });

      defResult = result[0];

      if (defResult != null) {
        wordFound = true;
      } else {
        defResult = "Word not found in dictionary.";
      }

    } else {
      JOptionPane.showMessageDialog(this, "Please select a search method.");
      return;
    }

    double executionTimeMillis = timeAnalyzer.getElapsedTimeInMilliseconds();
    String formattedTime = timeFormatter.formatMilliseconds(executionTimeMillis);

    lblResultWord.setText(searchWord.toUpperCase());
    txtDefinition.setText(defResult);
    txtDefinition.setCaretPosition(0);
    lblExecutionTime.setText(formattedTime);

    resultPanel.setVisible(true);

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
      int maxSuggestions = Math.min(5, suggestions.size());

      for (int i = 0; i < maxSuggestions; i++) {
        String suggestion = suggestions.get(i);
        JPanel suggestionItem = createSuggestionPanel(suggestion);
        suggestionsListPanel.add(suggestionItem);

        if (i < maxSuggestions - 1) {
          suggestionsListPanel.add(javax.swing.Box.createVerticalStrut(6));
        }
      }

      suggestionsPanel.setVisible(true);
    } else {
      JLabel noSuggestions = new JLabel("No suggestions");
      noSuggestions.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
      noSuggestions.setForeground(COLOR_TEXT_MUTED);
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
        new LineBorder(COLOR_BORDER, 1),
        new EmptyBorder(8, 10, 8, 10)));
    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    panel.setMaximumSize(new Dimension(190, 36));
    panel.setPreferredSize(new Dimension(190, 36));

    JLabel label = new JLabel(word);
    label.setFont(geistMonoBold.deriveFont(Font.PLAIN, 14f));
    label.setForeground(COLOR_TEXT_PRIMARY);

    panel.add(label, BorderLayout.CENTER);

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
    System.setProperty("awt.useSystemAAFontSettings", "lcd");
    System.setProperty("swing.aatext", "true");

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
