package main.java.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import main.java.utils.Entry;
import main.java.utils.HashTable;

public class Loader {

  private enum FileType {
    CSV,
    TXT,
  }

  private FileType fileType;
  private final char delimiter = ',';
  private String filePath;
  private HashTable<String, String> hashTable;

  public Loader(String filePath) {
    this.filePath = filePath;
    this.fileType = determineFileType(filePath);
    this.hashTable = new HashTable<>();
  }

  private FileType determineFileType(String filePath) {
    if (filePath.endsWith(".csv")) {
      return FileType.CSV;
    } else if (filePath.endsWith(".txt")) {
      return FileType.TXT;
    } else {
      throw new IllegalArgumentException("Unsupported file type for file: " + filePath);
    }
  }

  public HashTable<String, String> load() throws Exception {
    switch (fileType) {
      case CSV:
        return loadCsv();
      case TXT:
        return loadTxt();
      default:
        throw new IllegalArgumentException("Unsupported file type for file: " + filePath);
    }
  }

  public HashTable<String, String> loadTxt() throws FileNotFoundException, IOException {
    Reader reader = null;
    try {
      File file = new File(this.filePath);
      if (file.exists()) {
        reader = new FileReader(file);
      } else {
        InputStream is = getClass().getClassLoader().getResourceAsStream(this.filePath);
        if (is == null) {
          throw new FileNotFoundException("File not found: " + this.filePath);
        }
        reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      }

      try (BufferedReader br = new BufferedReader(reader)) {
        String line;
        while ((line = br.readLine()) != null) {
          line = line.trim();
          if (!line.isEmpty()) {
            @SuppressWarnings("unchecked")
            String key = line;
            Entry<String, String> entry = new Entry<>(key, null);
            this.hashTable.put(entry);
          }
        }
      }

      return this.hashTable;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  public HashTable<String, String> loadCsv() throws IOException {

    enum State {
      READING_KEY, READING_VALUE
    }

    State state = State.READING_KEY;
    boolean insideQuotes = false;
    boolean isFirstLine = true;

    StringBuilder keyBuffer = new StringBuilder();
    StringBuilder valueBuffer = new StringBuilder();

    Reader r = null;
    try {
      File file = new File(this.filePath);
      if (file.exists()) {
        r = new FileReader(file);
      } else {
        InputStream is = getClass().getClassLoader().getResourceAsStream(this.filePath);
        if (is == null) {
          throw new FileNotFoundException("File not found: " + this.filePath);
        }
        r = new InputStreamReader(is, StandardCharsets.UTF_8);
      }

      try (BufferedReader reader = new BufferedReader(r)) {
        int c;
        while ((c = reader.read()) != -1) {
          char ch = (char) c;

          if (isFirstLine) {
            if (ch == '\n') {
              isFirstLine = false;
            }
            continue;
          }

          switch (state) {
            case READING_KEY:
              if (ch == this.delimiter) {
                state = State.READING_VALUE;
              } else if (ch != '\r' && ch != '\n') {
                keyBuffer.append(ch);
              }
              break;

            case READING_VALUE:
              if (!insideQuotes) {
                if (ch == '"') {
                  insideQuotes = true;
                } else if (ch == '\n') {
                  // Commit entry and reset buffers
                  commitEntry(keyBuffer, valueBuffer);
                  state = State.READING_KEY;
                } else if (ch != '\r') {
                  // unquoted garbage just append
                  valueBuffer.append(ch);
                }
              } else {
                if (ch == '"') {
                  reader.mark(1);
                  int next = reader.read();
                  if (next == '"') {
                    // Escaped quote ""
                    valueBuffer.append('"');
                  } else {
                    // Closing quote
                    insideQuotes = false;
                    reader.reset();
                  }
                } else {
                  // Inside quotes — just append
                  valueBuffer.append(ch);
                }
              }
              break;
          }
        }

        // Commit last entry if file doesn't end with newline
        if (keyBuffer.length() > 0 || valueBuffer.length() > 0) {
          commitEntry(keyBuffer, valueBuffer);
        }

        return this.hashTable;
      }
    } finally {
      if (r != null) {
        try {
          r.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  private void commitEntry(StringBuilder keyBuffer, StringBuilder valueBuffer) {
    String key = keyBuffer.toString().trim();
    String value = valueBuffer.toString().trim();

    if (!key.isEmpty()) {
      this.hashTable.put(new Entry<>(key, value));
    }

    keyBuffer.setLength(0);
    valueBuffer.setLength(0);
  }
}
