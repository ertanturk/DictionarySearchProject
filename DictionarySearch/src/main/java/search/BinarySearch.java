package main.java.search;

public class BinarySearch implements Search {
  @Override
  public int search(Object[][] sortedArray, Object target) {
    int left = 0;
    int right = sortedArray.length - 1;

    while (left <= right) {
      int mid = left + (right - left) / 2;
      int comparison = compareFirstValue(sortedArray[mid], target);

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

  public void sort(Object[][] array, int left, int right) {
    if (array == null || array.length == 0) {
      return;
    }

    if (left >= right) {
      return;
    }

    // Find pivot's first element
    Object pivotVal = null;
    Object[] pivotRow = array[left + (right - left) / 2];
    if (pivotRow != null && pivotRow.length > 0) {
      pivotVal = pivotRow[0];
    }

    int i = left;
    int j = right;
    while (i <= j) {
      while (i <= right && compareFirstValue(array[i], pivotVal) < 0) {
        i++;
      }
      while (j >= left && compareFirstValue(array[j], pivotVal) > 0) {
        j--;
      }
      if (i <= j) {
        Object[] temp = array[i];
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

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private int compareFirstValue(Object[] row, Object pivotVal) {
    Object val = (row != null && row.length > 0) ? row[0] : null;
    if (val == null && pivotVal == null)
      return 0;
    if (val == null)
      return -1;
    if (pivotVal == null)
      return 1;
    if (val instanceof Comparable && pivotVal instanceof Comparable) {
      return ((Comparable) val).compareTo(pivotVal);
    }
    // Fallback to string comparison
    return val.toString().compareTo(pivotVal.toString());
  }
}
