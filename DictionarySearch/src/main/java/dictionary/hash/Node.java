package main.java.dictionary.hash;

public class Node<Key, Value> {
  private Key key;
  private Value value;
  private Node<Key, Value> next;

  public Node(Key key, Value value) {
    this.key = key;
    this.value = value;
    this.next = null;
  }

  public Key getKey() {
    return key;
  }

  public Value getValue() {
    return value;
  }

  public Node<Key, Value> getNext() {
    return next;
  }

  public void setValue(Value value) {
    this.value = value;
  }

  public void setNext(Node<Key, Value> next) {
    this.next = next;
  }

}
