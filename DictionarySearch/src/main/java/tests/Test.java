package main.java.tests;

import main.java.loader.Loader;
import main.java.search.BinarySearch;
import main.java.search.HashSearch;
import main.java.search.LinearSearch;
import main.java.utils.HashTable;
import main.java.utils.analysis.ExecutionTimeAnalyzer;
import main.java.utils.analysis.ExecutionTimeFormatter;

public class Test {
  public static void main(String[] args) {
    Loader<String, String> txtLoader = new Loader<>("DictionarySearch/data/testWords.txt");
    Loader<String, String> dicLoader = new Loader<>("DictionarySearch/data/dict.csv");
    HashTable<String, String> testWordsTable;
    HashTable<String, String> dicTable;
    ExecutionTimeAnalyzer analyzer = new ExecutionTimeAnalyzer();
    ExecutionTimeFormatter formatter = new ExecutionTimeFormatter();
    BinarySearch<String> binarySearch = new BinarySearch<>();
    HashSearch<String> hashSearch;
    LinearSearch<String> linearSearch = new LinearSearch<>();
    try {
      testWordsTable = txtLoader.load();
      dicTable = dicLoader.load();
      hashSearch = new HashSearch<>(dicTable);
      String[] testKeys = testWordsTable.getKeys();
      String[] dicKeys = dicTable.getKeys();
      binarySearch.sort(dicKeys, 0, dicKeys.length - 1);
      for (String key : testKeys) {
        // Linear Search Test
        long linearSearchTime = analyzer.run(() -> {
          linearSearch.search(dicKeys, key);
        });

        // Binary Search Test
        long binarySearchTime = analyzer.run(() -> {
          binarySearch.search(dicKeys, key);
        });

        // Hash Search Test
        long hashSearchTime = analyzer.run(() -> {
          hashSearch.search(dicKeys, key);
        });

        System.out.println("Key: " + key);
        System.out.println(formatter.formatComparison(
            new String[] { "Linear Search", "Binary Search", "Hash Search" },
            new Long[] {
                linearSearchTime,
                binarySearchTime,
                hashSearchTime
            }));

      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
