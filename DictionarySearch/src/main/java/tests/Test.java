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
      Long[][] timeLongs = new Long[testKeys.length][3];
      binarySearch.sort(dicKeys, 0, dicKeys.length - 1);
      for (int i = 0; i < testKeys.length; i++) {
        String key = testKeys[i];
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

        timeLongs[i][0] = linearSearchTime;
        timeLongs[i][1] = binarySearchTime;
        timeLongs[i][2] = hashSearchTime;
      }
      Long linearTotal = 0L;
      Long binaryTotal = 0L;
      Long hashTotal = 0L;
      for (int i = 0; i < timeLongs.length; i++) {
        linearTotal += timeLongs[i][0];
        binaryTotal += timeLongs[i][1];
        hashTotal += timeLongs[i][2];
      }

      linearTotal /= timeLongs.length;
      binaryTotal /= timeLongs.length;
      hashTotal /= timeLongs.length;
      System.out.println("Overall Average Times for Searches: (50 test words) (ns)");
      System.out.println(formatter.formatComparison(
          new String[] { "Linear Search", "Binary Search", "Hash Search" },
          new Long[] {
              linearTotal,
              binaryTotal,
              hashTotal
          }));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
