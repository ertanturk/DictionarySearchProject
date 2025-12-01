package main.java.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvDataLoader {
  private File source;
  private static final char DELIMITER = ',';
  private ArrayList<String[]> data = new ArrayList<>();

  public CsvDataLoader(File source) {
    this.source = source;
  }

  public File getSource() {
    return source;
  }

  public ArrayList<String[]> getData() {
    return data;
  }

  public void setSource(File source) {
    this.source = source;
  }

  public void loadData() {
    try (BufferedReader br = new BufferedReader(new FileReader(source))) {

      ArrayList<String> currentRow = new ArrayList<>();
      ArrayList<Character> field = new ArrayList<>();
      boolean insideQuotes = false;

      int characterCode;
      while ((characterCode = br.read()) != -1) {
        char ch = (char) characterCode;

        if (ch == '"') {
          insideQuotes = !insideQuotes;
        } else if (ch == DELIMITER && !insideQuotes) {
          // End current field
          currentRow.add(charListToString(field));
          field.clear();
        } else if ((ch == '\n' || ch == '\r') && !insideQuotes) {
          // End of one CSV record
          if (!currentRow.isEmpty() || !field.isEmpty()) {
            currentRow.add(charListToString(field));
            data.add(copyToStringArray(currentRow));
            currentRow.clear();
            field.clear();
          }
        } else {
          field.add(ch);
        }
      }

      // Handle last record without newline at EOF
      if (!currentRow.isEmpty() || !field.isEmpty()) {
        currentRow.add(charListToString(field));
        data.add(copyToStringArray(currentRow));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String charListToString(ArrayList<Character> list) {
    char[] arr = new char[list.size()];
    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }
    return new String(arr);
  }

  private String[] copyToStringArray(ArrayList<String> list) {
    String[] arr = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      arr[i] = list.get(i);
    }
    return arr;
  }

  public void printWords() {
    for (int i = 0; i < data.size(); i++) {
      String[] row = data.get(i);
      if (row.length > 0) {
        System.out.println(row[0]);
      }
    }
  }

  public void printDefinitions() {
    for (int i = 0; i < data.size(); i++) {
      String[] row = data.get(i);
      if (row.length > 1) {
        System.out.println(row[1]);
        System.out.println("------------------------------------------------");
      }
    }
  }

  public void printData() {
    for (int i = 0; i < data.size(); i++) {
      String[] row = data.get(i);
      for (int j = 0; j < row.length; j++) {
        System.out.print(row[j]);
        if (j < row.length - 1) {
          System.out.print(", ");
        }
      }
      System.out.println();
    }
  }
}
