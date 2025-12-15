package main.java.utils.analysis;

public class ExecutionTimeFormatter {
  private int decimalPlaces = 3;

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
    String formatString = "%." + decimalPlaces + "f ms";
    return String.format(formatString, milliseconds);
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
