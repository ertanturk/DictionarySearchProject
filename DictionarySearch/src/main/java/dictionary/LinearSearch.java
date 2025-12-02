package main.java.dictionary;

import java.io.File;

import main.java.performance.PerformanceTimer;

public class LinearSearch implements Search {
  public int searchWord(ArrayList<String[]> dictionary, String target) {
    for (int i = 0; i < dictionary.size(); i++) {
      String[] entry = dictionary.get(i);
      if (entry.length > 0 && entry[0].equals(target)) {
        return i;
      }
    }
    return -1;
  }
}
