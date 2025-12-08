package main.java.search;

public class BinarySearch<T extends Comparable<T>> implements Search<T> {
  @Override
  public int search(T[] sortedArray, T target) {
    int left = 0;
    int right = sortedArray.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;
      int comparison = sortedArray[mid].compareTo(target);

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

  public void sort(T[] array, int left, int right) {
    if (array == null || array.length == 0) {
      return;
    }

    if (left >= right) {
      return;
    }

    T pivot = array[left + (right - left) / 2];
    int i = left;
    int j = right;
    while (i <= j) {
      while (array[i].compareTo(pivot) < 0) {
        i++;
      }
      while (array[j].compareTo(pivot) > 0) {
        j--;
      }
      if (i <= j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        i++;
        j--;
      }
    }
    if (left < j) {
      sort(array, left, j);
    }

    if (right > i) {
      sort(array, i, right);
    }
  }
}
