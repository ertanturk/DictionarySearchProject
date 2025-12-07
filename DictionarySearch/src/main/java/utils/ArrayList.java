package main.java.utils;

public class ArrayList<T extends Comparable<T>> {
  private T[] elements;
  private int size;
  private static final int INITIAL_CAPACITY = 16;

  @SuppressWarnings("unchecked")
  public ArrayList() {
    this.elements = (T[]) new Comparable[INITIAL_CAPACITY];
    this.size = 0;
  }

  @SuppressWarnings("unchecked")
  public ArrayList(int capacity) {
    this.elements = (T[]) new Comparable[capacity];
    this.size = 0;
  }

  public void add(T element) {
    ensureCapacity();
    this.elements[this.size++] = element;
  }

  public void add(T element, int index) {
    ensureCapacity();
    checkIndex(index);
    for (int i = this.size; i > index; i--) {
      this.elements[i] = this.elements[i - 1];
    }
    this.elements[index] = element;
    this.size++;
  }

  public T get(int index) {
    if (index < 0 || index >= this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
    return this.elements[index];
  }

  public void set(int index, T element) {
    if (index < 0 || index >= this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
    this.elements[index] = element;
  }

  public T remove(int index) {
    if (index < 0 || index >= this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
    T removedElement = this.elements[index];
    for (int i = index; i < this.size - 1; i++) {
      this.elements[i] = this.elements[i + 1];
    }
    this.elements[this.size - 1] = null;
    this.size--;
    return removedElement;
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  @SuppressWarnings("unchecked")
  public void clear() {
    this.elements = (T[]) new Comparable[INITIAL_CAPACITY];
    this.size = 0;
  }

  private void ensureCapacity() {
    if (this.size == this.elements.length) {
      @SuppressWarnings("unchecked")
      T[] newElements = (T[]) new Comparable[this.elements.length * 2];
      System.arraycopy(this.elements, 0, newElements, 0, this.elements.length);
      this.elements = newElements;
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index > this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
  }

  @Override
  public String toString() {
    StringBuilder elementsString = new StringBuilder();
    elementsString.append("[");
    for (int i = 0; i < this.size; i++) {
      elementsString.append(this.elements[i]);
      if (i < this.size - 1) {
        elementsString.append(", ");
      }
    }
    elementsString.append("]");
    return elementsString.toString();
  }
}
