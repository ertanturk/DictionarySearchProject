package main.java.utils;

import java.lang.reflect.Array;

public class HashTable<Key extends Comparable<Key>, Value extends Comparable<Value>> {
  private Entry<Key, Value>[] table;
  private int capacity = 16;
  private int size;

  @SuppressWarnings("unchecked")
  public HashTable() {
    this.table = new Entry[this.capacity];
    this.size = 0;
  }

  @SuppressWarnings("unchecked")
  public HashTable(int capacity) {
    this.capacity = capacity;
    this.table = new Entry[this.capacity];
    this.size = 0;
  }

  public void put(Entry<Key, Value> entry) {
    ensureCapacity();
    int index = hash(entry.getKey());
    Entry<Key, Value> current = this.table[index];

    if (current == null) {
      this.table[index] = entry;
    } else {
      Entry<Key, Value> prev = null;
      while (current != null) {
        if (current.getKey().equals(entry.getKey())) {
          current.setValue(entry.getValue());
          return;
        }
        prev = current;
        current = current.getNext();
      }
      prev.setNext(entry);
    }
    this.size++;
  }

  public Value get(Key key) {
    int index = hash(key);
    Entry<Key, Value> current = this.table[index];
    while (current != null) {
      if (current.getKey().equals(key)) {
        return current.getValue();
      }
      current = current.getNext();
    }
    return null;
  }

  public Value remove(Key key) {
    int index = hash(key);
    Entry<Key, Value> current = this.table[index];
    Entry<Key, Value> prev = null;

    while (current != null) {
      if (current.getKey().equals(key)) {
        if (prev == null) {
          this.table[index] = current.getNext();
        } else {
          prev.setNext(current.getNext());
        }
        this.size--;
        return current.getValue();
      }
      prev = current;
      current = current.getNext();
    }
    return null;
  }

  public boolean containsKey(Key key) {
    int index = hash(key);
    Entry<Key, Value> current = this.table[index];
    while (current != null) {
      if (current.getKey().equals(key)) {
        return true;
      }
      current = current.getNext();
    }
    return false;
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  private int hash(Key key) {
    final long M = 100_003L;
    String keyStr = key.toString();
    long hash = 0L;

    for (int i = 0; i < keyStr.length(); i++) {
      int wi = keyStr.charAt(i);
      hash = (hash * 31 + wi) % M;
    }

    return (int) (hash % this.capacity);
  }

  @SuppressWarnings("unchecked")
  private void ensureCapacity() {
    if (this.size >= this.capacity) {
      int newCapacity = this.capacity * 2;
      Entry<Key, Value>[] newTable = new Entry[newCapacity];

      for (int i = 0; i < this.capacity; i++) {
        Entry<Key, Value> currentEntry = this.table[i];
        while (currentEntry != null) {
          Entry<Key, Value> nextEntry = currentEntry.getNext();

          int oldCapacity = this.capacity;
          this.capacity = newCapacity;
          int newIndex = hash(currentEntry.getKey());
          this.capacity = oldCapacity;

          currentEntry.setNext(newTable[newIndex]);
          newTable[newIndex] = currentEntry;
          currentEntry = nextEntry;
        }
      }

      this.table = newTable;
      this.capacity = newCapacity;
    }
  }

  public Key[] getKeys() {
    Class<?> keyClass = null;
    for (int i = 0; i < this.capacity; i++) {
      if (this.table[i] != null) {
        keyClass = this.table[i].getKey().getClass();
        break;
      }
    }

    if (keyClass == null) {
      @SuppressWarnings("unchecked")
      Key[] empty = (Key[]) new Comparable[0];
      return empty;
    }

    @SuppressWarnings("unchecked")
    Key[] keys = (Key[]) Array.newInstance(keyClass, this.size);
    int index = 0;
    for (int i = 0; i < this.capacity; i++) {
      Entry<Key, Value> current = this.table[i];
      while (current != null) {
        keys[index++] = current.getKey();
        current = current.getNext();
      }
    }
    return keys;
  }

  @Override
  public String toString() {
    StringBuilder tableString = new StringBuilder();
    tableString.append("{");
    for (int i = 0; i < this.capacity; i++) {
      Entry<Key, Value> current = this.table[i];
      if (current != null) {
        tableString.append("\n  [").append(i).append("]: ");
        while (current != null) {
          tableString.append("(").append(current.getKey()).append(", ").append(current.getValue()).append(") -> ");
          current = current.getNext();
        }
        tableString.append("null");
      }
    }
    tableString.append("\n}");
    return tableString.toString();
  }
}
