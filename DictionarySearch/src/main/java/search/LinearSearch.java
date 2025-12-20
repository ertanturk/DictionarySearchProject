package main.java.search;

public class LinearSearch implements Search {
  @Override
  public int search(Object[][] array, Object target) {
    for (int i = 0; i < array.length; i++) {
      if (array[i][0].equals(target)) {
        return i;
      }
    }
    return -1;
  }
}
