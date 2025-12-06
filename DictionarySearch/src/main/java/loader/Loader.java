package main.java.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.java.utils.Entry;
import main.java.utils.HashTable;

enum FileType {
  TXT,
  CSV
}

public class Loader<Key extends Comparable<Key>, Value extends Comparable<Value>> {
  private final String filePath;
  private final FileType fileType;
  private final Character delimiter = ',';
  private HashTable<Key, Value> hashTable;

  public Loader(String filePath) {
    this.filePath = filePath;
    this.fileType = determineFileType(filePath);
    this.hashTable = new HashTable<>();
  }

  public HashTable<Key, Value> load() throws FileNotFoundException, IOException {
    switch (this.fileType) {
      case TXT:
        return loadTxt();
      case CSV:
        return loadCsv();
      default:
        throw new IllegalArgumentException("Unsupported file type: " + this.fileType);
    }
  }

  public HashTable<Key, Value> loadTxt() throws FileNotFoundException, IOException {
    File file = new File(this.filePath);

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty()) {
          @SuppressWarnings("unchecked")
          Key key = (Key) line;
          Entry<Key, Value> entry = new Entry<>(key, null);
          this.hashTable.put(entry);
        }
      }
    }

    return this.hashTable;
  }

  public HashTable<Key, Value> loadCsv() throws IOException {
    try (FileReader reader = new FileReader(this.filePath)) {
      int currentChar;
      boolean insideQuotes = false;
      boolean readingKey = true;
      boolean isFirstRecord = true;

      StringBuilder keyBuilder = new StringBuilder();
      StringBuilder valueBuilder = new StringBuilder();

      while ((currentChar = reader.read()) != -1) {
        char ch = (char) currentChar;

        if (ch == '"') {
          insideQuotes = !insideQuotes;

          if (readingKey) {
            keyBuilder.append(ch);
          } else {
            valueBuilder.append(ch);
          }

        } else if (ch == this.delimiter && !insideQuotes) {
          readingKey = false;

        } else if ((ch == '\n' || ch == '\r') && !insideQuotes) {
          addEntryFromBuilders(keyBuilder, valueBuilder, isFirstRecord);
          isFirstRecord = false;

          keyBuilder.setLength(0);
          valueBuilder.setLength(0);
          readingKey = true;

        } else {
          if (readingKey) {
            keyBuilder.append(ch);
          } else {
            valueBuilder.append(ch);
          }
        }
      }

      if (keyBuilder.length() > 0 || valueBuilder.length() > 0) {
        addEntryFromBuilders(keyBuilder, valueBuilder, isFirstRecord);
      }
    }

    return this.hashTable;
  }

  @SuppressWarnings("unchecked")
  private void addEntryFromBuilders(StringBuilder keyBuilder,
      StringBuilder valueBuilder,
      boolean isFirstRecord) {
    String rawKey = keyBuilder.toString().trim();
    String rawValue = valueBuilder.toString().trim();

    if (rawKey.isEmpty() && rawValue.isEmpty()) {
      // blank line
      return;
    }

    String keyStr = unquoteCsvField(rawKey);
    String valueStr = unquoteCsvField(rawValue);

    if (isFirstRecord &&
        "word".equalsIgnoreCase(keyStr) &&
        valueStr.toLowerCase().startsWith("definition")) {
      return;
    }

    if (!keyStr.isEmpty() && !valueStr.isEmpty()) {
      Key key = (Key) keyStr;
      Value value = (Value) valueStr;
      Entry<Key, Value> entry = new Entry<>(key, value);
      this.hashTable.put(entry);
    }
  }

  private String unquoteCsvField(String field) {
    if (field.length() >= 2 &&
        field.charAt(0) == '"' &&
        field.charAt(field.length() - 1) == '"') {
      field = field.substring(1, field.length() - 1);
    }
    field = field.replace("\"\"", "\"");
    return field;
  }

  private FileType determineFileType(String filePath) {
    if (filePath.endsWith(".txt")) {
      return FileType.TXT;
    } else if (filePath.endsWith(".csv")) {
      return FileType.CSV;
    } else {
      throw new IllegalArgumentException("Unsupported file type: " + filePath);
    }
  }
}
