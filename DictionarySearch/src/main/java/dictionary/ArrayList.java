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

  // Custom static method to sort dictionary with quicksort
  public static void quicksort(ArrayList<String[]> list, int low, int high) {
    if (low < high) {
      int pivotIndex = partition(list, low, high);
      quicksort(list, low, pivotIndex - 1);
      quicksort(list, pivotIndex + 1, high);
    }
  }

  private static int partition(ArrayList<String[]> list, int low, int high) {
    String[] pivot = list.get(high);
    int i = low - 1;

    for (int j = low; j < high; j++) {
      String[] current = list.get(j);
      if (current.length > 0 && pivot.length > 0 && current[0].compareTo(pivot[0]) <= 0) {
        i++;
        swap(list, i, j);
      }
    }
    swap(list, i + 1, high);
    return i + 1;
  }

  private static void swap(ArrayList<String[]> list, int i, int j) {
    String[] temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
  }

  // Custom static method to linear search dictionary
  public static int linearSearch(ArrayList<String[]> list, String target) {
    for (int i = 0; i < list.size(); i++) {
      String[] entry = list.get(i);
      if (entry.length > 0 && entry[0].equals(target)) {
        return i;
      }
    }
    return -1;
  }

  // Custom static method to binary search dictionary
  public static int binarySearch(ArrayList<String[]> list, String target) {
    int left = 0;
    int right = list.size() - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;
      String[] midEntry = list.get(mid);
      if (midEntry.length == 0) {
        left = mid + 1;
        continue;
      }
      int cmp = midEntry[0].compareTo(target);
      if (cmp == 0) {
        return mid;
      } else if (cmp < 0) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    return -1;
  }
}
