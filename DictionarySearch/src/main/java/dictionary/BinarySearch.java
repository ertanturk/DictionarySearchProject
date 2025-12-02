package main.java.dictionary;

public class BinarySearch implements Search {
  public int searchWord(ArrayList<String[]> dictionary, String target) {
    if (dictionary == null || dictionary.size() == 0) {
      return -1;
    }
    quickSort(dictionary, 0, dictionary.size() - 1);

    int leftIndex = 0;
    int rightIndex = dictionary.size() - 1;
    while (leftIndex <= rightIndex) {
      int midIndex = leftIndex + (rightIndex - leftIndex) / 2;
      String midWord = dictionary.get(midIndex)[0];
      if (midWord.equals(target)) {
        return midIndex;
      } else if (midWord.compareTo(target) < 0) {
        leftIndex = midIndex + 1;
      } else {
        rightIndex = midIndex - 1;
      }
    }
    return -1;
  }

  private void quickSort(ArrayList<String[]> dictionary, int leftIndex, int rightIndex) {
    if (leftIndex >= rightIndex) {
      return;
    }

    int pivotIndex = partition(dictionary, leftIndex, rightIndex);
    quickSort(dictionary, leftIndex, pivotIndex);
    quickSort(dictionary, pivotIndex + 1, rightIndex);
  }

  private int partition(ArrayList<String[]> dictionary, int leftIndex, int rightIndex) {
    String pivotWord = dictionary.get(leftIndex + (rightIndex - leftIndex) / 2)[0];
    int leftPointer = leftIndex;
    int rightPointer = rightIndex;

    while (true) {
      while (dictionary.get(leftPointer)[0].compareTo(pivotWord) < 0) {
        leftPointer++;
      }
      while (dictionary.get(rightPointer)[0].compareTo(pivotWord) > 0) {
        rightPointer--;
      }
      if (leftPointer >= rightPointer) {
        return rightPointer;
      }
      swap(dictionary, leftPointer, rightPointer);
      leftPointer++;
      rightPointer--;
    }
  }

  private void swap(ArrayList<String[]> dictionary, int i, int j) {
    String[] temp = dictionary.get(i);
    dictionary.set(i, dictionary.get(j));
    dictionary.set(j, temp);
  }
}
