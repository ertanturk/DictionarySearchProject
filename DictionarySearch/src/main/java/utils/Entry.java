package main.java.utils;

public class Entry<K extends Comparable<K>, V extends Comparable<V>> {
  private final K key;
  private V value;
  private Entry<K, V> next;

  public Entry(K key, V value) {
    this.key = key;
    this.value = value;
    this.next = null;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public Entry<K, V> getNext() {
    return next;
  }

  public void setNext(Entry<K, V> next) {
    this.next = next;
  }

  public void setValue(V value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "(" + key + ", " + value + ")";
  }
}
