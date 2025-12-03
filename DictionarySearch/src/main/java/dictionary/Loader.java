package main.java.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import main.java.dictionary.hash.HashTable;

enum FileType {
  CSV,
  TXT
}

public class Loader {

  private static final char DELIMITER = ',';

  private File source;
  private ArrayList<String[]> destinationArrayList;
  private HashTable<String, String> destinationHashTable;
  private final FileType fileType;

  public Loader(File source, ArrayList<String[]> destinationArrayList) {
    this.source = source;
    this.destinationArrayList = destinationArrayList;
    this.fileType = determineFileType();
  }

  public Loader(File source, HashTable<String, String> destinationHashTable) {
    this.source = source;
    this.destinationHashTable = destinationHashTable;
    this.fileType = determineFileType();
  }

  public File getSource() {
    return source;
  }

  public void setSource(File source) {
    this.source = source;
  }

  public FileType getFileType() {
    return fileType;
  }

  public ArrayList<String[]> getArrayDestination() {
    return destinationArrayList;
  }

  public HashTable<String, String> getHashTableDestination() {
    return destinationHashTable;
  }

  private FileType determineFileType() {
    String name = source.getName().toLowerCase();
    if (name.endsWith(".csv")) {
      return FileType.CSV;
    } else if (name.endsWith(".txt")) {
      return FileType.TXT;
    }
    throw new IllegalArgumentException("Unsupported file type: " + name);
  }

  public void load() {
    if (source == null) {
      throw new IllegalStateException("Source file is not set.");
    }
    switch (fileType) {
      case CSV -> loadCsv();
      case TXT -> loadTxt();
      default -> throw new IllegalStateException("Unsupported file type: " + fileType);
    }
  }

  private void loadCsv() {
    if (destinationArrayList == null && destinationHashTable == null) {
      throw new IllegalStateException("No destination container provided.");
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
      ArrayList<String> rowBuffer = new ArrayList<>();
      ArrayList<Character> fieldBuffer = new ArrayList<>();
      boolean insideQuotes = false;

      int ch;
      while ((ch = reader.read()) != -1) {
        char current = (char) ch;

        if (current == '"') {
          insideQuotes = !insideQuotes;
        } else if (current == DELIMITER && !insideQuotes) {
          rowBuffer.add(convertField(fieldBuffer));
          fieldBuffer.clear();
        } else if ((current == '\n' || current == '\r') && !insideQuotes) {
          commitCsvRow(rowBuffer, fieldBuffer);
        } else {
          fieldBuffer.add(current);
        }
      }

      commitCsvRow(rowBuffer, fieldBuffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadTxt() {
    if (destinationArrayList == null && destinationHashTable == null) {
      throw new IllegalStateException("No destination container provided.");
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
      String line;
      int lineNumber = 0;

      while ((line = reader.readLine()) != null) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
          continue;
        }

        if (destinationArrayList != null) {
          destinationArrayList.add(new String[] { trimmed });
        }

        if (destinationHashTable != null) {
          destinationHashTable.put("line_" + lineNumber++, trimmed);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void commitCsvRow(ArrayList<String> rowBuffer, ArrayList<Character> fieldBuffer) {
    if (rowBuffer.isEmpty() && fieldBuffer.isEmpty()) {
      return;
    }
    rowBuffer.add(convertField(fieldBuffer));
    fieldBuffer.clear();
    storeCsvRow(rowBuffer);
    rowBuffer.clear();
  }

  private void storeCsvRow(ArrayList<String> rowBuffer) {
    String[] row = toStringArray(rowBuffer);
    if (row.length == 0) {
      return;
    }

    if (destinationArrayList != null) {
      destinationArrayList.add(row);
    }

    if (destinationHashTable != null && row.length >= 2) {
      String key = row[0];
      StringBuilder valueBuilder = new StringBuilder(row[1]);
      for (int i = 2; i < row.length; i++) {
        valueBuilder.append(',').append(row[i]);
      }
      destinationHashTable.put(key, valueBuilder.toString());
    }
  }

  private String convertField(ArrayList<Character> buffer) {
    char[] chars = new char[buffer.size()];
    for (int i = 0; i < buffer.size(); i++) {
      chars[i] = buffer.get(i);
    }
    return new String(chars);
  }

  private String[] toStringArray(ArrayList<String> buffer) {
    String[] row = new String[buffer.size()];
    for (int i = 0; i < buffer.size(); i++) {
      row[i] = buffer.get(i);
    }
    return row;
  }
}
