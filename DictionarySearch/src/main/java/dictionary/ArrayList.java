package main.java.dictionary;

public class ArrayList<T> implements Iterable<T> {
  private T[] array;
  private int size;

  @SuppressWarnings("unchecked")
  public ArrayList() {
    array = (T[]) new Object[16];
  }

  // Getters
  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  // Methods
  public void add(T element) {
    ensureCapacity(size + 1);
    array[size++] = element;
  }

  public void addAt(int index, T element) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    ensureCapacity(size + 1);
    for (int i = size; i > index; i--) {
      array[i] = array[i - 1];
    }
    array[index] = element;
    size++;
  }

  public T get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    return array[index];
  }

  public void set(int index, T element) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    array[index] = element;
  }

  public void remove(T element) {
    for (int i = 0; i < size; i++) {
      if (array[i].equals(element)) {
        removeAt(i);
        return;
      }
    }
  }

  public void removeAt(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    for (int i = index; i < size - 1; i++) {
      array[i] = array[i + 1];
    }
    array[--size] = null;
  }

  public void clear() {
    for (int i = 0; i < size; i++) {
      array[i] = null;
    }
    size = 0;
  }

  @SuppressWarnings("unchecked")
  public int compareTo(T element1, T element2) {
    if (!(element1 instanceof Comparable)) {
      throw new IllegalArgumentException("Elements must be comparable");
    }
    return ((Comparable<T>) element1).compareTo(element2);
  }

  public String toString() {
    String list = "";
    for (int i = 0; i < size; i++) {
      if (i == size - 1) {
        list += array[i];
      } else {
        list += array[i] + ", ";
      }
    }
    return "[" + list + "]";
  }

  public T[] toArray() {
    @SuppressWarnings("unchecked")
    T[] newArray = (T[]) new Object[size];
    for (int i = 0; i < size; i++) {
      newArray[i] = array[i];
    }
    return newArray;
  }

  @Override
  public java.util.Iterator<T> iterator() {
    return new java.util.Iterator<T>() {
      private int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < size;
      }

      @Override
      public T next() {
        if (!hasNext()) {
          throw new java.util.NoSuchElementException();
        }
        return array[currentIndex++];
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("remove not supported");
      }
    };
  }

  @SuppressWarnings("unchecked")
  public void ensureCapacity(int minCapacity) {
    if (array.length >= minCapacity) {
      return;
    }
    int newCapacity = array.length;
    while (newCapacity < minCapacity) {
      newCapacity *= 2;
    }
    T[] newArray = (T[]) new Object[newCapacity];
    for (int i = 0; i < size; i++) {
      newArray[i] = array[i];
    }
    array = newArray;
  }
}
