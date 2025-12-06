package main.java.search;

public class LinearSearch<T extends Comparable<T>> implements Search<T> {
  @Override
  public int search(T[] array, T target) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].compareTo(target) == 0) {
        return i;
      }
    }
    return -1;
  }
}
