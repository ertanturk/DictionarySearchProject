package main.java.search;

public interface Search<T extends Comparable<T>> {
  int search(T[] array, T target);
}
