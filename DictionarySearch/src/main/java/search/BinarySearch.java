package main.java.search;

public class BinarySearch<T extends Comparable<T>> implements Search<T> {
  @Override
  public int search(T[] array, T target) {
    int left = 0;
    int right = array.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;
      int comparison = array[mid].compareTo(target);

      if (comparison == 0) {
        return mid;
      } else if (comparison < 0) {
        left = mid + 1;
      } else {
        right = mid - 1;
      }
    }
    return -1;
  }
}
