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

  // Methods
  public void add(T element) {
    if (size == array.length) {
      resize();
    }
    array[size++] = element;
  }

  public void addAt(int index, T element) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    if (size == array.length) {
      resize();
    }
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

  @SuppressWarnings("unchecked")
  private void resize() {
    T[] newArray = (T[]) new Object[array.length * 2];
    for (int i = 0; i < size; i++) {
      newArray[i] = array[i];
    }
    array = newArray;
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

  public void set(int index, T element) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    array[index] = element;
  }

  @SuppressWarnings("unchecked")
  public int compareTo(T element1, T element2) {
    if (!(element1 instanceof Comparable)) {
      throw new IllegalArgumentException("Elements must be comparable");
    }
    return ((Comparable<T>) element1).compareTo(element2);
  }

  public void sort() {
    if (size <= 1) {
      return;
    }

    T pivot = array[size / 2];
    ArrayList<T> less = new ArrayList<>();
    ArrayList<T> equal = new ArrayList<>();
    ArrayList<T> greater = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      int cmp = compareTo(array[i], pivot);
      if (cmp < 0) {
        less.add(array[i]);
      } else if (cmp == 0) {
        equal.add(array[i]);
      } else {
        greater.add(array[i]);
      }
    }

    less.sort();
    greater.sort();

    int index = 0;
    for (int i = 0; i < less.size(); i++) {
      array[index++] = less.get(i);
    }
    for (int i = 0; i < equal.size(); i++) {
      array[index++] = equal.get(i);
    }
    for (int i = 0; i < greater.size(); i++) {
      array[index++] = greater.get(i);
    }

    size = index;
  }

  public void printList() {
    String list = "";
    for (int i = 0; i < size; i++) {
      if (i == size - 1) {
        list += array[i];
      } else {
        list += array[i] + ", ";
      }
    }
    System.out.println("[" + list + "]");
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
}
