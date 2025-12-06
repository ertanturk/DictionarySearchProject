package main.java.utils;

public class ArrayList<Key extends Comparable<Key>, Value extends Comparable<Value>> {
  private Entry<Key, Value>[] entries;
  private int size;
  private static final int INITIAL_CAPACITY = 16;

  @SuppressWarnings({ "unchecked", "static-access" })
  public ArrayList() {
    this.entries = new Entry[this.INITIAL_CAPACITY];
    this.size = -1;
  }

  @SuppressWarnings("unchecked")
  public ArrayList(int capacity) {
    this.entries = new Entry[capacity];
    this.size = -1;
  }

  // Adds the entry to the end of the list
  public void add(Entry<Key, Value> entry) {
    ensureCapacity();
    this.entries[++this.size] = entry;
  }

  public void add(Entry<Key, Value> entry, int index) {
    ensureCapacity();
    checkIndex(index);
    for (int i = this.size; i >= index; i--) { // Shift right
      this.entries[i] = this.entries[i - 1];
    }
    this.entries[index] = entry;
    this.size++;
  }

  public Entry<Key, Value> get(int index) {
    checkIndex(index);
    return this.entries[index];
  }

  public void set(int index, Entry<Key, Value> entry) {
    checkIndex(index);
    this.entries[index] = entry;
  }

  public Entry<Key, Value> remove(int index) {
    checkIndex(index);
    Entry<Key, Value> removedEntry = this.entries[index];
    for (int i = index; i < this.size; i++) { // Shift left
      this.entries[i] = this.entries[i + 1];
    }
    this.entries[this.size] = null; // Clear last entry
    this.size--;
    return removedEntry;
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size == -1;
  }

  @SuppressWarnings({ "unchecked", "static-access" })
  public void clear() {
    this.entries = new Entry[this.INITIAL_CAPACITY];
    this.size = -1;
  }

  private void ensureCapacity() {
    if (this.size + 1 == this.entries.length) {
      @SuppressWarnings("unchecked")
      Entry<Key, Value>[] newEntries = new Entry[this.entries.length * 2];
      System.arraycopy(this.entries, 0, newEntries, 0, this.entries.length);
      this.entries = newEntries;
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index > this.size + 1) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + (this.size + 1));
    }
  }

  @Override
  public String toString() {
    StringBuilder entriesString = new StringBuilder();
    entriesString.append("[");
    for (int i = 0; i <= this.size; i++) {
      Entry<Key, Value> entry = this.entries[i];
      if (i < this.size) {
        entriesString.append("(").append(entry.getKey()).append(", ").append(entry.getValue()).append("), ");
      } else {
        entriesString.append("(").append(entry.getKey()).append(", ").append(entry.getValue()).append(")");
      }
    }
    entriesString.append("]");
    return entriesString.toString();
  }
}
