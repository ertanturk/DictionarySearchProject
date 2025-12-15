package main.java.utils.analysis;

public class ExecutionTimeFormatter {
  private int decimalPlaces = 10;

  public ExecutionTimeFormatter() {
  }

  public ExecutionTimeFormatter(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public String formatNanoseconds(long nanoseconds) {
    double milliseconds = nanoseconds / 1_000_000.0;
    String formatString = "%." + decimalPlaces + "f ms";
    return String.format(formatString, milliseconds);
  }

  public String formatMilliseconds(double milliseconds) {
    double ms = milliseconds / 1_000_000.0;
    String formatString = "%." + decimalPlaces + "f ms";
    return String.format(formatString, ms);
  }

  public String formatSeconds(double seconds) {
    String formatString = "%." + decimalPlaces + "f s";
    return String.format(formatString, seconds);
  }

  public String formatSummary(long executionTime, String task, String word, int isInList) {
    StringBuilder sb = new StringBuilder();
    sb.append("----------------------------------------------------------------\n");
    sb.append("Execution Time for ").append(task);
    if (word != null && !word.isEmpty()) {
      if (isInList >= 0) {
        sb.append(" (Word: '").append(word).append("' exists in the dictionary)");
      } else if (isInList == -1) {
        sb.append(" (Word: '").append(word).append("' not exists in the dictionary)");
      }
    }
    sb.append(": ").append(formatMilliseconds(executionTime));
    sb.append("\n----------------------------------------------------------------\n");
    return sb.toString();
  }

  public String formatComparison(Long[] executionTimes, String[] tasks, String word, int isInList) {
    StringBuilder sb = new StringBuilder();
    sb.append("----------------------------------------------------------------\n");
    sb.append("Execution Time Comparison");
    if (word != null && !word.isEmpty()) {
      if (isInList >= 0) {
        sb.append(" (Word: '").append(word).append("' exists in the dictionary)");
      } else if (isInList == -1) {
        sb.append(" (Word: '").append(word).append("' not exists in the dictionary)");
      }
    }
    sb.append(":\n");
    for (int i = 0; i < tasks.length; i++) {
      sb.append("- ").append(tasks[i]).append(": ").append(formatMilliseconds(executionTimes[i]))
          .append(ratioToFastest(executionTimes, i)).append("\n");
    }
    sb.append("----------------------------------------------------------------\n");
    return sb.toString();
  }

  public String formatAverageComparison(Long[][] executionTimes, String[] tasks, int iterations) {
    Long[] averages = calculateAverages(executionTimes);
    StringBuilder sb = new StringBuilder();
    sb.append("----------------------------------------------------------------\n");
    sb.append("Average Execution Time Comparison for 50 words over ").append(iterations).append(" iterations:\n");
    for (int i = 0; i < tasks.length; i++) {
      sb.append("- ").append(tasks[i]).append(": ").append(formatMilliseconds(averages[i]))
          .append(ratioToFastest(averages, i)).append("\n");
    }
    sb.append("----------------------------------------------------------------\n");
    return sb.toString();
  }

  private Long[] calculateAverages(Long[][] executionTimes) {
    Long[] averages = new Long[executionTimes[0].length];
    long linearSearch = 0;
    long binarySearch = 0;
    long hashSearch = 0;

    for (int i = 0; i < executionTimes.length; i++) {
      linearSearch += executionTimes[i][0];
      binarySearch += executionTimes[i][1];
      hashSearch += executionTimes[i][2];
    }

    averages[0] = linearSearch / (executionTimes.length);
    averages[1] = binarySearch / (executionTimes.length);
    averages[2] = hashSearch / (executionTimes.length);
    return averages;
  }

  private String ratioToFastest(Long[] executionTimes, int index) {
    long fastest = Long.MAX_VALUE;
    for (long time : executionTimes) {
      if (time < fastest) {
        fastest = time;
      }
    }
    if (executionTimes[index] == fastest) {
      return " (Fastest)";
    } else {
      double ratio = (double) executionTimes[index] / fastest;
      String formatString = " (%.3fx Times Slower)";
      return String.format(formatString, ratio);
    }
  }
}
