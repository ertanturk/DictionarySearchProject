package main.java.dictionary.hash;

public class HashTable<Key, Value> {
  private Node<Key, Value>[] table;
  private int capacity;
  private int size;
  private static final double LOAD_FACTOR_THRESHOLD = 0.75;
  private static final int INITIAL_CAPACITY = 16;

  @SuppressWarnings("unchecked")
  HashTable() {
    this.capacity = INITIAL_CAPACITY;
    this.size = 0;
    this.table = new Node[capacity];
  }

  @SuppressWarnings("unchecked")
  HashTable(int initialCapacity) {
    this.capacity = initialCapacity;
    this.size = 0;
    this.table = new Node[capacity];
  }

  public void put(Key key, Value value) {
    if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
      resize();
    }

    int index = hash(key);
    Node<Key, Value> current = table[index];

    while (current != null) {
      if (current.getKey().equals(key)) {
        current.setValue(value);
        return;
      }
      current = current.getNext();
    }

    Node<Key, Value> newNode = new Node<>(key, value);
    newNode.setNext(table[index]);
    table[index] = newNode;
    size++;
  }

  public Value get(Key key) {
    int index = hash(key);
    Node<Key, Value> current = table[index];

    while (current != null) {
      if (current.getKey().equals(key)) {
        return current.getValue();
      }
      current = current.getNext();
    }

    return null;
  }

  public void remove(Key key) {
    int index = hash(key);
    Node<Key, Value> current = table[index];
    Node<Key, Value> prev = null;

    while (current != null) {
      if (current.getKey().equals(key)) {
        if (prev == null) {
          table[index] = current.getNext();
        } else {
          prev.setNext(current.getNext());
        }
        size--;
        return;
      }
      prev = current;
      current = current.getNext();
    }
  }

  public boolean containsKey(Key key) {
    int index = hash(key);
    Node<Key, Value> current = table[index];

    while (current != null) {
      if (current.getKey().equals(key)) {
        return true;
      }
      current = current.getNext();
    }

    return false;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  private int hash(Key key) {
    String word = key.toString();
    long M = 100003;
    long hashValue = 0;

    for (int i = 0; i < word.length(); i++) {
      hashValue = (hashValue * 31 + word.charAt(i)) % M;
    }

    return (int) (hashValue % capacity);
  }

  private void resize() {
    int newCapacity = this.capacity * 2;
    @SuppressWarnings("unchecked")
    Node<Key, Value>[] newTable = new Node[newCapacity];
    for (int i = 0; i < capacity; i++) {
      Node<Key, Value> current = table[i];
      while (current != null) {
        int newIndex = this.hash(current.getKey()) % newCapacity;
        Node<Key, Value> nextNode = current.getNext();
        current.setNext(newTable[newIndex]);
        newTable[newIndex] = current;
        current = nextNode;
      }
    }
    this.table = newTable;
    this.capacity = newCapacity;
  }
}
