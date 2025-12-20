package main.java.search;

import main.java.utils.HashTable;

public class HashSearch implements Search {
  private HashTable<String, String> hashTable;

  public HashSearch(HashTable<String, String> hashTable) {
    this.hashTable = hashTable;
  }

  @Override
  public int search(Object[][] array, Object target) {
    String result = hashTable.get((String) target);
    return result != null ? 1 : -1;
  }

  public String searchInHashTable(Object key) {
    return hashTable.get((String) key);
  }
}
