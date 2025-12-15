package main.java.tests;

import main.java.loader.Loader;
import main.java.search.BinarySearch;
import main.java.search.HashSearch;
import main.java.search.LinearSearch;
import main.java.utils.HashTable;
import main.java.utils.analysis.ExecutionTimeAnalyzer;
import main.java.utils.analysis.ExecutionTimeFormatter;

public class Test {
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
    String[] testKeys = testWords.getKeys();
    System.out.println("Testing search for " + testKeys.length + " words...");

    // Get keys from dictionary
    String[] dictKeys = dictionary.getKeys();
    System.out.println("Dictionary contains " + dictKeys.length + " unique words.");

    // Searchs
    BinarySearch<String> binarySearch = new BinarySearch<>();
    LinearSearch<String> linearSearch = new LinearSearch<>();
    HashSearch<String> hashSearch = new HashSearch<>(dictionary);

    // Sort dictionary keys for binary search
    binarySearch.sort(dictKeys, 0, dictKeys.length - 1);
    System.out.println("Dictionary keys sorted for binary search.");

    // Analyzer & Formatter
    ExecutionTimeAnalyzer analyzer = new ExecutionTimeAnalyzer();
    ExecutionTimeFormatter formatter = new ExecutionTimeFormatter();
    int isInList = -1;

    // Perform searches and analyze execution times
    for (String key : testKeys) {

      // Check existence in dictionary
      isInList = hashSearch.searchInHashTable(key) != null ? 1 : -1;

      // Linear Search
      long linearTime = analyzer.run(() -> {
        linearSearch.search(dictKeys, key);
      });

      // Binary Search
      long binaryTime = analyzer.run(() -> {
        binarySearch.search(dictKeys, key);
      });

      // Hash Search
      long hashTime = analyzer.run(() -> {
        hashSearch.search(dictKeys, key);
      });

      // Format and print results
      String output = formatter.formatComparison(new Long[] { linearTime, binaryTime, hashTime },
          new String[] { "Linear Search", "Binary Search", "Hash Search" }, key, isInList);
      System.out.println(output);
    }
  }
}
