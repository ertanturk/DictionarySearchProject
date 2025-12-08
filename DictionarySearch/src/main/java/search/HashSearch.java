package main.java.search;

import main.java.utils.HashTable;

public class HashSearch<T extends Comparable<T>> implements Search<T> {
  private HashTable<T, String> hashTable;

  public HashSearch(HashTable<T, String> hashTable) {
    this.hashTable = hashTable;
  }

  @Override
  public int search(T[] array, T target) {
    String result = hashTable.get(target);
    return result != null ? 1 : -1;
  }

  public String searchInHashTable(T key) {
    return hashTable.get(key);
  }
}
