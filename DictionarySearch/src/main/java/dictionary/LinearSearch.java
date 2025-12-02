package main.java.dictionary;

import java.io.File;

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

  public static void main(String[] args) {
    ArrayList<String[]> dictionary = new ArrayList<>();
    File dictionaryFile = new File("DictionarySearch/data/dict.csv");
    Loader<String[]> loader = new Loader<>(dictionaryFile, dictionary);
    loader.load();

  }
}
