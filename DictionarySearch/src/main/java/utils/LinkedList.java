package main.java.utils;

public class LinkedList<Key extends Comparable<Key>, Value extends Comparable<Value>> {
  private Entry<Key, Value> head;
  private Entry<Key, Value> tail;
  private int size;

  public LinkedList() {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  public void addFirst(Entry<Key, Value> entry) {
    if (this.size == 0) {
      this.head = entry;
      this.tail = entry;
    } else {
      Entry<Key, Value> oldHead = this.head;
      this.head = entry;
      this.head.setNext(oldHead);
    }
    this.size++;
  }

  public void addLast(Entry<Key, Value> entry) {
    if (this.size == 0) {
      this.head = entry;
      this.tail = entry;
    } else {
      this.tail.setNext(entry);
      this.tail = entry;
    }
    this.size++;
  }

  public void add(int index, Entry<Key, Value> entry) {
    checkIndex(index);
    int currentIndex = 0;
    Entry<Key, Value> current = this.head;
    Entry<Key, Value> previous = null;
    while (currentIndex < index) {
      previous = current;
      current = current.getNext();
      currentIndex++;
    }
    if (previous != null) {
      previous.setNext(entry);
    } else {
      this.head = entry;
    }
    entry.setNext(current);
    this.size++;
  }

  public Entry<Key, Value> removeFirst() {
    if (this.size == 0) {
      return null;
    }
    Entry<Key, Value> removedEntry = this.head;
    this.head = this.head.getNext();
    this.size--;
    if (this.size == 0) {
      this.tail = null;
    }
    return removedEntry;
  }

  public Entry<Key, Value> removeLast() {
    if (this.size == 0) {
      return null;
    }
    Entry<Key, Value> removedEntry = this.tail;
    if (this.size == 1) {
      this.head = null;
      this.tail = null;
    } else {
      Entry<Key, Value> current = this.head;
      while (current.getNext() != this.tail) {
        current = current.getNext();
      }
      current.setNext(null);
      this.tail = current;
    }
    this.size--;
    return removedEntry;
  }

  public Entry<Key, Value> remove(int index) {
    checkIndex(index);
    if (index == 0) {
      return removeFirst();
    }
    if (index == this.size - 1) {
      return removeLast();
    }
    int currentIndex = 0;
    Entry<Key, Value> current = this.head;
    Entry<Key, Value> previous = null;
    while (currentIndex < index) {
      previous = current;
      current = current.getNext();
      currentIndex++;
    }
    previous.setNext(current.getNext());
    this.size--;
    return current;
  }

  public Entry<Key, Value> get(int index) {
    checkIndex(index);
    int currentIndex = 0;
    Entry<Key, Value> current = this.head;
    while (currentIndex < index) {
      current = current.getNext();
      currentIndex++;
    }
    return current;
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
  }

  @Override
  public String toString() {
    StringBuilder entriesString = new StringBuilder();
    entriesString.append("[");
    Entry<Key, Value> current = this.head;
    while (current != null) {
      entriesString.append("(").append(current.getKey()).append(", ").append(current.getValue()).append(")");
      current = current.getNext();
      if (current != null) {
        entriesString.append(", ");
      }
    }
    entriesString.append("]");
    return entriesString.toString();
  }
}
