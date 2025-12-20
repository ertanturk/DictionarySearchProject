package main.java.tests;

import main.java.loader.Loader;
import main.java.search.BinarySearch;
import main.java.search.HashSearch;
import main.java.search.LinearSearch;
import main.java.utils.HashTable;
import main.java.utils.analysis.ExecutionTimeAnalyzer;
import main.java.utils.analysis.ExecutionTimeFormatter;

public class Test {
  private static volatile int SINK = 0;
  private final static int WARMUP_RUNS = 10;
  private final static int ITERATIONS = 10000;

  public static void main(String[] args) throws Exception {
    // Loaders
    Loader dicLoader = new Loader("DictionarySearch/data/dict.csv");
    Loader testWLoader = new Loader("DictionarySearch/data/testWords.txt");

    // HashTables
    HashTable<String, String> dictionary = dicLoader.load();
    System.out.println("Dictionary loaded with " + dictionary.size() + " entries.");
    HashTable<String, String> testWords = testWLoader.load();
    System.out.println("Test words loaded with " + testWords.size() + " entries.");

    // Get keys from testWords
    Object[][] testKeys = testWords.getKeyValuePairs();
    System.out.println("Testing search for " + testKeys.length + " words...");

    // Get keys from dictionary
    Object[][] dictKeys = dictionary.getKeyValuePairs();
    System.out.println("Dictionary contains " + dictKeys.length + " unique words.");

    // Searchs
    BinarySearch binarySearch = new BinarySearch();
    LinearSearch linearSearch = new LinearSearch();
    HashSearch hashSearch = new HashSearch(dictionary);

    // Sort dictionary keys for binary search
    binarySearch.sort(dictKeys, 0, dictKeys.length - 1);
    System.out.println("Dictionary keys sorted for binary search.");

    // Analyzer & Formatter
    ExecutionTimeAnalyzer analyzer = new ExecutionTimeAnalyzer();
    ExecutionTimeFormatter formatter = new ExecutionTimeFormatter();
    int isInList = -1;
    Long[][] executionTimes = new Long[testKeys.length][3];

    // Perform searches and analyze execution times
    long linearTime, binaryTime, hashTime;
    for (int i = 0; i < testKeys.length; i++) {
      String key = testKeys[i][0].toString().toLowerCase();
      // Check existence in dictionary
      isInList = binarySearch.search(dictKeys, key);

      // Alternate the order of searches to minimize caching effects
      if ((i & 1) == 0) {
        // Linear Search
        linearTime = analyzer.runRepeated(() -> {
          SINK = SINK * 31 + linearSearch.search(dictKeys, key);
        }, WARMUP_RUNS, ITERATIONS);

        // Binary Search
        binaryTime = analyzer.runRepeated(() -> {
          SINK = SINK * 31 + binarySearch.search(dictKeys, key);
        }, WARMUP_RUNS, ITERATIONS);

        // Hash Search
        hashTime = analyzer.runRepeated(() -> {
          SINK = SINK * 31 + hashSearch.search(dictKeys, key);
        }, WARMUP_RUNS, ITERATIONS);
      } else {
        // Hash Search
        hashTime = analyzer.runRepeated(() -> {
          SINK = SINK * 31 + hashSearch.search(dictKeys, key);
        }, WARMUP_RUNS, ITERATIONS);

        // Binary Search
        binaryTime = analyzer.runRepeated(() -> {
          SINK = SINK * 31 + binarySearch.search(dictKeys, key);
        }, WARMUP_RUNS, ITERATIONS);

        // Linear Search
        linearTime = analyzer.runRepeated(() -> {
          SINK = SINK * 31 + linearSearch.search(dictKeys, key);
        }, WARMUP_RUNS, ITERATIONS);
      }

      executionTimes[i][0] = linearTime;
      executionTimes[i][1] = binaryTime;
      executionTimes[i][2] = hashTime;

      // Format and print results
      String output = formatter.formatComparison(new Long[] { linearTime, binaryTime, hashTime },
          new String[] { "Linear Search", "Binary Search", "Hash Search" }, key, isInList);

      System.out.println("\nTest case " + (i + 1) + "/" + testKeys.length + ":");
      System.out.println(output);
    }
    String averageOutput = formatter.formatAverageComparison(executionTimes,
        new String[] { "Linear Search", "Binary Search", "Hash Search" }, ITERATIONS);
    System.out.println(averageOutput);
    System.out.println("Final SINK value to prevent optimization: " + SINK);
  }
}
