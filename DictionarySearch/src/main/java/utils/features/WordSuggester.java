package main.java.utils.features;

import main.java.utils.ArrayList;
import main.java.utils.HashTable;

public class WordSuggester {
  private HashTable<String, String> dictionary;

  public WordSuggester(HashTable<String, String> dictionary) {
    this.dictionary = dictionary;
  }

  public ArrayList<String> suggest(String word) {
    int wordLength = word.length();
    ArrayList<String> suggestions = new ArrayList<>();
    String[] keys = dictionary.getKeys();

    for (String key : keys) {
      if (key.length() == wordLength) {
        int differences = 0;
        for (int i = 0; i < wordLength; i++) {
          if (word.charAt(i) != key.charAt(i)) {
            differences++;
            if (differences > 1) {
              break;
            }
          }
        }
        if (differences == 1) {
          suggestions.add(key);
        }

      } else if (key.length() > wordLength) {
        if (key.startsWith(word)) {
          suggestions.add(key);
        }
      } else {
        continue;
      }
    }
    return suggestions;
  }

  public boolean hasSuggestions(String word) {
    int wordLength = word.length();
    String[] keys = dictionary.getKeys();
    for (String key : keys) {
      if (key.length() == wordLength) {
        int differences = 0;
        for (int i = 0; i < wordLength; i++) {
          if (word.charAt(i) != key.charAt(i)) {
            differences++;
            if (differences > 1) {
              break;
            }
          }
        }
        if (differences == 1) {
          return true;
        }

      } else if (key.length() > wordLength) {
        if (key.startsWith(word)) {
          return true;
        }
      } else {
        continue;
      }
    }
    return false;
  }
}
